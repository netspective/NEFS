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
package com.netspective.commons.text;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse comma-separated values (CSV), a common Windows file format.
 * Sample input: "LU",86.25,"11/4/1998","2:19PM",+4.0625
 * <p/>
 * Inner logic adapted from a C++ original that was
 * Copyright (C) 1999 Lucent Technologies
 * Excerpted from 'The Practice of Programming'
 * by Brian W. Kernighan and Rob Pike.
 * <p/>
 * Included by permission of the http://tpop.awl.com/ web site,
 * which says:
 * "You may use this code for any purpose, as long as you leave
 * the copyright notice and book citation attached." I have done so.
 *
 * @author Brian W. Kernighan and Rob Pike (C++ original)
 * @author Ian F. Darwin (translation into Java and removal of I/O)
 * @author Ben Ballard (rewrote advQuoted to handle '""' and for readability)
 * @author Don Brown (wanted to return a String array instead of Iterator)
 */
public class DelimitedValuesParser
{
    public static final char DEFAULT_SEP = ',';

    /**
     * Construct a CSV parser, with the default separator (`,').
     */
    public DelimitedValuesParser()
    {
        this(DEFAULT_SEP);
    }

    /**
     * Construct a CSV parser with a given separator. Must be
     * exactly the string that is the separator, not a list of
     * separator characters!
     */
    public DelimitedValuesParser(char sep)
    {
        fieldSep = sep;
    }

    /**
     * The fields in the current String
     */
    protected ArrayList list = new ArrayList();

    /**
     * the separator char for this parser
     */
    protected char fieldSep;

    /**
     * parse: break the input String into fields
     *
     * @return String array containing each field
     *         from the original as a String, in order.
     */
    public List parseAsList(String line)
    {
        StringBuffer sb = new StringBuffer();
        list.clear();			// discard previous, if any
        int i = 0;

        if(line.length() == 0)
        {
            list.add(line);
            return list;
        }

        do
        {
            sb.setLength(0);
            if(i < line.length() && line.charAt(i) == '"')
                i = advQuoted(line, sb, ++i);	// skip quote
            else
                i = advPlain(line, sb, i);
            list.add(sb.toString());
            i++;
        }
        while(i < line.length());

        return list;
    }

    /**
     * parse: break the input String into fields
     *
     * @return String array containing each field
     *         from the original as a String, in order.
     */
    public String[] parse(String line)
    {
        StringBuffer sb = new StringBuffer();
        list.clear();			// discard previous, if any
        int i = 0;

        if(line.length() == 0)
        {
            list.add(line);
            return (String[]) list.toArray(new String[]{});
        }

        do
        {
            sb.setLength(0);
            if(i < line.length() && line.charAt(i) == '"')
                i = advQuoted(line, sb, ++i);	// skip quote
            else
                i = advPlain(line, sb, i);
            list.add(sb.toString());
//Debug.println("csv", sb.toString());
            i++;
        }
        while(i < line.length());

        return (String[]) list.toArray(new String[]{});
    }

    /**
     * advQuoted: quoted field; return index of next separator
     */
    protected int advQuoted(String s, StringBuffer sb, int i)
    {
        int j;
        int len = s.length();
        for(j = i; j < len; j++)
        {
            if(s.charAt(j) == '"' && j + 1 < len)
            {
                if(s.charAt(j + 1) == '"')
                {
                    j++; // skip escape char
                }
                else if(s.charAt(j + 1) == fieldSep)
                { //next delimeter
                    j++; // skip end quotes
                    break;
                }
            }
            else if(s.charAt(j) == '"' && j + 1 == len)
            { // end quotes at end of line
                break; //done
            }
            sb.append(s.charAt(j));	// regular character.
        }
        return j;
    }

    /**
     * advPlain: unquoted field; return index of next separator
     */
    protected int advPlain(String s, StringBuffer sb, int i)
    {
        int j;

        j = s.indexOf(fieldSep, i); // look for separator
        //Debug.println("csv", "i = " + i + ", j = " + j);
        if(j == -1)
        {               	// none found
            sb.append(s.substring(i));
            return s.length();
        }
        else
        {
            sb.append(s.substring(i, j));
            return j;
        }
    }
}
