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
package com.netspective.sparx.panel;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.command.CommandException;
import com.netspective.commons.command.Commands;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.command.CommandList;
import com.netspective.sparx.command.CommandListCommand;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.theme.Theme;

/**
 * Html panel class for handling the presentation of the command-list command
 */
public class HtmlCommandListPanel extends AbstractPanel
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(HtmlCommandListPanel.class);

    private CommandList list;

    public HtmlCommandListPanel()
    {
    }

    /**
     * Gets the command list defined for this panel
     *
     * @return
     */
    public CommandList getCommandList()
    {
        return list;
    }

    public CommandList createCommandList()
    {
        if (list == null)
            list = new CommandList();
        return list;
    }

    /**
     * Sets the command list defined for this panel
     *
     * @param list
     */
    public void setCommandList(CommandList list)
    {
        this.list = list;
    }

    /**
     * Handles the rendering of the html panel
     *
     * @param writer
     * @param nc
     * @param theme
     * @param flags
     *
     * @throws IOException
     */
    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        CommandListCommand command = (CommandListCommand) Commands.getInstance().getCommand("command-list", null);
        command.setPanel(this);
        command.setCommandList(list);
        try
        {
            command.handleCommand(writer, nc, false);
        }
        catch (CommandException e)
        {
            log.error("Failed to handle the comannd list.", e);
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * Handles the rendering of the html panel
     *
     * @param writer
     * @param dc
     * @param theme
     * @param flags
     *
     * @throws IOException
     */
    public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
    {
        CommandListCommand command = (CommandListCommand) Commands.getInstance().getCommand("command-list", null);
        command.setPanel(this);
        command.setCommandList(list);
        try
        {
            command.handleCommand(writer, dc, false);
        }
        catch (CommandException e)
        {
            log.error("Failed to handle the comannd list.", e);
            throw new NestableRuntimeException(e);
        }
    }


}
