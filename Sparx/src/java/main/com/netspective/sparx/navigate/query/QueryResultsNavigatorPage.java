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
package com.netspective.sparx.navigate.query;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.commons.template.TemplateProcessor;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.Project;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.navigate.fts.FullTextSearchResults;
import com.netspective.sparx.template.freemarker.FreeMarkerTemplateProcessor;

public class QueryResultsNavigatorPage extends NavigationPage
{
    private String activeScrollPageParamName = "scroll-page";
    private String queryRef;
    private Query query;
    private boolean valid;
    private String invalidMessage;
    private int maxResultsPerPage = 25;
    private int resultSetTimeOutDuration = 30000; // 30 seconds of inactivity will close the result set automatically
    private ValueSource queryExecutionId; // how a unique query execution (parameters, etc) should be identified (should return text)
    private String qrNavigatorStateTemplateVarName = "qrNavigatorState";

    public QueryResultsNavigatorPage(NavigationTree owner)
    {
        super(owner);
        getFlags().setFlag(Flags.BODY_AFFECTS_NAVIGATION); // because we can redirect advanced queries
    }

    public QueryResultsNavigatorStatesManager getQueryResultsNavigatorStatesManager()
    {
        return DefaultQueryResultsNavigatorStatesManager.getInstance();
    }

    public void finalizeContents()
    {
        super.finalizeContents();

        valid = false;

        if(query == null && queryRef == null)
        {
            invalidMessage = "Either a query tag or query-ref attribute must be supplied.";
            return;
        }

        if(query != null && queryRef != null)
        {
            invalidMessage = "Query tag and query-ref attribute are mutually exclusive.";
            return;
        }

        if(queryRef != null)
        {
            final Project project = getOwner().getProject();
            Query q = project.getQuery(queryRef);
            if(q == null)
            {
                invalidMessage = "Query ref '" + queryRef + "' does not point to an already defined query. Available: " + project.getQueries().getNames();
                return;
            }

            // found one, so assign it
            this.query = q;
        }

        if(queryExecutionId == null)
        {
            invalidMessage = "No query execution identifier was provided.";
            return;
        }

        valid = true;
    }

    public int getResultSetTimeOutDuration()
    {
        return resultSetTimeOutDuration;
    }

    public void setResultSetTimeOutDuration(int resultSetTimeOutDuration)
    {
        this.resultSetTimeOutDuration = resultSetTimeOutDuration;
    }

    protected Map createDefaultTemplateVars(FullTextSearchResults searchResults)
    {
        final Map templateVars = new HashMap();
        return templateVars;
    }

    public ValueSource getQueryExecutionId()
    {
        return queryExecutionId;
    }

    public void setQueryExecutionId(ValueSource queryExecutionId)
    {
        this.queryExecutionId = queryExecutionId;
    }

    public String getQueryRef()
    {
        return queryRef;
    }

    public void setQueryRef(String queryRef)
    {
        this.queryRef = queryRef;
    }

    public Query createQuery()
    {
        return new Query();
    }

    public void addQuery(Query query)
    {
        this.query = query;
    }

    public Query getQuery()
    {
        return query;
    }

    public TemplateProcessor createResultsBody()
    {
        return new FreeMarkerTemplateProcessor();
    }

    public String getActiveScrollPageParamName()
    {
        return activeScrollPageParamName;
    }

    public void setActiveScrollPageParamName(String activeScrollPageParamName)
    {
        this.activeScrollPageParamName = activeScrollPageParamName;
    }

    public int getMaxResultsPerPage()
    {
        return maxResultsPerPage;
    }

    public void setMaxResultsPerPage(int maxResultsPerPage)
    {
        this.maxResultsPerPage = maxResultsPerPage;
    }

    /**
     * A utility method called by state manager instances to construct a new navigation state.
     */
    public QueryResultsNavigatorState constructQueryResults(NavigationContext nc, String executionId) throws SQLException, NamingException
    {
        QueryResultSet qrs = getQuery().execute(nc, null, true);
        return new DefaultQueryResultsNavigatorState(getQueryResultsNavigatorStatesManager(), this, executionId, qrs, maxResultsPerPage, resultSetTimeOutDuration);
    }

    public Map createDefaultResultsBodyTemplateVars(NavigationContext nc, QueryResultsNavigatorState state) throws SQLException
    {
        if(state == null) return null;
        final HashMap results = new HashMap();
        results.put(qrNavigatorStateTemplateVarName, state);
        return results;
    }

    public String getQrNavigatorStateTemplateVarName()
    {
        return qrNavigatorStateTemplateVarName;
    }

    public void setQrNavigatorStateTemplateVarName(String qrNavigatorStateTemplateVarName)
    {
        this.qrNavigatorStateTemplateVarName = qrNavigatorStateTemplateVarName;
    }

    public void handlePageBody(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        if(!valid)
        {
            writer.write(invalidMessage);
            return;
        }

        final Map templateVars;
        final ServletRequest request = nc.getRequest();
        try
        {
            final String executionId = queryExecutionId.getTextValue(nc);

            QueryResultsNavigatorState queryResults = getQueryResultsNavigatorStatesManager().getActiveUserQueryResults(this, nc, executionId);
            if(queryResults == null)
            {
                queryResults = constructQueryResults(nc, executionId);
                if(queryResults.getScrollState().isScrollable())
                    getQueryResultsNavigatorStatesManager().setActiveUserQueryResults(nc, queryResults);
            }

            final String scrollToPage = request.getParameter(activeScrollPageParamName);
            if(scrollToPage != null)
                queryResults.getScrollState().scrollToPage(Integer.parseInt(scrollToPage));

            templateVars = createDefaultResultsBodyTemplateVars(nc, queryResults);
        }
        catch(NamingException e)
        {
            throw new NestableRuntimeException(e);
        }
        catch(SQLException e)
        {
            throw new NestableRuntimeException(e);
        }

        // handle the body template
        if(templateVars != null)
            getBodyTemplate().process(writer, nc, templateVars);
        else
            writer.write("No active result set found.");
    }
}
