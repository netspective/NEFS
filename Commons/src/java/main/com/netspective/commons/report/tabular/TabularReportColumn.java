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
 * $Id: TabularReportColumn.java,v 1.2 2003-03-27 22:22:20 shahid.shah Exp $
 */

package com.netspective.commons.report.tabular;

import java.text.Format;
import java.util.List;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.report.tabular.calc.ColumnDataCalculator;
import com.netspective.commons.report.tabular.TabularReport;

public interface TabularReportColumn
{
    static public final long COLFLAG_HIDDEN = 1;
    static public final long COLFLAG_HASPLACEHOLDERS = COLFLAG_HIDDEN * 2;
    static public final long COLFLAG_HASOUTPUTPATTERN = COLFLAG_HASPLACEHOLDERS * 2;
    static public final long COLFLAG_WRAPURL = COLFLAG_HASOUTPUTPATTERN * 2;
    static public final long COLFLAG_HAVEANCHORATTRS = COLFLAG_WRAPURL * 2;
    static public final long COLFLAG_HAVECONDITIONALS = COLFLAG_HAVEANCHORATTRS * 2;
    static public final long COLFLAG_NOWORDBREAKS = COLFLAG_HAVECONDITIONALS * 2;
    static public final long COLFLAG_SORT_ALLOWED = COLFLAG_NOWORDBREAKS * 2;
    static public final long COLFLAG_SORTED_ASCENDING = COLFLAG_SORT_ALLOWED * 2;
    static public final long COLFLAG_SORTED_DESCENDING = COLFLAG_SORTED_ASCENDING * 2;
    static public final long COLFLAG_CUSTOMSTART = COLFLAG_SORTED_DESCENDING * 2;

    static public final int ALIGN_LEFT = 0;
    static public final int ALIGN_CENTER = 1;
    static public final int ALIGN_RIGHT = 2;

    static public final String PLACEHOLDER_COLDATA = "${.}";
    static public final String PLACEHOLDER_OPEN = "${";
    static public final String PLACEHOLDER_CLOSE = "}";

    static public class AlignStyle extends XdmEnumeratedAttribute
    {
        public AlignStyle()
        {
        }

        public AlignStyle(int valueIndex)
        {
            super(valueIndex);
        }

        public String[] getValues()
        {
            return new String[] { "left", "center", "right" };
        }
    }

    public TabularReportColumnState constructState(TabularReportValueContext rc);

    public int getDataType();

    public void setDataType(int value);

    public int getColIndex();

    public void setColIndex(int value);

    public ValueSource getHeading();

    public void setHeading(ValueSource value);

    public ValueSource getHeadingAnchorAttrs();

    public void setHeadingAnchorAttrs(ValueSource value);

    public ValueSource getUrl();

    public void setUrl(ValueSource value);

    public ValueSource getUrlAnchorAttrs();

    public void setUrlAnchorAttrs(ValueSource value);

    public int getWidth();

    public void setWidth(int value);

    public int getAlign();

    public void setAlign(AlignStyle value);

    public long getFlags();

    public boolean flagIsSet(long flag);

    public void setFlag(long flag);

    public void clearFlag(long flag);

    public void updateFlag(long flag, boolean set);

    public String getCalcCmd();

    public void setCalc(String value);

    public Format getFormatter();

    public void setFormatter(Format value);

    public void setFormat(String value);

    public String getOutput();

    public void setOutput(String value);

    public String resolvePattern(String srcStr);

    public void setWordWrap(boolean wrap);

    public String getBreak();

    public void setBreak(String header);

    public String getFormattedData(TabularReportValueContext rc, TabularReportDataSource ds, boolean doCalc);

    public String getFormattedData(TabularReportValueContext rc, ColumnDataCalculator calc);

    public List getConditionals();

    public void importFromColumn(TabularReportColumn rc);

    public void finalizeContents(TabularReport report);
}