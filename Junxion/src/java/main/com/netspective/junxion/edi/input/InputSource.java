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
 * $Id: InputSource.java,v 1.1 2003-03-13 18:37:22 shahid.shah Exp $
 */

package com.netspective.junxion.edi.input;

import java.io.InputStream;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * A single input source for an EDI entity. This class allows an application to
 * encapsulate information about an input source in a single object, which may include
 * a URL, a byte stream (possibly with a specified encoding), and/or a character stream.
 *
 * The EDI parser will use the InputSource object to determine how to read EDI input.
 * If there is a character stream available, the parser will read that stream directly;
 * if not, the parser will use a byte stream, if available; if neither a character stream
 * nor a byte stream is available, the parser will attempt to open a URI connection to
 * the resource identified by the URL string.
 *
 * An InputSource object belongs to the application: the SAX parser shall never modify it
 * in any way (it may modify a copy if necessary).
 */
public class InputSource
{
    private Reader characterStream;
    private File file;

    public InputSource(Reader characterStream)
    {
        setCharacterStream(characterStream);
    }

    public InputSource(File file) throws FileNotFoundException
    {
        setFile(file);
    }

    public String getSourceIdentifier()
    {
        return file != null ? file.getAbsolutePath() : characterStream.getClass().getName();
    }

    public Reader getCharacterStream()
    {
        return characterStream;
    }

    public void setCharacterStream(Reader characterStream)
    {
        this.characterStream = characterStream;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file) throws FileNotFoundException
    {
        this.file = file;
        setCharacterStream(new FileReader(file));
    }
}
