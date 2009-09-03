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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.LRUMap;

public class HttpServletResponseCache
{
    private Map pageCache;

    public HttpServletResponseCache(int maxSize)
    {
        this.pageCache = new LRUMap(maxSize);
    }

    public boolean isCacheHit(long lastModfTime, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        // Only do caching for GET requests
        String method = req.getMethod();
        if(!method.equals("GET"))
            return false;

        // A last modified of -1 means we shouldn't use any cache logic
        if(lastModfTime == -1)
            return false;

        // If the client sent an If-Modified-Since header equal or after the
        // servlet's last modified time, send a short "Not Modified" status code
        // Round down to the nearest second since client headers are in seconds
        if((lastModfTime / 1000 * 1000) <= req.getDateHeader("If-Modified-Since"))
        {
            res.setStatus(res.SC_NOT_MODIFIED);
            return true;
        }

        String cacheKey = req.getServletPath() + req.getPathInfo() + req.getQueryString();
        CacheData cacheData = (CacheData) pageCache.get(cacheKey);

        // Use the existing cache if it's current and valid
        if(cacheData != null && (lastModfTime <= cacheData.lastModf && cacheData.response.isValid()))
        {
            cacheData.response.writeTo(res);
            return true;
        }

        return false;
    }

    public ResponseCache createResponseCache(HttpServletResponse res)
    {
        return new ResponseCache(res);
    }

    public void cacheResponse(long servletLastModf, HttpServletRequest req, ResponseCache res)
    {
        synchronized(pageCache)
        {
            String cacheKey = req.getServletPath() + req.getPathInfo() + req.getQueryString();
            pageCache.put(cacheKey, new CacheData(res, servletLastModf, req.getServletPath(), req.getPathInfo(), req.getQueryString()));
        }
    }

    private class CacheData
    {
        private ResponseCache response;
        private long lastModf = -1;
        private String queryString = null;
        private String pathInfo = null;
        private String servletPath = null;

        public CacheData(ResponseCache response, long lastModf, String servletPath, String pathInfo, String queryString)
        {
            this.response = response;
            this.lastModf = lastModf;
            this.servletPath = servletPath;
            this.pathInfo = pathInfo;
            this.queryString = queryString;
        }

        public ResponseCache getResponse()
        {
            return response;
        }

        public long getLastModf()
        {
            return lastModf;
        }

        public String getQueryString()
        {
            return queryString;
        }

        public String getPathInfo()
        {
            return pathInfo;
        }

        public String getServletPath()
        {
            return servletPath;
        }
    }

    private class ResponseCache implements HttpServletResponse
    {
        // Store key response variables so they can be set later
        private int status;
        private Hashtable headers;
        private int contentLength;
        private String contentType;
        private Locale locale;
        private Vector cookies;
        private boolean didError;
        private boolean didRedirect;
        private boolean gotStream;
        private boolean gotWriter;

        private HttpServletResponse delegate;
        private CacheServletOutputStream out;
        private PrintWriter writer;

        ResponseCache(HttpServletResponse res)
        {
            delegate = res;
            try
            {
                out = new CacheServletOutputStream(res.getOutputStream());
            }
            catch(IOException e)
            {
                System.out.println("Got IOException constructing cached response: " + e.getMessage());
            }
            internalReset();
        }

        private void internalReset()
        {
            status = 200;
            headers = new Hashtable();
            contentLength = -1;
            contentType = null;
            locale = null;
            cookies = new Vector();
            didError = false;
            didRedirect = false;
            gotStream = false;
            gotWriter = false;
            out.getBuffer().reset();
        }

        public boolean isValid()
        {
            // We don't cache error pages or redirects
            return didError != true && didRedirect != true;
        }

        private void internalSetHeader(String name, Object value)
        {
            Vector v = new Vector();
            v.addElement(value);
            headers.put(name, v);
        }

        private void internalAddHeader(String name, Object value)
        {
            Vector v = (Vector) headers.get(name);
            if(v == null)
            {
                v = new Vector();
            }
            v.addElement(value);
            headers.put(name, v);
        }

        public void writeTo(HttpServletResponse res)
        {
            // Write status code
            res.setStatus(status);
            // Write convenience headers
            if(contentType != null) res.setContentType(contentType);
            if(locale != null) res.setLocale(locale);
            // Write cookies
            Enumeration cookieElems = cookies.elements();
            while(cookieElems.hasMoreElements())
            {
                Cookie c = (Cookie) cookieElems.nextElement();
                res.addCookie(c);
            }
            // Write standard headers
            cookieElems = headers.keys();
            while(cookieElems.hasMoreElements())
            {
                String name = (String) cookieElems.nextElement();
                Vector values = (Vector) headers.get(name); // may have multiple values
                Enumeration enum2 = values.elements();
                while(enum2.hasMoreElements())
                {
                    Object value = enum2.nextElement();
                    if(value instanceof String)
                    {
                        res.setHeader(name, (String) value);
                    }
                    if(value instanceof Integer)
                    {
                        res.setIntHeader(name, ((Integer) value).intValue());
                    }
                    if(value instanceof Long)
                    {
                        res.setDateHeader(name, ((Long) value).longValue());
                    }
                }
            }
            // Write content length
            res.setContentLength(out.getBuffer().size());
            // Write body
            try
            {
                out.getBuffer().writeTo(res.getOutputStream());
            }
            catch(IOException e)
            {
                System.out.println("Got IOException writing cached response: " + e.getMessage());
            }
        }

        public ServletOutputStream getOutputStream() throws IOException
        {
            if(gotWriter)
            {
                throw new IllegalStateException("Cannot get output stream after getting writer");
            }
            gotStream = true;
            return out;
        }

        public PrintWriter getWriter() throws UnsupportedEncodingException
        {
            if(gotStream)
            {
                throw new IllegalStateException("Cannot get writer after getting output stream");
            }
            gotWriter = true;
            if(writer == null)
            {
                OutputStreamWriter w =
                        new OutputStreamWriter(out, getCharacterEncoding());
                writer = new PrintWriter(w, true);  // autoflush is necessary
            }
            return writer;
        }

        public void setContentLength(int len)
        {
            delegate.setContentLength(len);
            // No need to save the length; we can calculate it later
        }

        public void setContentType(String type)
        {
            delegate.setContentType(type);
            contentType = type;
        }

        public String getCharacterEncoding()
        {
            return delegate.getCharacterEncoding();
        }

        public void setBufferSize(int size) throws IllegalStateException
        {
            delegate.setBufferSize(size);
        }

        public int getBufferSize()
        {
            return delegate.getBufferSize();
        }

        public void reset() throws IllegalStateException
        {
            delegate.reset();
            internalReset();
        }

        public void resetBuffer() throws IllegalStateException
        {
            delegate.resetBuffer();
            contentLength = -1;
            out.getBuffer().reset();
        }

        public boolean isCommitted()
        {
            return delegate.isCommitted();
        }

        public void flushBuffer() throws IOException
        {
            delegate.flushBuffer();
        }

        public void setLocale(Locale loc)
        {
            delegate.setLocale(loc);
            locale = loc;
        }

        public Locale getLocale()
        {
            return delegate.getLocale();
        }

        public void addCookie(Cookie cookie)
        {
            delegate.addCookie(cookie);
            cookies.addElement(cookie);
        }

        public boolean containsHeader(String name)
        {
            return delegate.containsHeader(name);
        }

        /**
         * @deprecated
         */
        public void setStatus(int sc, String sm)
        {
            delegate.setStatus(sc, sm);
            status = sc;
        }

        public void setStatus(int sc)
        {
            delegate.setStatus(sc);
            status = sc;
        }

        public void setHeader(String name, String value)
        {
            delegate.setHeader(name, value);
            internalSetHeader(name, value);
        }

        public void setIntHeader(String name, int value)
        {
            delegate.setIntHeader(name, value);
            internalSetHeader(name, new Integer(value));
        }

        public void setDateHeader(String name, long date)
        {
            delegate.setDateHeader(name, date);
            internalSetHeader(name, new Long(date));
        }

        public void sendError(int sc, String msg) throws IOException
        {
            delegate.sendError(sc, msg);
            didError = true;
        }

        public void sendError(int sc) throws IOException
        {
            delegate.sendError(sc);
            didError = true;
        }

        public void sendRedirect(String location) throws IOException
        {
            delegate.sendRedirect(location);
            didRedirect = true;
        }

        public String encodeURL(String url)
        {
            return delegate.encodeURL(url);
        }

        public String encodeRedirectURL(String url)
        {
            return delegate.encodeRedirectURL(url);
        }

        public void addHeader(String name, String value)
        {
            internalAddHeader(name, value);
        }

        public void addIntHeader(String name, int value)
        {
            internalAddHeader(name, new Integer(value));
        }

        public void addDateHeader(String name, long value)
        {
            internalAddHeader(name, new Long(value));
        }

        /**
         * @deprecated
         */
        public String encodeUrl(String url)
        {
            return this.encodeURL(url);
        }

        /**
         * @deprecated
         */
        public String encodeRedirectUrl(String url)
        {
            return this.encodeRedirectURL(url);
        }
    }

    private class CacheServletOutputStream extends ServletOutputStream
    {

        ServletOutputStream delegate;
        ByteArrayOutputStream cache;

        CacheServletOutputStream(ServletOutputStream out)
        {
            delegate = out;
            cache = new ByteArrayOutputStream(4096);
        }

        public ByteArrayOutputStream getBuffer()
        {
            return cache;
        }

        public void write(int b) throws IOException
        {
            delegate.write(b);
            cache.write(b);
        }

        public void write(byte b[]) throws IOException
        {
            delegate.write(b);
            cache.write(b);
        }

        public void write(byte buf[], int offset, int len) throws IOException
        {
            delegate.write(buf, offset, len);
            cache.write(buf, offset, len);
        }
    }
}
