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
package com.netspective.junxion.edi.input;

import java.io.StringReader;

import com.netspective.junxion.edi.input.x12.X12Reader;

import junit.framework.TestCase;

public class X12ReaderTest extends TestCase
{
    private final String X12MESSAGE =
            "ISA*00*          *00*          *27*00883          *ZZ*I08587         *020802*0046*U*00401*000000002*0*T*:~\n" +
            "GS*HP*00883*I08587*20020802*004653*13*X*004010X091~\n" +
            "ST*835*000000001~\n" +
            "BPR*H*0*C*NON************20020731~\n" +
            "TRN*1*507370267*1571062326~\n" +
            "REF*EV*I08587~\n" +
            "DTM*405*20020731~\n" +
            "N1*PR*PALMETTO GBA~\n" +
            "N3*PO BOX 182957~\n" +
            "N4*COLUMBUS*OH*432182957~\n" +
            "REF*2U*00883~\n" +
            "PER*CX*ATTN; PROGRAM INTEGRITY*TE*8775679232~\n" +
            "N1*PE*ROHOLT VISION INSTITUTE*FI*341958380~\n" +
            "N3*4425 METRO CIRCLE NW~\n" +
            "N4*NORTH CANTON*OH*447207755~\n" +
            "REF*1C*9316341~\n" +
            "LX*1~\n" +
            "CLP*3608*4*95*0*0*MB*0202212253490~\n" +
            "NM1*QC*1*ROLLINS*CAROL*A***HN*274300380A~\n" +
            "NM1*82*1******UP*U86692~\n" +
            "MOA***MA130~\n" +
            "DTM*050*20020731~\n" +
            "SVC*HC:92014*95*0**0**1~\n" +
            "DTM*472*20020220~\n" +
            "CAS*CO*11*95~\n" +
            "REF*6R*13508~\n" +
            "REF*LU*11~\n" +
            "REF*1C*4058991~\n" +
            "LQ*HE*M81~\n" +
            "SE*28*000000001~\n" +
            "GE*1*13~\n" +
            "IEA*1*000000002~";

    private X12Reader reader;
    private InputSource inputSource;

    public void setUp() throws Exception
    {
        reader = new X12Reader();
        inputSource = new InputSource(new StringReader(X12MESSAGE));
    }

    public void testParse() throws Exception
    {
        reader.parse(inputSource);
    }
}
