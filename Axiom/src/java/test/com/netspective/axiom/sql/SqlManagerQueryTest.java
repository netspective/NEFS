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
 * $Id: SqlManagerQueryTest.java,v 1.4 2003-04-06 23:49:58 shahbaz.javeed Exp $
 */

package com.netspective.axiom.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.naming.NamingException;

import junit.framework.TestCase;

import com.netspective.commons.xdm.XmlDataModelDtd;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.io.Resource;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.Value;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.collection.QueriesCollection;
import com.netspective.axiom.sql.collection.QueriesPackage;
import com.netspective.axiom.sql.collection.QueryDefinitionsCollection;
import com.netspective.axiom.*;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.value.BasicDatabaseConnValueContext;

public class SqlManagerQueryTest extends TestCase
{
    public static final String RESOURCE_NAME = "SqlManagerQueryTest.xml";
	protected SqlManagerComponent component = null;
	protected SqlManager manager = null;

	protected void setUp () throws Exception {
		super.setUp();

		component =
		        (SqlManagerComponent) XdmComponentFactory.get(SqlManagerComponent.class, new Resource(SqlManagerQueryTest.class, RESOURCE_NAME), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);
		assertNotNull(component);

		component.printErrorsAndWarnings();
		assertEquals(0, component.getErrors().size());

		manager = component.getManager();
		assertEquals(3, manager.getQueries().size());
		assertEquals(1, manager.getQueryDefns().size());
	}

	public void testQueriesObject()
	{
        QueriesCollection queries = (QueriesCollection) manager.getQueries();

		String[] expectedQueryNames = new String[] { "test.statement-0", "test.statement-1", "statement-2" };
		Set queryNames = queries.getNames();
		assertEquals(expectedQueryNames.length, queryNames.size());

		for (int i = 0; i < queries.size(); i ++)
		{
			Query q = queries.get(i);
			assertEquals("statement-" + i, q.getName());
			assertEquals(expectedQueryNames[i], q.getQualifiedName());
			assertTrue(queryNames.contains(expectedQueryNames[i].toUpperCase()));
		}

		String[] expectedNsNames = new String[] { "test" };
		Set nsNames = queries.getNameSpaceNames();
		assertEquals(expectedNsNames.length, nsNames.size());

		for (int i = 0; i < expectedNsNames.length; i ++)
			assertTrue(nsNames.contains(expectedNsNames[i]));
	}

	public void testQueryNameSpaceObjects()
	{
		Query testStatement = manager.getQuery("test.statement-0");
		assertNotNull(testStatement);
		assertNull(testStatement.getParams());

		QueriesPackage qPackage = (QueriesPackage) testStatement.getNameSpace();
        Queries queries = qPackage.getContainer();

		assertSame(manager.getQueries(), queries);
	}

	public void testQueryDefinitionsObject()
	{
        QueryDefinitionsCollection queryDefns = (QueryDefinitionsCollection) manager.getQueryDefns();

		String[] expectedQueryDefnNames = new String[] { "query-defn-1" };
		Set queryDefnNames = queryDefns.getNames();
		assertEquals(expectedQueryDefnNames.length, queryDefnNames.size());

		for (int i = 0; i < expectedQueryDefnNames.length; i ++)
			assertEquals(expectedQueryDefnNames[i], queryDefns.get(i).getName());
	}

    public void testStmt0Validity() throws DataModelException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, NamingException, SQLException
    {
        Query testStatement = manager.getQuery("test.statement-0");
        assertNotNull(testStatement);
        assertNull(testStatement.getParams());

	    DbmsSqlTexts dbmsSqlTexts = testStatement.getSqlTexts();
		String[] expectedDbmsIds = new String[] { "ansi", "oracle" };
	    Set availableDbmsIds = dbmsSqlTexts.getAvailableDbmsIds();

	    for (int i = 0; i < expectedDbmsIds.length; i ++)
	        assertTrue(availableDbmsIds.contains(expectedDbmsIds[i]));

	    assertEquals("", dbmsSqlTexts.getByDbmsId(DatabasePolicies.DBMSID_DEFAULT).getSql().trim());

	    String sqlStatement = dbmsSqlTexts.getByDbmsId("oracle").getSql().trim();
	    String[] sqlWords = TextUtils.split(sqlStatement, " \t\n", true);
	    sqlStatement = TextUtils.join(sqlWords, " ", true);
	    assertEquals("select * from test where column_a = 1 and column_b = 2 and column_c = 'this'", sqlStatement);
    }

    public void testStmt1Validity() throws DataModelException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, NamingException, SQLException
    {
        Query testStatement1 = manager.getQuery("test.statement-1");
        assertNotNull(testStatement1);
        assertNotNull(testStatement1.getParams());
        assertEquals(2, testStatement1.getParams().size());

	    DbmsSqlTexts dbmsSqlTextsTwo = testStatement1.getSqlTexts();
	    assertNull(dbmsSqlTextsTwo.getByDbmsId("oracle"));
	    assertNotNull(dbmsSqlTextsTwo.getByDbmsId(DatabasePolicies.DBMSID_DEFAULT));

	    String sqlStatement1 = dbmsSqlTextsTwo.getByDbmsId(DatabasePolicies.DBMSID_DEFAULT).getSql();
	    String[] sqlWords1 = TextUtils.split(sqlStatement1, " \t\n", true);
	    sqlStatement1 = TextUtils.join(sqlWords1, " ", true);
	    assertEquals("select * from test where column_a = ? and column_b in (${param-list:1}) and column_c = 'this'", sqlStatement1);

	    QueryParameters testParams1 = testStatement1.getParams();
        QueryParameter stmt1ColumnAParam = (QueryParameter) testParams1.get(0);
	    QueryParameter stmt1ColumnBParam = (QueryParameter) testParams1.get(1);

	    assertEquals("column_a", stmt1ColumnAParam.getName());
	    assertEquals("column_b", stmt1ColumnBParam.getName());
    }

    public void testStmt2Validity() throws DataModelException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, NamingException, SQLException
    {
        Query statement2 = manager.getQuery("statement-2");
        assertNotNull(statement2);
        assertNotNull(statement2.getParams());
        assertEquals(2, statement2.getParams().size());

        QueryDefinition qd1 = manager.getQueryDefinition("query-defn-1");
        assertNotNull(qd1);

        assertEquals(5, qd1.getFields().size());
        assertEquals(5, qd1.getJoins().size());
        assertEquals(1, qd1.getSelects().size());

        QueryDefnSelect qds1 = qd1.getSelects().get("query-select-1");
        assertNotNull(qds1);

        System.out.println(TestUtils.connProvider.getDataSourceInfo(TestUtils.DATASRCID_DEFAULT).getConnUrl());

        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        dbvc.setConnectionProvider(TestUtils.connProvider);
        ConnectionContext cc = dbvc.getConnection(TestUtils.DATASRCID_DEFAULT, true);

/*
        System.out.println(TextUtils.getUnindentedText(testStatement1.getSqlText(cc)));
        System.out.println(qds1.getQualifiedName());
        System.out.println(qds1.getSqlText(cc));
        System.out.println(component.getMetrics());
*/

        ValueSource vs = ValueSources.getInstance().getValueSource("data-sources:", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
//        Value value = vs.getValue(dbvc);
//        System.out.println(value.getListValue());

        String dtd = new XmlDataModelDtd().getDtd(component);
        assertTrue(dtd != null);

        //System.out.println(dtd);
    }

    public void testQueryDefnValidity() throws DataModelException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, NamingException, SQLException
    {
        QueryDefinition qd1 = manager.getQueryDefinition("query-defn-1");
        assertNotNull(qd1);

        assertEquals(5, qd1.getFields().size());
        assertEquals(5, qd1.getJoins().size());
        assertEquals(1, qd1.getSelects().size());

        QueryDefnSelect qds1 = qd1.getSelects().get("query-select-1");
        assertNotNull(qds1);

        System.out.println(TestUtils.connProvider.getDataSourceInfo(TestUtils.DATASRCID_DEFAULT).getConnUrl());

        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        dbvc.setConnectionProvider(TestUtils.connProvider);
        ConnectionContext cc = dbvc.getConnection(TestUtils.DATASRCID_DEFAULT, true);

/*
        System.out.println(TextUtils.getUnindentedText(testStatement1.getSqlText(cc)));
        System.out.println(qds1.getQualifiedName());
        System.out.println(qds1.getSqlText(cc));
        System.out.println(component.getMetrics());
*/

        ValueSource vs = ValueSources.getInstance().getValueSource("data-sources:", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
//        Value value = vs.getValue(dbvc);
//        System.out.println(value.getListValue());

        String dtd = new XmlDataModelDtd().getDtd(component);
        assertTrue(dtd != null);

        //System.out.println(dtd);
    }

    /*
    public void testIdConstantsGenerator() throws DataModelException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, NamingException, SQLException
    {
        SqlManagerComponent component =
                (SqlManagerComponent) XdmComponentFactory.get(SqlManagerComponent.class, new Resource(SqlManagerQueryTest.class, "test-data/statements.xml"), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);
        assertNotNull(component);

        File destDir = new File(((FileTracker) component.getInputSource()).getFile().getParent());
        component.generateIdentifiersConstants(destDir, "app.id");
    }
    */
}
