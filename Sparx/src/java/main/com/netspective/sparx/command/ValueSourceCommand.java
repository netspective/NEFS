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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.command.Command;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.command.CommandNotFoundException;
import com.netspective.commons.command.Commands;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;

public class ValueSourceCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(ValueSourceCommand.class);
    public static final String[] IDENTIFIERS = new String[]{"dynamic"};
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation("Wraps a dynamic command by evaluating the parameter as a value source.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("value-source", true, "The command that will be evaluated as a value source."),
            });

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private boolean isStatic;
    private ValueSource vs;
    private Command staticCommand;

    public ValueSourceCommand()
    {
    }

    public boolean isAbleToAffectNavigation()
    {
        return true;
    }

    public String getParameters()
    {
        return null;
    }

    public void setParameters(String params)
    {
        vs = ValueSources.getInstance().getValueSourceOrStatic(params);
        isStatic = vs instanceof StaticValueSource;
        if (isStatic)
            staticCommand = Commands.getInstance().getCommand(vs.getTextValue(null));
    }

    public void setParameters(StringTokenizer params)
    {
        // we're not using this method
    }

    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        if (isStatic)
            ((HttpServletCommand) staticCommand).handleCommand(writer, nc, unitTest);
        else
        {
            String commandSpec = vs.getTextValue(nc);
            if (commandSpec == null || commandSpec.length() == 0)
            {
                writer.write("Command value source '" + vs.getSpecification() + "' evaluated to empty text.");
                return;
            }

            try
            {
                HttpServletCommand command = (HttpServletCommand) Commands.getInstance().getCommand(commandSpec);
                command.handleCommand(writer, nc, false);
            }
            catch (CommandNotFoundException e)
            {
                log.error("error handling command", e);
                writer.write(e.toString());
            }
        }
    }
}
