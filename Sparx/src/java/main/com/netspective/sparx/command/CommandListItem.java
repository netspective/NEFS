package com.netspective.sparx.command;

import com.netspective.commons.command.Command;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.sparx.navigate.NavigationContext;

/**
 * Wrapper class around a Command object with extra information such as caption and
 * description for displaying the command as a link
 */
public class CommandListItem
{
    private int index;
    private String itemCaption;
    private String itemDescription;
    private Command command;

    public CommandListItem()
    {

    }

    public String getUrl(NavigationContext nc)
    {
        String requestURL = nc.getHttpRequest().getRequestURL().toString();

        return (requestURL.indexOf("?") != -1 ?  (requestURL + "&commandItem=" + index) :
                (requestURL + "?commandItem=" + index));
    }

    public String getItemCaption()
    {
        if (itemCaption == null)
        {
            if (command instanceof DialogCommand)
            {
                itemCaption = ((DialogCommand) command).getDialogName();
            }
            else if (command instanceof QueryCommand)
            {
                itemCaption = ((QueryCommand) command).getQueryName();
            }
            else if (command instanceof QueryDefnCommand)
            {
                itemCaption = ((QueryDefnCommand) command).getQueryDefnName();
            }

        }
        return itemCaption;
    }

    /**
     * Sets the caption for display of the command as a link. This is optional and if it
     * is not defined, a suitable name or caption from the Command itself is used. For example, for
     * dialogs the dialog name is used as the caption.
     * @param itemCaption
     */
    public void setItemCaption(String itemCaption)
    {
        this.itemCaption = itemCaption;
    }

    public String getItemDescription()
    {
        return itemDescription;
    }

    /**
     * Sets the description to display with the command caption
     * @param itemDescription
     */
    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    /**
     * Gets the command
     * @return
     */
    public Command getCommand()
    {
        return command;
    }

    /**
     * Sets the command
     * @param command
     */
    public void setCommand(Command command)
    {
        this.command = command;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }
}