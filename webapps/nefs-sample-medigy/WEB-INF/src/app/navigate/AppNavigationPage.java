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
 * $Id: AppNavigationPage.java,v 1.5 2004-03-08 02:48:16 aye.thu Exp $
 */

package app.navigate;

import com.netspective.commons.command.Command;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.command.Commands;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.command.PanelEditorCommand;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationPageBodyType;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.panel.HtmlCommandPanel;
import com.netspective.sparx.panel.HtmlPanels;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

public class AppNavigationPage extends NavigationPage
{
    private static final Log log = LogFactory.getLog(HtmlCommandPanel.class);

    public AppNavigationPage(NavigationTree owner)
    {
        super(owner);
    }

    public void handlePageBody(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        HttpServletRequest request = nc.getHttpRequest();
        String commandSpec = request.getParameter(PanelEditorCommand.PANEL_EDITOR_COMMAND_REQUEST_PARAM_NAME);
        if (commandSpec != null)
        {
            PanelEditorCommand command = new PanelEditorCommand();
            command.setParameters(commandSpec);
            // verify that this command is configured to be in this pae
            if (verifyPanelEditorInPage(nc, command))
            {
                try
                {
                    command.handleCommand(writer, nc, false);
                }
                catch (CommandException e)
                {
                    getLog().error("Command error in body", e);
                    throw new ServletException(e);
                }
                return;
            }
            else
            {
                log.error("Request to execute a panel editor '" + command.getPanelEditorName() +"' that does not exist in page.");
            }

        }

        super.handlePageBody(writer, nc);
    }

    /**
     * Verify that the request panel editor does exist in the page
     *
     * @param nc
     * @param peCommand
     * @return
     */
    public boolean verifyPanelEditorInPage(NavigationContext nc, PanelEditorCommand peCommand)
    {
        boolean valid = false;
        int bodyType = getBodyType().getValueIndex();
        if (bodyType == NavigationPageBodyType.COMMAND)
        {
            ValueSource commandExpr = getCommandExpr();
            if(commandExpr != null)
            {
                String commandText = commandExpr.getTextValue(nc);
                if(commandText != null)
                {
                    try
                    {
                        Command command = Commands.getInstance().getCommand(commandText);
                        if (command instanceof PanelEditorCommand)
                        {
                            String peName = ((PanelEditorCommand) command).getPanelEditorName();
                            if (peCommand.getPanelEditorName().equals(peName))
                                valid = true;
                        }
                    }
                    catch (Exception e)
                    {
                        getLog().error("Command error in " + this.getClass().getName(), e);
                    }
                }
            }
        }
        else if (bodyType == NavigationPageBodyType.PANEL)
        {
            // this is the layout panel defined for the page
            HtmlPanels panels = getBodyPanel().getChildren();
            for (int i = 0; i < panels.size(); i++)
            {
                if (panels.get(i) instanceof HtmlCommandPanel)
                {
                    Command command = ((HtmlCommandPanel)panels.get(i)).getCommand();
                    if (command instanceof PanelEditorCommand)
                    {
                        String peName = ((PanelEditorCommand) command).getPanelEditorName();
                        if (peCommand.getPanelEditorName().equals(peName))
                        {
                            valid = true;
                            break;
                        }
                    }
                }
            }
        }
        return valid;
    }
}
