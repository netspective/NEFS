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
package com.netspective.axiom.schema.table.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.table.BasicRow;
import com.netspective.commons.text.DelimitedValuesParser;
import com.netspective.commons.text.TextUtils;

public class EntityVariantRecordTypeTableRow extends BasicRow
{
    private static final Map VARIANT_CONSTRUCTORS = new HashMap();
    private static final Log log = LogFactory.getLog(EntityVariantRecordTypeTableRow.class);

    static
    {
        VARIANT_CONSTRUCTORS.put(String.class, new StringValueConstructor());
        VARIANT_CONSTRUCTORS.put(Boolean.class, new BooleanValueConstructor());
        VARIANT_CONSTRUCTORS.put(String[].class, new StringArrayConstructor());
    }

    private EntityVariantRecordTypeTableRows owner;
    private String javaConstant;
    private final int typeIdColIndex;
    private final int typeNameColIndex;
    private final int typeJavaClassColIndex;
    private final int typeDefaultColIndex;
    private final int typeDescrColIndex;

    public EntityVariantRecordTypeTableRow(EntityVariantRecordTypeTable table, EntityVariantRecordTypeTableRows rows)
    {
        super(table);
        this.owner = rows;

        final Columns columns = table.getColumns();
        this.typeIdColIndex = columns.getByName(table.getTypeIdColName()).getIndexInRow();
        this.typeNameColIndex = columns.getByName(table.getTypeNameColName()).getIndexInRow();
        this.typeJavaClassColIndex = columns.getByName(table.getTypeJavaClassColName()).getIndexInRow();
        this.typeDefaultColIndex = columns.getByName(table.getTypeDefaultColName()).getIndexInRow();
        this.typeDescrColIndex = columns.getByName(table.getTypeDescrColName()).getIndexInRow();
    }

    public int getId()
    {
        return ((Integer) getColumnValues().getByColumnIndex(typeIdColIndex).getValue()).intValue();
    }

    protected void setId(int id)
    {
        getColumnValues().getByColumnIndex(typeIdColIndex).setValue(new Integer(id));
    }

    public Integer getIdAsInteger()
    {
        return (Integer) getColumnValues().getByColumnIndex(typeIdColIndex).getValue();
    }

    public String getName()
    {
        return getColumnValues().getByColumnIndex(typeNameColIndex).getTextValue();
    }

    public boolean isJavaClassAvailable()
    {
        final String textValue = getJavaClassName();
        if(textValue == null || textValue.length() == 0)
            return false;

        try
        {
            Class.forName(textValue);
            return true;
        }
        catch(ClassNotFoundException e)
        {
            return false;
        }
    }

    public Class getJavaClass() throws ClassNotFoundException
    {
        return Class.forName(getColumnValues().getByColumnIndex(typeJavaClassColIndex).getTextValue());
    }

    public String getJavaClassName()
    {
        return getColumnValues().getByColumnIndex(typeJavaClassColIndex).getTextValue();
    }

    public String getJavaConstant()
    {
        return TextUtils.getInstance().xmlTextToJavaConstantTrimmed(javaConstant != null ? javaConstant : getName());
    }

    public void setJavaConstant(String javaConstant)
    {
        this.javaConstant = javaConstant;
    }

    public String getDefaultValue()
    {
        return getColumnValues().getByColumnIndex(typeDefaultColIndex).getTextValue();
    }

    public String getDescription()
    {
        return getColumnValues().getByColumnIndex(typeDescrColIndex).getTextValue();
    }

    public EntityVariantRecordTypeTableRows getOwner()
    {
        return owner;
    }

    public void setOwner(EntityVariantRecordTypeTableRows owner)
    {
        this.owner = owner;
    }

    public Object constructVariantPrime(EntityVariantRecordTable table, ColumnValue value) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        Class cls = getJavaClass();
        VariantValueConstructor varValueConstr = (VariantValueConstructor) VARIANT_CONSTRUCTORS.get(cls);
        if(varValueConstr != null)
            return varValueConstr.constructVariant(table, value, getDefaultValue());

        // look for the class using a string constructor and create a new instance based on that
        Constructor constructor = cls.getConstructor(new Class[]{String.class});
        final String textValue = value.getTextValue();
        return constructor.newInstance(new Object[]{textValue == null ? getDefaultValue() : null});
    }

    public Object constructVariant(EntityVariantRecordTable table, ColumnValue value, Object valueIfException)
    {
        try
        {
            return constructVariantPrime(table, value);
        }
        catch(Exception e)
        {
            log.error(value + " could not be created", e);
            return valueIfException;
        }
    }

    public static interface VariantValueConstructor
    {
        public Object constructVariant(EntityVariantRecordTable table, ColumnValue value, String defaultValue);
    }

    public static class StringValueConstructor implements VariantValueConstructor
    {
        public Object constructVariant(EntityVariantRecordTable table, ColumnValue value, String defaultValue)
        {
            return value.getTextValue();
        }
    }

    public static class BooleanValueConstructor implements VariantValueConstructor
    {
        public Object constructVariant(EntityVariantRecordTable table, ColumnValue value, String defaultValue)
        {
            return new Boolean(TextUtils.getInstance().toBoolean(value.getTextValue(), false));
        }
    }

    public static class StringArrayConstructor implements VariantValueConstructor
    {
        public Object constructVariant(EntityVariantRecordTable table, ColumnValue value, String defaultValue)
        {
            DelimitedValuesParser parser = new DelimitedValuesParser(',');
            return parser.parse(value.getTextValue());
        }
    }
}
