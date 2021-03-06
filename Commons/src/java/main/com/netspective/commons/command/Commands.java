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
package com.netspective.commons.command;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.discovery.tools.DiscoverClass;
import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.metric.Metric;
import com.netspective.commons.metric.MetricsProducer;

public class Commands implements MetricsProducer
{
    private static DiscoverClass discoverClass = new DiscoverClass();
    protected static final Log log = LogFactory.getLog(Commands.class);

    public static final String CMDNAME_AND_FIRST_PARAM_DELIM = ",";
    public static final String CMDMETHODNAME_GETIDENTIFIERS = "getIdentifiers";
    public static final String CMDMETHODNAME_GETDOCUMENTATION = "getDocumentation";

    private static Commands instance = (Commands) DiscoverSingleton.find(Commands.class, Commands.class.getName());

    private Map srcClassesMap;
    private Set srcClassesSet;

    public static final Commands getInstance()
    {
        return instance;
    }

    public Commands()
    {
        srcClassesMap = createSourceClassesMap();
        srcClassesSet = createSourceClassesSet();
        registerDefaultCommands();
    }

    public void produceMetrics(Metric parent)
    {
        parent.addValueMetric("Command Classes", Integer.toString(srcClassesSet.size()));
    }

    protected Map createSourceClassesMap()
    {
        return new TreeMap();
    }

    protected Set createSourceClassesSet()
    {
        return new HashSet();
    }

    protected void registerDefaultCommands()
    {
    }

    public Map getClassesMap()
    {
        return srcClassesMap;
    }

    public Set getClassesSet()
    {
        return srcClassesSet;
    }

    public void registerCommand(Class vsClass)
    {
        Class actualClass = discoverClass.find(vsClass, vsClass.getName());
        String[] identifiers = getCommandIdentifiers(actualClass);
        for(int i = 0; i < identifiers.length; i++)
        {
            srcClassesMap.put(identifiers[i], actualClass);
            if(log.isTraceEnabled())
                log.trace("Registered command " + actualClass.getName() + " as '" + identifiers[i] + "'.");
        }
        srcClassesSet.add(actualClass);
    }

    public String[] getCommandIdentifiers(Class cmdClass)
    {
        Method getIdsMethod = null;
        try
        {
            getIdsMethod = cmdClass.getMethod(CMDMETHODNAME_GETIDENTIFIERS, null);
        }
        catch(NoSuchMethodException e)
        {
            log.error("Error retreiving method " + CMDMETHODNAME_GETIDENTIFIERS, e);
            throw new NestableRuntimeException("Static method 'String[] " + CMDMETHODNAME_GETIDENTIFIERS + "()' not found in command " + cmdClass.getName(), e);
        }

        try
        {
            return (String[]) getIdsMethod.invoke(null, null);
        }
        catch(Exception e)
        {
            log.error("Error executing method " + CMDMETHODNAME_GETIDENTIFIERS, e);
            throw new NestableRuntimeException("Exception while obtaining identifiers using 'String[] " + CMDMETHODNAME_GETIDENTIFIERS + "()' method in command " + cmdClass.getName(), e);
        }
    }

    public CommandDocumentation getCommandDocumentation(Class cmdClass)
    {
        Method getDocsMethod = null;
        try
        {
            getDocsMethod = cmdClass.getMethod(CMDMETHODNAME_GETDOCUMENTATION, null);
        }
        catch(NoSuchMethodException e)
        {
            return null;
        }

        try
        {
            return (CommandDocumentation) getDocsMethod.invoke(null, null);
        }
        catch(Exception e)
        {
            log.error("Error executing method " + CMDMETHODNAME_GETDOCUMENTATION + " in command " + cmdClass.getName(), e);
            return null;
        }
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Command getCommand(String name, String params) throws CommandCreationException
    {
        Class ccClass = (Class) srcClassesMap.get(name);
        if(ccClass != null)
        {
            try
            {
                Command command = (Command) ccClass.newInstance();
                if(params != null) command.setParameters(params);
                return command;
            }
            catch(Exception e)
            {
                log.error("Error instantiating command " + name + " with params " + params, e);
                throw new CommandCreationException(e, name, params);
            }
        }
        else
            return null;
    }

    public Command getCommand(String cmdSpecification) throws CommandNotFoundException
    {
        String cmd = cmdSpecification;
        String cmdParam = null;
        int cmdDelimPos = cmdSpecification.indexOf(CMDNAME_AND_FIRST_PARAM_DELIM);
        if(cmdDelimPos != -1)
        {
            cmd = cmdSpecification.substring(0, cmdDelimPos);
            cmdParam = cmdSpecification.substring(cmdDelimPos + 1);
        }

        Command command = getCommand(cmd, cmdParam);
        if(command != null)
            return command;
        else
            throw new CommandNotFoundException("Command '" + cmd + "' not found for specification '" + cmdSpecification + "'.", cmdSpecification);
    }

}