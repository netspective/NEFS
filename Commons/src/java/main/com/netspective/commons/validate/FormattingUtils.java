/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.commons.validate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormattingUtils implements Serializable
{

    /**
     * <p>Checks if the value can safely be converted to a byte primitive.</p>
     *
     * @param value The value validation is being performed on.
     */
    public static Byte formatByte(String value)
    {
        Byte result = null;

        try
        {
            result = new Byte(value);
        }
        catch(Exception e)
        {
        }

        return result;
    }

    /**
     * <p>Checks if the value can safely be converted to a short primitive.</p>
     *
     * @param value The value validation is being performed on.
     */
    public static Short formatShort(String value)
    {
        Short result = null;

        try
        {
            result = new Short(value);
        }
        catch(Exception e)
        {
        }

        return result;
    }

    /**
     * <p>Checks if the value can safely be converted to a int primitive.</p>
     *
     * @param value The value validation is being performed on.
     */
    public static Integer formatInt(String value)
    {
        Integer result = null;

        try
        {
            result = new Integer(value);
        }
        catch(Exception e)
        {
        }

        return result;
    }

    /**
     * <p>Checks if the value can safely be converted to a long primitive.</p>
     *
     * @param value The value validation is being performed on.
     */
    public static Long formatLong(String value)
    {
        Long result = null;

        try
        {
            result = new Long(value);
        }
        catch(Exception e)
        {
        }

        return result;
    }

    /**
     * <p>Checks if the value can safely be converted to a float primitive.</p>
     *
     * @param value The value validation is being performed on.
     */
    public static Float formatFloat(String value)
    {
        Float result = null;

        try
        {
            result = new Float(value);
        }
        catch(Exception e)
        {
        }

        return result;
    }

    /**
     * <p>Checks if the value can safely be converted to a double primitive.</p>
     *
     * @param value The value validation is being performed on.
     */
    public static Double formatDouble(String value)
    {
        Double result = null;

        try
        {
            result = new Double(value);
        }
        catch(Exception e)
        {
        }

        return result;
    }

    /**
     * <p>Checks if the field is a valid date.  The <code>Locale</code> is
     * used with <code>java.text.DateFormat</code>.  The setLenient method
     * is set to <code>false</code> for all.</p>
     *
     * @param value  The value validation is being performed on.
     * @param locale The Locale to use to parse the date (system default if null)
     */
    public static Date formatDate(String value, Locale locale)
    {
        Date date = null;

        if(value != null)
        {
            try
            {
                DateFormat formatter = null;
                if(locale != null)
                {
                    formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                }
                else
                {
                    formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
                }

                formatter.setLenient(false);

                date = formatter.parse(value);
            }
            catch(ParseException e)
            {
            }
        }

        return date;
    }

    /**
     * <p>Checks if the field is a valid date.  The pattern is used with
     * <code>java.text.SimpleDateFormat</code>.  If strict is true, then the
     * length will be checked so '2/12/1999' will not pass validation with
     * the format 'MM/dd/yyyy' because the month isn't two digits.
     * The setLenient method is set to <code>false</code> for all.</p>
     *
     * @param value       The value validation is being performed on.
     * @param datePattern The pattern passed to <code>SimpleDateFormat</code>.
     * @param strict      Whether or not to have an exact match of the datePattern.
     */
    public static Date formatDate(String value, String datePattern, boolean strict)
    {
        Date date = null;

        if(value != null && datePattern != null && datePattern.length() > 0)
        {
            try
            {
                SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
                formatter.setLenient(false);

                date = formatter.parse(value);

                if(strict)
                {
                    if(datePattern.length() != value.length())
                    {
                        date = null;
                    }
                }
            }
            catch(ParseException e)
            {
            }
        }

        return date;
    }

    /**
     * <p>Checks if the field is a valid credit card number.</p>
     * <p>Translated to Java by Ted Husted (<a href="mailto:husted@apache.org">husted@apache.org</a>).<br>
     * &nbsp;&nbsp;&nbsp; Reference Sean M. Burke's script at http://www.ling.nwu.edu/~sburke/pub/luhn_lib.pl</p>
     *
     * @param value The value validation is being performed on.
     */
    public static Long formatCreditCard(String value)
    {
        Long result = null;

        try
        {
            if(ValidationUtils.validateCreditCardLuhnCheck(value) && ValidationUtils.validateCreditCardPrefixCheck(value))
            {
                result = new Long(value);
            }
        }
        catch(Exception e)
        {
        }

        return result;
    }

}
