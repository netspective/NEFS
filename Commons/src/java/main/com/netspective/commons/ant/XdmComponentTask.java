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
package com.netspective.commons.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;

import com.netspective.commons.io.Resource;
import com.netspective.commons.xdm.XdmComponent;
import com.netspective.commons.xdm.XdmComponentFactory;

public abstract class XdmComponentTask extends Task
{
    protected interface ActionHandler
    {
        public String getName();

        public void execute() throws BuildException;
    }

    public static final int CLEAN_FIRST_FALSE = 0;
    public static final int CLEAN_FIRST_DIR = 1;
    public static final int CLEAN_FIRST_PRESERVE_CVS = 2;

    private Map actionHandlers;
    private ActionHandler actionHandler;
    private File projectFile;
    private File destDir;
    private String property;
    private Resource xdmResource;
    private boolean metrics = false;
    private boolean debug = false;
    private boolean executeHandled = false;
    private int cleanFirst = CLEAN_FIRST_FALSE;
    private String genIdConstantsRootPkgAndClass;
    private Delete deleteTask = new Delete();

    public void setTaskName(String name)
    {
        super.setTaskName(name);
        deleteTask.setTaskName(name);
    }

    public void setProject(Project project)
    {
        super.setProject(project);
        deleteTask.setProject(project);
    }

    public void init() throws BuildException
    {
        actionHandler = null;
        projectFile = null;
        destDir = null;
        property = null;
        xdmResource = null;
        debug = false;
        metrics = false;
        executeHandled = false;
        cleanFirst = CLEAN_FIRST_FALSE;
        genIdConstantsRootPkgAndClass = null;
    }

    public void addActionHandler(ActionHandler handler)
    {
        actionHandlers.put(handler.getName(), handler);
    }

    public void setupActionHandlers()
    {
        if(actionHandlers != null)
            return;
        actionHandlers = new HashMap();
    }

    public ActionHandler getActionHandler()
    {
        return actionHandler;
    }

    public void setAction(String action) throws BuildException
    {
        setupActionHandlers();
        actionHandler = (ActionHandler) actionHandlers.get(action);
        if(actionHandler == null)
            throw new BuildException("Unknown action '" + action + "'. Available: " + actionHandlers.keySet());
    }

    public boolean isCleanFirst()
    {
        return cleanFirst != CLEAN_FIRST_FALSE;
    }

    public void setCleanFirst(String cleanFirstStyle)
    {
        if("no".equals(cleanFirstStyle))
            this.cleanFirst = CLEAN_FIRST_FALSE;
        else if("yes".equals(cleanFirstStyle))
            this.cleanFirst = CLEAN_FIRST_DIR;
        else if("preserve-cvs".equals(cleanFirstStyle))
            this.cleanFirst = CLEAN_FIRST_PRESERVE_CVS;
    }

    /**
     * Deletes the contents of the given directory.
     *
     * @param dir The directory to delete
     */
    public synchronized void delete(File dir)
    {
        switch(cleanFirst)
        {
            case CLEAN_FIRST_DIR:
                deleteTask.setDir(dir);
                deleteTask.execute();
                break;

            case CLEAN_FIRST_PRESERVE_CVS:
                if(dir.exists())
                {
                    FileSet everythingButCVSFileSet = new FileSet();
                    everythingButCVSFileSet.setDir(dir);
                    everythingButCVSFileSet.createInclude().setName("**/*");
                    everythingButCVSFileSet.createExclude().setName("CVS");

                    deleteTask.addFileset(everythingButCVSFileSet);
                    deleteTask.setIncludeEmptyDirs(false);
                    deleteTask.execute();
                }
                break;

            default:
                log("Unknown cleanFirst style: " + cleanFirst);
        }
    }

    public boolean isExecuteHandled()
    {
        return executeHandled;
    }

    public String getProperty()
    {
        return property;
    }

    public void setProperty(String property)
    {
        this.property = property;
    }

    public File getDestDir()
    {
        return destDir;
    }

    public void setDestDir(File destDir)
    {
        this.destDir = destDir;
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

    public void setProjectFile(File projectFile)
    {
        this.projectFile = projectFile;
    }

    public void setIdConstantsClass(String genIdConstantsRootPkgAndClass)
    {
        this.genIdConstantsRootPkgAndClass = genIdConstantsRootPkgAndClass;
    }

    public boolean generateIdentifierConstants(XdmComponent component) throws BuildException
    {
        if(getDestDir() != null || genIdConstantsRootPkgAndClass != null)
        {
            if(getDestDir() == null || genIdConstantsRootPkgAndClass == null)
                throw new BuildException("destDir and idConstantsClass are both required to generate Identifier Constants.");

            if(isCleanFirst())
                delete(new File(getDestDir(), genIdConstantsRootPkgAndClass.replace('.', '/')));

            try
            {
                component.generateIdentifiersConstants(getDestDir(), genIdConstantsRootPkgAndClass);
                log("Created ID constants package '" + genIdConstantsRootPkgAndClass + "' in " + getDestDir().getAbsolutePath());
            }
            catch(IOException e)
            {
                throw new BuildException(e);
            }
            return true;
        }
        else
            return false;
    }

    public abstract XdmComponent getComponent();

    protected XdmComponent getComponent(Class componentClass) throws BuildException
    {
        if(projectFile == null && xdmResource == null)
            throw new BuildException("No project resource or file attributes supplied (projectFile).");

        XdmComponent component = null;
        int flags = XdmComponentFactory.XDMCOMPFLAG_ALLOWRELOAD | XdmComponentFactory.XDMCOMPFLAG_INSIDE_ANT;

        try
        {
            if(projectFile != null)
                component = XdmComponentFactory.get(componentClass, projectFile, flags, false);
            else if(xdmResource != null)
                component = XdmComponentFactory.get(componentClass, xdmResource, flags, false);
        }
        catch(Exception e)
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

    public void execute() throws BuildException
    {
        if(getActionHandler() == null)
        {
            setupActionHandlers();
            throw new BuildException("action attribute expected with one of the following values: " + actionHandlers.keySet());
        }

        getActionHandler().execute();
    }
}
