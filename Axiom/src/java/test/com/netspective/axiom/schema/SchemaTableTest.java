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
 * $Id: SchemaTableTest.java,v 1.4 2003-07-19 00:39:37 shahid.shah Exp $
 */

package com.netspective.axiom.schema;

import com.netspective.axiom.*;
import com.netspective.axiom.schema.column.type.*;
import com.netspective.axiom.schema.column.*;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.axiom.schema.table.type.EnumerationTableRows;
import com.netspective.axiom.schema.table.BasicTable;
import com.netspective.axiom.schema.table.TablesCollection;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.sql.DbmsSqlText;
import com.netspective.axiom.sql.DbmsSqlTexts;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.axiom.value.BasicDatabaseConnValueContext;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.commons.io.Resource;
import com.netspective.commons.value.exception.ValueException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XdmComponentFactory;
import junit.framework.TestCase;

import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

import org.xml.sax.SAXException;

public class SchemaTableTest extends TestCase
{
    public static final String RESOURCE_NAME = "test-data-schema.xml";
    protected SqlManagerComponent component = null;
    protected SqlManager manager = null;
    protected Schema schema = null;

    protected Schema populatedSchema = null;

    protected void setUp() throws Exception
    {
        super.setUp();

        component =
                (SqlManagerComponent) XdmComponentFactory.get(SqlManagerComponent.class, new Resource(SchemaTableTest.class, RESOURCE_NAME), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);
        assertNotNull(component);

        schema = component.getManager().getSchema("local");
        assertNotNull(schema);

        populatedSchema = component.getManager().getSchema("db");
        assertNotNull(populatedSchema);

        TestUtils.getConnProvider(this.getClass().getPackage().getName(), false, false);
        TestUtils.getConnProvider(this.getClass().getPackage().getName(), true, true);
    }

    public void testEnumTableType(){
        EnumerationTable table = (EnumerationTable)populatedSchema.getTables().getByName("Enum_set_Lookup");
        assertNotNull(table);
        EnumerationTableRows rows = table.getEnums();

        assertEquals("Zero", rows.getEnumerationsPresentationValue().getTextValues()[0]);
        assertEquals("One", rows.getEnumerationsPresentationValue().getTextValues()[1]);
        assertEquals("Two", rows.getEnumerationsPresentationValue().getTextValues()[2]);
        assertNotNull(rows.getEnumerationsValue());
    }

    public void testBasicTable() throws NamingException, SQLException
    {
        BasicTable table = (BasicTable) populatedSchema.getTables().getByName("Test_Three");
        BasicTable table2 = (BasicTable)populatedSchema.getTables().getByName("Enum_set_Lookup");

        //Test getTableTypes Method
        assertEquals("Default", table.getTableTypes().get(0));

        //For this particular table there is no ParentColumn.  Need to create a scenario where this is not null and assert
        assertNull(table.getParentColumn());

        table.setXmlNodeName("Test-Three");
        assertEquals("Test-Three",table.getXmlNodeName());

        assertEquals("This is a sample table that is used in the SchemaTableTest Unit Test.  Do not discard.",table.getDescription());

        assertNotNull(table.getForeignKeyColumns().getByName("record_status_id"));
        assertNotNull(table.getForeignKeyColumns().getByName("enumIdRef"));

        try
        {
            table.createColumn(Byte.class);
            fail();
        }
        catch (RuntimeException e)
        {
            //This is good
        }
        catch (NoSuchMethodException e)
        {
            fail();
        }
        catch (InstantiationException e)
        {
            fail();
        }
        catch (IllegalAccessException e)
        {
            fail();
        }
        catch (InvocationTargetException e)
        {
            fail();
        }

        assertTrue(table.isParentTable());
        assertTrue(!table2.isParentTable());



        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        dbvc.setConnectionProvider(TestUtils.getConnProvider(this.getClass().getPackage().getName()));
        dbvc.setDefaultDataSource(this.getClass().getPackage().getName());
        ConnectionContext cc = dbvc.getConnection(this.getClass().getPackage().getName(), true);

        Row initialRow = table.createRow();
        QueryDefnSelect query = table.getAccessorByColumnEquality(table.getColumns().getByName("column_a"));
        QueryResultSet qrs = query.execute(dbvc, new Object[]{"abc"}, false);
        ResultSet rs = qrs.getResultSet();
        if (rs.next())
            initialRow.getColumnValues().populateValues(rs,1);


        Row row = table.createRow();
        row = table.getRowByPrimaryKeys(cc,initialRow.getPrimaryKeyValues(),row);

        assertEquals(initialRow, row);

        assertTrue(!table.dataChangedInStorage(cc, row));
        row.getColumnValues().getByName("column_a").setTextValue("cba");
        assertTrue(table.dataChangedInStorage(cc, row));


        table.refreshData(cc, row);
        assertEquals(initialRow, row);

        Rows rows = table.createRows();
        assertNotNull(rows);

        ColumnsCollection columns = new ColumnsCollection();
        columns.add(table.getColumns().getByName("column_a"));
        columns.add(table.getColumns().getByName("column_b"));
        table.getAccessorByColumnsEquality(columns);

        //TODO: Need to do some assertion on the columns accessor
        //QueryResultSet qrs3 = query.execute(dbvc, new Object[]{"abc","a"}, false);
        //ResultSet rs3 = qrs3.getResultSet();
        //if (rs3.next())
        //    initialRow.getColumnValues().populateValues(rs,1);

        try
        {
            assertNotNull(table.createAccessor());
        }
        catch (QueryDefinitionException e)
        {
            fail();
        }

        //getRowByPrimaryKeys
        //getRowByPrimaryKeys

    }

    public void testBasicTableHierarchyRef()
    {

        BasicTable table = (BasicTable) populatedSchema.getTables().getByName("Test_Three");
        TableHierarchyReference hierarchy = table.createHierarchy();
        assertNotNull(hierarchy);
        assertSame(hierarchy, table.getHierarchy());
        assertNull(hierarchy.getParent());
        hierarchy.setParent("Test-Parent");
        assertEquals(hierarchy.getParent(), "Test-Parent");

    }

    public void testBasicRow() throws NamingException, SQLException
    {
        BasicTable table = (BasicTable) populatedSchema.getTables().getByName("Test_Three");
        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        dbvc.setConnectionProvider(TestUtils.getConnProvider(this.getClass().getPackage().getName()));
        dbvc.setDefaultDataSource(this.getClass().getPackage().getName());
        ConnectionContext cc = dbvc.getConnection(this.getClass().getPackage().getName(), true);

        Row initialRow = table.createRow();
        QueryDefnSelect query = table.getAccessorByColumnEquality(table.getColumns().getByName("column_a"));
        QueryResultSet qrs = query.execute(dbvc, new Object[]{"abc"}, false);
        ResultSet rs = qrs.getResultSet();
        if (rs.next())
            initialRow.getColumnValues().populateValues(rs,1);

        assertNotNull(initialRow.toString());

    }

    public void testTableTree()
    {
        BasicTable table = (BasicTable) populatedSchema.getTables().getByName("Test_Three");
        BasicTable.BasicTableTreeNode treeNode = (BasicTable.BasicTableTreeNode) table.createTreeNode(populatedSchema.getStructure(),null,0);
        assertSame(populatedSchema.getStructure(), treeNode.getOwner());
        assertNotNull(treeNode.toString());
        assertTrue(treeNode.hasChildren());
    }

    public void testTablesCollection()
    {
        TablesCollection tables = (TablesCollection) populatedSchema.getTables();

        try
        {
            tables.getSole();
            fail();
        }
        catch (RuntimeException e)
        {
            // This is good
        }

        assertNotNull(tables.toString());

        Table table = tables.getByName("To_Be_Deleted");
        assertNotNull(table);
        tables.remove(table);
        assertNull(tables.getByName("To_Be_Deleted"));
    }

    public void testTableQueryDefinition()
    {
        Table table = (Table) populatedSchema.getTables().getByName("Test_Three");
        assertSame(table, table.getQueryDefinition().getOwner());
        table.getQueryDefinition().setOwner(table);
    }

    /*TODO:  Need to figure out how to test BasicTable.TablePresentationTemplate
    public void testTablePresentationTemplate()
    {
        BasicTable table = (BasicTable) populatedSchema.getTables().getByName("Test_Three");
        try
        {
            table.getPresentation().getTemplateName(null,null,null,null);
        }
        catch (SAXException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }*/

}
