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
 * $Id: NavigationPageTransformBodyHandler.java,v 1.3 2003-11-15 19:03:47 shahid.shah Exp $
 */

package com.netspective.sparx.navigate.handler;

import java.io.Writer;
import java.io.IOException;
import javax.servlet.ServletException;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.text.Transform;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationContext;

/**
 * Allows the navigation of a HTML website but makes it looks like it's part of the same theme (including headers, footers, etc)
 */

public class NavigationPageTransformBodyHandler extends NavigationPageBodyDefaultHandler
{
    private Transform transform = new Transform();

    public void addParam(Transform.StyleSheetParameter param)
    {
        transform.addParam(param);
    }

    public void addSystemProperty(Transform.SystemProperty param)
    {
        transform.addSystemProperty(param);
    }

    public Transform.StyleSheetParameter createParam()
    {
        return transform.createParam();
    }

    public Transform.SystemProperty createSystemProperty()
    {
        return transform.createSystemProperty();
    }

    public void setRelativeToClass(Class relativeToClass)
    {
        transform.setRelativeToClass(relativeToClass);
    }

    public void setSource(ValueSource source)
    {
        transform.setSourceResource(source);
    }

    public void setSourceFile(ValueSource source)
    {
        transform.setSourceFile(source);
    }

    public void setStyleSheet(ValueSource styleSheet)
    {
        transform.setStyleSheetResource(styleSheet);
    }

    public void setStyleSheetFile(ValueSource styleSheet)
    {
        transform.setStyleSheetFile(styleSheet);
    }

    public void handleNavigationPageBody(NavigationPage page, Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        try
        {
            transform.render(writer, nc, null, null, false);
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
}
