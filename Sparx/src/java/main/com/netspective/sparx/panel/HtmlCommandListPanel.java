package com.netspective.sparx.panel;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.command.CommandList;
import com.netspective.sparx.command.CommandListCommand;
import com.netspective.commons.command.Commands;
import com.netspective.commons.command.CommandException;

import java.io.Writer;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Html panel class for handling the presentation of the command-list command
 */
public class HtmlCommandListPanel extends AbstractPanel
{
    private static final Log log = LogFactory.getLog(HtmlCommandListPanel.class);

    private CommandList list;

    public HtmlCommandListPanel()
    {
    }

    /**
     * Gets the command list defined for this panel
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
     * @param list
     */
    public void setCommandList(CommandList list)
    {
        this.list = list;
    }

    /**
     * Handles the rendering of the html panel
     * @param writer
     * @param nc
     * @param theme
     * @param flags
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
     * @param writer
     * @param dc
     * @param theme
     * @param flags
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
