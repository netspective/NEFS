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
 * $Id: InputSourceLocator.java,v 1.2 2003-12-10 21:00:59 shahid.shah Exp $
 */

package com.netspective.commons.io;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class InputSourceLocator
{
    private InputSourceTracker inputSourceTracker;
    private int startLineNumber;
    private int startColumnNumber;
    private int endLineNumber;
    private int endColumnNumber;

    public InputSourceLocator(InputSourceTracker inputSourceTracker, int lineNumber, int columnNumber)
    {
        this.inputSourceTracker = inputSourceTracker;
        this.startLineNumber = lineNumber;
        this.startColumnNumber = columnNumber;
    }

    public InputSourceTracker getInputSourceTracker()
    {
        return inputSourceTracker;
    }

    public int getStartLineNumber()
    {
        return startLineNumber;
    }

    public int getStartColumnNumber()
    {
        return startColumnNumber;
    }

    public int getEndLineNumber()
    {
        return endLineNumber;
    }

    public int getEndColumnNumber()
    {
        return endColumnNumber;
    }

    public void setEndLine(int lineNumber, int columnNumber)
    {
        this.endLineNumber = lineNumber;
        this.endColumnNumber = columnNumber;
    }

    public String getLineNumbersText()
    {
        if(endLineNumber > startLineNumber)
            return " lines " + startLineNumber + " to " + endLineNumber;
        else
            return " line " + startLineNumber;
    }

    public String getSourceText() throws IOException
    {
        if(startLineNumber == 0 && endLineNumber == 0)
            return null;

        InputStream is = null;
        Reader isReader = null;
        LineNumberReader reader = null;
        StringBuffer result = new StringBuffer();

        try
        {
            is = inputSourceTracker.openStream();
            isReader = new InputStreamReader(is);
            reader = new LineNumberReader(isReader);

            String line = null;

            if(startLineNumber != 0 && endLineNumber == 0)
            {
                while((line = reader.readLine()) != null)
                {
                    if(reader.getLineNumber() == startLineNumber)
                        return line;
                }

            }
            else
            {
                while((line = reader.readLine()) != null)
                {
                    int lineNumber = reader.getLineNumber();

                    if(lineNumber < startLineNumber)
                        continue;

                    if(lineNumber > endLineNumber)
                        break;

                    result.append(line);
                    result.append("\n");
                }
            }
        }
        finally
        {
            if(reader != null)
                reader.close();

            if(isReader != null)
                is.close();

            if(is != null)
                is.close();
        }

        return result.toString();
    }

    public String toString()
    {
        return inputSourceTracker.getIdentifier() + getLineNumbersText();
    }
}
