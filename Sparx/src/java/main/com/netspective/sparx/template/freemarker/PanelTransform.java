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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractPanel;
import com.netspective.sparx.panel.BasicHtmlPanelValueContext;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.HtmlPanelSkin;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.value.HttpServletValueContext;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateTransformModel;

public class PanelTransform implements TemplateTransformModel
{
    private static final Log log = LogFactory.getLog(PanelTransform.class);

    public PanelTransform()
    {
    }

    public Writer getWriter(Writer out, Map args)
    {
        return new PanelWriter(out, args);
    }

    private class PanelWriter extends Writer
    {
        private Map args;
        private Writer out;
        private HtmlPanel panel;
        private HttpServletValueContext vc;
        private String panelSkinName;
        private HtmlPanelSkin tabbedPanelSkin;
        private HtmlPanelValueContext pvc;

        public class TransformPanel extends AbstractPanel
        {
            public TransformPanel()
            {
                Object heading = args.get("heading");
                if (heading != null)
                    getFrame().setHeading(new StaticValueSource(heading.toString()));
                Object skin = args.get("skin");
                if (skin != null)
                    panelSkinName = skin.toString();
            }

            public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
            {
            }

            public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
            {
            }
        }

        public PanelWriter(Writer out, Map args)
        {
            this.out = out;
            this.args = args;
            this.panel = new TransformPanel();

            Environment env = Environment.getCurrentEnvironment();
            StringModel model = null;
            try
            {
                model = (freemarker.ext.beans.StringModel) env.getDataModel().get("vc");
            }
            catch (TemplateModelException e)
            {
                log.error(e);
            }

            vc = (HttpServletValueContext) model.getWrappedObject();
            pvc = new BasicHtmlPanelValueContext(vc.getServlet(), vc.getRequest(), vc.getResponse(), panel);
            tabbedPanelSkin = panelSkinName == null
                    ? vc.getActiveTheme().getTabbedPanelSkin()
                    : (HtmlPanelSkin) vc.getActiveTheme().getPanelSkins().get(panelSkinName);
            try
            {
                tabbedPanelSkin.renderPanelRegistration(out, pvc);
                tabbedPanelSkin.renderFrameBegin(out, pvc);
            }
            catch (IOException e)
            {
                log.error(e);
            }
        }

        public void write(char[] cbuf, int off, int len) throws IOException
        {
            out.write(cbuf, off, len);
        }

        public void flush() throws IOException
        {
            out.flush();
        }

        public void close() throws IOException
        {
            tabbedPanelSkin.renderFrameEnd(out, pvc);
        }
    }
}