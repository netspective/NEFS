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
 * $Id: TabularReportCalcs.java,v 1.2 2003-05-24 20:28:14 shahid.shah Exp $
 */

package com.netspective.commons.report.tabular.calc;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Method;

import org.apache.commons.discovery.tools.DiscoverClass;
import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.netspective.commons.report.tabular.calc.ColumnDataCalculator;

public class TabularReportCalcs
{
    private static DiscoverClass discoverClass = new DiscoverClass();
    public static final String CALCMETHODNAME_GETIDENTIFIERS = "getIdentifiers";
    protected static final Log log = LogFactory.getLog(TabularReportCalcs.class);

    private static TabularReportCalcs instance = (TabularReportCalcs) DiscoverSingleton.find(TabularReportCalcs.class, TabularReportCalcs.class.getName());

    private Map calcsClassesMap = new HashMap();
    private Set calcsClassesSet = new HashSet();

    public static TabularReportCalcs getInstance()
    {
        return instance;
    }

    public void registerColumnDataCalc(Class cls)
    {
        Class actualClass = discoverClass.find(cls, cls.getName());
        String[] identifiers = getCalcIdentifiers(actualClass);
        for(int i = 0; i < identifiers.length; i++)
        {
            calcsClassesMap.put(identifiers[i], actualClass);
            if(log.isTraceEnabled())
                log.trace("Registered column data calculator "+ actualClass.getName() +" as '"+ identifiers[i] +"'.");
        }
        calcsClassesSet.add(actualClass);
    }

    public String[] getCalcIdentifiers(Class vsClass)
    {
        Method getIdsMethod = null;
        try
        {
            getIdsMethod = vsClass.getMethod(CALCMETHODNAME_GETIDENTIFIERS, null);
        }
        catch (NoSuchMethodException e)
        {
            log.error("Error retrieving method " + CALCMETHODNAME_GETIDENTIFIERS, e);
            throw new NestableRuntimeException("Static method 'String[] "+ CALCMETHODNAME_GETIDENTIFIERS +"()' not found in column data calc " + vsClass.getName(), e);
        }

        try
        {
            return (String[]) getIdsMethod.invoke(null, null);
        }
        catch (Exception e)
        {
            log.error("Error executing method " + CALCMETHODNAME_GETIDENTIFIERS, e);
            throw new NestableRuntimeException("Exception while obtaining identifiers using 'String[] "+ CALCMETHODNAME_GETIDENTIFIERS +"()' method in column data calc " + vsClass.getName(), e);
        }
    }

    /**
     * Returns a freshly instantiated ColumnDataCalculator named "cmd". If "cmd" is an already-existing named
     * class it will return a newInstance() of that class. If the "cmd" is a class name, a newInstance() of that
     * particular class will be created and the class will be cached in the calcsClasses Map.
     */

    public ColumnDataCalculator createDataCalc(String id)
    {
        Class cls = (Class) calcsClassesMap.get(id);
        if(cls == null)
        {
            try
            {
                cls = Class.forName(id);
                registerColumnDataCalc(cls);
            }
            catch(ClassNotFoundException e)
            {
                return null;
            }
        }

        try
        {
            return (ColumnDataCalculator) cls.newInstance();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.toString());
        }
    }
}