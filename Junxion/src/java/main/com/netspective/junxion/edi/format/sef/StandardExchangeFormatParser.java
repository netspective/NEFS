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
package com.netspective.junxion.edi.format.sef;

import java.util.ArrayList;
import java.util.List;

public class StandardExchangeFormatParser implements StandardExchangeFormatParserConstants
{
    static private Token formatVersion;
    static private List initFields;

    /**
     * *********************************
     * PARSER SPECIFICATIONS BEGIN HERE *
     * **********************************
     */
    static final public void formatFile() throws ParseException
    {
        header();
        System.out.println("Version: " + formatVersion);
        System.out.println("Init: " + initFields);
    }

    static final public void header() throws ParseException
    {
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
        {
            case 1:
                versionSection();
                break;
            default:
                jj_la1[0] = jj_gen;
                ;
        }
        initSection();
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
        {
            case 3:
                privateSection();
                break;
            default:
                jj_la1[1] = jj_gen;
                ;
        }
        setsSection();
    }

    static final public void versionSection() throws ParseException
    {
        jj_consume_token(1);
        formatVersion = freeText();
    }

    static final public void initSection() throws ParseException
    {
        jj_consume_token(2);
        jj_consume_token(EOL);
        initFields = commaSeparatedTextList();
        jj_consume_token(EOL);
    }

    static final public void privateSection() throws ParseException
    {
        jj_consume_token(3);
        ignoreTextToPublicSection();
    }

    static final public void setsSection() throws ParseException
    {
        jj_consume_token(4);
        jj_consume_token(EOL);
        equalText();
        segmentRefs();
    }

    static final public void segmentRefs() throws ParseException
    {
        segmentRef();
        label_1:
        while (true)
        {
            switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
            {
                case 5:
                case TABLE_START:
                case LBRACKET:
                    ;
                    break;
                default:
                    jj_la1[2] = jj_gen;
                    break label_1;
            }
            segmentRef();
        }
        jj_consume_token(EOL);
    }

    static final public void segmentRef() throws ParseException
    {
        Token segmentId = null;
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
        {
            case TABLE_START:
                jj_consume_token(TABLE_START);
                break;
            default:
                jj_la1[3] = jj_gen;
                ;
        }
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
        {
            case 5:
                jj_consume_token(5);
                jj_consume_token(INTEGER);
                break;
            default:
                jj_la1[4] = jj_gen;
                ;
        }
        jj_consume_token(LBRACKET);
        segmentId = jj_consume_token(SEGMENT_ID);
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
        {
            case COMMA:
                jj_consume_token(COMMA);
                switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
                {
                    case 6:
                        jj_consume_token(6);
                        break;
                    case INTEGER:
                        jj_consume_token(INTEGER);
                        break;
                    default:
                        jj_la1[5] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                break;
            default:
                jj_la1[6] = jj_gen;
                ;
        }
        jj_consume_token(RBRACKET);
        System.out.println("Segment ID: " + segmentId);
    }

    /**
     * ****************************************************************************
     * Utility methods for consuming comma separated text fields up to a line feed *
     * *****************************************************************************
     */
    static final public List commaSeparatedTextList() throws ParseException
    {
        List list = new ArrayList();
        commaSeparatedText(list);
        label_2:
        while (true)
        {
            switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk)
            {
                case COMMA:
                    ;
                    break;
                default:
                    jj_la1[7] = jj_gen;
                    break label_2;
            }
            jj_consume_token(COMMA);
            commaSeparatedText(list);
        }
        {if (true) return list;}
        throw new Error("Missing return statement in function");
    }

    static final public Token commaSeparatedText(List list) throws ParseException
    {
        Token token = null;
        token_source.SwitchTo(StandardExchangeFormatParserConstants.CommaSepTextDataSect);
        token = jj_consume_token(COMMASEPTEXTDATA);
        token_source.SwitchTo(DEFAULT);
        list.add(token.toString());
        {if (true) return token;}
        throw new Error("Missing return statement in function");
    }

    /**
     * ********************************************************************
     * Utility method for consuming all free text up to a line fee/newline *
     * *********************************************************************
     */
    static final public Token freeText() throws ParseException
    {
        Token token = null;
        token_source.SwitchTo(StandardExchangeFormatParserConstants.FreeTextDataSect);
        token = jj_consume_token(FREE_TEXT_DATA);
        jj_consume_token(FREE_TEXT_DATA_END);
        {if (true) return token;}
        throw new Error("Missing return statement in function");
    }

    static final public Token equalText() throws ParseException
    {
        Token token = null;
        token_source.SwitchTo(StandardExchangeFormatParserConstants.EqualTextDataSect);
        token = jj_consume_token(EQUAL_TEXT_DATA);
        jj_consume_token(EQUAL_TEXT_DATA_END);
        {if (true) return token;}
        throw new Error("Missing return statement in function");
    }

    static final public void ignoreTextToPublicSection() throws ParseException
    {
        token_source.SwitchTo(StandardExchangeFormatParserConstants.IgnoreToPublicSect);
        jj_consume_token(IGNORE_TO_PUBLIC_SECTION);
    }

    static private boolean jj_initialized_once = false;
    static public StandardExchangeFormatParserTokenManager token_source;
    static SimpleCharStream jj_input_stream;
    static public Token token, jj_nt;
    static private int jj_ntk;
    static private int jj_gen;
    static final private int[] jj_la1 = new int[8];
    static final private int[] jj_la1_0 = {0x2, 0x8, 0x40a0, 0x80, 0x20, 0x100040, 0x20000, 0x20000, };

    public StandardExchangeFormatParser(java.io.InputStream stream)
    {
        if (jj_initialized_once)
        {
            System.out.println("ERROR: Second call to constructor of static parser.  You must");
            System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        jj_initialized_once = true;
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new StandardExchangeFormatParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    }

    static public void ReInit(java.io.InputStream stream)
    {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    }

    public StandardExchangeFormatParser(java.io.Reader stream)
    {
        if (jj_initialized_once)
        {
            System.out.println("ERROR: Second call to constructor of static parser.  You must");
            System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        jj_initialized_once = true;
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new StandardExchangeFormatParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    }

    static public void ReInit(java.io.Reader stream)
    {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    }

    public StandardExchangeFormatParser(StandardExchangeFormatParserTokenManager tm)
    {
        if (jj_initialized_once)
        {
            System.out.println("ERROR: Second call to constructor of static parser.  You must");
            System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        jj_initialized_once = true;
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    }

    public void ReInit(StandardExchangeFormatParserTokenManager tm)
    {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    }

    static final private Token jj_consume_token(int kind) throws ParseException
    {
        Token oldToken;
        if ((oldToken = token).next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind)
        {
            jj_gen++;
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    static final public Token getNextToken()
    {
        if (token.next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    static final public Token getToken(int index)
    {
        Token t = token;
        for (int i = 0; i < index; i++)
        {
            if (t.next != null)
                t = t.next;
            else
                t = t.next = token_source.getNextToken();
        }
        return t;
    }

    static final private int jj_ntk()
    {
        if ((jj_nt = token.next) == null)
            return (jj_ntk = (token.next = token_source.getNextToken()).kind);
        else
            return (jj_ntk = jj_nt.kind);
    }

    static private java.util.Vector jj_expentries = new java.util.Vector();
    static private int[] jj_expentry;
    static private int jj_kind = -1;

    static final public ParseException generateParseException()
    {
        jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[28];
        for (int i = 0; i < 28; i++)
        {
            la1tokens[i] = false;
        }
        if (jj_kind >= 0)
        {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 8; i++)
        {
            if (jj_la1[i] == jj_gen)
            {
                for (int j = 0; j < 32; j++)
                {
                    if ((jj_la1_0[i] & (1 << j)) != 0)
                    {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 28; i++)
        {
            if (la1tokens[i])
            {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.addElement(jj_expentry);
            }
        }
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++)
        {
            exptokseq[i] = (int[]) jj_expentries.elementAt(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    static final public void enable_tracing()
    {
    }

    static final public void disable_tracing()
    {
    }

}
