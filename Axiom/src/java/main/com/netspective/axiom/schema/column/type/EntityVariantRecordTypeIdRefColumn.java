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
package com.netspective.axiom.schema.column.type;

import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.column.ColumnValueException;
import com.netspective.axiom.schema.table.type.EntityVariantRecordTypeTable;
import com.netspective.axiom.schema.table.type.EntityVariantRecordTypeTableRow;
import com.netspective.commons.text.TextUtils;

public class EntityVariantRecordTypeIdRefColumn extends IntegerColumn
{
    public class InvalidVariantRecordTypeValueException extends ColumnValueException
    {
        private EntityVariantRecordTypeTable table;
        private String value;

        public InvalidVariantRecordTypeValueException(EntityVariantRecordTypeTable table, String value)
        {
            super("Column '" + getQualifiedName() + "' value is invalid -- variant record type '" + table.getName() + "' does not contain an id or name called [" + value + "]. Available: " + TextUtils.getInstance().join(table.getTypes().getValidValues(), ", "));
            this.table = table;
            this.value = value;
        }

        public EntityVariantRecordTypeTable getTable()
        {
            return table;
        }

        public String getValue()
        {
            return value;
        }
    }

    public class EntityVariantRecordTypeIdRefValue extends IntegerColumn.IntegerColumnValue
    {
        // we never really append id refs, we replace them
        public void appendText(String text)
        {
            setTextValue(text);
        }

        public EntityVariantRecordTypeTableRow getVariantRecordTypeRow()
        {
            EntityVariantRecordTypeTable variantRecTypeTable = getEntityVariantRecordTypeTable();
            return variantRecTypeTable.getTypes().getByIdOrName(getTextValue());
        }

        public void setTextValue(String value) throws ColumnValueException
        {
            EntityVariantRecordTypeTable variantRecTypeTable = getEntityVariantRecordTypeTable();
            EntityVariantRecordTypeTableRow row = variantRecTypeTable.getTypes().getByIdOrName(value);
            if(row != null)
                super.setValue(row.getIdAsInteger());
            else
                throw new InvalidVariantRecordTypeValueException(variantRecTypeTable, value);
        }

        public void setValue(Object value) throws ColumnValueException
        {
            if(value instanceof Integer)
            {
                EntityVariantRecordTypeTable variantRecTypeTable = getEntityVariantRecordTypeTable();
                EntityVariantRecordTypeTableRow row = variantRecTypeTable.getTypes().getById((Integer) value);
                if(row != null)
                    super.setValue(value);
                else
                    throw new InvalidVariantRecordTypeValueException(variantRecTypeTable, value.toString());
            }
            else if(value != null)
                setTextValue(value.toString());
        }

        public void setValue(int value)
        {
            super.setValue(new Integer(value));
        }

        public EntityVariantRecordTypeTableRow getEntityVariantRecordTypeRow()
        {
            EntityVariantRecordTypeTable variantRecTypeTable = getEntityVariantRecordTypeTable();
            return variantRecTypeTable.getTypes().getById(getIntValue());
        }
    }

    public EntityVariantRecordTypeIdRefColumn(Table table)
    {
        super(table);
    }

    public EntityVariantRecordTypeTable getEntityVariantRecordTypeTable()
    {
        return (EntityVariantRecordTypeTable) getForeignKey().getReferencedColumns().getFirst().getTable();
    }

    public ColumnValue constructValueInstance()
    {
        return new EntityVariantRecordTypeIdRefValue();
    }
}
