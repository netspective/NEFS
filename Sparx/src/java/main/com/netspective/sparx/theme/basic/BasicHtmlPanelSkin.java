/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: BasicHtmlPanelSkin.java,v 1.9 2003-05-13 02:13:39 shahid.shah Exp $
 */

package com.netspective.sparx.theme.basic;

import java.io.Writer;
import java.io.IOException;

import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.panel.*;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.value.BasicDbHttpServletValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.command.CommandNotFoundException;
import com.netspective.commons.command.Command;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;

public class BasicHtmlPanelSkin extends AbstractThemeSkin implements HtmlPanelSkin
{
    public static class Flags extends XdmBitmaskedFlagsAttribute
    {
        public static final int FULL_WIDTH = 1;
        public static final int SHOW_BANNER = FULL_WIDTH * 2;
        public static final int STARTCUSTOM = SHOW_BANNER * 2;

        public static final XdmBitmaskedFlagsAttribute.FlagDefn[] FLAGDEFNS = new XdmBitmaskedFlagsAttribute.FlagDefn[]
        {
            new FlagDefn(ACCESS_XDM, "FULL_WIDTH", FULL_WIDTH),
            new FlagDefn(ACCESS_XDM, "SHOW_BANNER", SHOW_BANNER),
        };

        public XdmBitmaskedFlagsAttribute.FlagDefn[] getFlagsDefns()
        {
            return FLAGDEFNS;
        }
    }

    private boolean defaultPanel;
    protected String panelClassNamePrefix;
    protected String panelResourcesPrefix;
    protected Flags flags;

    public BasicHtmlPanelSkin(Theme theme, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme);
        flags = createFlags();
        setPanelClassNamePrefix(panelClassNamePrefix);
        setPanelResourcesPrefix(panelResourcesPrefix);
        flags.setFlag(Flags.SHOW_BANNER);
        if(fullWidth)
            flags.setFlag(Flags.FULL_WIDTH);
    }

    public boolean isDefault()
    {
        return defaultPanel;
    }

    public void setDefault(boolean defaultPanel)
    {
        this.defaultPanel = defaultPanel;
    }

    public Flags createFlags()
    {
        return new Flags();
    }

    public String getPanelClassNamePrefix()
    {
        return panelClassNamePrefix;
    }

    public void setPanelClassNamePrefix(String panelClassNamePrefix)
    {
        this.panelClassNamePrefix = panelClassNamePrefix;
    }

    public String getPanelResourcesPrefix()
    {
        return panelResourcesPrefix;
    }

    public void setPanelResourcesPrefix(String panelResourcesPrefix)
    {
        this.panelResourcesPrefix = panelResourcesPrefix;
    }

    public Flags getFlags()
    {
        return flags;
    }

    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

    public void produceHeadingExtras(Writer writer, HtmlPanelValueContext vc, HtmlPanelFrame frame) throws IOException
    {
        HtmlPanelActions actions = frame.getActions();

        if(actions != null && actions.size() > 0)
        {
            Theme theme = ((HtmlTabularReportValueContext) vc).getActiveTheme();
            String imgPath = ((HtmlTabularReportValueContext) vc).getThemeImagesRootUrl(theme) + "/" + panelResourcesPrefix;

            int colCount = 0;

            // create a temporary string buffer for the HTML of the heading action items
            StringBuffer itemBuffer = new StringBuffer();
            for (int i=0; i < actions.size(); i++)
            {
                if (i != 0)
                {
                    itemBuffer.append("            <td bgcolor=\"white\"><img src=\"" + imgPath + "/login/spacer.gif\" width=\"5\" height=\"5\"></td>");
                    colCount++;
                }
                HtmlPanelAction item = actions.get(i);
                String itemUrl = "TODO"; //item.getParameters();
                String itemCaption = item.getCaption().getTextValue(vc);
                ValueSource itemIcon = item.getIcon();
                if (itemIcon != null)
                {
                    // icon for this item is defined so use the passed in image INSTEAD of using the CSS based background image
                    itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-item\" width=\"18\"></td>");
                    colCount++;
                }
                else
                {
                    itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-item\" width=\"18\"><img src=\"" + imgPath + "/login/spacer.gif\" width=\"18\" height=\"19\"></td>");
                    colCount++;
                }
                itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-box\">" +
                        "<a class=\""+ panelClassNamePrefix +"-frame-action\" href=\""+ itemUrl + "\">&nbsp;" + itemCaption + "&nbsp;</a></td>");
                colCount++;
            }

//            writer.write("<td valign=\"bottom\" class=\""+ panelClassNamePrefix +"-frame-table-action\" bgcolor=\"white\">\n");
//            writer.write("    <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
//            writer.write("        <tr>\n");
//            writer.write("            <td bgcolor=\"white\" width=\"18\" colspan=\"" + colCount + "\">" +
//                    "<img src=\"" + imgPath + "/login/spacer.gif\" height=\"5\" width=\"100%\" border=\"0\"></td>\n");
//            writer.write("        </tr>\n");
            if (itemBuffer.length() > 0)
            {
//                writer.write("        <tr>\n");
                writer.write(itemBuffer.toString());
//                writer.write("        </tr>\n");
            }
//            writer.write("        </tr>\n  ");
//            writer.write("    </table>\n");
//            writer.write("</td>\n");
        }
    }

    public void renderPanelRegistration(Writer writer, HtmlPanelValueContext vc) throws IOException
    {
        HtmlPanel panel = vc.getPanel();
        writer.write("<script>ACTIVE_PANEL_PARENT.registerPanel(new Panel(\""+ panel.getIdentifier() +"\", false, PANELSTYLE_TOPLEVEL, \""+ panelClassNamePrefix +"\"))</script>\n");
    }

    public void renderFrameBegin(Writer writer, HtmlPanelValueContext vc) throws IOException
    {
        HtmlPanel panel = vc.getPanel();
        HtmlPanelFrame frame = panel.getFrame();

        Theme theme = ((BasicDbHttpServletValueContext) vc).getActiveTheme();
        String imgPath = ((BasicDbHttpServletValueContext) vc).getThemeImagesRootUrl(theme) + "/" + panelResourcesPrefix;

        writer.write("<table id=\""+ panel.getIdentifier() +"_frame\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" nowrap ");
        if(flags.flagIsSet(Flags.FULL_WIDTH))
            writer.write("width='100%' ");
        writer.write(">\n");

        if(frame.hasHeadingOrFooting())
        {
            String heading = null;
            ValueSource hvs = frame.getHeading();
            if(hvs != null)
                heading = hvs.getValue(vc).getTextValue();

            if(heading != null)
            {
                writer.write("<tr>\n");
                writer.write("    <td class=\""+ panelClassNamePrefix +"\">\n");
                writer.write("    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" nowrap>\n");
                writer.write("        <tr>\n");
                if (frame.getFlags().flagIsSet(HtmlPanelFrame.Flags.ALLOW_COLLAPSE))
                {
                    if (vc.isMinimized())
                        writer.write("            <td id=\""+ panel.getIdentifier() +"_action\" class=\""+ panelClassNamePrefix +"-frame-heading-action-expand\" align=\"left\" valign=\"middle\" nowrap width=\"17\" onclick=\"ALL_PANELS.togglePanelExpandCollapse('"+ panel.getIdentifier() +"')\">" +
                            "<!-- <img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\">--></td>");
                    else
                        writer.write("            <td id=\""+ panel.getIdentifier() +"_action\" class=\""+ panelClassNamePrefix +"-frame-heading-action-collapse\"   align=\"left\" valign=\"middle\" nowrap width=\"17\" onclick=\"ALL_PANELS.togglePanelExpandCollapse('"+ panel.getIdentifier() +"')\">" +
                            "<!-- <img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\"> --></td>");

                    writer.write("<script>ALL_PANELS.getPanel(\""+ panel.getIdentifier() +"\").minimized = "+ (vc.isMinimized() ? "true" : "false") +"</script>");
                }
                else
                {
                    writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-heading-action-left-blank\" align=\"left\" valign=\"middle\" nowrap width=\"17\">" +
                        "<!-- <img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\">--></td>\n");
                }
                writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-heading\" align=\"left\" valign=\"middle\" nowrap>" + heading +
                        "</td>\n");
                writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-heading-action-right-blank\" align=\"center\" valign=\"middle\" nowrap width=\"17\">" +
                    "<!-- <img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\">--></td>\n");
                writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-mid\" align=\"right\" valign=\"top\" nowrap width=\"100%\">" +
                    "<!-- <img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"100%\" border=\"0\">--></td>\n");
                writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-end-cap\" align=\"right\" valign=\"top\" nowrap width=\"2\"></td>\n");
                produceHeadingExtras(writer, vc, frame);
                writer.write("        </tr>\n");
                writer.write("    </table>\n");
                writer.write("    </td>\n");
                writer.write("</tr>\n");
            }
        }

        renderBanner(writer, vc);

        int height = panel.getHeight();
        if(height > 0)
            writer.write("<tr id=\""+ panel.getIdentifier() +"_content\">\n     <td class=\""+ panelClassNamePrefix +"-content\"><div style=\"height: "+ height +"; overflow: auto;\">\n");
        else
            writer.write("<tr id=\""+ panel.getIdentifier() +"_content\">\n     <td class=\""+ panelClassNamePrefix +"-content\"><div>\n");
    }

    public void renderFrameEnd(Writer writer, HtmlPanelValueContext vc) throws IOException
    {
        HtmlPanel panel = vc.getPanel();
        HtmlPanelFrame frame = panel.getFrame();

        writer.write("    </div></td>\n");
        writer.write("</tr>\n");

        if(frame.hasHeadingOrFooting())
        {
            ValueSource fvs = frame.getFooting();
            if(fvs != null)
            {
                writer.write("<tr id=\""+ panel.getIdentifier() +"_banner_footer\">\n");
                writer.write("    <td class=\""+ panelClassNamePrefix +"-banner-footer\">" + fvs.getTextValue(vc) + "</td>\n");
                writer.write("</tr>\n");
            }
        }

        writer.write("</table>\n");
    }

    protected void producerBannerActionsHtml(Writer writer, HtmlPanelValueContext rc, HtmlPanelActions actions) throws IOException, CommandNotFoundException
    {
        int actionsCount = actions.size();
        if(actionsCount == 0) return;
        String bannerItemFontAttrs = "";

        //TODO: conversion from Sparx 2.x required below
        //ReportSkin skin = rc.getSkin();
        //if (skin instanceof com.netspective.sparx.xaf.skin.HtmlReportSkin)
        //    bannerItemFontAttrs = ((HtmlReportSkin) skin).getBannerItemFontAttrs();

        if(actions.getStyle().getValueIndex() == HtmlPanelActions.Style.HORIZONTAL)
        {
            for(int i = 0; i < actionsCount; i++)
            {
                HtmlPanelAction action = actions.get(i);
                Command itemCmd = action.getCommand(rc);
                ValueSource itemCaption = action.getCaption();
                ValueSource itemIcon = action.getIcon();
                String caption = itemCaption != null ? (itemCmd != null ? ("<a href='" + itemCmd.getParameters() + "'>" + itemCaption.getValue(rc) + "</a>") : itemCaption.getValue(rc).getTextValue()) : null;

                writer.write("<font " + bannerItemFontAttrs + ">");
                if(i > 0)
                    writer.write(", ");
                if(itemIcon != null)
                    writer.write("<img src='" + itemIcon.getValue(rc) + "'>");
                writer.write(caption);
                writer.write("</font>");
            }
        }
        else
        {
            writer.write("<table border=0 cellspacing=0>");
            for(int i = 0; i < actionsCount; i++)
            {
                HtmlPanelAction action = actions.get(i);
                Command itemCmd = action.getCommand(rc);
                ValueSource itemCaption = action.getCaption();
                ValueSource itemIcon = action.getIcon();
                HtmlPanelActions childItems = action.getChildren();
                String caption = itemCaption != null ? (itemCmd != null ? ("<a href='" + itemCmd.getParameters() + "'>" + itemCaption.getValue(rc) + "</a>") : itemCaption.getValue(rc).getTextValue()) : null;

                writer.write("<tr><td>");
                writer.write(itemIcon != null ? "<img src='" + itemIcon.getValue(rc) + "'>" : "-");
                writer.write("</td>");
                writer.write("<td><font " + bannerItemFontAttrs + ">");
                if(caption != null)
                    writer.write(caption);
                if(childItems != null && childItems.size() > 0)
                    producerBannerActionsHtml(writer, rc, childItems);
                writer.write("</font></td>");
                writer.write("</tr>");
            }
            writer.write("</table>");
        }
    }

    /**
     * Displays the report banner html. Utilizes the THEME
     * @param writer
     * @param vc
     * @throws java.io.IOException
     */
    protected void renderBanner(Writer writer, HtmlPanelValueContext vc) throws IOException
    {
        HtmlPanel panel = vc.getPanel();
        HtmlPanelBanner banner = panel.getBanner();
        if (banner == null)
            return;

        HtmlPanelActions actions = banner.getActions();
        ValueSource content = banner.getContent();
        if((actions == null || (actions != null || actions.size() == 0) && content == null))
            return;

        writer.write("<tr id=\""+ panel.getIdentifier() +"_banner\"><td class=\""+ panelClassNamePrefix +"-banner\">\n");
        if(content != null)
        {
            writer.write(content.getTextValue(vc));
        }
        else
        {
            try
            {
                producerBannerActionsHtml(writer, vc, actions);
            }
            catch (CommandNotFoundException e)
            {
                writer.write(e.toString());
            }
        }
        writer.write("</td></tr>\n");
    }
}
