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
 * $Id: FreeMarkerConfigurationAdapter.java,v 1.13 2003-07-05 19:28:07 shahid.shah Exp $
 */

package com.netspective.sparx.template.freemarker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.NestableRuntimeException;

import freemarker.template.Configuration;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;

public class FreeMarkerConfigurationAdapter
{
    private Configuration configuration;
    private boolean defaultAdapter;
    private String name;
    private File baseDir;
    private Class baseClass;

    public FreeMarkerConfigurationAdapter()
    {
    }

    public boolean isDefault()
    {
        return defaultAdapter;
    }

    public void setDefault(boolean defaultAdapter)
    {
        this.defaultAdapter = defaultAdapter;
        if(defaultAdapter)
            Configuration.setDefaultConfiguration(configuration);
    }

    public Configuration getConfiguration()
    {
        if(configuration == null)
        {
            configuration = new Configuration();
            configuration.setTemplateUpdateDelay(0);
            configuration.setTemplateLoader(FreeMarkerConfigurationAdapters.getInstance().getStringTemplateLoader());
            configuration.setSharedVariable("templateExists", new TemplateExistsMethod());
            configuration.setSharedVariable("getXmlDataModelSchema", new XmlDataModelSchemaMethod());
            configuration.setSharedVariable("getClassForName", new ClassReferenceMethod());
            configuration.setSharedVariable("getClassInstanceForName", new ClassInstanceMethod());
            configuration.setSharedVariable("getAntBuildProject", new AntBuildProjectMethod());
            configuration.setSharedVariable("getInputSourceDependencies", new InputSourceDependenciesMethod());
            configuration.setSharedVariable("executeCommand", new ExecuteCommandMethod());
            configuration.setSharedVariable("panel", new PanelTransform());
            configuration.setSharedVariable("statics", BeansWrapper.getDefaultInstance().getStaticModels());
            SyntaxHighlightTransform.registerTransforms(configuration);
        }
        return configuration;
    }

    protected void updateConfiguration()
    {
        List tmplLoaders = new ArrayList();
        tmplLoaders.add(FreeMarkerConfigurationAdapters.getInstance().getStringTemplateLoader());

        try
        {
            if(baseDir != null)
                tmplLoaders.add(new FileTemplateLoader(baseDir));
        }
        catch (IOException e)
        {
            throw new NestableRuntimeException(e);
        }

        if(baseClass != null)
            tmplLoaders.add(new ClassTemplateLoader(baseClass));

        getConfiguration().setTemplateLoader(new MultiTemplateLoader((TemplateLoader[]) tmplLoaders.toArray(new TemplateLoader[tmplLoaders.size()])));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public File getBaseDir()
    {
        return baseDir;
    }

    public void setBaseDir(File baseDir)
    {
        this.baseDir = baseDir;
        updateConfiguration();
    }

    public Class getBaseClass()
    {
        return baseClass;
    }

    public void setBaseClass(Class baseClass)
    {
        this.baseClass = baseClass;
        updateConfiguration();
    }
}
