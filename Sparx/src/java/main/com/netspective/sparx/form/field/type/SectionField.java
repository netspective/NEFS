/*
 * Created by IntelliJ IDEA.
 * User: aye
 * Date: Jan 27, 2004
 * Time: 6:31:35 PM
 */
package com.netspective.sparx.form.field.type;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.DialogContextBeanMemberInfo;
import com.netspective.sparx.form.field.DialogField;

/**
 *
 *
 * <pre>
 *
 * <field type="section" style="two-column" caption="Section Heading">
 *      <field type="text" .../>
 *      <field type="select" ../>
 * </field>
 *
 * </pre>
 */
public class SectionField extends DialogField
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    private int displayCols = 1;

    /**
     * Default sole constructor
     */
    public SectionField()
    {
    }

    /**
     * Gets the dialog context bean information of all the children contained in this field
     *
     * @return
     */
    public DialogContextBeanMemberInfo getDialogContextBeanMemberInfo()
    {
        return getChildren().getDialogContextBeanMemberInfo(createDialogContextMemberInfo());
    }

    public int getDisplayColumns()
    {
        return displayCols;
    }

    /**
     * Sets the number of columns for the layout  
     *
     * @param displayCols
     */
    public void setDisplayColumns(int displayCols)
    {
        this.displayCols = displayCols;
    }

}