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
package com.netspective.sparx.navigate;

import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;

/**
 * Class for defining conditional actions for a navigation path item.
 */
public class NavigationConditionalAction implements TemplateConsumer
{
    public static final String ATTRNAME_TYPE = "action";
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[]{"name"};

    private static ConditionalTypeTemplateConsumerDefn conditionalActionConsumer = new ConditionalTypeTemplateConsumerDefn();
    private NavigationPath path;
    private List conditionalActions = new ArrayList();
    private String name;

    static
    {
        TemplateCatalog.registerConsumerDefnForClass(conditionalActionConsumer, NavigationConditionalAction.class, true, true);
    }

    protected static class ConditionalTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public ConditionalTypeTemplateConsumerDefn()
        {
            super(null, ATTRNAME_TYPE, ATTRNAMES_SET_BEFORE_CONSUMING);
        }

        public String getNameSpaceId()
        {
            return NavigationConditionalAction.class.getName();
        }
    }

    public NavigationConditionalAction(NavigationPath path)
    {
        this.path = path;
    }

    /**
     * Gets the navigation path item the conditional belongs to
     */
    public NavigationPath getPath()
    {
        return path;
    }

    /**
     * Sets the navigation path item the conditional belongs to
     */
    public void setPath(NavigationPath path)
    {
        this.path = path;
    }

    /**
     * @return
     */
    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        return conditionalActionConsumer;
    }

    /**
     * @param template
     */
    public void registerTemplateConsumption(Template template)
    {
        conditionalActions.add(template.getTemplateName());
    }

    public String getName()
    {
        return name;
    }

    /**
     * Name of the conditional action for a navigation path item.
     *
     * @param name the name of the conditional action
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isReferencingPermission(String permissionId)
    {
        return false;
    }

    public void execute(NavigationContext nc)
    {

    }
}
