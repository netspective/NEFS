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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.table.BasicRows;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class EnumerationTableRows extends BasicRows
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
    }

    private int lastEnumId = -1;
    private Map mapById = new HashMap();
    private Map mapByAbbrevOrCaption = new HashMap();
    private Value value;
    private PresentationValue pValue;

    public EnumerationTableRows(Table owner)
    {
        super(owner);
    }

    public void indexRow(Row row)
    {
        mapById.put(((EnumerationTableRow) row).getIdAsInteger(), row);

        String abbrev = ((EnumerationTableRow) row).getAbbrevOrCaption();
        String caption = ((EnumerationTableRow) row).getCaption();

        if(abbrev != null)
            mapByAbbrevOrCaption.put(abbrev.toUpperCase(), row);

        if(caption != null)
            mapByAbbrevOrCaption.put(caption.toUpperCase(), row);
    }

    public void addRow(Row row)
    {
        super.addRow(row);
    }

    public EnumerationTableRow getById(int id)
    {
        return (EnumerationTableRow) mapById.get(new Integer(id));
    }

    public EnumerationTableRow getById(Integer id)
    {
        return (EnumerationTableRow) mapById.get(id);
    }

    public EnumerationTableRow getByIdOrCaptionOrAbbrev(String text)
    {
        try
        {
            int id = Integer.parseInt(text);
            return (EnumerationTableRow) mapById.get(new Integer(id));
        }
        catch(NumberFormatException e)
        {
            return (EnumerationTableRow) mapByAbbrevOrCaption.get(text.toUpperCase());
        }
    }

    public EnumerationTableRow createEnum()
    {
        EnumerationTableRow row = (EnumerationTableRow) createRow();
        row.setId(lastEnumId + 1);
        return row;
    }

    public void addEnum(EnumerationTableRow row)
    {
        addRow(row);
        lastEnumId = row.getId();
    }

    public String[] getValidValues()
    {
        List validValues = new ArrayList();
        for(int i = 0; i < size(); i++)
        {
            EnumerationTableRow row = (EnumerationTableRow) getRow(i);
            validValues.add(row.getIdAsInteger().toString());
            validValues.add(row.getCaption());
        }
        return (String[]) validValues.toArray(new String[size() * 2]);
    }

    public Value getEnumerationsValue()
    {
        if(value == null)
        {
            List list = new ArrayList();
            for(int i = 0; i < size(); i++)
            {
                EnumerationTableRow row = (EnumerationTableRow) getRow(i);
                list.add(row.getIdAsInteger().toString());
            }

            value = new GenericValue(list);
        }

        return value;
    }

    public PresentationValue getEnumerationsPresentationValue()
    {
        if(pValue == null)
        {
            pValue = new PresentationValue();
            PresentationValue.Items items = pValue.createItems();

            for(int i = 0; i < size(); i++)
            {
                EnumerationTableRow row = (EnumerationTableRow) getRow(i);
                items.addItem(row.getCaption(), row.getIdAsInteger().toString());
            }
        }

        return pValue;
    }
}
