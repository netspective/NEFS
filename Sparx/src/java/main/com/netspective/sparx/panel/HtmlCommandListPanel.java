package com.netspective.sparx.panel;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.basic.HtmlListPanelSkin;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.command.CommandList;

import java.io.Writer;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        return new CommandList();
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
     * Renders the panel
     * @param writer
     * @param nc
     * @param theme
     * @param flags
     * @throws IOException
     */
    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        HtmlListPanelSkin skin = theme.getListPanelSkin();
        skin.renderHtml(writer, nc, list.getItems());
    }

    public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
    {
        //To change body of implemented methods use Options | File Templates.
    }
}
