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
 * @author Shahid N. Shah
 */

package com.netspective.sparx.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.DialogDebugFlags;
import com.netspective.sparx.form.DialogState;
import com.netspective.sparx.navigate.NavigationContext;

import java.util.StringTokenizer;
import java.io.Writer;
import java.io.IOException;


public class AutoPerspectiveDialogCommand extends DialogCommand
{
    private static final Log log = LogFactory.getLog(AutoPerspectiveDialogCommand.class);
    public static final String[] IDENTIFIERS = new String[] { "perspective-dialog" };
    public static final String[] DIALOG_COMMAND_RETAIN_PARAMS =
            {
                PAGE_COMMAND_REQUEST_PARAM_NAME
            };
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays and executes a dialog box.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("dialog-name", true, "The fully qualified name of the dialog (package-name.dialog-name)"),
                new CommandDocumentation.Parameter("dialog-perspective", false, new DialogPerspectives(), null, "The dialog perspective to send to DialogContext."),
                new SkinParameter(),
                new CommandDocumentation.Parameter("debug-flags", false, new DialogDebugFlags(), null, "The debug flags."),
                new CommandDocumentation.Parameter("perspective-key", false, "The key that is used to determine if the dialog is in add or edit mode")
            }
    );

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
