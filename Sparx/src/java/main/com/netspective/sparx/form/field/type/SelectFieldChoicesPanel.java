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
 * $Id: SelectFieldChoicesPanel.java,v 1.4 2003-09-07 19:26:00 aye.thu Exp $
 */

package com.netspective.sparx.form.field.type;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;
import java.io.StringReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.panel.HtmlSyntaxHighlightPanel;
import com.netspective.sparx.console.panel.data.sql.QueryDetailPanel;
import com.netspective.axiom.sql.DbmsSqlTexts;
import com.netspective.axiom.sql.DbmsSqlText;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.command.Command;

public class SelectFieldChoicesPanel extends QueryDetailPanel
{
    public static final HtmlTabularReport choicesReport = new BasicHtmlTabularReport();
    public static final String CHOICE_ID_COLUMN_STRING = "ID";
    public static final String CHOICE_CAPTION_COLUMN_STRING = "Caption";

    public static final int CHOICE_ID_COLUMN_INDEX = 0;
    public static final int CHOICE_CAPTION_COLUMN_INDEX = 1;

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource(CHOICE_ID_COLUMN_STRING));
        choicesReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource(CHOICE_CAPTION_COLUMN_STRING));
        choicesReport.addColumn(column);
    }

    private TabularReportDataSource dataSource;

    public SelectFieldChoicesPanel()
    {
        getFrame().setHeading(new StaticValueSource("Select Field Choices"));
        setIdColumnCommand("redirect,javascript\\:opener.activeDialogPopup.populateControls(\"${0}\"\\, \"${1}\")");
        setCaptionColumnCommand("redirect,javascript\\:opener.activeDialogPopup.populateControls(\"${0}\"\\, \"${1}\")");
    }

    /**
     * Sets the datasource with the select choices data
     * @param ds
     */
    public void setDataSource(TabularReportDataSource ds)
    {
        dataSource = ds;
    }

    /**
     * Creates the datasource containing the data to fill the report with
     * @param nc
     * @return
     */
    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        QueryDetailPanel.SelectedQuery selectedQuery = getSelectedQuery(nc);
        if (selectedQuery == null && dataSource != null)
        {
            // There is no query associated with this
            return dataSource;
        }
        else
        {
            if(selectedQuery.getDataSource() != null)
                return selectedQuery.getDataSource();
            else
            {
                nc.setPageHeading("Static SQL: " + selectedQuery.getQuery().getQualifiedName());
                return new SqlTextDataSource(selectedQuery);
            }
        }
    }

    /**
     * Gets the associated report object
     * @param nc
     * @return
     */
    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return choicesReport;
    }

    /**
     * Gets the <i>Id</i> report column
     * @return
     */
    public  TabularReportColumn getIdReportColumn()
    {
        return choicesReport.getColumn(CHOICE_ID_COLUMN_INDEX);
    }

    /**
     * Gets the <i>Caption</i> report column
     * @return
     */
    public TabularReportColumn getCaptionReportColumn()
    {
        return choicesReport.getColumn(CHOICE_CAPTION_COLUMN_INDEX);
    }

    public Command getIdColumnCommand()
    {
        return choicesReport.getColumn(CHOICE_ID_COLUMN_INDEX).getCommand();
    }

    public Command getCaptionColumnCommand()
    {
        return choicesReport.getColumn(CHOICE_CAPTION_COLUMN_INDEX).getCommand();
    }

    /**
     * Sets the command associated with the ID column
     */
    public void setIdColumnCommand(String command)
    {
        choicesReport.getColumn(CHOICE_ID_COLUMN_INDEX).setCommand(command);
    }

    /**
     * Sets the command asociated with the caption column
     */
    public void setCaptionColumnCommand(String command)
    {
        choicesReport.getColumn(CHOICE_CAPTION_COLUMN_INDEX).setCommand(command);
    }

    /**
     *
     */
    public class SqlTextDataSource extends AbstractHtmlTabularReportDataSource
    {
        private List rows = new ArrayList();
        private int activeRow = -1;
        private int lastRow;

        public SqlTextDataSource(QueryDetailPanel.SelectedQuery selectedQuery)
        {
            super();
            DbmsSqlTexts texts = selectedQuery.getQuery().getSqlTexts();
            Set availableDbmsIds = new TreeSet(texts.getAvailableDbmsIds());

            for(Iterator i = availableDbmsIds.iterator(); i.hasNext(); )
            {
                String dbmsId = (String) i.next();
                rows.add(new Object[] { dbmsId, texts.getByDbmsId(dbmsId) });
            }

            lastRow = rows.size() - 1;
        }

        public int getActiveRowNumber()
        {
            return activeRow;
        }

        public int getTotalRows()
        {
            return rows.size();
        }

        public boolean hasMoreRows()
        {
            return activeRow < lastRow;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            activeRow = rowNum;
        }

        public boolean next()
        {
            if(! hasMoreRows())
                return false;

            setActiveRow(activeRow + 1);
            return true;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    return ((Object[]) rows.get(activeRow))[0];

                case 1:
                    DbmsSqlText sqlText = (DbmsSqlText) ((Object[]) rows.get(activeRow))[1];
                    String sql = TextUtils.getUnindentedText(sqlText.getSql(reportValueContext));
                    Reader reader = new StringReader(sql);
                    Writer writer = new StringWriter();
                    try
                    {
                        HtmlSyntaxHighlightPanel.emitHtml("sql", reader, writer);
                    }
                    catch (IOException e)
                    {
                        return e.getMessage();
                    }
                    return writer.toString();

                default:
                    return null;
            }
        }
    }
}
