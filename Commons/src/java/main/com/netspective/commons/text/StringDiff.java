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

/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */

package com.netspective.commons.text;

public class StringDiff
{
    public static final String getHtmlDiff(String s1, String s2,
                                           String addTagOpen, String addTagClose,
                                           String updateTagOpen, String updateTagClose,
                                           String deleteTagOpen, String deleteTagClose)
    {
        StringDiff diff = new StringDiff(s1, s2, addTagOpen, addTagClose, updateTagOpen, updateTagClose,
                                         deleteTagOpen, deleteTagClose);
        return diff.getResult();
    }

    public static final String getHtmlDiff(String s1, String s2)
    {
        return getHtmlDiff(s1, s2,
                           "<span class=\"add\">", "</span>",
                           "<span class=\"update\">", "</span>",
                           "<span class=\"delete\">", "</span>");
    }

    private static final int INSERT = 1;
    private static final int DELETE = 3;

    private final int MAXCHARS;
    private final String s1;
    private final String s2;
    private final int editDistance;

    private final String addTagOpen;
    private final String addTagClose;
    private final String updateTagOpen;
    private final String updateTagClose;
    private final String deleteTagOpen;
    private final String deleteTagClose;

    private char[] A;
    private char[] B;
    private edit start;

    private StringBuffer result = new StringBuffer();
    private int lastInsertPos = 0;
    private int tagStartPos = 0;
    private StringBuffer tmpBuf;

    public StringDiff(String s1, String s2,
                      String addTagOpen, String addTagClose,
                      String updateTagOpen, String updateTagClose,
                      String deleteTagOpen, String deleteTagClose)
    {
        this.MAXCHARS = Math.max(s1.length(), s2.length());
        this.s1 = s1;
        this.s2 = s2;
        this.addTagClose = addTagClose;
        this.addTagOpen = addTagOpen;
        this.updateTagClose = updateTagClose;
        this.updateTagOpen = updateTagOpen;
        this.deleteTagClose = deleteTagClose;
        this.deleteTagOpen = deleteTagOpen;

        this.A = new char[MAXCHARS];
        this.B = new char[MAXCHARS];

        s1.getChars(0, s1.length(), A, 0);
        s2.getChars(0, s2.length(), B, 0);

        this.editDistance = calcEditDistance();
        scan();
    }

    public String getResult()
    {
        return result.toString();
    }

    protected int calcEditDistance()
    {
        final int ORIGIN = MAXCHARS;

        int max_d = 2 * MAXCHARS,
                m = 0, n = 0,
                lower, upper,
                d = 1, k, row, col;

        int last_d[] = new int[2 * MAXCHARS + 1];
        edit[] script = new edit[2 * MAXCHARS + 1];

        m = s1.length();
        n = s2.length();

        for(row = 0; row < m && row < n && A[row] == B[row]; row++) ;

        last_d[ORIGIN] = row;
        script[ORIGIN] = null;
        if(row == m)
            lower = ORIGIN + 1;
        else
            lower = ORIGIN - 1;
        if(row == n)
            upper = ORIGIN - 1;
        else
            upper = ORIGIN + 1;
        if(lower > upper)
            return 0;
        else
        {
            for(d = 1; d <= max_d; d++)
            {
                for(k = lower; k <= upper; k += 2)
                {
                    edit e = new edit();
                    if(k == ORIGIN - d || k != ORIGIN + d && last_d[k + 1] >= last_d[k - 1])
                    {
                        row = last_d[k + 1] + 1;
                        e.next = script[k + 1];
                        e.op = DELETE;
                    }
                    else
                    {
                        row = last_d[k - 1];
                        e.next = script[k - 1];
                        e.op = INSERT;
                    }
                    e.ch1 = row;
                    col = row + k - ORIGIN;
                    e.ch2 = col;
                    script[k] = e;

                    while(row < m && col < n && A[row] == B[col])
                    {
                        row++;
                        col++;
                    }
                    last_d[k] = row;
                    if(row == m && col == n)
                    {
                        start = script[k];
                        return last_d[k];
                    }

                    if(row == m)
                        lower = k + 2;
                    if(col == n)
                        upper = k - 2;
                }

                lower--;
                upper++;
            }
        }

        return -1;
    }

    protected void beginAdd(int startPos)
    {
        tmpBuf = new StringBuffer();
        this.tagStartPos = startPos;
    }

    protected void add(char ch)
    {
        tmpBuf.append(ch);
    }

    protected void endAdd()
    {
        final int len = tagStartPos - lastInsertPos;
        result.append(A, lastInsertPos, len);
        result.append(addTagOpen);
        result.append(tmpBuf);
        result.append(addTagClose);

        lastInsertPos += len;
    }

    protected void beginUpdate(int startPos)
    {
        tmpBuf = new StringBuffer();
        this.tagStartPos = startPos;
    }

    protected void update(boolean orig, char ch)
    {
        if(!orig) tmpBuf.append(ch);
    }

    protected void endUpdate()
    {
        final int len = tagStartPos - lastInsertPos - tmpBuf.length();
        result.append(A, lastInsertPos, len);
        result.append(updateTagOpen);
        result.append(tmpBuf);
        result.append(updateTagClose);
        lastInsertPos += len + 1;
    }

    protected void beginDelete(int startPos)
    {
        tmpBuf = new StringBuffer();
        this.tagStartPos = startPos;
    }

    protected void delete(char ch)
    {
        tmpBuf.append(ch);
    }

    protected void endDelete()
    {
        final int len = tagStartPos - lastInsertPos - tmpBuf.length();
        result.append(A, lastInsertPos, len);
        result.append(deleteTagOpen);
        result.append(tmpBuf);
        result.append(deleteTagClose);
        lastInsertPos += len + 1;
    }

    protected void scan()
    {
        boolean change;
        edit ep = new edit();
        edit behind = new edit();
        edit ahead = new edit();
        edit a = new edit();
        edit b = new edit();
        ahead = start;
        ep = null;
        while(ahead != null)
        {
            behind = ep;
            ep = ahead;
            ahead = ahead.next;
            ep.next = behind;
        }

        while(ep != null)
        {
            b = ep;
            if(ep.op == INSERT)
            {
                a = ep;
                behind = ep.next;
                while(behind != null && behind.op == INSERT && ep.ch1 == behind.ch1)
                {
                    a = behind;
                    behind = behind.next;
                }

                beginAdd(ep.ch1);
                do
                {
                    add(B[ep.ch2 - 1]);
                    ep = ep.next;
                }
                while(ep != null && ep.op == INSERT && ep.ch1 == b.ch1);
                endAdd();
            }
            else
            {
                do
                {
                    a = b;
                    b = b.next;
                }
                while(b != null && b.op == DELETE && b.ch1 == a.ch1 + 1);
                change = (b != null && b.op == INSERT && b.ch1 == a.ch1);
                if(change)
                {
                    behind = b;
                    while(behind != null && behind.op == INSERT && behind.ch1 == b.ch1)
                        behind = behind.next;

                    beginUpdate(ep.ch1);
                    do
                    {
                        update(true, A[ep.ch1 - 1]);
                        ep = ep.next;
                    }
                    while(ep != b);

                    do
                    {
                        update(false, B[ep.ch2 - 1]);
                        ep = ep.next;
                    }
                    while(ep != null && ep.op == INSERT && ep.ch1 == b.ch1);
                    endUpdate();
                }
                else
                {
                    beginDelete(ep.ch1);
                    do
                    {
                        delete(A[ep.ch1 - 1]);
                        ep = ep.next;
                    }
                    while(ep != b);
                    endDelete();
                }
            }
        }
    }

    protected static final class edit
    {
        private int op;
        private int ch1;
        private int ch2;
        private edit next;

        public String toString()
        {
            return op + " " + ch1 + " " + ch2;
        }
    }
}
