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

import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.ResultSetUtils;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XdmHandler;
import com.netspective.commons.xdm.XdmHandlerNodeStackEntry;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xml.NodeIdentifiers;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateApplyContext;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.TemplateElement;
import com.netspective.commons.xml.template.TemplateNode;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.report.tabular.column.ClobColumn;
import com.netspective.commons.report.tabular.TabularReportColumns;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.sparx.Project;
import com.netspective.sparx.panel.QueryReportPanel;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogDebugFlags;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.DialogSkin;
import com.netspective.sparx.form.QueryResultDialog;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.form.field.DialogFieldStates;
import com.netspective.sparx.form.field.type.GridField;
import com.netspective.sparx.form.field.type.GridFieldRow;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.theme.Theme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Command class for creating and invoking a dynamic dialog whose field contents are populated from the result
 * of a database query.
 *
 * @see com.netspective.sparx.form.QueryResultDialog
 */
public class QueryResultDialogCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(QueryResultDialogCommand.class);

    public static final String[] IDENTIFIERS = new String[]{"dynamic-dialog"};
    public static final String[] DIALOG_COMMAND_RETAIN_PARAMS = { PAGE_COMMAND_REQUEST_PARAM_NAME };
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation("Displays and executes a dialog box.",
                                                                                      new CommandDocumentation.Parameter[]
                                                                                      {
                                                                                          new CommandDocumentation.Parameter("dialog-type-name", true, "The fully qualified name of the dialog (package-name.dialog-name)"),
                                                                                          new CommandDocumentation.Parameter("dialog-perspective", false, new DialogPerspectives(), null, "The dialog perspective to send to DialogContext."),
                                                                                          new CommandDocumentation.Parameter("dialog-skin-name", false, ""),
                                                                                          new CommandDocumentation.Parameter("debug-flags", false, new DialogDebugFlags(), null, "The debug flags.")
                                                                                      });


    /**
     * Gets the command names for invoking the command
     *
     * @return  list of names
     */
    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    /**
     * Gets the documentation object associated with the command
     *
     * @return
     */
    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private String dialogTypeName;
    private DialogPerspectives perspective;
    private String skinName;
    private DialogDebugFlags debugFlags;
    private String queryName;

    public QueryResultDialogCommand()
    {
        super();
    }

    /**
     * Gets the dialog type template name
     *
     * @return
     */
    public String getDialogTypeName()
    {
        return dialogTypeName;
    }

    /**
     * Sets the dialog type template name
     *
     * @param dialogTypeName
     */
    public void setDialogTypeName(String dialogTypeName)
    {
        this.dialogTypeName = dialogTypeName;
    }

    /**
     * Gets the command parameters
     *
     * @return
     */
    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(dialogTypeName);
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

    /**
     * Sets the command parameters
     *
     * @param params
     */
    public void setParameters(StringTokenizer params)
    {
        dialogTypeName = params.nextToken();

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

    /**
     * Executes the query associated with the dynamic dialog
     *
     * @param nc    current navigation context
     * @return      a two dimensional array of data
     */
    protected Object[][] executeQuery(NavigationContext nc)
    {
        com.netspective.sparx.sql.Query query = (com.netspective.sparx.sql.Query) nc.getSqlManager().getQuery(queryName);
        QueryReportPanel panel = query.getPresentation().getDefaultPanel();
        TabularReportDataSource dataSource = panel.createDataSource(nc);
        HtmlTabularReport report = panel.getReport();

        TabularReportColumns columns = report.getColumns();

        String datasrc = query.getDataSrc() != null ? query.getDataSrc().getTextValue(nc) : null;

        Object[][] resultArray = null;
        Connection conn = null;
        try
        {
            //conn = nc.getConnection(datasrc, false).getConnection();
            resultArray = nc.getSqlManager().executeQueryAndGetMatrix(nc.getConnection(datasrc, false), queryName, null, true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error(e);
        }
        finally
        {
            if (conn != null)
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                log.error(e);
            }
        }
        return resultArray;
    }


    /**
     * Constructs a new dialog instance
     *
     * @param nc
     * @param dialogTypeName
     * @return
     */
    public Dialog createDynamicDialog(NavigationContext nc,  String dialogTypeName)
    {
        Dialog dialog = null;
        Project.DialogTypeTemplate dialogTypes = nc.getProject().getDialogTypes();
        Template template = (Template) dialogTypes.getInstancesMap().get(dialogTypeName);
        queryName = template.getAttributes().getValue("query");

        ((XdmHandler)template.getDefnContentHandler()).getParseContext().setTemplateCatalog(template.getTemplateCatalog());
            //dialog = (Dialog) template.getClass().newInstance();
        try
        {
            dialog = (Dialog) Class.forName(template.getAlternateClassName()).newInstance();
        }
        catch (Exception e)
        {
            log.error("Failed to instantiate dynamic dialog type '"+ dialogTypeName + "'", e);
            return null;
        }

        dialog.setName(dialogTypeName);
        dialog.getDialogFlags().setFlag(DialogFlags.RETAIN_ALL_REQUEST_PARAMS);
        XdmHandlerNodeStackEntry dialogEntry = new XdmHandlerNodeStackEntry(template.getElementName(), template.getAttributes(), dialog);
        template.getDefnContentHandler().getNodeStack().push(dialogEntry);
        TemplateConsumerDefn dialogTmplConsumer = dialog.getTemplateConsumerDefn();
        List consumeTemplates = new ArrayList();
        consumeTemplates.add(template);

        try
        {
            dialogTmplConsumer.consume(template.getDefnContentHandler(), consumeTemplates, template.getElementName(), template.getAttributes());
        }
        catch (SAXException e)
        {
            log.error("Failed to consume the dynamic dialog type '" + dialogTypeName + "'", e);
            return null;
        }

        return dialog;
    }

    /**
     *
     * @param nc
     * @param dialog
     * @param rowCount
     * @throws SAXException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws DataModelException
     */
    private void createDialogFields(NavigationContext nc, Dialog dialog, int rowCount)
            throws SAXException, InstantiationException, IllegalAccessException, ClassNotFoundException, DataModelException
    {
        QueryResultDialog.RowFieldTemplate rowFieldTemplate = ((QueryResultDialog) dialog).getRowFieldTemplate();

        TemplateProducer templateProducer = ((QueryResultDialog)dialog).getTemplateProducers().getByIndex(0);
        final List templateInstances = templateProducer.getInstances();
        if(templateInstances.size() == 0)
        {
            log.error("There were no <row> template registered.");
        }

        // make sure to get the last template only because inheritance may have create multiples
        Template rowTemplate = (Template) templateInstances.get(templateInstances.size() - 1);
        List childFieldElements = rowTemplate.getChildren();


        DialogField field = null;
        int fieldCount = childFieldElements.size();

        GridField gridField = new GridField();
        gridField.setName(QueryResultDialog.GRID_FIELD_PREFIX);
        dialog.addGrid(gridField);

        Project.DialogFieldTypeTemplate fieldTypesTemplate = nc.getProject().getFieldTypes();
        Map fieldTypeInstancesMap = fieldTypesTemplate.getInstancesMap();
        for (int j=0; j < rowCount; j++)
        {
            GridFieldRow row = new GridFieldRow();
            row.setName(QueryResultDialog.ROW_FIELD_PREFIX + j);
            gridField.addField(row);

            for (int k=0; k < fieldCount; k++)
            {
                TemplateNode childFieldNode = (TemplateNode) childFieldElements.get(k);
                if(childFieldNode instanceof TemplateElement)
                {
                    TemplateElement fieldElement = (TemplateElement) childFieldNode;
                    String elementName = fieldElement.getElementName();
                    Attributes attributes = fieldElement.getAttributes();
                    String alternateClassName = attributes.getValue(NodeIdentifiers.ATTRNAME_ALTERNATE_CLASS_NAME);

                    String fieldType = fieldElement.getAttributes().getValue("type");
                    Template fieldTemplate = (Template) fieldTypeInstancesMap.get(fieldType);
                    XdmHandler xdmHandler = ((XdmHandler) fieldTemplate.getDefnContentHandler());
                    XdmParseContext pContext = (XdmParseContext) xdmHandler.getParseContext();
                    TemplateApplyContext applyContext = fieldTemplate.createApplyContext(fieldTemplate.getDefnContentHandler(),
                            fieldElement.getElementName(), fieldElement.getAttributes());

                    // can't do a XmlDataModelSchema.createElement() since the <row> element doesn't have an actual
                    // class implementation that would have had an createField() method so do a manual instantiation
                    // of the dialog field class
                    field = (DialogField) Class.forName(fieldTemplate.getAlternateClassName()).newInstance();
                    TemplateConsumerDefn fieldTemplateConsumer = field.getTemplateConsumerDefn();

                    // see if we have any templates that need to be applied                    
                    XdmHandlerNodeStackEntry childEntry = new XdmHandlerNodeStackEntry(elementName, attributes, field);
                    xdmHandler.getNodeStack().push(childEntry);
                    String[] attrNamesToSet = fieldTemplateConsumer.getAttrNamesToApplyBeforeConsuming();
                    if(attrNamesToSet != null)
                    {
                        for(int i = 0; i < attrNamesToSet.length; i++)
                        {
                            String attrValue = attributes.getValue(attrNamesToSet[i]);
                            String lowerCaseAttrName = attrNamesToSet[i].toLowerCase();
                            childEntry.getSchema().setAttribute(((XdmParseContext) xdmHandler.getParseContext()), childEntry.getInstance(),
                                    lowerCaseAttrName, attrValue, false);
                        }
                    }

                    // create the temporary list and add the dialog-field-type template to pass to the consume call
                    List templatesToConsume = new ArrayList();
                    templatesToConsume.add(fieldTemplate);
                    fieldTemplateConsumer.consume(fieldTemplate.getDefnContentHandler(), templatesToConsume, fieldElement.getElementName(),
                            fieldElement.getAttributes());
                    for(int i = 0; i < attributes.getLength(); i++)
                    {
                        String origAttrName = attributes.getQName(i);
                        String lowerCaseAttrName = origAttrName.toLowerCase();
                        if(!childEntry.getOptions().ignoreAttribute(lowerCaseAttrName) && !lowerCaseAttrName.equals(NodeIdentifiers.ATTRNAME_ALTERNATE_CLASS_NAME) &&
                           !(xdmHandler.getNodeStack().size() < 3 && lowerCaseAttrName.startsWith(NodeIdentifiers.ATTRNAMEPREFIX_NAMESPACE_BINDING)) &&
                           !(fieldTemplateConsumer != null && fieldTemplateConsumer.isTemplateAttribute(origAttrName)) &&
                           !(origAttrName.startsWith(xdmHandler.getNodeIdentifiers().getXmlNameSpacePrefix())))

                            childEntry.getSchema().setAttribute(pContext, childEntry.getInstance(), lowerCaseAttrName,
                                    attributes.getValue(i), false);
                    }

                    row.addField(field);
                }
            }
        }
    }

    /**
     * Handles the dialog command. Constructs a new instance of the dialog, adds the children fields, and
     * then populates the fields using the resultset from the query.
     *
     * @param writer    response buffer object
     * @param nc        current navigation context
     * @param unitTest  flag indicating if this invocation is for a console unit test
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {

        QueryResultDialog dialog = (QueryResultDialog) createDynamicDialog(nc, dialogTypeName);
        if (dialog == null)
            throw new CommandException("Failed to create dialog.", this);

        Theme theme = nc.getActiveTheme();
        DialogSkin skin = skinName != null ? theme.getDialogSkin(skinName) : theme.getDefaultDialogSkin();
        if(skin == null)
        {
            writer.write("DialogSkin '" + skinName + "' not found in skin factory.");
            return;
        }

        DialogContext dc = dialog.createContext(nc, skin);
        Object[][] resultArray = executeQuery(nc);
        try
        {
            createDialogFields(nc, dialog, resultArray.length);
        }
        catch (Exception e)
        {
            log.error("Failed to add dialog fields to the dynamic dialog instance.", e);
            throw new CommandException("Failed to add dialog fields to the dynamic dialog instance.", e, this);
        }
        if (dc.getDialogState().isInitialEntry())
        {
            // populate the dialog with the query values!
            GridField field = (GridField) dialog.getFields().getByName(QueryResultDialog.GRID_FIELD_PREFIX);
            DialogFields children = field.getChildren();
            DialogFieldStates fieldStates = dc.getFieldStates();
            if (children != null && children.size() > 0)
            {
                for (int i=0; i < resultArray.length; i++)
                {
                    GridFieldRow row = (GridFieldRow) children.getByName(QueryResultDialog.ROW_FIELD_PREFIX + i);
                    DialogFields rowFields = row.getChildren();
                    for (int k=0; k < resultArray[i].length ; k++)
                    {
                        if (resultArray[i][k] != null)
                        {
                            rowFields.get(k).setDefault(new StaticValueSource(resultArray[i][k].toString()));
                            DialogField.State state = fieldStates.getState(rowFields.get(k));

                            if (resultArray[i][k] instanceof java.sql.Date)
                            {
                                state.getValue().setValue(resultArray[i][k]);
                            }
                            else if (resultArray[i][k] instanceof java.sql.Clob)
                            {
                                try
                                {
                                    state.getValue().setTextValue(ClobColumn.getClobAsString((java.sql.Clob)resultArray[i][k]));
                                }
                                catch(Exception e)
                                {
                                    log.error(e);
                                    state.getValue().setTextValue("Failed to extract CLOB value.");
                                }
                            }
                            else
                            {
                                state.getValue().setTextValue(resultArray[i][k].toString());
                            }
                        }
                    }
                }
            }
        }
        // Populate the dialog before preparing the context!
        dialog.prepareContext(dc);

        try
        {
            dialog.render(writer, dc, true);
        }
        catch(DialogExecuteException e)
        {
            log.error("Unable to execute dialog", e);
            throw new CommandException(e, this);
        }
    }
}
