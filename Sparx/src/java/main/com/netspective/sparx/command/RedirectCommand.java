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
package com.netspective.sparx.command;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;

public class RedirectCommand extends AbstractHttpServletCommand
{
    public static final String[] IDENTIFIERS = new String[]{"redirect"};
    public static final String IS_NAV_ID_FLAG = "IS_NAV_ID";
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation("Redirects the current page to another page via an URL.",
                                                                                      new CommandDocumentation.Parameter[]
                                                                                      {
                                                                                          new CommandDocumentation.Parameter("location", true, "The URL or navigation id."),
                                                                                          new CommandDocumentation.Parameter("flags", false, "Set to '" + IS_NAV_ID_FLAG + "' if the location is a Sparx navigation id.")
                                                                                      });

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private ValueSource location;
    private boolean isNavId;

    public boolean isAbleToAffectNavigation()
    {
        return true;
    }

    public String getParameters()
    {
        String result = location instanceof StaticValueSource ?
                        location.getTextValue(null) :
                        location.getSpecification().getSpecificationText();

        if(isNavId)
            return result + getParametersDelimiter() + IS_NAV_ID_FLAG;
        else
            return result;
    }

    public void setParameters(StringTokenizer params)
    {
        if(!params.hasMoreTokens())
            throw new RuntimeException("Expected location");

        location = ValueSources.getInstance().getValueSourceOrStatic(params.nextToken());

        if(params.hasMoreTokens())
            isNavId = params.nextToken().equals(IS_NAV_ID_FLAG);
    }

    public boolean isNavId()
    {
        return isNavId;
    }

    public void setNavId(boolean navId)
    {
        isNavId = navId;
    }

    public ValueSource getLocation()
    {
        return location;
    }

    public void setLocation(ValueSource location)
    {
        this.location = location;
    }

    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        writer.write("Redirect to " + getLocation() + " not implemented yet.");
    }
}
