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

package com.netspective.sparx.form.field.type;

import java.io.IOException;
import java.io.Writer;

import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogContextBeanMemberInfo;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldValue;
import com.netspective.sparx.form.field.DialogFields;

/**
 * Logical dialog field class to allow creation of grids.
 *
 * @version $Id: GridField.java,v 1.13 2004-09-13 13:44:11 aye.thu Exp $
 */
public class GridField extends DialogField
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private ValueSource captions;

    /**
     * Grid field's state class
     */
    public class GridFieldState extends State
    {
        public GridFieldState(DialogContext dc, DialogField field)
        {
            super(dc, field);
        }

        /**
         * Gets the value of the grid entry child
         *
         * @param row    row index
         * @param column column index
         */
        public DialogFieldValue getValue(int row, int column)
        {
            DialogFields rows = getField().getChildren();
            if(row > rows.size())
                return null;
            DialogFields rowChildren = rows.get(row).getChildren();
            DialogField field = rowChildren.get(column);
            return getDialogContext().getFieldStates().getState(field).getValue();
        }

    }

    public GridField()
    {
    }

    /**
     * Creates a new instance of the grid field's state
     */
    public DialogField.State constructStateInstance(DialogContext dc)
    {
        return new GridFieldState(dc, this);
    }

    /**
     * Gets the class to use for the state of the grid field
     */
    public Class getStateClass()
    {
        return GridFieldState.class;
    }

    public ValueSource getCaptions()
    {
        return captions;
    }

    public void setCaptions(ValueSource captions)
    {
        this.captions = captions;
    }

    public GridFieldRow createRow()
    {
        return new GridFieldRow();
    }

    /**
     * Adds a row to the grid field
     *
     * @param row grid row field
     */
    public void addRow(GridFieldRow row)
    {
        addField(row);
    }

    /**
     * Returns an array of caption names for the grid columns
     *
     * @param dc current dialog context
     */
    public String[] getCaptions(DialogContext dc)
    {
        String[] result = null;

        if(captions == null)
        {
            DialogFields rows = getChildren();
            if(rows == null)
                return null;

            DialogField firstRow = rows.get(0);
            if(firstRow == null)
                return null;

            DialogFields firstRowChildren = firstRow.getChildren();
            result = new String[firstRowChildren.size()];
            for(int i = 0; i < firstRowChildren.size(); i++)
            {
                DialogField field = firstRowChildren.get(i);
                if(field.isAvailable(dc) && !field.isInputHiddenFlagSet(dc))
                    result[i] = field.getCaption().getTextValue(dc);
            }
        }
        else
        {
            result = captions.getTextValues(dc);
        }
        return result;
    }

    public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
    {
        dc.getSkin().renderGridControlsHtml(writer, dc, this);
    }

    /**
     * Gets the grid field's entry for the generated dialog context bean
     *
     * @return DialogContextBeanMemberInfo    The bean member info contains the grid field's utility methods to access the values of the grid's children
     */
    public DialogContextBeanMemberInfo getDialogContextBeanMemberInfo()
    {
        DialogContextBeanMemberInfo mi = createDialogContextMemberInfo();
        String memberName = mi.getMemberName();
        String fieldName = mi.getFieldName();

        if(memberName == null || fieldName == null)
            return mi;

        String stateClassName = getStateClass().getName().replace('$', '.');
        mi.addJavaCode("\tpublic com.netspective.sparx.form.field.DialogFieldValue get" + mi.getMemberName() + "GridMemberFieldValue(int row, int column)\n" +
                       "\t{\n " +
                       "\t\tDialogFields rows = get" + mi.getMemberName() + "State().getField().getChildren();\n" +
                       "\t\tif (row > rows.size())\n" +
                       "\t\t\treturn null;\n" +
                       "\t\tDialogFields rowChildren = rows.get(row).getChildren();\n" +
                       "\t\tDialogField field = rowChildren.get(column);\n" +
                       "\t\treturn this.dialogContext.getFieldStates().getState(field).getValue();\n" +
                       "\t}\n");
        DialogFields rows = getChildren();
        if(rows.size() > 0)
        {
            // get the first row and use it as if it represents every row (assume that every row has the same number of columns)
            GridFieldRow row = (GridFieldRow) rows.get(0);
            DialogFields columns = row.getChildren();
            for(int i = 0; i < columns.size(); i++)
            {
                DialogField columnField = columns.get(i);
                mi.addJavaCode("\tpublic DialogFieldValue get" + mi.getMemberName() + TextUtils.getInstance().xmlTextToJavaIdentifier(columnField.getName(), true) + "ValueByRow(int rowNumber)\n" +
                               "\t{\n" +
                               "\t\t" + stateClassName + " state = get" + mi.getMemberName() + "State();\n" +
                               "\t\treturn state.getValue(rowNumber, " + i + ");\n" +
                               "\t}\n");
            }
        }
        return getChildren().getDialogContextBeanMemberInfo(mi);
    }
}