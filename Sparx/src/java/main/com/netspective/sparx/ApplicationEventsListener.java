/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: ApplicationEventsListener.java,v 1.2 2003-08-17 13:37:17 shahid.shah Exp $
 */

package com.netspective.sparx;

import java.util.Enumeration;
import java.util.Set;
import java.util.Iterator;
import java.sql.SQLException;
import java.sql.Connection;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.report.tabular.TabularReportDataSourceScrollState;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.connection.AbstractConnectionContext;

public class ApplicationEventsListener implements ServletContextListener, HttpSessionListener
{
    private static final Log log = LogFactory.getLog(ApplicationEventsListener.class);

    /**
     * Properly dispose of all Sparx objects stored in a user's session. It will timeout any authenticated users,
     * timeout any scroll states (from pageable queries, query defns, etc), rollback and close any database Connections,
     * and timeOut all ConnectionContexts.
     * @param session The user's HttpSession where session-specific data is stored in session attributes
     */
    public void cleanupSession(HttpSession session)
    {
        log.trace("Cleaning up session for " + session.getServletContext().getServletContextName());
        for(Enumeration e = session.getAttributeNames(); e.hasMoreElements(); )
        {
            String sessAttrName = (String) e.nextElement();
            Object sessAttrValue = session.getAttribute(sessAttrName);

            if(sessAttrValue instanceof AuthenticatedUser)
            {
                ((AuthenticatedUser) sessAttrValue).registerTimeOut();
                continue;
            }

            if(sessAttrValue instanceof TabularReportDataSourceScrollState)
            {
                ((TabularReportDataSourceScrollState) sessAttrValue).timeOut();
                continue;
            }

            if(sessAttrValue instanceof Connection)
            {
                Connection sharedConn = (Connection) sessAttrValue;
                try
                {
                    try
                    {
                        sharedConn.rollback();
                    }
                    finally
                    {
                        sharedConn.close();
                    }
                }
                catch (SQLException se)
                {
                    log.error("Unable to rollback and close the shared (session) connection.", se);
                }
            }

            if(sessAttrValue instanceof ConnectionContext)
            {
                ConnectionContext cc = (ConnectionContext) sessAttrValue;
                if(cc.getOwnership() != ConnectionContext.OWNERSHIP_AUTHENTICATED_USER)
                {
                    String message = "A connection context for DataSource '"+ cc.getDataSourceId() +"' was created and " +
                                     "stored in the session but was not marked as being owned by a user. It will still " +
                                     "be closed but needs to be marked if you don't want this error message to appear again.";
                    if(log.isErrorEnabled())
                        log.error(message, cc.getContextNotClosedException());
                    else
                    {
                        System.err.println(message);
                        cc.getContextNotClosedException().printStackTrace(System.err);
                    }
                }

                try
                {
                    ((ConnectionContext) sessAttrValue).timeOut();
                }
                catch (SQLException se)
                {
                    log.error("Unable to timeOut the connection ", se);
                }
                continue;
            }
        }
    }

    public void contextInitialized(ServletContextEvent event)
    {
        log.trace("Init context " + event.getServletContext().getServletContextName());
    }

    public void contextDestroyed(ServletContextEvent event)
    {
        log.trace("Destroying context " + event.getServletContext().getServletContextName());

        Set connContextsWithOpenConnections = AbstractConnectionContext.getConnectionContextsWithOpenConnections();
        for(Iterator i = connContextsWithOpenConnections.iterator(); i.hasNext(); )
        {
            ConnectionContext cc = (ConnectionContext) i.next();

            // if the connection is owned by a particular user than it's probably stored in a session so we're not
            // going to meddle with it (it will be disposed of appropriately in the session cleanup)
            if(cc.getOwnership() != ConnectionContext.OWNERSHIP_AUTHENTICATED_USER)
            {
                String message = "Connection for DataSource '"+ cc.getDataSourceId() +"' not closed (ownership = " + cc.getOwnership() + ")";
                if(log.isErrorEnabled())
                    log.error(message, cc.getContextNotClosedException());
                else
                {
                    System.err.println(message);
                    cc.getContextNotClosedException().printStackTrace(System.err);
                }
            }
        }
    }

    public void sessionCreated(HttpSessionEvent event)
    {
        log.trace("Init session for " + event.getSession().getServletContext().getServletContextName());
    }

    public void sessionDestroyed(HttpSessionEvent event)
    {
        cleanupSession(event.getSession());
    }
}
