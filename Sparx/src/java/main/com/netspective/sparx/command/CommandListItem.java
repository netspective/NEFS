package com.netspective.sparx.command;

import com.netspective.commons.command.Command;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * Wrapper class around a Command object with additional information such as caption and
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

    /**
     * Gets the URL associated with this command item
     * @param nc
     * @return
     */
    public String getUrl(HttpServletValueContext nc)
    {
        String requestURL = nc.getHttpRequest().getRequestURL().toString();

        return (requestURL.indexOf("?") != -1 ?  (requestURL + "&" + AbstractListCommand.ACTIVE_LIST_ITEM + "=" + index) :
                (requestURL + "?" + AbstractListCommand.ACTIVE_LIST_ITEM + "=" + index));
    }

    /**
     * Gets the presentation caption for the command
     * @return
     */
    public String getCaption()
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
    public void setCaption(String itemCaption)
    {
        this.itemCaption = itemCaption;
    }

    /**
     * Gets the more detailed information (compared to the caption) about the command item
     * @return
     */
    public String getDescription()
    {
        return itemDescription;
    }

    /**
     * Sets the detailed description to display with the command caption
     * @param itemDescription
     */
    public void setDescription(String itemDescription)
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

    /**
     * Gets the index (position) of this item in the list
     * @return
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index (position) of this item in the list
     * @param index
     */
    public void setIndex(int index)
    {
        this.index = index;
    }
}
