package com.netspective.commons.value.source;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.netspective.commons.value.ValueSourcesTest;

/**
 * $Id: Suite.java,v 1.2 2003-03-23 16:49:03 shahbaz.javeed Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(ExpressionValueSourceTest.class));
		suite.addTest(new TestSuite(GlobabllyUniqueIdValueSourceTest.class));
		suite.addTest(new TestSuite(FilesystemEntriesValueSourceTest.class));
		suite.addTest(new TestSuite(StaticListValueSourceTest.class));
		suite.addTest(new TestSuite(SystemPropertyValueSourceTest.class));
		suite.addTest(new TestSuite(ConcatValueSourceTest.class));

		return suite;
	}
}
