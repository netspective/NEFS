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
 * $Id: TextReportSkin.java,v 1.6 2003-04-06 03:57:43 shahid.shah Exp $
 */

package com.netspective.commons.report.tabular;

import java.io.IOException;
import java.io.Writer;

import com.netspective.commons.command.Command;

public class TextReportSkin implements TabularReportSkin
{
    private String fileExtn;
    private String delimiter;
    private String textQualifier;

    public TextReportSkin(String fileExtn, String delimiter, String textQualifier)
    {
        this.fileExtn = fileExtn;
        this.delimiter = delimiter;
        this.textQualifier = textQualifier;
    }

    public String getFileExtension()
    {
        return fileExtn;
    }

    public String getDelimiter()
    {
        return delimiter;
    }

    public String getTextQualifier()
    {
        return textQualifier;
    }

    public String getBlankValue()
    {
        return "";
    }

    public String constructClassRef(Class cls)
    {
        return cls.getName();
    }

    public String constructRedirect(TabularReportValueContext rc, Command command, String label, String hint, String target)
    {
        return label;
    }

    public void render(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        produceHeadingRow(writer, rc, ds);
        produceDataRows(writer, rc, ds);
        produceFootRow(writer, rc);
    }

    public void produceHeadingRow(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        TabularReportColumns columns = rc.getColumns();
        TabularReportColumnState[] states = rc.getStates();
        int dataColsCount = columns.size();
        int lastDataCol = dataColsCount - 1;

        StringBuffer row = new StringBuffer();
        for(int i = 0; i < dataColsCount; i++)
        {
            if(! states[i].isVisible())
                continue;

            String colHeading = ds.getHeadingRowColumnData(i);
            if(colHeading != null)
                writeText(row, colHeading, i < lastDataCol);
            else
                writeText(row, getBlankValue(), i < lastDataCol);
        }
        writer.write(row.toString());
        writer.write("\n");
    }

    public void produceDataRows(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        TabularReport report = rc.getReport();
        TabularReportColumns columns = rc.getColumns();
        TabularReportColumnState[] states = rc.getStates();

        int dataColsCount = columns.size();
        int lastDataCol = dataColsCount - 1;
        boolean hierarchical = ds.isHierarchical();

        while(ds.next())
        {
            int hiearchyCol = 0;
            int activeLevel = 0;

            if(hierarchical)
            {
                TabularReportDataSource.Hierarchy activeHierarchy = ds.getActiveHierarchy();
                hiearchyCol = activeHierarchy.getColumn();
                activeLevel = activeHierarchy.getLevel();
            }

            StringBuffer row = new StringBuffer();
            for(int i = 0; i < dataColsCount; i++)
            {
                TabularReportColumn column = columns.getColumn(i);
                TabularReportColumnState state = states[i];

                if(! state.isVisible())
                    continue;

                String data =
                        state.getFlags().flagIsSet(TabularReportColumn.Flags.HAS_OUTPUT_PATTERN) ?
                        state.getOutputFormat() :
                        column.getFormattedData(rc, ds, TabularReportColumn.GETDATAFLAG_DO_CALC);

                if(hierarchical && (hiearchyCol == i) && activeLevel > 0)
                {
                    for(int sp = 0; sp < activeLevel; sp++)
                        data = " " + data;
                }

                writeText(row, data, i < lastDataCol);
            }
            writer.write(report.replaceOutputPatterns(rc, ds, row.toString()));
            writer.write("\n");
        }

    }

    public void produceFootRow(Writer writer, TabularReportValueContext rc) throws IOException
    {
        int calcsCount = rc.getCalcsCount();
        if(calcsCount == 0)
            return;

        TabularReportColumnState[] states = rc.getStates();
        TabularReportColumns columns = rc.getColumns();
        int dataColsCount = columns.size();
        int lastDataCol = dataColsCount - 1;

        StringBuffer row = new StringBuffer();
        for(int i = 0; i < dataColsCount; i++)
        {
            TabularReportColumn column = columns.getColumn(i);
            if(! states[i].isVisible())
                continue;

            String summary = column.getFormattedData(rc, states[i].getCalc());
            if(summary == null)
                summary = "";

            writeText(row, summary, i < lastDataCol);
        }
        writer.write(row.toString());
        writer.write("\n");
    }

    public void writeText(StringBuffer sb, String text, boolean delimiterColumn) throws IOException
    {
        if(textQualifier != null)
        {
            sb.append(textQualifier);
            if(text != null)
                sb.append(text);
            sb.append(textQualifier);
        }
        else if(text != null)
            sb.append(text);

        if(delimiterColumn)
            sb.append(delimiter);
    }
}