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
 * $Id: NodeIdentifiers.java,v 1.2 2003-03-29 13:00:26 shahid.shah Exp $
 */

package com.netspective.commons.xml;

import com.netspective.commons.xml.template.TemplateProducer;

public final class NodeIdentifiers
{
    public static final String GENERIC_TEMPLATES_NAMESPACE_ID = "/generic";
    public static final String ATTRNAME_GENERIC_TEMPLATE_NAME = "name";
    public static final String ATTRNAME_GENERIC_TEMPLATE_EXTENDS = "extends";

    public static final String ATTRNAME_INCLUDE_FILE = "file";
    public static final String ATTRNAME_INCLUDE_RESOURCE = "resource";
    public static final String ATTRNAME_INCLUDE_TEMPLATE = "template";
    public static final String ATTRNAME_INCLUDE_RESOURCE_RELATIVE_TO = "relative-to";

    public static final String ATTRNAME_ALTERNATE_CLASS_NAME = "class";
    public static final String ATTRNAMEPREFIX_NAMESPACE_BINDING = "xmlns:";

    public static final String ATTRNAME_TEMPLATE_PARAM_DECL_NAME = "name";
    public static final String ATTRNAME_TEMPLATE_PARAM_DECL_DEFAULT = "default";
    public static final String ATTRNAME_TEMPLATE_PARAM_DECL_REQUIRED = "required";
    public static final String ATTRNAME_TEMPLATE_PARAM_DECL_COPYATTR = "copy-attribute";

    private String xmlNameSpace;
    private String xmlNameSpacePrefix;
    private String templateElementName;
    private String templateParamAttrPrefix;
    private String templateParamDecl;
    private String includeElementName;
    private String containerElementName;
    private String templateReplaceExprsAttrName;
    private TemplateProducer genericTemplateProducer;

    public NodeIdentifiers(String nameSpace)
    {
        xmlNameSpace = nameSpace;
        xmlNameSpacePrefix = nameSpace + ":";
        templateElementName = xmlNameSpacePrefix + "template";
        containerElementName = xmlNameSpacePrefix + "container";
        includeElementName = xmlNameSpacePrefix + "include";
        templateParamAttrPrefix = xmlNameSpacePrefix + "param-";
        templateParamDecl = xmlNameSpacePrefix + "template-param";
        templateReplaceExprsAttrName = xmlNameSpacePrefix + "replace-template-expressions";

        genericTemplateProducer = new TemplateProducer(GENERIC_TEMPLATES_NAMESPACE_ID, templateElementName, ATTRNAME_GENERIC_TEMPLATE_NAME, ATTRNAME_GENERIC_TEMPLATE_EXTENDS, true, false);
    }

    public final String getXmlNameSpace()
    {
        return xmlNameSpace;
    }

    public final String getXmlNameSpacePrefix()
    {
        return xmlNameSpacePrefix;
    }

    public final String getContainerElementName()
    {
        return containerElementName;
    }

    public final String getIncludeElementName()
    {
        return includeElementName;
    }

    public final String getTemplateElementName()
    {
        return templateElementName;
    }

    public final String getTemplateParamAttrPrefix()
    {
        return templateParamAttrPrefix;
    }

    public final String getTemplateParamDecl()
    {
        return templateParamDecl;
    }

    public final TemplateProducer getGenericTemplateProducer()
    {
        return genericTemplateProducer;
    }

    public final String getTemplateReplaceExprsAttrName()
    {
        return templateReplaceExprsAttrName;
    }
}

