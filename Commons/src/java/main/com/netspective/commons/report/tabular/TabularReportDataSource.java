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
 * $Id: TabularReportDataSource.java,v 1.9 2003-09-14 17:01:16 shahid.shah Exp $
 */

package com.netspective.commons.report.tabular;

import com.netspective.commons.value.ValueSource;

public interface TabularReportDataSource
{
    public static final int TOTAL_ROWS_UNKNOWN = -1;

    /**
     * The type of data returned by getActiveRowColumnData() when structural information is available.
     */
    public interface Hierarchy
    {
        /**
         * Return the column number of the column that should demonstrate the hiearchy
         */
        public int getColumn();

        /**
         * Return the current row's level
         */
        public int getLevel();

        /**
         * Return the current row's parent row
         */
        public int getParentRow();
    }

    /**
     * Create a scroll state that will manage the paging of this data source
     */
    public TabularReportDataSourceScrollState createScrollState(String identifier);

    /**
     * Set the data source to automatically close after a set amount of time
     * @param autoCloseInactivityDuration Number of milliseconds of inactivity that should retire this datasource
     */
    public void setAutoClose(long autoCloseInactivityDuration);

    /**
     * Close the data source and release all allocated resources immediately.
     */
    public void close();

    /**
     * Determines whether the data source is already closed
     * @return True if close() was already called, false otherwise
     */
    public boolean isClosed();

    /**
     * Cycle to next row in the data source -- this will be called even for the first row (the first call should
     * "initialize" the data source).
     */
    public boolean next();

    /**
     * Ascertain whether or not there are additional rows available
     * @return Return true if next() will return a valid row or false if the end of the data source has been reached.
     */
    public boolean hasMoreRows();

    /**
     * Retrieve the active row number (1-based).
     */
    public int getActiveRowNumber();

    /**
     * Retrieve data for the heading row's columns based on a column index.
     * @param columnIndex The column we're interested in (0-based)
     * @return The column's heading as text
     */
    public String getHeadingRowColumnData(int columnIndex);

    /**
     * Retrieve data for one of the current row's columns based on a column index.
     * @param columnIndex The column we're interested in (0-based)
     * @return The raw data the report can use to put into the report
     */
    public Object getActiveRowColumnData(int columnIndex, int flags);

    /**
     * Retrieve data for one of the current row's columns based on a column name (may throw an exception if not
     * supported).
     * @param columnName The name of the column we're interested in
     * @return The raw data the report can use to put into the report
     */
    public Object getActiveRowColumnData(String columnName, int flags);

    /**
     * Return true if this data source has some structure
     * @return
     */
    public boolean isHierarchical();

    /**
     * Return the active hiearchy
     */
    public Hierarchy getActiveHierarchy();

    /**
     * Return true if the active row is currently "selected" (the exact meaning is important to the skin, not the data)
     */
    public boolean isActiveRowSelected();

    /**
     * Returns the value source that provides the "no data provided" message in case the data source is empty. If this
     * method returns null, no message is rendered.
     */
    public ValueSource getNoDataFoundMessage();

    /**
     * Ascertain whether or not the data source is active
     * @param timeOut The number of milliseconds to consider as a valid
     * @return  True if the time since this data source was last used is less than the timeOut otherwise return false
     */
    public boolean isActive(long timeOut);

    /**
     * Ascertain whether or not the data source is considered "scrollable" (allows paging of results/data)
     * @return True if data may be scrolled forward/backward or false if the data source is unidirectional (forward only)
     */
    public boolean isScrollable();

    /**
     * Move to the selected row.
     * @param rowNum The 0-based row number that should become the active row.
     */
    public void setActiveRow(int rowNum);

    /**
     * Get the total number of rows in the data source (assuming the data source is scrollable)
     * @return TOTAL_ROWS_UNKNOWN if there is no way to know the total rows, otherwise the number of rows in the data source
     */
    public int getTotalRows();

    public TabularReportValueContext getReportValueContext();
    public void setReportValueContext(TabularReportValueContext vc);
}
