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

/**
 * $Id: DialogCommand.java,v 1.9 2003-07-11 20:53:15 shahid.shah Exp $
 */

package com.netspective.sparx.command;

import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.DialogDebugFlags;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogSkin;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.panel.HtmlPanel;

import java.util.StringTokenizer;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DialogCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(DialogCommand.class);
    public static final String[] IDENTIFIERS = new String[] { "dialog" };
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
                new CommandDocumentation.Parameter("debug-flags", false, new DialogDebugFlags(), null, "The debug flags.")
            }
    );

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private String dialogName;
    private DialogPerspectives perspective;
    private String skinName;
    private DialogDebugFlags debugFlags;

    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(dialogName);
        sb.append(delim);
        sb.append(perspective != null ? perspective.getFlagsText() : PARAMVALUE_DEFAULT);
        if(skinName != null)
        {
            sb.append(delim);
            sb.append(skinName);
        }
        if(debugFlags != null)
        {
            sb.append(delim);
            sb.append(debugFlags.getFlagsText());
        }
        return sb.toString();
    }

    public void setParameters(StringTokenizer params)
    {
        dialogName = params.nextToken();

        if(params.hasMoreTokens())
        {
            String dataCmdText = params.nextToken();
            if(dataCmdText.length() == 0 || dataCmdText.equals(PARAMVALUE_DEFAULT))
                dataCmdText = null;
            if(dataCmdText != null)
            {
                perspective = new DialogPerspectives();
                perspective.setValue(dataCmdText);
            }
        }
        else
            perspective = null;

        if(params.hasMoreTokens())
        {
            skinName = params.nextToken();
            if(skinName.length() == 0 || skinName.equals(PARAMVALUE_DEFAULT))
                skinName = null;
        }
        else
            skinName = null;

        if(params.hasMoreTokens())
        {
            String debugFlagsSpec = params.nextToken();
            if(debugFlagsSpec.length() == 0 || debugFlagsSpec.equals(PARAMVALUE_DEFAULT))
                debugFlagsSpec = null;
            debugFlags = new DialogDebugFlags();
            debugFlags.setValue(debugFlagsSpec);
        }
        else
            debugFlags = null;
    }

    public DialogPerspectives getPerspective()
    {
        return perspective;
    }

    public void setPerspective(DialogPerspectives perspective)
    {
        this.perspective = perspective;
    }

    public DialogDebugFlags getDebugFlags()
    {
        return debugFlags;
    }

    public void setDebugFlags(DialogDebugFlags debugFlags)
    {
        this.debugFlags = debugFlags;
    }

    public String getDialogName()
    {
        return dialogName;
    }

    public void setDialogName(String dialogName)
    {
        this.dialogName = dialogName;
    }

    public String getSkinName()
    {
        return skinName;
    }

    public void setSkinName(String skinName)
    {
        this.skinName = skinName;
    }

    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        if(perspective != null)
            nc.getRequest().setAttribute(Dialog.PARAMNAME_PERSPECTIVE_INITIAL, perspective.getFlagsText());

        Dialog dialog = nc.getDialogsManager().getDialog(dialogName);
        if(dialog == null)
        {
            writer.write("Dialog '" + dialogName + "' not found in navigation context.");
            return;
        }

        Theme theme = nc.getActiveTheme();
        DialogSkin skin = skinName != null ? theme.getDialogSkin(skinName) : theme.getDefaultDialogSkin();
        if(skin == null)
        {
            writer.write("DialogSkin '" + skinName + "' not found in skin factory.");
            return;
        }

        DialogContext dc = dialog.createContext(nc, skin);
        if(debugFlags != null)
            dc.getDebugFlags().setFlag(debugFlags.getFlags());

        dc.addRetainRequestParams(DIALOG_COMMAND_RETAIN_PARAMS);

        /*
        TODO:
        if (dialog instanceof StatementDialog)
        {
            dialog.retainAllRequestParams();
            dialog.render(writer, dc, true);
        }
        else
        */

        dialog.prepareContext(dc);
        if(unitTest || (debugFlags != null && debugFlags.flagIsSet(DialogDebugFlags.SHOW_FIELD_DATA)))
            dc.setRedirectDisabled(true);

        dialog.render(writer, nc, nc.getActiveTheme(), HtmlPanel.RENDERFLAGS_DEFAULT);
    }

    public static class SkinParameter extends CommandDocumentation.Parameter
    {
        public SkinParameter()
        {
            super("dialog-skin-name", false, "The name of a DialogSkin implementation registered in the SkinFactory.");
        }

        public String[] getEnums()
        {
            //TODO: return (String[]) SkinFactory.getInstance().getDialogSkins().keySet().toArray(new String[SkinFactory.getInstance().getNavigationSkins().keySet().size()]);
            return null;
        }
    }
}
