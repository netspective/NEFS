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
 * $Id: BasicColumn.java,v 1.8 2003-05-24 20:27:57 shahid.shah Exp $
 */

package com.netspective.axiom.schema.column;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.column.ForeignKeyPlaceholderColumn;
import com.netspective.axiom.schema.BasicSchema;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.constraint.BasicTableColumnReference;
import com.netspective.axiom.schema.constraint.BasicForeignKey;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.axiom.schema.constraint.SelfForeignKey;
import com.netspective.axiom.schema.table.TablesCollection;
import com.netspective.axiom.schema.table.BasicTable;
import com.netspective.axiom.schema.table.TableQueryDefinition;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.axiom.schema.table.type.EnumerationTableRows;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.sql.DbmsSqlText;
import com.netspective.axiom.sql.DbmsSqlTexts;
import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.TemplateContentHandler;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.NodeIdentifiers;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.validate.ValidationRules;
import com.netspective.commons.validate.ValidationRulesCollection;
import com.netspective.commons.value.AbstractValue;

public class BasicColumn implements Column, TemplateProducerParent, TemplateConsumer
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();
    private static final Log log = LogFactory.getLog(BasicColumn.class);
    public static final String ATTRNAME_TYPE = "type";
    public static final String[] ATTRNAMES_FKEYREFS = new String[] { "parentref", "parent-ref", "lookupref", "lookup-ref", "selfref", "self-ref" };
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[] { "name" };

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
    }

    protected class DataTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public DataTypeTemplateConsumerDefn()
        {
            super(getSchema().getDataTypesTemplatesNameSpaceId(), ATTRNAME_TYPE, ATTRNAMES_SET_BEFORE_CONSUMING);
        }

        public String getAlternateClassName(TemplateContentHandler contentHandler, List templates, String elementName, Attributes attributes) throws SAXException
        {
            String altClassName = attributes.getValue(NodeIdentifiers.ATTRNAME_ALTERNATE_CLASS_NAME);
            if(altClassName != null)
                return altClassName;

            // if we have a reference to a foreign key, mark it as a placeholder and the Schema will come back to it
            for(int i = 0; i < ATTRNAMES_FKEYREFS.length; i++)
            {
                String attrName = ATTRNAMES_FKEYREFS[i];
                String attrValue = attributes.getValue(attrName);
                if(attrValue != null)
                    return ForeignKeyPlaceholderColumn.class.getName();
            }

            return super.getAlternateClassName(contentHandler, templates, elementName, attributes);
        }
    }

    protected class ColumnPresentationTemplate extends TemplateProducer
    {
        public ColumnPresentationTemplate()
        {
            super(schema.getPresentationTemplatesNameSpaceId(), BasicSchema.TEMPLATEELEMNAME_PRESENTATION, null, null, false, false);
        }

        public String getTemplateName(String url, String localName, String qName, Attributes attributes) throws SAXException
        {
            return getQualifiedName();
        }
    }

    public class BasicColumnValue extends AbstractValue implements ColumnValue
    {
        private DbmsSqlTexts sqlExprs;

        public BasicColumnValue()
        {
            DbmsSqlTexts columnDefaultExprs = valueDefn.getDefaultSqlExprValues();
            if(columnDefaultExprs.size() > 0)
                sqlExprs = columnDefaultExprs.getCopy();
        }

        public Column getColumn()
        {
            return BasicColumn.this;
        }

        public void setValue(ColumnValue value)
        {
            setValue(value.getValue());
            sqlExprs = value.getSqlExprs();
        }

        public boolean isSqlExpr()
        {
            return sqlExprs != null;
        }

        public DbmsSqlText createSqlExpr()
        {
            if(sqlExprs == null) sqlExprs = new DbmsSqlTexts(this, "value");
            return sqlExprs.create();
        }

        public DbmsSqlTexts getSqlExprs()
        {
            return sqlExprs;
        }

        public void addSqlExpr(DbmsSqlText sqlText)
        {
            sqlExprs.add(sqlText);
        }

        public Row getReferencedForeignKeyRow(ConnectionContext cc) throws NamingException, SQLException
        {
            if(! hasValue())
                return null;

            ForeignKey fKey = getForeignKey();
            if(fKey != null)
            {
                Columns fkCol = fKey.getReferencedColumns();
                Table fkTable = fkCol.getFirst().getTable();
                if(fkTable instanceof EnumerationTable)
                {
                    int id = getIntValue();
                    EnumerationTableRows rows = ((EnumerationTable) fkTable).getEnums();
                    return rows.getById(id);
                }
                else
                    return fKey.getFirstReferencedRow(cc, this);
            }
            else
                throw new RuntimeException("Column '"+ getQualifiedName() +"' does not have a foreign key.");
        }

        public Rows getReferencedForeignKeyRows(ConnectionContext cc) throws NamingException, SQLException
        {
            if(! hasValue())
                return null;

            ForeignKey fKey = getForeignKey();
            if(fKey != null)
            {
                Columns fkCol = fKey.getReferencedColumns();
                Table fkTable = fkCol.getFirst().getTable();
                if(fkTable instanceof EnumerationTable)
                {
                    int id = getIntValue();
                    EnumerationTableRows allRows = ((EnumerationTable) fkTable).getEnums();
                    Rows matchingRows = fkTable.createRows();
                    matchingRows.addRow(allRows.getById(id));
                    return matchingRows;
                }
                else
                    return fKey.getReferencedRows(cc, this);
            }
            else
                throw new RuntimeException("Column '"+ getQualifiedName() +"' does not have a foreign key.");
        }

        public EnumerationTableRow getReferencedEnumRow()
        {
            try
            {
                return (EnumerationTableRow) getReferencedForeignKeyRow(null);
            }
            catch (NamingException e)
            {
                log.error("This should never happen!", e);
                throw new NestableRuntimeException("This should never happen!", e);
            }
            catch (SQLException e)
            {
                log.error("This should never happen!", e);
                throw new NestableRuntimeException("This should never happen!", e);
            }
        }

        public String toString()
        {
            StringBuffer sb = new StringBuffer();
            sb.append(getName());
            sb.append("=");
            sb.append(getValue());
            if(sqlExprs != null) sb.append(" (SQL Exprs: "+ sqlExprs.size() +")");
            return sb.toString();
        }
    }

    //TODO: need to add required validation
    /*
    protected class RequiredValueValidationRule extends BasicValidationRule
    {
        public boolean isValid(ValidationContext vc, ScalarValue value)
        {
            if(isRequiredByApp() && ! value.hasValue())
            {
                vc.addValidationError(BasicColumn.this, "Column '"+ getQualifiedName() +"' is required but has no value.");
                return false;
            }

            return true;
        }
    }
    */

    private Schema schema;
    private Table table;
    private String name;
    private String abbrev;
    private String xmlNodeName;
    private List dataTypesConsumed = new ArrayList();
    private int requirement = RequirementEnumeratedAttribute.NOT_REQUIRED;
    private boolean primaryKey;
    private boolean unique;
    private boolean indexed;
    private boolean allowAddToTable;
    private int size = -1;
    private int indexInRow = -1;
    private SqlDataDefns sqlDataDefn = new SqlDataDefns(this);
    private ValueDefns valueDefn = new ValueDefns(this);
    private String descr;
    private ForeignKey foreignKey;
    private String sequenceName;
    private Set dependentFKeys;
    private Tables autoGeneratedColumnTables;
    private TemplateProducers templateProducers;
    private TemplateConsumerDefn templateConsumer;
    private ValidationRules validationRules;

    static public String translateColumnNameForMapKey(String name)
    {
        return name != null ? name.toLowerCase() : null;
    }

    public BasicColumn(Table table)
    {
        setTable(table);
    }

    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        if(templateConsumer == null)
            templateConsumer = new DataTypeTemplateConsumerDefn();
        return templateConsumer;
    }

    public void registerTemplateConsumption(Template template)
    {
        dataTypesConsumed.add(template.getTemplateName());
    }

    public TemplateProducers getTemplateProducers()
    {
        if(templateProducers == null)
        {
            templateProducers = new TemplateProducers();
            templateProducers.add(new ColumnPresentationTemplate());
        }
        return templateProducers;
    }

    public ColumnValue constructValueInstance()
    {
        return new BasicColumnValue();
    }

    public void initValidationRules(ValidationRules rules)
    {
        // empty here, but may be overridden by children
    }

    public ValidationRules getValidationRules()
    {
        if(validationRules == null)
        {
            validationRules = new ValidationRulesCollection();
            initValidationRules(validationRules);
        }
        return validationRules;
    }

    public ValidationRules createValidation()
    {
        return getValidationRules();
    }

    public void addValidation(ValidationRules rules)
    {
        // do nothing
    }

    public ColumnQueryDefnField createQueryDefnField(TableQueryDefinition owner)
    {
        ColumnQueryDefnField result = (ColumnQueryDefnField) owner.createField();
        result.setName(getName());
        result.setColumn(getName());
        result.setTableColumn(this);
        result.setCaption(TextUtils.sqlIdentifierToText(getName(), true));
        return result;
    }

    public void finishConstruction()
    {
        if(this instanceof ForeignKeyPlaceholderColumn)
        {
            ForeignKey fkey = getForeignKey();
            Column referenced = fkey.getReferencedColumns().getSole();
            if(referenced == null)
                throw new RuntimeException("Unable to finish construction of '"+ getQualifiedName() +"': referenced foreign key '"+ fkey +"' not found.");

            // make sure the referenced column has completed its construction
            referenced.finishConstruction();

            Column actualColumn = null;
            try
            {
                actualColumn = getTable().createColumn(referenced.getForeignKeyReferenceeClass());
            }
            catch (Exception e)
            {
                throw new NestableRuntimeException(e);
            }

            ((BasicColumn) actualColumn).inheritForeignKeyReferencedColumn(referenced);
            ((BasicColumn) actualColumn).inheritForeignKeyPlaceholderColumn((ForeignKeyPlaceholderColumn) this);

            getTable().getColumns().replace(this, actualColumn);
        }
    }

    protected void inheritForeignKeyReferencedColumn(Column column)
    {
        setSize(column.getSize());
        getSqlDdl().mergeReferenced(column.getSqlDdl());
        dataTypesConsumed.addAll(column.getDataTypeNames());
    }

    protected void inheritForeignKeyPlaceholderColumn(ForeignKeyPlaceholderColumn column)
    {
        // todo: SQL defns for foreign keys, child tables and other stuff still required??

        setName(column.getName());
        setXmlNodeName(column.getXmlNodeName());
        setSize(column.getSize());
        setIndexInRow(column.getIndexInRow());
        setDescr(column.getDescr());
        setForeignKey(column.getForeignKey());
        Columns srcColumns = new ColumnsCollection();
        srcColumns.add(this);
        foreignKey.setSourceColumns(srcColumns);
        setSequenceName(column.getSequenceName());
        setRequired(new RequirementEnumeratedAttribute(column.getRequirement()));
        setPrimaryKey(column.isPrimaryKey());
        setIndexed(column.isIndexed());
        setUnique(column.isUnique());
        getSqlDdl().merge(column.getSqlDdl());
        getValidationRules().merge(column.getValidationRules());
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public String getName()
    {
        return name;
    }

    public String getAbbrev()
    {
        return abbrev != null ? abbrev : name;
    }

    public String getQualifiedName()
    {
        return BasicTableColumnReference.createReference(this);
    }

    public String getNameForMapKey()
    {
        return translateColumnNameForMapKey(name);
    }

    public String getXmlNodeName()
    {
        return xmlNodeName == null ? TextUtils.xmlTextToNodeName(getName()) : xmlNodeName;
    }

    public void setName(String value)
    {
        name = value;
    }

    public void setAbbrev(String abbrev)
    {
        this.abbrev = abbrev;
    }

    public void setXmlNodeName(String xmlNodeName)
    {
        this.xmlNodeName = xmlNodeName;
    }

    public List getDataTypeNames()
    {
        return dataTypesConsumed;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public int getIndexInRow()
    {
        return indexInRow;
    }

    public void setIndexInRow(int value)
    {
        indexInRow = value;
    }
    /* ------------------------------------------------------------------------------------------------------------- */

    public Schema getSchema()
    {
        return schema;
    }

    public void setSchema(Schema owner)
    {
        this.schema = owner;
    }

    public Table getTable()
    {
        return table;
    }

    public void setTable(Table value)
    {
        table = value;
        setSchema(table.getSchema());
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public String getSequenceName()
    {
        return sequenceName != null ? sequenceName : (table.getAbbrev() + "_" + getName() + "_SEQ").toUpperCase();
    }

    public void setSequenceName(String value)
    {
        sequenceName = value;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public SqlDataDefns getSqlDdl()
    {
        return sqlDataDefn;
    }

    public SqlDataDefns createSqlDdl()
    {
        return sqlDataDefn;
    }

    public void addSqlDdl(SqlDataDefns sqlDataDefn)
    {
        // do nothing -- we have the instance already created, but the XML data model will call this anyway
    }

    public String formatSqlLiteral(Object value)
    {
        return value != null ? value.toString() : null;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public ValueDefns getValueDefns()
    {
        return valueDefn;
    }

    public ValueDefns createValueDefn()
    {
        return valueDefn;
    }

    public void addValueDefn(ValueDefns valueDefns)
    {
        // do nothing -- we have the instance already created, but the XML data model will call this anyway
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public String getDescr()
    {
        return descr;
    }

    public void setDescr(String value)
    {
        descr = value;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public int getSize()
    {
        return size;
    }

    public void setSize(int value)
    {
        size = value;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Class getForeignKeyReferenceeClass()
    {
        return this.getClass();
    }

    public ForeignKey getForeignKey()
    {
        return foreignKey;
    }

    public void setForeignKey(ForeignKey foreignKey)
    {
        this.foreignKey = foreignKey;
    }

    public void setLookupRef(String reference)
    {
        setForeignKey(new BasicForeignKey(this, new BasicTableColumnReference(reference)));
    }

    public void setParentRef(String reference)
    {
        setForeignKey(new ParentForeignKey(this, new BasicTableColumnReference(reference)));
    }

    public void setSelfRef(String reference)
    {
        setForeignKey(new SelfForeignKey(this, new BasicTableColumnReference(reference)));
    }

    public Set getDependentForeignKeys()
    {
        return dependentFKeys;
    }

    public void removeForeignKeyDependency(ForeignKey fKey)
    {
        if (dependentFKeys != null)
            dependentFKeys.remove(fKey);
        fKey.getSourceColumns().getFirst().getTable().removeForeignKeyDependency(fKey);
    }

    public void registerForeignKeyDependency(ForeignKey fKey)
    {
        if (dependentFKeys == null) dependentFKeys = new HashSet();
        dependentFKeys.add(fKey);
        fKey.getSourceColumns().getFirst().getTable().registerForeignKeyDependency(fKey);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public int getRequirement()
    {
        return requirement;
    }

    public boolean isRequiredByApp()
    {
        return requirement == RequirementEnumeratedAttribute.REQUIRED_BY_APP;
    }

    public boolean isRequiredByDbms()
    {
        return requirement == RequirementEnumeratedAttribute.REQUIRED_BY_DBMS;
    }

    public void setRequired(RequirementEnumeratedAttribute requirement)
    {
        this.requirement = requirement.getValueIndex();
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public boolean isIndexed()
    {
        return indexed;
    }

    public void setIndexed(boolean flag)
    {
        indexed = flag;
    }

    public boolean isPrimaryKey()
    {
        return primaryKey;
    }

    public void setPrimaryKey(boolean flag)
    {
        primaryKey = flag;
    }

    public boolean isUnique()
    {
        return unique;
    }

    public void setUnique(boolean flag)
    {
        unique = flag;
    }

    public boolean isAllowAddToTable()
    {
        return allowAddToTable;
    }

    public void setAllowAddToTable(boolean flag)
    {
        allowAddToTable = flag;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public void addTable(Table table)
    {
        if(autoGeneratedColumnTables == null)
            autoGeneratedColumnTables = new TablesCollection();
        autoGeneratedColumnTables.add(table);
        schema.addTable(table);
    }

    public Table createTable()
    {
        return new BasicTable(this);
    }

    public Tables getColumnTables()
    {
        return autoGeneratedColumnTables;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public CompositeColumns createComposite()
    {
        return new CompositeColumns(this);
    }

    public void addComposite(CompositeColumns instance)
    {
        // nothing to do here, composite's children have already been added by the CompositeColumns.addColumn() method
        // since composites are "virtual" columns, we don't add them -- only their children get added
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(TextUtils.getRelativeClassName(BasicColumn.class, getClass()));
        sb.append(" [" + getIndexInRow() + "] ");
        sb.append(getName());

        ForeignKey fkey = getForeignKey();
        if(fkey != null)
        {
            sb.append(" ");
            sb.append(fkey);
        }
        return sb.toString();
    }

}
