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
 * $Id: ServletContextUriValueSource.java,v 1.2 2003-09-13 05:03:15 roque.hernandez Exp $
 */

package com.netspective.sparx.value.source;

import javax.servlet.http.HttpServletRequest;

import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.sparx.value.HttpServletValueContext;
import com.netspective.axiom.ConnectionContext;

public class ServletContextUriValueSource extends AbstractValueSource
{
    public static final String[] IDENTIFIERS = new String[] { "servlet-context-uri" };
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation(
            "Creates a URI relative to the servlet context root.",
            new ValueSourceDocumentation.Parameter[]
            {
                new ValueSourceDocumentation.Parameter("relative-path", true, "The relative path of the URI.")
            }
    );

    public final int URITYPE_ROOT = 0;
    public final int URITYPE_ACTIVE_SERVLET = 1;
    public final int URITYPE_CUSTOM_FROM_ROOT = 2;
    public final int URITYPE_CUSTOM_FROM_SERVLET = 3;
    public final int URITYPE_SERVER_ROOT = 4;

    private int type;

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
        String relativePath = spec.getParams();
        type = URITYPE_ROOT;
        if(relativePath.equals("/"))
            type = URITYPE_ROOT;
        else if(relativePath.equals("active-servlet"))
            type = URITYPE_ACTIVE_SERVLET;
        else if (relativePath.equals("server-root"))
            type = URITYPE_SERVER_ROOT;
        else
        {
            if(relativePath.startsWith("/"))
                type = URITYPE_CUSTOM_FROM_ROOT;
            else
                type = URITYPE_CUSTOM_FROM_SERVLET;
        }
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        return new PresentationValue(getValue(vc));
    }

    public Value getValue(ValueContext vc)
    {
        final HttpServletValueContext svc = (HttpServletValueContext)
                (vc instanceof ConnectionContext ? ((ConnectionContext) vc).getDatabaseValueContext() :
                vc);

        HttpServletRequest request = svc.getHttpRequest();
        String contextPath = request.getContextPath();
        switch(type)
        {
            case URITYPE_ROOT:
                return new GenericValue(contextPath);

            case URITYPE_ACTIVE_SERVLET:
                return new GenericValue(contextPath + request.getServletPath());

            case URITYPE_CUSTOM_FROM_ROOT:
                return new GenericValue(contextPath + spec.getParams());

            case URITYPE_CUSTOM_FROM_SERVLET:
                return new GenericValue(contextPath + request.getServletPath() + spec.getParams());

            case URITYPE_SERVER_ROOT:
                {
                    String reqURL = request.getRequestURL().toString();
                    String reqURI = request.getRequestURI();
                    return new GenericValue(reqURL.substring(0, reqURL.length() - reqURI.length()));
                }
        }

        return null;
    }

    public boolean hasValue(ValueContext vc)
    {
        return true;
    }
}