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
 * $Id: DialogField.java,v 1.1 2003-05-05 21:25:30 shahid.shah Exp $
 */

package com.netspective.sparx.form.field;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContextMemberInfo;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.sparx.form.DialogDataCommands;
import com.netspective.sparx.form.field.conditional.DialogFieldConditionalData;
import com.netspective.sparx.form.field.conditional.DialogFieldConditionalApplyFlag;
import com.netspective.sparx.form.field.conditional.DialogFieldConditionalDisplay;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.validate.ValidationRules;
import com.netspective.commons.validate.ValidationRulesCollection;

/**
 * A <code>DialogField</code> object represents a data field of a form/dialog. It contains functionalities
 * such as data validation rules, dynamic data binding, HTML rendering, and conditional logics.
 * It provides the default behavior and functionality for all types of dialog fields.
 * All dialog classes representing specialized  fields such as text fields, numerical fields, and phone fields subclass
 * the <code>DialogField</code> class.
 */
public class DialogField
{
    private static final Log log = LogFactory.getLog(DialogField.class);

    public static final Flags.FlagDefn[] FLAG_DEFNS = new Flags.FlagDefn[]
    {
        new Flags.FlagDefn(Flags.ACCESS_XDM, "REQUIRED", Flags.REQUIRED),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "PRIMARY_KEY", Flags.PRIMARY_KEY),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "VISIBLE", Flags.VISIBLE),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "READ_ONLY", Flags.READ_ONLY),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "INITIAL_FOCUS", Flags.INITIAL_FOCUS),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "PERSIST", Flags.PERSIST),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "CREATE_ADJACENT_AREA", Flags.CREATE_ADJACENT_AREA),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "SHOW_CAPTION_AS_CHILD", Flags.SHOW_CAPTION_AS_CHILD),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "HIDDEN", Flags.INPUT_HIDDEN),
        new Flags.FlagDefn(Flags.ACCESS_PRIVATE, "HAS_CONDITIONAL_DATA", Flags.HAS_CONDITIONAL_DATA),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "COLUMN_BREAK_BEFORE", Flags.COLUMN_BREAK_BEFORE),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "COLUMN_BREAK_AFTER", Flags.COLUMN_BREAK_AFTER),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "BROWSER_READONLY", Flags.BROWSER_READONLY),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "IDENTIFIER", Flags.IDENTIFIER),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "READONLY_HIDDEN_UNLESS_HAS_DATA", Flags.READONLY_HIDDEN_UNLESS_HAS_DATA),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "READONLY_INVISIBLE_UNLESS_HAS_DATA", Flags.READONLY_INVISIBLE_UNLESS_HAS_DATA),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "DOUBLE_ENTRY", Flags.DOUBLE_ENTRY),
        new Flags.FlagDefn(Flags.ACCESS_PRIVATE, "SCANNABLE", Flags.SCANNABLE),
        new Flags.FlagDefn(Flags.ACCESS_PRIVATE, "AUTO_BLUR", Flags.AUTO_BLUR),
        new Flags.FlagDefn(Flags.ACCESS_PRIVATE, "SUBMIT_ONBLUR", Flags.SUBMIT_ONBLUR),
        new Flags.FlagDefn(Flags.ACCESS_XDM, "CREATE_ADJACENT_AREA_HIDDEN", Flags.CREATEADJACENTAREA_HIDDEN),
    };

    public static final int[] CHILD_CARRY_FLAGS = new int[] { Flags.REQUIRED, Flags.VISIBLE, Flags.READ_ONLY, Flags.PERSIST, Flags.CREATE_ADJACENT_AREA, Flags.SHOW_CAPTION_AS_CHILD };

    public class Flags extends XdmBitmaskedFlagsAttribute
    {
        // all these values are also defined in dialog.js (make sure they are always in sync)
        public static final int REQUIRED = 1;
        public static final int PRIMARY_KEY = REQUIRED * 2;
        public static final int VISIBLE = PRIMARY_KEY * 2;
        public static final int READ_ONLY = VISIBLE * 2;
        public static final int INITIAL_FOCUS = READ_ONLY * 2;
        public static final int PERSIST = INITIAL_FOCUS * 2;
        public static final int CREATE_ADJACENT_AREA = PERSIST * 2;
        public static final int SHOW_CAPTION_AS_CHILD = CREATE_ADJACENT_AREA * 2;
        public static final int INPUT_HIDDEN = SHOW_CAPTION_AS_CHILD * 2;
        public static final int HAS_CONDITIONAL_DATA = INPUT_HIDDEN * 2;
        public static final int COLUMN_BREAK_BEFORE = HAS_CONDITIONAL_DATA * 2;
        public static final int COLUMN_BREAK_AFTER = COLUMN_BREAK_BEFORE * 2;
        public static final int BROWSER_READONLY = COLUMN_BREAK_AFTER * 2;
        public static final int IDENTIFIER = BROWSER_READONLY * 2;
        public static final int READONLY_HIDDEN_UNLESS_HAS_DATA = IDENTIFIER * 2;
        public static final int READONLY_INVISIBLE_UNLESS_HAS_DATA = READONLY_HIDDEN_UNLESS_HAS_DATA * 2;
        public static final int DOUBLE_ENTRY = READONLY_INVISIBLE_UNLESS_HAS_DATA * 2;
        public static final int SCANNABLE = DOUBLE_ENTRY * 2;
        public static final int AUTO_BLUR = SCANNABLE * 2;
        public static final int SUBMIT_ONBLUR = AUTO_BLUR * 2;
        public static final int CREATEADJACENTAREA_HIDDEN = SUBMIT_ONBLUR * 2;
        public static final int START_CUSTOM = CREATEADJACENTAREA_HIDDEN * 2; // all DialogField "children" will use this

        public Flags()
        {
            setFlag(VISIBLE);
        }

        public FlagDefn[] getFlagsDefns()
        {
            return FLAG_DEFNS;
        }

        public void clearFlag(long flag)
        {
            super.clearFlag(flag);
            if (children != null)
                children.clearFlags(flag);
        }

        public void setFlag(long flag)
        {
            super.setFlag(flag);
            if (children != null)
                children.setFlags(flag);
        }
    }

    public class DialogFieldState
    {
        public DialogFieldValue value = constructValueInstance();
        public String adjacentAreaValue;
        public Flags stateFlags = createFlags();
        public ArrayList errorMessages;
        public DialogContext dc;

        public DialogFieldState(DialogContext dc)
        {
            this.dc = dc;
            stateFlags.copy(getFlags());
            if(dc.getRunSequence() == 1 && stateFlags.flagIsSet(Flags.PERSIST))
            {
                Cookie[] cookies = dc.getHttpRequest().getCookies();
                if(cookies != null)
                {
                    for(int i =0; i < cookies.length; i++)
                    {
                        Cookie cookie = cookies[i];
                        if(cookie.getName().equals(getCookieName()))
                            value.setTextValue(URLDecoder.decode(cookie.getValue()));
                    }
                }
            }

            switch((int) dc.getDataCommands().getFlags())
            {
                case DialogDataCommands.EDIT:
                    // when in "edit" mode, the primary key should be read-only
                    if(stateFlags.flagIsSet(Flags.PRIMARY_KEY))
                        stateFlags.setFlag(Flags.READ_ONLY);
                    break;

                case DialogDataCommands.CONFIRM:
                case DialogDataCommands.DELETE:
                case DialogDataCommands.PRINT:
                    // when in "delete" mode, all the fields should be read-only
                    stateFlags.setFlag(Flags.READ_ONLY);
                    break;
            }
        }

        public DialogFieldValue getValue()
        {
            return value;
        }

        public String getAdjacentAreaValue()
        {
            return adjacentAreaValue;
        }

        public Flags getStateFlags()
        {
            return stateFlags;
        }

        public DialogField getField()
        {
            return DialogField.this;
        }

        public List getErrorMessages()
        {
            if(DialogField.this.getErrors() != null)
                return DialogField.this.getErrors();

            return errorMessages;
        }

        public void addErrorMessage(String message)
        {
            if(errorMessages == null)
                errorMessages = new ArrayList();

            for(Iterator i = errorMessages.iterator(); i.hasNext();)
            {
                if(i.next().equals(message))
                    return;
            }

            errorMessages.add(message);
        }

        public void persistValue()
        {
            if(stateFlags.flagIsSet(Flags.PERSIST) && value.hasValue())
            {
                Cookie cookie = new Cookie(getCookieName(), URLEncoder.encode(value.getTextValue()));
                cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
                dc.getHttpResponse().addCookie(cookie);
            }
        }
    }

    public static final String translateFieldNameForMapKey(final String name)
    {
        return name.toLowerCase();
    }

    public static final int DISPLAY_FORMAT = 0;
    public static final int SUBMIT_FORMAT = 0;

	public static final String CUSTOM_CAPTION = new String();
	public static final String GENERATE_CAPTION = "*";

	static public int fieldCounter = 0;
	private boolean multi = false;

    private Dialog owner;
	private DialogField parent;
	private String id;
	private String name;
	private String qualifiedName;
	private ValueSource caption = ValueSource.NULL_VALUE_SOURCE;
	private String cookieName;
	private String errorMessage;
	private ValueSource defaultValue = ValueSource.NULL_VALUE_SOURCE;
	private List errors;
	private DialogFields children;
	private List conditionalActions;
	private List dependentConditions;
	private List clientJavascripts;
	private Flags flags;
	private DialogFieldPopup popup;
    private DialogFieldScanEntry scanEntry;
    private DialogFieldAutoBlur autoBlur;
    private DialogFieldSubmitOnBlur submitOnBlur;
    private ValidationRules validationRules;
	private ValueSource hint = ValueSource.NULL_VALUE_SOURCE;

	/**
	 * Creates a dialog field
	 */
	public DialogField(Dialog owner)
	{
        this.owner = owner;
        intialize();
	}

    public DialogField(DialogField parent)
    {
        this(parent.getOwner());
        this.parent = parent;
    }

    public Dialog getOwner()
    {
        return owner;
    }

    public void intialize()
    {
        defaultValue = null;
        errorMessage = null;
        flags = createFlags();
    }

    public DialogFieldValue constructValueInstance()
    {
        //TODO: return new something;
        return null;
    }

    public DialogFieldState constructStateInstance(DialogContext dc)
    {
        return new DialogFieldState(dc);
    }

    public void initValidationRules(ValidationRules rules)
    {
        // empty here, but may be overridden by children
    }

    public ValidationRules getValidationRules()
    {
        if(validationRules == null)
        {
            validationRules = new ValidationRulesCollection();
            initValidationRules(validationRules);
        }
        return validationRules;
    }

    public ValidationRules createValidation()
    {
        return getValidationRules();
    }

    public void addValidation(ValidationRules rules)
    {
        // do nothing
    }

    public Flags createFlags()
    {
        return new Flags();
    }

    public Flags getFlags()
    {
        return flags;
    }

    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

	/**
	 * Checks to see if the field requires multi-part endcoding
	 *
	 * @return boolean
	 */
	public boolean requiresMultiPartEncoding()
	{
		// if any child requires multi part encoding, then return true (this will take of things recursively)
		if (children != null)
            if(children.requiresMultiPartEncoding())
                return true;

		// no child requires it and we don't require it by default, either
		return false;
	}

    /**
     * Get a list of conditional actions
     *
     * @return List a list of conditional actions
     */
    public List getConditionalActions()
    {
        return conditionalActions;
    }

    public void addConditional(DialogFieldConditionalAction action)
    {
        conditionalActions.add(action);
        flags.setFlag(Flags.HAS_CONDITIONAL_DATA);
    }

    public DialogFieldPopup getPopup()
    {
        return popup;
    }

    public void addPopup(DialogFieldPopup popup)
    {
        if(popup.getFill() == null)
            popup.setFill(getQualifiedName());
        this.popup = popup;
    }

	/**
	 * Gets all the javascripts defined for this field
	 *
	 * @return ArrayList
	 */
	public List getClientJavascripts()
	{
		return this.clientJavascripts;
	}

    public void addClientJs(DialogFieldClientJavascript clientJs)
    {
        clientJavascripts.add(clientJs);
    }

    public void addAutoBlur(DialogFieldAutoBlur autoBlur)
    {
        flags.setFlag(Flags.AUTO_BLUR);
        this.autoBlur = autoBlur;
    }

    public void addSubmitOnBlur(DialogFieldSubmitOnBlur submitOnBlur)
    {
        flags.setFlag(Flags.SUBMIT_ONBLUR);
        this.submitOnBlur = submitOnBlur;
    }

    public void addScanEntry(DialogFieldScanEntry scanEntry)
    {
        flags.setFlag(Flags.SCANNABLE);
        this.scanEntry = scanEntry;
    }

	public void invalidate(DialogContext dc, String message)
	{
		dc.getFieldStates().addErrorMessage(parent != null ? parent : this, message);
	}

	/**
	 * Gets the parent dialog field
	 *
	 * @return DialogField
	 */
	public DialogField getParent()
	{
		return parent;
	}

	/**
	 * Sets the parent dialog field
	 *
	 * @param newParent the parent field
	 */
	public void setParent(DialogField newParent)
	{
		parent = newParent;
	}

	/**
	 * Returns true if the field is supposed to return multiple values
	 *
	 * @return boolean Does this field return multiple values (e.g. multilist select field)
	 */
	public boolean isMulti()
	{
		return multi;
	}

	/**
	 * Sets the value of the multi attribute which determines whether a field returns multiple values or not (e.g.
	 * a multilist select field)
	 *
	 * @param multi boolean true/false - Whether or not this field returns multiple values
	 */
	public void setMulti(boolean multi)
	{
		this.multi = multi;
	}

	public String getId()
	{
		return id;
	}

	/**
	 * Gets the simple name of the dialog
	 *
	 * @return String
	 */
	public String getName()
	{
		return name;
	}

    public String getNameForMapKey()
    {
        return name != null ? name.toLowerCase() :null;
    }

	/**
	 * Gets the qualified name of the dialog
	 *
	 * @return String
	 */
	public String getQualifiedName()
	{
		return qualifiedName;
	}

	/**
	 * Sets the simple name of the dialog
	 *
	 * @param newName new simple name
	 */
	public void setName(String newName)
	{
		name = newName;
		if (name != null)
		{
			id = Dialog.PARAMNAME_CONTROLPREFIX + name;
			setQualifiedName(name);
		}
	}

	/**
	 * Sets the qualified name of the dialog
	 *
	 * @param newName new qualified name
	 */
	public void setQualifiedName(String newName)
	{
		qualifiedName = newName;
		if (qualifiedName != null)
			id = Dialog.PARAMNAME_CONTROLPREFIX + qualifiedName;
	}

	/**
	 * Gets the cookie name associated with the dialog
	 *
	 * @return String cookie name
	 */
	public String getCookieName()
	{
		return cookieName == null ? (Dialog.PARAMNAME_CONTROLPREFIX + getQualifiedName()) : cookieName;
	}

	/**
	 * Sets the cookie name associated with the dialog
	 *
	 * @param name cookie name
	 */
	public void setCookieName(String name)
	{
		cookieName = name;
	}

	/**
	 * Gets the caption of the dialog as a single value source
	 *
	 * @return ValueSource
	 */
	public ValueSource getCaption()
	{
		return caption;
	}

	/**
	 * Sets the caption of the dialog from a value source
	 *
	 * @param value value source object from which the caption is being extracted
	 */
	public void setCaption(ValueSource value)
	{
		caption = value;
	}

	/**
	 * Gets the hint string associated with the dialog field
	 *
	 * @return String
	 */
	public ValueSource getHint()
	{
		return hint;
	}

	/**
	 * Sets the hint string associated with the dialog field
	 *
	 * @param value hint string
	 */
	public void setHint(ValueSource value)
	{
		hint = value;
	}

	/**
	 * Gets the display error message when a validation fails
	 *
	 * @return String
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * Sets the error message for display when a validation fails
	 *
	 * @param newMessage error message string
	 */
	public void setErrorMessage(String newMessage)
	{
		errorMessage = newMessage;
	}

	/**
	 * Gets the default value of the field as a value source
	 *
	 * @return ValueSource    value source containing the field's value
	 */
	public ValueSource getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * Sets the default value for the field
	 *
	 * @param value value source containing the value
	 */
	public void setDefaultValue(ValueSource value)
	{
		defaultValue = value;
	}

	/**
	 * Gets a list of children fields
	 *
	 * @return List list of children fields
	 */
	public DialogFields getChildren()
	{
		return children;
	}

    public DialogField createField()
    {
        return new DialogField(this);
    }

    public DialogField createField(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        if(DialogField.class.isAssignableFrom(cls))
        {
            Constructor c = cls.getConstructor(new Class[] { DialogField.class });
            return (DialogField) c.newInstance(new Object[] { this });
        }
        else
            throw new RuntimeException("Don't know what to do with with class: " + cls);
    }

	/**
	 * Adds a child field.
	 *
	 * @param field child field
	 */
	public void addField(DialogField field)
	{
		for (int i = 0; i < CHILD_CARRY_FLAGS.length; i++)
		{
			int flag = CHILD_CARRY_FLAGS[i];
			if (flags.flagIsSet(flag))
				field.flags.setFlag(flag);
		}

		if (children == null) children = new DialogFields(this);
		children.add(field);

		field.setParent(this);
		if (qualifiedName != null)
			field.setQualifiedName(qualifiedName + "." + field.getName());
	}

	/**
	 * Gets a list of errors
	 *
	 * @return List list of errors
	 */
	public List getErrors()
	{
		return errors;
	}

	/**
	 * Adds a error message for the field
	 *
	 * @param msg error message
	 */
	public void addErrorMessage(String msg)
	{
		if (errors == null) errors = new ArrayList();
		errors.add(msg);
	}

	/**
	 * Finalize the dialog field's contents: loops through each conditional action of the field to
	 * assign partner fields and loops through each child field to finalize their contents.
	 */
	public void finalizeContents()
	{
		if (conditionalActions != null)
		{
			Iterator i = conditionalActions.iterator();
			while (i.hasNext())
			{
				DialogFieldConditionalAction action = (DialogFieldConditionalAction) i.next();
				DialogField partnerField = owner.getFields().getByQualifiedName(action.getPartnerFieldName());
				if (partnerField != null)
					action.setPartnerField(partnerField);
                else
                    log.error("Unknown partner supplied for conditional action " + action.getSourceField().getQualifiedName());
			}
		}

		if (children != null)
		{
			for(int i = 0; i < children.size(); i++)
			{
				DialogField field = children.get(i);
				field.finalizeContents();
			}
		}

		if (flags.flagIsSet(Flags.DOUBLE_ENTRY))
			this.setupDoubleEntry();
	}

	public void setupDoubleEntry()
	{
		this.setHint(new StaticValueSource("Double Entry"));
		DialogFieldClientJavascript doubleEntryJS = new DialogFieldClientJavascript();
		doubleEntryJS.setType(new DialogFieldClientJavascript.ScriptType(DialogFieldClientJavascript.ScriptType.EXTENDS));
		doubleEntryJS.setEvent(new DialogFieldClientJavascript.ControlEvent(DialogFieldClientJavascript.ControlEvent.LOSE_FOCUS));
		doubleEntryJS.setJsExpr(new StaticValueSource("doubleEntry(field, control)"));
		this.addClientJs(doubleEntryJS);

		DialogFieldClientJavascript deOnChangeJS = new DialogFieldClientJavascript();
		deOnChangeJS.setType(new DialogFieldClientJavascript.ScriptType(DialogFieldClientJavascript.ScriptType.EXTENDS));
		deOnChangeJS.setEvent(new DialogFieldClientJavascript.ControlEvent(DialogFieldClientJavascript.ControlEvent.VALUE_CHANGED));
		deOnChangeJS.setJsExpr(new StaticValueSource("field.successfulEntry = false"));
		this.addClientJs(deOnChangeJS);
	}

	public List getDependentConditions()
	{
		return dependentConditions;
	}

	public void addDependentCondition(DialogFieldConditionalAction action)
	{
		if (dependentConditions == null) dependentConditions = new ArrayList();
		dependentConditions.add(action);
	}

	/**
	 * Indicates whether or not the field is a required field. It checks the  <code>Flags.REQUIRED</code>
	 * flag of the field and its' children.
	 *
	 * @param dc  dialog context
	 */
	public boolean isRequired(DialogContext dc)
	{
		String qName = getQualifiedName();
		if (qName != null)
		{
			if (dc.getFieldStates().getState(this).getStateFlags().flagIsSet(Flags.REQUIRED)) return true;
		}
		else
		{
			if (flags.flagIsSet(Flags.REQUIRED)) return true;
		}

		if (children != null)
		{
            for(int i = 0; i < children.size(); i++)
            {
				if (children.get(i).isRequired(dc))
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether or not the field is visible. The check is done by seeing if the invisible flag, <code>Flags.INVISIBLE</code>
	 * is set or not and by making sure each partner field of its' conditionals have a value or not.
	 *
	 * @param dc dialog context
	 * @return boolean True if the field is visible
	 */
	public boolean isVisible(DialogContext dc)
	{
		if (flags.flagIsSet(Flags.HAS_CONDITIONAL_DATA))
		{
			Iterator i = conditionalActions.iterator();
			while (i.hasNext())
			{
				DialogFieldConditionalAction action = (DialogFieldConditionalAction) i.next();
				if (action instanceof DialogFieldConditionalData)
				{
					// if the partner field doesn't have data yet, hide this field
					if (isRequired(dc))
					{
						String value = dc.getFieldStates().getState(action.getPartnerField()).getValue().getTextValue();
						if (value == null || value.length() == 0)
							return false;
					}
				}
			}
		}

        DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
        DialogField.Flags stateFlags = state.getStateFlags();

        if (! stateFlags.flagIsSet(Flags.VISIBLE))
            return false;

        if (children == null && stateFlags.flagIsSet(Flags.READ_ONLY) &&
            (stateFlags.flagIsSet(Flags.READONLY_INVISIBLE_UNLESS_HAS_DATA) ||
            dc.getDialog().getDialogFlags().flagIsSet(DialogFlags.READONLY_FIELDS_INVISIBLE_UNLESS_HAVE_DATA)))
        {
            Object value = state.getValue().getValue();
            return value == null ? false : (value instanceof String ? (((String) value).length() == 0 ? false : true) : true);
        }
        else
            return true;
	}

	public boolean isReadOnly(DialogContext dc)
	{
        DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
        return state.getStateFlags().flagIsSet(Flags.READ_ONLY);
	}

	public boolean isBrowserReadOnly(DialogContext dc)
	{
        DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
        return state.getStateFlags().flagIsSet(Flags.BROWSER_READONLY);
	}

	public boolean isInputHiddenFlagSet(DialogContext dc)
	{
        DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
        return state.getStateFlags().flagIsSet(Flags.INPUT_HIDDEN);
	}

	public boolean isInputHidden(DialogContext dc)
	{
		String qName = getQualifiedName();
		if (qName != null)
		{
            DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
            DialogField.Flags stateFlags = state.getStateFlags();

			if (stateFlags.flagIsSet(Flags.INPUT_HIDDEN))
				return true;

			if (children == null && stateFlags.flagIsSet(Flags.READ_ONLY) &&
				(stateFlags.flagIsSet(Flags.READONLY_HIDDEN_UNLESS_HAS_DATA) ||
				dc.getDialog().getDialogFlags().flagIsSet(DialogFlags.READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA)))
			{
				Object value = state.getValue().getValue();
				return value == null ? true : (value instanceof String ? (((String) value).length() == 0 ? true : false) : false);
			}
			else
				return false;
		}
		else
			return flags.flagIsSet(Flags.INPUT_HIDDEN);
	}

	public boolean persistValue()
	{
		return flags.flagIsSet(Flags.PERSIST);
	}

	public boolean showCaptionAsChild()
	{
        return flags.flagIsSet(Flags.SHOW_CAPTION_AS_CHILD);
	}

	public String getHiddenControlHtml(DialogContext dc)
	{
        DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
		String value = state.getValue().getTextValue();
		return "<input type='hidden' name='" + getId() + "' value=\"" + (value != null ? TextUtils.escapeHTML(value) : "") + "\">";
	}

	public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
	{
		if (isInputHidden(dc))
		{
			getHiddenControlHtml(dc);
			return;
		}

		if (children == null)
			return;

		dc.getSkin().renderCompositeControlsHtml(writer, dc, this);
	}

	public boolean needsValidation(DialogContext dc)
	{
		if (flags.flagIsSet(Flags.HAS_CONDITIONAL_DATA))
			return true;

		if (children == null)
			return isRequired(dc);

		int validateFieldsCount = 0;
        for(int i = 0; i < children.size(); i++)
        {
            DialogField field = children.get(i);
			if (field.isVisible(dc) && field.needsValidation(dc))
				validateFieldsCount++;
		}

		return validateFieldsCount > 0 ? true : false;
	}

	public boolean defaultIsValid(DialogContext dc)
	{
		if (flags.flagIsSet(Flags.HAS_CONDITIONAL_DATA))
		{
			Iterator i = conditionalActions.iterator();
			while (i.hasNext())
			{
				DialogFieldConditionalAction action = (DialogFieldConditionalAction) i.next();
				if (action instanceof DialogFieldConditionalData)
				{
					// if the partner field doesn't have data, then this field is "invalid"
					if (isRequired(dc) && dc.getFieldStates().getState(action.getPartnerField()).getValue().getTextValue() == null)
						return false;
				}
			}
		}
		if (children == null)
			return true;

		int invalidFieldsCount = 0;
        for(int i = 0; i < children.size(); i++)
        {
            DialogField field = children.get(i);
			if (field.isVisible(dc) && (!field.isValid(dc)))
				invalidFieldsCount++;
		}
		return invalidFieldsCount == 0 ? true : false;
	}

	public boolean isValid(DialogContext dc)
	{
		return defaultIsValid(dc);
	}

	/**
	 * Format the dialog value after it has been validated and is ready for submission
	 *
	 * @param value dialog field value
	 * @return String
	 */
	public String formatSubmitValue(String value)
	{
		return value;
	}

	/**
	 * Format the dialog value for every dialog stage except before submission
	 *
	 * @param value dialog field value
	 * @return String
	 */
	public String formatDisplayValue(String value)
	{
		return value;
	}

	public void makeStateChanges(DialogContext dc, int stage)
	{
		if (stage == DialogContext.STATECALCSTAGE_INITIAL && conditionalActions != null)
		{
			for (int i = 0; i < conditionalActions.size(); i++)
			{
				DialogFieldConditionalAction action = (DialogFieldConditionalAction) conditionalActions.get(i);
				if (action instanceof DialogFieldConditionalApplyFlag)
					((DialogFieldConditionalApplyFlag) action).applyFlags(dc);
			}
		}

		if (children != null)
		{
            for(int i = 0; i < children.size(); i++)
            {
                DialogField field = children.get(i);
				field.makeStateChanges(dc, stage);
			}
		}
	}

	public void populateValue(DialogContext dc, int formatType)
	{
		if (id == null) return;

        DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
		DialogFieldValue dfValue = state.getValue();
        String textValue = dfValue.getTextValue();

		if (textValue == null)
			textValue = dc.getRequest().getParameter(id);

		if (dc.getRunSequence() == 1)
		{
			if ((textValue != null && textValue.length() == 0 && defaultValue != null) ||
				(textValue == null && defaultValue != null))
				textValue = defaultValue.getTextValueOrBlank(dc);
		}

		if (formatType == DialogField.DISPLAY_FORMAT)
			dfValue.setValue(formatDisplayValue(textValue));
		else if (formatType == DialogField.SUBMIT_FORMAT)
			dfValue.setValue(formatSubmitValue(textValue));

		if (children == null) return;

        for(int i = 0; i < children.size(); i++)
        {
            DialogField field = children.get(i);
			if (field.isVisible(dc)) field.populateValue(dc, formatType);
		}
	}

	/**
	 * Produces JavaScript code to handle Client-side events for the dialog field
	 *
	 */
	public String getJavaScriptDefn(DialogContext dc)
	{
		String fieldClassName = this.getClass().getName();
		String fieldQualfName = this.getQualifiedName();
        String fieldCaption = caption != null ? caption.getTextValueOrBlank(dc) : "";
		String js =
			"field = new DialogField(\"" + fieldClassName + "\", \"" + this.getId() + "\", \"" + this.getName() + "\", \"" + fieldQualfName + "\", \"" + fieldCaption + "\", " + dc.getFieldStates().getState(this).getStateFlags() + ");\n" +
			"dialog.registerField(field);\n";
		String customStr = this.getEventJavaScriptFunctions(dc);
		customStr += this.getCustomJavaScriptDefn(dc);
		if (customStr != null)
			js += customStr;

		List dependentConditions = this.getDependentConditions();
		if (dependentConditions != null)
		{
			StringBuffer dcJs = new StringBuffer();
			Iterator i = dependentConditions.iterator();
			while (i.hasNext())
			{
				DialogFieldConditionalAction o = (DialogFieldConditionalAction) i.next();

				if (o instanceof DialogFieldConditionalDisplay)
				{
					DialogFieldConditionalDisplay action = (DialogFieldConditionalDisplay) o;
					if (action.getPartnerField().isVisible(dc))
						dcJs.append("field.dependentConditions[field.dependentConditions.length] = new DialogFieldConditionalDisplay(\"" + action.getSourceField().getQualifiedName()
							+ "\", \"" + action.getPartnerField().getQualifiedName() + "\", \"" + action.getExpression() + "\");\n");
				}
			}
			js = js + dcJs.toString();
		}

		if (children != null)
		{
			StringBuffer childJs = new StringBuffer();
            for(int i = 0; i < children.size(); i++)
            {
                DialogField child = children.get(i);
				childJs.append(child.getJavaScriptDefn(dc));
			}
			js = js + childJs.toString();
		}

		return js;
	}

	/**
	 * Retrieves user defined java script strings for this field and creates JS functions
	 * out of them
	 */
	public String getEventJavaScriptFunctions(DialogContext dc)
	{
		String ret = "";

		List jsList = this.getClientJavascripts();
		if (jsList != null)
		{
			String eventName = "";

			StringBuffer jsBuffer = new StringBuffer();
			Iterator i = jsList.iterator();
			while (i.hasNext())
			{
				DialogFieldClientJavascript jsObject = (DialogFieldClientJavascript) i.next();
				String script = (jsObject.getJsExpr() != null ? jsObject.getJsExpr().getTextValue(dc) : null);
				eventName = TextUtils.xmlTextToJavaIdentifier(jsObject.getEvent().getValue(), false);
				// append function signature
				if (script != null)
				{
					jsBuffer.append("field.customHandlers." + eventName + " = new Function(\"field\", \"control\", \"" +
						jsObject.getJsExpr().getValue(dc) + "\");\n");
					jsBuffer.append("field.customHandlers." + eventName + "Type = '" + jsObject.getType().getValue() + "';\n");
				}
			}
			ret = ret + jsBuffer.toString();
		}
		return ret;
	}

	public String getCustomJavaScriptDefn(DialogContext dc)
	{
		StringBuffer sb = new StringBuffer();

        if(this.isBrowserReadOnly(dc))
            sb.append("field.readonly = 'yes';\n");
        else
            sb.append("field.readonly = 'no';\n");

        if(flags.flagIsSet(Flags.IDENTIFIER))
            sb.append("field.identifier = 'yes';\n");
        else
            sb.append("field.identifier = 'no';\n");

		if (flags.flagIsSet(Flags.DOUBLE_ENTRY))
		{
			sb.append("field.doubleEntry = 'yes';\n");
			sb.append("field.firstEntryValue = '';\n");
			sb.append("field.successfulEntry = true;\n");
		}

		if (flags.flagIsSet(Flags.SCANNABLE))
		{
			sb.append("field.scannable = 'yes';\n");
			sb.append("field.scanStartCode = '" + scanEntry.getStartCode() + "';\n");
			sb.append("field.scanStopCode = '" + scanEntry.getStopCode() + "';\n");
			sb.append("field.scanCodeIgnoreCase = '" + scanEntry.getIgnoreCase() + "';\n");
			sb.append("field.isScanned = false;\n");
			sb.append("field.scanPartnerField = '" + scanEntry.getPartnerField() + "';\n");
			if(scanEntry.getCustomScript() != null && scanEntry.getCustomScript().length() > 0)
				sb.append("field.scanFieldCustomScript = new Function(\"field\", \"control\", \"inputString\", \"" + scanEntry.getCustomScript() + "\");\n");
			else
				sb.append("field.scanFieldCustomScript = '';\n");
		}

		if (flags.flagIsSet(Flags.AUTO_BLUR))
		{
			sb.append("field.autoBlur = 'yes';\n");
			sb.append("field.autoBlurLength = " + autoBlur.getLength() + ";\n");
			sb.append("field.autoBlurExcRegExp = '" + autoBlur.getExcludeExpr() + "';\n");
			sb.append("field.numCharsEntered = 0;\n");
		}

		if (flags.flagIsSet(Flags.SUBMIT_ONBLUR))
		{
			sb.append("field.submitOnBlur = true;\n");
			sb.append("field.submitOnBlurPartnerField ='" + submitOnBlur.getPartner() + "';\n");
			if(submitOnBlur.getCustomScript() != null && submitOnBlur.getCustomScript().length() >0)
				sb.append("field.submitOnBlurCustomScript = new Function(\"field\", \"control\", \"" + submitOnBlur.getCustomScript() + "\");\n");
			else
				sb.append("field.submitOnBlurCustomScript = '';\n");
		}

		return sb.toString();
	}

	/**
	 * Produces Java code when a custom DialogContext is created
	 */
	public DialogContextMemberInfo createDialogContextMemberInfo(String dataTypeName)
	{
		DialogContextMemberInfo mi = new DialogContextMemberInfo(this.getQualifiedName(), dataTypeName);

		String memberName = mi.getMemberName();
		String fieldName = mi.getFieldName();

		mi.addJavaCode("\t/* To change the following auto-generated code, modify createDialogContextMemberInfo() method in " + this.getClass().getName() + " */\n");
		mi.addJavaCode("\tpublic boolean is" + memberName + "ValueSet() { return hasValue(\"" + fieldName + "\"); }\n");
		mi.addJavaCode("\tpublic boolean is" + memberName + "FlagSet(long flag) { return flagIsSet(\"" + fieldName + "\", flag); }\n");
		mi.addJavaCode("\tpublic void set" + memberName + "Flag(long flag) { setFlag(\"" + fieldName + "\", flag); }\n");
		mi.addJavaCode("\tpublic void clear" + memberName + "Flag(long flag) { clearFlag(\"" + fieldName + "\", flag); }\n");
		mi.addJavaCode("\tpublic String get" + memberName + "RequestParam() { return request.getParameter(\"" + Dialog.PARAMNAME_CONTROLPREFIX + fieldName + "\"); }\n");
		mi.addJavaCode("\tpublic DialogField get" + memberName + "Field() { return getField(\"" + fieldName + "\"); }\n");
		mi.addJavaCode("\tpublic DialogContext.DialogFieldState get" + memberName + "FieldState() { return getFieldState(\"" + fieldName + "\"); }\n");
		mi.addJavaCode("\tpublic void add" + memberName + "ErrorMsg(String msg) { addErrorMessage(\"" + fieldName + "\", msg); }\n");

		return mi;
	}

	/**
	 * Produces Java code when a custom DialogContext is created
	 * The default method produces nothing; all the subclasses must define what they need.
	 */
	public DialogContextMemberInfo getDialogContextMemberInfo()
	{
		if (children == null) return null;

		DialogContextMemberInfo mi = createDialogContextMemberInfo("children");
        for(int i = 0; i < children.size(); i++)
        {
            DialogField field = children.get(i);
			DialogContextMemberInfo childMI = field.getDialogContextMemberInfo();
			if (childMI == null)
				continue;

			String[] childImports = childMI.getImportModules();
			if (childImports != null)
			{
				for (int m = 0; m < childImports.length; m++)
					mi.addImportModule(childImports[m]);
			}

			mi.addJavaCode(childMI.getCode());
		}

		return mi;
	}
}
