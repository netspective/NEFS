/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Aye Thu
 */

/**
 * $Id: StudyRelatedPageBodyHandler.java,v 1.1 2003-10-31 08:33:02 aye.thu Exp $
 */
package app.cts.navigate;

import com.netspective.sparx.navigate.handler.NavigationPageBodyDefaultHandler;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPath;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.ConnectionContext;

import javax.servlet.ServletException;
import java.io.Writer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import app.cts.AppAuthenticatedUser;

/**
 * Class for handling all pages that are children of the main selected clinical trial page.
 * This will be used to return to the main page and to keep track of current page's location
 * relative to the main trial/study page.
 */
public class StudyRelatedPageBodyHandler extends NavigationPageBodyDefaultHandler
{
    public void handleNavigationPageBody(NavigationPage page, Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        List parentList = nc.getActivePage().getAncestorsList();
        for (int i=0; i < parentList.size(); i++)
        {
            NavigationPage path = (NavigationPage) parentList.get(i);
            System.out.println(path.getCaption() + " " + path.getUrl(nc));
        }
        /*
        SqlManager sqlManager = nc.getSqlManager();
        String studyId = nc.getHttpRequest().getParameter("study_id");
        AppAuthenticatedUser user = (AppAuthenticatedUser)nc.getAuthenticatedUser();

        Object[][] studyList = null;
        ConnectionContext cc = null;
        try
        {
            cc = nc.getConnection(null, true);
            studyList = sqlManager.executeQueryAndGetMatrix(cc, "person.self-active-studies", new Object[] {user.getPersonId()},
                    true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            writer.write("<div id=\"study-selection-list\" style=\"padding:10\">\n");
            writer.write("An error occurred while processing your request. Please contact the " +
                    "Administrator with the following error information: "  + e.toString());
            writer.write("</div>\n");
        }
        finally
        {
            if (cc != null)
            {
                try
                {
                    cc.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();

                }
            }
        }


        writer.write("<div id=\"study-selection-list\" style=\"padding:10\">\n");
        writer.write("My Clinical Trial: &nbsp; <select name=\"study-list\" " +
                "onChange=\"javascript:document.location.href=this.options[this.selectedIndex].value;\">\n");
        writer.write("\t<option value=\""+ page.getUrl(nc) + "\"></option>\n");
        for (int i=0; studyList != null && i < studyList.length; i++)
        {
            String url = page.getUrl(nc) + "?study_id=" + studyList[i][0];
            if (studyId != null && studyId.equals(studyList[i][0].toString()))
                writer.write("\t<option value=\"" + url + "\" selected>" + studyList[i][1] + "</option>\n");
            else
                writer.write("\t<option value=\"" + url + "\">" + studyList[i][1] + "</option>\n");
        }
        writer.write("</select>\n");
        writer.write("</div>\n");

        if (studyId == null || studyId.length() == 0)
        {
            writer.write("<div align=\"left\" style=\"padding:10\">\n");
            if (studyList != null && studyList.length > 0)
            {
                writer.write("You are currently involved with <b>" + studyList.length + "</b> active clinical trials. " +
                        " Please select from above select list to view information about the individual clinical trials .\n");
            }
            else
            {
                writer.write("You are not currently involved with any active clinical trials.");
            }
            writer.write("</div>\n");
        }
        else
        {
            page.getBodyPanel().render(writer, nc, nc.getActiveTheme(), HtmlPanel.RENDERFLAGS_DEFAULT);
        }
        */
    }

}
