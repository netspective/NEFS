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
 * $Id: Value.java,v 1.5 2003-08-19 04:59:30 aye.thu Exp $
 */

package com.netspective.commons.value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.netspective.commons.value.exception.ValueException;
import org.w3c.dom.Element;

public interface Value
{
    public static final int VALUELISTTYPE_NONE        = 0;
    public static final int VALUELISTTYPE_STRINGARRAY = 1;
    public static final int VALUELISTTYPE_LIST        = 2;

    /**
     * Return the class name of the actual value holder (Object, String, Integer, etc). All the values are variant, but
     * this method is used by value consumers and producers to potentially render type-safe values and validate the
     * the values are properly assigned the appropriate classes at runtime.
     */
    public Class getValueHolderClass();

    /**
     * Return the class name of the SQL bind parameter value holder. This method is used by value consumers and
     * producers to potentially render type-safe values and validate the the SQL bind parameter values are properly
     * assigned the appropriate classes at runtime.
     */
    public Class getBindParamValueHolderClass();

    /**
     * Ascertains whether the instance contains data at all
     */
    public boolean hasValue();

    /**
     * Ascertains whether the instance contains a list of values or a single value
     */
    public boolean isListValue();

    /**
     * Returns one of VALUELISTTYPE_* constants to indicate the kind of list available in this value.
     */
    public int getListValueType();

    /**
     * Returns the value that is used by the application for processing.
     */
    public Object getValue();

    /**
     * Returns the value that can be used to bind a SQL parameter; if isMulti() is true, then the getValue method
     * returns the result of RowDataProvider.translateMultiValue(this).
     */
    public Object getValueForSqlBindParam();

    /**
     * Returns the value as text.
     */
    public String getTextValue();

    /**
     * Returns the value as an integer.
     */
    public int getIntValue();

    /**
     * Returns the value as an doube.
     */
    public double getDoubleValue();

    /**
     * Returns the value as text value or a blank string if the value is null.
     */
    public String getTextValueOrBlank();

    /**
     * Returns the value as text value or a blank string if the value is null.
     */
    public String getTextValueOrDefault(String defaultText);

    /**
     * Sets the value that is used by the application for processing.
     */
    public void setValue(Object value) throws ValueException;

    /**
     * Copy the given Value by reference
     * @param value
     * @throws ValueException
     */
    public void copyValueByReference(Value value) throws ValueException;

    public void setValue(String[] value);

    public void setValue(List value);

    /**
     * Sets the value from a result set
     */
    public void setValueFromSqlResultSet(ResultSet rs, int rowNum, int colIndex) throws SQLException, ValueException;

    /**
     * Sets the value that is used by the application for processing.
     */
    public void setTextValue(String value) throws ValueException;

    /**
     * Append the given text to this value.
     */
    public void appendText(String text) throws ValueException;

    /**
     * Get the contents of this value as a text array.
     */
    public String[] getTextValues();

    /**
     * Get the contents of this value as a list
     */
    public List getListValue();

    public void importFromXml(Element parent);

    public void exportToXml(Element parent);
}
