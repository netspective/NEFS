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
 * $Id: PersonTableRow.java,v 1.1 2003-10-13 05:51:19 aye.thu Exp $
 */

package app.cts.schema;

import com.netspective.axiom.schema.table.BasicRow;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.ConnectionContext;

public class PersonTableRow extends BasicRow
{
    /* column numbers for the columns we're going to change in our trigger */
    static private final int COMPLETE_NAME = 12;
    static private final int COMPLETE_SORTABLE_NAME = 14;
    static private final int NAME_FIRST = 6;
    static private final int NAME_LAST = 8;
    static private final int NAME_MIDDLE = 7;
    static private final int NAME_PREFIX_ID = 4;
    static private final int NAME_SUFFIX = 9;
    static private final int SHORT_NAME = 10;
    static private final int SHORT_SORTABLE_NAME = 13;
    static private final int SIMPLE_NAME = 11;
    public PersonTableRow(Table table)
    {
        super(table);
    }

    public void populateNames(ColumnValues values)
    {
        try
        {
            ColumnValue nameFirstValue = values.getByColumnIndex(NAME_FIRST);
            ColumnValue nameLastValue = values.getByColumnIndex(NAME_LAST);
            ColumnValue nameMiddleValue = values.getByColumnIndex(NAME_MIDDLE);
            ColumnValue nameSuffixValue = values.getByColumnIndex(NAME_SUFFIX);
            ColumnValue namePrefixIdValue = values.getByColumnIndex(NAME_PREFIX_ID);

            String nameFirst = nameFirstValue.getTextValue();
            String nameLast = nameLastValue.getTextValue();

            if(nameFirst == null || nameLast == null)
                throw new RuntimeException("Both first and last name are required.");

            String nameMiddle = nameMiddleValue.getTextValue();
            String nameSuffix = nameSuffixValue.getTextValue();
            EnumerationTableRow namePrefixEnum = namePrefixIdValue.getReferencedEnumRow();

            String shortName = nameFirst.charAt(0) + " " + nameLast;
            String simpleName = nameFirst + " " + nameLast;

            StringBuffer sb = new StringBuffer();
            if(namePrefixEnum != null)
            {
                sb.append(namePrefixEnum.getCaption());
                sb.append(" ");
            }
            sb.append(nameFirst);
            sb.append(" ");
            if(nameMiddle != null)
            {
                sb.append(nameMiddle);
                sb.append(" ");
            }
            sb.append(nameLast);
            if(nameSuffix != null)
                sb.append(nameSuffix);
            String completeName = sb.toString();

            String shortSortableName = nameLast + ", " + nameFirst.charAt(0);

            sb = new StringBuffer();
            sb.append(nameLast);
            sb.append(", ");
            sb.append(nameFirst);
            if(nameMiddle != null && nameMiddle.length() > 0)
            {
                sb.append(" ");
                sb.append(nameMiddle.charAt(0));
            }
            String completeSortableName = sb.toString();

            values.getByColumnIndex(SHORT_NAME).setTextValue(shortName);
            values.getByColumnIndex(SIMPLE_NAME).setTextValue(simpleName);
            values.getByColumnIndex(COMPLETE_NAME).setTextValue(completeName);
            values.getByColumnIndex(SHORT_SORTABLE_NAME).setTextValue(shortSortableName);
            values.getByColumnIndex(COMPLETE_SORTABLE_NAME).setTextValue(completeSortableName);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    public void beforeInsert(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
        super.beforeInsert(cc, flags, columnValues);
        populateNames(columnValues);
    }

    public void beforeUpdate(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
        super.beforeUpdate(cc, flags, columnValues);
        populateNames(columnValues);
    }
}
