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
 * $Id: SchemaConstraintTest.java,v 1.4 2003-08-31 22:42:33 shahid.shah Exp $
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
import com.netspective.axiom.schema.constraint.BasicTableColumnReference;
import com.netspective.axiom.schema.constraint.BasicForeignKey;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
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

public class SchemaConstraintTest extends TestCase
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
                (SqlManagerComponent) XdmComponentFactory.get(SqlManagerComponent.class, new Resource(SchemaConstraintTest.class, RESOURCE_NAME), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);
        assertNotNull(component);

        schema = component.getManager().getSchema("local");
        assertNotNull(schema);

        populatedSchema = component.getManager().getSchema("db");
        assertNotNull(populatedSchema);

        TestUtils.getConnProvider(this.getClass().getPackage().getName(), false, false);
        TestUtils.getConnProvider(this.getClass().getPackage().getName(), true, true);
    }

    public void testBasicTableColumnReference()
    {
        Table table = populatedSchema.getTables().getByName("Test_Three");
        Column column = table.getColumns().getByName("enumIdRef");
        BasicTableColumnReference colRef = (BasicTableColumnReference) column.getForeignKey().getReference();

        assertEquals("Enum_set_Lookup", colRef.getReference());
        assertNull(colRef.getColumns(schema));
        assertNotNull(colRef.toString());

    }

    public void testBasicForeingKey() throws NamingException, SQLException
    {
        Table table = populatedSchema.getTables().getByName("Test_Three");
        EnumerationTable enumTable = (EnumerationTable) populatedSchema.getTables().getByName("Enum_set_Lookup");
        BasicColumn column = (BasicColumn) table.getColumns().getByName("enumIdRef");
        BasicForeignKey fKey = (BasicForeignKey) column.getForeignKey();

        ColumnValue colVal = column.constructValueInstance();
        colVal.setTextValue("0");

        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        dbvc.setConnectionProvider(TestUtils.getConnProvider(this.getClass().getPackage().getName()));
        dbvc.setDefaultDataSource(this.getClass().getPackage().getName());
        ConnectionContext cc = dbvc.getConnection(this.getClass().getPackage().getName(), true);

        Row row = fKey.getFirstReferencedRow(cc, colVal);
        assertEquals("Zero", row.getColumnValues().getByName("caption").getTextValue());

        Rows rows = fKey.getReferencedRows(cc, colVal);
        assertEquals(1, rows.size());
        assertEquals("Zero", rows.getRow(0).getColumnValues().getByName("caption").getTextValue());

        ColumnValues colVals = table.getColumns().constructValuesInstance();
        colVals.getByName("enumIdRef").setTextValue("0");
        rows = fKey.getReferencedRows(cc, colVals);


        ColumnValues srcVals = table.getColumns().constructValuesInstance();
        assertNull(srcVals.getByName("enumIdRef").getTextValue());
        fKey.fillSourceValuesFromReferencedConnector(srcVals, enumTable.getEnums().getById(2).getColumnValues());
        assertEquals("2", srcVals.getByName("enumIdRef").getTextValue());

        fKey.getReferencedRows(cc, enumTable.getEnums().getById(2));

        try
        {
            fKey.getReferencedRows(cc, table.createRow());
            fail();
        }
        catch (SQLException e)
        {
            // This is good
        }

        Columns refCols = fKey.getReferencedColumns();
        fKey.setReferencedColumns(refCols);
        assertSame(refCols, fKey.getReferencedColumns());

        //TODO: Need to see why setReferencedColumns is checking for instanceof agains a class that could not be a child of Columns
        //Columns fkHolderCol = (Columns) new ForeignKeyPlaceholderColumn(table);
        //fKey.setReferencedColumns(fkHolderCol);
    }

    public void testParentForeignKey() throws NamingException, SQLException
    {
        Table table4 = populatedSchema.getTables().getByName("Test_Four");
        Table table3 = populatedSchema.getTables().getByName("Test_Three");
        ParentForeignKey pfKey = (ParentForeignKey) table4.getColumns().getByName("child_column_a").getForeignKey();

        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        dbvc.setConnectionProvider(TestUtils.getConnProvider(this.getClass().getPackage().getName()));
        dbvc.setDefaultDataSource(this.getClass().getPackage().getName());
        ConnectionContext cc = dbvc.getConnection(this.getClass().getPackage().getName(), true);

        Rows rows;
        try
        {
            rows = pfKey.getChildRowsByParentRow(cc, table4.createRow());
            fail();
        }
        catch (SQLException e)
        {
            // Thi is ok
        }

        Row row = table3.createRow();
        QueryResultSet result = table3.getAccessorByColumnEquality(table3.getColumns().getByName("column_a")).execute(dbvc,new String[]{"abc"}, false);
        ResultSet resultSet = result.getResultSet();
        if (resultSet.next())
            row.getColumnValues().populateValues(resultSet, 1);

        rows = pfKey.getChildRowsByParentRow(cc,row);
        assertEquals(1, rows.size());
        assertEquals("column_b", rows.getRow(0).getColumnValues().getByName("column_b").getTextValue());

        rows = null;
        rows = pfKey.getChildRowsByParentValues(cc, row.getColumnValues());
        assertEquals(1, rows.size());
        assertEquals("column_b", rows.getRow(0).getColumnValues().getByName("column_b").getTextValue());

        Row row2 = table4.createRow();

        pfKey.fillChildValuesFromParentConnector(row2.getColumnValues(),row.getColumnValues());
        assertEquals("abc", row2.getColumnValues().getByName("child_column_a").getTextValue());

    }

}
