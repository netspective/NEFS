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
 * $Id: TextUtils.java,v 1.19 2004-08-12 00:24:32 shahid.shah Exp $
 */

package com.netspective.commons.text;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.oro.text.perl.Perl5Util;

public class TextUtils
{
    private static final TextUtils INSTANCE = (TextUtils) DiscoverSingleton.find(TextUtils.class, TextUtils.class.getName());

    public static TextUtils getInstance()
    {
        return INSTANCE;
    }

    public static final String[] BOOLEAN_CHOICES = new String[]
    {
        "yes", "no",
        "true", "false",
        "on", "off",
        "1", "0"
    };

    /**
     * Return the name of the given cls that is different from the relativeTo class. Basically, this chops off the
     * package name of the cls that is equivalent to that of the relativeTo class.
     *
     * @param relativeTo
     * @param cls
     *
     * @return
     */
    public String getRelativeClassName(Class relativeTo, Class cls)
    {
        String className = cls.getName();
        String relativeToPkg = relativeTo.getPackage().getName();
        if (className.startsWith(relativeToPkg))
            return className.substring(relativeToPkg.length() + 1);
        else
            return className;
    }

    public String getClassNameWithoutPackage(String pkgAndClassName, char sep)
    {
        int classNameDelimPos = pkgAndClassName.lastIndexOf(sep);
        return classNameDelimPos != -1 ? pkgAndClassName.substring(classNameDelimPos + 1) : pkgAndClassName;
    }

    /**
     * A version of getClassNameWithoutPackage with sensible defaults for most common usage: getting only the class
     * name (without the package name) for a Java class
     *
     * @param pkgAndClassName
     *
     * @return
     */
    public String getClassNameWithoutPackage(String pkgAndClassName)
    {
        return getClassNameWithoutPackage(pkgAndClassName, '.');
    }

    public String getPackageName(String pkgAndClassName, char sep)
    {
        int classNameDelimPos = pkgAndClassName.lastIndexOf(sep);
        return classNameDelimPos != -1 ? pkgAndClassName.substring(0, classNameDelimPos) : null;
    }

    /**
     * A version of getPackageName with sensible defaults for most common usage: getting the package name for a Java
     * class
     *
     * @param pkgAndClassName
     *
     * @return
     */
    public String getPackageName(String pkgAndClassName)
    {
        return getPackageName(pkgAndClassName, '.');
    }

    public String[] split(String source, String delimiter, boolean trim)
    {
        if (source == null)
            return null;

        if (null == delimiter)
            delimiter = " ";

        List list = new ArrayList();
        StringTokenizer st = new StringTokenizer(source, delimiter);
        if (trim)
        {
            while (st.hasMoreTokens())
                list.add(st.nextToken().trim());
        }
        else
        {
            while (st.hasMoreTokens())
                list.add(st.nextToken());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * A version of split with sensible defaults for most common usage: splitting sentences into words
     *
     * @param source
     *
     * @return
     */
    public String[] split(String source)
    {
        return split(source, " ", false);
    }

    public String join(String[] source, String delimiter)
    {
        if (source == null) return null;
        if (null == delimiter)
            delimiter = "";

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < source.length; i++)
        {
            result.append(source[i]);
            if (i < source.length - 1)
                result.append(delimiter);
        }
        return result.toString();
    }

    /**
     * A version of join with sensible defaults for most common usage: concatenating multiple String objects together
     *
     * @param source
     *
     * @return
     */
    public String join(String[] source)
    {
        return join(source, "");
    }

    /**
     * A version of join with an additional parameter (trim) which automatically trims each String in the source and
     * then does the join.
     *
     * @param source
     * @param delimiter
     * @param trim
     *
     * @return
     */
    public String join(String[] source, String delimiter, boolean trim)
    {
        if (trim)
        {
            for (int i = 0; i < source.length; i++)
                source[i] = source[i].trim();
        }

        return join(source, delimiter);
    }

    /**
     * Perform a simple string replacement of findStr to replStr in origStr and returns the result. All instances
     * of findStr are replaced to replStr (regardless of how many there are). Not optimized for performance.
     *
     * @param originalText The source text
     * @param findText     The text to locate
     * @param replaceText  The text to replace for each findStr
     */
    public String replaceTextValues(final String originalText, final String findText, final String replaceText)
    {
        if (findText == null || replaceText == null)
            return originalText;

        final StringBuffer result = new StringBuffer();
        //startIdx and idxOld delimit various chunks of aInput; these
        //chunks always end where aOldPattern begins
        int startIdx = 0;
        int idxOld = 0;
        while ((idxOld = originalText.indexOf(findText, startIdx)) >= 0)
        {
            //grab a part of aInput which does not include aOldPattern
            result.append(originalText.substring(startIdx, idxOld));
            //add aNewPattern to take place of aOldPattern
            result.append(replaceText);

            //reset the startIdx to just after the current match, to see
            //if there are any further matches
            startIdx = idxOld + findText.length();
        }
        //the final chunk will go to the end of aInput
        result.append(originalText.substring(startIdx));
        return result.toString();
    }

    /**
     * Perform a simple string replacement of findStr to replStr in all the members of origStr and returns the result.
     * All instances of findStr are replaced to replStr (regardless of how many there are). Not optimized for
     * performance.
     *
     * @param originalText An array containing all the source texts on which this replacement should be performed
     * @param findText     The text to locate
     * @param replaceText  The text to replace for each findStr
     */
    public String[] replaceTextValues(final String[] originalText, final String findText, final String replaceText)
    {
        String[] returnValue = new String[originalText.length];

        if (null == findText || null == replaceText)
            returnValue = originalText;
        else
            for (int i = 0; i < originalText.length; i++)
                returnValue[i] = replaceTextValues(originalText[i], findText, replaceText);

        return returnValue;
    }

    public String[] getBooleanChoices()
    {
        return BOOLEAN_CHOICES;
    }

    /**
     * returns the boolean equivalent of a string, which is considered true
     * if either "on", "true", or "yes" is found, ignoring case.
     */
    public boolean toBoolean(String s)
    {
        return (s.equalsIgnoreCase("yes") ||
                s.equalsIgnoreCase("true") ||
                s.equalsIgnoreCase("on") ||
                s.equalsIgnoreCase("1"));
    }

    /**
     * returns the boolean equivalent of a string, which is considered true but allows a default value if s is null
     */
    public boolean toBoolean(String s, boolean valueIfNull)
    {
        if (s == null)
            return valueIfNull;
        else
            return toBoolean(s);
    }

    /**
     * Given some text, return it as a literal string (surrounded by single or double quotes).
     *
     * @param text               The text to surround as SQL literal
     * @param surroundWith       The string to place before and after the text
     * @param escapeText         Any special text that should be replaced (like ' to '' or " to \")
     * @param replaceWith        If escapeText is specified, this is the string to put in place of the escape
     * @param trim               Remove leading and trailing spaces
     * @param stripNewLines      True if newlines (carriage returns, etc) should be replaced by a single space
     * @param valueIfNullOrBlank The string to return if the string is null or has no text (blank string)
     *
     * @return The text surrounded by ' and with all internal ' characters escaped as ''
     */
    public String createLiteral(String text, String surroundWith, String escapeText, String replaceWith,
                                boolean trim, boolean stripNewLines, String valueIfNullOrBlank)
    {
        if (text == null || text.length() == 0)
            return valueIfNullOrBlank;

        if(stripNewLines)
            text = replaceTextValues(text, "\n", " ");

        if(trim)
            text = text.trim();

        if(escapeText != null)
            text = replaceTextValues(text, escapeText, replaceWith);

        StringBuffer sb = new StringBuffer(surroundWith);
        sb.append(text);
        sb.append(surroundWith);
        return sb.toString();
    }

    /**
     * Given a text string that defines a SQL table name or column name or other SQL identifier,
     * return a string that would be suitable for that string to be used as a caption or plain text.
     */
    public String sqlIdentifierToText(String original, boolean uppercaseEachWord)
    {
        if (original == null || original.length() == 0)
            return original;

        StringBuffer text = new StringBuffer();
        text.append(Character.toUpperCase(original.charAt(0)));
        boolean wordBreak = false;
        for (int i = 1; i < original.length(); i++)
        {
            char ch = original.charAt(i);
            if (ch == '_')
            {
                text.append(' ');
                wordBreak = true;
            }
            else if (wordBreak)
            {
                text.append(uppercaseEachWord ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
                wordBreak = false;
            }
            else
                text.append(Character.toLowerCase(ch));
        }
        return text.toString();
    }

    /**
     * Given a method name, return a string that would be suitable for that string to be used
     * as a xml node name. Basically, what this does is allows something like setAbcDef() to
     * match both "abcDef" and "abc-def" as node names in XML. It turns a java identifier into
     * a reasonable xml node name.
     */
    public String javaIdentifierToXmlNodeName(final String javaIdentifier)
    {
        if (javaIdentifier == null || javaIdentifier.length() == 0)
            return javaIdentifier;

        StringBuffer nodeName = new StringBuffer();
        nodeName.append(javaIdentifier.charAt(0));
        for (int i = 1; i < javaIdentifier.length(); i++)
        {
            //TODO: Might be a good idea to replace _ with - and to lower the case of any uppercase letters
            char ch = javaIdentifier.charAt(i);
            if (Character.isLowerCase(ch))
                nodeName.append(ch);
            else
            {
                nodeName.append('-');
                nodeName.append(ch);
            }
        }

        return nodeName.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for that string to be used
     * as a Java identifier (as a variable or method name). Depending upon whether ucaseInitial
     * is set, the string starts out with a lowercase or uppercase letter. Then, the rule is
     * to convert all periods into underscores and title case any words separated by
     * underscores. This has the effect of removing all underscores and creating mixed case
     * words. For example, Person_Address becomes personAddress or PersonAddress depending upon
     * whether ucaseInitial is set to true or false. Person.Address would become Person_Address.
     */
    public String xmlTextToJavaIdentifier(String xml, boolean ucaseInitial)
    {
        if (xml == null || xml.length() == 0)
            return xml;

        StringBuffer identifier = new StringBuffer();
        char ch = xml.charAt(0);
        if (Character.isJavaIdentifierStart(ch))
            identifier.append(ucaseInitial ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
        else
        {
            identifier.append('_');
            if (Character.isJavaIdentifierPart(ch))
                identifier.append(ucaseInitial ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
        }

        boolean uCase = false;
        for (int i = 1; i < xml.length(); i++)
        {
            ch = xml.charAt(i);
            if (ch == '.')
            {
                identifier.append('_');
            }
            else if (ch != '_' && Character.isJavaIdentifierPart(ch))
            {
                identifier.append(Character.isUpperCase(ch)
                        ? ch : (uCase ? Character.toUpperCase(ch) : Character.toLowerCase(ch)));
                uCase = false;
            }
            else
                uCase = true;
        }
        return identifier.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for that string to be used
     * as a Java package name. The rule is to leave all periods and treat words as XML identifiers.
     */
    public String xmlTextToJavaPackageName(String xml)
    {
        if (xml == null || xml.length() == 0)
            return xml;

        StringBuffer result = new StringBuffer();
        StringTokenizer st = new StringTokenizer(xml, ".");
        while (st.hasMoreTokens())
        {
            result.append(xmlTextToJavaIdentifier(st.nextToken(), false).toLowerCase());
            if (st.hasMoreTokens())
                result.append(".");
        }

        return result.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for that string to be used
     * as a Java constant (public static final XXX). The rule is to basically take every letter
     * or digit and return it in uppercase and every non-letter or non-digit as an underscore.
     */
    public String xmlTextToJavaConstant(String xml)
    {
        if (xml == null || xml.length() == 0)
            return xml;

        StringBuffer constant = new StringBuffer();
        for (int i = 0; i < xml.length(); i++)
        {
            char ch = xml.charAt(i);
            constant.append(Character.isJavaIdentifierPart(ch) ? Character.toUpperCase(ch) : '_');
        }
        return constant.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for that string to be used
     * as a Java constant (public static final XXX). The rule is to basically take every letter
     * or digit and return it in uppercase and every non-letter or non-digit as an underscore.
     * This trims all non-letter/digit characters from the beginning of the string.
     */
    public String xmlTextToJavaConstantTrimmed(String xml)
    {
        if (xml == null || xml.length() == 0)
            return xml;

        boolean stringStarted = false;
        StringBuffer constant = new StringBuffer();
        for (int i = 0; i < xml.length(); i++)
        {
            char ch = xml.charAt(i);
            if (Character.isJavaIdentifierPart(ch))
            {
                stringStarted = true;
                constant.append(Character.toUpperCase(ch));
            }
            else if (stringStarted)
                constant.append('_');
        }
        return constant.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for an XML element name. For example,
     * when given Person_Address it would return person-address. The rule is to basically take every letter
     * or digit and return it in lowercase and every non-letter or non-digit as a dash.
     */
    public String xmlTextToNodeName(String xml)
    {
        if (xml == null || xml.length() == 0)
            return xml;

        StringBuffer constant = new StringBuffer();
        for (int i = 0; i < xml.length(); i++)
        {
            char ch = xml.charAt(i);
            constant.append(Character.isLetterOrDigit(ch) ? Character.toLowerCase(ch) : '-');
        }
        return constant.toString();
    }

    /**
     * Return the given text unindented by whatever the first line is indented by
     *
     * @param text The original text
     *
     * @return Unindented text or original text if not indented
     */
    public String getUnindentedText(String text)
    {
        /*
         * if the string is indented, find out how far the first line is indented
         */
        StringBuffer replStr = new StringBuffer();
        for (int i = 0; i < text.length(); i++)
        {
            char ch = text.charAt(i);
            if (Character.isWhitespace(ch))
                replStr.append(ch);
            else
                break;
        }

        /*
         * If the first line is indented, unindent all the lines the distance of just the first line
         */
        Perl5Util perlUtil = new Perl5Util();

        if (replStr.length() > 0)
            return perlUtil.substitute("s/^" + replStr + "//gm", text).trim();
        else
            return text;
    }

    public String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Return the given block of text indented by the given string.
     *
     * @param text The original text
     *
     * @return Unindented text or original text if not indented
     */
    public String getIndentedText(String text, String indent, boolean appendNewLine)
    {
        text = getUnindentedText(text);

        /*
         * If the first line is indented, unindent all the lines the distance of just the first line
         */
        Perl5Util perlUtil = new Perl5Util();
        text = perlUtil.substitute("s/^/" + indent + "/gm", text);
        return appendNewLine ? text + "\n" : text;
    }

    /* make the table name title cased (cap each letter after _) */
    public String fixupTableNameCase(String tableNameOrig)
    {
        if (null == tableNameOrig)
            return null;

        StringBuffer tableNameBuf = new StringBuffer(tableNameOrig.toLowerCase());
        boolean capNext = false;
        for (int i = 0; i < tableNameBuf.length(); i++)
        {
            if (tableNameBuf.charAt(i) == '_')
                capNext = true;
            else
            {
                if (i == 0 || capNext)
                {
                    tableNameBuf.setCharAt(i, Character.toUpperCase(tableNameBuf.charAt(i)));
                    capNext = false;
                }
            }
        }
        return tableNameBuf.toString();
    }

    /**
     * Check if the given string is null, empty or made up only of whitespace.
     *
     * @param value a tested string
     *
     * @return true if the string is empty
     */
    public boolean isEmpty(String value)
    {
        if (value == null) return true;
        if (value.length() == 0) return true;
        if (value.trim().length() == 0) return true;
        return false;
    }

    /**
     * Retrieve the contents of the given URL into a String.
     *
     * @param strLocation The URL
     *
     * @return The text of the contents of the URL
     *
     * @throws IOException
     */
    public String getUrlContents(String strLocation) throws IOException
    {
        StringBuffer sb = new StringBuffer();
        URL url = new URL(strLocation);
        URLConnection urlConn = url.openConnection();
        InputStream urlIn = urlConn.getInputStream();
        int iRead = urlIn.read();
        while (iRead != -1)
        {
            sb.append((char) iRead);
            iRead = urlIn.read();
        }
        return sb.toString();
    }

    /**
     * Retrieve the contents of the given File into a String.
     *
     * @param location The URL
     *
     * @return The text of the contents of the File
     *
     * @throws IOException
     */
    public String getFileContents(String location) throws IOException
    {
        StringBuffer sb = new StringBuffer();
        InputStream is = new BufferedInputStream(new FileInputStream(location));
        int iRead = is.read();
        while (iRead != -1)
        {
            sb.append((char) iRead);
            iRead = is.read();
        }
        return sb.toString();
    }

    public String pad(String text, int length, String fillWith)
    {
        if(text != null && text.length() > length)
            return text;

        if(text == null)
            text = "";

        StringBuffer sb = new StringBuffer(text);
        while(sb.length() < length)
            sb.append(fillWith);
        return sb.toString();
    }

    /**
     * Retrieve lines of text from an input stream
     *
     * @param location        The fully qualified name of the file to read
     * @param startLineNumber The starting line number
     * @param endLineNumber   The ending line number
     *
     * @return The text contained in line numbers startingLineNumber to endingLineNumber
     *
     * @throws IOException
     */
    public String getTextFileLines(String location, int startLineNumber, int endLineNumber) throws IOException
    {
        return getTextStreamLines(new FileInputStream(location), startLineNumber, endLineNumber);
    }

    /**
     * Retrieve lines of text from an input stream
     *
     * @param is              The input stream to read
     * @param startLineNumber The starting line number
     * @param endLineNumber   The ending line number
     *
     * @return The text contained in line numbers startingLineNumber to endingLineNumber
     *
     * @throws IOException
     */
    public String getTextStreamLines(InputStream is, int startLineNumber, int endLineNumber) throws IOException
    {
        if (is == null)
            return null;

        if (startLineNumber <= 0 && endLineNumber <= 0)
            return null;

        Reader isReader = null;
        LineNumberReader reader = null;
        StringBuffer result = new StringBuffer();

        try
        {
            isReader = new InputStreamReader(is);
            reader = new LineNumberReader(isReader);

            String line = null;

            if (startLineNumber > 0 && endLineNumber <= 0)
            {
                while ((line = reader.readLine()) != null)
                {
                    if (reader.getLineNumber() == startLineNumber)
                        return line;
                }

            }
            else
            {
                while ((line = reader.readLine()) != null)
                {
                    int lineNumber = reader.getLineNumber();

                    if (lineNumber < startLineNumber)
                        continue;

                    if (lineNumber > endLineNumber)
                        break;

                    result.append(line);
                    result.append("\n");
                }
            }
        }
        finally
        {
            if (reader != null)
                reader.close();

            if (isReader != null)
                is.close();

            is.close();
        }

        return result.toString();
    }

    public final String escapeHTML(String s)
    {
        if (s == null) return null;
        StringBuffer sb = new StringBuffer();
        int n = s.length();
        for (int i = 0; i < n; i++)
        {
            char c = s.charAt(i);
            switch (c)
            {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case 'à':
                    sb.append("&agrave;");
                    break;
                case 'À':
                    sb.append("&Agrave;");
                    break;
                case 'â':
                    sb.append("&acirc;");
                    break;
                case 'Â':
                    sb.append("&Acirc;");
                    break;
                case 'ä':
                    sb.append("&auml;");
                    break;
                case 'Ä':
                    sb.append("&Auml;");
                    break;
                case 'å':
                    sb.append("&aring;");
                    break;
                case 'Å':
                    sb.append("&Aring;");
                    break;
                case 'æ':
                    sb.append("&aelig;");
                    break;
                case 'Æ':
                    sb.append("&AElig;");
                    break;
                case 'ç':
                    sb.append("&ccedil;");
                    break;
                case 'Ç':
                    sb.append("&Ccedil;");
                    break;
                case 'é':
                    sb.append("&eacute;");
                    break;
                case 'É':
                    sb.append("&Eacute;");
                    break;
                case 'è':
                    sb.append("&egrave;");
                    break;
                case 'È':
                    sb.append("&Egrave;");
                    break;
                case 'ê':
                    sb.append("&ecirc;");
                    break;
                case 'Ê':
                    sb.append("&Ecirc;");
                    break;
                case 'ë':
                    sb.append("&euml;");
                    break;
                case 'Ë':
                    sb.append("&Euml;");
                    break;
                case 'ï':
                    sb.append("&iuml;");
                    break;
                case 'Ï':
                    sb.append("&Iuml;");
                    break;
                case 'ô':
                    sb.append("&ocirc;");
                    break;
                case 'Ô':
                    sb.append("&Ocirc;");
                    break;
                case 'ö':
                    sb.append("&ouml;");
                    break;
                case 'Ö':
                    sb.append("&Ouml;");
                    break;
                case 'ø':
                    sb.append("&oslash;");
                    break;
                case 'Ø':
                    sb.append("&Oslash;");
                    break;
                case 'ß':
                    sb.append("&szlig;");
                    break;
                case 'ù':
                    sb.append("&ugrave;");
                    break;
                case 'Ù':
                    sb.append("&Ugrave;");
                    break;
                case 'û':
                    sb.append("&ucirc;");
                    break;
                case 'Û':
                    sb.append("&Ucirc;");
                    break;
                case 'ü':
                    sb.append("&uuml;");
                    break;
                case 'Ü':
                    sb.append("&Uuml;");
                    break;
                case '®':
                    sb.append("&reg;");
                    break;
                case '©':
                    sb.append("&copy;");
                    break;
                case '€':
                    sb.append("&euro;");
                    break;

                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
