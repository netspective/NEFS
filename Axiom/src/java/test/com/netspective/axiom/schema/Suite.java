package com.netspective.axiom.schema;

import junit.framework.TestSuite;
import junit.framework.Test;
import com.netspective.axiom.sql.MiscSqlObjectsTest;
import com.netspective.axiom.sql.SqlManagerQueryTest;
import com.netspective.axiom.sql.DynamicSqlTest;

/**
 * $Id: Suite.java,v 1.2 2003-06-20 21:00:50 roque.hernandez Exp $
 */
public class Suite extends TestSuite
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(SchemaColumnsTest.class));
        suite.addTest(new TestSuite(SchemaTableTest.class));
        return suite;
    }
}
