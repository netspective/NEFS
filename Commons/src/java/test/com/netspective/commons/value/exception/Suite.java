package com.netspective.commons.value.exception;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.netspective.commons.value.ValueSourcesTest;
import com.netspective.commons.value.DefaultValueContextTest;

/**
 * $Id: Suite.java,v 1.1 2003-03-29 19:11:49 shahbaz.javeed Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(ValueSourceExceptionTest.class));

		return suite;
	}
}
