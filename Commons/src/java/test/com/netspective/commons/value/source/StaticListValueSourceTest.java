package com.netspective.commons.value.source;

import junit.framework.TestCase;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;

/**
 * $Id: StaticListValueSourceTest.java,v 1.1 2003-03-23 16:49:03 shahbaz.javeed Exp $
 */
public class StaticListValueSourceTest extends TestCase
{
	public void testStaticListValueSource()
	{
		String[] expectedList = new String[] { "one", "two", "three", "four" };
		ValueSource vs = ValueSources.getInstance().getValueSource("strings:[ ]one two three four", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);

		assertEquals(expectedList[0], vs.getTextValue(null));
		String[] stringList = vs.getTextValues(null);

		assertEquals(expectedList.length, stringList.length);
		for (int i = 0; i < stringList.length; i ++)
			assertEquals(expectedList[i], stringList[i]);

		StaticListValueSource slVS = new StaticListValueSource(expectedList);
		assertEquals(expectedList[0], slVS.getTextValue(null));
		String[] stringListTwo = slVS.getTextValues(null);

		assertEquals(expectedList.length, stringListTwo.length);
		for (int i = 0; i < stringListTwo.length; i ++)
			assertEquals(expectedList[i], stringListTwo[i]);

		slVS = (StaticListValueSource) ValueSources.getInstance().getValueSource("text-list:[+]one+two+three+four", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);

		assertEquals(expectedList[0], vs.getTextValue(null));
		String[] stringListThree = vs.getTextValues(null);

		assertEquals(expectedList.length, stringListThree.length);
		for (int i = 0; i < stringListThree.length; i ++)
			assertEquals(expectedList[i], stringListThree[i]);
	}
}
