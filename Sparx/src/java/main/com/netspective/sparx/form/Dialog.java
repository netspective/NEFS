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
 * $Id: Dialog.java,v 1.11 2003-05-16 21:23:14 shahid.shah Exp $
 */

package com.netspective.sparx.form;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.form.field.type.GridField;
import com.netspective.sparx.form.field.type.CompositeField;
import com.netspective.sparx.form.field.type.SeparatorField;
import com.netspective.sparx.panel.AbstractPanel;
import com.netspective.sparx.theme.Theme;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.text.TextUtils;

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
public class Dialog extends AbstractPanel
{
    private static final Log log = LogFactory.getLog(Dialog.class);

    /**
     * Request parameter which indicates whether or not the dialog should be automatically executed when it is being loaded
     */
    public static final String PARAMNAME_AUTOEXECUTE = "_d_exec";
    public static final String PARAMNAME_OVERRIDE_SKIN = "_d_skin";
    public static final String PARAMNAME_DIALOGPREFIX = "_d.";
    public static final String PARAMNAME_CONTROLPREFIX = "_dc.";
    public static final String PARAMNAME_DIALOGQNAME = "_d.dialog_qname";

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

    /*
	   the debug flags when first passed in (start of dialog, run seq == 1)
	   is passed using the parameter "debug_flags" (INITIAL). When the dialog is
	   in run sequence > 1 (after submit) the data command is passed in as a
	   hidden "pass-thru" variable with the suffix PARAMNAME_DEBUG_FLAGS
    */
    public static final String PARAMNAME_DEBUG_FLAGS_INITIAL = "debug_flags";
    public static final String PARAMNAME_DEBUG_FLAGS = ".debug_flags";
    /*
	   the Data Command when first passed in (start of dialog, run seq == 1)
	   is passed using the parameter "data_cmd" (INITIAL). When the dialog is
	   in run sequence > 1 (after submit) the data command is passed in as a
	   hidden "pass-thru" variable with the suffix PARAMNAME_DATA_CMD
    */
    public static final String PARAMNAME_DATA_CMD_INITIAL = "data_cmd";
    public static final String PARAMNAME_DATA_CMD = ".data_cmd";

    public static final String translateNameForMapKey(String name)
    {
        return name != null ? name.toUpperCase() : null;
    }

    private DialogFields fields;
    private DialogFlags dialogFlags;
    private DialogDebugFlags debugFlags;
    private DialogLoopStyle loop = new DialogLoopStyle(DialogLoopStyle.APPEND);
    private DialogDirector director = createDirector();
    private DialogsPackage nameSpace;
    private String name;
    private String htmlFormName;
    private int layoutColumnsCount = 1;
    private String[] retainRequestParams;
    private Class dcClass = DialogContext.class;
    private ValueSource includeJSFile = ValueSource.NULL_VALUE_SOURCE;

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

    public Class getDcClass()
    {
        return dcClass;
    }

    public void setDcClass(Class dcClass)
    {
        this.dcClass = dcClass;
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

    public String getValuesRequestAttrName()
    {
        return "dialog-" + htmlFormName + "-field-values";
    }

    public String getDataCmdParamName()
    {
        return PARAMNAME_DIALOGPREFIX + htmlFormName + PARAMNAME_DATA_CMD;
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

    public ValueSource getIncludeJSFile()
    {
        return includeJSFile;
    }

    public void setDialogContextClass(Class cls)
    {
        dcClass = cls;
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

    /**
     * Loops through each dialog field and finalize them.
     */
    public void finalizeContents(ServletContext context)
    {
        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            field.finalizeContents();

            if(field.requiresMultiPartEncoding())
                dialogFlags.setFlag(DialogFlags.ENCTYPE_MULTIPART_FORMDATA);

            if(field.getFlags().flagIsSet(DialogField.Flags.COLUMN_BREAK_BEFORE | DialogField.Flags.COLUMN_BREAK_AFTER))
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
    }

    /**
     * Execute the actions of the dialog
     * @param writer output stream for error message
     * @param dc dialog context
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        if(! dc.executeStageHandled())
        {
            dc.renderDebugPanels(writer);
            dc.setExecuteStageHandled(true);
        }
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
        log.error(e);
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
            finalizeContents(nc.getServletContext());

        DialogContext dc = null;
        try
        {
            dc = (DialogContext) dcClass.newInstance();
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
    protected void render(Writer writer, DialogContext dc, boolean contextPreparedAlready) throws IOException, DialogExecuteException
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
        DialogContext dc = createContext(nc, theme.getDialogSkin());
        try
        {
            render(nc.getResponse().getWriter(), dc, false);
        }
        catch (DialogExecuteException e)
        {
            log.error(e);
            writer.write(e.toString());
        }
    }

    public String getSubclassedDialogContextCode(String pkgPrefix)
    {
        StringBuffer importsCode = new StringBuffer();
        StringBuffer membersCode = new StringBuffer();

        Set modulesImported = new HashSet();

        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = fields.get(i);
            DialogContextMemberInfo mi = field.getDialogContextMemberInfo();
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
        }

        StringBuffer code = new StringBuffer();
        code.append("\n/* this file is generated by com.netspective.sparx.form.Dialog.getSubclassedDialogContextCode(), do not modify (you can extend it, though) */\n\n");
        code.append("package " + pkgPrefix + getNameSpace().getNameSpaceId() + ";\n\n");
        if(importsCode.length() > 0)
            code.append(importsCode.toString());
        code.append("import com.netspective.sparx.form.*;\n\n");
        code.append("public class " + TextUtils.xmlTextToJavaIdentifier(getName(), true) + "Context extends com.netspective.sparx.form.DialogContext\n");
        code.append("{\n");
        code.append(membersCode.toString());
        code.append("}\n");
        return code.toString();
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

        boolean isValid = dvc.isValid();
        dvc.setValidationStage(isValid ? DialogValidationContext.VALSTAGE_PERFORMED_SUCCEEDED : DialogValidationContext.VALSTAGE_PERFORMED_FAILED);
        return isValid;
    }
}
