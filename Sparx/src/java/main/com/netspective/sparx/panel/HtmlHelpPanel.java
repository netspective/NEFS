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
import java.io.StringWriter;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.theme.Theme;

public class HtmlHelpPanel extends TemplateContentPanel
{
    private ValueSource popupImageSrc = ValueSource.NULL_VALUE_SOURCE;
    private ValueSource popupHelpUrl;
    private DialogField field;
    private boolean popup = true;
    private ValueSource popupWindowName = new StaticValueSource("SparxPopupHelp");
    private ValueSource popupWindowFeatures = new StaticValueSource("width=520,height=350,scrollbars,resizable");

    public HtmlHelpPanel(DialogField field)
    {
        this.field = field;
        popupHelpUrl = ValueSources.getInstance().getValueSource("page-id:/help", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
    }

    public boolean isPopup()
    {
        return popup;
    }

    public void setPopup(boolean popup)
    {
        this.popup = popup;
    }

    public ValueSource getPopupImageSrc()
    {
        return popupImageSrc;
    }

    public void setPopupImageSrc(ValueSource popupImageSrc)
    {
        this.popupImageSrc = popupImageSrc;
    }

    public ValueSource getPopupHelpUrl()
    {
        return popupHelpUrl;
    }

    public void setPopupHelpUrl(ValueSource popupHelpUrl)
    {
        this.popupHelpUrl = popupHelpUrl;
    }

    public ValueSource getPopupWindowFeatures()
    {
        return popupWindowFeatures;
    }

    public void setPopupWindowFeatures(ValueSource popupWindowFeatures)
    {
        this.popupWindowFeatures = popupWindowFeatures;
    }

    public ValueSource getPopupWindowName()
    {
        return popupWindowName;
    }

    public void setPopupWindowName(ValueSource popupWindowName)
    {
        this.popupWindowName = popupWindowName;
    }

    public String getPopupHtml(DialogContext dc, Theme theme)
    {
        String imageUrl = getPopupImageSrc().getTextValue(dc);
        if(imageUrl == null)
            imageUrl = theme.getResourceUrl("/images/item-help.gif");

        return "<a href='#' style='cursor:hand;' onclick=\"javascript:showHelp('" + popupHelpUrl.getTextValue(dc) + "/dialog/" + dc.getDialog().getQualifiedName() + "/" + field.getQualifiedName() + "', '" + popupWindowName.getTextValue(dc) + "', '" + popupWindowFeatures.getTextValue(dc) + "');return false;\"><img border='0' src='" + imageUrl + "' alt='Help'></a>&nbsp;";
    }

    public String getInlineHtml(DialogContext dc, Theme theme)
    {
        String imageUrl = getPopupImageSrc().getTextValue(dc);
        if(imageUrl == null)
            imageUrl = theme.getResourceUrl("/images/item-help.gif");

        StringWriter sw = new StringWriter();
        String html = "";
        try
        {
            render(sw, dc, theme, 0);
            html = sw.toString();
        }
        catch(IOException e)
        {
            html = e.toString();
        }

        return "<img border='0' src='" + imageUrl + "' title=" + html + " alt='Help'>&nbsp;";
    }

    public String getDialogFieldHelpHtml(DialogContext dc, Theme theme)
    {
        return isPopup() ? getPopupHtml(dc, theme) : getInlineHtml(dc, theme);
    }
}
