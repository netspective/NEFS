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

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import com.netspective.sparx.navigate.NavigationContext;

/**
 * Holds the results of one invocation of any particular query navigator page. If another query with a different execution
 * id is passed in (meaning new parameters were probably used) then the old one is disposed and a new query is created.
 */
public class QueryResultsSessionStateManager implements QueryResultsStateManager
{
    public QueryResultsNavigatorState getQueryResultsNavigatorState(QueryResultsNavigatorPage page, NavigationContext nc, String executionId) throws SQLException, NamingException
    {
        final String sessAttrName = page.getQualifiedName() + "_active-query-results";
        final HttpSession session = nc.getHttpRequest().getSession();
        QueryResultsNavigatorState result = (QueryResultsNavigatorState) session.getAttribute(sessAttrName);

        if(result != null)
        {
            // if the state has timed out or we're now starting a new execution of the same query (using different params),
            // we want to get rid of it and create another one
            if(!result.isValid() || !result.getExecutionIdentifer().equals(executionId))
            {
                session.removeAttribute(sessAttrName);
                result = null;
            }
        }

        if(result == null)
        {
            result = page.constructQueryResults(nc, executionId);
            session.setAttribute(sessAttrName, result);
        }
        return result;
    }
}
