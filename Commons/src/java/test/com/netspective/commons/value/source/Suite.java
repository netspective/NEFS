package com.netspective.commons.value.source;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.netspective.commons.value.ValueSourcesTest;

/**
 * $Id: Suite.java,v 1.1 2003-03-21 13:54:46 shahbaz.javeed Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(ExpressionValueSourceTest.class));
		suite.addTest(new TestSuite(GlobabllyUniqueIdValueSourceTest.class));

		return suite;
	}
}
