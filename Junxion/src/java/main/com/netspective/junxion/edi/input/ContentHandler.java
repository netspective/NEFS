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
 * $Id: ContentHandler.java,v 1.1 2003-03-13 18:37:22 shahid.shah Exp $
 */

package com.netspective.junxion.edi.input;

import com.netspective.junxion.JunxionException;
import com.netspective.junxion.edi.format.validation.InputValidator;
import com.netspective.junxion.edi.format.InterchangeFormat;

public interface ContentHandler
{
    /**
     * Establish what format will be used to validate the EDI messages that this reader obtains.
     * @param format The format to use (null to set no format)
     */
    public void setFormat(InterchangeFormat format);

    /**
     * Returns the validation context being used for validating the input against the appropriate format.
     * This method should only be called after the document parsing has begun.
     */
    public InputValidator getValidator();

    /**
     * Return the active delimiters for the various levels (segments, fields, etc).
     */
    public Delimiter[] getDelimiters();

    /**
     * Return the number of segments that comprise a "header" -- meaning they won't have any
     * data elements, only meta data or other information.
     * @return The number of segments that make up a header
     */
    public boolean isHeaderSegment(Locator locator);

    /**
     * Get the active document locator object.
     * @return the active locator or null if none was set using setDocumentLocator()
     */
    public Locator getDocumentLocator();

    /**
     * Receive an object for locating the origin of document events. All parsers are strongly
     * encouraged (though not absolutely required) to supply a locator: if it does so, it must
     * supply the locator to the application by invoking this method before invoking any of
     * the other methods in the ContentHandler interface.
     */
    public void setDocumentLocator(Locator locator);

    /**
     * Receive notification of the beginning of a document. The parser will invoke this
     * method only once, before any other methods in this interface (except for
     * setDocumentLocator).
     */
    public void startDocument() throws JunxionException;

    /**
     * Receive notification of the end of a document. The parser will invoke this method only
     * once, and it will be the last method invoked during the parse. The parser shall not
     * invoke this method until it has either abandoned parsing (because of an unrecoverable
     * error) or reached the end of input.
     */
    public void endDocument() throws JunxionException;

    /**
     * Receive notification of a data element within a segment.
     * @param value the value of the data element
     */
    public void dataElement(String value) throws JunxionException;

    /**
     * Receive notification of the beginning of a header. The Parser will invoke this method
     * at the beginning of only the first segments in the input source; there will be a corresponding
     * endHeaderSegment.
     */
    public void startHeaderSegment() throws JunxionException;

    /**
     * Receive notification of the end of the first segment.
     * @param segmentText The complete text contained in the segment.
     */
    public void endHeaderSegment(String segmentText) throws JunxionException;

    /**
     * Receive notification of the beginning of a segment. The Parser will invoke this method
     * at the beginning of every segment in the input source; there will be a corresponding
     * endSegment event for every startSegment event. All of the element's content will be
     * reported, in order, before the corresponding startSegment event.
     */
    public void startSegment() throws JunxionException;

    /**
     * Receive notification of the end of a segment.
     */
    public void endSegment() throws JunxionException;
}
