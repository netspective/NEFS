/*
 * Created by IntelliJ IDEA.
 * User: aye
 * Date: Feb 28, 2004
 * Time: 10:45:00 PM
 */
package com.netspective.sparx.command;

import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.QueryReportPanel;
import com.netspective.sparx.panel.RecordEditorPanel;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.sql.Query;
import com.netspective.sparx.theme.Theme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

public class RecordEditorCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(RecordEditorCommand.class);
    public static final int UNLIMITED_ROWS = Integer.MAX_VALUE;
    public static final String[] IDENTIFIERS = new String[] { "record-editor-panel" };

    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays the result of a SQL query with the ability to edit/delete the displayed records and also to add additional records.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("record-editor-panel-name", true, "The fully qualified name of the record editor panel (package-name.panel-name)."),
                new CommandDocumentation.Parameter("dialog-name", true, "The name of the specific dialog for record add/edit/delete actions or 'DEFAULT' for default dialog."),
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
    /* the selected dialog name */
    private String dialogName;
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
        sb.append(dialogName != null ? dialogName : PARAMVALUE_DEFAULT);

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

        dialogName = params.nextToken();
        if(dialogName.length() == 0 || dialogName.equals(PARAMVALUE_DEFAULT))
                dialogName = null;

    }

    public String getRecordEditorPanelName()
    {
        return recordEditorPanelName;
    }

    public String getRecordAction()
    {
        return recordAction;
    }

    public String getDialogName()
    {
        return dialogName;
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

        // handle the basic display mode first
        Query query = (com.netspective.sparx.sql.Query) ePanel.getQuery();
        if(query == null)
        {
            throw new RuntimeException("Query not found in "+ this +".");
        }

        QueryReportPanel qrp = null;
        if (query.getPresentation() != null)
            qrp =  query.getPresentation().getDefaultPanel();
        if (qrp == null)
        {
            qrp = new QueryReportPanel();
            qrp.setQuery(query);
            qrp.setFrame(new HtmlPanelFrame());
            qrp.setDefault(true);
        }
        qrp.addReport(createRecordActions(nc, ePanel));
        //QueryReportPanel qrp = query.getPresentation().getDefaultPanel();
        qrp.setReportSkin("record-editor");
        qrp.render(writer, nc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
    }

    /**
     * Generate the record actions for each record
     *
     * @param nc
     * @return
     */
    protected BasicHtmlTabularReport createRecordActions(NavigationContext nc, RecordEditorPanel ePanel)
    {
        String url = nc.getActivePage().getUrl(nc);
        if (url.indexOf("?") != -1)
            url = url + "&key=${0}";
        else
            url = url + "?key=${0}";

        HtmlReportActions actions = new HtmlReportActions();
        HtmlReportAction editAction = actions.createAction();
        editAction.setTitle(new StaticValueSource("Edit"));
        editAction.setRedirect(new RedirectValueSource(url + "&recordPanel=" + ePanel.getQualifiedName() + ",edit"));
        editAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_EDIT));

        HtmlReportAction deleteAction = actions.createAction();
        deleteAction.setTitle(new StaticValueSource("Delete"));
        deleteAction.setRedirect(new RedirectValueSource(url + "&recordPanel=" + ePanel.getQualifiedName() + ",delete"));
        deleteAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_DELETE));
        actions.addAction(editAction);
        actions.addAction(deleteAction);

        BasicHtmlTabularReport report = new BasicHtmlTabularReport();
        report.addActions(actions);
        return report;
    }

}