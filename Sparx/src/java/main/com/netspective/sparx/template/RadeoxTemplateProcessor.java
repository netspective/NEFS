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
package com.netspective.sparx.template;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseRenderContext;

import com.netspective.commons.template.AbstractTemplateProcessor;
import com.netspective.commons.template.TemplateProcessorException;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.sparx.template.freemarker.FreeMarkerConfigurationAdapters;

public class RadeoxTemplateProcessor extends AbstractTemplateProcessor
{
    private ValueSource DEFAULT_DIV_CLASS = new StaticValueSource("wiki");

    private static final int CONTENTTYPE_PCDATA = 0;
    private static final int CONTENTTYPE_FILE = 1;
    private static final int CONTENTTYPE_VALUE_SOURCE = 2;

    private static final Log log = LogFactory.getLog(RadeoxTemplateProcessor.class);

    private int contentType = CONTENTTYPE_PCDATA;
    private RenderEngine engine = new BaseRenderEngine();
    private ValueSource source;
    private ValueSource divClass = DEFAULT_DIV_CLASS;

    public RadeoxTemplateProcessor()
    {
    }

    public void addTemplateContent(String text)
    {
        super.addTemplateContent(text);
        contentType = CONTENTTYPE_PCDATA;
    }

    public ValueSource getSource()
    {
        return source;
    }

    public void setSourceFile(ValueSource source)
    {
        this.source = source;
        this.contentType = CONTENTTYPE_FILE;
    }

    public void setSourceValue(ValueSource source)
    {
        this.source = source;
        this.contentType = CONTENTTYPE_VALUE_SOURCE;
    }

    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        super.finalizeConstruction(pc, element, elementName);
        finalizeContents();
    }

    public void finalizeContents()
    {
        if (source == null)
            FreeMarkerConfigurationAdapters.getInstance().getStringTemplateLoader().addTemplate(Integer.toString(this.hashCode()), getTemplateContent());
    }

    public ValueSource getDivClass()
    {
        return divClass;
    }

    public void setDivClass(ValueSource divClass)
    {
        this.divClass = divClass;
    }

    public int getContentType()
    {
        return contentType;
    }

    public void process(Writer writer, ValueContext vc, Map templateVars) throws IOException, TemplateProcessorException
    {
        writer.write("<div class=\"" + divClass.getTextValue(vc) + "\">\n");

        RenderContext context = new BaseRenderContext();
        context.set("vc", vc);

        String content = null;

        switch (contentType)
        {
            case CONTENTTYPE_FILE:
                String fileName = source.getTextValue(vc);
                File file = new File(fileName);
                content = file.exists() ? file.getAbsolutePath() : ("FileNotFound: " + fileName);
                break;

            case CONTENTTYPE_PCDATA:
                content = getTemplateContent();
                break;

            case CONTENTTYPE_VALUE_SOURCE:
                content = source.getTextValue(vc);
                break;
        }

        engine.render(writer, getClass().getName() + " " + content, context);

        writer.write("\n</div>");
    }
}
