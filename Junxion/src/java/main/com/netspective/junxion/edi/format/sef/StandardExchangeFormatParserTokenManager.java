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


public class StandardExchangeFormatParserTokenManager implements StandardExchangeFormatParserConstants
{
    public static java.io.PrintStream debugStream = System.out;

    public static void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }

    private static final int jjStopStringLiteralDfa_0(int pos, long active0)
    {
        debugStream.println("   No more string literal token matches are possible.");
        switch (pos)
        {
            default :
                return -1;
        }
    }

    private static final int jjStartNfa_0(int pos, long active0)
    {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    static private final int jjStopAtPos(int pos, int kind)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        debugStream.println("   No more string literal token matches are possible.");
        debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        return pos + 1;
    }

    static private final int jjStartNfaWithStates_0(int pos, int kind, int state)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        debugStream.println("   No more string literal token matches are possible.");
        debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        { return pos + 1; }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        return jjMoveNfa_0(state, pos + 1);
    }

    static private final int jjMoveStringLiteralDfa0_0()
    {
        switch (curChar)
        {
            case 40:
                return jjStopAtPos(0, 10);
            case 41:
                return jjStopAtPos(0, 11);
            case 43:
                return jjStopAtPos(0, 5);
            case 44:
                return jjStopAtPos(0, 17);
            case 46:
                jjmatchedKind = 18;
                return jjMoveStringLiteralDfa1_0(0x1eL);
            case 59:
                return jjStopAtPos(0, 16);
            case 62:
                return jjMoveStringLiteralDfa1_0(0x40L);
            case 91:
                return jjStopAtPos(0, 14);
            case 93:
                return jjStopAtPos(0, 15);
            case 94:
                return jjStopAtPos(0, 7);
            case 123:
                return jjStopAtPos(0, 12);
            case 125:
                return jjStopAtPos(0, 13);
            default :
                debugStream.println("   No string literal matches possible.");
                return jjMoveNfa_0(0, 0);
        }
    }

    static private final int jjMoveStringLiteralDfa1_0(long active0)
    {
        if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
            debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        debugStream.println("   Possible string literal matches : { "
                +
                jjKindsForBitVector(0, active0) + " } ");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(0, active0);
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            return 1;
        }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        switch (curChar)
        {
            case 49:
                if ((active0 & 0x40L) != 0L)
                    return jjStopAtPos(1, 6);
                break;
            case 73:
                return jjMoveStringLiteralDfa2_0(active0, 0x4L);
            case 80:
                return jjMoveStringLiteralDfa2_0(active0, 0x8L);
            case 83:
                return jjMoveStringLiteralDfa2_0(active0, 0x10L);
            case 86:
                return jjMoveStringLiteralDfa2_0(active0, 0x2L);
            default :
                debugStream.println("   No string literal matches possible.");
                break;
        }
        return jjStartNfa_0(0, active0);
    }

    static private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(0, old0);
        if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
            debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        debugStream.println("   Possible string literal matches : { "
                +
                jjKindsForBitVector(0, active0) + " } ");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(1, active0);
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            return 2;
        }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        switch (curChar)
        {
            case 69:
                return jjMoveStringLiteralDfa3_0(active0, 0x12L);
            case 78:
                return jjMoveStringLiteralDfa3_0(active0, 0x4L);
            case 82:
                return jjMoveStringLiteralDfa3_0(active0, 0x8L);
            default :
                debugStream.println("   No string literal matches possible.");
                break;
        }
        return jjStartNfa_0(1, active0);
    }

    static private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(1, old0);
        if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
            debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        debugStream.println("   Possible string literal matches : { "
                +
                jjKindsForBitVector(0, active0) + " } ");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(2, active0);
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            return 3;
        }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        switch (curChar)
        {
            case 73:
                if ((active0 & 0x4L) != 0L)
                    return jjStopAtPos(3, 2);
                return jjMoveStringLiteralDfa4_0(active0, 0x8L);
            case 82:
                return jjMoveStringLiteralDfa4_0(active0, 0x2L);
            case 84:
                return jjMoveStringLiteralDfa4_0(active0, 0x10L);
            default :
                debugStream.println("   No string literal matches possible.");
                break;
        }
        return jjStartNfa_0(2, active0);
    }

    static private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(2, old0);
        if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
            debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        debugStream.println("   Possible string literal matches : { "
                +
                jjKindsForBitVector(0, active0) + " } ");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(3, active0);
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            return 4;
        }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        switch (curChar)
        {
            case 32:
                if ((active0 & 0x2L) != 0L)
                    return jjStopAtPos(4, 1);
                break;
            case 83:
                if ((active0 & 0x10L) != 0L)
                    return jjStopAtPos(4, 4);
                break;
            case 86:
                return jjMoveStringLiteralDfa5_0(active0, 0x8L);
            default :
                debugStream.println("   No string literal matches possible.");
                break;
        }
        return jjStartNfa_0(3, active0);
    }

    static private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(3, old0);
        if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
            debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        debugStream.println("   Possible string literal matches : { "
                +
                jjKindsForBitVector(0, active0) + " } ");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(4, active0);
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            return 5;
        }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        switch (curChar)
        {
            case 65:
                return jjMoveStringLiteralDfa6_0(active0, 0x8L);
            default :
                debugStream.println("   No string literal matches possible.");
                break;
        }
        return jjStartNfa_0(4, active0);
    }

    static private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(4, old0);
        if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
            debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        debugStream.println("   Possible string literal matches : { "
                +
                jjKindsForBitVector(0, active0) + " } ");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(5, active0);
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            return 6;
        }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        switch (curChar)
        {
            case 84:
                return jjMoveStringLiteralDfa7_0(active0, 0x8L);
            default :
                debugStream.println("   No string literal matches possible.");
                break;
        }
        return jjStartNfa_0(5, active0);
    }

    static private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(5, old0);
        if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
            debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        debugStream.println("   Possible string literal matches : { "
                +
                jjKindsForBitVector(0, active0) + " } ");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(6, active0);
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            return 7;
        }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        switch (curChar)
        {
            case 69:
                if ((active0 & 0x8L) != 0L)
                    return jjStopAtPos(7, 3);
                break;
            default :
                debugStream.println("   No string literal matches possible.");
                break;
        }
        return jjStartNfa_0(6, active0);
    }

    static private final void jjCheckNAdd(int state)
    {
        if (jjrounds[state] != jjround)
        {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    static private final void jjAddStates(int start, int end)
    {
        do
        {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        }
        while (start++ != end);
    }

    static private final void jjCheckNAddTwoStates(int state1, int state2)
    {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    static private final void jjCheckNAddStates(int start, int end)
    {
        do
        {
            jjCheckNAdd(jjnextStates[start]);
        }
        while (start++ != end);
    }

    static private final void jjCheckNAddStates(int start)
    {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    static private final int jjMoveNfa_0(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 9;
        int i = 1;
        jjstateSet[0] = startState;
        debugStream.println("   Starting NFA to match one of : " + jjKindsForStateVector(curLexState, jjstateSet, 0, 1));
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        int j, kind = 0x7fffffff;
        for (; ;)
        {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            if ((0x3ff000000000000L & l) != 0L)
                            {
                                if (kind > 20)
                                    kind = 20;
                                jjCheckNAdd(3);
                            }
                            else if ((0x2400L & l) != 0L)
                            {
                                if (kind > 9)
                                    kind = 9;
                            }
                            if (curChar == 13)
                                jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 1:
                            if (curChar == 10 && kind > 9)
                                kind = 9;
                            break;
                        case 2:
                            if (curChar == 13)
                                jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 3:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 20)
                                kind = 20;
                            jjCheckNAdd(3);
                            break;
                        case 6:
                            if ((0x3ff200000000000L & l) == 0L)
                                break;
                            if (kind > 8)
                                kind = 8;
                            jjAddStates(0, 1);
                            break;
                        case 8:
                            if ((0x3ff200000000000L & l) == 0L)
                                break;
                            if (kind > 19)
                                kind = 19;
                            jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            if ((0x7fffffe87fffffeL & l) == 0L)
                                break;
                            if (kind > 8)
                                kind = 8;
                            jjCheckNAddStates(2, 5);
                            break;
                        case 5:
                            if ((0x7fffffe87fffffeL & l) == 0L)
                                break;
                            if (kind > 8)
                                kind = 8;
                            jjCheckNAddTwoStates(5, 6);
                            break;
                        case 7:
                            if ((0x7fffffe87fffffeL & l) == 0L)
                                break;
                            if (kind > 19)
                                kind = 19;
                            jjCheckNAddTwoStates(7, 8);
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            if ((i = jjnewStateCnt) == (startsAt = 9 - (jjnewStateCnt = startsAt)))
                return curPos;
            debugStream.println("   Possible kinds of longer matches : " + jjKindsForStateVector(curLexState, jjstateSet, startsAt, i));
            try
            { curChar = input_stream.readChar(); }
            catch (java.io.IOException e)
            { return curPos; }
            debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        }
    }

    private static final int jjStopStringLiteralDfa_1(int pos, long active0)
    {
        debugStream.println("   No more string literal token matches are possible.");
        switch (pos)
        {
            default :
                return -1;
        }
    }

    private static final int jjStartNfa_1(int pos, long active0)
    {
        return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
    }

    static private final int jjStartNfaWithStates_1(int pos, int kind, int state)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        debugStream.println("   No more string literal token matches are possible.");
        debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
        try
        { curChar = input_stream.readChar(); }
        catch (java.io.IOException e)
        { return pos + 1; }
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        return jjMoveNfa_1(state, pos + 1);
    }

    static private final int jjMoveStringLiteralDfa0_1()
    {
        switch (curChar)
        {
            case 61:
                return jjStopAtPos(0, 22);
            default :
                debugStream.println("   No string literal matches possible.");
                return jjMoveNfa_1(0, 0);
        }
    }

    static final long[] jjbitVec0 = {
        0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
    };

    static private final int jjMoveNfa_1(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 1;
        int i = 1;
        jjstateSet[0] = startState;
        debugStream.println("   Starting NFA to match one of : " + jjKindsForStateVector(curLexState, jjstateSet, 0, 1));
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        int j, kind = 0x7fffffff;
        for (; ;)
        {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            if ((0xdfffffffffffffffL & l) == 0L)
                                break;
                            kind = 21;
                            jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            kind = 21;
                            jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            if ((jjbitVec0[i2] & l2) == 0L)
                                break;
                            if (kind > 21)
                                kind = 21;
                            jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
                return curPos;
            debugStream.println("   Possible kinds of longer matches : " + jjKindsForStateVector(curLexState, jjstateSet, startsAt, i));
            try
            { curChar = input_stream.readChar(); }
            catch (java.io.IOException e)
            { return curPos; }
            debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        }
    }

    static private final int jjMoveStringLiteralDfa0_4()
    {
        return jjMoveNfa_4(9, 0);
    }

    static private final int jjMoveNfa_4(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 10;
        int i = 1;
        jjstateSet[0] = startState;
        debugStream.println("   Starting NFA to match one of : " + jjKindsForStateVector(curLexState, jjstateSet, 0, 1));
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        int j, kind = 0x7fffffff;
        for (; ;)
        {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 1:
                            if ((0x2400L & l) != 0L && kind > 27)
                                kind = 27;
                            break;
                        case 2:
                            if (curChar == 10 && kind > 27)
                                kind = 27;
                            break;
                        case 3:
                            if (curChar == 13)
                                jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 9:
                            if (curChar == 46)
                                jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            if (curChar == 67)
                                jjAddStates(6, 7);
                            break;
                        case 4:
                            if (curChar == 73)
                                jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        case 5:
                            if (curChar == 76)
                                jjstateSet[jjnewStateCnt++] = 4;
                            break;
                        case 6:
                            if (curChar == 66)
                                jjstateSet[jjnewStateCnt++] = 5;
                            break;
                        case 7:
                            if (curChar == 85)
                                jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 8:
                            if (curChar == 80)
                                jjstateSet[jjnewStateCnt++] = 7;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            if ((i = jjnewStateCnt) == (startsAt = 10 - (jjnewStateCnt = startsAt)))
                return curPos;
            debugStream.println("   Possible kinds of longer matches : " + jjKindsForStateVector(curLexState, jjstateSet, startsAt, i));
            try
            { curChar = input_stream.readChar(); }
            catch (java.io.IOException e)
            { return curPos; }
            debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        }
    }

    static private final int jjMoveStringLiteralDfa0_2()
    {
        return jjMoveNfa_2(1, 0);
    }

    static private final int jjMoveNfa_2(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 4;
        int i = 1;
        jjstateSet[0] = startState;
        debugStream.println("   Starting NFA to match one of : " + jjKindsForStateVector(curLexState, jjstateSet, 0, 1));
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        int j, kind = 0x7fffffff;
        for (; ;)
        {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 1:
                            if ((0xffffffffffffdbffL & l) != 0L)
                            {
                                if (kind > 23)
                                    kind = 23;
                                jjCheckNAdd(0);
                            }
                            else if ((0x2400L & l) != 0L)
                            {
                                if (kind > 24)
                                    kind = 24;
                            }
                            if (curChar == 13)
                                jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 0:
                            if ((0xffffffffffffdbffL & l) == 0L)
                                break;
                            kind = 23;
                            jjCheckNAdd(0);
                            break;
                        case 2:
                            if (curChar == 10 && kind > 24)
                                kind = 24;
                            break;
                        case 3:
                            if (curChar == 13)
                                jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 1:
                        case 0:
                            kind = 23;
                            jjCheckNAdd(0);
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 1:
                        case 0:
                            if ((jjbitVec0[i2] & l2) == 0L)
                                break;
                            if (kind > 23)
                                kind = 23;
                            jjCheckNAdd(0);
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            if ((i = jjnewStateCnt) == (startsAt = 4 - (jjnewStateCnt = startsAt)))
                return curPos;
            debugStream.println("   Possible kinds of longer matches : " + jjKindsForStateVector(curLexState, jjstateSet, startsAt, i));
            try
            { curChar = input_stream.readChar(); }
            catch (java.io.IOException e)
            { return curPos; }
            debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        }
    }

    static private final int jjMoveStringLiteralDfa0_3()
    {
        return jjMoveNfa_3(0, 0);
    }

    static private final int jjMoveNfa_3(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 1;
        int i = 1;
        jjstateSet[0] = startState;
        debugStream.println("   Starting NFA to match one of : " + jjKindsForStateVector(curLexState, jjstateSet, 0, 1));
        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        int j, kind = 0x7fffffff;
        for (; ;)
        {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            if ((0xffffefffffffdbffL & l) == 0L)
                                break;
                            kind = 25;
                            jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            kind = 25;
                            jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                        case 0:
                            if ((jjbitVec0[i2] & l2) == 0L)
                                break;
                            if (kind > 25)
                                kind = 25;
                            jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        default :
                            break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
                debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
            if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
                return curPos;
            debugStream.println("   Possible kinds of longer matches : " + jjKindsForStateVector(curLexState, jjstateSet, startsAt, i));
            try
            { curChar = input_stream.readChar(); }
            catch (java.io.IOException e)
            { return curPos; }
            debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
        }
    }

    static final int[] jjnextStates = {
        5, 6, 5, 6, 7, 8, 1, 3,
    };
    public static final String[] jjstrLiteralImages = {
        "", "\56\126\105\122\40", "\56\111\116\111",
        "\56\120\122\111\126\101\124\105", "\56\123\105\124\123", "\53", "\76\61", "\136", null, null, "\50", "\51",
        "\173", "\175", "\133", "\135", "\73", "\54", "\56", null, null, null, "\75", null,
        null, null, null, null,
    };
    public static final String[] lexStateNames = {
        "DEFAULT",
        "EqualTextDataSect",
        "FreeTextDataSect",
        "CommaSepTextDataSect",
        "IgnoreToPublicSect",
    };
    public static final int[] jjnewLexState = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, 0,
        -1, -1, 0,
    };
    static final long[] jjtoToken = {
        0xbffffffL,
    };
    static final long[] jjtoMore = {
        0x4000000L,
    };
    static private SimpleCharStream input_stream;
    static private final int[] jjrounds = new int[10];
    static private final int[] jjstateSet = new int[20];
    static StringBuffer image;
    static int jjimageLen;
    static int lengthOfMatch;
    static protected char curChar;

    public StandardExchangeFormatParserTokenManager(SimpleCharStream stream)
    {
        if (input_stream != null)
            throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
        input_stream = stream;
    }

    public StandardExchangeFormatParserTokenManager(SimpleCharStream stream, int lexState)
    {
        this(stream);
        SwitchTo(lexState);
    }

    static public void ReInit(SimpleCharStream stream)
    {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    static private final void ReInitRounds()
    {
        int i;
        jjround = 0x80000001;
        for (i = 10; i-- > 0;)
            jjrounds[i] = 0x80000000;
    }

    static public void ReInit(SimpleCharStream stream, int lexState)
    {
        ReInit(stream);
        SwitchTo(lexState);
    }

    static public void SwitchTo(int lexState)
    {
        if (lexState >= 5 || lexState < 0)
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
        else
            curLexState = lexState;
    }

    static private final Token jjFillToken()
    {
        Token t = Token.newToken(jjmatchedKind);
        t.kind = jjmatchedKind;
        if (jjmatchedPos < 0)
        {
            t.image = "";
            t.beginLine = t.endLine = input_stream.getBeginLine();
            t.beginColumn = t.endColumn = input_stream.getBeginColumn();
        }
        else
        {
            String im = jjstrLiteralImages[jjmatchedKind];
            t.image = (im == null) ? input_stream.GetImage() : im;
            t.beginLine = input_stream.getBeginLine();
            t.beginColumn = input_stream.getBeginColumn();
            t.endLine = input_stream.getEndLine();
            t.endColumn = input_stream.getEndColumn();
        }
        return t;
    }

    static int curLexState = 0;
    static int defaultLexState = 0;
    static int jjnewStateCnt;
    static int jjround;
    static int jjmatchedPos;
    static int jjmatchedKind;

    public static final Token getNextToken()
    {
        int kind;
        Token specialToken = null;
        Token matchedToken;
        int curPos = 0;

        EOFLoop :
        for (; ;)
        {
            try
            {
                curChar = input_stream.BeginToken();
            }
            catch (java.io.IOException e)
            {
                debugStream.println("Returning the <EOF> token.");
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                return matchedToken;
            }
            image = null;
            jjimageLen = 0;

            for (; ;)
            {
                switch (curLexState)
                {
                    case 0:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
                        curPos = jjMoveStringLiteralDfa0_0();
                        break;
                    case 1:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
                        curPos = jjMoveStringLiteralDfa0_1();
                        break;
                    case 2:
                        debugStream.println("   Matched the empty string as " + tokenImage[23] + " token.");
                        jjmatchedKind = 23;
                        jjmatchedPos = -1;
                        curPos = 0;
                        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
                        curPos = jjMoveStringLiteralDfa0_2();
                        break;
                    case 3:
                        debugStream.println("   Matched the empty string as " + tokenImage[25] + " token.");
                        jjmatchedKind = 25;
                        jjmatchedPos = -1;
                        curPos = 0;
                        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
                        curPos = jjMoveStringLiteralDfa0_3();
                        break;
                    case 4:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
                        curPos = jjMoveStringLiteralDfa0_4();
                        if (jjmatchedPos == 0 && jjmatchedKind > 26)
                        {
                            debugStream.println("   Current character matched as a " + tokenImage[26] + " token.");
                            jjmatchedKind = 26;
                        }
                        break;
                }
                if (jjmatchedKind != 0x7fffffff)
                {
                    if (jjmatchedPos + 1 < curPos)
                    {
                        debugStream.println("   Putting back " + (curPos - jjmatchedPos - 1) + " characters into the input stream.");
                        input_stream.backup(curPos - jjmatchedPos - 1);
                    }
                    debugStream.println("****** FOUND A " + tokenImage[jjmatchedKind] + " MATCH (" + TokenMgrError.addEscapes(new String(input_stream.GetSuffix(jjmatchedPos + 1))) + ") ******\n");
                    if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
                    {
                        matchedToken = jjFillToken();
                        TokenLexicalActions(matchedToken);
                        if (jjnewLexState[jjmatchedKind] != -1)
                            curLexState = jjnewLexState[jjmatchedKind];
                        return matchedToken;
                    }
                    jjimageLen += jjmatchedPos + 1;
                    if (jjnewLexState[jjmatchedKind] != -1)
                        curLexState = jjnewLexState[jjmatchedKind];
                    curPos = 0;
                    jjmatchedKind = 0x7fffffff;
                    try
                    {
                        curChar = input_stream.readChar();
                        debugStream.println("<" + lexStateNames[curLexState] + ">" + "Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int) curChar + ")");
                        continue;
                    }
                    catch (java.io.IOException e1)
                    { }
                }
                int error_line = input_stream.getEndLine();
                int error_column = input_stream.getEndColumn();
                String error_after = null;
                boolean EOFSeen = false;
                try
                {
                    input_stream.readChar();
                    input_stream.backup(1);
                }
                catch (java.io.IOException e1)
                {
                    EOFSeen = true;
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                    if (curChar == '\n' || curChar == '\r')
                    {
                        error_line++;
                        error_column = 0;
                    }
                    else
                        error_column++;
                }
                if (!EOFSeen)
                {
                    input_stream.backup(1);
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                }
                throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
            }
        }
    }

    protected static final int[][][] statesForState = {
        {
            {0, 2, 3, 4, },
            {1},
            {0, 2, 3, 4, },
            {0, 2, 3, 4, },
            {0, 2, 3, 4, },
            {5},
            {6},
            {7},
            {8},
        },
        {
            {0},
        },
        {
            {0, 1, 3, },
            {0, 1, 3, },
            {2},
            {0, 1, 3, },
        },
        {
            {0},
        },
        {
            {0},
            {1},
            {2},
            {3},
            {4},
            {5},
            {6},
            {7},
            {8},
            {9},
        },

    };
    protected static final int[][] kindForState = {
        {9, 9, 9, 20, 8, 8, 8, 19, 19},
        {21},
        {23, 24, 24, 24},
        {25},
        {27, 27, 27, 27, 27, 27, 27, 27, 27, 27}
    };
    static int kindCnt = 0;

    protected static final String jjKindsForBitVector(int i, long vec)
    {
        String retVal = "";
        if (i == 0)
            kindCnt = 0;
        for (int j = 0; j < 64; j++)
        {
            if ((vec & (1L << j)) != 0L)
            {
                if (kindCnt++ > 0)
                    retVal += ", ";
                if (kindCnt % 5 == 0)
                    retVal += "\n     ";
                retVal += tokenImage[i * 64 + j];
            }
        }
        return retVal;
    }

    protected static final String jjKindsForStateVector(int lexState, int[] vec, int start, int end)
    {
        boolean[] kindDone = new boolean[28];
        String retVal = "";
        int cnt = 0;
        for (int i = start; i < end; i++)
        {
            if (vec[i] == -1)
                continue;
            int[] stateSet = statesForState[curLexState][vec[i]];
            for (int j = 0; j < stateSet.length; j++)
            {
                int state = stateSet[j];
                if (!kindDone[kindForState[lexState][state]])
                {
                    kindDone[kindForState[lexState][state]] = true;
                    if (cnt++ > 0)
                        retVal += ", ";
                    if (cnt % 5 == 0)
                        retVal += "\n     ";
                    retVal += tokenImage[kindForState[lexState][state]];
                }
            }
        }
        if (cnt == 0)
            return "{  }";
        else
            return "{ " + retVal + " }";
    }

    static int[] jjemptyLineNo = new int[5];
    static int[] jjemptyColNo = new int[5];
    static boolean[] jjbeenHere = new boolean[5];

    static final void TokenLexicalActions(Token matchedToken)
    {
        switch (jjmatchedKind)
        {
            case 23:
                if (jjmatchedPos == -1)
                {
                    if (jjbeenHere[2] &&
                            jjemptyLineNo[2] == input_stream.getBeginLine() &&
                            jjemptyColNo[2] == input_stream.getBeginColumn())
                        throw new TokenMgrError(("Error: Bailing out of infinite loop caused by repeated empty string matches at line " + input_stream.getBeginLine() + ", column " + input_stream.getBeginColumn() + "."), TokenMgrError.LOOP_DETECTED);
                    jjemptyLineNo[2] = input_stream.getBeginLine();
                    jjemptyColNo[2] = input_stream.getBeginColumn();
                    jjbeenHere[2] = true;
                }
                break;
            case 24:
                break;
            case 25:
                if (jjmatchedPos == -1)
                {
                    if (jjbeenHere[3] &&
                            jjemptyLineNo[3] == input_stream.getBeginLine() &&
                            jjemptyColNo[3] == input_stream.getBeginColumn())
                        throw new TokenMgrError(("Error: Bailing out of infinite loop caused by repeated empty string matches at line " + input_stream.getBeginLine() + ", column " + input_stream.getBeginColumn() + "."), TokenMgrError.LOOP_DETECTED);
                    jjemptyLineNo[3] = input_stream.getBeginLine();
                    jjemptyColNo[3] = input_stream.getBeginColumn();
                    jjbeenHere[3] = true;
                }
                break;
            default :
                break;
        }
    }
}
