package com.netspective.commons.schedule;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * $Id: Suite.java,v 1.1 2004-03-26 03:57:43 shahid.shah Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(CalendarUtilsTest.class));
		suite.addTest(new TestSuite(ScheduleManagerTest.class));

		return suite;
	}
}
