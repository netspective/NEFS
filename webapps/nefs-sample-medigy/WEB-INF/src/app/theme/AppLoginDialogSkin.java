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
 * @author Aye Thu
 */

/**
 * $Id: AppLoginDialogSkin.java,v 1.1 2004-04-23 04:18:29 aye.thu Exp $
 */

package app.theme;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.basic.LoginDialogSkin;

import java.io.IOException;
import java.io.Writer;

public class AppLoginDialogSkin extends LoginDialogSkin
{
    private String loginImage = "login.gif";

    public AppLoginDialogSkin()
    {
        super();
    }

    public AppLoginDialogSkin(Theme theme, String name, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, name, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    public String getLoginImage()
    {
        return loginImage;
    }

    public void setLoginImage(String loginImage)
    {
        this.loginImage = loginImage;
    }

    public void renderContentsHtml(Writer writer, DialogContext dc, String dialogName, String actionURL, String encType, int dlgTableColSpan, StringBuffer errorMsgsHtml, StringBuffer fieldsHtml) throws IOException
    {
       Theme theme = getTheme();
        writer.write("                    <table border=\"0\" width=\"50%\" cellspacing=\"0\" cellpadding=\"0\">");

           writer.write("                        <tr width=\"100%\" height=\"100%\">");
            writer.write("                            <td class=\"login-dialog-fields-header\" align=\"left\" valign=\"bottom\" height=\"90\"></td>");
            writer.write("                        </tr>");

        writer.write("                        <tr>");
        writer.write("                            <td class=\"panel-input-login-content\">");
        writer.write("                                <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
        writer.write("                                    <tr>");
        writer.write("                                        <td align=\"left\" valign=\"bottom\">" +
                "<img src=\"" + theme.getResourceUrl("/images/login/"+ getLoginImage()) +"\" " +
                "alt=\"\" border=\"0\"></td>");
        writer.write("                                        <td align=\"left\" valign=\"middle\">");

        writer.write("          <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");

        if(summarizeErrors)
            writer.write(errorMsgsHtml.toString());

        writer.write(
                "<form id='" + dialogName + "' name='" + dialogName + "' action='" + actionURL + "' method='post' " +
                encType + " onsubmit='return(activeDialog.isValid())'>\n" +
                dc.getStateHiddens() + "\n" +
                fieldsHtml +
                "</form>\n");

        writer.write("          </table>\n");

        writer.write("                                        </td>");
        writer.write("                                    </tr>");

        writer.write("                                </table>");

        writer.write("                            </td>");

        writer.write("                        </tr>");
        writer.write("                    </table>");

    }
}