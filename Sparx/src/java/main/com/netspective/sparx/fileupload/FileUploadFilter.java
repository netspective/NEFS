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

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.text.TextUtils;

/**
 * This class is implemented as a Servlet Filter class to upload files to server
 * The filter detects multipart form data requests and uploads any file to an
 * upload directory. The upload directory is specified relative the webapp root
 * in web.xml as an init param to the filter
 */
public final class FileUploadFilter implements Filter
{
    private static final String FILTERPARAM_UPLOAD_DIRS = "uploadDirs";
    private static final String FILTERPARAM_UPLOADED_FILES_PREFIX = "uploadPrefix";
    private static final String FILTERPARAM_UPLOADED_FILES_REQUEST_ATTR_NAME = "uploadFileArg";

    private static final String FILTERPARAMVALUE_DEFAULT_UPLOADED_FILES_PREFIX = "sparxUpload-";
    private static final String FILTERPARAMVALUE_DEFAULT_UPLOADED_FILES_REQUEST_ATTR_NAME = "sparxUpload";

    private Log log = LogFactory.getLog(FileUploadFilter.class);

    private String uploadDir = null;
    private String uploadPrefix = null;
    private String uploadFileArg = null;

    /**
     * This method is called by the server before the filter goes into service,
     * and here it determines the file upload directory.
     *
     * @param <b>config</b> The filter config passed by the servlet engine
     */
    public void init(FilterConfig config)
            throws ServletException
    {
        String tryDirectoryNames = config.getInitParameter(FILTERPARAM_UPLOAD_DIRS); // comma-separated list of directories to try
        if(tryDirectoryNames != null)
        {
            // find the first available directory either as a resource or a physical directory
            String[] tryDirectories = TextUtils.getInstance().split(tryDirectoryNames, ",", true);
            for(int i = 0; i < tryDirectories.length; i++)
            {
                String tryDirectory = tryDirectories[i];
                try
                {
                    // first try the directory as a servlet resource
                    java.net.URL uploadDirURL = config.getServletContext().getResource(tryDirectory);
                    if(uploadDirURL != null && uploadDirURL.getFile() != null)
                    {
                        uploadDir = uploadDirURL.getFile();
                        break;
                    }

                    File f = new File(tryDirectory);
                    if(f.exists())
                    {
                        uploadDir = f.getAbsolutePath();
                        break;
                    }

                }
                catch(java.net.MalformedURLException ex)
                {
                    throw new ServletException(ex.getMessage());
                }
            }
        }

        // If upload directory parameter is null, assign a temp directory
        if(uploadDir == null)
        {
            File tempdir = (File) config.getServletContext().getAttribute("javax.servlet.context.tempdir");
            if(tempdir != null)
            {
                uploadDir = tempdir.toString();
            }
            else
            {
                throw new ServletException("Error in FileUploadFilter : No upload " +
                                           "directory found: set an uploadDir init " +
                                           "parameter or ensure the " +
                                           "javax.servlet.context.tempdir directory " +
                                           "is valid");
            }
        }

        uploadPrefix = config.getInitParameter(FILTERPARAM_UPLOADED_FILES_PREFIX);
        if(uploadPrefix == null)
            uploadPrefix = FILTERPARAMVALUE_DEFAULT_UPLOADED_FILES_PREFIX;
        log.info("uploadPrefix is: " + uploadPrefix);

        uploadFileArg = config.getInitParameter(FILTERPARAM_UPLOADED_FILES_REQUEST_ATTR_NAME);
        if(uploadFileArg == null)
            uploadFileArg = FILTERPARAMVALUE_DEFAULT_UPLOADED_FILES_REQUEST_ATTR_NAME;
        log.info("uploadFileArg is: " + uploadFileArg);

    }

    /**
     * This method performs the actual filtering work .In its doFilter() method,
     * each filter receives the current request and response, as well as a
     * FilterChain containing the filters that still must be processed. Here
     * the doFilter() method examines the request content type, and if it is a
     * multipart/form-data request, wraps the request with a FileUpload class.
     *
     * @param <b> request </b> The servlet request
     * @param <b> response </b> The servlet response
     * @param <b> chain </b> Object representing the chain of all filters
     */
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException
    {

        HttpServletRequest req = (HttpServletRequest) request;

        // Get the content type from the request
        String content = req.getHeader("Content-Type");

        // If the content type is not a multipart/form-data , continue
        if(content == null || !content.startsWith("multipart/form-data"))
        {
            chain.doFilter(request, response);
        }
        else
        {
            log.debug("Procesing multipart request");
            FileUploadWrapper load = new FileUploadWrapper(req, uploadDir, uploadPrefix, uploadFileArg);
            chain.doFilter(load, response);
        }
    }

    /**
     * This method is called Called after the filter has been taken
     * out of service.
     */
    public void destroy()
    {
    }
}
