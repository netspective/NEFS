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
package com.netspective.sparx.navigate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Serves the files located in WEB-INF/lib/netspective-sparx.jar!resources/XXX/YYY. Works in conjunction with the
 * UriAddressableSparxJarResourceLocator class.
 */
public class SparxResourcesServlet extends HttpServlet
{
    private static final Log log = LogFactory.getLog(SparxResourcesServlet.class);
    private static final String SPARX_JAR = "WEB-INF/lib/netspective-sparx.jar";
    private static final int BUFFER_SIZE = 2048;

    private ZipFile jarFile;
    private long jarFileModf;

    public void init() throws ServletException
    {
        final String jarFileParamValue = getServletConfig().getInitParameter("jar-file");

        File src = jarFileParamValue != null
                   ? new File(jarFileParamValue) : new File(getServletContext().getRealPath(SPARX_JAR));
        if(!src.exists())
            throw new ServletException("Resource file (" + jarFile + ") not found.");

        try
        {
            jarFile = new ZipFile(src);
            jarFileModf = src.lastModified();
        }
        catch(IOException e)
        {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        if(jarFileModf <= httpServletRequest.getDateHeader("If-Modified-Since"))
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        final String pathInfo = httpServletRequest.getPathInfo();
        if(pathInfo == null)
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        final String entryName = "resources" + pathInfo;
        final ZipEntry ze = jarFile.getEntry(entryName);
        if(ze == null || ze.isDirectory())
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        httpServletResponse.setContentLength((int) ze.getSize());
        httpServletResponse.setDateHeader("Last-Modified", jarFileModf);

        InputStream is = jarFile.getInputStream(ze);
        OutputStream os = httpServletResponse.getOutputStream();

        int count;
        byte data[] = new byte[BUFFER_SIZE];
        while((count = is.read(data, 0, BUFFER_SIZE)) != -1)
            os.write(data, 0, count);

        is.close();
    }
}
