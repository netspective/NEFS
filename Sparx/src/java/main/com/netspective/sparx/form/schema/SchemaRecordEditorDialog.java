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
 * $Id: SchemaRecordEditorDialog.java,v 1.1 2003-10-16 17:10:54 shahid.shah Exp $
 */

package com.netspective.sparx.form.schema;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateElement;
import com.netspective.commons.text.TextUtils;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogContextUtils;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.handler.DialogExecuteHandlers;

public class SchemaRecordEditorDialog extends Dialog implements TemplateProducerParent
{
    public static final String PARAMNAME_PRIMARYKEY = "pk";

    protected class InsertDataTemplate extends TemplateProducer
    {
        public InsertDataTemplate()
        {
            super("/dialog/" + getQualifiedName() + "/on-add-data", "on-add-data", null, null, false, false);
        }

        public String getTemplateName(String url, String localName, String qName, Attributes attributes) throws SAXException
        {
            return getName();
        }
    }

    protected class EditDataTemplate extends TemplateProducer
    {
        public EditDataTemplate()
        {
            super("/dialog/" + getQualifiedName() + "/on-edit-data", "on-edit-data", null, null, false, false);
        }

        public String getTemplateName(String url, String localName, String qName, Attributes attributes) throws SAXException
        {
            return getName();
        }
    }

    private ValueSource dataSrc;
    private ValueSource primaryKeyValueForEditOrDelete;
    private InsertDataTemplate insertDataTemplateProducer;
    private EditDataTemplate editDataTemplateProducer;
    private TemplateProducers templateProducers;

    public SchemaRecordEditorDialog()
    {
        setDialogContextClass(SchemaRecordEditorDialogContext.class);
    }

    public SchemaRecordEditorDialog(DialogsPackage pkg)
    {
        super(pkg);
        setDialogContextClass(SchemaRecordEditorDialogContext.class);
    }

    public TemplateProducers getTemplateProducers()
    {
        if(templateProducers == null)
        {
            templateProducers = new TemplateProducers();
            insertDataTemplateProducer = new InsertDataTemplate();
            editDataTemplateProducer = new EditDataTemplate();
            templateProducers.add(insertDataTemplateProducer);
            templateProducers.add(editDataTemplateProducer);
        }
        return templateProducers;
    }

    public DialogFlags createDialogFlags()
    {
        return new SchemaRecordEditorDialogFlags();
    }

    public ValueSource getDataSrc()
    {
        return dataSrc;
    }

    public void setDataSrc(ValueSource dataSrc)
    {
        this.dataSrc = dataSrc;
    }

    public ValueSource getPrimaryKeyValueForEditOrDelete()
    {
        return primaryKeyValueForEditOrDelete;
    }

    public void setPrimaryKeyValueForEditOrDelete(ValueSource primaryKeyValueForEditOrDelete)
    {
        this.primaryKeyValueForEditOrDelete = primaryKeyValueForEditOrDelete;
    }

    public void populateFieldValuesUsingAttributes(SchemaRecordEditorDialogContext tdc, Row row, TemplateElement templateElement)
    {
        DialogContext.DialogFieldStates states = tdc.getFieldStates();
        ColumnValues columnValues = row.getColumnValues();

        Attributes templateAttributes = templateElement.getAttributes();
        for(int i = 0; i < templateAttributes.getLength(); i++)
        {
            String columnName = templateAttributes.getQName(i);
            String columnTextValue = templateAttributes.getValue(i);

            // these are private "instructions"
            if(columnName.startsWith("_"))
                continue;

            ColumnValue columnValue = columnValues.getByNameOrXmlNodeName(columnName);
            if(columnValue == null)
            {
                tdc.getDialog().getLog().error("Table '"+ row.getTable().getName() +"' does not have a column named '"+ columnName +"'.");
                continue;
            }

            // if the column value is a value source spec, we don't map the value to a field
            ValueSource vs = ValueSources.getInstance().getValueSource(ValueSources.createSpecification(columnTextValue), ValueSources.VSNOTFOUNDHANDLER_NULL);
            if(vs == null)
            {
                DialogField.State fieldState = states.getState(columnTextValue, null);
                if(fieldState != null)
                    fieldState.getValue().copyValueByReference(columnValue);
                else
                    tdc.getDialog().getLog().error("Table '"+ row.getTable().getName() +"' is mapping a column called '"+ columnName +"' to the field '"+ columnTextValue +"' but that field was not found.");
            }
        }
    }

    public void autoMapColumnNamesToFieldNames(SchemaRecordEditorDialogContext dc, Table table, TemplateElement templateElement)
    {
        String autoMapAttrValue = templateElement.getAttributes().getValue("_auto-map");
        if(autoMapAttrValue == null && autoMapAttrValue.length() == 0)
            return;

        AttributesImpl attrs = new AttributesImpl(templateElement.getAttributes());
        attrs.removeAttribute(attrs.getIndex("_auto-map"));

        if(autoMapAttrValue.equals("*"))
        {
            Columns columns = table.getColumns();
            for(int i = 0; i < columns.size(); i++)
            {
                Column column = columns.get(i);
                attrs.addAttribute(null, null, column.getXmlNodeName(), "CDATA", column.getXmlNodeName());
            }
        }
        else
        {
            String[] columnNames = TextUtils.split(autoMapAttrValue, ",", true);
            for(int i = 0; i < columnNames.length; i++)
                attrs.addAttribute(null, null, columnNames[i], "CDATA", columnNames[i]);
        }

        templateElement.setAttributes(attrs);
    }

    public void populateDataUsingTemplateElement(SchemaRecordEditorDialogContext dc, ConnectionContext cc, TemplateElement templateElement) throws NamingException, SQLException
    {
        Schema schema = null;
        String tableName = null;

        String[] tableNameParts = TextUtils.split(templateElement.getElementName(), ".", false);
        if(tableNameParts.length == 1)
        {
            Table bindTable = getBindTable();
            schema = bindTable != null ? bindTable.getSchema() : dc.getProject().getSchemas().get(0);
            tableName = tableNameParts[0];
        }
        else
        {
            schema = dc.getProject().getSchemas().getByNameOrXmlNodeName(tableNameParts[0]);
            if(schema == null)
            {
                dc.getDialog().getLog().error("Unable to find schema '"+ tableNameParts[0] +"' in SchemaRecordEditorDialog '"+ getQualifiedName() +"'");
            }

            tableName = tableNameParts[1];
        }

        Table table = schema.getTables().getByNameOrXmlNodeName(tableName);
        if(table == null)
        {
            dc.getDialog().getLog().error("Unable to find table '"+ tableName +"' in schema '"+ schema.getName() +"' for SchemaRecordEditorDialog '"+ getQualifiedName() +"'");
            return;
        }

        autoMapColumnNamesToFieldNames(dc, table,  templateElement);

        // see if a primary key value is provided -- if it is, we're going to populate using the primary key value
        String primaryKeyValue = templateElement.getAttributes().getValue("_primary-key-value");
        if(primaryKeyValue == null || primaryKeyValue.length() == 0)
        {
            primaryKeyValue = templateElement.getAttributes().getValue(table.getPrimaryKeyColumns().getSole().getXmlNodeName());
            if(primaryKeyValue == null || primaryKeyValue.length() == 0)
                primaryKeyValue = templateElement.getAttributes().getValue(table.getPrimaryKeyColumns().getSole().getName());
        }
        if(primaryKeyValue != null && primaryKeyValue.length() > 0)
        {
            ValueSource pkValue = ValueSources.getInstance().getValueSourceOrStatic(primaryKeyValue);
            Row activeRow = table.getRowByPrimaryKeys(cc, new Object[] { pkValue.getValue(dc).getValue() }, null);
            if(activeRow != null)
                populateFieldValuesUsingAttributes(dc, activeRow, templateElement);
            else
                dc.getValidationContext().addValidationError("Unable to locate primary key using value {0} in table {1}.", new Object[] { pkValue, table.getName() });
        }

        // TODO: other alternatives for population like _accessor or _query-id should be provided so that programmers
        //       can choose how to populate their tables if they're using something other than primary keys
    }

    public void populateDataUsingTemplate(SchemaRecordEditorDialogContext tdc, ConnectionContext cc) throws NamingException, SQLException
    {
        if(editDataTemplateProducer == null)
            return;

        if(editDataTemplateProducer.getInstances().size() == 0)
            return;

        Template insertAdditionalDataTemplate = (Template) editDataTemplateProducer.getInstances().get(0);
        List childTableElements = insertAdditionalDataTemplate.getChildren();
        for(int i = 0; i < childTableElements.size(); i++)
        {
            TemplateElement childTableElement = (TemplateElement) childTableElements.get(i);
            populateDataUsingTemplateElement(tdc, cc, childTableElement);
        }
    }

    public void populateValues(DialogContext dc, int formatType)
    {
        if(dc.isInitialEntry())
        {
            if(!dc.addingData())
            {
                try
                {
                    if(editDataTemplateProducer == null)
                        return;

                    if(editDataTemplateProducer.getInstances().size() == 0)
                        return;

                    ConnectionContext cc = dc.getConnection(dataSrc != null ? dataSrc.getTextValue(dc) : null, false);
                    Template insertAdditionalDataTemplate = (Template) editDataTemplateProducer.getInstances().get(0);
                    List childTableElements = insertAdditionalDataTemplate.getChildren();
                    for(int i = 0; i < childTableElements.size(); i++)
                    {
                        TemplateElement childTableElement = (TemplateElement) childTableElements.get(i);
                        populateDataUsingTemplateElement((SchemaRecordEditorDialogContext) dc, cc, childTableElement);
                    }
                }
                catch (Exception e)
                {
                    dc.getDialog().getLog().error("Error in populateValues", e);
                }
            }
        }

        super.populateValues(dc, formatType);
    }

    public boolean isConditionalExpressionTrue(SchemaRecordEditorDialogContext tdc, String exprText)
    {
        Object result = null;
        try
        {
            Expression e = ExpressionFactory.createExpression(exprText);
            JexlContext jc = JexlHelper.createContext();
            Map vars = new HashMap();
            vars.put("vc", tdc);
            jc.setVars(vars);
            result = e.evaluate(jc);

            if(result instanceof Boolean)
                return ((Boolean) result).booleanValue();
            else
                return false;
        }
        catch (Exception e)
        {
            tdc.getDialog().getLog().error("Unable to evaluate '"+ exprText +"': ", e);
            return false;
        }
    }

    public void insertDataUsingTemplateElement(SchemaRecordEditorDialogContext tdc, ConnectionContext cc, TemplateElement templateElement, Row parentRow) throws SQLException
    {
        Schema schema = null;
        String tableName = null;

        String[] tableNameParts = TextUtils.split(templateElement.getElementName(), ".", false);
        if(tableNameParts.length == 1)
        {
            Table bindTable = getBindTable();
            schema = bindTable != null ? bindTable.getSchema() : tdc.getProject().getSchemas().get(0);
            tableName = tableNameParts[0];
        }
        else
        {
            schema = tdc.getProject().getSchemas().getByNameOrXmlNodeName(tableNameParts[0]);
            if(schema == null)
            {
                tdc.getDialog().getLog().error("Unable to find schema '"+ tableNameParts[0] +"' in SchemaRecordEditorDialog '"+ getQualifiedName() +"'");
            }

            tableName = tableNameParts[1];
        }

        Table table = schema.getTables().getByNameOrXmlNodeName(tableName);
        if(table == null)
        {
            tdc.getDialog().getLog().error("Unable to find table '"+ tableName +"' in schema '"+ schema.getName() +"' for SchemaRecordEditorDialog '"+ getQualifiedName() +"'");
            return;
        }

        Row activeRow = null;

        // find the connector from the child table to the parent table if one is available
        Columns parentKeyCols = table.getForeignKeyColumns(ForeignKey.FKEYTYPE_PARENT);
        if(parentKeyCols.size() == 1)
        {
            Column connector = parentKeyCols.getSole();
            activeRow = table.createRow((ParentForeignKey) connector.getForeignKey(), parentRow);
            System.out.println("Parent key");
        }
        else
            activeRow = table.createRow();

        ColumnValues columnValues = activeRow.getColumnValues();
        boolean doInsert = true;

        // each of the attributes provided in the template are supposed to column-name="column-value" where
        // column-value may be a static string which refers to a dialog field name or a value source specification
        // which refers to some dynamic value
        Attributes templateAttributes = templateElement.getAttributes();
        for(int i = 0; i < templateAttributes.getLength(); i++)
        {
            String columnName = templateAttributes.getQName(i);
            String columnTextValue = templateAttributes.getValue(i);

            // if we have an attribute called _condition then it's a JSTL-style expression that should return true if
            // we want to do this insert or false if we don't
            if(columnName.equalsIgnoreCase("_condition"))
            {
                doInsert = isConditionalExpressionTrue(tdc, columnTextValue);
                if(! doInsert)
                    break; // don't bother setting values since we're not inserting
            }

            // these are private "instructions"
            if(columnName.startsWith("_"))
                continue;

            ColumnValue columnValue = columnValues.getByNameOrXmlNodeName(columnName);
            if(columnValue == null)
            {
                tdc.getDialog().getLog().error("Table '"+ table.getName() +"' does not have a column named '"+ columnName +"'.");
                continue;
            }

            // if the column value is a value source spec, we get the value from the VS otherwise it's a field name in the active dialog
            ValueSource vs = ValueSources.getInstance().getValueSource(ValueSources.createSpecification(columnTextValue), ValueSources.VSNOTFOUNDHANDLER_NULL);
            if(vs == null)
                DialogContextUtils.getInstance().populateColumnValueWithFieldValue(tdc, columnValue, columnTextValue);
            else
                columnValue.setTextValue(vs.getTextValue(tdc));
        }

        if(doInsert)
        {
            table.insert(cc, activeRow);

            // now recursively add children if any are available
            List childTableElements = templateElement.getChildren();
            for(int i = 0; i < childTableElements.size(); i++)
            {
                TemplateElement childTableElement = (TemplateElement) childTableElements.get(i);
                insertDataUsingTemplateElement(tdc, cc, childTableElement, activeRow);
            }
        }
    }

    public void insertDataUsingTemplate(SchemaRecordEditorDialogContext tdc, ConnectionContext cc) throws SQLException
    {
        if(insertDataTemplateProducer == null)
            return;

        if(insertDataTemplateProducer.getInstances().size() == 0)
            return;

        Template insertAdditionalDataTemplate = (Template) insertDataTemplateProducer.getInstances().get(0);
        List childTableElements = insertAdditionalDataTemplate.getChildren();
        for(int i = 0; i < childTableElements.size(); i++)
        {
            TemplateElement childTableElement = (TemplateElement) childTableElements.get(i);
            insertDataUsingTemplateElement(tdc, cc, childTableElement, tdc.getPrimaryTableRow());
        }
    }

    public void execute(Writer writer, DialogContext dc) throws IOException
    {
        if(dc.executeStageHandled())
            return;

        SchemaRecordEditorDialogContext tdc = ((SchemaRecordEditorDialogContext) dc);

        ConnectionContext cc = null;
        DialogExecuteHandlers handlers = getExecuteHandlers();
        try
        {
            // open the connection with the selected shared mode. The default mode
            // is no sharing.
            // cc = dc.getSharedConnection(dataSrc != null ? dataSrc.getTextValue(dc) : null, true,
            //        getConnectionShareType().getValueIndex());
            cc = dc.getConnection(dataSrc != null ? dataSrc.getTextValue(dc) : null, true);
        }
        catch (Exception e)
        {
            handlePostExecuteException(writer, dc, dc.getPerspectives() + ": unable to establish connection", e);
            return;
        }

        Table table = getBindTable();
        Row row = DialogContextUtils.getInstance().createRowWithFieldValues(dc, table);
        tdc.setPrimaryTableRow(row);
        try
        {
            switch((int) dc.getPerspectives().getFlags())
            {
                case DialogPerspectives.ADD:
                    insertDataUsingTemplate(tdc, cc);
                    break;

                case DialogPerspectives.EDIT:
                    if(getDialogFlags().flagIsSet(SchemaRecordEditorDialogFlags.ALLOW_INSERT_IF_EDIT_PK_NOT_FOUND))
                    {
                        Object pkValue = tdc.getPrimaryKeyValue();
                        if(pkValue == null)
                            insertDataUsingTemplate(tdc, cc);
                        else
                            table.update(cc, row);
                    }
                    else
                        table.update(cc, row);
                    break;

                case DialogPerspectives.DELETE:
                    table.delete(cc, row);
                    break;
            }
            if(handlers.size() > 0)
                handlers.handleDialogExecute(writer, dc);
            cc.commitAndClose();
            dc.setExecuteStageHandled(true);
            handlePostExecute(writer, dc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                cc.rollbackAndClose();
            }
            catch (SQLException e1)
            {
                dc.getDialog().getLog().error("Error while rolling back DML", e1);
            }
            handlePostExecuteException(writer, dc, dc.getPerspectives() + ": " + row, e);
        }
    }
}
