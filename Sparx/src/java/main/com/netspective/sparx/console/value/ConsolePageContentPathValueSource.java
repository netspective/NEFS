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
package com.netspective.sparx.console.value;

import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.value.AbstractValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.BasicHtmlPanelValueContext;

public class ConsolePageContentPathValueSource extends AbstractValueSource
{
    public static final String[] IDENTIFIERS = new String[]{"console-page-content"};
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation("Provides the relative path of a Console page's content.",
                                                                                              new ValueSourceDocumentation.Parameter[]
                                                                                              {
                                                                                                  new ValueSourceDocumentation.Parameter("source", true, "The source to locate in the console page content path. If the source starts with ../ it will be assumed to be in the parent's path.")
                                                                                              });

    private boolean inParent;
    private String source;

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);
        source = spec.getParams();
        if(source.startsWith("../"))
        {
            inParent = true;
            source = source.substring(3);
        }
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        return new PresentationValue(getValue(vc));
    }

    public Value getValue(ValueContext vc)
    {
        final NavigationContext nc =
                (vc instanceof NavigationContext ? (NavigationContext) vc :
                 (
                     vc instanceof BasicHtmlPanelValueContext ? ((BasicHtmlPanelValueContext) vc).getNavigationContext() :
                     null
                 ));

        if(nc == null)
            throw new RuntimeException("No navigation context available!");

        return new AbstractValue()
        {
            public Object getValue()
            {
                return "content" + (inParent
                                    ? nc.getActivePage().getParent().getQualifiedName()
                                    : nc.getActivePage().getQualifiedName()) + "/" + source;
            }

            public String getTextValue()
            {
                return (String) getValue();
            }

            public String[] getTextValues()
            {
                return new String[]{(String) getValue()};
            }

            public List getListValue()
            {
                List list = new ArrayList();
                list.add(getValue());
                return list;
            }
        };
    }

    public boolean hasValue(ValueContext vc)
    {
        Value value = getValue(vc);
        return value.getTextValue() != null;
    }
}