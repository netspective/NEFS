/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: GeneralColumn.java,v 1.13 2003-11-07 17:37:49 shahid.shah Exp $
 */

package com.netspective.commons.report.tabular.column;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.report.tabular.calc.ColumnDataCalculator;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportColumnConditionalApplyFlag;
import com.netspective.commons.report.tabular.TabularReportColumnConditionalState;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.calc.TabularReportCalcs;
import com.netspective.commons.report.tabular.TabularReportColumnState;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.command.Command;
import com.netspective.commons.command.Commands;
import com.netspective.commons.command.CommandNotFoundException;

public class GeneralColumn implements TabularReportColumn, TemplateConsumer
{
    static public final String PLACEHOLDER_COLDATA = "{.}";
    static public final String PLACEHOLDER_OPEN = "{";
    static public final String PLACEHOLDER_CLOSE = "}";

    public static final String CTYPE_TEMPLATE_NAMESPACE = TabularReportColumn.class.getName();
    public static final String CTYPE_ATTRNAME_TYPE = "type";
    public static final String[] CTYPE_ATTRNAMES_SET_BEFORE_CONSUMING = null;
    public static final ColumnTypeTemplateConsumerDefn templateConsumer = new ColumnTypeTemplateConsumerDefn();

    static
    {
        TemplateCatalog.registerConsumerDefnForClass(templateConsumer, GeneralColumn.class, true, true);
    }

    protected static class ColumnTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public ColumnTypeTemplateConsumerDefn()
        {
            super(CTYPE_TEMPLATE_NAMESPACE, CTYPE_ATTRNAME_TYPE, CTYPE_ATTRNAMES_SET_BEFORE_CONSUMING);
        }
    }

    private int dataType;
    private int align;
    private int colIndex = -1;
    private ValueSource heading;
    private Command headingCommand;
    private RedirectValueSource redirectUrl;
    private String calcCmd;
    private Format formatter;
    private String outputPattern;
    private int width;
    private Flags flags;
    private String breakHeader;
    private List conditionals;
    private NumberFormat generalNumberFmt = NumberFormat.getNumberInstance();

    public GeneralColumn()
    {
        setColIndex(-1);
        flags = createFlags();
    }

    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        return templateConsumer;
    }

    public void registerTemplateConsumption(Template template)
    {
    }

    public final int getDataType()
    {
        return dataType;
    }

    public final void setDataType(int value)
    {
        dataType = value;
    }

    public final String getBreak()
    {
        return breakHeader;
    }

    public final void setBreak(String header)
    {
        breakHeader = header;
    }

    public final boolean isColIndexSet()
    {
        return colIndex != -1;
    }

    public final int getColIndex()
    {
        return colIndex;
    }

    public final void setColIndex(int value)
    {
        colIndex = value;
    }

    public final ValueSource getHeading()
    {
        return heading;
    }

    public void setHeading(ValueSource heading)
    {
        this.heading = heading;
    }

    public RedirectValueSource getRedirect()
    {
        return redirectUrl;
    }

    public void setRedirect(RedirectValueSource vs)
    {
        this.redirectUrl = vs;
    }

    public Command getHeadingCommand()
    {
        return headingCommand;
    }

    public void setHeadingCommand(String headingCommand) throws CommandNotFoundException
    {
        this.headingCommand = Commands.getInstance().getCommand(headingCommand);;
    }

    public final int getWidth()
    {
        return width;
    }

    public final void setWidth(int value)
    {
        width = value;
    }

    public final int getAlign()
    {
        return align;
    }

    public final void setAlign(AlignStyle value)
    {
        align = value.getValueIndex();
    }

    public Flags createFlags()
    {
        return new Flags();
    }

    public Flags getFlags()
    {
        return flags;
    }

    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

    public final String getCalcCmd()
    {
        return calcCmd;
    }

    public final void setCalc(String value)
    {
        calcCmd = value;
    }

    public final Format getFormatter()
    {
        return formatter;
    }

    public void setFormatter(Format value)
    {
        formatter = value;
    }

    public void setFormat(String value)
    {
        formatter = ReportColumnFactory.getFormat(value);
    }

    public final String getOutput()
    {
        return outputPattern;
    }

    public final void setOutput(String value)
    {
        outputPattern = value;
        getFlags().updateFlag(Flags.HAS_OUTPUT_PATTERN, outputPattern != null);
    }

    public String resolvePattern(String srcStr)
    {
        // find all occurrences of ${.} and replace with ${x} where x is the col index (array)

        int findLoc = srcStr.indexOf(PLACEHOLDER_COLDATA);
        if(findLoc == -1)
            return srcStr;

        getFlags().setFlag(Flags.HAS_PLACEHOLDERS);

        String replacedIn = srcStr;
        String replaceWith = PLACEHOLDER_OPEN + colIndex + PLACEHOLDER_CLOSE;
        while(findLoc >= 0)
        {
            StringBuffer sb = new StringBuffer(replacedIn);
            sb.replace(findLoc, findLoc + PLACEHOLDER_COLDATA.length(), replaceWith);
            replacedIn = sb.toString();
            findLoc = replacedIn.indexOf(PLACEHOLDER_COLDATA);
        }
        return replacedIn;
    }

    public String getFormattedData(TabularReportValueContext rc, TabularReportDataSource ds, int flags)
    {
        Object oData = ds.getActiveRowColumnData(getColIndex(), flags);
        String data = oData == null ? "" : oData.toString();
        if((flags & TabularReportColumn.GETDATAFLAG_DO_CALC) != 0)
        {
            ColumnDataCalculator calc = rc.getCalc(getColIndex());
            if(calc != null)
                calc.addValue(rc, this, ds, data);
        }
        return data;
    }

    public String getFormattedData(TabularReportValueContext rc, ColumnDataCalculator calc)
    {
        if(calc != null)
        {
            if(formatter != null)
                synchronized(formatter)
                {
                    return formatter.format(new Double(calc.getValue(rc)));
                }
            else
                synchronized(generalNumberFmt)
                {
                    return generalNumberFmt.format(calc.getValue(rc));
                }
        }
        else
            return null;
    }

    public List getConditionals()
    {
        return conditionals;
    }

    public void importFromColumn(TabularReportColumn rc)
    {
        flags = rc.getFlags();

        this.heading = rc.getHeading();
        this.headingCommand = rc.getHeadingCommand();
        this.redirectUrl = rc.getRedirect();
        this.conditionals = rc.getConditionals();
        setAlign(new AlignStyle(rc.getAlign()));
        setWidth(rc.getWidth());
        setCalc(rc.getCalcCmd());
        Format fmt = rc.getFormatter();
        if(fmt != null)
            setFormatter(fmt);
        setOutput(rc.getOutput());
    }

    public TabularReportColumnConditionalState createConditional()
    {
        return new TabularReportColumnConditionalApplyFlag();
    }

    public void addConditional(TabularReportColumnConditionalState state)
    {
        if(conditionals == null)
            conditionals = new ArrayList();
        conditionals.add(state);
    }

    public void finalizeContents(TabularReport report)
    {
    }

    public TabularReportColumnState constructState(TabularReportValueContext rc)
    {
        return new GeneralColumnState(rc);
    }

    public class GeneralColumnState implements TabularReportColumnState
    {
        protected TabularReportValueContext rc;
        protected ColumnDataCalculator calc;
        protected Flags flags;
        protected String outputFormat;

        protected GeneralColumnState(TabularReportValueContext rc)
        {
            this.rc = rc;

            String calcCmd = getCalcCmd();
            if(calcCmd != null)
            {
                calc = TabularReportCalcs.getInstance().createDataCalc(calcCmd);
                if(calc == null)
                    throw new RuntimeException("Unable to find calc '"+ calcCmd +"' in column " + getHeading());
            }

            flags = (Flags) GeneralColumn.this.getFlags().cloneFlags();

            if(! flags.flagIsSet(Flags.HIDDEN))
            {
                if(flags.flagIsSet(Flags.HAS_OUTPUT_PATTERN))
                    outputFormat = resolvePattern(GeneralColumn.this.getOutput());
            }

            if(flags.flagIsSet(Flags.HAVE_CONDITIONALS))
            {
                List conditionals = getConditionals();
                for(int i = 0; i < conditionals.size(); i++)
                    ((TabularReportColumnConditionalState) conditionals.get(i)).makeStateChanges(rc, this);
            }
        }

        public final boolean isVisible()
        {
            return ! flags.flagIsSet(Flags.HIDDEN);
        }

        public final boolean haveCalc()
        {
            return calc != null;
        }

        public final ColumnDataCalculator getCalc()
        {
            return calc;
        }

        public final TabularReportColumn.Flags getFlags()
        {
            return flags;
        }

        public final String getOutputFormat()
        {
            return outputFormat;
        }

        public String getCssStyleAttrValue()
        {
            StringBuffer style = new StringBuffer();
            switch(getAlign())
            {
                case TabularReportColumn.ALIGN_CENTER:
                    style.append("text-align: center;");
                    break;

                case TabularReportColumn.ALIGN_RIGHT:
                    style.append("text-align: right;");
                    break;
            }

            if(flags.flagIsSet(Flags.PREVENT_WORD_WRAP))
                style.append(" white-space: nowrap;");

            return style.toString();
        }

        public final void setOutputFormat(String value)
        {
            outputFormat = value;
        }
    }
}