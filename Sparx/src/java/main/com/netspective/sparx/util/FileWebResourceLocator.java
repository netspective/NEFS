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
 * $Id: FileWebResourceLocator.java,v 1.1 2003-08-22 03:33:44 shahid.shah Exp $
 */

package com.netspective.sparx.util;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link WebResourceLocator} that uses files in a specified directory as the
 * source of resource.
 */
public class FileWebResourceLocator implements WebResourceLocator
{
    private static final Log log = LogFactory.getLog(FileWebResourceLocator.class);
    protected static final boolean SEP_IS_SLASH = File.separatorChar == '/';
    private final Map cache = Collections.synchronizedMap(new HashMap());
    private final String rootUrl;
    private final File baseDir;
    private final String canonicalPath;
    private final boolean cacheLocations;

    /**
     * Creates a new file resource locator that will use the specified directory
     * as the base directory for loading templates.
     * @param baseDir the base directory for loading templates
     */
    public FileWebResourceLocator(final String rootUrl, final File baseDir, final boolean cacheLocations) throws IOException
    {
        this.rootUrl = rootUrl;
        this.cacheLocations = cacheLocations;
        try
        {
            Object[] retval = (Object[]) AccessController.doPrivileged(new PrivilegedExceptionAction()
            {
                public Object run()
                        throws
                        IOException
                {
                    if (!baseDir.exists())
                    {
                        throw new FileNotFoundException(baseDir + " does not exist.");
                    }
                    if (!baseDir.isDirectory())
                    {
                        throw new IOException(baseDir + " is not a directory.");
                    }
                    Object[] retval = new Object[2];
                    retval[0] = baseDir.getCanonicalFile();
                    retval[1] = ((File) retval[0]).getPath() + File.separatorChar;
                    return retval;
                }
            });
            this.baseDir = (File) retval[0];
            this.canonicalPath = (String) retval[1];
        }
        catch (PrivilegedActionException e)
        {
            throw (IOException) e.getException();
        }
    }

    public WebResource findWebResource(final String name) throws IOException
    {
        final boolean logging = log.isDebugEnabled();
        if(logging) log.debug("FileWebResourceLocator searching for " + name);

        if(cacheLocations)
        {
            WebResource resource = (WebResource) cache.get(name);
            if(resource != null)
            {
                if(logging) log.debug("FileWebResourceLocator cache hit for " + resource);
                return resource;
            }
        }

        try
        {
            return (WebResource) AccessController.doPrivileged(new PrivilegedExceptionAction()
            {
                public Object run() throws IOException
                {
                    File source = new File(baseDir, SEP_IS_SLASH ? name : name.replace(File.separatorChar, '/'));
                    // Security check for inadvertently returning something outside the
                    // resource directory.
                    String normalized = source.getCanonicalPath();
                    if (!normalized.startsWith(canonicalPath))
                    {
                        throw new SecurityException();
                    }

                    if(logging) log.debug("FileWebResourceLocator looking for '"+ name +"' as " + source);
                    WebResource result = source.exists() ? new WebResource(rootUrl, name, source) : null;
                    if(result != null)
                    {
                        if(logging) log.debug("FileWebResourceLocator found " + result);
                        if(cacheLocations) cache.put(name, result);
                    }
                    return result;
                }
            });
        }
        catch (PrivilegedActionException e)
        {
            throw (IOException) e.getException();
        }
    }

    public String toString()
    {
        return getClass().getName() + " ["+ hashCode() +"] baseDir = '"+ baseDir +"', canonicalPath = '"+ canonicalPath +"'";
    }
}