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
 * $Id: AppAuthenticatedUser.java,v 1.7 2003-10-31 08:31:33 aye.thu Exp $
 */

package app.cts;

import com.netspective.commons.security.AuthenticatedUserInitializationException;
import com.netspective.commons.security.BasicAuthenticatedUser;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.Value;
import com.netspective.axiom.ConnectionContext;
import com.netspective.sparx.security.LoginDialogContext;
import com.netspective.sparx.navigate.NavigationControllerAuthenticatedUser;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.navigate.NavigationControllerServlet;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.io.IOException;

public class AppAuthenticatedUser extends BasicAuthenticatedUser implements NavigationControllerAuthenticatedUser
{
    /**
     * Initialize the authenticated user object with relevant data
     * @param vc
     * @throws AuthenticatedUserInitializationException
     */

    public void init(ValueContext vc) throws AuthenticatedUserInitializationException
    {
        super.init(vc);
        LoginDialogContext ldc = (LoginDialogContext) vc;

        ConnectionContext cc = null;
        try
        {
            cc = ldc.getConnection(null, false);
            Query query = ldc.getProject().getQuery(auto.id.sql.query.Person.AUTHENTICATED_USER_INFO);
            QueryResultSet qrs = query.execute(cc, new Object[] {getUserId()}, false);
            ResultSet rs = qrs.getResultSet();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next())
            {
                for (int i=1; i <= rsmd.getColumnCount(); i++)
                {
                    setAttribute(rsmd.getColumnName(i).toLowerCase(), rs.getString(i));
                    System.out.println(rsmd.getColumnName(i).toLowerCase() + " " + rs.getString(i));
                }
            }
            rs.close();
            // check to see if the user is related to any active studies as a subject

            query =  ldc.getProject().getQuery(auto.id.sql.query.Person.GET_ACTIVE_STUDY_AS_SUBJECT);
            qrs = query.execute(cc, new Object[] {getAttribute("person_id")}, false);
            rs = qrs.getResultSet();
            rsmd = rs.getMetaData();
            if (rs.next())
            {
                // IMPORTANT: A person can only be a subject to one study at any given time
                for (int i=1; i <= rsmd.getColumnCount(); i++)
                {
                    setAttribute(rsmd.getColumnName(i).toLowerCase(), rs.getString(i));
                    System.out.println(rsmd.getColumnName(i).toLowerCase() + " " + rs.getString(i));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(cc != null)
                    cc.close();
            }
            catch (SQLException e)
            {
                ldc.getDialog().getLog().error("Unable to close connection", e);
                throw new AuthenticatedUserInitializationException(e, this);
            }
        }
    }

    /**
     * Specifies if the user has its own navigation tree
     * @return
     */
    public boolean hasUserSpecificNavigationTree()
    {
        return true;
    }

    /**
     * Returns the tree object to be used for this user
     * @param ncServlet
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    public NavigationTree getUserSpecificNavigationTree(NavigationControllerServlet ncServlet, HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse)
    {
        String treeName = null;
        String type = (String) getAttribute("person_type");
        if (type.equals("Patient"))
            treeName = "patient";
        else if (type.equals("Certified Clinical Research Coordinator"))
            treeName = "coordinator";
        else
            throw new RuntimeException("Unknown User Type");

        return ncServlet.getProject().getNavigationTree(treeName);
    }

    public void redirectToUserTree(NavigationContext nc)
    {
        NavigationTree userTree = getUserSpecificNavigationTree((NavigationControllerServlet)nc.getHttpServlet(), nc.getHttpRequest(), nc.getHttpResponse());
        try
        {
            nc.getHttpResponse().sendRedirect(userTree.getHomePage().getUrl(nc));
        }
        catch (IOException e)
        {
            // Failed to redirect
            e.printStackTrace();
        }
    }

    public String getPersonId()
    {
        return  (String)getAttribute("person_id");
    }

    public String getLastName()
    {
        return  (String)getAttribute("name_last");
    }

    public String getCompleteName()
    {
        return (String) getAttribute("complete_name");
    }

}
