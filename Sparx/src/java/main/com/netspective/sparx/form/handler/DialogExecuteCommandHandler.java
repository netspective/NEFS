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
package com.netspective.sparx.form.handler;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.command.Command;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.command.Commands;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.command.HttpServletCommand;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;

public class DialogExecuteCommandHandler extends DialogExecuteDefaultHandler
{
    private static final Log log = LogFactory.getLog(DialogExecuteCommandHandler.class);
    private Command command;
    private ValueSource commandExpr;

    public DialogExecuteCommandHandler()
    {
    }

    public Command getCommand()
    {
        return command;
    }

    /**
     * Specify a particular command that will be executed when the dialog completes input and validation phases.
     */
    public void setCommand(Command command)
    {
        this.command = command;
    }

    public ValueSource getCommandExpr()
    {
        return commandExpr;
    }

    /**
     * A value source that will be evaluated and the result of the evaluation will be treated as a command
     * specification. This method allows the actual command that will be executed to be dynamic.
     */
    public void setCommandExpr(ValueSource commandExpr)
    {
        this.commandExpr = commandExpr;
    }

    public void executeDialog(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        ValueSource commandExpr = getCommandExpr();
        if(commandExpr != null)
        {
            String commandText = commandExpr.getTextValue(dc);
            if(commandText != null)
            {
                try
                {
                    HttpServletCommand httpCommand = (HttpServletCommand) Commands.getInstance().getCommand(commandText);
                    httpCommand.handleCommand(writer, dc, false);
                    return;
                }
                catch(Exception e)
                {
                    log.error("Command error in " + this.getClass().getName(), e);
                    throw new DialogExecuteException(e);
                }
            }
        }

        // if we get to here, we don't have an expression or the expression returned null so see if we have static
        // command supplied

        HttpServletCommand httpCommand = (HttpServletCommand) getCommand();
        if(httpCommand == null)
        {
            writer.write("No command provided.");
            return;
        }

        try
        {
            httpCommand.handleCommand(writer, dc, false);
        }
        catch(CommandException e)
        {
            log.error("Command error in " + this.getClass().getName(), e);
            throw new DialogExecuteException(e);
        }
    }
}
