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

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.template.AbstractTemplateProcessor;
import com.netspective.commons.template.TemplateProcessorException;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.sparx.value.ServletValueContext;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerTemplateProcessor extends AbstractTemplateProcessor
{
    public static final String VCATTRNAME_SHARED_FM_CONFIG = "SHARED_CONFIG";
    private static final Log log = LogFactory.getLog(FreeMarkerTemplateProcessor.class);

    private FreeMarkerConfigurationAdapter fmConfigAdapter;
    private String configName;
    private ValueSource source;

    public FreeMarkerTemplateProcessor()
    {
    }

    public String getConfig()
    {
        return configName;
    }

    public void setConfig(String config)
    {
        this.configName = config;
        FreeMarkerConfigurationAdapter adapter = FreeMarkerConfigurationAdapters.getInstance().getConfiguration(config);
        if(adapter == null)
            throw new RuntimeException("FreeMarkerConfigurationAdapter '" + config + "' not found.");
        else
            fmConfigAdapter = adapter;
    }

    public ValueSource getSource()
    {
        return source;
    }

    public void setSource(ValueSource source)
    {
        this.source = source;
    }

    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        super.finalizeConstruction(pc, element, elementName);
        finalizeContents();
    }

    public void finalizeContents()
    {
        if(source == null)
            FreeMarkerConfigurationAdapters.getInstance().getStringTemplateLoader().addTemplate(Integer.toString(this.hashCode()), getTemplateContent());
    }

    public void process(Writer writer, ValueContext vc, Map templateVars) throws IOException, TemplateProcessorException
    {
        Configuration fmConfig = fmConfigAdapter == null ?
                                 ((vc instanceof ServletValueContext)
                                  ? ((ServletValueContext) vc).getFreeMarkerConfiguration() : null) :
                                 fmConfigAdapter.getConfiguration();

        // if we have a shared configuration (like from a theme or something) then use it
        Configuration sharedConfig = (Configuration) vc.getAttribute(VCATTRNAME_SHARED_FM_CONFIG);
        if(sharedConfig != null)
            fmConfig = sharedConfig;

        // fmConfig may be null if not running from a Servlet or other templating environment (like within Ant)
        if(fmConfig == null)
        {
            FreeMarkerConfigurationAdapter adapter = FreeMarkerConfigurationAdapters.getInstance().createConfigurationAdapter();
            fmConfig = adapter.getConfiguration();
        }

        try
        {
            Map instanceVars = new HashMap();
            Map sharedVars = (Map) vc.getAttribute(VCATTRNAME_SHARED_TEMPLATE_VARS);
            if(sharedVars != null)
                instanceVars.putAll(sharedVars);
            if(templateVars != null)
                instanceVars.putAll(templateVars);

            Template template = null;
            if(source != null)
            {
                String sourceText = source.getTextValue(vc);
                template = fmConfig.getTemplate(sourceText);
                instanceVars.put("source", sourceText);
            }
            else
                template = fmConfig.getTemplate(Integer.toString(this.hashCode()));

            instanceVars.put("vc", BeansWrapper.getDefaultInstance().wrap(vc));
            template.process(instanceVars, writer);
        }
        catch(TemplateException e)
        {
            log.error("Unable to process template", e);
            throw new TemplateProcessorException(e);
        }
    }
}
