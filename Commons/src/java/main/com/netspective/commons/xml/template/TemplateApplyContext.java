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
package com.netspective.commons.xml.template;

import java.util.Map;
import java.util.Stack;

import org.apache.commons.jexl.JexlContext;

import com.netspective.commons.value.DefaultValueContext;

public class TemplateApplyContext extends DefaultValueContext
{
    public class StackEntry
    {
        protected TemplateElement activeTemplate;
        protected boolean replacementExprsFoundInAttrs;

        protected StackEntry(TemplateElement activeTemplate, boolean replacementExprsFoundInAttrs)
        {
            this.activeTemplate = activeTemplate;
            this.replacementExprsFoundInAttrs = replacementExprsFoundInAttrs;
        }

        public TemplateElement getActiveTemplate()
        {
            return activeTemplate;
        }

        public boolean isReplacementExprsFoundInAttrs()
        {
            return replacementExprsFoundInAttrs;
        }
    }

    private Map templateParamValues;
    private Stack templatesStack = new Stack();
    private TemplateContentHandler contentHandler;
    private JexlContext exprContext;
    private boolean allowReplaceExpressions;

    public TemplateApplyContext(TemplateContentHandler contentHandler)
    {
        this.contentHandler = contentHandler;
        this.allowReplaceExpressions = false;
    }

    public TemplateApplyContext(TemplateContentHandler contentHandler, JexlContext exprContext, Map templateParamValues)
    {
        this(contentHandler);
        this.exprContext = exprContext;
        this.allowReplaceExpressions = true;
        this.templateParamValues = templateParamValues;
    }

    public TemplateContentHandler getContentHandler()
    {
        return contentHandler;
    }

    public StackEntry getActiveEntry()
    {
        return templatesStack.isEmpty() ? null : (StackEntry) templatesStack.peek();
    }

    public void pushActiveTemplate(TemplateElement activeTemplate, boolean replacementExprsFoundInAttrs)
    {
        templatesStack.push(new StackEntry(activeTemplate, replacementExprsFoundInAttrs));
    }

    public void popActiveTemplate()
    {
        templatesStack.pop();
    }

    public JexlContext getExprContext()
    {
        return exprContext;
    }

    public Map getTemplateParamValues()
    {
        return templateParamValues;
    }

    public boolean isAllowReplaceExpressions()
    {
        return allowReplaceExpressions;
    }
}

