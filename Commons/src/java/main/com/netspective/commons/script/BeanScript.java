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
package com.netspective.commons.script;

import java.io.File;
import java.io.IOException;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.ValueContext;

public class BeanScript implements Script
{
    private Log log = LogFactory.getLog(BeanScript.class);
    private ScriptsNameSpace nameSpace;
    private String name;
    private String language;
    private String script;
    private File source;

    public BeanScript()
    {
    }

    public BeanScript(ScriptsNameSpace nameSpace)
    {
        this.nameSpace = nameSpace;
    }

    public Log getLog()
    {
        return log;
    }

    public static String translateNameForMapKey(String name)
    {
        return name != null ? name.toUpperCase() : null;
    }

    public String getNameForMapKey()
    {
        return translateNameForMapKey(getQualifiedName());
    }

    public ScriptsNameSpace getNameSpace()
    {
        return nameSpace;
    }

    public String getQualifiedName()
    {
        return nameSpace != null ? (nameSpace.getNameSpaceId() + "." + name) : name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        this.log = LogFactory.getLog(BeanScript.class.getName() + "." + name);
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getScript()
    {
        return script;
    }

    public void setScript(String script)
    {
        this.script = script;
    }

    public String getSource()
    {
        return source == null ? null : source.getAbsolutePath();
    }

    public void setSource(File source) throws IOException
    {
        this.source = source;
        setScript(TextUtils.getInstance().getFileContents(source.getAbsolutePath()));
    }

    public void addText(String text)
    {
        if(script == null)
            script = text;
        else
            script += text;
    }

    public Object callFunction(ScriptContext scriptContext, Object className, String methodName, Object[] params) throws ScriptException
    {
        DefaultScriptContext dsc = (DefaultScriptContext) scriptContext;
        if(!dsc.isExecuted())
            execute(scriptContext);

        BSFEngine bsfEngine = scriptContext.getBSFEngine();
        try
        {
            return bsfEngine.call(className, methodName, params);
        }
        catch(BSFException e)
        {
            log.error("Error executing script: " + getScript() + " language " + getLanguage(), e);
            throw new ScriptException(e);
        }
    }

    public Object evaluateAsExpression(ScriptContext scriptContext) throws ScriptException
    {
        BSFEngine bsfEngine = scriptContext.getBSFEngine();
        try
        {
            return bsfEngine.eval("(java)", 1, 1, getScript());
        }
        catch(BSFException e)
        {
            log.error("Error executing script: " + getScript() + " language " + getLanguage(), e);
            throw new ScriptException(e);
        }
    }

    public void execute(ScriptContext scriptContext) throws ScriptException
    {
        DefaultScriptContext dsc = (DefaultScriptContext) scriptContext;
        BSFEngine bsfEngine = scriptContext.getBSFEngine();
        try
        {
            bsfEngine.exec("(java)", 1, 1, getScript());
            dsc.setExecuted(true);
        }
        catch(BSFException e)
        {
            log.error("Error executing script: " + getScript() + " language " + getLanguage(), e);
            throw new ScriptException(e);
        }
    }

    public Object callFunction(ValueContext vc, Object className, String methodName, Object[] params) throws ScriptException
    {
        return callFunction(vc.getScriptContext(this), className, methodName, params);
    }

    public Object evaluateAsExpression(ValueContext vc) throws ScriptException
    {
        return evaluateAsExpression(vc.getScriptContext(this));
    }

    public void execute(ValueContext vc) throws ScriptException
    {
        execute(vc.getScriptContext(this));
    }

    public ScriptContext createScriptContext() throws ScriptException
    {
        return new DefaultScriptContext(this);
    }
}
