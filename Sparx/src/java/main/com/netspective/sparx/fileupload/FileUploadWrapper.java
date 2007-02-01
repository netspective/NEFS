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

package com.netspective.sparx.fileupload;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * A wrapper class for the HttpServletRequest to handle multipart/form-data
 * requests, the kind of requests that support file uploads.
 */
public class FileUploadWrapper extends HttpServletRequestWrapper
{
    // The file upload utility class instance
    private FileUpload mreq = null;

    /**
     * The constructor for the HttpServletRequestWrapper
     *
     * @param <b>request </b>The original request
     * @param <b>dir</b> file upload directory path
     *
     * @throws <b>IOException</b> If there was a failure while instantiating
     *                            the FileUpload object
     */
    public FileUploadWrapper(HttpServletRequest req, String dir, String prefix, String uploadFileArg)
            throws IOException
    {
        super(req);
        mreq = new FileUpload(req, dir, prefix, uploadFileArg);
    }

    /**
     * Returns the value of a request parameter as a String, or null if
     * the parameter does not exist.The method overrides parent method.
     *
     * @param <b>name</b> The name of the parameter
     *
     * @return <b>String </b> The value of the paramter
     */
    public String getParameter(String name)
    {
        return mreq.getParameter(name);
    }

    /**
     * Returns an Enumeration of String objects containing the names of the
     * parameters contained in this request. If the  request has no parameters,
     * the method returns an empty Enumeration.The method overrides parent method.
     *
     * @return <b>Enumeration </b>of String objects, each String containing the
     *         name of a request parameter; or an empty Enumeration if the request has
     *         no parameters
     */
    public java.util.Enumeration getParameterNames()
    {
        return mreq.getParameterNames();
    }

    /**
     * Returns an array of String objects containing all of the values the given
     * request parameter has, or null if the parameter does not exist.
     * The method overrides parent method.
     *
     * @param <b>name</b> the name of the parameter whose value is requested
     *
     * @return <b><String[]</b> an array of String objects containing the
     *         parameter's values
     */
    public String[] getParameterValues(String name)
    {
        return mreq.getParameterValues(name);
    }


    /**
     * Returns a Map of String names as keys, String arrays as values 
     * containing all of the request.
     *
     * @return Map of String names as keys, String arrays as values
     */
    public Map getParameterMap()
	{
		return mreq.getParameterMap();
	}
}
