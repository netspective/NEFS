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
package com.netspective.axiom.schema.transport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.axiom.schema.table.type.EnumerationTableRows;

public class DataImportDtd
{
    private final String lSep = System.getProperty("line.separator");

    private final String BOOLEAN = "%boolean;";

    private Map visited = new HashMap();

    public void generate(Schema schema, PrintWriter out)
    {
        Tables tables = schema.getApplicationTables();
        Set childColumnElemNames = new TreeSet();

        try
        {
            printHead(out, tables);

            for(int i = 0; i < tables.size(); i++)
                printElementDecl(out, tables.get(i), childColumnElemNames);

            for(Iterator i = childColumnElemNames.iterator(); i.hasNext(); )
            {
                String elemName = (String) i.next();
                out.println("<!ELEMENT " + elemName + " (#PCDATA)>");
                out.println("<!ATTLIST " + elemName + " IDREF CDATA #IMPLIED>");
                out.println();
            }

            printTail(out);
        }
        finally
        {
            if (out != null) out.close();
            visited.clear();
        }
    }

    public void generate(Schema schema, File output) throws IOException
    {
        PrintWriter out = null;
        try
        {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF8"));
        }
        catch (UnsupportedEncodingException ue)
        {
            /*
             * Plain impossible with UTF8, see
             * http://java.sun.com/products/jdk/1.2/docs/guide/internat/encoding.doc.html
             *
             * fallback to platform specific anyway.
             */
            out = new PrintWriter(new FileWriter(output));
        }
        generate(schema, out);
    }

    public String getDtd(Schema schema)
    {
        StringWriter sw = new StringWriter();
        generate(schema, new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Prints the header of the generated output.
     *
     * <p>Basically this prints the XML declaration, defines some
     * entities and the DAL element.</p>
     */
    private void printHead(PrintWriter out, Tables tables)
    {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        out.println("<!ENTITY % boolean \"(true | false | on | off | yes | no)\">");
        out.println("<!ENTITY % enum_table_without_enums \"CDATA\">");
        out.println("<!ENTITY % elements \"dal\">\n");
        out.print("<!ELEMENT dal (");
        boolean first = true;
        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);
            if(table.isChildTable())
                continue;

            if (!first)
                out.print(" | ");
            else
                first = false;
            out.print(table.getXmlNodeName());
        }
        out.println(")*>");
        out.println("");
    }

    /**
     * Print the definition for a given element.
     */
    private void printElementDecl(PrintWriter out, Table parentTable, Set childColumnElemNames)
    {
        if (visited.containsKey(parentTable.getName()))
            return;
        visited.put(parentTable.getName(), "");

        Tables childTables = parentTable.getChildTables();
        Columns columns = parentTable.getColumns();

        StringBuffer sb = new StringBuffer("<!ELEMENT ");
        sb.append(parentTable.getXmlNodeName()).append(" ");

        List list = new ArrayList();

        for(int i = 0; i < columns.size(); i++)
            list.add(columns.get(i).getXmlNodeName());

        for(int i = 0; i < childTables.size(); i++)
            list.add(childTables.get(i).getXmlNodeName());

        if (list.isEmpty())
        {
            sb.append("EMPTY");
        }
        else
        {
            sb.append("(");
            final int count = list.size();
            for (int i = 0; i < count; i++)
            {
                if (i != 0)
                    sb.append(" | ");
                sb.append(list.get(i));
                childColumnElemNames.add(list.get(i));
            }

            sb.append(")");
            if (count > 1 || !list.get(0).equals("#PCDATA"))
            {
                sb.append("*");
            }
        }
        sb.append(">");

        out.println(sb);

        sb.setLength(0);
        sb.append("<!ATTLIST ").append(parentTable.getXmlNodeName());

        sb.append(lSep);
        sb.append("          ID CDATA #IMPLIED").append(lSep);
        sb.append("          IDREF CDATA #IMPLIED");

        for(int i = 0; i < columns.size(); i++)
        {
            Column column = columns.get(i);
            String attrName = column.getXmlNodeName();

            sb.append(lSep).append("          ").append(attrName).append(" ");
            Class type = column.getClass();
            if (type.equals(Boolean.class) || type.equals(Boolean.TYPE))
            {
                sb.append(BOOLEAN).append(" ");
            }
            else if (column instanceof EnumerationIdRefColumn)
            {
                EnumerationTable enumTable = (EnumerationTable) column.getForeignKey().getReferencedColumns().getFirst().getTable();
                EnumerationTableRows enumRows = (EnumerationTableRows) enumTable.getData();

                if(enumRows != null)
                {
                    String[] values = enumRows.getValidValues();
                    if (values == null || values.length == 0)
                    {
                        sb.append("CDATA ");
                    }
                    else if(!areNmtokens(values))
                    {
                        sb.append("CDATA ");
                    }
                    else
                    {
                        sb.append("(");
                        for (int v = 0; v < values.length; v++)
                        {
                            if (v != 0)
                            {
                                sb.append(" | ");
                            }
                            sb.append(values[v]);
                        }
                        sb.append(") ");
                    }
                }
                else
                    // Enum table enumTable.getName() (referenced by column.getQualifiedName() has no enumerations
                    sb.append("CDATA ");
            }
            else
            {
                sb.append("CDATA ");
            }

            if(column.isPrimaryKey() || column.isRequiredByApp() || column.isRequiredByDbms())
                sb.append("#IMPLIED"); // this column is required but #REQUIRED is not used because it could be provided using tag instead of attribute
            else
                sb.append("#IMPLIED");
        }

        sb.append(">").append(lSep);
        out.println(sb);

        if(childTables != null)
        {
            for(int i = 0; i < childTables.size(); i++)
                printElementDecl(out, childTables.get(i), childColumnElemNames);
        }
    }

    private void printTail(PrintWriter out)
    {
    }

    /**
     * Does this String match the XML-NMTOKEN production?
     */
    protected boolean isNmtoken(String s)
    {
        final int length = s.length();
        for (int i = 0; i < length; i++)
        {
            char c = s.charAt(i);
            // XXX - we are ommitting CombiningChar and Extender here
            if (!Character.isLetterOrDigit(c) &&
                    c != '.' && c != '-' &&
                    c != '_' && c != ':')
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Do the Strings all match the XML-NMTOKEN production?
     *
     * <p>Otherwise they are not suitable as an enumerated attribute,
     * for example.</p>
     */
    protected boolean areNmtokens(String[] s)
    {
        for (int i = 0; i < s.length; i++)
        {
            if (!isNmtoken(s[i]))
            {
                return false;
            }
        }
        return true;
    }

}
