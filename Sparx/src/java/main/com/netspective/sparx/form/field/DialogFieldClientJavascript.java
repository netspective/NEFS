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
 * $Id: DialogFieldClientJavascript.java,v 1.3 2003-06-15 20:32:17 shahid.shah Exp $
 */

package com.netspective.sparx.form.field;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.xdm.XmlDataModelSchema;

/**
 * Manages custom JavaScript defined as the action script for an event generated from a dialog field within a client
 * browser. The programmer may specify the control event, whether to override or extend an event, and an arbitrary
 * JavaScript expression.
 */
public class DialogFieldClientJavascript
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();
    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.addRequiredAttributes(new String[] { "js-expr" });
    }

    private ControlEvent event = new ControlEvent(ControlEvent.IS_VALID);
    private ScriptType type = new ScriptType(ScriptType.EXTENDS);
    private ValueSource jsExpr;

    public DialogFieldClientJavascript()
    {
    }

    /**
     * Retrieve the event that the client javascript should handle.
     */
    public ControlEvent getEvent()
    {
        return event;
    }

    /**
     * Declare the event that the client javascript should handle.
     */
    public void setEvent(ControlEvent event)
    {
        this.event = event;
    }

    /**
     * Retrieve the actual JavaScript expression.
     */
    public ValueSource getJsExpr()
    {
        return jsExpr;
    }

    /**
     * Declare the JavaScript expression that will be executed when the control event is fired. Although the expression
     * is arbitrary, the programmer should usually define a JavaScript method in separate file, include the file, and
     * then call that method in this expression.
     */
    public void setJsExpr(ValueSource jsExpr)
    {
        this.jsExpr = jsExpr;
    }

    /**
     * Retrieve the type of client java-script being provided.
     */
    public ScriptType getType()
    {
        return type;
    }

    /**
     * Declare the type of client java-script being provided. The extends option will run the normal processing and
     * then call your code. The overrides option only calls your code.
     */
    public void setType(ScriptType type)
    {
        this.type = type;
    }

    public static class ControlEvent extends XdmEnumeratedAttribute
    {
        private static final String[] VALUES = new String[] { "is-valid", "value-changed", "click", "key-press", "get-focus", "lose-focus" };

        public static final int IS_VALID = 0;
        public static final int VALUE_CHANGED = 1;
        public static final int CLICK = 2;
        public static final int KEY_PRESS = 3;
        public static final int GET_FOCUS = 4;
        public static final int LOSE_FOCUS = 5;

        public ControlEvent()
        {
        }

        public ControlEvent(int valueIndex)
        {
            super(valueIndex);
        }

        public String[] getValues()
        {
            return VALUES;
        }
    }

    public static class ScriptType extends XdmEnumeratedAttribute
    {
        private static final String[] VALUES = new String[] { "extends", "override" };

        public static final int EXTENDS = 0;
        public static final int OVERRIDE = 1;

        public ScriptType()
        {
        }

        public ScriptType(int valueIndex)
        {
            super(valueIndex);
        }

        public String[] getValues()
        {
            return VALUES;
        }
    }

}
