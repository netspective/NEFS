package com.netspective.commons.value.exception;

import junit.framework.TestCase;

import com.netspective.commons.value.*;

/**
 * $Id: ValueSourceExceptionTest.java,v 1.2 2003-06-12 14:46:17 shahid.shah Exp $
 */
public class ValueSourceExceptionTest extends TestCase
{
    public static void testValueReadOnlyException()
    {
        ReadOnlyValue rov = new ReadOnlyValue();

        boolean exceptionThrown = true;

        try
        {
            rov.setTextValue("Test");
            exceptionThrown = false;
        }
        catch (ValueReadOnlyException e)
        {
            assertTrue(exceptionThrown);
        }

        assertTrue(exceptionThrown);
    }

    public static void testValueSourceNotFoundException()
    {
        ValueSource vs = null;
        boolean exceptionThrown = true;

        try
        {
            vs = ValueSources.getInstance().getValueSource("blah:test", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
            exceptionThrown = false;
        }
        catch (ValueSourceNotFoundException e)
        {
            assertTrue(exceptionThrown);
        }

        assertTrue(exceptionThrown);
    }
}
