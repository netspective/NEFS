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
 * $Id: ExpressionText.java,v 1.1 2003-03-13 18:33:11 shahid.shah Exp $
 */

package com.netspective.commons.text;

import com.netspective.commons.value.ValueContext;

/**
 * Manages a String with ${xxx} expressions inside it. Each ${xxx} will automatically call the getReplacement()
 * method when the doReplacements() method is called.
 */
abstract public class ExpressionText
{
    public static final String EXPRESSION_REPLACEMENT_PREFIX = "${";
    public static final String EXPRESSION_REPLACEMENT_SUFFIX = "}";

    public static final String EXPRESSION_REPLACEMENT_FIRST_CHAR = "$";
    public static final char EXPRESSION_REPLACEMENT_SECOND_CHAR = '{';
    public static final char EXPRESSION_REPLACEMENT_LAST_CHAR = '}';

    private String staticExpr;
    private boolean staticExprHasReplacements;

    public ExpressionText()
    {
    }

    public ExpressionText(String staticExpr)
    {
        setStaticExpr(staticExpr);
    }

    public static boolean hasExpression(String text)
    {
        return text.indexOf(EXPRESSION_REPLACEMENT_PREFIX) != -1;
    }

    public String getStaticExpr()
    {
        return staticExpr;
    }

    public void setStaticExpr(String staticExpr)
    {
        this.staticExpr = staticExpr;
        staticExprHasReplacements = hasExpression(staticExpr);
    }

    /**
     * Callback method that is called within getFinalText() whenever there is a ${xxx} formatted text string found.
     * @param replaceToken The text inside the ${ and } characters.
     * @return
     */
    abstract protected String getReplacement(ValueContext vc, String entireText, String replaceToken) throws ExpressionTextException;

    /**
     * This method is called to reconstruct a text string so that it looks like the original replacement expression.
     * @param text The text that was inside the ${ and } characters.
     */
    public String getOriginalReplacement(String text)
    {
        return EXPRESSION_REPLACEMENT_PREFIX + text + EXPRESSION_REPLACEMENT_SUFFIX;
    }

    /**
     * Given a static expression, replace all ${xxx} text strings by calling getReplacement() method for each occurrence.
     * @return The replaced text
     */
    public String getFinalText(ValueContext vc)
    {
        if(staticExpr == null)
            throw new RuntimeException("Static expression not provided.");

        if(staticExprHasReplacements)
            return getFinalText(vc, staticExpr);
        else
            return staticExpr;
    }

    /**
     * Given a value, replace all ${xxx} text strings by calling getReplacement() method for each occurrence.
     * @param value The original text
     * @return The replaced text
     */
    public String getFinalText(ValueContext vc, String value) throws ExpressionTextException
    {
        StringBuffer result = new StringBuffer();

        int prev = 0;

        int pos;
        while((pos = value.indexOf(EXPRESSION_REPLACEMENT_FIRST_CHAR, prev)) >= 0)
        {
            if(pos > 0)
            {
                result.append(value.substring(prev, pos));
            }
            if(pos == (value.length() - 1))
            {
                result.append('$');
                prev = pos + 1;
            }
            else if(value.charAt(pos + 1) != EXPRESSION_REPLACEMENT_SECOND_CHAR)
            {
                result.append(value.charAt(pos + 1));
                prev = pos + 2;
            }
            else
            {
                int endName = value.indexOf(EXPRESSION_REPLACEMENT_LAST_CHAR, pos);
                if(endName < 0)
                    throw new RuntimeException("Syntax error in expression: " + value);

                String replaceText = value.substring(pos + 2, endName);
                String replacedText = getReplacement(vc, value, replaceText);

                result.append(replacedText);
                prev = endName + 1;
            }
        }

        if(prev < value.length()) result.append(value.substring(prev));
        return result.toString();
    }
}
