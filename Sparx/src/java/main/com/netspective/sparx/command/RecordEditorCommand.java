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

package com.netspective.sparx.command;

import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.RecordEditorPanel;
import com.netspective.sparx.theme.Theme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * Class for handling the record-editor-panel command
 *
 *
 * @version $Id: RecordEditorCommand.java,v 1.3 2004-03-02 07:43:20 aye.thu Exp $
 */
public class RecordEditorCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(RecordEditorCommand.class);


    public static final String RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME = "record-editor";
    public static final String[] IDENTIFIERS = new String[] { "record-editor-panel" };

    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays the result of a SQL query with the ability to edit/delete the displayed records and also to add additional records.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("record-editor-panel-name", true, "The fully qualified name of the record editor panel (package-name.panel-name)."),
                new CommandDocumentation.Parameter("command-action", false, "The action to perform for the selected record."),
                new CommandDocumentation.Parameter("record-key", false, "The primary key of the selected record."),
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

    /* the name of the record editor panel */
    private String recordEditorPanelName;
    private String recordKey;
    /* the action to perform */
    private String recordAction;

    /**
     * Sole constructor
     */
    public RecordEditorCommand()
    {
        super();
    }

    /**
     * Gets the parameters for the command
     *
     * @return
     */
    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(recordEditorPanelName);
        sb.append(delim);
        sb.append(recordAction);
        sb.append(delim);
        sb.append(recordKey);

        return sb.toString();
    }

    /**
     * Sets the parameters for the command
     *
     * @param params
     */
    public void setParameters(StringTokenizer params)
    {
        recordEditorPanelName = params.nextToken();

        if (params.hasMoreTokens())
            recordAction = params.nextToken();
        if (params.hasMoreTokens())
            recordKey = params.nextToken();
    }

    public String getRecordEditorPanelName()
    {
        return recordEditorPanelName;
    }

    public String getRecordAction()
    {
        return recordAction;
    }

    /**
     * Calculate the mode the record editor panel is in
     *
     * @return
     */
    public int calculateEditorPanelMode()
    {
        int mode = RecordEditorPanel.UNKNOWN_MODE;
        if (recordAction == null)
        {
            mode = RecordEditorPanel.DEFAULT_DISPLAY_MODE;
        }
        else if (recordAction.equals("add"))
        {
            mode = RecordEditorPanel.ADD_RECORD_DISPLAY_MODE;
        }
        else if (recordAction.equals("edit") && recordKey != null)
        {
            mode = RecordEditorPanel.EDIT_RECORD_DISPLAY_MODE;
        }
        else if (recordAction.equals("delete") && recordKey != null)
        {
            mode = RecordEditorPanel.DELETE_RECORD_DISPLAY_MODE;
        }
        else if (recordAction.equals("manage"))
        {
            mode = RecordEditorPanel.MANAGE_RECORDS_DISPLAY_MODE;
        }

        return mode;
    }

    /**
     *
     *
     * @param writer
     * @param nc
     * @param unitTest
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        RecordEditorPanel ePanel = nc.getProject().getRecordEditorPanel(getRecordEditorPanelName());
        if (ePanel == null)
        {
             throw new RuntimeException("Record editor panel '"+ getRecordEditorPanelName() + "' not found in "+ this +".");
        }
        Theme theme = nc.getActiveTheme();
        int mode = calculateEditorPanelMode();

        if (mode == RecordEditorPanel.UNKNOWN_MODE)
        {
            log.error("Unexpected mode encountered for the record editor panel '" + getRecordEditorPanelName() + "'.");
            throw new RuntimeException("Unexpected mode encountered for the record editor panel '" + getRecordEditorPanelName() + "'.");
        }

        if (mode == RecordEditorPanel.ADD_RECORD_DISPLAY_MODE || mode == RecordEditorPanel.EDIT_RECORD_DISPLAY_MODE ||
            mode == RecordEditorPanel.DELETE_RECORD_DISPLAY_MODE)
        {
            // record action was defined so we need to display the requested display mode
            Dialog dialog = ePanel.getDialog();
            if (dialog == null)
            {
                throw new RuntimeException("Dialog defined in record editor panel '"+ getRecordEditorPanelName() + "' cannot be found.");
            }
            DialogContext dc = dialog.createContext(nc, theme.getDefaultDialogSkin());
            dc.addRetainRequestParams(DialogCommand.DIALOG_COMMAND_RETAIN_PARAMS);
            dialog.prepareContext(dc);
            try
            {
                dialog.render(writer, dc, true);
            }
            catch (DialogExecuteException e)
            {
                log.error("Unable to execute dialog", e);
                throw new CommandException(e, this);
            }
        }
        nc.setAttribute(RecordEditorPanel.DISPLAY_MODE_CONTEXT_ATTRIBUTE, new Integer(mode));
        ePanel.render(writer, nc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);

    }


}