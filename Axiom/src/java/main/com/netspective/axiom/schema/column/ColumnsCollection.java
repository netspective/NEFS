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
 * $Id: ColumnsCollection.java,v 1.1 2003-03-13 18:25:41 shahid.shah Exp $
 */

package com.netspective.axiom.schema.column;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.ColumnValuesProducer;
import com.netspective.axiom.schema.Index;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.column.BasicColumn;
import com.netspective.commons.validate.ValidationContext;
import com.netspective.commons.validate.BasicValidationContext;
import com.netspective.commons.validate.ValidationRules;

public class ColumnsCollection implements Columns
{
    private List columns = new ArrayList();
    private List namesOnly = new ArrayList();
    private Map mapByName = new HashMap();
    private Map mapByNameOrXmlNodeName = new HashMap();

    public ColumnsCollection()
    {
    }

    public void finishConstruction()
    {
        for(int i = 0; i < columns.size(); i++)
        {
            Column column = get(i);
            column.finishConstruction();

            if(column.isIndexed())
            {
                Table table = column.getTable();
                Index index = table.createIndex(column);
                table.addIndex(index);
            }
        }
    }

    public ColumnValues constructValuesInstance()
    {
        return new BasicValues();
    }

    protected void storeNames(Column column)
    {
        namesOnly.add(column.getName());
        mapByName.put(column.getNameForMapKey(), column);

        mapByNameOrXmlNodeName.put(column.getNameForMapKey(), column);
        mapByNameOrXmlNodeName.put(BasicColumn.translateColumnNameForMapKey(column.getXmlNodeName()), column);
    }

    public int add(Column column)
    {
        columns.add(column);
        storeNames(column);
        return columns.size() - 1; // this was the index used to add the column
    }

    public void replace(Column original, Column replacement)
    {
        columns.set(original.getIndexInRow(), replacement);
        namesOnly.remove(original.getName());
        mapByName.remove(original.getNameForMapKey());

        mapByNameOrXmlNodeName.remove(original.getNameForMapKey());
        mapByNameOrXmlNodeName.remove(BasicColumn.translateColumnNameForMapKey(original.getXmlNodeName()));

        storeNames(replacement);
    }

    public boolean contains(Column column)
    {
        return columns.indexOf(column) != -1;
    }

    public List getOnlyNames()
    {
        return namesOnly;
    }

    public String getOnlyNames(String delimiter)
    {
        StringBuffer result = new StringBuffer();
        List names = getOnlyNames();
        for(int i = 0; i < names.size(); i++)
        {
            if(i > 0)
                result.append(delimiter);
            result.append(names.get(i));
        }
        return result.toString();
    }

    public String getNamesDelimited(String delimiter)
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < namesOnly.size(); i++)
        {
            if(i > 0)
                sb.append(delimiter);
            sb.append(namesOnly.get(i));
        }
        return sb.toString();
    }

    public Column getSole()
    {
        if(size() != 1)
            throw new RuntimeException("Only a single column is expected in this collection (not "+ size() +"): " + this);
        return (Column) columns.get(0);
    }

    public Column get(int i)
    {
        return (Column) columns.get(i);
    }

    public Column getByName(String name)
    {
        return (Column) mapByName.get(BasicColumn.translateColumnNameForMapKey(name));
    }

    public Column getByNameOrXmlNodeName(String name)
    {
        return (Column) mapByNameOrXmlNodeName.get(BasicColumn.translateColumnNameForMapKey(name));
    }

    public int getColumnIndexInRowByName(String name)
    {
        Column column = (Column) mapByName.get(BasicColumn.translateColumnNameForMapKey(name));
        if (column != null)
            return column.getIndexInRow();
        else
            return COLUMN_INDEX_NOT_FOUND;
    }

    public int getColumnIndexInRowByNameOrXmlNodeName(String name)
    {
        Column column = (Column) mapByNameOrXmlNodeName.get(BasicColumn.translateColumnNameForMapKey(name));
        if (column != null)
            return column.getIndexInRow();
        else
            return COLUMN_INDEX_NOT_FOUND;
    }

    public int size()
    {
        return columns.size();
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < columns.size(); i++)
        {
            sb.append("  ");
            sb.append(columns.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    protected class BasicValues implements ColumnValues
    {
        private ColumnValue[] values;

        public BasicValues()
        {
            int colsCount = columns.size();
            values = new ColumnValue[colsCount];
            for(int i = 0; i < colsCount; i++)
            {
                Column column = (Column) columns.get(i);
                values[i] = column.constructValueInstance();
            }
        }

        public ValidationContext getValidationResult(ValidationContext vc)
        {
            ValidationContext result = vc != null ? vc : new BasicValidationContext();
            for(int i = 0; i < values.length; i++)
            {
                ColumnValue value = values[i];
                ValidationRules rules = value.getColumn().getValidationRules();
                if(rules.size() > 0)
                    rules.validateValue(result, value);
            }
            return result;
        }

        public void copy(ColumnValues source, int copyType)
        {
            switch(copyType)
            {
                case COPYTYPE_DIRECT:
                    for(int i = 0; i < values.length; i++)
                        values[i].setValue(source.getByColumnIndex(i));
                    break;

                case COPYTYPE_MATCH_BY_COLUMN_NAME:
                    for(int i = 0; i < values.length; i++)
                    {
                        ColumnValue cv = values[i];
                        ColumnValue copyValue = source.getByName(cv.getColumn().getName());
                        if(copyValue != null)
                            cv.setValue(copyValue);
                    }
                    break;

                case COPYTYPE_MATCH_BY_COLUMN_INSTANCE:
                    for(int i = 0; i < values.length; i++)
                    {
                        ColumnValue cv = values[i];
                        ColumnValue copyValue = source.getByColumn(cv.getColumn());
                        if(copyValue != null)
                            cv.setValue(copyValue);
                    }
                    break;

                case COPYTYPE_MATCH_BY_COLUMN_FKEY_REF:
                    for(int i = 0; i < values.length; i++)
                    {
                        ColumnValue cv = values[i];
                        ForeignKey fKey = cv.getColumn().getForeignKey();
                        if(fKey != null)
                        {
                            ColumnValue copyValue = source.getByColumn(fKey.getReferencedColumn());
                            if(copyValue != null)
                                cv.setValue(copyValue);
                        }
                    }
                    break;

                default:
                    throw new RuntimeException("Unknown copyType " + copyType);
            }
        }

        public ColumnValue getByColumn(Column column)
        {
            int index = columns.indexOf(column);
            return index != -1 ? getByColumnIndex(index) : null;
        }

        public ColumnValue getByColumnIndex(int columnIndex)
        {
            return values[columnIndex];
        }

        public Object[] getValuesForSqlBindParams()
        {
            Object[] result = new Object[values.length];

            for(int i = 0; i < values.length; i++)
                result[i] = values[i].getValueForSqlBindParam();

            return result;
        }

        public void populateValues(ResultSet resultSet, int rowNum) throws SQLException
        {
            for(int i = 0; i < values.length; i++)
                values[i].setValueFromSqlResultSet(resultSet, rowNum, i+1);
        }

        public void populateValues(ColumnValuesProducer cvp)
        {
            cvp.produceColumnValues(this);
        }

        public ColumnValue getByName(String columnName)
        {
            Column column = ColumnsCollection.this.getByName(columnName);
            if(column == null)
                throw new RuntimeException("Column named '"+ columnName +"' not found in columns collection " + ColumnsCollection.this + ". Available: " + getOnlyNames());

            return values[column.getIndexInRow()];
        }

        public ColumnValue getByNameOrXmlNodeName(String colXmlNodeName)
        {
            Column column = ColumnsCollection.this.getByNameOrXmlNodeName(colXmlNodeName);
            if(column == null)
                throw new RuntimeException("Column named by xml-node '"+ colXmlNodeName +"' not found in columns collection " + ColumnsCollection.this + ". Available: " + getOnlyNames());

            return values[column.getIndexInRow()];
        }

        public int size()
        {
            return values.length;
        }

        public String toString()
        {
            StringBuffer sb = new StringBuffer();
            sb.append(this.getClass().getName());
            sb.append(" ("+ size() +")");
            sb.append(" [");
            for(int i = 0; i < values.length; i++)
            {
                if(i > 0)
                    sb.append(", ");
                sb.append(values[i].toString());
            }
            sb.append("]");
            return sb.toString();
        }
    }
}
