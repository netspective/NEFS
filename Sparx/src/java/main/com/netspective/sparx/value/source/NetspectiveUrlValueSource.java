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
 * $Id: NetspectiveUrlValueSource.java,v 1.1 2003-12-03 00:35:12 shahid.shah Exp $
 */

package com.netspective.sparx.value.source;

import java.io.File;
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

public class NetspectiveUrlValueSource extends AbstractValueSource
{
    public static final String[] IDENTIFIERS = new String[] { "netspective-url" };
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation(
            "Creates a URL specific to a particular Netspective site.",
            new ValueSourceDocumentation.Parameter[]
            {
                new ValueSourceDocumentation.Parameter("site-id", true, "The site identifier (www, sampler, sample-apps, or docs, downloads).")
            }
    );

    public static final int URITYPE_WWW = 0;
    public static final int URITYPE_SAMPLER = 1;
    public static final int URITYPE_SAMPLE_APPS = 2;
    public static final int URITYPE_DOCS = 3;
    public static final int URITYPE_DOWNLOADS = 4;

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
        type = getNetspectiveUrlType(spec.getParams());
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        return new PresentationValue(getValue(vc));
    }

    public Value getValue(ValueContext vc)
    {
        return new GenericValue(getNetspectiveUrlForId(vc, type));
    }

    public static int getNetspectiveUrlType(String typeId)
    {
        if(typeId.equals("main"))
            return URITYPE_WWW;
        else if(typeId.equals("sampler"))
            return URITYPE_SAMPLER;
        else if (typeId.equals("nefs-sample-apps"))
            return URITYPE_SAMPLE_APPS;
        else if (typeId.equals("docs"))
            return URITYPE_DOCS;
        else if (typeId.equals("downloads"))
            return URITYPE_DOWNLOADS;
        else
            return URITYPE_WWW;
    }

    public static String getNetspectiveUrlForId(ValueContext vc, int type)
    {
        final HttpServletValueContext svc = (HttpServletValueContext)
                (vc instanceof ConnectionContext ? ((ConnectionContext) vc).getDatabaseValueContext() :
                vc);

        final String wwwContextName = "www.netspective.com";

        final HttpServletRequest request = svc.getHttpRequest();
        final File contextPath = new File(svc.getServlet().getServletConfig().getServletContext().getRealPath("/"));
        final boolean isInWWW = contextPath.getAbsolutePath().endsWith(wwwContextName);

        final String wwwPublicContextUri = "http://" + wwwContextName;
        final String wwwLocalContextUri = request.getContextPath() + "/../" + wwwContextName;

        final boolean wwwIsLocal = new File(contextPath.getParentFile(), wwwContextName).exists();
        final String wwwContextUrl = isInWWW ? request.getContextPath() : (wwwIsLocal ? wwwLocalContextUri : wwwPublicContextUri);

        switch(type)
        {
            case URITYPE_WWW:
                return wwwContextUrl;

            case URITYPE_SAMPLER:
                return wwwContextUrl + "/sampler";

            case URITYPE_SAMPLE_APPS:
                return wwwContextUrl + "/sampler/sample-apps";

            case URITYPE_DOCS:
                return wwwContextUrl + "/sampler/documentation";

            case URITYPE_DOWNLOADS:
                return wwwContextUrl + "/resources/downloads";
        }

        return null;
    }

    public static String getNetspectiveUrl(ValueContext vc, String type)
    {
        return getNetspectiveUrlForId(vc, getNetspectiveUrlType(type));
    }

    public boolean hasValue(ValueContext vc)
    {
        return true;
    }
}