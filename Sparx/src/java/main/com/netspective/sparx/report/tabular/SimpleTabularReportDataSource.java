package com.netspective.sparx.report.tabular;

import java.util.List;
import java.util.ArrayList;

/**
 * Report datasource class for a simple multi-column row report. The {@link #addRowData(String[]) addRowData} is used to
 * supply the datasource with the data.
 *
 */
public class SimpleTabularReportDataSource extends AbstractHtmlTabularReportDataSource
{
    private List rowData = new ArrayList();
    private int activeRow = -1;

    public SimpleTabularReportDataSource()
    {
    }

    /**
     * Adds row data
     *
     * @param beanData
     */
    public void addRowData(String[] beanData)
    {
        rowData.add(beanData);
    }

    public String getHeadingRowColumnData(int columnIndex)
    {
        return super.getHeadingRowColumnData(columnIndex);
    }

    public Object getActiveRowColumnData(int columnIndex, int flags)
    {
        String[] columnData = (String[])rowData.get(activeRow);
        return columnData[columnIndex];
    }

    public int getActiveRowNumber()
    {
        return activeRow;
    }

    public boolean hasMoreRows()
    {
        if (activeRow < getTotalRows()-1)
            return true;
        else
            return false;
    }

    public boolean next()
    {
        if(!hasMoreRows())
            return false;

        setActiveRow(activeRow + 1);
        return true;
    }

    public void setActiveRow(int rowNum)
    {
        activeRow = rowNum;
    }

    public boolean isScrollable()
    {
        return false;
    }

    public int getTotalRows()
    {
        return rowData.size();
    }
}
