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

package com.netspective.sparx.form.sql;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.sparx.form.DialogLoopStyle;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldFlags;
import com.netspective.sparx.form.field.DialogFieldStates;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.form.field.type.BooleanField;
import com.netspective.sparx.form.field.type.DataSourceNavigatorButtonsField;
import com.netspective.sparx.panel.QueryReportPanel;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollState;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollStates;
import com.netspective.sparx.report.tabular.HtmlTabularReportDestination;
import com.netspective.sparx.report.tabular.HtmlTabularReportDestinations;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.destination.HtmlTabularReportBrowserDestination;
import com.netspective.sparx.report.tabular.destination.HtmlTabularReportEmailDestination;
import com.netspective.sparx.report.tabular.destination.HtmlTabularReportFileDestination;
import com.netspective.sparx.sql.QueryDefinition;
import com.netspective.sparx.sql.QueryDefnSelect;

/**
 * Dialog class for executing dynamic queries defined in XML. This class is based on
 * <code>QueryBuilderDialog</code> and can handle pageable reporting by keeping track of
 * the scrolled state of the result set.
 *
 * @version $Id: QuerySelectDialog.java,v 1.14 2004-08-15 02:27:28 shahid.shah Exp $
 */
public class QuerySelectDialog extends QueryBuilderDialog
{
    static public final String QSDIALOG_QUERYDEFN_NAME_PASSTHRU_FIELDNAME = "queryDefnName";
    static public final String QSDIALOG_DIALOG_NAME_PASSTHRU_FIELDNAME = "queryDefnSelectDialogName";

    private QueryDefnSelect qdSelect = null;

    public QuerySelectDialog(Project project)
    {
        super(project);
    }

    public QuerySelectDialog(Project project, QueryDefinition queryDefn)
    {
        super(project);
        setQueryDefn(queryDefn);
        setLoop(new DialogLoopStyle(DialogLoopStyle.APPEND));
        getDialogFlags().setFlag(DialogFlags.READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA | DialogFlags.HIDE_HEADING_IN_EXEC_MODE | QueryBuilderDialogFlags.ALWAYS_SHOW_DSNAV);
    }

    public void createContents()
    {
        addInputFields();
        addOutputDestinationFields();
        addDisplayOptionsFields();
        addField(new DataSourceNavigatorButtonsField());
        addSelectedItemsField();
    }

    public void addDisplayOptionsFields()
    {
        DialogField options = new DialogField();
        options.setName("options");
        options.getFlags().setFlag(DialogFieldFlags.SHOW_CAPTION_AS_CHILD);
        addField(options);

        if(getDialogFlags().flagIsSet(QueryBuilderDialogFlags.ALLOW_DEBUG))
        {
            BooleanField debugField = new BooleanField();
            debugField.setName("debug");
            debugField.setCheckLabel(new StaticValueSource("View Generated SQL"));
            debugField.setStyle(new BooleanField.Style(BooleanField.Style.CHECK));
            options.addField(debugField);
        }
    }

    public void addInputFields()
    {
        DialogField hiddenName = new DialogField();
        hiddenName.setName(QSDIALOG_QUERYDEFN_NAME_PASSTHRU_FIELDNAME);
        hiddenName.getFlags().setFlag(DialogFieldFlags.INPUT_HIDDEN);
        addField(hiddenName);

        hiddenName = new DialogField();
        hiddenName.setName(QSDIALOG_DIALOG_NAME_PASSTHRU_FIELDNAME);
        hiddenName.getFlags().setFlag(DialogFieldFlags.INPUT_HIDDEN);
        addField(hiddenName);

    }

    /**
     * Create a query definition based select object
     */
    public QueryDefnSelect createSelect()
    {
        qdSelect = new com.netspective.sparx.sql.QueryDefnSelect(getQueryDefn());
        return qdSelect;
    }

    public void setSelect(QueryDefnSelect select)
    {
        qdSelect = select;
    }

    public DialogFlags createDialogFlags()
    {
        return new QueryBuilderDialogFlags();
    }


    public void makeStateChanges(DialogContext dc, int stage)
    {
        DialogField field = null;
        DialogFields fields = this.getFields();
        for(int i = 0; i < fields.size(); i++)
        {
            field = fields.get(i);
            field.makeStateChanges(dc, stage);
        }
        DialogFieldStates fieldStates = dc.getFieldStates();
        fieldStates.getState(QSDIALOG_QUERYDEFN_NAME_PASSTHRU_FIELDNAME).getValue().setTextValue(getQueryDefn().getName());
        fieldStates.getState(QSDIALOG_DIALOG_NAME_PASSTHRU_FIELDNAME).getValue().setTextValue(getName());
        QueryBuilderDialogFlags dFlags = (QueryBuilderDialogFlags) getDialogFlags();
        if(dc.getDialogState().isInExecuteMode())
        {
            int flag =
                    dFlags.flagIsSet(QueryBuilderDialogFlags.HIDE_CRITERIA)
                    ? DialogFieldFlags.UNAVAILABLE : DialogFieldFlags.READ_ONLY;
            for(int i = 0; i < fields.size(); i++)
            {
                fieldStates.getState(fields.get(i)).getStateFlags().setFlag(flag);
            }
            fieldStates.getState("output").getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
            if(dFlags.flagIsSet(QueryBuilderDialogFlags.ALLOW_DEBUG))
                fieldStates.getState("options").getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);

            fieldStates.getState(getDirector()).getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
            fieldStates.getState("selected_item_list").getStateFlags().clearFlag(DialogFieldFlags.UNAVAILABLE);
            fieldStates.getState("selected_item_list").getStateFlags().clearFlag(DialogFieldFlags.READ_ONLY);
            fieldStates.getState("ds_nav_buttons").getStateFlags().clearFlag(DialogFieldFlags.UNAVAILABLE);
        }
        else
        {
            fieldStates.getState("selected_item_list").getStateFlags().clearFlag(DialogFieldFlags.UNAVAILABLE);
            if(dFlags.flagIsSet(QueryBuilderDialogFlags.HIDE_OUTPUT_DESTS))
                fieldStates.getState("output").getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
            fieldStates.getState("ds_nav_buttons").getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
        }

    }

    /**
     * Handles the execution of the query select dialog
     *
     * @param writer the writer object to write any output to
     * @param dc     current dialog context
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        DialogFieldStates states = dc.getFieldStates();
        if(getDialogFlags().flagIsSet(QueryBuilderDialogFlags.ALLOW_DEBUG))
        {
            String debugStr = states.getState("options.debug").getValue().getTextValue();
            if(debugStr != null && debugStr.equals("1"))
            {
                ConnectionContext cc = null;
                try
                {
                    QueryDefnSelect select = qdSelect;
                    ValueSource dataSource = getQueryDefn().getDataSrc();
                    cc = dc.getConnection(dataSource != null ? dataSource.getTextValue(dc) : null, false);
                    String message = select.createExceptionMessage(cc, null);
                    writer.write("<p><pre><code>" + message + "</code></pre>");
                    return;
                }
                catch(Exception e)
                {
                    getLog().error("Error trying to get debug SQL", e);
                    throw new DialogExecuteException(e);
                }
                finally
                {
                    try
                    {
                        if(cc != null) cc.close();
                    }
                    catch(SQLException e)
                    {
                        getLog().error("Error while trying to close the connection", e);
                        throw new DialogExecuteException(e);
                    }
                }
            }
        }
        String outputStyleStr = getFirstAvailableFieldValue(dc, new String[]{
            "output_style", "output-style", "output.style"
        }, "0");
        String outputDestStr = getFirstAvailableFieldValue(dc, new String[]{
            "output_destination", "output-destination", "output.destination"
        }, "0");

        int outputStyle = Integer.parseInt(outputStyleStr);
        int outputDest = Integer.parseInt(outputDestStr);

        HtmlTabularReportDestinations desintations = HtmlTabularReportDestinations.getInstance();
        HtmlTabularReportDestination destination = null;
        HtmlTabularReportSkin reportSkin = null;

        switch(outputDest)
        {
            case DESTINATION_BROWSER:
                HtmlTabularReportBrowserDestination browserDest = desintations.createBrowserDestination(writer, dc);
                destination = browserDest;
                break;

            case DESTINATION_BROWSER_PAGED:
                String rowsPerPageStr = getFirstAvailableFieldValue(dc, new String[]{
                    "rows_per_page", "rows-per-page", "output.rows_per_page"
                }, "20");
                browserDest = desintations.createBrowserDestination(writer, dc, Integer.parseInt(rowsPerPageStr));
                destination = browserDest;
                break;

            case DESTINATION_DOWNLOAD_FILE:
                HtmlTabularReportFileDestination fileDest = desintations.createDownloadableFileDestination();
                destination = fileDest;
                break;

            case DESTINATION_EMAIL:
                HtmlTabularReportEmailDestination emailDest = desintations.createEmailDestination();
                destination = emailDest;
                break;

            default:
                writer.write("Destination" + outputDest + " is unknown.");
                return;
        }

        switch(outputStyle)
        {
            case OUTPUTSTYLE_HTML:
                String reportSkinName = getFirstAvailableFieldValue(dc, new String[]{
                    "report_skin", "report-skin", "output.report_skin"
                }, "report");
                reportSkin = dc.getActiveTheme().getReportSkin(reportSkinName);
                break;

            case OUTPUTSTYLE_TEXT_CSV:
                reportSkin = dc.getActiveTheme().getReportSkin("text-csv");
                break;

            case OUTPUTSTYLE_TEXT_TAB:
                reportSkin = dc.getActiveTheme().getReportSkin("text-tab");
                break;

            default:
                writer.write("Output Style " + outputStyle + " is unknown.");
                return;
        }

        try
        {
            renderReport(dc, destination, reportSkin);
        }
        catch(Exception e)
        {
            getLog().error("Exception while trying to render report", e);
            throw new DialogExecuteException(e);
        }
    }

    /**
     * Renders the report output of the query executed by the dialog.
     *
     * @param dc          curent dialog context in which the query was executed
     * @param destination the output format such as html, csv, etc
     * @param reportSkin  the skin for the output
     */
    public void renderReport(DialogContext dc, HtmlTabularReportDestination destination, HtmlTabularReportSkin reportSkin) throws IOException, QueryDefinitionException
    {
        QueryReportPanel reportPanel = null;

        if(destination instanceof HtmlTabularReportBrowserDestination)
        {
            HtmlTabularReportBrowserDestination browserDest = (HtmlTabularReportBrowserDestination) destination;
            HtmlTabularReportDataSourceScrollStates scrollStatesManager = dc.getProject().getScrollStates();
            HtmlTabularReportDataSourceScrollState scrollStateById = scrollStatesManager.getScrollStateByDialogTransactionId(dc);

            if(scrollStateById == null)
            {
                // if our transaction does not have a scroll state, but there is an active scroll state available, then it
                // means that we need to close the previous one and remove the attribute so that the connection can be
                // closed and returned to the pool
                HtmlTabularReportDataSourceScrollState activeScrollState = scrollStatesManager.getActiveScrollState(dc);

                String resortBy = dc.getRequest().getParameter(QBDIALOG_RESORT_PARAMNAME);
                if(activeScrollState != null && resortBy != null)
                {
                    // TODO
                    //handleSortOrderChange(dc, activeState, resortBy);
                }

                if(activeScrollState != null && !getDialogFlags().flagIsSet(QueryBuilderDialogFlags.ALLOW_MULTIPLE_SCROLL_STATES))
                    scrollStatesManager.removeActiveState(dc, activeScrollState);

                reportPanel = qdSelect.getPresentation().getDefaultPanel();
                reportPanel.setQuery(qdSelect);
                if(browserDest.isScrollable())
                {
                    reportPanel.setScrollable(true);
                    reportPanel.setScrollRowsPerPage(browserDest.getPageSize());
                }
            }
            else
            {
                reportPanel = (QueryReportPanel) scrollStateById.getPanel();
            }
        }
        else
        {
            //QueryDefnSelect select = createSelect(dc);
            reportPanel = qdSelect.getPresentation().getDefaultPanel();
            reportPanel.setQuery(qdSelect);
        }

        destination.render(reportPanel, reportSkin);
    }

}
