/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: TextUtilsTest.java,v 1.5 2004-08-09 22:14:28 shahid.shah Exp $
 */

package com.netspective.commons.text;

import junit.framework.TestCase;

public class TextUtilsTest extends TestCase
{
    protected TextUtils textUtils = TextUtils.getInstance();

    public void testClassUtilities()
    {
/*
        String relativeClassNameCommonPackage = TextUtils.getRelativeClassName(this.getClass(), ExpressionText.class);
		assertEquals("ExpressionText", relativeClassNameCommonPackage);

	    String relativeClassNameJavaStandardClass = TextUtils.getRelativeClassName(this.getClass(), java.lang.String.class);
	    assertEquals("java.lang.String", relativeClassNameJavaStandardClass);
*/

	    String bareClassName = textUtils.getClassNameWithoutPackage("java.lang.String", '.');
	    String altBareClassName = textUtils.getClassNameWithoutPackage("java.lang.String");
	    assertEquals("String", bareClassName);
	    assertEquals(bareClassName, altBareClassName);

	    String bareClassNameModifiedSeparator = textUtils.getClassNameWithoutPackage("java.lang.String", '/');
	    assertEquals("java.lang.String", bareClassNameModifiedSeparator);

	    String bareFileName = textUtils.getClassNameWithoutPackage("/usr/local/src/bite-me-0.0.1.tar.gz", '/');
	    assertEquals("bite-me-0.0.1.tar.gz", bareFileName);

	    String barePackageName = textUtils.getPackageName("java.lang.String", '.');
	    String altBarePackageName = textUtils.getPackageName("java.lang.String");
	    assertEquals("java.lang", barePackageName);
	    assertEquals(barePackageName, altBarePackageName);

	    String barePackageNameModifiedSeparator = textUtils.getPackageName("java.lang.String", '/');
	    assertNull(barePackageNameModifiedSeparator);

	    String bareDirectoryName = textUtils.getPackageName("/usr/local/src/what-is-this-0.0.2.tar.gz", '/');
	    assertEquals("/usr/local/src", bareDirectoryName);
    }

	public void testStringSplit()
	{
		String testSentence = "This is a sample sentence";
		String testSentenceWithExtraSpaces = " This is  a sample  sentence   ";
		String testSentenceWithAlternateDelimiterAndExtraDelimiters = ".This.is.a.sample. sentence... ";

		String[] testWordsNoTrim = textUtils.split(testSentence, " ", false);
		assertEquals(5, testWordsNoTrim.length);
        assertEquals("This", testWordsNoTrim[0]);
		assertEquals("is", testWordsNoTrim[1]);
		assertEquals("a", testWordsNoTrim[2]);
		assertEquals("sample", testWordsNoTrim[3]);
		assertEquals("sentence", testWordsNoTrim[4]);

		String[] testWordsNoTrim2 = textUtils.split(testSentenceWithExtraSpaces, " ", false);
		assertEquals(5, testWordsNoTrim2.length);
		assertEquals("This", testWordsNoTrim2[0]);
		assertEquals("is", testWordsNoTrim2[1]);
		assertEquals("a", testWordsNoTrim2[2]);
		assertEquals("sample", testWordsNoTrim2[3]);
		assertEquals("sentence", testWordsNoTrim2[4]);

		String[] testWordsNoTrim3 = textUtils.split(testSentenceWithExtraSpaces);
		assertEquals(5, testWordsNoTrim3.length);
		assertEquals("This", testWordsNoTrim3[0]);
		assertEquals("is", testWordsNoTrim3[1]);
		assertEquals("a", testWordsNoTrim3[2]);
		assertEquals("sample", testWordsNoTrim3[3]);
		assertEquals("sentence", testWordsNoTrim3[4]);
		assertEquals(testWordsNoTrim2.length, testWordsNoTrim3.length);
		assertEquals(testWordsNoTrim2[0], testWordsNoTrim3[0]);
		assertEquals(testWordsNoTrim2[1], testWordsNoTrim3[1]);
		assertEquals(testWordsNoTrim2[2], testWordsNoTrim3[2]);
		assertEquals(testWordsNoTrim2[3], testWordsNoTrim3[3]);
		assertEquals(testWordsNoTrim2[4], testWordsNoTrim3[4]);

		String[] testWordsTrimmed = textUtils.split(testSentenceWithExtraSpaces, " ", true);
		assertEquals(5, testWordsTrimmed.length);
		assertEquals("This", testWordsTrimmed[0]);
		assertEquals("is", testWordsTrimmed[1]);
		assertEquals("a", testWordsTrimmed[2]);
		assertEquals("sample", testWordsTrimmed[3]);
		assertEquals("sentence", testWordsTrimmed[4]);

		String[] testWordsAlternateDelimiter = textUtils.split(testSentenceWithAlternateDelimiterAndExtraDelimiters, ".", false);
		assertEquals(6, testWordsAlternateDelimiter.length);
		assertEquals("This", testWordsAlternateDelimiter[0]);
		assertEquals("is", testWordsAlternateDelimiter[1]);
		assertEquals("a", testWordsAlternateDelimiter[2]);
		assertEquals("sample", testWordsAlternateDelimiter[3]);
		assertEquals(" sentence", testWordsAlternateDelimiter[4]);
		assertEquals(" ", testWordsAlternateDelimiter[5]);

		String[] testWordsAlternateDelimiterTrimmed = textUtils.split(testSentenceWithAlternateDelimiterAndExtraDelimiters, ".", true);
		assertEquals(6, testWordsAlternateDelimiterTrimmed.length);
		assertEquals("This", testWordsAlternateDelimiterTrimmed[0]);
		assertEquals("is", testWordsAlternateDelimiterTrimmed[1]);
		assertEquals("a", testWordsAlternateDelimiterTrimmed[2]);
		assertEquals("sample", testWordsAlternateDelimiterTrimmed[3]);
		assertEquals("sentence", testWordsAlternateDelimiterTrimmed[4]);
		assertEquals("", testWordsAlternateDelimiterTrimmed[5]);
	}

	public void testStringSplitErrors()
	{
		String testSentence = "This is a sample sentence";

		String[] testWordsErrorOne = textUtils.split(null, " ", false);
		assertNull(testWordsErrorOne);

		String[] testWordsErrorTwo = textUtils.split(null, " ", true);
		assertNull(testWordsErrorTwo);

		String[] testWordsErrorThree = textUtils.split(null, null, false);
		assertNull(testWordsErrorThree);

		String[] testWordsErrorFour = textUtils.split(null, null, true);
		assertNull(testWordsErrorFour);

		String[] expectedWordsErrorFive = textUtils.split(testSentence, " ", false);
		String[] testWordsErrorFive = textUtils.split(testSentence, null, false);
		assertEquals(5, testWordsErrorFive.length);
		assertEquals(expectedWordsErrorFive.length, testWordsErrorFive.length);
		assertEquals("This", testWordsErrorFive[0]);
		assertEquals("is", testWordsErrorFive[1]);
		assertEquals("a", testWordsErrorFive[2]);
		assertEquals("sample", testWordsErrorFive[3]);
		assertEquals("sentence", testWordsErrorFive[4]);
		assertEquals(expectedWordsErrorFive[0], testWordsErrorFive[0]);
		assertEquals(expectedWordsErrorFive[1], testWordsErrorFive[1]);
		assertEquals(expectedWordsErrorFive[2], testWordsErrorFive[2]);
		assertEquals(expectedWordsErrorFive[3], testWordsErrorFive[3]);
		assertEquals(expectedWordsErrorFive[4], testWordsErrorFive[4]);

		String[] expectedWordsErrorSix = textUtils.split(testSentence, " ", true);
		String[] testWordsErrorSix = textUtils.split(testSentence, null, true);
		assertEquals(5, testWordsErrorSix.length);
		assertEquals(expectedWordsErrorSix.length, testWordsErrorSix.length);
		assertEquals("This", testWordsErrorSix[0]);
		assertEquals("is", testWordsErrorSix[1]);
		assertEquals("a", testWordsErrorSix[2]);
		assertEquals("sample", testWordsErrorSix[3]);
		assertEquals("sentence", testWordsErrorSix[4]);
		assertEquals(expectedWordsErrorSix[0], testWordsErrorSix[0]);
		assertEquals(expectedWordsErrorSix[1], testWordsErrorSix[1]);
		assertEquals(expectedWordsErrorSix[2], testWordsErrorSix[2]);
		assertEquals(expectedWordsErrorSix[3], testWordsErrorSix[3]);
		assertEquals(expectedWordsErrorSix[4], testWordsErrorSix[4]);
	}

	public void testStringJoin()
	{
		String[] testWordsOne = { "This", "is", "a", "sentence" };
		String[] testWordsTwo = { "One ", " ring ", "to ", "rule", "them", " all   " };

		// Basic, trimmed words ...
		String testSentenceOne = textUtils.join(testWordsOne, " ");
		assertEquals("This is a sentence", testSentenceOne);

		String testSentenceTwo = textUtils.join(testWordsOne, null);
		assertEquals("Thisisasentence", testSentenceTwo);

		String testSentenceThree = textUtils.join(testWordsOne, ".");
		assertEquals("This.is.a.sentence", testSentenceThree);

		String testSentenceFour = textUtils.join(testWordsOne);
		assertEquals(testSentenceTwo, testSentenceFour);

        String testSentenceFive = textUtils.join(testWordsOne, " ", false);
		assertEquals(testSentenceOne, testSentenceFive);

		String testSentenceSix = textUtils.join(testWordsOne, " ", true);
		assertEquals(testSentenceOne, testSentenceSix);

		String testSentenceSeven = textUtils.join(testWordsOne, null, false);
		assertEquals(testSentenceTwo, testSentenceSeven);

		String testSentenceEight = textUtils.join(testWordsOne, null, true);
		assertEquals(testSentenceTwo, testSentenceEight);

		// Slightly more flexible, untrimmed words...
		String testSentenceTen = textUtils.join(testWordsTwo, " ");
		assertEquals("One   ring  to  rule them  all   ", testSentenceTen);

		String testSentenceEleven = textUtils.join(testWordsTwo, null);
		assertEquals("One  ring to rulethem all   ", testSentenceEleven);

		String testSentenceTwelve = textUtils.join(testWordsTwo, ".");
		assertEquals("One . ring .to .rule.them. all   ", testSentenceTwelve);

		String testSentenceThirteen = textUtils.join(testWordsTwo);
		assertEquals(testSentenceEleven, testSentenceThirteen);

		String testSentenceFourteen = textUtils.join(testWordsTwo, " ", false);
		assertEquals(testSentenceTen, testSentenceFourteen);

		String testSentenceFifteen = textUtils.join(testWordsTwo, " ", true);
		assertEquals("One ring to rule them all", testSentenceFifteen);

		//TODO: This test is not working properly.  The behavior is similar to the one for testSentenceSeventeen
		String testSentenceSixteen = textUtils.join(testWordsTwo, null, false);
//		assertEquals(testSentenceEleven, testSentenceSixteen);

		String testSentenceSeventeen = textUtils.join(testWordsTwo, null, true);
		assertEquals("Oneringtorulethemall", testSentenceSeventeen);
	}

	public void testStringJoinErrors()
	{
		String testSentenceOne = textUtils.join(null, null);
		assertNull(testSentenceOne);

		String testSentenceTwo = textUtils.join(null, " ");
		assertNull(testSentenceTwo);

		String testSentenceThree = textUtils.join(null);
		assertNull(testSentenceThree);
	}

	public void testStringSearchAndReplace()
	{
		String testSentenceOne = "One ring to rule them all";
		String testSentnceTwo = "And somehow do something to them";

		String rule = textUtils.replaceTextValues(testSentenceOne, "rule", "rule");
		assertEquals(testSentenceOne, rule);

		String find = textUtils.replaceTextValues(testSentenceOne, "rule", "find");
		assertEquals("One ring to find them all", find);

		String bring = textUtils.replaceTextValues(testSentenceOne, "rule", "bring");
		assertEquals("One ring to bring them all", bring);

		String bind = textUtils.replaceTextValues(testSentnceTwo, "somehow", "in the darkness");
		assertEquals("And in the darkness do something to them", bind);

		bind = textUtils.replaceTextValues(bind, "do something to", "bind");
		assertEquals("And in the darkness bind them", bind);
	}

	public void testStringSearchAndReplaceErrors()
	{
		String original = "Testing Code since 1990 - Weird Phrases Magazine";

		String replacedOne = textUtils.replaceTextValues(original, null, "test");
		assertEquals(original, replacedOne);

		String replacedTwo = textUtils.replaceTextValues(original, "since", null);
		assertEquals(original, replacedTwo);
	}

	public void testStringMiscellaneousUtilities()
	{
		assertTrue(textUtils.toBoolean("true"));
		assertTrue(textUtils.toBoolean("YeS"));
		assertTrue(textUtils.toBoolean("1"));
		assertTrue(textUtils.toBoolean("oN"));

		assertFalse(textUtils.toBoolean("faLse"));
		assertFalse(textUtils.toBoolean("NO"));
		assertFalse(textUtils.toBoolean("0"));
		assertFalse(textUtils.toBoolean("oFf"));
	}

	public void testStringIdentifierConversion()
	{
		// SQL to Identifier
        String sqlIdentifierOne = "disaster_recovery_items";

		String sqlTextOne = textUtils.sqlIdentifierToText(sqlIdentifierOne, false);
		assertEquals("Disaster recovery items", sqlTextOne);

		String sqlTextTwo = textUtils.sqlIdentifierToText(sqlIdentifierOne, true);
		assertEquals("Disaster Recovery Items", sqlTextTwo);

		// Java to XML Node Name
		String javaIdentifierOne = "disasterRecoveryItemList";
		String javaIdentifierTwo = "disaster_recovery_item_list";

		String xmlIdentifierOne = textUtils.javaIdentifierToXmlNodeName(javaIdentifierOne);
		assertEquals("disaster-Recovery-Item-List", xmlIdentifierOne);

		String xmlIdentifierTwo = textUtils.javaIdentifierToXmlNodeName(javaIdentifierTwo);
		assertEquals("disaster-_recovery-_item-_list", xmlIdentifierTwo);

		// XML to Java Constant
		String xmlTagOne = "all-work-and-no-play";
		String xmlTagTwo = ":makes_jack:a_dull.boy";
		String xmlTagThree = "04makes_jack_dull";

		String javaConstantOne = textUtils.xmlTextToJavaConstant(xmlTagOne);
		assertEquals("ALL_WORK_AND_NO_PLAY", javaConstantOne);

		String javaConstantTwo = textUtils.xmlTextToJavaConstant(xmlTagTwo);
		assertEquals("_MAKES_JACK_A_DULL_BOY", javaConstantTwo);

		String javaConstantThree = textUtils.xmlTextToJavaConstantTrimmed(xmlTagOne);
		assertEquals(javaConstantOne, javaConstantThree);

		String javaConstantFour = textUtils.xmlTextToJavaConstantTrimmed(xmlTagTwo);
		assertEquals("MAKES_JACK_A_DULL_BOY", javaConstantFour);

		String javaConstantFive = textUtils.xmlTextToJavaIdentifier(xmlTagOne, false);
		assertEquals("allWorkAndNoPlay", javaConstantFive);

		String javaConstantSix = textUtils.xmlTextToJavaIdentifier(xmlTagOne, true);
		assertEquals("AllWorkAndNoPlay", javaConstantSix);

		String javaConstantSeven = textUtils.xmlTextToJavaIdentifier(xmlTagTwo, false);
		assertEquals("_makesJackADull_boy", javaConstantSeven);

		String javaConstantEight = textUtils.xmlTextToJavaIdentifier(xmlTagTwo, true);
		assertEquals(javaConstantSeven, javaConstantEight);

		String javaConstantNine = textUtils.xmlTextToJavaIdentifier(xmlTagThree, true);
		assertEquals("_04makesJackDull", javaConstantNine);

		// XML to Node Name
		String xmlTextOne = "Jack_Be_Nimble";
		String xmlTextTwo = "_jack_be_:quick";

		String xmlNodeOne = textUtils.xmlTextToNodeName(xmlTextOne);
		assertEquals("jack-be-nimble", xmlNodeOne);

		String xmlNodeTwo = textUtils.xmlTextToNodeName(xmlTextTwo);
		assertEquals("-jack-be--quick", xmlNodeTwo);

		// Table Name Case Conversion
		String tableNameOne = "Jacks_CANdlEStick_manufacturers";
		String tableNameTwo = "CandleSTICK_LOCAtion";

		String tableOne = textUtils.fixupTableNameCase(tableNameOne);
		assertEquals("Jacks_Candlestick_Manufacturers", tableOne);

		String tableTwo = textUtils.fixupTableNameCase(tableNameTwo);
		assertEquals("Candlestick_Location", tableTwo);
	}

	public void testStringIdentifierConversionErrors()
	{
		// SQL -> Java
		assertNull(textUtils.sqlIdentifierToText(null, false));
		assertNull(textUtils.sqlIdentifierToText(null, true));

		// Java -> XML
		assertNull(textUtils.javaIdentifierToXmlNodeName(null));

		// XML -> Java
		assertNull(textUtils.xmlTextToJavaConstant(null));
		assertNull(textUtils.xmlTextToJavaConstantTrimmed(null));
		assertNull(textUtils.xmlTextToJavaIdentifier(null, false));
		assertNull(textUtils.xmlTextToJavaIdentifier(null, true));

		// XML -> Node Name
		assertNull(textUtils.xmlTextToNodeName(null));

		// Table Name
		assertNull(textUtils.fixupTableNameCase(null));
	}
}
