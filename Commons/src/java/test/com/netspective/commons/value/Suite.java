package com.netspective.commons.value;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * $Id: Suite.java,v 1.1 2003-03-25 17:52:45 shahbaz.javeed Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(ValueSourcesTest.class));
		suite.addTest(new TestSuite(DefaultValueContextTest.class));

		return suite;
	}
}
