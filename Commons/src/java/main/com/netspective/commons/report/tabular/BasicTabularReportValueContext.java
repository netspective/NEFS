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
 * $Id: BasicTabularReportValueContext.java,v 1.3 2003-04-02 23:58:16 shahid.shah Exp $
 */

package com.netspective.commons.report.tabular;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.report.tabular.calc.ColumnDataCalculator;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.report.tabular.TabularReportContextListener;
import com.netspective.commons.report.tabular.TabularReportColumns;
import com.netspective.commons.report.tabular.TabularReportSkin;
import com.netspective.commons.report.tabular.TabularReportColumnState;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.DefaultValueContext;

public class BasicTabularReportValueContext extends DefaultValueContext implements TabularReportValueContext
{
    static public final String REQUESTATTRNAME_LISTENER = "ReportContext.DefaultListener";

    private List listeners = new ArrayList();
    private TabularReportColumnState[] states;
    private TabularReport reportDefn;
    private int calcsCount;
    private int visibleColsCount;
    private TabularReportSkin skin;
    private int rowCurrent, rowStart, rowEnd;

    public BasicTabularReportValueContext(TabularReport reportDefn, TabularReportSkin skin)
    {
        this.reportDefn = reportDefn;
        this.skin = skin;
        this.rowStart = 0;
        this.rowEnd = 0;
        this.rowCurrent = 0;
        this.visibleColsCount = -1; // calculate on first-call (could change)

        TabularReportColumns columns = reportDefn.getColumns();
        int columnsCount = columns.size();

        calcsCount = 0;
        states = new TabularReportColumnState[columnsCount];
        for(int i = 0; i < columns.size(); i++)
        {
            TabularReportColumnState state = columns.getColumn(i).constructState(this);
            if(state.haveCalc())
                calcsCount++;
            states[i] = state;
        }
    }

    public boolean isMinimized()
    {
        return false;
    }

    public List getListeners()
    {
        return listeners;
    }

    public void addListener(TabularReportContextListener listener)
    {
        listeners.add(listener);
    }

    public final TabularReport getReport()
    {
        return reportDefn;
    }

    public final TabularReportSkin getSkin()
    {
        return skin;
    }

    public final TabularReportColumnState[] getStates()
    {
        return states;
    }

    public final TabularReportColumnState getState(int col)
    {
        return states[col];
    }

    public final int getVisibleColsCount()
    {
        if(visibleColsCount != -1)
            return visibleColsCount;

        TabularReportColumns columns = reportDefn.getColumns();
        int columnsCount = columns.size();

        visibleColsCount = 0;
        for(int i = 0; i < columnsCount; i++)
        {
            if(states[i].isVisible())
                visibleColsCount++;
        }
        return visibleColsCount;
    }

    public final TabularReportColumns getColumns()
    {
        return reportDefn.getColumns();
    }

    public final ColumnDataCalculator getCalc(int col)
    {
        return states[col].getCalc();
    }

    public final int getCalcsCount()
    {
        return calcsCount;
    }

    public final boolean endOfPage()
    {
        rowCurrent++;
        return rowCurrent >= rowEnd;
    }

    public final int getRowStart()
    {
        return rowStart;
    }

    public final int getRowEnd()
    {
        return rowEnd;
    }

    public void produceReport(Writer writer, TabularReportDataSource ds) throws IOException
    {
        reportDefn.makeStateChanges(this, ds);
        skin.produceReport(writer, this, ds);
    }
}