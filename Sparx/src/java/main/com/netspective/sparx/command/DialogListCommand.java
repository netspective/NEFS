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
 * @version $Id: DialogListCommand.java,v 1.1 2003-12-08 05:11:50 aye.thu Exp $
 */
package com.netspective.sparx.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.Commands;
import com.netspective.commons.command.Command;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.ValueSources;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.basic.HtmlListPanelSkin;
import com.netspective.sparx.panel.BasicHtmlPanelValueContext;
import com.netspective.sparx.panel.HtmlCommandPanel;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.value.HttpServletValueContext;

import java.io.Writer;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;


/**
 * Command class for displaying dialogs belonging to one package as a list of command links. This is used to display
 * a "selection" list of dialogs and when one  dialog link is selected, the dialog itself is displayed.
 */
public class DialogListCommand  extends CommandListCommand
{
    private static final Log log = LogFactory.getLog(DialogListCommand.class);
    public static final String[] IDENTIFIERS = new String[] { "dialog-list" };
    public static final String[] DIALOG_COMMAND_RETAIN_PARAMS =
            {
                PAGE_COMMAND_REQUEST_PARAM_NAME
            };
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays a list of command items such as dialogs, queries, and query definitions to execute.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("dialog-pkg-name", true, "The name of the dialog package"),
            }
    );

    private String pkgName;

    /**
     * Gets the list of command aliases
     * @return
     */
    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    /**
     * Gets the documentation object
     * @return
     */
    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    /**
     * Gets the command parameters
     * @return
     */
    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(pkgName);

        return sb.toString();
    }

    /**
     * Sets the command parameters
     * @param params
     */
    public void setParameters(StringTokenizer params)
    {
        pkgName = params.nextToken();
    }

    public HtmlPanelValueContext getPanelContext(HttpServletValueContext vc)
    {
        if (getPanel() == null)
        {
            // need a panel instance so that a context can be passed to the skin.
            HtmlCommandPanel panel = new HtmlCommandPanel();
            HtmlPanelFrame frame = new HtmlPanelFrame();
            frame.setHeading(ValueSources.getInstance().getValueSourceOrStatic(pkgName));
            panel.setFrame(frame);

            return new BasicHtmlPanelValueContext(vc.getServlet(), vc.getRequest(),
                vc.getResponse(), panel);
        }
        else
            return super.getPanelContext(vc);
    }

    public List getItems(HttpServletValueContext vc)
    {
        List dialogs = vc.getDialogsManager().getDialogs(pkgName);
        List cmdItems = new ArrayList();
        for (int i=0; i < dialogs.size(); i++)
        {
            Dialog dialog = (Dialog) dialogs.get(i);
            Command dCommand = Commands.getInstance().getCommand(DialogCommand.IDENTIFIERS[0], dialog.getQualifiedName());
            CommandListItem cli = new CommandListItem();
            cli.setCommand(dCommand);
            cli.setCaption(dialog.getFrame().getHeading());
            cli.setIndex(i);
            cmdItems.add(i, cli);
        }
        return cmdItems;
    }
}
