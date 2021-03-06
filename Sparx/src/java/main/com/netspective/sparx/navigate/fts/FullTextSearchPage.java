/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.sparx.navigate.fts;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import com.netspective.commons.io.FileSearchPath;
import com.netspective.commons.io.InputSourceLocator;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationPageBodyType;
import com.netspective.sparx.navigate.NavigationTree;

public class FullTextSearchPage extends NavigationPage
{
    private static final Map indexSearchersPool = new HashMap();

    public static final synchronized IndexSearcher getSharedIndexSearcher(File directory) throws IOException
    {
        final String dir = directory.getAbsolutePath();
        IndexSearcher searcher = (IndexSearcher) indexSearchersPool.get(dir);
        if(searcher == null)
        {
            searcher = new IndexSearcher(dir);
            indexSearchersPool.put(dir, searcher);
        }

        return searcher;
    }

    private String activeScrollPageParamName = "scroll-page";
    private boolean valid; // is the search page valid?
    private File indexDirectory;
    private FileSearchPath indexDirectorySearchPath;
    private IndexSearcher indexSearcher;
    private Analyzer analyzer = new StandardAnalyzer();
    private String defaultSearchFieldName;
    private String[] defaultAdvancedSearchFieldNames;
    private SearchHitsRenderer renderer;
    private int maxResultsPerPage = 25;
    private int totalDocsInIndex = 0;
    private String[] allFieldNames;
    private String[] allIndexedFieldNames;
    private Map fieldAttributes = new HashMap();
    private String[] advancedSearchFieldNames;
    private int searchResultsTimeOutDuration = 30000; // 30 seconds of inactivity will close the results automatically

    public FullTextSearchPage(NavigationTree owner)
    {
        super(owner);
        setBodyType(new NavigationPageBodyType(NavigationPageBodyType.CUSTOM_HANDLER));
        getFlags().setFlag(Flags.BODY_AFFECTS_NAVIGATION); // because we can redirect advanced queries
    }

    public FullTextSearchResultsManager getSearchResultsManager()
    {
        return DefaultFullTextSearchResultsManager.getInstance();
    }

    public void finalizeContents()
    {
        super.finalizeContents();

        for(Iterator i = fieldAttributes.values().iterator(); i.hasNext();)
        {
            FieldAttribute fieldAttribute = (FieldAttribute) i.next();
            final Analyzer fieldAnalyzer = fieldAttribute.getAnalyzer();
            if(fieldAnalyzer != null)
            {
                if(!analyzer.getClass().equals(PerFieldAnalyzerWrapper.class))
                    analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer());

                PerFieldAnalyzerWrapper pfaw = (PerFieldAnalyzerWrapper) analyzer;
                pfaw.addAnalyzer(fieldAttribute.getName(), fieldAnalyzer);
            }
        }

        if(indexDirectorySearchPath != null)
        {
            File firstValid = indexDirectorySearchPath.getFirstValidDirectory();
            if(firstValid != null)
            {
                try
                {
                    setIndexDirectory(firstValid);
                }
                catch(IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        if(indexDirectory == null || !indexDirectory.exists() || !indexDirectory.isDirectory())
            getLog().error("Index directory '" + indexDirectory + "' in FullTextSearchPage " + getQualifiedName() + " is not valid. Use index-directory, index-locator-properties, or <index-directory-search-path> to specify a valid index directory location. Search Path: " + indexDirectorySearchPath);
    }

    public FileSearchPath createIndexDirectorySearchPath()
    {
        return new FileSearchPath(this);
    }

    public void addIndexDirectorySearchPath(FileSearchPath searchPath)
    {
        indexDirectorySearchPath = searchPath;
    }

    public File getIndexDirectory()
    {
        return indexDirectory;
    }

    public void setIndexDirectory(File indexDir) throws IOException
    {
        final InputSourceLocator inputSourceLocator = getInputSourceLocator();
        if(!indexDir.isAbsolute() && inputSourceLocator != null)
            indexDir = inputSourceLocator.getRelativeFile(indexDir);

        if(!indexDir.exists() || !indexDir.isDirectory())
            getLog().error("Index directory " + indexDir + " does not exist for FullTextSearchPage " + getQualifiedName());
        else
        {
            try
            {
                readIndexInfo(indexDir);
                indexSearcher = getSharedIndexSearcher(indexDir);
                valid = true;
            }
            catch(Exception e)
            {
                getLog().error(e);
                valid = false;
            }
        }
        this.indexDirectory = indexDir;
    }

    public int getSearchResultsTimeOutDuration()
    {
        return searchResultsTimeOutDuration;
    }

    public void setSearchResultsTimeOutDuration(int searchResultsTimeOutDuration)
    {
        this.searchResultsTimeOutDuration = searchResultsTimeOutDuration;
    }

    public String getActiveScrollPageParamName()
    {
        return activeScrollPageParamName;
    }

    public void setActiveScrollPageParamName(String activeScrollPageParamName)
    {
        this.activeScrollPageParamName = activeScrollPageParamName;
    }

    public String getSortCriteria(NavigationContext nc)
    {
        return null;
    }

    public IndexSearcher getIndexSearcher()
    {
        return indexSearcher;
    }

    public String[] getAllFieldNames()
    {
        return allFieldNames;
    }

    public String[] getAllIndexedFieldNames()
    {
        return allIndexedFieldNames;
    }

    public int getTotalDocsInIndex()
    {
        return totalDocsInIndex;
    }

    public String getDefaultSearchFieldName()
    {
        return defaultSearchFieldName;
    }

    public void setDefaultSearchFieldName(String defaultSearchFieldName)
    {
        this.defaultSearchFieldName = defaultSearchFieldName;
    }

    public String[] getDefaultAdvancedSearchFieldNames()
    {
        return defaultAdvancedSearchFieldNames;
    }

    public void setDefaultAdvancedSearchFieldNames(String[] defaultAdvancedSearchFieldNames)
    {
        this.defaultAdvancedSearchFieldNames = defaultAdvancedSearchFieldNames;
    }

    public Analyzer getAnalyzer()
    {
        return analyzer;
    }

    public void addAnalyzer(Analyzer analyzer)  // called "addAnalyzer" so that XDM users can say <analyzer class="x.y.z"/>
    {
        this.analyzer = analyzer;
    }

    public int getMaxResultsPerPage()
    {
        return maxResultsPerPage;
    }

    public void setMaxResultsPerPage(int maxResultsPerPage)
    {
        this.maxResultsPerPage = maxResultsPerPage;
    }

    public SearchHitsRenderer createRenderer()
    {
        return new SearchHitsTemplateRenderer();
    }

    public void addRenderer(SearchHitsRenderer renderer)
    {
        this.renderer = renderer;
    }

    public SearchHitsRenderer getRenderer()
    {
        return renderer;
    }

    protected Query parseQuery(String query) throws ParseException
    {
        return QueryParser.parse(query, defaultSearchFieldName, analyzer);
    }

    public Query parseQuery(NavigationContext nc, SearchExpression expression) throws ParseException
    {
        if(expression.isSearchWithinPreviousResults())
        {
            final FullTextSearchResults searchResults = getSearchResultsManager().getActiveUserSearchResults(this, nc);
            if(searchResults == null)
                getLog().error("Attempting to search within a search but there are no active search results");
            else
            {
                BooleanQuery booleanQuery = new BooleanQuery();
                booleanQuery.add(searchResults.getQuery(), true, false);
                booleanQuery.add(parseQuery(expression.getExprText()), true, false);
                return booleanQuery;
            }
        }

        final Query advancedQuery = expression.getAdvancedQuery();
        return advancedQuery != null ? advancedQuery : parseQuery(expression.getExprText());
    }

    protected SearchHits search(final NavigationContext nc, final SearchExpression expression, final Query query) throws IOException
    {
        final Hits hits = indexSearcher.search(query);
        return new LuceneSearchHitsWrapper(hits);
    }

    public FullTextSearchResults constructSearchResults(NavigationContext nc, SearchExpression expression, Query query, SearchHits hits)
    {
        return new DefaultSearchResults(getSearchResultsManager(), this, expression, query, hits, maxResultsPerPage, searchResultsTimeOutDuration);
    }

    public Map getTermsByFields() throws IOException
    {
        TreeMap termsByFieldsMap = new TreeMap();
        IndexReader indexReader = IndexReader.open(indexDirectory);
        TermEnum terms = indexReader.terms();
        while(terms.next())
        {
            Term term = terms.term();
            Map termMap = (Map) termsByFieldsMap.get(term.field());
            if(termMap == null)
            {
                termMap = new TreeMap();
                termsByFieldsMap.put(term.field(), termMap);
            }

            termMap.put(term.text(), new Integer((terms.docFreq())));
        }
        indexReader.close();
        return termsByFieldsMap;
    }

    public void handlePageBody(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        if(!valid)
        {
            writer.write("Index loading was not completed properly, this page is not valid.");
            return;
        }

        final ServletRequest request = nc.getRequest();
        if(request.getParameter("terms") != null)
        {
            renderer.renderTerms(writer, nc, getTermsByFields());
            return;
        }

        final SearchExpression expression = renderer.getSearchExpression(nc);
        if(expression != null)
        {
            // if there was an advanced search, the search is rewritten and we want to redirect back to use with the new
            // expression
            String redirectParams = expression.getRewrittenExpressionRedirectParams();
            if(redirectParams != null)
            {
                final String url = getUrl(nc);
                nc.getHttpResponse().sendRedirect(url + (url.indexOf('?') >= 0 ? "&" : "?") + redirectParams);
                return;
            }

            if(expression.isEmptyExpression())
            {
                renderer.renderEmptyQuery(writer, nc);
                return;
            }

            // check to see if we're requesting a scroll to another page; if so, we expect search results to be stored
            // in the session
            final String scrollToPage = request.getParameter(activeScrollPageParamName);
            if(scrollToPage != null)
            {
                final FullTextSearchResults searchResults = getSearchResultsManager().getActiveUserSearchResults(this, nc);

                // if the search expression has not changed, reused the existing hits and go to another page
                if(searchResults != null && searchResults.getExpression().equals(expression))
                {
                    searchResults.getScrollState().scrollToPage(Integer.parseInt(scrollToPage));
                    renderer.renderSearchResults(writer, nc, searchResults);
                    return;
                }

                // if we get to here it means that the search expression has changed or is null and we'll drop to the
                // "normal" processing (we also get to here if the previous query timed out because of lack of use)
            }

            final Query query;
            try
            {
                query = parseQuery(nc, expression);
            }
            catch(ParseException e)
            {
                renderer.renderQueryError(writer, nc, expression, e);
                return;
            }

            final SearchHits hits;
            try
            {
                hits = search(nc, expression, query);
            }
            catch(IOException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                getLog().error(e);
                renderer.renderSearchError(writer, nc, expression, e);
                return;
            }

            final FullTextSearchResults searchResults = constructSearchResults(nc, expression, query, hits);
            FullTextSearchResultsActivity activity = new FullTextSearchResultsActivity(nc, searchResults);
            nc.getProject().broadcastActivity(activity);

            if(searchResults.getScrollState().isScrollable())
                getSearchResultsManager().setActiveUserSearchResults(nc, searchResults);

            // if the scrollToPage is not null and we get to this point it means we recreated the query because of a
            // timeout so we need to get back to the page request in the new result set
            if(scrollToPage != null)
                searchResults.getScrollState().scrollToPage(Integer.parseInt(scrollToPage));

            renderer.renderSearchResults(writer, nc, searchResults);
        }
        else
            renderer.renderSearchRequest(writer, nc);
    }

    protected void readIndexInfo(File indexDir) throws IOException
    {
        IndexReader indexReader = IndexReader.open(indexDir);
        totalDocsInIndex = indexReader.numDocs();

        List fields = new ArrayList();
        List indexedFields = new ArrayList();

        Iterator fieldIterator = indexReader.getFieldNames().iterator();
        while(fieldIterator.hasNext())
        {
            Object field = fieldIterator.next();
            if(field != null && !field.equals(""))
                fields.add(field.toString());
        }

        fieldIterator = indexReader.getFieldNames(true).iterator();
        while(fieldIterator.hasNext())
        {
            Object field = fieldIterator.next();
            if(field != null && !field.equals(""))
                indexedFields.add(field.toString());
        }
        indexReader.close();

        allFieldNames = (String[]) fields.toArray(new String[fields.size()]);
        allIndexedFieldNames = (String[]) indexedFields.toArray(new String[indexedFields.size()]);
    }

    public FieldAttribute createField()
    {
        return new FieldAttribute();
    }

    public void addField(FieldAttribute fieldAttribute)
    {
        fieldAttributes.put(fieldAttribute.getName(), fieldAttribute);
    }

    public Map getFieldAttributes()
    {
        return fieldAttributes;
    }

    public FieldAttribute getFieldAttribute(String fieldName)
    {
        return (FieldAttribute) fieldAttributes.get(fieldName);
    }

    public String[] getAdvancedSearchFieldNames()
    {
        return advancedSearchFieldNames;
    }

    public void setAdvancedSearchFieldNames(String[] advancedSearchFieldNames)
    {
        this.advancedSearchFieldNames = advancedSearchFieldNames;
    }

    public class FieldAttribute
    {
        private String name;
        private String description;
        private String usage;
        private Analyzer analyzer;
        private boolean treatAdvancedFieldExprsAsPhrases;
        private boolean uppercase;
        private boolean lowercase;

        public FieldAttribute()
        {
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getUsage()
        {
            return usage;
        }

        public void setUsage(String usage)
        {
            this.usage = usage;
        }

        public Analyzer getAnalyzer()
        {
            return analyzer;
        }

        public Analyzer createAnalyzer()
        {
            return new StandardAnalyzer();
        }

        public void addAnalyzer(Analyzer analyzer)
        {
            this.analyzer = analyzer;
        }

        public boolean isTreatAdvancedFieldExprsAsPhrases()
        {
            return treatAdvancedFieldExprsAsPhrases;
        }

        public void setTreatAdvancedFieldExprsAsPhrases(boolean treatAdvancedFieldExprsAsPhrases)
        {
            this.treatAdvancedFieldExprsAsPhrases = treatAdvancedFieldExprsAsPhrases;
        }

        public boolean isLowercase()
        {
            return lowercase;
        }

        public void setLowercase(boolean lowercase)
        {
            this.lowercase = lowercase;
        }

        public boolean isUppercase()
        {
            return uppercase;
        }

        public void setUppercase(boolean uppercase)
        {
            this.uppercase = uppercase;
        }

        public void addText(String text)
        {
            // do nothing -- it's here so XDM doesn't complain about this object not accepting PCDATA
        }
    }

}
