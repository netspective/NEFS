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
 * $Id: FileSystemFilePage.java,v 1.3 2003-12-13 17:33:32 shahid.shah Exp $
 */

package com.netspective.sparx.navigate.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.netspective.sparx.navigate.fs.FileSystemEntryPage;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.commons.value.source.StaticValueSource;

public class FileSystemFilePage extends FileSystemEntryPage
{
    static public final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private File file;
    private String fileExtension;
    private String contentType = DEFAULT_CONTENT_TYPE;

    public FileSystemFilePage(NavigationTree owner)
    {
        super(owner);
    }

    public boolean isValid(NavigationContext npc)
    {
        return file.exists();
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;

        String fileName = file.getName();
        String caption = fileName;
        int extnIndex = fileName.lastIndexOf('.');
        if(extnIndex > -1)
        {
            caption = fileName.substring(0, extnIndex);
            setFileExtension(fileName.substring(extnIndex + 1));
        }

        setName(file.getName());
        setCaption(new StaticValueSource(caption.replace('_', ' ')));
    }

    public String getFileExtension()
    {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public void send(HttpServletResponse response) throws IOException
    {
        InputStream in = new BufferedInputStream(new FileInputStream(getFile().getAbsolutePath()));

        response.setContentType(contentType);
        response.setContentLength((int) getFile().length()); // seems that here sits a possible problem for files >= 2 GB...

        OutputStream out = response.getOutputStream();

        int readlen;
        byte buffer[] = new byte[256];

        while((readlen = in.read(buffer)) != -1)
            // throws IOException: broken pipe when download is canceled.
            out.write(buffer, 0, readlen);

        // Success ! Close streams.
        out.flush();
        out.close();

        in.close();
    }
}
