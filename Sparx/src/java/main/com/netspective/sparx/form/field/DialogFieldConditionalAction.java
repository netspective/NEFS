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
package com.netspective.sparx.form.field;

import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.sparx.form.DialogContext;

/**
 * Class for defining conditional actions for dialog fields.
 */
public class DialogFieldConditionalAction implements TemplateConsumer
{
    private DialogField sourceField = null;
    private DialogField partnerField = null;
    private String partnerFieldName = null;
    private String name;

    public static final String ATTRNAME_TYPE = "action";
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[]{"name"};
    private static ConditionalTypeTemplateConsumerDefn conditionalActionConsumer = new ConditionalTypeTemplateConsumerDefn();
    private List conditionalActions = new ArrayList();

    static
    {
        TemplateCatalog.registerConsumerDefnForClass(conditionalActionConsumer, DialogFieldConditionalAction.class, true, true);
    }

    protected static class ConditionalTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public ConditionalTypeTemplateConsumerDefn()
        {
            super(null, ATTRNAME_TYPE, ATTRNAMES_SET_BEFORE_CONSUMING);
        }

        public String getNameSpaceId()
        {
            return DialogFieldConditionalAction.class.getName();
        }
    }

    /**
     * Empty constructor
     */
    public DialogFieldConditionalAction()
    {
    }

    /**
     * Construct a <code>DialogFieldConditionalAction</code> object
     */
    public DialogFieldConditionalAction(DialogField sourceField)
    {
        setSourceField(sourceField);
    }

    /**
     * Construct a <code>DialogFieldConditionalAction</code> object whose source dialog
     * field is related to another dialog field
     *
     * @param sourceField parent dialog field
     * @param partnerName name of the parter dialog field
     */
    public DialogFieldConditionalAction(DialogField sourceField, String partnerName)
    {
        setSourceField(sourceField);
        setPartnerFieldName(partnerName);
    }

    /**
     * Gets the template cosumer object
     */
    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        return conditionalActionConsumer;
    }

    /**
     * Registers a template for consumption
     */
    public void registerTemplateConsumption(Template template)
    {
        conditionalActions.add(template.getTemplateName());
    }

    /**
     * Indicates whether or not a parter field is required for the dialog field condition
     */
    public boolean isPartnerRequired()
    {
        return true;
    }

    public boolean isValid(DialogContext dc)
    {
        if(isPartnerRequired())
        {
            if(partnerFieldName == null || partnerFieldName.length() == 0)
            {
                sourceField.invalidate(dc, "Conditional " + this + " has no associated 'partner' (field).");
                partnerFieldName = null;
                return false;
            }
        }

        return true;
    }

    public DialogField getSourceField()
    {
        return sourceField;
    }

    public void setSourceField(DialogField value)
    {
        sourceField = value;
    }

    public String getPartnerFieldName()
    {
        return partnerFieldName;
    }

    /**
     * Sets the name of the partner field on which the source dialog field is dependent.
     *
     * @param value name of the partner field
     */
    public void setPartnerFieldName(String value)
    {
        partnerFieldName = value;
    }

    public DialogField getPartnerField()
    {
        return partnerField;
    }

    public void setPartnerField(DialogField value)
    {
        partnerField = value;
        if(partnerField != null)
            partnerField.getDependentConditions().addAction(this);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}