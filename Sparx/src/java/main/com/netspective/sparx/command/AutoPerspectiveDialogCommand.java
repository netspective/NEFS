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

package com.netspective.sparx.command;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.sparx.form.DialogDebugFlags;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.DialogState;
import com.netspective.sparx.navigate.NavigationContext;


public class AutoPerspectiveDialogCommand extends DialogCommand
{
    private static final Log log = LogFactory.getLog(AutoPerspectiveDialogCommand.class);
    public static final String[] IDENTIFIERS = new String[]{"perspective-dialog"};
    public static final String[] DIALOG_COMMAND_RETAIN_PARAMS =
            {
                PAGE_COMMAND_REQUEST_PARAM_NAME
            };
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation("Displays and executes a dialog box.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("dialog-name", true, "The fully qualified name of the dialog (package-name.dialog-name)"),
                new CommandDocumentation.Parameter("dialog-perspective", false, new DialogPerspectives(), null, "The dialog perspective to send to DialogContext."),
                new SkinParameter(),
                new CommandDocumentation.Parameter("debug-flags", false, new DialogDebugFlags(), null, "The debug flags."),
                new CommandDocumentation.Parameter("perspective-key", false, "The key that is used to determine if the dialog is in add or edit mode")
            });

    private String perspectiveKey;

    public void setParameters(StringTokenizer params)
    {
        super.setParameters(params);
        if (params.hasMoreTokens())
        {
            perspectiveKey = params.nextToken();
        }
    }

    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        if (perspectiveKey != null && getPerspective() == null)
        {
            // no perspective was set but the key was set
            String keyValue = nc.getRequest().getParameter(perspectiveKey);
            if (keyValue != null && keyValue.length() > 0)
            {
                // keyValue exists so put the dialog in EDIT mode
                nc.getRequest().setAttribute(DialogState.PARAMNAME_PERSPECTIVE, "EDIT");

            }
            else
            {
                nc.getRequest().setAttribute(DialogState.PARAMNAME_PERSPECTIVE, "ADD");
            }

        }

        super.handleCommand(writer, nc, unitTest);
    }
}
