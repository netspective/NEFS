package com.netspective.commons.value.exception;

import junit.framework.TestCase;
import com.netspective.commons.value.*;

/**
 * $Id: ValueSourceExceptionTest.java,v 1.1 2003-03-29 19:11:49 shahbaz.javeed Exp $
 */
public class ValueSourceExceptionTest extends TestCase {
	public static void testUnexpectedValueContextException ()
	{
		ValueSources vs = ValueSources.getInstance();
        ValueContext vc = vs.createDefaultValueContext();
		ValueSource valueSrc = vs.getValueSource("static:abc", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);

        boolean exceptionThrown = true;

		try {
			vs.assertValueContextInstance(String.class, vc, valueSrc);
			exceptionThrown = false;
		} catch (UnexpectedValueContextException e) {
			assertTrue(exceptionThrown);
			assertEquals(String.class, e.getExpectedContextClass());
		}

		assertTrue(exceptionThrown);
	}

	public static void testValueReadOnlyException ()
	{
        ReadOnlyValue rov = new ReadOnlyValue();

		boolean exceptionThrown = true;

		try {
			rov.setTextValue("Test");
			exceptionThrown = false;
		} catch (ValueReadOnlyException e) {
			assertTrue(exceptionThrown);
		}

		assertTrue(exceptionThrown);
	}

/*
	public static void testValueException ()
	{
		ReadOnlyValue rov = new ReadOnlyValue();

		boolean exceptionThrown = true;

		try {
			rov.setTextValue("Test");
			exceptionThrown = false;
		} catch (ValueException e) {
			assertTrue(exceptionThrown);
		}

		assertTrue(exceptionThrown);
	}
*/

    public static void testValueSourceNotFoundException ()
    {
	    ValueSource vs = null;
		boolean exceptionThrown = true;

	    try {
		    vs = ValueSources.getInstance().getValueSource("blah:test", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
		    exceptionThrown = false;
	    } catch (ValueSourceNotFoundException e) {
		    assertTrue(exceptionThrown);
	    }

	    assertTrue(exceptionThrown);
    }
}
