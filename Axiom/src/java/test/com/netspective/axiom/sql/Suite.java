package com.netspective.axiom.sql;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * $Id: Suite.java,v 1.1 2003-04-12 05:46:39 shahbaz.javeed Exp $
 */
public class Suite extends TestSuite
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(MiscSqlObjectsTest.class));
        suite.addTest(new TestSuite(SqlManagerQueryTest.class));
        suite.addTest(new TestSuite(DynamicSqlTest.class));
        return suite;
    }
}
