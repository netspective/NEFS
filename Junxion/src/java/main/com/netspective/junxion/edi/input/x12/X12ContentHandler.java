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
 * $Id: X12ContentHandler.java,v 1.1 2003-03-13 18:37:22 shahid.shah Exp $
 */

package com.netspective.junxion.edi.input.x12;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.junxion.JunxionException;
import com.netspective.junxion.edi.input.basic.BasicContentHandler;
import com.netspective.junxion.edi.input.basic.BasicDelimiter;
import com.netspective.junxion.edi.input.Locator;
import com.netspective.junxion.edi.input.ParseException;
import com.netspective.junxion.edi.input.Delimiter;
import com.netspective.junxion.edi.format.InterchangeFormat;
import com.netspective.junxion.edi.element.Interchange;
import com.netspective.junxion.edi.element.ElementFactory;
import com.netspective.junxion.edi.element.FunctionalGroup;
import com.netspective.junxion.edi.element.basic.FunctionalGroupSegment;

public class X12ContentHandler extends BasicContentHandler
{
    private final Log log = LogFactory.getLog(X12ContentHandler.class);
    public final String SEGMENTID_INTERCHANGE_START = "ISA";
    public final String SEGMENTID_FUNCGROUP_START = "GS";
    public final String SEGMENTID_FUNCGROUP_END = "GE";
    public final String SEGMENTID_INTERCHANGE_END = "IEA";

    private List headerDataElems;
    private Interchange interchange;
    private FunctionalGroup activeFunctionalGroup;

    public X12ContentHandler(InterchangeFormat format)
    {
        super(format);
    }

    public boolean isHeaderSegment(Locator locator)
    {
        return locator.getSegmentNumber() == 1 ? true : false;
    }

    public void endHeaderSegment(String segmentText) throws JunxionException
    {
        if(!segmentText.startsWith(SEGMENTID_INTERCHANGE_START))
            throw new ParseException("An X12 message must begin with an '"+ SEGMENTID_INTERCHANGE_START +"' segment.", getDocumentLocator());

        if(segmentText.length() < 105)
            throw new ParseException("An X12 '"+ SEGMENTID_INTERCHANGE_START +"' segment must be 105 characters long.", getDocumentLocator());

        String dataElemsDelim = String.valueOf(segmentText.charAt(3));
        setDelimiters(new Delimiter[]
        {
            new BasicDelimiter(String.valueOf(segmentText.charAt(105)), null), // allow newlines in segment separators
            new BasicDelimiter(dataElemsDelim, null), // field delimiter
            new BasicDelimiter(String.valueOf(segmentText.charAt(104)), null), // composite delimiter
        });

        headerDataElems = new ArrayList();
        StringTokenizer dataElems = new StringTokenizer(segmentText, dataElemsDelim);
        while(dataElems.hasMoreTokens())
            headerDataElems.add(dataElems.nextToken());

        interchange = ElementFactory.createInterchange();

        super.endHeaderSegment(segmentText);
    }

    public void endSegment() throws JunxionException
    {
        String segmentId = getActiveSegmentId();
        if(SEGMENTID_FUNCGROUP_START.equals(segmentId))
        {
            if(interchange == null)
                throw new ParseException("Encountered functional group '"+ segmentId +"' without an interchange.", getDocumentLocator());

            activeFunctionalGroup = new FunctionalGroupSegment();
            interchange.addFunctionalGroup(activeFunctionalGroup);
        }
        else if(SEGMENTID_FUNCGROUP_END.equals(segmentId))
        {
            activeFunctionalGroup = null;
        }
        else if(SEGMENTID_INTERCHANGE_END.equals(segmentId))
        {
            interchange = null;
        }
        else
        {
            if(activeFunctionalGroup == null)
                throw new ParseException("Encountered segment '"+ segmentId +"' without a functional group.", getDocumentLocator());

            getValidator().validate(getActiveSegment(), activeFunctionalGroup);
        }
    }

    public void endDocument() throws JunxionException
    {
        if(!log.isDebugEnabled())
            return;

        log.debug("Header segment: " + headerDataElems);

        List segments = getValidator().getSegments();
        if(segments != null)
        {
            for(int segment = 0; segment < segments.size(); segment++)
            {
                List dataElements = (List) segments.get(segment);
                if(dataElements.size() > 0)
                    log.debug("Segment " + (segment+1) + ": " + dataElements);
                else
                    log.debug("No data elements found in segment " + (segment+1));
            }
        }
        else
            log.debug("No segments found.");
    }
}
