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
 * $Id: SchemasCollection.java,v 1.3 2003-09-29 01:55:38 shahid.shah Exp $
 */

package com.netspective.axiom.schema;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.schema.Schemas;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.BasicSchema;
import com.netspective.axiom.schema.table.BasicTable;
import com.netspective.commons.text.TextUtils;

public class SchemasCollection implements Schemas
{
    private static final Log log = LogFactory.getLog(SchemasCollection.class);

    private List schemas = new ArrayList();
    private Map mapByName = new TreeMap();
    private Map mapByNameOrXmlNodeName = new TreeMap();
    private boolean modified;

    public void add(Schema schema)
    {
        schemas.add(schema);
        mapByName.put(schema.getNameForMapKey(), schema);
        mapByNameOrXmlNodeName.put(schema.getNameForMapKey(), schema);
        mapByNameOrXmlNodeName.put(BasicTable.translateTableNameForMapKey(schema.getXmlNodeName()), schema);
        modified = true;
    }

    public void remove(Schema schema)
    {
        schemas.remove(schema);
        mapByName.remove(schema.getNameForMapKey());
        mapByNameOrXmlNodeName.remove(BasicTable.translateTableNameForMapKey(schema.getXmlNodeName()));
        modified = true;
    }

    public boolean isModified()
    {
        return modified;
    }

    public Schema get(int i)
    {
        return (Schema) schemas.get(i);
    }

    public Schema getByName(String name)
    {
        return (Schema) mapByName.get(BasicSchema.translateNameForMapKey(name));
    }

    public Schema getByNameOrXmlNodeName(String name)
    {
        return (Schema) mapByNameOrXmlNodeName.get(BasicSchema.translateNameForMapKey(name));
    }

    public int size()
    {
        return schemas.size();
    }

    public Set getNames()
    {
        return mapByName.keySet();
    }

    public Table getTable(String schemaTableNames)
    {
        if(size() == 0)
        {
            log.error("No schemas available in getTable()");
            return null;
        }

        String[] parts = TextUtils.split(schemaTableNames, ".", true);
        Schema schema = null;
        String tableName = null;

        switch(parts.length)
        {
            case 1:
                schema = get(0); // the "default" schema
                tableName = parts[0];
                break;

            case 2:
                schema = getByName(parts[0]);
                tableName = parts[1];
                break;

            default:
                log.error("Unknown format for getTable(): " + schemaTableNames + ", exepcted Schema_Name.Table_Name or just Table_Name");
                return null;
        }

        if(schema == null)
        {
            log.error("No schema found in getTable(): " + schemaTableNames + ", exepcted Schema_Name.Table_Name or just Table_Name");
            return null;
        }
        else
            return schema.getTables().getByName(tableName);
    }

    public Column getColumn(String schemaTableColumnNames, Table defaultTable)
    {
        if(size() == 0)
            return null;

        Table table = null;
        String columnName = null;

        String[] parts = TextUtils.split(schemaTableColumnNames, ".", true);
        switch(parts.length)
        {
            case 1:
                table = defaultTable;
                columnName = parts[0];
                break;

            case 2:
                table = getTable(parts[0]);
                columnName = parts[1];
                break;

            case 3:
                table = getTable(parts[0] + "." + parts[1]);
                columnName = parts[2];

            default:
                log.error("Unknown format for getColumn(): " + schemaTableColumnNames + ", expected Schema_Name.Table_Name.Column_Name, Table_Name.Column_Name, or just Column_Name");
                return null;
        }

        if(table == null)
        {
            log.error("Table not found in getColumn(): " + schemaTableColumnNames + ", expected Schema_Name.Table_Name.Column_Name, Table_Name.Column_Name, or just Column_Name");
            return null;
        }
        else
            return table.getColumns().getByName(columnName);
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < schemas.size(); i++)
        {
            sb.append(schemas.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
