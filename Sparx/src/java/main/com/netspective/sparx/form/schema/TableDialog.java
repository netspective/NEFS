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
 * $Id: TableDialog.java,v 1.13 2003-10-14 14:54:51 shahid.shah Exp $
 */

package com.netspective.sparx.form.schema;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

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
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.navigate.NavigationContext;

public class TableDialog extends Dialog implements TemplateProducerParent
{
    private static final Log log = LogFactory.getLog(TableDialog.class);
    public static final String PARAMNAME_PRIMARYKEY = "pk";

    protected class InsertAdditionalDataTemplate extends TemplateProducer
    {
        public InsertAdditionalDataTemplate()
        {
            super("/dialog/" + getQualifiedName() + "/insert-additional-data", "insert-additional-data", null, null, false, false);
        }

        public String getTemplateName(String url, String localName, String qName, Attributes attributes) throws SAXException
        {
            return getName();
        }
    }

    private boolean valid;
    private Column primaryKeyColumn;
    private ValueSource dataSrc;
    private ValueSource primaryKeyValueForEditOrDelete;
    private InsertAdditionalDataTemplate insertAdditionalDataTemplateProducer;
    private TemplateProducers templateProducers;

    public TableDialog()
    {
        setDialogContextClass(TableDialogContext.class);
    }

    public TableDialog(DialogsPackage pkg)
    {
        super(pkg);
        setDialogContextClass(TableDialogContext.class);
    }

    public TemplateProducers getTemplateProducers()
    {
        if(templateProducers == null)
        {
            templateProducers = new TemplateProducers();
            insertAdditionalDataTemplateProducer = new InsertAdditionalDataTemplate();
            templateProducers.add(insertAdditionalDataTemplateProducer);
        }
        return templateProducers;
    }

    public DialogFlags createDialogFlags()
    {
        return new TableDialogFlags();
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

    public synchronized void finalizeContents(NavigationContext nc)
    {
        super.finalizeContents(nc);

        Table table = getBindTable();
        if(table != null)
        {
            if(table.getPrimaryKeyColumns().size() != 1)
                log.error(MessageFormat.format("Table {0}.{1} does not have a single primary key. This dialog can only manage tables with single primary keys.'", new Object[] { table.getSchema().getName(), table.getName() }));
            else
            {
                valid = true;
                primaryKeyColumn = table.getPrimaryKeyColumns().getSole();
            }
        }
        else
            log.error(MessageFormat.format("Unable to bind to table {0}. Check that the 'bind-table' attribute is valid.", new Object[] { getBindTableName() }));
    }

    public void render(Writer writer, DialogContext dc, boolean contextPreparedAlready) throws IOException, DialogExecuteException
    {
        if(! valid)
        {
            writer.write("There is an error in dialog '"+ getQualifiedName() +"', please check the error log.");
            return;
        }

        super.render(writer, dc, contextPreparedAlready);
    }

    protected Object getPrimaryKeyValueForEditDeleteConfirmOrPrint(DialogContext dc)
    {
        if(primaryKeyValueForEditOrDelete != null)
            return primaryKeyValueForEditOrDelete.getValue(dc).getValue();

        HttpServletRequest request = dc.getHttpRequest();
        Object pkValue = request.getAttribute(PARAMNAME_PRIMARYKEY);
        if(pkValue == null)
        {
            pkValue = request.getAttribute(primaryKeyColumn.getName());
            if(pkValue == null && primaryKeyColumn != null)
            {
                pkValue = request.getParameter(PARAMNAME_PRIMARYKEY);
                if(pkValue == null)
                    pkValue = request.getParameter(primaryKeyColumn.getName());
            }
        }
        return pkValue;
    }

    public void populateValues(DialogContext dc, int formatType)
    {
        if(dc.isInitialEntry())
        {
            if(dc.addingData())
            {
                DialogContextUtils.getInstance().populateFieldValuesFromRequestParamsAndAttrs(dc);
            }
            else
            {
                Object pkValue = getPrimaryKeyValueForEditDeleteConfirmOrPrint(dc);

                try
                {
                    if(pkValue != null)
                    {
                        ConnectionContext cc = null;
                        try
                        {
                            cc = dc.getConnection(dataSrc != null ? dataSrc.getTextValue(dc) : null, false);
                            Row row = getBindTable().getRowByPrimaryKeys(cc, new Object[] { pkValue }, null);
                            if(row != null)
                            {
                                DialogContextUtils.getInstance().populateFieldValuesFromRow(dc, row);
                                ((TableDialogContext) dc).setPrimaryKeyValue(pkValue);
                            }
                            else if(! getDialogFlags().flagIsSet(TableDialogFlags.ALLOW_INSERT_IF_EDIT_PK_NOT_FOUND))
                                dc.getValidationContext().addValidationError("Unable to locate primary key {0}", new Object[] { pkValue });
                        }
                        finally
                        {
                            if(cc != null) cc.close();
                        }
                    }
                    else
                        dc.getValidationContext().addValidationError("Primary key not specified in primary-key-value-for-edit-or-delete attr, request attr or param.", (Object[]) null);
                }
                catch (NamingException e)
                {
                    dc.getDialog().getLog().error("Error in populateValues PK = " + pkValue, e);
                }
                catch (SQLException e)
                {
                    dc.getDialog().getLog().error("Error in populateValues PK = " + pkValue, e);
                }
            }
        }

        super.populateValues(dc, formatType);
    }

    public void insertParentAndChildren(TableDialogContext tdc, ConnectionContext cc, Table table, Row row, boolean isPrimaryTable) throws SQLException
    {
        table.insert(cc, row);
        if(isPrimaryTable)
            tdc.setPrimaryKeyValue(row.getColumnValues().getByColumn(primaryKeyColumn));

        for(int i = 0; i < table.getChildTables().size(); i++)
        {
            Table childTable = table.getChildTables().get(i);

            // does the child table have any columns that are bound to fields in the dialog?
            if(getBindColumnTablesSet().contains(childTable))
            {
                // find the connector from the child table to the parent table
                Columns parentKeyCols = childTable.getForeignKeyColumns(ForeignKey.FKEYTYPE_PARENT);
                if(parentKeyCols.size() == 1)
                {
                    Column connector = parentKeyCols.getSole();
                    Row childRow = childTable.createRow((ParentForeignKey) connector.getForeignKey(), row);
                    DialogContextUtils.getInstance().populateColumnValuesWithFieldValues(tdc, childRow.getColumnValues());
                    insertParentAndChildren(tdc, cc, childTable, childRow, false);
                }
                else
                    tdc.getDialog().getLog().error("Child table '"+ childTable.getName() +"' has bound fields but doesn't have a single parent foreign key column.");
            }
        }
    }

    public boolean isConditionalExpressionTrue(TableDialogContext tdc, String exprText)
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

    public void insertDataUsingTemplateElement(TableDialogContext tdc, ConnectionContext cc, TemplateElement templateElement, Row parentRow) throws SQLException
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
                tdc.getDialog().getLog().error("Unable to find schema '"+ tableNameParts[0] +"' in TableDialog '"+ getQualifiedName() +"'");
            }

            tableName = tableNameParts[1];
        }

        Table table = schema.getTables().getByNameOrXmlNodeName(tableName);
        if(table == null)
        {
            tdc.getDialog().getLog().error("Unable to find table '"+ tableName +"' in schema '"+ schema.getName() +"' for TableDialog '"+ getQualifiedName() +"'");
            return;
        }

        Row activeRow = null;

        // find the connector from the child table to the parent table if one is available
        Columns parentKeyCols = table.getForeignKeyColumns(ForeignKey.FKEYTYPE_PARENT);
        if(parentKeyCols.size() == 1)
        {
            Column connector = parentKeyCols.getSole();
            activeRow = table.createRow((ParentForeignKey) connector.getForeignKey(), parentRow);
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
                insertDataUsingTemplateElement(tdc, cc, childTableElement, tdc.getPrimaryTableRow());
            }
        }
    }

    public void insertDataUsingTemplate(TableDialogContext tdc, ConnectionContext cc) throws SQLException
    {
        if(insertAdditionalDataTemplateProducer == null)
            return;

        if(insertAdditionalDataTemplateProducer.getInstances().size() == 0)
            return;

        Template insertAdditionalDataTemplate = (Template) insertAdditionalDataTemplateProducer.getInstances().get(0);
        List childTableElements = insertAdditionalDataTemplate.getChildren();
        for(int i = 0; i < childTableElements.size(); i++)
        {
            TemplateElement childTableElement = (TemplateElement) childTableElements.get(i);
            insertDataUsingTemplateElement(tdc, cc, childTableElement, tdc.getPrimaryTableRow());
        }
    }

    public void execute(Writer writer, DialogContext dc) throws IOException
    {
        TableDialogContext tdc = ((TableDialogContext) dc);

        ConnectionContext cc = null;
        try
        {
            cc = dc.getConnection(dataSrc != null ? dataSrc.getTextValue(dc) : null, false);
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
                    insertParentAndChildren(tdc, cc, table, row, true);
                    insertDataUsingTemplate(tdc, cc);
                    break;

                case DialogPerspectives.EDIT:
                    if(getDialogFlags().flagIsSet(TableDialogFlags.ALLOW_INSERT_IF_EDIT_PK_NOT_FOUND))
                    {
                        Object pkValue = tdc.getPrimaryKeyValue();
                        if(pkValue == null)
                        {
                            insertParentAndChildren(tdc, cc, table, row, true);
                            insertDataUsingTemplate(tdc, cc);
                        }
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

            cc.commitAndClose();
            handlePostExecute(writer, dc);
        }
        catch (Exception e)
        {
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
