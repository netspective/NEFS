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
package com.netspective.sparx.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet response wrapper that allows the response's output to be sent to our own writer. Instead
 * of sending output directly to the response's writer object, we intercept the getOutputStream() and
 * getWriter() calls so that we can send the output to a writer of our choice. This is very useful for
 * capturing output from a RequestDispatcher.include() call.
 * <p/>
 * Example:
 * RequestDispatcher rd = dc.getRequest().getRequestDispatcher(includeUrl);
 * rd.include(dc.getRequest(), new AlternateWriterServletResponse(writer, dc.getResponse()));
 */
public class AlternateOutputDestServletResponse implements HttpServletResponse
{
    private PrintWriter writer;
    private WriterOutputStream os;
    private HttpServletResponse response;

    /**
     * Send the output from a ServletOutputStream to our writer
     */
    public class WriterOutputStream extends ServletOutputStream
    {
        public WriterOutputStream()
        {
        }

        public void write(int b) throws IOException
        {
            writer.write(b);
        }

        public void write(byte b[]) throws IOException
        {
            for (int i = 0; i < b.length; i++)
                writer.write(b[i]);
        }

        public void write(byte b[], int off, int len) throws IOException
        {
            for (int i = off; i < len; i++)
                writer.write(b[i]);
        }

        public void flush() throws IOException
        {
            // do nothing
        }

        public void close() throws IOException
        {
            // do nothing
        }
    }

    public AlternateOutputDestServletResponse(Writer writer, ServletResponse response) throws IOException
    {
        this.writer = new PrintWriter(writer);
        this.os = new WriterOutputStream();
        this.response = (HttpServletResponse) response;
    }

    public ServletOutputStream getOutputStream() throws IOException
    {
        return os;
    }

    public PrintWriter getWriter() throws IOException
    {
        return writer;
    }

    /* ------------------- */

    public void addCookie(Cookie cookie)
    {
        response.addCookie(cookie);
    }

    public boolean containsHeader(String s)
    {
        return response.containsHeader(s);
    }

    public String encodeURL(String s)
    {
        return response.encodeURL(s);
    }

    public String encodeRedirectURL(String s)
    {
        return response.encodeRedirectURL(s);
    }

    public String encodeUrl(String s)
    {
        return response.encodeUrl(s);
    }

    public String encodeRedirectUrl(String s)
    {
        return response.encodeRedirectUrl(s);
    }

    public void sendError(int i, String s) throws IOException
    {
        response.sendError(i, s);
    }

    public void sendError(int i) throws IOException
    {
        response.sendError(i);
    }

    public void sendRedirect(String s) throws IOException
    {
        response.sendRedirect(s);
    }

    public void setDateHeader(String s, long l)
    {
        response.setDateHeader(s, l);
    }

    public void addDateHeader(String s, long l)
    {
        response.addDateHeader(s, l);
    }

    public void setHeader(String s, String s1)
    {
        response.setHeader(s, s1);
    }

    public void addHeader(String s, String s1)
    {
        response.addHeader(s, s1);
    }

    public void setIntHeader(String s, int i)
    {
        response.setIntHeader(s, i);
    }

    public void addIntHeader(String s, int i)
    {
        response.addIntHeader(s, i);
    }

    public void setStatus(int i)
    {
        response.setStatus(i);
    }

    public void setStatus(int i, String s)
    {
        response.setStatus(i, s);
    }

    public String getCharacterEncoding()
    {
        return response.getCharacterEncoding();
    }

    public void setContentLength(int i)
    {
        response.setContentLength(i);
    }

    public void setContentType(String s)
    {
        response.setContentType(s);
    }

    public void setBufferSize(int i)
    {
        response.setBufferSize(i);
    }

    public int getBufferSize()
    {
        return response.getBufferSize();
    }

    public void flushBuffer() throws IOException
    {
        response.flushBuffer();
    }

    public void resetBuffer()
    {
        response.resetBuffer();
    }

    public boolean isCommitted()
    {
        return response.isCommitted();
    }

    public void reset()
    {
        response.reset();
    }

    public void setLocale(Locale locale)
    {
        response.setLocale(locale);
    }

    public Locale getLocale()
    {
        return response.getLocale();
    }
}
