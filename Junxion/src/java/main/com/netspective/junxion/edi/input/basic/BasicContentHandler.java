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
package com.netspective.junxion.edi.input.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.junxion.JunxionException;
import com.netspective.junxion.edi.format.InterchangeFormat;
import com.netspective.junxion.edi.format.validation.InputValidator;
import com.netspective.junxion.edi.input.ContentHandler;
import com.netspective.junxion.edi.input.Delimiter;
import com.netspective.junxion.edi.input.Locator;

public class BasicContentHandler implements ContentHandler
{
    private static Log log = LogFactory.getLog(BasicContentHandler.class.getName());
    private InterchangeFormat format;
    private InputValidator validator;
    private Locator locator;
    private Delimiter[] delimiters;
    private List activeSegment;

    public BasicContentHandler(InterchangeFormat format)
    {
        setFormat(format);
    }

    public void setFormat(InterchangeFormat format)
    {
        this.format = format;
    }

    public InterchangeFormat getFormat()
    {
        return format;
    }

    public InputValidator getValidator()
    {
        return validator;
    }

    public List getActiveSegment()
    {
        return activeSegment;
    }

    public String getActiveSegmentId()
    {
        return (String) activeSegment.get(0);
    }

    public void setDelimiters(Delimiter[] delimiters)
    {
        this.delimiters = delimiters;
    }

    public Delimiter[] getDelimiters()
    {
        if (delimiters == null)
        {
            delimiters = new Delimiter[]
            {
                new BasicDelimiter("~\n", null),
                new BasicDelimiter("*", null),
                new BasicDelimiter(":", null),
            };
        }
        return delimiters;
    }

    public Locator getDocumentLocator()
    {
        return locator;
    }

    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    public void startDocument() throws JunxionException
    {
        validator = format.createInputValidator();
        validator.setLocator(locator);
    }

    public boolean isHeaderSegment(Locator locator)
    {
        return false;
    }

    public void startHeaderSegment() throws JunxionException
    {
    }

    public void endHeaderSegment(String segmentText) throws JunxionException
    {
    }

    public void startSegment() throws JunxionException
    {
        activeSegment = new ArrayList();
    }

    public void dataElement(String value) throws JunxionException
    {
        activeSegment.add(value);
    }

    public void endSegment() throws JunxionException
    {
    }

    public void endDocument() throws JunxionException
    {
    }

    protected void logDebugMessage(String message)
    {
        log.debug(message + ", class: " + this.getClass().getName() + ", locator: " + locator);
    }
}
