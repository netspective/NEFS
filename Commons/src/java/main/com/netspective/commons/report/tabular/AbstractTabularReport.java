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
 * $Id: AbstractTabularReport.java,v 1.5 2003-08-01 14:58:50 aye.thu Exp $
 */

package com.netspective.commons.report.tabular;

import java.net.URLEncoder;
import java.util.List;

import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportColumns;
import com.netspective.commons.report.tabular.TabularReportContextListener;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;

public class AbstractTabularReport implements TabularReport, XmlDataModelSchema.ConstructionFinalizeListener
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] { "flag" });
    }

    private String name;
    private TabularReportColumns columns = new TabularReportColumns();
    private Flags flags = new Flags();

    public AbstractTabularReport()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public TabularReportColumns getColumns()
    {
        return columns;
    }

    public TabularReport.Flags createFlags()
    {
        return new Flags();
    }

    public TabularReportColumn getColumn(int i)
    {
        return columns.getColumn(i);
    }

    public TabularReport.Flags getFlags()
    {
        return flags;
    }

    public void setFlags(TabularReport.Flags flags)
    {
        this.flags.copy(flags);
    }

    public void finalizeContents()
    {
        for(int c = 0; c < columns.size(); c++)
        {
            TabularReportColumn colDefn = columns.getColumn(c);
            colDefn.finalizeContents(this);

            if(colDefn.getFlags().flagIsSet(TabularReportColumn.Flags.HAS_PLACEHOLDERS))
                flags.setFlag(Flags.HAS_PLACE_HOLDERS);
        }
    }

    public TabularReportColumn createColumn()
    {
        return new GeneralColumn();
    }

    public void addColumn(TabularReportColumn column)
    {
        column.setColIndex(columns.size());
        columns.add(column);
    }

    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        // if a record add caption/url was provided but no banner was created, go ahead and generate a banner automatically
        // banners are automatically hidden by record-viewer and shown by record-editor skins
        //TODO: fix this
/*
        if(frame != null && banner == null)
        {
            ValueSource recordAddCaption = frame.getRecordAddCaption();
            ValueSource recordAddUrl = frame.getRecordAddUrl();
            if(recordAddCaption != null && recordAddUrl != null)
            {
                banner = new TabularReportBanner();
                banner.addItem(new ReportBanner.Item(recordAddCaption, recordAddUrl, ValueSources.getInstance().getValueSource("config-expr:${sparx.shared.images-url}/design/action-edit-add.gif", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION)));
            }
        }
*/

        finalizeContents();
    }

    /**
     * Replace contents from rowData using the String row as a template. Each
     * occurrence of ${#} will be replaced with rowNum and occurrences of ${x}
     * where x is a number between 0 and rowData.length will be replaced with
     * the contents of rowData[x]. NOTE: this function needs to be
     * improved from both an elegance and performance perspective.
     */

    public String replaceOutputPatterns(TabularReportValueContext rc, TabularReportDataSource ds, String row)
    {
        StringBuffer sb = new StringBuffer();
        int prev = 0;
        boolean encode = false;

        int pos = 0;
        int pos1 = row.indexOf("$", prev);
        int pos2 = row.indexOf("%", prev);
        if(pos2 != -1)
        {
            if(pos1 != -1 && pos2 > pos1)
            {
                pos = pos1;
            }
            else
            {
                encode = true;
                pos = pos2;
            }
        }
        else
        {
            encode = false;
            pos = pos1;
        }

        while(pos >= 0)
        {
            if(pos > 0)
            {
                // append the substring before the '$' or '%' character
                sb.append(row.substring(prev, pos));
            }
            if(pos == (row.length() - 1))
            {
                if(encode)
                    sb.append('%');
                else
                    sb.append('$');
                prev = pos + 1;
            }
            else if(row.charAt(pos + 1) != '{')
            {
                sb.append(row.charAt(pos));
                sb.append(row.charAt(pos + 1));
                prev = pos + 2;
            }
            else
            {
                int endName = row.indexOf('}', pos);
                if(endName < 0)
                {
                    throw new RuntimeException("Syntax error in: " + row);
                }
                String expression = row.substring(pos + 2, endName);

                if(expression.equals("#"))
                    sb.append(ds.getActiveRowNumber());
                else
                {
                    try
                    {
                        int colIndex = Integer.parseInt(expression);
                        if(encode)
                            sb.append(URLEncoder.encode(columns.getColumn(colIndex).getFormattedData(rc, ds, TabularReportColumn.GETDATAFLAG_FOR_URL)));
                        else
                            sb.append(columns.getColumn(colIndex).getFormattedData(rc, ds, TabularReportColumn.GETDATAFLAGS_DEFAULT));
                    }
                    catch(NumberFormatException e)
                    {
                        ValueSource vs = ValueSources.getInstance().getValueSource(expression, ValueSources.VSNOTFOUNDHANDLER_NULL);
                        if(vs == null)
                            sb.append("Invalid: '" + expression + "'");
                        else
                            sb.append(vs.getValue(rc));
                    }
                }

                prev = endName + 1;
            }

            pos1 = row.indexOf("$", prev);
            pos2 = row.indexOf("%", prev);
            if(pos2 != -1)
            {
                if(pos1 != -1 && pos2 > pos1)
                {
                    pos = pos1;
                }
                else
                {
                    encode = true;
                    pos = pos2;
                }
            }
            else
            {
                encode = false;
                pos = pos1;
            }
        }

        if(prev < row.length()) sb.append(row.substring(prev));
        return sb.toString();
    }

    public void makeStateChanges(TabularReportValueContext rc, TabularReportDataSource ds)
    {
        List listeners = rc.getListeners();
        for(int i = 0; i < listeners.size(); i++)
            ((TabularReportContextListener) listeners.get(i)).makeReportStateChanges(rc, ds);
    }
}