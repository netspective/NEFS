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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.command.CommandException;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * Abstract class for handling commands that present a list of link items for selection by default.
 * When a selected item or its index is passed in the request as a parameter, the corresponding list
 * item is presented instead of the list.
 */
public abstract class AbstractListCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(AbstractListCommand.class);

    /* the request parameter name for the active (selected) list item */
    public static final String ACTIVE_LIST_ITEM = "activeItem";

    /**
     * Gets the list
     *
     * @param vc
     *
     * @return
     */
    public abstract List getItems(HttpServletValueContext vc);

    /**
     * @param vc
     *
     * @return
     */
    public abstract HtmlPanelValueContext getPanelContext(HttpServletValueContext vc);

    /**
     * Gets the active (selected) item from the request
     *
     * @param vc
     *
     * @return
     */
    public String getActiveItem(HttpServletValueContext vc)
    {
        return vc.getHttpRequest().getParameter(ACTIVE_LIST_ITEM);
    }

    /**
     * Renders the item list
     *
     * @param writer
     * @param nc
     */
    protected abstract void renderList(Writer writer, NavigationContext nc) throws IOException;

    /**
     * Render the selected item component from the list
     *
     * @param writer
     * @param nc
     *
     * @throws IOException
     */
    protected abstract void renderListItem(Writer writer, NavigationContext nc, String activeItem) throws IOException;

    /**
     * Renders the item list
     *
     * @param writer
     * @param nc
     */
    protected abstract void renderList(Writer writer, DialogContext nc) throws IOException;

    /**
     * Render the selected item component from the list
     *
     * @param writer
     * @param nc
     *
     * @throws IOException
     */
    protected abstract void renderListItem(Writer writer, DialogContext nc, String activeItem) throws IOException;

    /**
     * Handles the command
     *
     * @param writer
     * @param nc
     * @param unitTest
     *
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        String activeItem = getActiveItem(nc);
        if (activeItem != null && activeItem.length() > 0)
            renderListItem(writer, nc, activeItem);
        else
            renderList(writer, nc);
    }

    /**
     * Handles the command
     *
     * @param writer
     * @param dc
     * @param unitTest
     *
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, DialogContext dc, boolean unitTest) throws CommandException, IOException
    {
        String activeItem = getActiveItem(dc);
        if (activeItem != null && activeItem.length() > 0)
            renderListItem(writer, dc, activeItem);
        else
            renderList(writer, dc);

    }
}
