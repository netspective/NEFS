package com.netspective.commons.io;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * $Id: Suite.java,v 1.2 2003-03-25 17:51:31 shahbaz.javeed Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(FindFileTest.class));
		suite.addTest(new TestSuite(RegexpFileFilterTest.class));
		suite.addTest(new TestSuite(FileTrackerTest.class));
		suite.addTest(new TestSuite(ResourceTest.class));
		suite.addTest(new TestSuite(InheritableFileResourcesTest.class));

		return suite;
	}
}
