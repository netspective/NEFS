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
package com.netspective.sparx.panel;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.util.HttpUtils;

public class HtmlIncludePanel extends AbstractPanel
{
    private static final Log log = LogFactory.getLog(HtmlIncludePanel.class);

    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    public static final String REQATTRNAME_NAVIGATION_CONTEXT = "navigationContext";
    public static final String REQATTRNAME_DIALOG_CONTEXT = "dialogContext";
    private boolean local;
    private ValueSource path;

    public HtmlIncludePanel()
    {
    }

    public ValueSource getPath()
    {
        return path;
    }

    public void setPath(ValueSource path)
    {
        this.path = path;
        setLocal(true);
    }

    public void setUrl(ValueSource url)
    {
        this.path = url;
        setLocal(false);
    }

    public boolean isLocal()
    {
        return local;
    }

    public void setLocal(boolean local)
    {
        this.local = local;
    }


    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        BasicHtmlPanelValueContext vc = new BasicHtmlPanelValueContext(nc.getServlet(), nc.getRequest(), nc.getResponse(), this);
        vc.setNavigationContext(nc);
        HtmlPanelSkin templatePanelSkin = theme.getTemplatePanelSkin();
        templatePanelSkin.renderFrameBegin(writer, vc);

        if(path == null)
        {
            writer.write("No path to resource or URL provided.");
            return;
        }

        String includePath = getPath().getTextValue(nc);
        if(local)
        {
            try
            {
                HttpUtils.includeServletResourceContent(writer, nc, includePath, REQATTRNAME_NAVIGATION_CONTEXT);
            }
            catch(ServletException e)
            {
                log.error(e);
                throw new NestableRuntimeException("Error including '" + includePath + "'", e);
            }
        }
        else
            HttpUtils.includeUrlContent(includePath, writer);

        templatePanelSkin.renderFrameEnd(writer, vc);
    }

    public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
    {
        BasicHtmlPanelValueContext vc = new BasicHtmlPanelValueContext(dc.getServlet(), dc.getRequest(), dc.getResponse(), this);
        vc.setNavigationContext(dc.getNavigationContext());
        vc.setDialogContext(dc);
        HtmlPanelSkin templatePanelSkin = theme.getTemplatePanelSkin();
        templatePanelSkin.renderFrameBegin(writer, vc);

        if(path == null)
        {
            writer.write("No path to resource or URL provided.");
            return;
        }

        String includePath = getPath().getTextValue(dc);
        if(local)
        {
            try
            {
                HttpUtils.includeServletResourceContent(writer, dc, includePath, REQATTRNAME_DIALOG_CONTEXT);
            }
            catch(ServletException e)
            {
                log.error(e);
                throw new NestableRuntimeException("Error including '" + includePath + "'", e);
            }
        }
        else
            HttpUtils.includeUrlContent(includePath, writer);

        templatePanelSkin.renderFrameEnd(writer, vc);
    }
}
