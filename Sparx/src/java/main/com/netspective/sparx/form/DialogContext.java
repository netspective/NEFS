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
 * $Id: DialogContext.java,v 1.23 2003-08-28 13:02:27 shahid.shah Exp $
 */

package com.netspective.sparx.form;

import java.io.IOException;
import java.io.Writer;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.netspective.sparx.value.BasicDbHttpServletValueContext;
import com.netspective.sparx.value.source.DialogFieldValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.panel.HtmlLayoutPanel;
import com.netspective.sparx.panel.HtmlPanelsStyleEnumeratedAttribute;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.console.panel.presentation.dialogs.DialogContextAttributesPanel;
import com.netspective.sparx.console.panel.presentation.dialogs.DialogContextFieldStatesPanel;
import com.netspective.sparx.console.panel.presentation.dialogs.DialogContextFieldStatesClassesPanel;
import com.netspective.sparx.console.panel.presentation.HttpRequestParametersPanel;
import com.netspective.sparx.command.AbstractHttpServletCommand;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.text.TextUtils;

/**
 * A dialog context functions as the controller of the dialog, tracking and managing field state and field data.
 * A new <code>DialogContext</code> object is created for each HTTP request coming from a JSP even though
 * the dialogs are cached.
 * <p>
 * <img src="doc-files/dialogcontext-1.jpg"/>
 * <p>
 * For most occasions, the default <code>DialogContext</code> object should be sufficient but
 * for special curcumstances when the behavior of a dialog needs to be modified, the <code>DialogContext</code> class
 * can be extended (inherited) to create a customzied dialog context.
 *
 */
public class DialogContext extends BasicDbHttpServletValueContext implements HtmlPanelValueContext
{
    private static final Log log = LogFactory.getLog(DialogContext.class);

    /**
     * The name of a URL parameter, if present, that will be used as the redirect string after dialog execution
     */
    static public final String DEFAULT_REDIRECT_PARAM_NAME = "redirect";

    /* if dialog fields need to be pre-populated (before the context is created)
     * then a java.util.Map can be created and stored in the request attribute
     * called "dialog-field-values". All keys in the map are field names, and values
     * are the field values
     */
    static public final String DIALOG_FIELD_VALUES_ATTR_NAME = "dialog-field-values";

    /* after the dialog context is created, it is automatically stored as
     * request parameter with this name so that it is available throughout the
     * request
     */
    static public final String DIALOG_CONTEXT_ATTR_NAME = "dialog-context";

    static public final ValueSource dialogFieldStoreValueSource = new DialogFieldValueSource();

    /**
     * Unknown dialog stage
     */
    static public final char DIALOGMODE_UNKNOWN = ' ';
    /**
     * Dialog is in input stage
     */
    static public final char DIALOGMODE_INPUT = 'I';
    /**
     * Dialog is in validation stage
     */
    static public final char DIALOGMODE_VALIDATE = 'V';
    /**
     * Dialog is in execution stage
     */
    static public final char DIALOGMODE_EXECUTE = 'E';

    /**
     * The following constants are setup as flags but are most often used as enumerations. We use powers of two to
     * allow them to be used either as enums or as flags.
     */

    /**
     * Used for indicating that the calculation of a dialog's stage is starting
     */
    static public final int STATECALCSTAGE_INITIAL = 0;
    /**
     * Used for indicating that the calculation of a dialog's stage is ending
     */
    static public final int STATECALCSTAGE_FINAL = 1;

    public class DialogFieldStates
    {
        private Map statesByQualifiedName = new HashMap();

        public DialogFieldStates()
        {
        }

        public Map getStatesByQualifiedName()
        {
            return statesByQualifiedName;
        }

        public void addState(DialogField.State state)
        {
            statesByQualifiedName.put(state.getField().getQualifiedName(), state);
        }

        public DialogField.State getState(DialogField field)
        {
            DialogField.State state = (DialogField.State) statesByQualifiedName.get(field.getQualifiedName());
            if(state == null)
            {
                state = field.constructStateInstance(DialogContext.this);
                statesByQualifiedName.put(field.getQualifiedName(), state);
            }

            return state;
        }

        public DialogField.State getState(String qualifiedName)
        {
            DialogField field = dialog.getFields().getByQualifiedName(qualifiedName);
            if(field == null)
                throw new RuntimeException("Field '"+ qualifiedName +"' not found in dialog '"+ dialog.getName() +"'.");
            else
                return getState(field);
        }

        public DialogField.State getState(String qualifiedName, DialogField.State defaultValue)
        {
            DialogField field = dialog.getFields().getByQualifiedName(qualifiedName);
            if(field == null)
                return defaultValue;
            else
                return getState(field);
        }

        public void persistValues()
        {
            Iterator i = statesByQualifiedName.values().iterator();
            while(i.hasNext())
            {
                DialogField.State state = (DialogField.State) i.next();
                state.persistValue();
            }
        }

        /**
         * Given a map of values, assign the value to each field. Each key in the map is
         * a case-sensitive field name (should be the same fully qualified name as the field)
         * and the value is either a String[] or an Object. If the value is an Object, then
         * the toString() method will be called on the object to get a single value. If the
         * value is a String[] then the assignment will be made directly (by reference).
         */
        public void assignFieldValues(DialogContext dc, Map values)
        {
            DialogFields dialogFields = dc.getDialog().getFields();
            for(Iterator i = values.entrySet().iterator(); i.hasNext();)
            {
                String fieldName = (String) ((Map.Entry) i.next()).getKey();
                DialogField field = dialogFields.getByQualifiedName(fieldName);
                DialogField.State state = getState(field);
                state.getValue().setValue(((Map.Entry) i.next()).getValue());
            }
        }

        public void setStateFlag(DialogField field, long flag)
        {
            DialogField.State state = getState(field);
            state.getStateFlags().setFlag(flag);
            DialogFields children = field.getChildren();
            if(children != null)
            {
                for(int i = 0; i < children.size(); i++)
                    setStateFlag(children.get(i), flag);
            }
        }

        public void clearStateFlag(DialogField field, long flag)
        {
            DialogField.State state = getState(field);
            state.getStateFlags().clearFlag(flag);
            DialogFields children = field.getChildren();
            if(children != null)
            {
                for(int i = 0; i < children.size(); i++)
                    clearStateFlag(children.get(i), flag);
            }
        }

        public void importFromXml(Element parent)
        {
            NodeList children = parent.getChildNodes();
            for(int n = 0; n < children.getLength(); n++)
            {
                Node node = children.item(n);
                if(node.getNodeName().equals("field-state"))
                {
                    Element fieldElem = (Element) node;
                    String fieldName = fieldElem.getAttribute("name");
                    DialogField.State state = getState(fieldName);
                    if(state != null)
                        state.importFromXml(fieldElem);
                }
            }
        }

        public void exportToXml(Element parent)
        {
            Iterator i = statesByQualifiedName.values().iterator();
            while(i.hasNext())
            {
                DialogField.State state = (DialogField.State) i.next();
                state.exportToXml(parent);
            }
        }

        public int size()
        {
            return statesByQualifiedName.size();
        }

        public String toString()
        {
            return statesByQualifiedName.toString();
        }
    }

    private int panelRenderFlags;
    private DialogFieldStates fieldStates = new DialogFieldStates();
    private DialogFieldStates initialFieldStates = new DialogFieldStates();
    private DialogValidationContext validationContext = new DialogValidationContext(this);
    private boolean resetContext;
    private String transactionId;
    private Dialog dialog;
    private DialogSkin skin;
    private char activeMode;
    private char nextMode;
    private int runSequence;
    private int execSequence;
    private String originalReferer;
    private boolean executeHandled;
    private DialogDebugFlags debugFlags = new DialogDebugFlags();
    private DialogPerspectives perspectives = new DialogPerspectives();
    private String[] retainReqParams;
    private boolean redirectDisabled;
    private String initialContextXml;
    private boolean cancelButtonPressed;

    public DialogContext()
    {
    }

    public HtmlPanel getPanel()
    {
        return dialog;
    }

    public int getPanelRenderFlags()
    {
        return panelRenderFlags;
    }

    public boolean isMinimized()
    {
        return false;
    }

    public void setPanelRenderFlags(int panelRenderFlags)
    {
        this.panelRenderFlags = panelRenderFlags;
    }

    /**
     * Initializes the dialog context object. Called by the <code>Dialog</code> after creating the context.
     *
     * @param aDialog the Dialog object which this context is associated with
     * @param aSkin the DialogSkin object of the dialog
     */
    public void initialize(NavigationContext nc, Dialog aDialog, DialogSkin aSkin)
    {
        super.initialize(nc);

        HttpServletRequest request = nc.getHttpRequest();
        nc.getRequest().setAttribute(DIALOG_CONTEXT_ATTR_NAME, this);

        String overrideSkin = request.getParameter(Dialog.PARAMNAME_OVERRIDE_SKIN);
        if(overrideSkin != null)
            aSkin = nc.getActiveTheme().getDialogSkin(overrideSkin);

        dialog = aDialog;
        skin = aSkin == null ? nc.getActiveTheme().getDefaultDialogSkin() : aSkin;

        activeMode = DIALOGMODE_UNKNOWN;
        nextMode = DIALOGMODE_UNKNOWN;
        String runSeqValue = request.getParameter(dialog.getRunSequenceParamName());
        if(runSeqValue != null)
            runSequence = new Integer(runSeqValue).intValue();
        else
            runSequence = 1;

        String execSeqValue = request.getParameter(dialog.getExecuteSequenceParamName());
        if(execSeqValue != null)
            execSequence = new Integer(execSeqValue).intValue();
        else
            execSequence = 0; // we have not executed at all yet

        String resetContext = request.getParameter(dialog.getResetContextParamName());
        if(resetContext != null)
        {
            runSequence = 1;
            execSequence = 0;
            this.resetContext = true;
        }
        String initialContextValues = request.getParameter(dialog.getInitialContextParamName());
        if (initialContextValues != null && initialContextValues.length() > 0)
        {
            initialContextXml = URLDecoder.decode(initialContextValues);

            try
            {
                createInitialStateFields();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        String dataCmdStr = null;
        String debugFlagsStr = null;

        if(runSequence == 1)
        {
            originalReferer = request.getHeader("Referer");
            try
            {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((dialog.getHtmlFormName() + new Date().toString()).getBytes());
                transactionId = md.digest().toString();
            }
            catch(NoSuchAlgorithmException e)
            {
                transactionId = "No MessageDigest Algorithm found!";
                log.error("Unable to create transation id", e);
            }
            dataCmdStr = (String) request.getAttribute(Dialog.PARAMNAME_PERSPECTIVE_INITIAL);
            if(dataCmdStr == null)
                dataCmdStr = request.getParameter(Dialog.PARAMNAME_PERSPECTIVE_INITIAL);
            debugFlagsStr = (String) request.getAttribute(Dialog.PARAMNAME_DEBUG_FLAGS_INITIAL);
            if(debugFlagsStr == null)
                debugFlagsStr = request.getParameter(Dialog.PARAMNAME_DEBUG_FLAGS_INITIAL);
        }
        else
        {
            originalReferer = request.getParameter(dialog.getOriginalRefererParamName());
            transactionId = request.getParameter(dialog.getTransactionIdParamName());
            dataCmdStr = request.getParameter(dialog.getDataCmdParamName());
            debugFlagsStr = request.getParameter(dialog.getDebugFlagsParamName());
        }

        perspectives.setValue(dataCmdStr);
        debugFlags.setValue(debugFlagsStr);

        // check to see if the dialog was submitted using the cancel button
        String cancelValue = request.getParameter("cancelButton");
        if (cancelValue != null && cancelValue.length() > 0)
        {
            setCancelButtonPressed(true);
        }

        ValueSource ncRetainVS = nc.getActivePage().getRetainParams();
        if(ncRetainVS != null)
        {
            String ncRetain = ncRetainVS.getTextValue(this);
            if(ncRetain != null && ncRetain.length() > 0)
                addRetainRequestParams(TextUtils.split(ncRetain, ",", true));
        }

        nc.setDialogContext(this);
    }

    /**
     * Checks to see if the cancel button was pressed for dialog submittal
     * @return true if the dialog was submitted using the cancel cutton
     */
    public boolean isCancelButtonPressed()
    {
        return cancelButtonPressed;
    }

    /**
     * Sets the flag for the cancel button press
     * @param cancelButtonPressed
     */
    public void setCancelButtonPressed(boolean cancelButtonPressed)
    {
        this.cancelButtonPressed = cancelButtonPressed;
    }

    public DialogValidationContext getValidationContext()
    {
        return validationContext;
    }

    public void persistValues()
    {
        fieldStates.persistValues();
    }

    public boolean isRedirectDisabled()
    {
        return redirectDisabled;
    }

    public void setRedirectDisabled(boolean value)
    {
        redirectDisabled = value;
    }

    /**
     * Return the next action url (where to redirect after execute) based on user input or other method.
     */
    public String getNextActionUrl(String defaultUrl)
    {
        return dialog.getNextActionUrl(this, defaultUrl);
    }

    public void performDefaultRedirect(Writer writer, String redirect) throws IOException
    {
        ServletRequest request = getRequest();
        String redirectToUrl = redirect != null ? redirect : request.getParameter(DEFAULT_REDIRECT_PARAM_NAME);
        if(redirectToUrl == null)
        {
            redirectToUrl = request.getParameter(dialog.getPostExecuteRedirectUrlParamName());
            if(redirectToUrl == null)
                redirectToUrl = getNextActionUrl(getOriginalReferer());
        }

        if(redirectDisabled || redirectToUrl == null)
        {
            writer.write("<p><b>Redirect is disabled</b>.");
            writer.write("<br><code>redirect</code> method parameter is <code>"+ redirect +"</code>");
            writer.write("<br><code>redirect</code> URL parameter is <code>"+ request.getParameter(DEFAULT_REDIRECT_PARAM_NAME) +"</code>");
            writer.write("<br><code>redirect</code> form field is <code>"+ request.getParameter(dialog.getPostExecuteRedirectUrlParamName()) +"</code>");
            writer.write("<br><code>getNextActionUrl</code> method result is <code>"+ getNextActionUrl(null) +"</code>");
            writer.write("<br><code>original referer</code> url is <code>"+ getOriginalReferer() +"</code>");
            writer.write("<p><font color=red>Would have redirected to <code>"+ redirectToUrl +"</code>.</font>");
            return;
        }

        HttpServletResponse response = (HttpServletResponse) getResponse();
        if(response.isCommitted())
            skin.renderRedirectHtml(writer, this, redirectToUrl);
        else
            response.sendRedirect(redirectToUrl);
    }

    /**
     * Check the dataCmdCondition against each available data command and see if it's set in the condition; if the
     * data command is set in the condition, then check to see if our data command for that command id is set. If any
     * of the data commands in dataCommandCondition match our current dataCmd, return true.
     *
     * @param perspectives the data command condition
     * @return boolean True if the data commands in the passes in condition matches the current dialog data command
     */
    public boolean matchesPerspective(int perspectives)
    {
        if(perspectives == DialogPerspectives.NONE || this.perspectives.getFlags() == DialogPerspectives.NONE)
            return false;

        int lastDataCmd = DialogPerspectives.LAST;
        for(int i = 1; i <= lastDataCmd; i *= 2)
        {
            // if the dataCmdCondition's dataCmd i is set, it means we need to check our dataCmd to see if we're set
            if((perspectives & i) != 0 && this.perspectives.flagIsSet(i))
                return true;
        }

        // if we get to here, nothing matched
        return false;
    }

    /**
     * Returns a string useful for displaying a unique Id for this DialogContext
     * in a log or monitor file.
     *
     * @return String Log id
     */
    public String getLogId()
    {
        return dialog.getHtmlFormName() + " (" + transactionId + ")";
    }

    /**
     * Using a Document or element that was serialized using the exportToXml method in this class,
     * reconstruct the DialogFieldStates hash map. This is basically a data deserialization method.
     *
     * @param parent dialog context element's parent
     */
    public void importFromXml(Element parent)
    {
        NodeList dcList = parent.getElementsByTagName("dialog-context");
        if(dcList.getLength() > 0)
        {
            Element dcElem = (Element) dcList.item(0);
            fieldStates.importFromXml(dcElem);
        }
    }

    static public void exportParamToXml(Element parent, String name, String[] values)
    {
        Document doc = parent.getOwnerDocument();
        Element fieldElem = doc.createElement("request-param");
        fieldElem.setAttribute("name", name);
        if(values != null && values.length > 1)
        {
            fieldElem.setAttribute("value-type", "strings");
            Element valuesElem = doc.createElement("values");
            for(int i = 0; i < values.length; i++)
            {
                Element valueElem = doc.createElement("value");
                valueElem.appendChild(doc.createTextNode(values[i]));
                valuesElem.appendChild(valueElem);
            }
            fieldElem.appendChild(valuesElem);
            parent.appendChild(fieldElem);
        }
        else if(values != null)
        {
            fieldElem.setAttribute("value-type", "string");
            Element valueElem = doc.createElement("value");
            valueElem.appendChild(doc.createTextNode(values[0]));
            fieldElem.appendChild(valueElem);
            parent.appendChild(fieldElem);
        }
    }

    public void exportToXml(Element parent)
    {
        ServletRequest request = getRequest();
        Element dcElem = parent.getOwnerDocument().createElement("dialog-context");
        dcElem.setAttribute("name", dialog.getName());
        dcElem.setAttribute("transaction", transactionId);
        fieldStates.exportToXml(dcElem);

        Set retainedParams = null;
        if(retainReqParams != null)
        {
            retainedParams = new HashSet();
            for(int i = 0; i < retainReqParams.length; i++)
            {
                String paramName = retainReqParams[i];
                String[] paramValues = request.getParameterValues(paramName);
                if(paramValues != null)
                    exportParamToXml(dcElem, paramName, paramValues);
                retainedParams.add(paramName);
            }
        }
        boolean retainedAnyParams = retainedParams != null;

        if(dialog.retainRequestParams())
        {
            if(dialog.getDialogFlags().flagIsSet(DialogFlags.RETAIN_ALL_REQUEST_PARAMS))
            {
                for(Enumeration e = request.getParameterNames(); e.hasMoreElements();)
                {
                    String paramName = (String) e.nextElement();
                    if(paramName.startsWith(Dialog.PARAMNAME_DIALOGPREFIX) ||
                            paramName.startsWith(Dialog.PARAMNAME_CONTROLPREFIX) ||
                            (retainedAnyParams && retainedParams.contains(paramName)))
                        continue;

                    exportParamToXml(dcElem, paramName, request.getParameterValues(paramName));
                }
            }
            else
            {
                String[] retainParams = dialog.getRetainParams();
                int retainParamsCount = retainParams.length;

                for(int i = 0; i < retainParamsCount; i++)
                {
                    String paramName = retainParams[i];
                    if(retainedAnyParams && retainedParams.contains(paramName))
                        continue;

                    exportParamToXml(dcElem, paramName, request.getParameterValues(paramName));
                }
            }
        }

        parent.appendChild(dcElem);
    }

    public void setFromXml(String xml) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = new java.io.ByteArrayInputStream(xml.getBytes());
        Document doc = builder.parse(is);
        importFromXml(doc.getDocumentElement());
    }

    public Document getAsXmlDocument() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("sparx");
        doc.appendChild(root);

        exportToXml(doc.getDocumentElement());
        return doc;
    }

    public String getAsXml() throws ParserConfigurationException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Document doc = getAsXmlDocument();

        // we use reflection so that org.apache.xml.serialize.* is not a package requirement
        // TODO: when DOM Level 3 is finalized, switch it over to Load/Save methods in that DOM3 spec

        Class serializerCls = Class.forName("org.apache.xml.serialize.XMLSerializer");
        Class outputFormatCls = Class.forName("org.apache.xml.serialize.OutputFormat");

        Constructor serialCons = serializerCls.getDeclaredConstructor(new Class[]{OutputStream.class, outputFormatCls});
        Constructor outputCons = outputFormatCls.getDeclaredConstructor(new Class[]{Document.class});

        OutputStream os = new java.io.ByteArrayOutputStream();

        Object outputFormat = outputCons.newInstance(new Object[]{doc});
        Method indenting = outputFormatCls.getMethod("setIndenting", new Class[]{boolean.class});
        indenting.invoke(outputFormat, new Object[]{new Boolean(true)});
        Method omitXmlDecl = outputFormatCls.getMethod("setOmitXMLDeclaration", new Class[]{boolean.class});
        omitXmlDecl.invoke(outputFormat, new Object[]{new Boolean(true)});

        Object serializer = serialCons.newInstance(new Object[]{os, outputFormat});
        Method serialize = serializerCls.getMethod("serialize", new Class[]{Document.class});
        serialize.invoke(serializer, new Object[]{doc});

        return os.toString();
    }

    /**
     * Creates a map of initial field states
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void createInitialStateFields() throws ParserConfigurationException, SAXException, IOException
    {
        String initialContextValues = getRequest().getParameter(dialog.getInitialContextParamName());
        if (initialContextValues == null)
            return ;

        initialContextXml = URLDecoder.decode(initialContextValues);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = new java.io.ByteArrayInputStream(initialContextXml.getBytes());
        Document doc = builder.parse(is);
        NodeList dcList = doc.getDocumentElement().getElementsByTagName("dialog-context");
        if(dcList.getLength() > 0)
        {
            Element dcElem = (Element) dcList.item(0);
            NodeList children = dcElem.getChildNodes();
            for(int n = 0; n < children.getLength(); n++)
            {
                Node node = children.item(n);
                if(node.getNodeName().equals("field"))
                {
                    Element fieldElem = (Element) node;
                    String fieldName = fieldElem.getAttribute("name");
                    DialogField field = this.getDialog().getFields().getByName(fieldName);
                    if (field != null)
                    {
                        DialogField.State state = field.constructStateInstance(this);
                        state.importFromXml(fieldElem);
                        initialFieldStates.addState(state);
                    }
                }
            }
        }
    }

    /**
     * Calculate what the next state or stage of the dialog should be.
     */
    public void calcState()
    {
        activeMode = DIALOGMODE_INPUT;
        dialog.makeStateChanges(this, STATECALCSTAGE_INITIAL);

        ServletRequest request = getRequest();
        String ignoreVal = request.getParameter(Dialog.PARAMNAME_PEND_DATA);
        if(ignoreVal != null && !ignoreVal.equals("no"))
            validationContext.setValidationStage(DialogValidationContext.VALSTAGE_IGNORE);

        String autoExec = request.getParameter(Dialog.PARAMNAME_AUTOEXECUTE);
        if (autoExec == null || autoExec.length() == 0)
        {
            // if no autoexec is defined in the request parameter, look for it also in the request attribute
            autoExec = (String) request.getAttribute(Dialog.PARAMNAME_AUTOEXECUTE);
        }

        int isValidReturnVal = -1;

        if(autoExec != null && !autoExec.equals("no"))
        {
            activeMode = dialog.isValid(this) ? DIALOGMODE_EXECUTE : DIALOGMODE_VALIDATE;
        }
        else if(!resetContext)
        {
            String modeParamValue = request.getParameter(dialog.getActiveModeParamName());
            if(modeParamValue != null)
            {
                char givenMode = modeParamValue.charAt(0);
                if(givenMode == DIALOGMODE_VALIDATE)
                {
                    isValidReturnVal = dialog.isValid(this) ? 1 : 0;
                    activeMode = isValidReturnVal == 1 ? DIALOGMODE_EXECUTE : DIALOGMODE_VALIDATE;
                }
                else
                    activeMode = givenMode;
//                activeMode = (
//                        givenMode == DIALOGMODE_VALIDATE ?
//                        (dialog.isValid(this) ? DIALOGMODE_EXECUTE : DIALOGMODE_VALIDATE) :
//                        givenMode
//                        );
            }
        }

        nextMode = activeMode;
        if(activeMode == DIALOGMODE_INPUT)
        {
            nextMode = dialog.needsValidation(this) ? DIALOGMODE_VALIDATE : DIALOGMODE_EXECUTE;
        }
        else if(activeMode == DIALOGMODE_VALIDATE)
        {
            if(isValidReturnVal == -1)
                isValidReturnVal = dialog.isValid(this) ? 1 : 0;
            nextMode = isValidReturnVal == 1 ? DIALOGMODE_EXECUTE : DIALOGMODE_VALIDATE;
        }
        else if(activeMode == DIALOGMODE_EXECUTE)
        {
            execSequence++;

            if(dialog.getLoop().getValueIndex() != DialogLoopStyle.NONE)
                nextMode = dialog.needsValidation(this) ? DIALOGMODE_VALIDATE : DIALOGMODE_EXECUTE;
            else
                nextMode = DIALOGMODE_INPUT;
        }

        if(! validationContext.isValid())
            nextMode = DIALOGMODE_VALIDATE;

        dialog.makeStateChanges(this, STATECALCSTAGE_FINAL);
    }

    public DialogFieldStates getFieldStates()
    {
        return this.fieldStates;
    }

    /**
     * Gets the initial dialog field states
     * @return
     */
    public DialogFieldStates getInitialFieldStates()
    {
        return this.initialFieldStates;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    /**
     * Indicates whether or not the context was reset
     *
     * @return boolean True if context was reset
     */
    public boolean contextWasReset()
    {
        return resetContext;
    }

    /**
     * Gets the number of times the dialog has been ran
     *
     * @return int sequence number
     */
    public int getRunSequence()
    {
        return runSequence;
    }

    /**
     * Gets the number of times the dialog has been executed
     *
     * @return int execution count
     */
    public int getExecuteSequence()
    {
        return execSequence;
    }

    public boolean isInitialEntry()
    {
        return runSequence == 1;
    }

    /**
     * Indicates whether or no if this is a first attempt at executing the dialog
     *
     * @return boolean True if the execution is for the first time
     */
    public boolean isInitialExecute()
    {
        return execSequence == 1;
    }

    /**
     * Indicates whether or not if the dialog has been executed already
     *
     * @return boolean True if current exectuion is a duplicate one
     */
    public boolean isDuplicateExecute()
    {
        return execSequence > 1;
    }

    /**
     * Returns the active mode of the dialog
     *
     * @return char Mode
     */
    public char getActiveMode()
    {
        return activeMode;
    }

    /**
     *  Sets the active mode of the dialog
     * @param mode
     */
    public void setActiveMode(char mode)
    {
        activeMode = mode;
    }

    /**
     * Returns what the next mode of the dialog is
     *
     * @return char Mode
     */
    public char getNextMode()
    {
        return nextMode;
    }

    /**
     * Set the next mode of the dialog
     */
    public void setNextMode(char mode)
    {
        nextMode = mode;
    }
    /**
     * Indicates whether or not the dialog is in input mode
     *
     * @return boolean True if the dialog is in input mode
     */
    public boolean inInputMode()
    {
        return activeMode == DIALOGMODE_INPUT;
    }

    /**
     * Indicates whether or not the dialog is in execution mode
     *
     * @return boolean True if the dialog is in execution mode
     */
    public boolean inExecuteMode()
    {
        return activeMode == DIALOGMODE_EXECUTE;
    }

    /**
     * Return true if the "pending" button was pressed in the dialog.
     *
     * @return boolean
     */
    public boolean isPending()
    {
        return validationContext.getValidationStage() == DialogValidationContext.VALSTAGE_IGNORE;
    }

    /**
     *
     */
    public String getOriginalReferer()
    {
        return originalReferer;
    }

    /**
     * Returns the <code>Dialog</code> object this context is associated with
     *
     * @return Dialog dialog object
     */
    public Dialog getDialog()
    {
        return dialog;
    }

    /**
     * Returns the <code>DialogSkin</code> object the dialog is using for its display
     *
     * @return DialogSkin dialog skin
     */
    public DialogSkin getSkin()
    {
        return skin;
    }

    public DialogPerspectives getPerspectives()
    {
        return perspectives;
    }

    public DialogDebugFlags getDebugFlags()
    {
        return debugFlags;
    }

    public boolean addingData()
    {
        return perspectives.flagIsSet(DialogPerspectives.ADD);
    }

    public boolean editingData()
    {
        return perspectives.flagIsSet(DialogPerspectives.EDIT);
    }

    public boolean deletingData()
    {
        return perspectives.flagIsSet(DialogPerspectives.DELETE);
    }

    public boolean confirmingData()
    {
        return perspectives.flagIsSet(DialogPerspectives.CONFIRM);
    }

    public boolean printingData()
    {
        return perspectives.flagIsSet(DialogPerspectives.PRINT);
    }

    public boolean executeStageHandled()
    {
        return executeHandled;
    }

    public void setExecuteStageHandled(boolean value)
    {
        executeHandled = value;
    }

    /**
     * Retrieves the HTTP request parameters that has been retained through the different dialog states
     *
     * @return String[] a string array of request parameters
     */
    public String[] getRetainRequestParams()
    {
        return retainReqParams;
    }

    /**
     * Sets the HTTP request parameters to retain
     *
     * @param params HTTP request parameters
     */
    public void addRetainRequestParams(String[] params)
    {
        if(retainReqParams == null)
            retainReqParams = params;
        else
        {
            String[] oldReqParams = retainReqParams;
            retainReqParams = new String[oldReqParams.length + params.length];
            for(int i = 0; i < oldReqParams.length; i++)
                retainReqParams[i] = oldReqParams[i];
            for(int i = 0; i < params.length; i++)
                retainReqParams[oldReqParams.length + i] = params[i];
        }
    }

    /**
     * Returns a HTML string which contains hidden  form fields representing the dialog's information
     */
    public String getStateHiddens()
    {
        ServletRequest request = getRequest();

        StringBuffer hiddens = new StringBuffer();
        hiddens.append("<input type='hidden' name='" + dialog.getOriginalRefererParamName() + "' value='" + originalReferer + "'>\n");
        hiddens.append("<input type='hidden' name='" + dialog.getTransactionIdParamName() + "' value='" + transactionId + "'>\n");
        hiddens.append("<input type='hidden' name='" + dialog.getRunSequenceParamName() + "' value='" + (runSequence + 1) + "'>\n");
        hiddens.append("<input type='hidden' name='" + dialog.getExecuteSequenceParamName() + "' value='" + execSequence + "'>\n");
        hiddens.append("<input type='hidden' name='" + dialog.getActiveModeParamName() + "' value='" + nextMode + "'>\n");

        String pageCmd = request.getParameter(AbstractHttpServletCommand.PAGE_COMMAND_REQUEST_PARAM_NAME);
        if(pageCmd != null)
            hiddens.append("<input type='hidden' name='" + AbstractHttpServletCommand.PAGE_COMMAND_REQUEST_PARAM_NAME + "' value='" + pageCmd + "'>\n");
        //TODO: hiddens.append("<input type='hidden' name='" + dialog.PARAMNAME_DIALOGQNAME + "' value='" + (runSequence > 1 ? request.getParameter(Dialog.PARAMNAME_DIALOGQNAME) : request.getParameter(DialogManager.REQPARAMNAME_DIALOG)) + "'>\n");

        String redirectUrlParamValue = (runSequence > 1 ? request.getParameter(dialog.getPostExecuteRedirectUrlParamName()) : request.getParameter(DialogContext.DEFAULT_REDIRECT_PARAM_NAME));
        if(redirectUrlParamValue != null)
            hiddens.append("<input type='hidden' name='" + dialog.getPostExecuteRedirectUrlParamName() + "' value='" + redirectUrlParamValue + "'>\n");

        if(perspectives.getFlags() != 0)
            hiddens.append("<input type='hidden' name='" + dialog.getDataCmdParamName() + "' value='" + perspectives.getFlagsText() + "'>\n");

        if(debugFlags.getFlags() != 0)
            hiddens.append("<input type='hidden' name='" + dialog.getDebugFlagsParamName() + "' value='" + debugFlags.getFlagsText() + "'>\n");

        Set retainedParams = null;
        if(retainReqParams != null)
        {
            retainedParams = new HashSet();
            for(int i = 0; i < retainReqParams.length; i++)
            {
                String paramName = retainReqParams[i];
                Object paramValue = request.getParameter(paramName);
                if(paramValue == null)
                    continue;

                hiddens.append("<input type='hidden' name='");
                hiddens.append(paramName);
                hiddens.append("' value='");
                hiddens.append(paramValue);
                hiddens.append("'>\n");
                retainedParams.add(paramName);
            }
        }
        boolean retainedAnyParams = retainedParams != null;

        if(dialog.retainRequestParams())
        {
            if(dialog.getDialogFlags().flagIsSet(DialogFlags.RETAIN_ALL_REQUEST_PARAMS))
            {
                for(Enumeration e = request.getParameterNames(); e.hasMoreElements();)
                {
                    String paramName = (String) e.nextElement();
                    if(paramName.startsWith(Dialog.PARAMNAME_DIALOGPREFIX) ||
                            paramName.startsWith(Dialog.PARAMNAME_CONTROLPREFIX) ||
                            (retainedAnyParams && retainedParams.contains(paramName)))
                        continue;

                    hiddens.append("<input type='hidden' name='");
                    hiddens.append(paramName);
                    hiddens.append("' value='");
                    hiddens.append(request.getParameter(paramName));
                    hiddens.append("'>\n");
                }
            }
            else
            {
                String[] retainParams = dialog.getRetainParams();
                int retainParamsCount = retainParams.length;

                for(int i = 0; i < retainParamsCount; i++)
                {
                    String paramName = retainParams[i];
                    if(retainedAnyParams && retainedParams.contains(paramName))
                        continue;

                    hiddens.append("<input type='hidden' name='");
                    hiddens.append(paramName);
                    hiddens.append("' value='");
                    hiddens.append(request.getParameter(paramName));
                    hiddens.append("'>\n");
                }
            }
        }

        return hiddens.toString();
    }

    public void renderDebugPanels(Writer writer) throws IOException
    {
        debugPanels.render(writer, this, this.getActiveTheme(), HtmlPanel.RENDERFLAGS_DEFAULT);
    }

    protected static final HtmlLayoutPanel debugPanels = new HtmlLayoutPanel();
    static
    {
        debugPanels.getFrame().setHeading(new StaticValueSource("Dialog Context Debug"));
        debugPanels.setStyle(new HtmlPanelsStyleEnumeratedAttribute(HtmlPanelsStyleEnumeratedAttribute.TABBED));
        debugPanels.getFrame().setFooting(new StaticValueSource("NOTE: You need to add override Dialog.execute(Writer, DialogContext)."));

        debugPanels.addPanel(new DialogContextAttributesPanel());
        debugPanels.addPanel(new DialogContextFieldStatesPanel());
        debugPanels.addPanel(new DialogContextFieldStatesClassesPanel());
        debugPanels.addPanel(new HttpRequestParametersPanel());
    }
}
