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

import com.netspective.commons.command.Command;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * Wrapper class around a Command object with additional information such as caption and
 * description for displaying the command as a link
 */
public class CommandListItem
{
    private int index;
    private ValueSource description;
    private Command command;
    private ValueSource caption;

    // TODO: This is very similar to the HtmlPanelAction. Need to see if they can be refactored.
    public CommandListItem()
    {

    }

    /**
     * Gets the URL associated with this command item
     *
     * @return URL value source
     */
    public String getUrl(HttpServletValueContext nc)
    {
        // TODO: Need to change this to return some kind of value source instead of returning a string
        String requestURL = nc.getHttpRequest().getRequestURL().toString();
        return (requestURL.indexOf("?") != -1 ? (requestURL + "&" + AbstractListCommand.ACTIVE_LIST_ITEM + "=" + index) :
                (requestURL + "?" + AbstractListCommand.ACTIVE_LIST_ITEM + "=" + index));
    }

    /**
     * Gets the presentation caption for the command
     *
     * @return Value source object containing the value expression
     */
    public ValueSource getCaption()
    {
        if (this.caption == null)
        {
            String caption = null;
            if (command instanceof DialogCommand)
                caption = ((DialogCommand) command).getDialogName();
            else if (command instanceof QueryCommand)
                caption = ((QueryCommand) command).getQueryName();
            else if (command instanceof QueryDefnCommand)
                caption = ((QueryDefnCommand) command).getQueryDefnName();

            if (caption != null)
                this.caption = ValueSources.getInstance().createValueSourceOrStatic("caption");
        }

        return this.caption;
    }

    /**
     * Sets the caption for display of the command as a link. This is optional and if it
     * is not defined, a suitable name or caption from the Command itself is used. For example, for
     * dialogs the dialog name is used as the caption.
     *
     * @param itemCaption the value source for the caption
     */
    public void setCaption(ValueSource itemCaption)
    {
        this.caption = itemCaption;
    }

    /**
     * Gets the more detailed information (compared to the caption) about the command item
     *
     * @return
     */
    public ValueSource getDescription()
    {
        return description;
    }

    /**
     * Sets the detailed description to display with the command caption
     *
     * @param itemDescription
     */
    public void setDescription(ValueSource itemDescription)
    {
        this.description = itemDescription;
    }

    /**
     * Gets the command
     *
     * @return
     */
    public Command getCommand()
    {
        return command;
    }

    /**
     * Sets the command
     *
     * @param command
     */
    public void setCommand(Command command)
    {
        this.command = command;
    }

    /**
     * Gets the index (position) of this item in the list
     *
     * @return
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index (position) of this item in the list
     *
     * @param index
     */
    public void setIndex(int index)
    {
        this.index = index;
    }
}
