package com.netspective.commons.io;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * $Id: Suite.java,v 1.1 2003-03-25 08:03:38 shahbaz.javeed Exp $
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

		return suite;
	}
}
