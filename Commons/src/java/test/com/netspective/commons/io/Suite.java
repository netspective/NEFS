package com.netspective.commons.io;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * $Id: Suite.java,v 1.3 2003-08-23 16:05:16 shahid.shah Exp $
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
