/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: Dialog.java,v 1.35 2003-10-16 12:46:01 aye.thu Exp $
 */

package com.netspective.sparx.form;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Main;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.form.field.DialogFieldFlags;
import com.netspective.sparx.form.field.type.GridField;
import com.netspective.sparx.form.field.type.CompositeField;
import com.netspective.sparx.form.field.type.SeparatorField;
import com.netspective.sparx.form.handler.DialogExecuteHandler;
import com.netspective.sparx.form.handler.DialogExecuteHandlers;
import com.netspective.sparx.form.handler.DialogExecuteDefaultHandler;
import com.netspective.sparx.form.handler.DialogNextActionProvider;
import com.netspective.sparx.form.listener.DialogPopulateForSubmitListener;
import com.netspective.sparx.form.listener.DialogStateListener;
import com.netspective.sparx.form.listener.DialogInitialPopulateForSubmitListener;
import com.netspective.sparx.form.listener.DialogInitialPopulateForDisplayListener;
import com.netspective.sparx.form.listener.DialogInitialPopulateListener;
import com.netspective.sparx.form.listener.DialogPopulateListener;
import com.netspective.sparx.form.listener.DialogPopulateForDisplayListener;
import com.netspective.sparx.form.listener.DialogStateBeforeValidationListener;
import com.netspective.sparx.form.listener.DialogStateAfterValidationListener;
import com.netspective.sparx.form.listener.DialogValidateListener;
import com.netspective.sparx.form.listener.DialogListener;
import com.netspective.sparx.form.listener.DialogListenerPlaceholder;
import com.netspective.sparx.panel.AbstractPanel;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.value.BasicDbHttpServletValueContext;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.io.InputSourceLocator;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.Schema;

/**
 * The <code>Dialog</code> object contains the dialog/form's structural information, field types, rules, and
 * execution logic. It is cached and reused whenever needed. It contains methods to create the HTML for display,
 * to perform client-side validations, and to perform server-side validations.
 * <p>
 * <center>
 * <img src="doc-files/dialog-1.jpg"/>
 * </center>
 * <p>
 * The dialog execution logic can contain different actions
 * such as SQL and business actions. These actions may be specified as XML or can point to any Java action classes.
 * For dialog objects that need more complex actions for data population, validation,
 * and execution, the <code>Dialog</code> class can be subclassed to implement customized actions.
 */
public class Dialog extends AbstractPanel implements TemplateConsumer, XmlDataModelSchema.InputSourceLocatorListener
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    public static final String ATTRNAME_TYPE = "type";
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[] { "name" };
    private static DialogTypeTemplateConsumerDefn dialogTypeConsumer = new DialogTypeTemplateConsumerDefn();
    private static int dialogNumber = 0;

    protected static class DialogTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public DialogTypeTemplateConsumerDefn()
        {
            super(null, ATTRNAME_TYPE, ATTRNAMES_SET_BEFORE_CONSUMING);
        }

        public String getNameSpaceId()
        {
            return Dialog.class.getName();
        }
    }

    public static class ConnectionShareType extends XdmEnumeratedAttribute
    {
        public ConnectionShareType()
        {
            super(BasicDbHttpServletValueContext.SHARED_CONN_TYPE_NONE);
        }

        public ConnectionShareType(int valueIndex)
        {
            super(valueIndex);
        }

        public String[] getValues()
        {
            return BasicDbHttpServletValueContext.SHARED_CONN_TYPES;
        }
    }

    /**
     * Request parameter which indicates whether or not the dialog should be automatically executed when it is being loaded
     */
    public static final String PARAMNAME_AUTOEXECUTE = "_d_exec";
    public static final String PARAMNAME_OVERRIDE_SKIN = "_d_skin";
    public static final String PARAMNAME_DIALOGPREFIX = "_d.";
    public static final String PARAMNAME_CONTROLPREFIX = "_dc.";

    public static final String PARAMNAME_ACTIVEMODE = ".active_mode";
    public static final String PARAMNAME_NEXTMODE = ".next_mode";
    public static final String PARAMNAME_RUNSEQ = ".run_sequence";
    public static final String PARAMNAME_EXECSEQ = ".exec_sequence";
    public static final String PARAMNAME_ORIG_REFERER = ".orig_referer";
    public static final String PARAMNAME_POST_EXECUTE_REDIRECT = ".post_exec_redirect";
    public static final String PARAMNAME_TRANSACTIONID = ".transaction_id";
    public static final String PARAMNAME_SUBMIT_DATA = ".submit_data";
    public static final String PARAMNAME_PEND_DATA = ".pend_data";
    public static final String PARAMNAME_RESETCONTEXT = ".reset_context";
    public static final String PARAMNAME_INITIALCONTEXT = ".initial_context";

    /*
	   the debug flags when first passed in (start of dialog, run seq == 1)
	   is passed using the parameter "debug_flags" (INITIAL). When the dialog is
	   in run sequence > 1 (after submit) the data command is passed in as a
	   hidden "pass-thru" variable with the suffix PARAMNAME_DEBUG_FLAGS
    */
    public static final String PARAMNAME_DEBUG_FLAGS_INITIAL = "debug_flags";
    public static final String PARAMNAME_DEBUG_FLAGS = ".debug_flags";
    /*
	   the data perspective when first passed in (start of dialog, run seq == 1)
	   is passed using the parameter "perspective" (INITIAL). When the dialog is
	   in run sequence > 1 (after submit) the data command is passed in as a
	   hidden "pass-thru" variable with the suffix PARAMNAME_PERSPECTIVE
    */
    public static final String PARAMNAME_PERSPECTIVE_INITIAL = "data_perspective";
    public static final String PARAMNAME_PERSPECTIVE = ".data_perspective";

    public static final String translateNameForMapKey(String name)
    {
        return name != null ? name.toUpperCase() : null;
    }

    private InputSourceLocator inputSourceLocator;
    private Log log = LogFactory.getLog(Dialog.class);
    private DialogFields fields;
    private DialogFlags dialogFlags;
    private DialogDebugFlags debugFlags;
    private DialogLoopStyle loop = new DialogLoopStyle(DialogLoopStyle.APPEND);
    private DialogDirector director = createDirector();
    private DialogsPackage nameSpace;
    private String name = "dialog_" + (++dialogNumber);
    private String htmlFormName;
    private int layoutColumnsCount = 1;
    private String[] retainRequestParams;
    private Class dialogContextClass = DialogContext.class;
    private List dialogTypes = new ArrayList();
    private List clientJavascripts = new ArrayList();
    private DialogExecuteHandlers executeHandlers = new DialogExecuteHandlers();
    private DialogNextActionProvider nextActionProvider;
    private String bindSchemaTableName;

    private Table bindTable;
    private Map bindColumnFieldsMap = new HashMap();  // key is a Column instance, value is a DialogField
    private Set bindColumnTablesSet = new HashSet(); // a list of all the tables the fields map to (in case of child tables, etc)
    private ConnectionShareType sharedType = new ConnectionShareType();

    private boolean haveInitialPopulateForDisplayListeners;
    private boolean haveInitialPopulateForSubmitListeners;
    private boolean haveInitialPopulateListeners;
    private boolean havePopulateForDisplayListeners;
    private boolean havePopulateForSubmitListeners;
    private boolean havePopulateListeners;
    private boolean haveStateAfterValidationListeners;
    private boolean haveStateBeforeValidationListeners;
    private boolean haveStateListeners;
    private boolean haveValidationListeners;
    private List initialPopulateForDisplayListeners = new ArrayList();
    private List initialPopulateForSubmitListeners = new ArrayList();
    private List initialPopulateListeners = new ArrayList();
    private List populateForDisplayListeners = new ArrayList();
    private List populateForSubmitListeners = new ArrayList();
    private List populateListeners = new ArrayList();
    private List stateAfterValidationListeners = new ArrayList();
    private List stateBeforeValidationListeners = new ArrayList();
    private List stateListeners = new ArrayList();
    private List validationListeners = new ArrayList();

    /**
     * Create a dialog
     */
    public Dialog()
    {
        fields = constructFields();
        dialogFlags = createDialogFlags();
        debugFlags = createDebugFlags();
    }

    public Dialog(DialogsPackage pkg)
    {
        this();
        setNameSpace(pkg);
    }

    /**
     * Gets the connection sharing mode
     * @return
     */
    public ConnectionShareType getConnectionShareType()
    {
        return sharedType;
    }

    /**
     * Sets the connection sharing mode
     * @param share
     */
    public void setConnectionShareType(ConnectionShareType share)
    {
        sharedType = share;
    }

    public InputSourceLocator getInputSourceLocator()
    {
        return inputSourceLocator;
    }

    public void setInputSourceLocator(InputSourceLocator inputSourceLocator)
    {
        this.inputSourceLocator = inputSourceLocator;
    }

    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        return dialogTypeConsumer;
    }

    public void registerTemplateConsumption(Template template)
    {
        dialogTypes.add(template.getTemplateName());
    }

    public DialogFields constructFields()
    {
        return new DialogFields(this);
    }

    public DialogFlags createDialogFlags()
    {
        return new DialogFlags();
    }

    public DialogDebugFlags createDebugFlags()
    {
        return new DialogDebugFlags();
    }

    public Log getLog()
    {
        return log;
    }

    public String getName()
    {
        return name;
    }

    public String getNameForMapKey()
    {
        return translateNameForMapKey(getQualifiedName());
    }

    public String getQualifiedName()
    {
        return nameSpace != null ? nameSpace.getNameSpaceId() + "." + name : name;
    }

    public void setName(String name)
    {
        this.name = name;
        setHtmlFormName(name);
        log = LogFactory.getLog(getClass() + "." + getQualifiedName());
    }

    /**
     * Gets the dialog name
     *
     * @return String name
     */
    public String getHtmlFormName()
    {
        return htmlFormName;
    }

    /**
     * Sets the dialog name
     *
     * @param newName dialog name
     */
    public void setHtmlFormName(String newName)
    {
        htmlFormName = TextUtils.xmlTextToJavaIdentifier(newName, false);
    }

    public DialogDebugFlags getDebugFlags()
    {
        return debugFlags;
    }

    public void setDebugFlags(DialogDebugFlags debugFlags)
    {
        this.debugFlags = debugFlags;
    }

    public DialogFlags getDialogFlags()
    {
        return dialogFlags;
    }

    public void setDialogFlags(DialogFlags dialogFlags)
    {
        this.dialogFlags = dialogFlags;
    }

    public DialogLoopStyle getLoop()
    {
        return loop;
    }

    public void setLoop(DialogLoopStyle loop)
    {
        this.loop = loop;
    }

    public String getLoopSeparator()
    {
        return loop.getLoopSeparator();
    }

    public void setLoopSeparator(String loopSeparator)
    {
        loop.setLoopSeparator(loopSeparator);
    }

    /**
     * Returns true if the heading should be hidden
     */
    public boolean hideHeading(DialogContext dc)
    {
        if(dialogFlags.flagIsSet(DialogFlags.HIDE_HEADING_IN_EXEC_MODE) && dc.inExecuteMode())
            return true;
        else
            return false;
    }

    public Class getDialogContextClass()
    {
        return dialogContextClass;
    }

    public void setDialogContextClass(Class dialogContextClass)
    {
        this.dialogContextClass = dialogContextClass;
    }

    public int getLayoutColumnsCount()
    {
        return layoutColumnsCount;
    }

    public String getPostExecuteRedirectUrlParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_POST_EXECUTE_REDIRECT;
    }

    public String getOriginalRefererParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_ORIG_REFERER;
    }

    public String getActiveModeParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_ACTIVEMODE;
    }

    public String getNextModeParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_NEXTMODE;
    }

    public String getRunSequenceParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_RUNSEQ;
    }

    public String getExecuteSequenceParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_EXECSEQ;
    }

    public String getTransactionIdParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_TRANSACTIONID;
    }

    public String getResetContextParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_RESETCONTEXT;
    }

    public String getSubmitDataParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_SUBMIT_DATA;
    }

    public String getPendDataParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_PEND_DATA;
    }

    public final String getInitialContextParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_INITIALCONTEXT;
    }

    public String getValuesRequestAttrName()
    {
        return "dialog-" + htmlFormName + "-field-values";
    }

    public String getDataCmdParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_PERSPECTIVE;
    }

    public String getDebugFlagsParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_DEBUG_FLAGS;
    }

    /**
     * Get a list of dialog fields
     *
     * @return List
     */
    public DialogFields getFields()
    {
        return fields;
    }

    /**
     * Indicates whether or not to retain the HTTP request parameters as dialog fields
     *
     * @return boolean True if the request parameters are retained in the dialog
     */
    public boolean retainRequestParams()
    {
        return dialogFlags.flagIsSet(DialogFlags.RETAIN_ALL_REQUEST_PARAMS) || (retainRequestParams != null);
    }

    /**
     * Get the retained request parameters as a string array
     *
     * @return String[]
     */
    public String[] getRetainParams()
    {
        return retainRequestParams;
    }

    /**
     * Set the retained request parameters
     *
     * @param value array of string values
     */
    public void setRetainParams(String value)
    {
        if(value.equals("*"))
            dialogFlags.setFlag(DialogFlags.RETAIN_ALL_REQUEST_PARAMS);
        else
            retainRequestParams = TextUtils.split(value, ",", true);
    }

    public String getNextActionUrl(DialogContext dc, String defaultUrl)
    {
        if(nextActionProvider != null)
            return nextActionProvider.getDialogNextActionUrl(dc, defaultUrl);

        if(director == null)
            return defaultUrl;

        String result = director.getNextActionUrl(dc);
        if(result == null || result.equals("-"))
            return defaultUrl;

        return result;
    }

    public DialogDirector getDirector()
    {
        return director;
    }

    public DialogDirector createDirector()
    {
        return new DialogDirector();
    }

    public void addDirector(DialogDirector value)
    {
        director = value;
        value.setOwner(this);
    }

    /**
	 * Gets all the javascript files to be included with this dialog
	 *
	 * @return ArrayList
	 */
	public List getClientJs()
	{
		return this.clientJavascripts;
	}

    /**
     * Adds a javascript file to be included
     * @param clientJsFile
     */
    public void addClientJs(DialogIncludeJavascriptFile clientJsFile)
    {
        clientJavascripts.add(clientJsFile);
    }

    public DialogsPackage getNameSpace()
    {
        return nameSpace;
    }

    public void setNameSpace(DialogsPackage nameSpace)
    {
        this.nameSpace = nameSpace;
    }

    public DialogField createField()
    {
        return new DialogField();
    }

    /**
     * Add a dialog field
     *
     * @param field datlog field
     */
    public void addField(DialogField field)
    {
        fields.add(field);
        field.setOwner(this);
    }

    public CompositeField createComposite()
    {
        return new CompositeField();
    }

    public void addComposite(CompositeField field)
    {
        addField(field);
    }

    public SeparatorField createSeparator()
    {
        return new SeparatorField();
    }

    public void addSeparator(SeparatorField field)
    {
        addField(field);
    }

    public GridField createGrid()
    {
        return new GridField();
    }

    public void addGrid(GridField field)
    {
        addField(field);
    }

    public String getBindTableName()
    {
        return bindSchemaTableName;
    }

    public void setBindTable(String schemaTableNames)
    {
        bindSchemaTableName = schemaTableNames;
    }

    public Table getBindTable()
    {
        return bindTable;
    }

    public Map getBindColumnFieldsMap()
    {
        return bindColumnFieldsMap;
    }

    public Set getBindColumnTablesSet()
    {
        return bindColumnTablesSet;
    }

    /**
     * Loops through each dialog field and finalize them. Because this can be called via multiple threads but all threads
     * usually use the same dialog, it is synchronized.
     */
    public synchronized void finalizeContents(NavigationContext nc)
    {
        // be sure to get the bound table first because children may need it
        if(bindSchemaTableName != null)
        {
            bindTable = nc.getProject().getSchemas().getTable(bindSchemaTableName);
            if(bindTable == null)
                log.error("Dialog '"+ getQualifiedName() +"' tried to bind to table '"+ bindSchemaTableName +"' but it does not exist.");
        }

        fields.finalizeContents(nc);
        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            if(field.getFlags().flagIsSet(DialogFieldFlags.COLUMN_BREAK_BEFORE | DialogFieldFlags.COLUMN_BREAK_AFTER))
                layoutColumnsCount++;
        }

        dialogFlags.setFlag(DialogFlags.CONTENTS_FINALIZED);
    }

    /**
     * Populate the dialog with field values.
     * This should be called everytime the dialog is loaded except when it is ready for
     * execution (validated already)
     */
    public void populateValues(DialogContext dc, int formatType)
    {
        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            if(field.isAvailable(dc))
                field.populateValue(dc, formatType);
        }

        if(director != null)
        {
            DialogField field = director.getNextActionsField();
            if(field != null)
                field.populateValue(dc, formatType);
        }

        if(dc.isInitialEntry())
        {
            if(formatType == DialogField.DISPLAY_FORMAT && haveInitialPopulateForDisplayListeners)
            {
                for(int i = 0; i < initialPopulateForDisplayListeners.size(); i++)
                    ((DialogInitialPopulateForDisplayListener) initialPopulateForDisplayListeners.get(i)).populateInitialDialogValuesForDisplay(dc);
            }

            if(formatType == DialogField.SUBMIT_FORMAT && haveInitialPopulateForSubmitListeners)
            {
                for(int i = 0; i < initialPopulateForSubmitListeners.size(); i++)
                    ((DialogInitialPopulateForSubmitListener) initialPopulateForSubmitListeners.get(i)).populateInitialDialogValuesForSubmit(dc);
            }

            if(haveInitialPopulateListeners)
            {
                for(int i = 0; i < initialPopulateListeners.size(); i++)
                    ((DialogInitialPopulateListener) initialPopulateListeners.get(i)).populateInitialDialogValues(dc, formatType);
            }
        }

        if(formatType == DialogField.DISPLAY_FORMAT && havePopulateForDisplayListeners)
        {
            for(int i = 0; i < populateForDisplayListeners.size(); i++)
                ((DialogPopulateForDisplayListener) populateForDisplayListeners.get(i)).populateDialogValuesForDisplay(dc);
        }

        if(formatType == DialogField.SUBMIT_FORMAT && havePopulateForSubmitListeners)
        {
            for(int i = 0; i < populateForSubmitListeners.size(); i++)
                ((DialogPopulateForSubmitListener) populateForSubmitListeners.get(i)).populateDialogValuesForSubmit(dc);
        }

        if(havePopulateListeners)
        {
            for(int i = 0; i < populateListeners.size(); i++)
                ((DialogPopulateListener) populateListeners.get(i)).populateDialogValues(dc, formatType);
        }
    }

    /**
     * Checks each field to make sure the state of it needs to be changed or not
     * usually based on Conditionals.
     *
     * <b>IMPORTANT</b>: If any changes are made in this class, make sure
     * that they are also reflected in QuerySelectDialog and QueryBuilderDialog classes
     * which extend this class but they overwrite this method and doesn't make a call
     * to this method.
     */
    public void makeStateChanges(DialogContext dc, int stage)
    {
        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            field.makeStateChanges(dc, stage);
        }
        DialogDirector director = getDirector();
        if(director != null)
            director.makeStateChanges(dc, stage);

        if(stage == DialogContext.STATECALCSTAGE_INITIAL && haveStateBeforeValidationListeners)
        {
            for(int i = 0; i < stateBeforeValidationListeners.size(); i++)
                ((DialogStateBeforeValidationListener) stateBeforeValidationListeners.get(i)).makeDialogStateChangesBeforeValidation(dc);
        }

        if(stage == DialogContext.STATECALCSTAGE_FINAL && haveStateAfterValidationListeners)
        {
            for(int i = 0; i < stateAfterValidationListeners.size(); i++)
                ((DialogStateAfterValidationListener) stateAfterValidationListeners.get(i)).makeDialogStateChangesChangesAfterValidation(dc);
        }

        if(haveStateListeners)
        {
            for(int i = 0; i < stateListeners.size(); i++)
                ((DialogStateListener) stateListeners.get(i)).makeDialogStateChanges(dc, stage);
        }
    }

    public DialogExecuteHandler createOnExecute()
    {
        return new DialogExecuteDefaultHandler();
    }

    public void addOnExecute(DialogExecuteHandler handler)
    {
        executeHandlers.add(handler);
        addListener(handler); // see if there are any other interfaces implemented by this handler
    }

    public DialogExecuteHandlers getExecuteHandlers()
    {
        return executeHandlers;
    }

    /**
     * Execute the actions of the dialog
     * @param writer stream for dialog execution output
     * @param dc dialog context
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        if(dc.executeStageHandled())
            return;

        if(executeHandlers.size() > 0)
            executeHandlers.handleDialogExecute(writer, dc);
        else
            dc.renderDebugPanels(writer);

        dc.setExecuteStageHandled(true);
    }

    public DialogNextActionProvider getNextActionProvider()
    {
        return nextActionProvider;
    }

    public void addNextActionProvider(DialogNextActionProvider nextActionProvider)
    {
        this.nextActionProvider = nextActionProvider;
    }

    public void handlePostExecute(Writer writer, DialogContext dc) throws IOException
    {
        dc.setExecuteStageHandled(true);
        dc.performDefaultRedirect(writer, null);
    }

    public void handlePostExecute(Writer writer, DialogContext dc, String redirect) throws IOException
    {
        dc.setExecuteStageHandled(true);
        dc.performDefaultRedirect(writer, redirect);
    }

    public void handlePostExecuteException(Writer writer, DialogContext dc, String message, Exception e) throws IOException
    {
        dc.setExecuteStageHandled(true);
        log.error(message, e);
        dc.setRedirectDisabled(true);
        dc.performDefaultRedirect(writer, null);
        writer.write(message + e.toString());
    }

    /**
     * Create a dialog context for this dialog
     *
     * @param skin      dialog skin
     *
     * @return DialogContext
     */
    public DialogContext createContext(NavigationContext nc, DialogSkin skin)
    {
        if(!dialogFlags.flagIsSet(DialogFlags.CONTENTS_FINALIZED))
            finalizeContents(nc);

        DialogContext dc = null;
        try
        {
            dc = (DialogContext) dialogContextClass.newInstance();
        }
        catch(Exception e)
        {
            dc = new DialogContext();
        }
        dc.initialize(nc, this, skin);
        return dc;
    }

    /**
     * Initially populates the dialog with values in display format and then calculates the state of the dialog.
     * If the dialog is in execute mode, the values are then formatted for submittal.
     *
     * @param dc dialog context
     */
    public void prepareContext(DialogContext dc)
    {
        populateValues(dc, DialogField.DISPLAY_FORMAT);
        dc.calcState();
        // validated and the dialog is ready for execution
        if(dc.inExecuteMode())
        {
            dc.persistValues();
            populateValues(dc, DialogField.SUBMIT_FORMAT);
        }
    }

    /**
     * Create and write the HTML for the dialog
     *
     * @param writer                    stream to write the HTML
     * @param dc                        dialog context
     * @param contextPreparedAlready    flag to indicate whether or not the context has been prepared
     */
    public void render(Writer writer, DialogContext dc, boolean contextPreparedAlready) throws IOException, DialogExecuteException
    {
        if(!contextPreparedAlready)
            prepareContext(dc);

        if(dc.inExecuteMode())
        {
            boolean debug = debugFlags.flagIsSet(DialogDebugFlags.SHOW_FIELD_DATA);
            if(debug)
                dc.setRedirectDisabled(true);

            switch(loop.getValueIndex())
            {
                case DialogLoopStyle.NONE:
                    if(debug)
                        dc.renderDebugPanels(writer);
                    else
                        execute(writer, dc);
                    break;

                case DialogLoopStyle.APPEND:
                    if(debug)
                        dc.renderDebugPanels(writer);
                    else
                        execute(writer, dc);
                    writer.write(loop.getLoopSeparator());
                    dc.getSkin().renderHtml(writer, dc);
                    break;

                case DialogLoopStyle.PREPEND:
                    dc.getSkin().renderHtml(writer, dc);
                    writer.write(loop.getLoopSeparator());
                    if(debug)
                        dc.renderDebugPanels(writer);
                    else
                        execute(writer, dc);
                    break;
            }
        }
        else
        {
            dc.getSkin().renderHtml(writer, dc);
        }
    }

    public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
    {
        render(writer, dc.getNavigationContext(), theme, flags);
    }

    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        DialogContext dc = createContext(nc, theme.getDefaultDialogSkin());
        try
        {
            render(writer, dc, false);
        }
        catch (DialogExecuteException e)
        {
            log.error("Dialog execute error", e);
            writer.write(e.toString());
        }
    }

    public File generateDialogContextBean(File destDir, String pkgPrefix) throws IOException
    {
        StringBuffer importsCode = new StringBuffer();
        StringBuffer membersCode = new StringBuffer();

        Set modulesImported = new HashSet();

        DialogFields fields = getFields();
        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            DialogContextBeanMemberInfo mi = field.getDialogContextBeanMemberInfo();
            if(mi != null)
            {
                String[] importModules = mi.getImportModules();
                if(importModules != null)
                {
                    for(int m = 0; m < importModules.length; m++)
                    {
                        String module = importModules[m];
                        if(!modulesImported.contains(module))
                        {
                            modulesImported.add(module);
                            importsCode.append("import " + module + ";\n");
                        }
                    }
                }
                membersCode.append(mi.getCode());
                membersCode.append("\n");
            }
            DialogFields childrenFields = field.getChildren();
            if (childrenFields != null && childrenFields.size() > 0)
            {
                for (int j=0; j < childrenFields.size() ; j++)
                {
                    DialogField child = childrenFields.get(j);
                    DialogContextBeanMemberInfo miChild = child.getDialogContextBeanMemberInfo();
                    if(mi != null)
                    {
                        String[] importModules = miChild.getImportModules();
                        if(importModules != null)
                        {
                            for(int m = 0; m < importModules.length; m++)
                            {
                                String module = importModules[m];
                                if(!modulesImported.contains(module))
                                {
                                    modulesImported.add(module);
                                    importsCode.append("import " + module + ";\n");
                                }
                            }
                        }
                        membersCode.append(miChild.getCode());
                        membersCode.append("\n");
                    }
                }
            }
        }

        String className = TextUtils.xmlTextToJavaIdentifier(getName(), true);

        String packageName = pkgPrefix + "." + TextUtils.xmlTextToJavaPackageName(getNameSpace().getNameSpaceId());

        StringBuffer code = new StringBuffer();
        code.append("\n/* this file is generated by com.netspective.sparx.form.Dialog.getSubclassedDialogContextCode(), do not modify (you can extend it, though) */\n\n");
        code.append("package " + packageName + ";\n\n");
        if(importsCode.length() > 0)
            code.append(importsCode.toString());
        code.append("import com.netspective.sparx.form.*;\n");
        code.append("import com.netspective.sparx.form.field.*;\n");
        code.append("import com.netspective.sparx.form.field.type.*;\n\n");
        code.append("public class " + className + "Context\n");
        code.append("{\n");
        code.append("    public static final String DIALOG_ID = \""+ getQualifiedName() +"\";\n");
        code.append("    private DialogContext dialogContext;\n");
        code.append("    private DialogContext.DialogFieldStates fieldStates;\n\n");
        code.append("    public "+ className +"Context(DialogContext dc)\n");
        code.append("    {\n");
        code.append("        this.dialogContext = dc;\n");
        code.append("        this.fieldStates = dc.getFieldStates();\n");
        code.append("    }\n\n");
        code.append("    public DialogContext getDialogContext() { return dialogContext; }\n\n");
        code.append(membersCode.toString());
        code.append("}\n");

        File javaFilePath = new File(destDir, packageName.replace('.', '/'));
        javaFilePath.mkdirs();

        File javaFile = new File(javaFilePath, className + "Context.java");

        Writer writer = new java.io.FileWriter(javaFile);
        writer.write(code.toString());
        writer.close();

        return javaFile;
    }

    /**
     * Indicates whether not the dialog needs validation
     *
     * @param dc dialog context
     * @return boolean
     */
    public boolean needsValidation(DialogContext dc)
    {
        int validateFieldsCount = 0;

        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            if(field.isAvailable(dc) && field.needsValidation(dc))
                validateFieldsCount++;
        }

        return validateFieldsCount > 0 ? true : false;
    }

    /**
     * Checks whether or not the dailog is valid for the execution
     *
     * @param dc dialog context
     * @return boolean
     */
    public boolean isValid(DialogContext dc)
    {
        DialogValidationContext dvc = dc.getValidationContext();
        int valStage = dvc.getValidationStage();
        if(valStage == DialogValidationContext.VALSTAGE_PERFORMED_SUCCEEDED || valStage == DialogValidationContext.VALSTAGE_IGNORE)
            return true;
        if(valStage == DialogValidationContext.VALSTAGE_PERFORMED_FAILED)
            return false;

        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            if((field.isAvailable(dc) && !field.isInputHidden(dc)))
                field.validate(dvc);
        }

        if(haveValidationListeners)
        {
            for(int i = 0; i < validationListeners.size(); i++)
                ((DialogValidateListener) validationListeners.get(i)).validateDialog(dvc);
        }

        boolean isValid = dvc.isValid();
        dvc.setValidationStage(isValid ? DialogValidationContext.VALSTAGE_PERFORMED_SUCCEEDED : DialogValidationContext.VALSTAGE_PERFORMED_FAILED);
        return isValid;
    }

    protected void renderFormattedExceptionMessage(Writer writer, Exception e) throws IOException
    {
        StringWriter stackTraceWriter = new StringWriter();
        PrintWriter stackTrace = new PrintWriter(stackTraceWriter);
        e.printStackTrace(stackTrace);
        writer.write("<div class='textbox'>"+ Main.getAntVersion() +"<p><pre>");
        writer.write(stackTraceWriter.toString());
        writer.write("</pre>");
    }

    public DialogListener createListener()
    {
        return new DialogListenerPlaceholder();
    }

    private void registerListener(List listeners, DialogListener listener)
    {
        if(! listeners.contains(listener))
            listeners.add(listener);
    }

    public void addListener(DialogListener listener)
    {
        if(listener instanceof DialogInitialPopulateForDisplayListener)
        {
            haveInitialPopulateForDisplayListeners = true;
            registerListener(initialPopulateForDisplayListeners, listener);
        }

        if(listener instanceof DialogInitialPopulateForSubmitListener)
        {
            haveInitialPopulateForSubmitListeners = true;
            registerListener(initialPopulateForSubmitListeners, listener);
        }

        if(listener instanceof DialogInitialPopulateListener)
        {
            haveInitialPopulateListeners = true;
            registerListener(initialPopulateListeners, listener);
        }

        if(listener instanceof DialogPopulateForDisplayListener)
        {
            havePopulateForDisplayListeners = true;
            registerListener(populateForDisplayListeners, listener);
        }

        if(listener instanceof DialogPopulateForSubmitListener)
        {
            havePopulateForSubmitListeners = true;
            registerListener(populateForSubmitListeners, listener);
        }

        if(listener instanceof DialogPopulateListener)
        {
            havePopulateListeners = true;
            registerListener(populateListeners, listener);
        }

        if(listener instanceof DialogStateAfterValidationListener)
        {
            haveStateAfterValidationListeners = true;
            registerListener(stateAfterValidationListeners, listener);
        }

        if(listener instanceof DialogStateBeforeValidationListener)
        {
            haveStateBeforeValidationListeners = true;
            registerListener(stateBeforeValidationListeners, listener);
        }

        if(listener instanceof DialogStateListener)
        {
            haveStateListeners = true;
            registerListener(stateListeners, listener);
        }

        if(listener instanceof DialogValidateListener)
        {
            haveValidationListeners = true;
            registerListener(validationListeners, listener);
        }

        if(listener instanceof DialogExecuteHandler)
            executeHandlers.add((DialogExecuteHandler) listener);
    }

    public List getInitialPopulateForDisplayListeners()
    {
        return initialPopulateForDisplayListeners;
    }

    public List getInitialPopulateForSubmitListeners()
    {
        return initialPopulateForSubmitListeners;
    }

    public List getInitialPopulateListeners()
    {
        return initialPopulateListeners;
    }

    public List getPopulateForDisplayListeners()
    {
        return populateForDisplayListeners;
    }

    public List getPopulateForSubmitListeners()
    {
        return populateForSubmitListeners;
    }

    public List getPopulateListeners()
    {
        return populateListeners;
    }

    public List getStateAfterValidationListeners()
    {
        return stateAfterValidationListeners;
    }

    public List getStateBeforeValidationListeners()
    {
        return stateBeforeValidationListeners;
    }

    public List getStateListeners()
    {
        return stateListeners;
    }

    public List getValidationListeners()
    {
        return validationListeners;
    }
}
