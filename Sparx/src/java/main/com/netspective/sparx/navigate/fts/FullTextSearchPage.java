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

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationPageBodyType;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.navigate.fts.SearchHitsRenderer.SearchExpression;

public class FullTextSearchPage extends NavigationPage
{
    private String activeScrollPageParamName = "scroll-page";
    private String activeUserSearchResultsSessAttrName = "active-search-results";
    private File indexDir;
    private IndexSearcher indexSearcher;
    private Analyzer analyzer = new StandardAnalyzer();
    private String defaultSearchFieldName;
    private String defaultAdvancedSearchFieldName;
    private SearchHitsRenderer renderer;
    private int maxResultsPerPage = 25;
    private int totalDocsInIndex = 0;
    private String[] allFieldNames;
    private String[] allIndexedFieldNames;
    private Map fieldAttributes = new HashMap();
    private String[] advancedSearchFieldNames;

    public FullTextSearchPage(NavigationTree owner)
    {
        super(owner);
        setBodyType(new NavigationPageBodyType(NavigationPageBodyType.CUSTOM_HANDLER));
        getFlags().setFlag(Flags.BODY_AFFECTS_NAVIGATION); // because we can redirect advanced queries
    }

    public File getIndexDir()
    {
        return indexDir;
    }

    public void setIndexDirectory(File indexDir) throws IOException
    {
        if(!indexDir.isAbsolute())
        {
            // if we're not giving an absolute location of the index then find the index directory relative to the
            // location of the XDM file where this was defined
            indexDir = new File(new File(new File(getInputSourceLocator().getInputSourceTracker().getIdentifier()).getParent()),
                                indexDir.getAbsolutePath());
        }

        if(!indexDir.exists() || !indexDir.isDirectory())
            getLog().error("Index directory " + indexDir + " does not exist for FullTextSearchPage " + getQualifiedName());
        else
        {
            readIndexInfo(indexDir);
            indexSearcher = new IndexSearcher(indexDir.getAbsolutePath());
        }
        this.indexDir = indexDir;
    }

    public String getActiveScrollPageParamName()
    {
        return activeScrollPageParamName;
    }

    public void setActiveScrollPageParamName(String activeScrollPageParamName)
    {
        this.activeScrollPageParamName = activeScrollPageParamName;
    }

    public String getActiveUserSearchResultsSessAttrName()
    {
        return activeUserSearchResultsSessAttrName;
    }

    public void setActiveUserSearchResultsSessAttrName(String activeUserSearchResultsSessAttrName)
    {
        this.activeUserSearchResultsSessAttrName = activeUserSearchResultsSessAttrName;
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

    public String getDefaultAdvancedSearchFieldName()
    {
        return defaultAdvancedSearchFieldName;
    }

    public void setDefaultAdvancedSearchFieldName(String defaultAdvancedSearchFieldName)
    {
        this.defaultAdvancedSearchFieldName = defaultAdvancedSearchFieldName;
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

    public FullTextSearchResults getActiveUserSearchResults(NavigationContext nc)
    {
        return (FullTextSearchResults) nc.getHttpRequest().getSession().getAttribute(activeUserSearchResultsSessAttrName);
    }

    public void setActiveUserSearchResults(NavigationContext nc, FullTextSearchResults searchResults)
    {
        nc.getHttpRequest().getSession().setAttribute(activeUserSearchResultsSessAttrName, searchResults);
    }

    public Query parseQuery(NavigationContext nc, SearchExpression expression) throws ParseException
    {
        if(expression.isSearchWithinPreviousResults())
        {
            final FullTextSearchResults searchResults = getActiveUserSearchResults(nc);
            if(searchResults == null)
                getLog().error("Attempting to search within a search but there is are no active search results");
            else
            {
                BooleanQuery booleanQuery = new BooleanQuery();
                booleanQuery.add(searchResults.getQuery(), true, false);
                booleanQuery.add(QueryParser.parse(expression.getExprText(), defaultSearchFieldName, analyzer), true, false);
                return booleanQuery;
            }
        }

        return QueryParser.parse(expression.getExprText(), defaultSearchFieldName, analyzer);
    }

    public FullTextSearchResults constructSearchResults(NavigationContext nc, SearchExpression expression, Query query, Hits hits)
    {
        return new DefaultSearchResults(this, expression, query, hits, maxResultsPerPage);
    }

    public void handlePageBody(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        final ServletRequest request = nc.getRequest();
        final SearchExpression expression = renderer.getSearchExpression(nc);
        if(expression != null)
        {
            // if there was an advanced search, the search is rewritten and we want to redirect back to use with the new
            // expression
            String redirectParams = expression.getRewrittenExpressionRedirectParams();
            if(redirectParams != null)
            {
                nc.getHttpResponse().sendRedirect(getUrl(nc) + "?" + redirectParams);
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
                final FullTextSearchResults searchResults = getActiveUserSearchResults(nc);

                // if the search expression has not changed, reused the existing hits and go to another page
                if(searchResults != null && expression.getExprText().equals(searchResults.getExpression().getExprText()))
                {
                    final String activePageParamValue = request.getParameter(activeScrollPageParamName);
                    if(activePageParamValue != null)
                        searchResults.scrollToPage(Integer.parseInt(activePageParamValue));
                    renderer.renderSearchResults(writer, nc, searchResults);
                    return;
                }

                // if we get to here it means that the search expression has changed or is null and we'll drop to the
                // "normal" processing
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

            final Hits hits = indexSearcher.search(query);
            final FullTextSearchResults searchResults = constructSearchResults(nc, expression, query, hits);
            FullTextSearchResultsActivity activity = new FullTextSearchResultsActivity(nc, searchResults);
            nc.getProject().broadcastActivity(activity);

            if(searchResults.isScrollable())
                setActiveUserSearchResults(nc, searchResults);

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
    }

}
