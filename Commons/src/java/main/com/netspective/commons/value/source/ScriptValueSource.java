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
package com.netspective.commons.value.source;

import java.util.List;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.netspective.commons.script.Script;
import com.netspective.commons.script.ScriptException;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.exception.ValueSourceInitializeException;

public class ScriptValueSource extends AbstractValueSource
{
    public static final String[] IDENTIFIERS = new String[]{"script"};
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation("A value source that wraps a bean script to provide dynamic values. A function called getValue(ValueContext) must be" +
            "present in the script. Optionally, the script may contain getPresentationValue(ValueContext) and hasValue(ValueContext)" +
            "methods.",
            new ValueSourceDocumentation.Parameter[]
            {
                new ValueSourceDocumentation.Parameter("script-name", true, "The name of the script (must have been defined elsewhere)")
            });

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private String scriptName;

    public ScriptValueSource()
    {
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);
        scriptName = spec.getParams();
    }

    public String getScriptName()
    {
        return scriptName;
    }

    public Object callFunctionInScript(ValueContext vc, String method, Object[] params)
    {
        Script script = vc.getScriptsManager().getScript(scriptName);
        if (script == null)
            throw new RuntimeException("Script '" + scriptName + "' not found in " + this + ". Available: " +
                    vc.getScriptsManager().getScriptNames());

        Object result;
        try
        {
            result = script.callFunction(vc, null, method, params);
        }
        catch (ScriptException e)
        {
            throw new NestableRuntimeException(e);
        }

        return result;
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        Object result;
        try
        {
            result = callFunctionInScript(vc, "getPresentationValue", new Object[]{vc});
        }
        catch (Exception e)
        {
            // if we get any exceptions, just use the getValue() function
            result = getValue(vc);
        }

        if (result instanceof PresentationValue)
            return (PresentationValue) result;

        if (result instanceof Value)
            return new PresentationValue((Value) result);

        if (result instanceof String[])
            return new PresentationValue(new GenericValue((String[]) result));

        if (result instanceof List)
            return new PresentationValue(new GenericValue((List) result));

        return new PresentationValue(new GenericValue(result));
    }

    public Value getValue(ValueContext vc)
    {
        Object result = callFunctionInScript(vc, "getValue", new Object[]{vc});

        if (result instanceof Value)
            return (Value) result;

        if (result == null)
            return null;

        if (result instanceof String[])
            return new GenericValue((String[]) result);

        if (result instanceof List)
            return new GenericValue((List) result);

        return new GenericValue(result);
    }

    public boolean hasValue(ValueContext vc)
    {
        Object result;
        try
        {
            result = callFunctionInScript(vc, "hasValue", new Object[]{vc});
        }
        catch (Exception e)
        {
            // if we get any exceptions, just use the getValue() function
            result = getValue(vc);
        }

        if (result instanceof Boolean)
            return ((Boolean) result).booleanValue();

        if (result instanceof Value)
            return ((Value) result).hasValue();

        return result != null ? TextUtils.getInstance().toBoolean(result.toString(), false) : false;
    }
}
