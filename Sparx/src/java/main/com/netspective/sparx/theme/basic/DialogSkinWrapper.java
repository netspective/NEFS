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
 * $Id: DialogSkinWrapper.java,v 1.1 2004-07-11 02:27:45 shahid.shah Exp $
 */

package com.netspective.sparx.theme.basic;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.netspective.commons.template.TemplateProcessor;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogSkin;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.type.GridField;
import com.netspective.sparx.form.field.type.SeparatorField;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.template.freemarker.FreeMarkerTemplateProcessor;
import com.netspective.sparx.security.LoginDialogSkin;

public class DialogSkinWrapper extends BasicHtmlPanelSkin implements DialogSkin, LoginDialogSkin
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    private String pageMetaDataHtmlVarName = "pageMetaDataHtml";
    private String dialogHtmlVarName = "dialogHtml";
    private DialogSkin wrappedSkin;
    private TemplateProcessor body;

    public void addWrappedDialogSkin(DialogSkin wrappedSkin)
    {
        this.wrappedSkin = wrappedSkin;
    }

    public TemplateProcessor createBody()
    {
        return new FreeMarkerTemplateProcessor();
    }

    public void addBody(TemplateProcessor templateProcessor)
    {
        body = templateProcessor;
    }

    public Map createDefaultBodyTemplateVars(DialogContext dc) throws IOException
    {
        Map result = new HashMap();
        StringWriter pageMetaDataHtml = new StringWriter();
        StringWriter dialogHtml = new StringWriter();

        if (pageMetaDataHtmlVarName != null && pageMetaDataHtmlVarName.length() > 0)
        {
            final NavigationContext navigationContext = dc.getNavigationContext();
            navigationContext.getSkin().renderPageMetaData(pageMetaDataHtml, navigationContext);
            result.put(pageMetaDataHtmlVarName, pageMetaDataHtml.toString());
        }

        wrappedSkin.setTheme(getTheme());
        wrappedSkin.renderHtml(dialogHtml, dc);
        result.put(dialogHtmlVarName, dialogHtml.toString());

        return result;
    }

    public TemplateProcessor getBodyTemplate()
    {
        return body;
    }

    public void renderHtml(Writer writer, DialogContext dc) throws IOException
    {
        if (body != null)
            body.process(writer, dc, createDefaultBodyTemplateVars(dc));
        else
            wrappedSkin.renderHtml(writer, dc);
    }

    public String getControlAreaFontAttrs()
    {
        return wrappedSkin.getControlAreaFontAttrs();
    }

    public String getControlAreaReadonlyStyleClass()
    {
        return wrappedSkin.getControlAreaReadonlyStyleClass();
    }

    public String getControlAreaRequiredStyleClass()
    {
        return wrappedSkin.getControlAreaRequiredStyleClass();
    }

    public String getControlAreaStyleAttrs()
    {
        return wrappedSkin.getControlAreaStyleAttrs();
    }

    public String getControlAreaStyleClass()
    {
        return wrappedSkin.getControlAreaStyleClass();
    }

    public String getDefaultControlAttrs()
    {
        return wrappedSkin.getDefaultControlAttrs();
    }

    public void renderCompositeControlsHtml(Writer writer, DialogContext dc, DialogField field) throws IOException
    {
        wrappedSkin.renderCompositeControlsHtml(writer, dc, field);
    }

    public void renderGridControlsHtml(Writer writer, DialogContext dc, GridField gridField) throws IOException
    {
        wrappedSkin.renderGridControlsHtml(writer, dc, gridField);
    }

    public void renderRedirectHtml(Writer writer, DialogContext dc, String redirectUrl) throws IOException
    {
        wrappedSkin.renderRedirectHtml(writer, dc, redirectUrl);
    }

    public void renderSeparatorHtml(Writer writer, DialogContext dc, SeparatorField field) throws IOException
    {
        wrappedSkin.renderSeparatorHtml(writer, dc, field);
    }
}
