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
 * $Id: XdmComponentTask.java,v 1.1 2003-03-13 18:33:12 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import com.netspective.commons.io.Resource;

public class XdmComponentTask extends Task
{
    private File xdmFile;
    private Resource xdmResource;
    private boolean metrics = false;
    private boolean debug = false;
    private boolean executeHandled = false;
    private File genIdConstantsRootPath;
    private String genIdConstantsRootPkgAndClass;

    public void init() throws BuildException
    {
        xdmFile = null;
        xdmResource = null;
        debug = false;
        metrics = false;
        executeHandled = false;
        genIdConstantsRootPath = null;
        genIdConstantsRootPkgAndClass = null;
    }

    public boolean isExecuteHandled()
    {
        return executeHandled;
    }

    public boolean isMetrics()
    {
        return metrics;
    }

    public void setMetrics(boolean metrics)
    {
        this.metrics = metrics;
    }

    public boolean isDebug()
    {
        return debug;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public void setXdmResource(String xdmResource)
    {
        this.xdmResource = new Resource(this.getClass(), xdmResource);
    }

    public void setXdmFile(File xdmFile)
    {
        this.xdmFile = xdmFile;
    }

    public void setIdConstantsDir(File genIdConstantsRootPath)
    {
        this.genIdConstantsRootPath = genIdConstantsRootPath;
    }

    public void setIdConstantsClass(String genIdConstantsRootPkgAndClass)
    {
        this.genIdConstantsRootPkgAndClass = genIdConstantsRootPkgAndClass;
    }

    public boolean generateIdentifierConstants(XdmComponent component) throws BuildException
    {
        if(genIdConstantsRootPath != null || genIdConstantsRootPkgAndClass != null)
        {
            if(genIdConstantsRootPath == null || genIdConstantsRootPkgAndClass == null)
                throw new BuildException("idConstantsFile and idConstantsClass are both required to generate Identifier Constants.");

            try
            {
                component.generateIdentifiersConstants(genIdConstantsRootPath, genIdConstantsRootPkgAndClass);
                log("Generated '"+ genIdConstantsRootPkgAndClass +"' in " + genIdConstantsRootPath.getAbsolutePath());
            }
            catch (IOException e)
            {
                throw new BuildException(e);
            }
            return true;
        }
        else
            return false;
    }

    public XdmComponent getComponent(Class componentClass) throws BuildException
    {
        if(xdmFile == null && xdmResource == null)
            throw new BuildException("No resource or file attributes supplied.");

        XdmComponent component = null;
        int flags = XdmComponentFactory.XDMCOMPFLAG_ALLOWRELOAD;

        try
        {
            if(xdmFile != null)
                component = XdmComponentFactory.get(componentClass, xdmFile, flags);
            else if(xdmResource != null)
                component = XdmComponentFactory.get(componentClass, xdmResource, flags);
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }

        List errors = component.getErrors();
        if(errors.size() > 0)
        {
            for(int i = 0; i < errors.size(); i++)
            {
                Object error = errors.get(i);
                if(error instanceof Exception)
                    throw new BuildException((Exception) error);
                log(error.toString());
            }
        }

        if(isMetrics())
            log(component.getMetrics().toString());

        return component;
    }
}
