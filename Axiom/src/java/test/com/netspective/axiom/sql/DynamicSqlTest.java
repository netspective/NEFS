package com.netspective.axiom.sql;

import junit.framework.TestCase;
import com.netspective.axiom.SqlManagerComponent;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.TestUtils;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.value.BasicDatabaseConnValueContext;
import com.netspective.axiom.sql.collection.QueryDefinitionsCollection;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.axiom.sql.dynamic.QueryDefnFields;
import com.netspective.axiom.sql.dynamic.QueryDefnField;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.XmlDataModelDtd;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.io.Resource;
import com.netspective.commons.io.FileFind;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.text.TextUtils;

import javax.naming.NamingException;
import java.util.Set;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import java.io.File;
import java.sql.SQLException;

/**
 * $Id: DynamicSqlTest.java,v 1.1 2003-04-12 05:46:39 shahbaz.javeed Exp $
 */
public class DynamicSqlTest extends TestCase
{
    public static final String RESOURCE_NAME = "SqlManagerQueryTest.xml";
    protected SqlManagerComponent component = null;
    protected SqlManager manager = null;
    protected String[] queryDefnNames = new String[]{"query-defn-1"};
    protected String[] fqQueryDefnNames = new String[]{"query-defn-1"};
    static protected File dbFile = null;
    static protected String dbFilename = null;

    static
    {
        FileFind.FileFindResults ffr = FileFind.findInPath(new String[] { "../../Axiom" }, "medspective.data", FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
        assertTrue("The Medspective Demo Schema was not found!  Did you run 'build generate-dal generate-ddl create-database import-data' ?", ffr.isFileFound());
        dbFile = ffr.getFoundFile();
        try
        {
            dbFilename = dbFile.getParentFile().getCanonicalPath() + File.separator + "medspective";
        }
        catch (IOException e)
        {
            dbFilename = "Error occurred while getting canonical filename for the Medspective Demo Database";
        }
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        component =
                (SqlManagerComponent) XdmComponentFactory.get(SqlManagerComponent.class, new Resource(DynamicSqlTest.class, RESOURCE_NAME), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);
        assertNotNull(component);

        component.printErrorsAndWarnings();
        assertEquals(0, component.getErrors().size());

        manager = component.getManager();
        assertEquals(this.queryDefnNames.length, manager.getQueryDefns().size());
    }

    public void testQueryDefinitionsObject()
    {
        QueryDefinitionsCollection queryDefns = (QueryDefinitionsCollection) manager.getQueryDefns();

        Set queryDefnNames = queryDefns.getNames();
        assertEquals(this.queryDefnNames.length, queryDefnNames.size());

        for (int i = 0; i < this.queryDefnNames.length; i++)
            assertEquals(this.queryDefnNames[i], queryDefns.get(i).getName());
    }

    public void testQueryDefnValidity() throws QueryDefinitionException, DataModelException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, NamingException, SQLException
    {
        QueryDefinitionsCollection queryDefns = (QueryDefinitionsCollection) manager.getQueryDefns();
        assertNotNull(queryDefns);
        assertEquals(queryDefnNames.length, queryDefns.size());

        Set actualQueryDefnNames = queryDefns.getNames();
        for (int i = 0; i < queryDefnNames.length; i++)
        {
            assertNotNull(queryDefns.get(queryDefnNames[i]));
            assertTrue(actualQueryDefnNames.contains(queryDefnNames[i].toUpperCase()));
        }

        QueryDefinition queryDefn = manager.getQueryDefinition("query-defn-1");
        assertNotNull(queryDefn);
        assertEquals(queryDefn, queryDefns.get("query-defn-1"));

        // Verify that this query-defn has the right number of fingers and toes ;)
        assertEquals(5, queryDefn.getFields().size());
        assertEquals(5, queryDefn.getJoins().size());
        assertEquals(1, queryDefn.getSelects().size());

        // Verify Fields...
        System.out.println();
        QueryDefnFields fields = queryDefn.getFields();
        String[] expectedFieldCaption = new String[] { null, "Test Field 02 Caption", "Test Field 03 Caption", "Test Field 04 Caption", "Test Field 05 Caption" };
        String[] expectedFieldColumnLabel = new String[] { "field_01", "Test Field 02 Caption", "Test Field 03 Caption", "Test Field 04 Caption", "Test Field 05 Caption" };
        String[] expectedFieldQualifiedColName = new String[] { "join_01.column_01", "join_02.column_02", "column_03", "column_04", "column_05" };
        String[] expectedFieldTableName = new String[] { "join_01", "Table_02", null, null, null };
        String[] expectedFieldTableAlias = new String[] { "join_01", "join_02", null, null, null };
        String[] expectedSelectClauseExprAndLabel = new String[] { "join_01.column_01 as \"field_01\"", "join_02.column_02 as \"Test Field 02 Caption\"", "column_03 as \"Test Field 03 Caption\"", "column_04 as \"Test Field 04 Caption\"", "column_05 as \"Test Field 05 Caption\"" };
        String[] expectedFieldColExpr = new String[]{"join_01.column_01", "join_02.column_02", "column_03", "column_04", "column_05"};
        String[] expectedFieldWhereExpr = new String[]{"join_01.column_01", "join_02.column_02", "column_03", "column_04", "column_05"};
        String[] expectedFieldOrderByExpr = new String[]{"join_01.column_01", "join_02.column_02", "column_03", "column_04", "column_05"};

        for (int i = 0; i < fields.size(); i++)
        {
            QueryDefnField field = fields.get(i);
            assertEquals("field_0" + (i + 1), field.getName());
            assertEquals(expectedFieldCaption[i], field.getCaption());
            assertEquals(expectedFieldColumnLabel[i], field.getColumnLabel());
            assertEquals("column_0" + (i + 1), field.getColumn());
            assertEquals(field.getName(), field.getColumnAlias());
            assertEquals(expectedFieldQualifiedColName[i], field.getQualifiedColName());
            assertEquals(expectedFieldTableName[i], field.getTableName());
            assertEquals(expectedFieldTableAlias[i], field.getTableAlias());
            assertEquals(expectedSelectClauseExprAndLabel[i], field.getSelectClauseExprAndLabel());
            assertEquals(expectedFieldColExpr[i], field.getColumnExpr());
            assertEquals(expectedFieldQualifiedColName[i], field.getColumnExpr());
            assertEquals(expectedFieldWhereExpr[i], field.getWhereExpr());
            assertEquals(expectedFieldQualifiedColName[i], field.getWhereExpr());
            assertEquals(expectedFieldOrderByExpr[i], field.getOrderByExpr());
            assertEquals(expectedFieldQualifiedColName[i], field.getOrderByExpr());

            String output = "QueryDefnField #" + i + ":";

            try
            {
                output += " Join: " + field.getJoin();
            }
            catch (QueryDefinitionException e)
            {
            }

//            System.out.println(output);
        }

        QueryDefnSelect qdsl = queryDefn.getSelects().get("query-select-1");
        assertNotNull(qdsl);

        assertEquals("jdbc:hsqldb:" + dbFilename, TestUtils.connProvider.getDataSourceInfo(TestUtils.DATASRCID_DEFAULT).getConnUrl());

        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        dbvc.setConnectionProvider(TestUtils.connProvider);
        ConnectionContext cc = dbvc.getConnection(TestUtils.DATASRCID_DEFAULT, true);

/*
        System.out.println();
        System.out.println(qdsl.getQualifiedName());
        System.out.println(qdsl.getSqlText(cc));
        System.out.println(component.getMetrics());
*/

        ValueSource vs = ValueSources.getInstance().getValueSource("data-sources:", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
//        Value value = vs.getValue(dbvc);
//        System.out.println(value.getListValue());

        String dtd = new XmlDataModelDtd().getDtd(component);
        assertTrue(dtd != null);

        //System.out.println(dtd);
    }

}
