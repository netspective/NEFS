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
package com.netspective.sparx.template.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.command.CommandException;
import com.netspective.commons.command.Commands;
import com.netspective.sparx.command.HttpServletCommand;
import com.netspective.sparx.navigate.NavigationContext;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateTransformModel;

public class CommandTransform implements TemplateTransformModel
{
    private static final Log log = LogFactory.getLog(CommandTransform.class);

    public CommandTransform()
    {
    }

    public Writer getWriter(Writer out, Map args)
    {
        return new CommandWriter(out, args);
    }

    private class CommandWriter extends Writer
    {
        private Map args;
        private Writer out;
        private NavigationContext nc;
        private HttpServletCommand command;

        public CommandWriter(Writer out, Map args)
        {
            this.out = out;
            this.args = args;

            Environment env = Environment.getCurrentEnvironment();
            StringModel model = null;
            try
            {
                model = (StringModel) env.getDataModel().get("vc");
            }
            catch(TemplateModelException e)
            {
                log.error(e);
            }

            try
            {
                Object commandAttrValue = args.get("command");
                if(commandAttrValue != null)
                {
                    nc = (NavigationContext) model.getWrappedObject();
                    command = (HttpServletCommand) Commands.getInstance().getCommand(commandAttrValue.toString());
                }
                else
                {
                    out.write("No command provided.");
                    command = null;
                }
            }
            catch(IOException e)
            {
                log.error(e);
            }
        }

        public void write(char[] cbuf, int off, int len) throws IOException
        {
            out.write(cbuf, off, len);
        }

        public void flush() throws IOException
        {
            out.flush();
        }

        public void close() throws IOException
        {
            try
            {
                command.handleCommand(out, nc, false);
            }
            catch(CommandException e)
            {
                log.error(e);
                throw new RuntimeException(e);
            }
        }
    }
}