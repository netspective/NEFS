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
package com.netspective.sparx.template.freemarker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.text.TextUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;

public class FreeMarkerConfigurationAdapters
{
    private static final Log log = LogFactory.getLog(FreeMarkerConfigurationAdapters.class);
    private static final FreeMarkerConfigurationAdapters INSTANCE = (FreeMarkerConfigurationAdapters) DiscoverSingleton.find(FreeMarkerConfigurationAdapters.class, FreeMarkerConfigurationAdapters.class.getName());

    private StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
    private Map configs = new HashMap();
    private FreeMarkerConfigurationAdapter defaultAdapter;

    public static FreeMarkerConfigurationAdapters getInstance()
    {
        return INSTANCE;
    }

    public FreeMarkerConfigurationAdapters()
    {
    }

    public StringTemplateLoader getStringTemplateLoader()
    {
        return stringTemplateLoader;
    }

    public FreeMarkerConfigurationAdapter getDefaultAdapter()
    {
        return defaultAdapter;
    }

    public void setDefaultAdapter(FreeMarkerConfigurationAdapter defaultAdapter)
    {
        this.defaultAdapter = defaultAdapter;
    }

    public FreeMarkerConfigurationAdapter createConfigurationAdapter()
    {
        return new FreeMarkerConfigurationAdapter();
    }

    public void addConfiguration(FreeMarkerConfigurationAdapter adapter)
    {
        configs.put(adapter.getName(), adapter);
        if(adapter.isDefault())
            setDefaultAdapter(adapter);
    }

    public FreeMarkerConfigurationAdapter getConfiguration(String name)
    {
        return (FreeMarkerConfigurationAdapter) configs.get(name);
    }

    public void configureSharedVariables(Configuration configuration)
    {
        configuration.setSharedVariable("templateExists", new TemplateExistsMethod());
        configuration.setSharedVariable("getXmlDataModelSchema", new XmlDataModelSchemaMethod());
        configuration.setSharedVariable("getClassForName", new ClassReferenceMethod());
        configuration.setSharedVariable("getClassSourceForName", new ClassSourceMethod());
        configuration.setSharedVariable("getClassInstanceForName", new ClassInstanceMethod());
        configuration.setSharedVariable("getAntBuildProject", new AntBuildProjectMethod());
        configuration.setSharedVariable("getFile", new GetFileMethod());
        configuration.setSharedVariable("getXmlDoc", new GetXmlDocumentMethod());
        configuration.setSharedVariable("getInputSourceDependencies", new InputSourceDependenciesMethod());
        configuration.setSharedVariable("executeCommand", new ExecuteCommandMethod());
        configuration.setSharedVariable("getQueryResultSet", new GetQueryResultSetMethod());
        configuration.setSharedVariable("getQueryResultsAsMatrix", new GetQueryResultsAsMatrixMethod());
        configuration.setSharedVariable("getQueryResultsAsMapArray", new GetQueryResultsAsMapArrayMethod(false));
        configuration.setSharedVariable("getQueryResultsAsMapArrayWithLabelAsKey", new GetQueryResultsAsMapArrayMethod(true));
        configuration.setSharedVariable("getQueryResultsSingleRowAsMap", new GetQueryResultsSingleRowAsMapMethod(false));
        configuration.setSharedVariable("getQueryResultsSingleRowAsMapWithLabelAsKey", new GetQueryResultsSingleRowAsMapMethod(true));
        configuration.setSharedVariable("getFileContentsSyntaxHighlighted", new GetFileContentsSyntaxHighlightedMethod());
        configuration.setSharedVariable("panel", new PanelTransform());
        configuration.setSharedVariable("command", new CommandTransform());
        configuration.setSharedVariable("statics", BeansWrapper.getDefaultInstance().getStaticModels());
        SyntaxHighlightTransform.registerTransforms(configuration);
    }

    public Configuration constructWebAppConfiguration(ServletContext servletContext)
    {
        Configuration result = new Configuration();
        applyWebAppConfiguration(result, servletContext);
        return result;
    }

    public void applyWebAppConfiguration(Configuration fmConfig, ServletContext servletContext)
    {
        String templatePathsText = servletContext.getInitParameter("com.netspective.sparx.template.freemarker.template-paths");
        String templatePathsDelim = servletContext.getInitParameter("com.netspective.sparx.template.freemarker.template-path-delim");

        List templateLoaders = new ArrayList();
        templateLoaders.add(stringTemplateLoader);
        templateLoaders.add(new WebappTemplateLoader(servletContext));

        try
        {
            if(templatePathsText != null)
            {
                String[] templatePaths = TextUtils.getInstance().split(templatePathsText, templatePathsDelim == null
                                                                                          ? File.pathSeparator
                                                                                          : templatePathsDelim, true);
                for(int i = 0; i < templatePaths.length; i++)
                    templateLoaders.add(new FileTemplateLoader(new File(servletContext.getRealPath(templatePaths[i]))));
            }
        }
        catch(Exception e)
        {
            log.error("Unable to setup file templates loader.", e);
            throw new NestableRuntimeException(e);
        }

        // allow stuff to be loaded from CLASSPATH too (like Console, etc)
        templateLoaders.add(new ClassTemplateLoader(com.netspective.sparx.ProductRelease.class));
        templateLoaders.add(new ClassTemplateLoader(com.netspective.axiom.ProductRelease.class));
        templateLoaders.add(new ClassTemplateLoader(com.netspective.commons.ProductRelease.class));

        fmConfig.setTemplateLoader(new MultiTemplateLoader((TemplateLoader[]) templateLoaders.toArray(new TemplateLoader[templateLoaders.size()])));

        configureSharedVariables(fmConfig);
    }

    public int size()
    {
        return configs.size();
    }
}
