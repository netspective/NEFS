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
 * $Id: ExpressionTextTest.java,v 1.2 2003-03-20 05:38:25 shahbaz.javeed Exp $
 */

package com.netspective.commons.text;

import java.util.Map;
import java.util.HashMap;

import junit.framework.TestCase;

import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.DefaultValueContext;
import org.apache.commons.jexl.JexlContext;

public class ExpressionTextTest extends TestCase
{
    private static final Map replaceTexts = new HashMap();

    static
    {
        replaceTexts.put("one", "[one]-replaced");
        replaceTexts.put("two", "[two]-replaced");
    }

    public static class ExpressionTestClass extends ExpressionText
    {
        protected String getReplacement(ValueContext vc, String entireText, String replaceToken)
        {
            return (String) replaceTexts.get(replaceToken);
        }
    }

    public static final String TEXT_BEFORE_REPLACE =
            "This is a test string that has two replacements that should be replaced: "+
            "${one} and ${two}";

    public static final String TEXT_AFTER_REPLACE =
            "This is a test string that has two replacements that should be replaced: "+
            "[one]-replaced and [two]-replaced";

    public void testSimpleExpressionReplacement()
    {
		// Expression Test
        ExpressionTestClass etc = new ExpressionTestClass();
        String afterReplace = etc.getFinalText(null, TEXT_BEFORE_REPLACE);
        assertEquals(TEXT_AFTER_REPLACE, afterReplace);
    }

	public void testJavaExpressionText ()
	{
		// Java Expression Test
		JavaExpressionText jetOne = new JavaExpressionText();
		assertNotNull(jetOne);

		JexlContext jc = jetOne.getJexlContext();
		assertNotNull(jc);

		String javaExpInputOne = "2 + 2 = ${2 + 2}";
		String javaExpOutput = jetOne.getFinalText(null, javaExpInputOne);
		assertEquals("2 + 2 = 4", javaExpOutput);

		jc.getVars().put("pinky", new String("pinky"));
		String javaExpInputTwo = "'pinky' uppercased is ${pinky.toUpperCase()}";
		String javaExpOutputTwo = jetOne.getFinalText(null, javaExpInputTwo);
		assertEquals("'pinky' uppercased is PINKY", javaExpOutputTwo);

		Map vars = new HashMap();
		vars.put("theBrain", new String("The Brain"));
		vars.put("pi", new Float(3.14));
		vars.put("radius", new Float(2.5));

		JavaExpressionText jetTwo = new JavaExpressionText("static expression");
		assertNotNull(jetTwo);

		JexlContext jcTwo = jetTwo.getJexlContext();
		assertNotNull(jcTwo);

		jetTwo.init(vars);

		String javaExpInputThree = "Area = pi * radius^2 = ${pi * radius * radius}";
		String javaExpOutputThree = jetTwo.getFinalText(null, javaExpInputThree);
		assertEquals("Area = pi * radius^2 = 19.625", javaExpOutputThree);
		assertEquals("static expression", jetTwo.getFinalText(null));
		assertEquals("static expression", jetTwo.getStaticExpr());

		JavaExpressionText jetThree = new JavaExpressionText("${theBrain} is a static expression", vars);
		assertNotNull(jetThree);

		String javaExpInputFour = "Pinky's smarter half: ${theBrain}";
		String javaExpOutputFour = jetThree.getFinalText(null, javaExpInputFour);
		assertEquals("Pinky's smarter half: The Brain", javaExpOutputFour);
		assertEquals("The Brain is a static expression", jetThree.getFinalText(null));
		assertEquals("${theBrain} is a static expression", jetThree.getStaticExpr());

		JavaExpressionText jetFour = new JavaExpressionText(vars);
		assertNotNull(jetFour);

		String javaExpInputFive = "Tom and Jerry like ${pi}";
		String javaExpOutputFive = jetFour.getFinalText(null, javaExpInputFive);
		assertEquals("Tom and Jerry like 3.14", javaExpOutputFive);
		assertNull(jetFour.getStaticExpr());

		ValueContext testVC = new DefaultValueContext();
		testVC.setAttribute("test-attribute", new String("Test Attribute - Do NOT Use"));
		assertEquals(javaExpOutputFive, jetFour.getFinalText(testVC, javaExpInputFive));
	}

	public void testJavaExpressionTextErrors()
	{
		Map vars = new HashMap();
		vars.put("theBrain", new String("The Brain"));
		vars.put("pi", new Float(3.14));
		vars.put("radius", new Float(2.5));

		JavaExpressionText jetFour = new JavaExpressionText(vars);
		assertNotNull(jetFour);

		String javaExpInputFive = "Tom and Jerry like ${pi}";
		String javaExpOutputFive = jetFour.getFinalText(null, javaExpInputFive);
		assertEquals("Tom and Jerry like 3.14", javaExpOutputFive);
		assertNull(jetFour.getStaticExpr());

		String javaExpInputSix = "The perimeter of this circle is: ${perimeter}";
		boolean exceptionThrown = true;
		String javaExpOutputSix = null;

		try {
			javaExpOutputSix = jetFour.getFinalText(null, javaExpInputSix);
			exceptionThrown = false;
		} catch (ExpressionTextException e) {
			assertTrue(exceptionThrown);
		}

		assertNull(javaExpOutputSix);

		ValueContext testVC = new DefaultValueContext();
		testVC.setAttribute("test-attribute", new String("Test Attribute - Do NOT Use"));
		assertEquals(javaExpOutputFive, jetFour.getFinalText(testVC, javaExpInputFive));

		exceptionThrown = true;
		String javaExpOutputString = null;

		try {
			javaExpOutputString = jetFour.getFinalText(null);
			exceptionThrown = false;
		} catch (Exception e) {
			assertTrue(exceptionThrown);
			assertNull(javaExpOutputString);
		}

		exceptionThrown = true;
		javaExpOutputString = null;

		try {
			javaExpOutputString = jetFour.getFinalText(testVC);
			exceptionThrown = false;
		} catch (Exception e) {
			assertTrue(exceptionThrown);
			assertNull(javaExpOutputString);
		}
	}

	public void testValueSourceExpressionText()
	{
		// ValueSource Expression Test
		ValueSourceExpressionText vsetOne = new ValueSourceExpressionText();
		assertNotNull(vsetOne);

		String vsInputOne = "Life is ${static:good}";
		String vsOutputOne = vsetOne.getFinalText(null, vsInputOne);
		assertEquals("Life is good", vsOutputOne);

		ValueSourceExpressionText vsetTwo = new ValueSourceExpressionText("static-expression");
		assertNotNull(vsetTwo);

		String vsInputTwo = "Life is ${not:bad}";
		boolean exceptionThrown = true;

		try {
			String vsOutputTwo = vsetTwo.getFinalText(null, vsInputTwo);
			exceptionThrown = false;
		} catch (ExpressionTextException e) {
			assertTrue(exceptionThrown);
		}
	}

	public void testValueSourceOrJavaExpressionText ()
	{
		// Java Expression Test
		ValueSourceOrJavaExpressionText jetOne = new ValueSourceOrJavaExpressionText();
		assertNotNull(jetOne);

		JexlContext jc = jetOne.getJexlContext();
		assertNotNull(jc);

		String javaExpInputOne = "2 + 2 = ${2 + 2}";
		String javaExpOutput = jetOne.getFinalText(null, javaExpInputOne);
		assertEquals("2 + 2 = 4", javaExpOutput);

		jc.getVars().put("pinky", new String("pinky"));
		String javaExpInputTwo = "'pinky' uppercased is ${pinky.toUpperCase()}";
		String javaExpOutputTwo = jetOne.getFinalText(null, javaExpInputTwo);
		assertEquals("'pinky' uppercased is PINKY", javaExpOutputTwo);

		Map vars = new HashMap();
		vars.put("theBrain", new String("The Brain"));
		vars.put("pi", new Float(3.14));
		vars.put("radius", new Float(2.5));

		ValueSourceOrJavaExpressionText jetTwo = new ValueSourceOrJavaExpressionText("${static:static} expression");
		assertNotNull(jetTwo);

		JexlContext jcTwo = jetTwo.getJexlContext();
		assertNotNull(jcTwo);

		jetTwo.init(vars);

		String javaExpInputThree = "Area = pi * radius^2 = ${pi * radius * radius}";
		String javaExpOutputThree = jetTwo.getFinalText(null, javaExpInputThree);
		assertEquals("Area = pi * radius^2 = 19.625", javaExpOutputThree);
		assertEquals("static expression", jetTwo.getFinalText(null));
		assertEquals("${static:static} expression", jetTwo.getStaticExpr());

		ValueSourceOrJavaExpressionText jetThree = new ValueSourceOrJavaExpressionText("${theBrain} is a ${static:static expression}", vars);
		assertNotNull(jetThree);

		String javaExpInputFour = "Pinky's smarter half: ${theBrain}";
		String javaExpOutputFour = jetThree.getFinalText(null, javaExpInputFour);
		assertEquals("Pinky's smarter half: The Brain", javaExpOutputFour);
		assertEquals("The Brain is a static expression", jetThree.getFinalText(null));
		assertEquals("${theBrain} is a ${static:static expression}", jetThree.getStaticExpr());

		ValueSourceOrJavaExpressionText jetFour = new ValueSourceOrJavaExpressionText(vars);
		assertNotNull(jetFour);

		String javaExpInputFive = "Tom and Jerry like ${pi}";
		String javaExpOutputFive = jetFour.getFinalText(null, javaExpInputFive);
		assertEquals("Tom and Jerry like 3.14", javaExpOutputFive);
		assertNull(jetFour.getStaticExpr());

		ValueContext testVC = new DefaultValueContext();
		testVC.setAttribute("test-attribute", new String("Test Attribute - Do NOT Use"));
		assertEquals(javaExpOutputFive, jetFour.getFinalText(testVC, javaExpInputFive));
	}

	public void testValueSourceOrJavaExpressionTextErrors()
	{
		Map vars = new HashMap();
		vars.put("theBrain", new String("The Brain"));
		vars.put("pi", new Float(3.14));
		vars.put("radius", new Float(2.5));

		ValueSourceOrJavaExpressionText jetFour = new ValueSourceOrJavaExpressionText(vars);
		assertNotNull(jetFour);

		String javaExpInputFive = "Tom and ${static:Jerry} like ${pi}";
		String javaExpOutputFive = jetFour.getFinalText(null, javaExpInputFive);
		assertEquals("Tom and Jerry like 3.14", javaExpOutputFive);
		assertNull(jetFour.getStaticExpr());

		String javaExpInputSix = "The ${static:perimeter} of this circle is: ${perimeter}";
		boolean exceptionThrown = true;
		String javaExpOutputSix = null;

		try {
			javaExpOutputSix = jetFour.getFinalText(null, javaExpInputSix);
			exceptionThrown = false;
		} catch (ExpressionTextException e) {
			assertTrue(exceptionThrown);
		}

		assertNull(javaExpOutputSix);

		ValueContext testVC = new DefaultValueContext();
		testVC.setAttribute("test-attribute", new String("Test Attribute - Do NOT Use"));
		assertEquals(javaExpOutputFive, jetFour.getFinalText(testVC, javaExpInputFive));

		exceptionThrown = true;
		String javaExpOutputString = null;

		try {
			javaExpOutputString = jetFour.getFinalText(null);
			exceptionThrown = false;
		} catch (Exception e) {
			assertTrue(exceptionThrown);
			assertNull(javaExpOutputString);
		}

		exceptionThrown = true;
		javaExpOutputString = null;

		try {
			javaExpOutputString = jetFour.getFinalText(testVC);
			exceptionThrown = false;
		} catch (Exception e) {
			assertTrue(exceptionThrown);
			assertNull(javaExpOutputString);
		}
	}

}
