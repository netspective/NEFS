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
 * @author Aye Thu
 */

/**
 * $Id: PanelEditor.java,v 1.9 2004-03-06 17:16:28 aye.thu Exp $
 */

package com.netspective.sparx.panel;

import com.netspective.sparx.Project;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.command.PanelEditorCommand;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.sql.Query;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xdm.XmlDataModelSchema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.io.Writer;
import java.io.IOException;

/**
 * Base class for custom panel editors. This class is meant to be EXTENDED to create new panel editor types. This does not
 * define a complete panel editor behavior since the content editing will be up to the child class to implement. The
 * constructor's of this class are all protected and only meant to be instantiated by the PanelEditorsPackage.
 *
 */
public class PanelEditor extends AbstractPanel
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();
    public static final String PANELTYPE_TEMPLATE_NAMESPACE = PanelEditor.class.getName();
    public static final String PANELTYPE_ATTRNAME_TYPE = "type";
    public static final String[] PANELTYPE_ATTRNAMES_SET_BEFORE_CONSUMING = new String[] { "name" };
    public static final PanelEditorTypeTemplateConsumerDefn panelEditorTemplateConsumer = new PanelEditorTypeTemplateConsumerDefn();

    static
    {
        TemplateCatalog.registerConsumerDefnForClass(panelEditorTemplateConsumer, PanelEditor.class, true, true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
    }

    protected static class PanelEditorTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public PanelEditorTypeTemplateConsumerDefn()
        {
            super(PANELTYPE_TEMPLATE_NAMESPACE, PANELTYPE_ATTRNAME_TYPE, PANELTYPE_ATTRNAMES_SET_BEFORE_CONSUMING);
        }

        public String getNameSpaceId()
        {
            return PanelEditor.class.getName();
        }
    }

    protected static final Log log = LogFactory.getLog(ReportPanelEditor.class);
    public static final String PANEL_RECORD_EDIT_ACTION     = "Edit";
    public static final String PANEL_CONTENT_ADD_ACTION      = "Add";
    public static final String PANEL_CONTENT_MANAGE_ACTION   = "Manage";
    public static final String PANEL_RECORD_DONE_ACTION     = "Done";
    public static final String PANEL_CONTENT_DELETE_ACTION   = "Delete";

    // the following are all the possible displaymodes that the editor panel can be in
    public static final int UNKNOWN_MODE                = -1;
    public static final int DEFAULT_DISPLAY_MODE        = 1;    /* default display report mode */
    public static final int EDIT_CONTENT_DISPLAY_MODE   = 2;    /* editing panel content mode (dialog and current content (e.g. report)) */
    public static final int DELETE_CONTENT_DISPLAY_MODE = 3;    /* deleting panel content mode (dialog and current content) */
    public static final int ADD_CONTENT_DISPLAY_MODE    = 4;    /* add content mode (dialog and current content) */
    public static final int MANAGE_CONTENT_DISPLAY_MODE = 5;    /* managing content (report only but different from default) */

    /* default skin to use to display query report panel */
    public static final String DEFAULT_EDITOR_SKIN = "panel-editor";
    /* default name assigned to the dialog defined in the panel editor */
    public static final String DEFAULT_DIALOG_NAME = "panel-editor-dialog";
    /* a request attribute name used to save the name of the panel editor */
    public static final String PANEL_EDITOR_CONTEXT_ATTRIBUTE = "panel-editor-name";
    /* the display mode is passed to the panel using a request attribute with this name */
    public static final String CURRENT_MODE_CONTEXT_ATTRIBUTE = "panel-editor-mode";
    /* the primary key of the record that is to be edited/deleted is passed to the dialog context using this attribute name */
    public static final String POPULATE_KEY_CONTEXT_ATTRIBUTE = "panel-editor-key";
    /* the previous mode of the panel passed using the request attribute */
    public static final String PREV_MODE_CONTEXT_ATTRIBUTE = "panel-editor-prev-mode";
    /* associated project */
    protected Project project;
    /* query associated with this panel */
    protected Query query;
    /* default dialog to use for panel content editing */
    protected Dialog dialog;
    /* the package that the panel belongs to */
    private PanelEditorsPackage nameSpace;
    /* name of the panel */
    private String name;
    /* flag to indicate if the record actions for this panel have been prepared or not */
    private boolean actionsPrepared = false;
    /* indicates what column index of the query is used as the primary key */
    protected int pkColumnIndex = 0;
    /* indicates what request parameter is required for this panel */
    private String requireRequestParam = null;

    /* all available panel editor types */
    private List panelEditorTypes = new ArrayList();

    protected PanelEditor(Project project)
    {
        this.project = project;
    }

    /**
     *
     */
    protected PanelEditor(Project project, PanelEditorsPackage pkg)
    {
        this(project);
        setNameSpace(pkg);
    }


    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        return panelEditorTemplateConsumer;
    }

    public void registerTemplateConsumption(Template template)
    {
        panelEditorTypes.add(template.getTemplateName());
    }


    /**
    * Calculate the mode the record editor panel is in and also set the
    *
    * @return  the current mode of the panel editor
    */
   public static int validatePanelEditorMode(String panelMode, String recordKey)
   {
       int mode = ReportPanelEditor.UNKNOWN_MODE;
       if (panelMode == null)
       {
           mode = ReportPanelEditor.DEFAULT_DISPLAY_MODE;
       }
       else if (panelMode.equals("add"))
       {
           mode = ReportPanelEditor.ADD_CONTENT_DISPLAY_MODE;
       }
       else if (panelMode.equals("edit") && recordKey != null)
       {
           mode = ReportPanelEditor.EDIT_CONTENT_DISPLAY_MODE;
       }
       else if (panelMode.equals("delete") && recordKey != null)
       {
           mode = ReportPanelEditor.DELETE_CONTENT_DISPLAY_MODE;
       }
       else if (panelMode.equals("manage"))
       {
           mode = ReportPanelEditor.MANAGE_CONTENT_DISPLAY_MODE;
       }

       return mode;
   }

    /**
     * Gets the associated project
     *
     * @return  associated project
     */
    public Project getProject()
    {
        return project;
    }

    /**
     * Gets the request parameter that is required for this panel editor
     *
     * @return      request parameter name
     */
    public String getRequireRequestParam()
    {
        return requireRequestParam;
    }

    /**
     * Sets the request parameter that is required for this panel editor
     *
     * @param requireRequestParam
     */
    public void setRequireRequestParam(String requireRequestParam)
    {
        this.requireRequestParam = requireRequestParam;
    }

    /**
     * Gets the name of the panel
     *
     * @return  name of the panel
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the panel
     *
     * @param name      name of the panel
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public String getQualifiedName()
    {
        return nameSpace != null ? nameSpace.getNameSpaceId() + "." + name : name;
    }

    /**
     * Gets the namespace (package name)
     *
     * @return      namespace
     */
    public PanelEditorsPackage getNameSpace()
    {
        return nameSpace;
    }

    /**
     * Sets the namespace (package name)
     *
     * @param nameSpace     package name
     */
    public void setNameSpace(PanelEditorsPackage nameSpace)
    {
        this.nameSpace = nameSpace;
    }

    /**
     * Set the dialog ID associated with the panel editor
     *
     * @param dialog
     */
    public void setDialogRef(String dialog)
    {
        this.dialog = this.project.getDialog(dialog);
        if (this.dialog == null)
            log.error("Unknown dialog '" + dialog + "'.");
    }

    /**
     * Gets the dialog associated with the panel editor
     *
     * @return
     */
    public Dialog getDialog()
    {
        return dialog;
    }

    /**
     * Adds a dialog to the panel editor.
     *
     * @param dialog
     */
    public void addDialog(Dialog dialog)
    {
        this.dialog = dialog;
        if (this.dialog.getName() == null)
            this.dialog.setName(DEFAULT_DIALOG_NAME);
        this.dialog.setNameSpace(getNameSpace().getDialogsNameSpace());
    }

    /**
     * Creates a dialog instance
     *
     * @return
     */
    public Dialog createDialog()
    {
        return new Dialog(project);
    }

    /**
     * Gets the query defined for the default display mode of the panel editor
     *
     * @return   query name (NULL if query definition is defined instead)
     */
    public Query getQuery()
    {
        return query;
    }

    /**
     * Gets the name to use when saving this panel editor in a collection map
     *
     * @return
     */
    public String getNameForMapKey()
    {
        return translateNameForMapKey(getQualifiedName());
    }

    /**
     * Translate name of the panel editor to a key name suitable for use in a map
     *
     * @param name  name of the panel editor
     * @return      translated key name
     */
    public static final String translateNameForMapKey(String name)
    {
        return name != null ? name.toUpperCase() : null;
    }

    public void setActionsPrepared(boolean actionsPrepared)
    {
        this.actionsPrepared = actionsPrepared;
    }

    /**
     * Generates the URL string for the  panel editor's associated actions
     *
     * @param actionMode    the mode  for which the URL is being calculated
     * @return              url string (containing context sensitive elements) used to construct a redirect value source
     */
    public String generatePanelActionUrl(NavigationContext nc, int actionMode)
    {
        return null;
    }

    public static String calculateNextMode(DialogContext dc, String panelName, String panelMode, String prevMode,
                                         String panelRecordKey)
    {
        String url ="?";
        String currentUrl = dc.getNavigationContext().getActivePage().getUrl(dc);
        int previousMode = validatePanelEditorMode(prevMode, panelRecordKey);
        if (previousMode != DEFAULT_DISPLAY_MODE)
            url = url + PanelEditorCommand.PANEL_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + panelName + ",manage";
        else
            url = currentUrl;

        return url;
    }

    /**
     * Creates all the panel actions for the  panel editor. This method SHOULD only be called once to populate the
     * panel editor.
     *
     * @param nc    current navigation context
     */
    public void createPanelActions(NavigationContext nc)
    {
        //createPanelBannerActions(nc);
        createPanelFrameActions(nc);
        createPanelContentActions(nc);
        actionsPrepared = true;
    }

    /**
     * Creates actions to be defined in the content of the panel
     *
     * @param nc
     */
    public void createPanelContentActions(NavigationContext nc) {}

    /**
     * Creates actions to be defined in the frame of the panel
     *
     * @param nc
     */
    public  void createPanelFrameActions(NavigationContext nc) {}


    /**
     * Creates all the panel actions for the banner
     *
     * @param nc    current navigation context
     */
    public void createPanelBannerActions(NavigationContext nc){}

    public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Calculate and process the state of the all the panel actions based on current context
     *
     * @param nc                current navigation context
     * @param vc                current report panel context
     * @param panelRecordCount  total number of records being displayed
     * @param mode              panel mode
     */
    public void preparePanelActionStates(NavigationContext nc, HtmlPanelValueContext vc, int panelRecordCount, int mode)
    {
        HtmlPanelActionStates actionStates = vc.getPanelActionStates();

        if (mode == DEFAULT_DISPLAY_MODE)
        {
            actionStates.getState(PANEL_RECORD_DONE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            if (panelRecordCount > 0)
                actionStates.getState(PANEL_CONTENT_ADD_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            actionStates.getState(PANEL_CONTENT_DELETE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            if (panelRecordCount <= 0)
                actionStates.getState(PANEL_CONTENT_MANAGE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
        }
        else if (mode == ADD_CONTENT_DISPLAY_MODE || mode == EDIT_CONTENT_DISPLAY_MODE || mode == DELETE_CONTENT_DISPLAY_MODE)
        {
            actionStates.getState(PANEL_CONTENT_MANAGE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            actionStates.getState(PANEL_CONTENT_ADD_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
        }
        else if (mode == MANAGE_CONTENT_DISPLAY_MODE)
        {
            actionStates.getState(PANEL_CONTENT_MANAGE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
        }
    }

    /**
     * Check to see if all the actions needed for the  panel edito have been ADDED. The
     * adding of the actions need to be done only once and this flag is set
     * once the addition is done.
     *
     * @return  True if all required actions have been added
     */
    public boolean isActionsPrepared()
    {
        return actionsPrepared;
    }
}