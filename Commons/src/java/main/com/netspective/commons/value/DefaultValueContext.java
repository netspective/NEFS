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
package com.netspective.commons.value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.discovery.tools.DiscoverClass;
import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.RuntimeEnvironmentFlags;
import com.netspective.commons.acl.AccessControlListsManager;
import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.commons.script.Script;
import com.netspective.commons.script.ScriptContext;
import com.netspective.commons.script.ScriptException;
import com.netspective.commons.script.ScriptsManager;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.text.GloballyUniqueIdentifier;
import com.netspective.commons.text.TextUtils;

public class DefaultValueContext implements ValueContext
{
    private static final Log log = LogFactory.getLog(DefaultValueContext.class);
    protected static DiscoverClass discoverClass = new DiscoverClass();
    protected static int contextNum = 0;

    private String contextId;
    private long creationTime;
    private RuntimeEnvironmentFlags environmentFlags;
    private JexlContext jexlContext;
    private Map scriptContexts;

    public DefaultValueContext()
    {
        this.creationTime = System.currentTimeMillis();
    }

    public ScriptContext getScriptContext(Script script) throws ScriptException
    {
        if (scriptContexts == null)
            scriptContexts = new HashMap();

        ScriptContext result = (ScriptContext) scriptContexts.get(script.getQualifiedName());
        if (result == null)
        {
            result = script.createScriptContext();
            scriptContexts.put(script.getQualifiedName(), result);
        }

        return result;
    }

    public RuntimeEnvironmentFlags getRuntimeEnvironmentFlags()
    {
        if (environmentFlags == null)
        {
            try
            {
                environmentFlags = (RuntimeEnvironmentFlags) discoverClass.newInstance(RuntimeEnvironmentFlags.class, RuntimeEnvironmentFlags.class.getName());
            }
            catch (Exception e)
            {
                log.error("Unable to instantiate environment flags using SPI -- creating statically instead", e);
                environmentFlags = new RuntimeEnvironmentFlags();
            }
        }
        return environmentFlags;
    }

    public long getCreationTime()
    {
        return creationTime;
    }

    public Date getCreationDate()
    {
        return new Date(creationTime);
    }

    public ScriptsManager getScriptsManager()
    {
        return null;
    }

    public AccessControlListsManager getAccessControlListsManager()
    {
        return null;
    }

    public ConfigurationsManager getConfigurationsManager()
    {
        return null;
    }

    public AuthenticatedUser getAuthenticatedUser()
    {
        return null;
    }

    public Object getAttribute(String attributeId)
    {
        return null;
    }

    public void removeAttribute(String attributeId)
    {
    }

    public void setAttribute(String attributeId, Object attributeValue)
    {
    }

    public Object getContextLocation()
    {
        return null;
    }

    public void setContextLocation(Object locator)
    {
    }

    public final String getContextId()
    {
        if (contextId == null)
        {
            try
            {
                contextId = GloballyUniqueIdentifier.getRandomGUID(false);
            }
            catch (Exception e)
            {
                contextId = Long.toString(contextNum);
                log.error("Unable to create context id.", e);
            }
        }

        return contextId;
    }

    public boolean isConditionalExpressionTrue(String expr, Map vars)
    {
        Object evalResult = evaluateExpression(expr, vars);
        if (evalResult instanceof Boolean)
            return ((Boolean) evalResult).booleanValue();
        else if (evalResult != null)
            return TextUtils.getInstance().toBoolean(evalResult.toString(), false);
        else
        {
            log.error("Conditional expression '" + expr + "' did not return a boolean or non-Object.");
            return false;
        }
    }

    public Object evaluateExpression(String expr, Map vars)
    {
        try
        {
            Expression e = ExpressionFactory.createExpression(expr);
            if (jexlContext == null)
                jexlContext = JexlHelper.createContext();

            Map jexlVars = vars != null ? vars : new HashMap();
            jexlVars.put("vc", this);
            jexlContext.setVars(jexlVars);

            return e.evaluate(jexlContext);
        }
        catch (Exception e)
        {
            log.error("Unable to evaluate '" + expr + "': ", e);
            return null;
        }
    }
}
