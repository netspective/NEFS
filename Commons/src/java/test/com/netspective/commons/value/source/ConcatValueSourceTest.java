package com.netspective.commons.value.source;

import junit.framework.TestCase;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;

/**
 * $Id: ConcatValueSourceTest.java,v 1.1 2003-03-23 16:49:03 shahbaz.javeed Exp $
 */
public class ConcatValueSourceTest extends TestCase
{
	public void testConcatValueSource ()
	{
		ConcatenateValueSource cVS = new ConcatenateValueSource("prepend[", ValueSources.getInstance().getValueSource("static:test", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION), "]append");
		assertTrue(cVS.hasValue(null));
		assertEquals("prepend[test]append", cVS.getTextValue(null));
		assertEquals(1, cVS.getTextValues(null).length);
	}
}
