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
package com.netspective.axiom.schema;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.netspective.commons.validate.ValidationContext;
import com.netspective.commons.value.Values;

public interface ColumnValues extends Values
{
    public final static int RESULTSETROWNUM_SINGLEROW = -1;

    public ColumnValue getByColumnIndex(int columnIndex);

    public ColumnValue getByColumn(Column column);

    public ColumnValues getByColumns(Columns columns);

    public ColumnValue getByName(String columnName);

    public ColumnValue getByNameOrXmlNodeName(String colXmlNodeName);

    public Object[] getValuesForSqlBindParams();

    /**
     * Copy the values from the given source values into this column object. Only the columns that match will be copied
     * and all other other values will be ignored.
     */
    void copyValuesUsingColumnNames(ColumnValues source);

    /**
     * Copy the values from the given source values into this column object. Only the columns that match will be copied
     * and all other other values will be ignored.
     */
    void copyValuesUsingColumnInstances(ColumnValues source);

    /**
     * Given a ResultSet, populate the values of the Row with the values provided in the
     * ResultSet. This method assumes that the ResultSet columns are ordered in the same
     * manner as when the table was defined and is almost always used to populate a Row
     * when the ResultSet contains all the columns that are defined for a particular table.
     * Row num is the row number being populated or RESULTSETROWNUM_SINGLEROW if there is only a single row (not part of a container).
     */
    public void populateValues(ResultSet resultSet, int rowNum) throws SQLException;

    /**
     * Use the given ColumnValuesProducer to populate the values in this object.
     */
    public void populateValues(ColumnValuesProducer cvp);

    public int size();

    /**
     * Conduct validation and return the resulting validation context.
     *
     * @param vc optional validation context (if it's null, one will be created)
     */
    public ValidationContext getValidationResult(ValidationContext vc);

    /**
     * Assign the given text array to the columns in this structure. This is most often used when a CSV or tab-delimited
     * set of text values should be assigned.
     *
     * @param textValues        The values that should be assigned (must be in same order as the column definitions)
     * @param treatBlanksAsNull What to do with blank values (treat them as nulls or assign blanks).
     */
    public void setValues(String[] textValues, boolean treatBlanksAsNull);
}
