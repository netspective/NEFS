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

import java.util.HashMap;
import java.util.Map;

import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.table.BasicRows;

public class EntityVariantRecordTableRows extends BasicRows
{
    private Map rowsByTypeId = new HashMap();
    private Map rowsByTypeName = new HashMap();

    public EntityVariantRecordTableRows(Table owner)
    {
        super(owner);
    }

    public void addRow(Row row)
    {
        super.addRow(row);
        final EntityVariantRecordTableRow evrRow = (EntityVariantRecordTableRow) row;
        rowsByTypeId.put(evrRow.getVariantTypeRow().getIdAsInteger(), evrRow);
        rowsByTypeName.put(evrRow.getVariantTypeRow().getName().toUpperCase(), evrRow);
    }

    public EntityVariantRecordTableRow getRowByTypeName(String typeName)
    {
        return (EntityVariantRecordTableRow) rowsByTypeName.get(typeName.toUpperCase());
    }

    public EntityVariantRecordTableRow getRowByTypeIdInt(int typeId)
    {
        return (EntityVariantRecordTableRow) rowsByTypeId.get(new Integer(typeId));
    }

    public EntityVariantRecordTableRow getRowByTypeId(Integer typeId)
    {
        return (EntityVariantRecordTableRow) rowsByTypeId.get(typeId);
    }

    public Object getVariantValueByTypeName(String typeName, Object valueIfException)
    {
        EntityVariantRecordTableRow row = (EntityVariantRecordTableRow) rowsByTypeName.get(typeName.toUpperCase());
        if(row != null)
            return row.getVariantValue(valueIfException);
        else
            return valueIfException;
    }

    public Object getVariantValueByTypeIdInt(int typeId, Object valueIfException)
    {
        EntityVariantRecordTableRow row = (EntityVariantRecordTableRow) rowsByTypeId.get(new Integer(typeId));
        if(row != null)
            return row.getVariantValue(valueIfException);
        else
            return null;
    }

    public Object getVariantValueByTypeId(Integer typeId, Object valueIfException)
    {
        EntityVariantRecordTableRow row = (EntityVariantRecordTableRow) rowsByTypeId.get(typeId);
        if(row != null)
            return row.getVariantValue(valueIfException);
        else
            return null;
    }

    public Map createNameValueMap()
    {
        Map result = new HashMap();
        for(int i = 0; i < size(); i++)
        {
            EntityVariantRecordTableRow evrRow = (EntityVariantRecordTableRow) getRow(i);
            result.put(evrRow.getVariantTypeRow().getName(), evrRow.getVariantValue(null));
        }
        return result;
    }
}
