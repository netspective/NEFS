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
package com.netspective.sparx.form.field.type;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;

import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportDataSourceScrollState;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldFlags;
import com.netspective.sparx.form.sql.QueryDialogContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;

public class DataSourceNavigatorButtonsField extends DialogField
{
    static public final String RSNAV_BUTTONNAME_NEXT = Dialog.PARAMNAME_CONTROLPREFIX + "rs_nav_next";
    static public final String RSNAV_BUTTONNAME_PREV = Dialog.PARAMNAME_CONTROLPREFIX + "rs_nav_prev";
    static public final String RSNAV_BUTTONNAME_FIRST = Dialog.PARAMNAME_CONTROLPREFIX + "rs_nav_first";
    static public final String RSNAV_BUTTONNAME_LAST = Dialog.PARAMNAME_CONTROLPREFIX + "rs_nav_last";

    static public ValueSource SUBMIT_CAPTION = new StaticValueSource(" OK ");
    static public ValueSource FIRST_CAPTION = new StaticValueSource(" First ");
    static public ValueSource PREV_CAPTION = new StaticValueSource(" Previous ");
    static public ValueSource NEXT_CAPTION = new StaticValueSource(" Next ");
    static public ValueSource LAST_CAPTION = new StaticValueSource(" Last ");
    static public ValueSource DONE_CAPTION = new StaticValueSource(" Done ");

    private ValueSource submitCaption = SUBMIT_CAPTION;
    private ValueSource firstCaption = FIRST_CAPTION;
    private ValueSource prevCaption = PREV_CAPTION;
    private ValueSource nextCaption = NEXT_CAPTION;
    private ValueSource lastCaption = LAST_CAPTION;
    private ValueSource doneCaption = DONE_CAPTION;
    private ValueSource doneUrl;

    public class Flags extends DialogFieldFlags
    {
        public static final int HANDLE_ON_CLICK = DialogFieldFlags.START_CUSTOM;

        public Flags()
        {
            super();
            setFlag(UNAVAILABLE);
        }

        public Flags(State dfs)
        {
            super(dfs);
            setFlag(UNAVAILABLE);
        }
    }

    public DataSourceNavigatorButtonsField()
    {
        super();
        setName("ds_nav_buttons");
    }

    public DialogFieldFlags createFlags()
    {
        return new Flags();
    }

    /**
     * Normally returns true if the input is hidden via a flag or if data might be missing. In our case, we only want
     * to use the flags because we don't really have any "data".
     */
    public boolean isInputHidden(DialogContext dc)
    {
        return isInputHiddenFlagSet(dc);
    }

    public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
    {
        boolean selectableReport = false;
        if (dc instanceof QueryDialogContext)
        {
            QueryDialogContext qdc = (QueryDialogContext) dc;
            HtmlTabularReport reportPanel = qdc.getReportPanel().getReport(dc.getNavigationContext());
            if (reportPanel != null && reportPanel.getFlags().flagIsSet(HtmlTabularReport.Flags.SELECTABLE))
            {
                selectableReport = true;
            }
        }

        String attrs = dc.getSkin().getDefaultControlAttrs();
        TabularReportDataSourceScrollState scrollState = dc.getProject().getScrollStates().getScrollStateByDialogTransactionId(dc);
        if (scrollState == null)
        {
            writer.write("<input type='submit' class=\"dialog-button\" name='" + dc.getDialog().getResetContextParamName() + "' value='" + submitCaption.getTextValue(dc) + "' " + attrs + "> ");
            return;
        }

        TabularReportDataSource dataSource = scrollState.getDataSource();
        boolean isScrollable = dataSource.isScrollable();
        int activePage = scrollState.getActivePage();
        int lastPage = scrollState.getTotalPages();
        writer.write("<span class=\"textbox\"><center>");
        if (lastPage > 0)
        {
            writer.write("<nobr>Page ");
            writer.write(Integer.toString(activePage));
            if (isScrollable)
            {
                writer.write(" of ");
                writer.write(Integer.toString(lastPage));
            }
            writer.write("</nobr>&nbsp;&nbsp;");
            if (activePage > 1)
                writer.write("<input type='submit' class=\"dialog-button\" name='" + RSNAV_BUTTONNAME_FIRST + "' value='" + firstCaption.getTextValue(dc) + "' " + attrs + "> ");

            if (activePage > 2)
                writer.write("<input type='submit' class=\"dialog-button\" name='" + RSNAV_BUTTONNAME_PREV + "' value='" + prevCaption.getTextValue(dc) + "' " + attrs + "> ");

            boolean hasMoreRows = false;
            //dataSource.hasMoreRows()
            if (activePage != lastPage)
            {
                writer.write("<input type='submit' class=\"dialog-button\" name='" + RSNAV_BUTTONNAME_NEXT + "' value='" + nextCaption.getTextValue(dc) + "' " + attrs + "> ");
                hasMoreRows = true;
            }

            if (isScrollable)
            {
                if (activePage < lastPage)
                    writer.write("<input type='submit' class=\"dialog-button\" name='" + RSNAV_BUTTONNAME_LAST + "' value='" + lastCaption.getTextValue(dc) + "' " + attrs + "> ");
                writer.write("&nbsp;&nbsp;<nobr>");
                writer.write(NumberFormat.getNumberInstance().format(dataSource.getTotalRows()));
                writer.write(" total rows</nobr>");
            }
            else if (hasMoreRows)
            {
                writer.write("&nbsp;&nbsp;<nobr>");
                writer.write(NumberFormat.getNumberInstance().format(scrollState.getRowsProcessed()));
                writer.write(" rows so far</nobr>");
            }
            else
            {
                writer.write("&nbsp;&nbsp;<nobr>");
                writer.write(NumberFormat.getNumberInstance().format(scrollState.getRowsProcessed()));
                writer.write(" total rows</nobr>");
            }

        }

        if (doneCaption != null)
        {
            if (doneUrl == null)
                writer.write("&nbsp;&nbsp;<input type='submit' class=\"dialog-button\" name='" + dc.getDialog().getResetContextParamName() + "' value='" + doneCaption.getTextValue(dc) + "' " + attrs + "> ");
            else
                writer.write("&nbsp;&nbsp;<input type='button' class=\"dialog-button\" name='jump' value='" + doneCaption.getTextValue(dc) +
                        "' onclick='this.form.action=\"" + doneUrl.getTextValue(dc) + "\";this.form.submit();'" + attrs + "> ");
        }
        writer.write("</center></span>");
    }

    public ValueSource getDoneCaption()
    {
        return doneCaption;
    }

    public void setDoneCaption(ValueSource doneCaption)
    {
        this.doneCaption = doneCaption;
    }

    public ValueSource getDoneUrl()
    {
        return doneUrl;
    }

    public void setDoneUrl(ValueSource doneUrl)
    {
        this.doneUrl = doneUrl;
    }

    public ValueSource getFirstCaption()
    {
        return firstCaption;
    }

    public void setFirstCaption(ValueSource firstCaption)
    {
        this.firstCaption = firstCaption;
    }

    public ValueSource getLastCaption()
    {
        return lastCaption;
    }

    public void setLastCaption(ValueSource lastCaption)
    {
        this.lastCaption = lastCaption;
    }

    public ValueSource getNextCaption()
    {
        return nextCaption;
    }

    public void setNextCaption(ValueSource nextCaption)
    {
        this.nextCaption = nextCaption;
    }

    public ValueSource getPrevCaption()
    {
        return prevCaption;
    }

    public void setPrevCaption(ValueSource prevCaption)
    {
        this.prevCaption = prevCaption;
    }

    public ValueSource getSubmitCaption()
    {
        return submitCaption;
    }

    public void setSubmitCaption(ValueSource submitCaption)
    {
        this.submitCaption = submitCaption;
    }
}
