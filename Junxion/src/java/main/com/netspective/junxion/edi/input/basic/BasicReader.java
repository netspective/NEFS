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

import java.io.IOException;

import com.netspective.junxion.JunxionException;
import com.netspective.junxion.edi.input.ContentHandler;
import com.netspective.junxion.edi.input.Delimiter;
import com.netspective.junxion.edi.input.ErrorHandler;
import com.netspective.junxion.edi.input.InputSource;
import com.netspective.junxion.edi.input.Locator;
import com.netspective.junxion.edi.input.NotRecognizedException;
import com.netspective.junxion.edi.input.NotSupportedException;
import com.netspective.junxion.edi.input.Reader;

public class BasicReader implements Reader
{
    private LocatorImpl locator;
    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;
    private Delimiter segmentDelimiter;
    private Delimiter dataElementDelimiter;
    private boolean inHeaderSegment;
    private boolean escapeNextSegmentCh;
    private boolean escapeNextDataElementCh;

    public ContentHandler getContentHandler()
    {
        return null;
    }

    public ErrorHandler getErrorHandler()
    {
        return null;
    }

    public boolean getFeature(String name) throws NotRecognizedException, NotSupportedException
    {
        return false;
    }

    public Object getProperty(String name) throws NotRecognizedException, NotSupportedException
    {
        return null;
    }

    protected void beginParse(InputSource source) throws JunxionException
    {
        locator = new LocatorImpl();
        locator.setSourceIdentifier(source.getSourceIdentifier());
        contentHandler.setDocumentLocator(locator);
        contentHandler.startDocument();
        segmentDelimiter = contentHandler.getDelimiters()[0];
        escapeNextSegmentCh = false;
    }

    protected void beginHeaderSegment() throws JunxionException
    {
        contentHandler.startHeaderSegment();
    }

    protected void endHeaderSegment(StringBuffer segmentText) throws JunxionException
    {
        contentHandler.endHeaderSegment(segmentText.toString());
    }

    protected void beginSegment() throws JunxionException
    {
        dataElementDelimiter = contentHandler.getDelimiters()[1];
        locator.newSegment();
        inHeaderSegment = contentHandler.isHeaderSegment(locator);
        contentHandler.startSegment();
    }

    protected void endSegment() throws JunxionException
    {
        contentHandler.endSegment();
    }

    protected StringBuffer beginDataElement() throws JunxionException
    {
        locator.dataElementNumber++;
        return new StringBuffer();
    }

    protected void endDataElement(StringBuffer value) throws JunxionException
    {
        contentHandler.dataElement(value.toString());
        if (locator.dataElementNumber == 1)
            locator.segmentIdentifier = value.toString();
    }

    protected void endParse() throws JunxionException
    {
        contentHandler.endDocument();
    }

    public void parse(InputSource input) throws IOException, JunxionException
    {
        beginParse(input);
        beginSegment();
        if (inHeaderSegment)
            beginHeaderSegment();

        StringBuffer segmentText = new StringBuffer();
        locator.columnNumber = 1;

        java.io.Reader reader = input.getCharacterStream();
        int segmentChInt = reader.read();
        while (segmentChInt != -1)
        {
            char segmentCh = (char) segmentChInt;
            switch (segmentDelimiter.getTokenType(segmentCh, escapeNextSegmentCh))
            {
                case Delimiter.DELIMTOKEN_NONE:
                    if (inHeaderSegment)
                    {
                        segmentText.append(segmentCh);
                    }
                    else
                    {
                        StringBuffer dataElementText = beginDataElement();
                        int dataElemChInt = segmentChInt;
                        READ_DATA_ELEMENT:
                          while (dataElemChInt != -1)
                          {
                              char dataElemCh = (char) dataElemChInt;
                              switch (dataElementDelimiter.getTokenType(dataElemCh, escapeNextDataElementCh))
                              {
                                  case Delimiter.DELIMTOKEN_NONE:
                                      if (segmentDelimiter.getTokenType(dataElemCh, false) == Delimiter.DELIMTOKEN_DELIMITER)
                                      {
                                          endDataElement(dataElementText);
                                          endSegment();
                                          if (dataElemCh == '\n')
                                              locator.newLine();
                                          beginSegment();
                                          break READ_DATA_ELEMENT;
                                      }
                                      dataElementText.append(dataElemCh);
                                      escapeNextDataElementCh = false;
                                      break;

                                  case Delimiter.DELIMTOKEN_DELIMITER:
                                      endDataElement(dataElementText);
                                      escapeNextDataElementCh = false;
                                      break READ_DATA_ELEMENT;

                                  case Delimiter.DELIMTOKEN_ESCAPE:
                                      escapeNextDataElementCh = true;
                                      break;
                              }

                              dataElemChInt = reader.read();
                              locator.columnNumber++;
                          }
                    }
                    escapeNextSegmentCh = false;
                    break;

                case Delimiter.DELIMTOKEN_DELIMITER:
                    if (inHeaderSegment)
                    {
                        segmentText.append(segmentCh); // we want the delimiter in the header in case anyone needs it
                        endHeaderSegment(segmentText);
                        if (segmentCh == '\n')
                            locator.newLine();
                        segmentText = new StringBuffer();
                        beginSegment();
                        if (inHeaderSegment)
                            beginHeaderSegment();
                    }
                    escapeNextSegmentCh = false;
                    break;

                case Delimiter.DELIMTOKEN_ESCAPE:
                    escapeNextSegmentCh = true;
                    break;
            }

            segmentChInt = reader.read();
            locator.columnNumber++;
        }

        endSegment();
        endParse();
    }

    public void setContentHandler(ContentHandler handler)
    {
        this.contentHandler = handler;
    }

    public void setErrorHandler(ErrorHandler handler)
    {
        this.errorHandler = handler;
    }

    public void setFeature(String name, boolean value) throws NotRecognizedException, NotSupportedException
    {
        throw new NotSupportedException("Features are not supported.");
    }

    public void setProperty(String name, Object value) throws NotRecognizedException, NotSupportedException
    {
        throw new NotSupportedException("Properties are not supported.");
    }

    protected class LocatorImpl implements Locator
    {
        private String sourceIdentifier;
        private int segmentNumber;
        private String segmentIdentifier;
        private int dataElementNumber;
        private int lineNumber;
        private int columnNumber;

        public LocatorImpl()
        {
        }

        public int getColumnNumber()
        {
            return columnNumber;
        }

        public void setColumnNumber(int columnNumber)
        {
            this.columnNumber = columnNumber;
        }

        public int getDataElementNumber()
        {
            return dataElementNumber;
        }

        public void setDataElementNumber(int dataElementNumber)
        {
            this.dataElementNumber = dataElementNumber;
        }

        public int getLineNumber()
        {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber)
        {
            this.lineNumber = lineNumber;
        }

        public String getSegmentIdentifier()
        {
            return segmentIdentifier;
        }

        public void setSegmentIdentifier(String segmentIdentifier)
        {
            this.segmentIdentifier = segmentIdentifier;
        }

        public int getSegmentNumber()
        {
            return segmentNumber;
        }

        public void setSegmentNumber(int segmentNumber)
        {
            this.segmentNumber = segmentNumber;
        }

        public String getSourceIdentifier()
        {
            return sourceIdentifier;
        }

        public void setSourceIdentifier(String sourceIdentifier)
        {
            this.sourceIdentifier = sourceIdentifier;
        }

        public void newSegment()
        {
            locator.segmentNumber++;
            locator.dataElementNumber = 0;
            locator.segmentIdentifier = null;
        }

        public void newLine()
        {
            locator.lineNumber++;
            locator.columnNumber = 0;
        }

        public String toString()
        {
            StringBuffer sb = new StringBuffer(sourceIdentifier);
            sb.append(", seg ");
            sb.append(segmentIdentifier != null ? segmentIdentifier : "?");
            sb.append(" (");
            sb.append(segmentNumber);
            sb.append(")");
            sb.append(", de ");
            sb.append(dataElementNumber);
            sb.append(" at line ");
            sb.append(lineNumber);
            sb.append(" column ");
            sb.append(columnNumber);
            return sb.toString();
        }
    }
}
