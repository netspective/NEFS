/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: HttpUtils.java,v 1.5 2003-08-31 23:40:52 shahid.shah Exp $
 */

package com.netspective.sparx.util;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.RuntimeEnvironmentFlags;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.ProjectComponent;
import com.netspective.sparx.template.freemarker.FreeMarkerTemplateProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.Writer;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtils
{
    private static final Log log = LogFactory.getLog(HttpUtils.class);
    private static final ValueSource develEnvironmentHeaderTemplate = new StaticValueSource("devel-environment-header.ftl");
    private static final FreeMarkerTemplateProcessor develEnvironmentHeader = new FreeMarkerTemplateProcessor();

    static
    {
        develEnvironmentHeader.setSource(develEnvironmentHeaderTemplate);
    }

    public static void assignParamToInstance(HttpServletRequest req, XmlDataModelSchema schema, Object instance, String paramName, String defaultValue) throws IllegalAccessException, InvocationTargetException, DataModelException
    {
        boolean required = false;
        if(paramName.endsWith("!"))
        {
            required = true;
            paramName = paramName.substring(0, paramName.length() - 1);
        }

        Method method = (Method) schema.getAttributeSetterMethods().get(paramName);
        if(method != null)
        {
            Class[] args = method.getParameterTypes();
            if(args.length == 1)
            {
                Class arg = args[0];
                if(java.lang.String.class.equals(arg) && arg.isArray())
                {
                    String[] paramValues = req.getParameterValues(paramName);
                    if((paramValues == null || paramValues.length == 0) && required)
                            throw new ServletParameterRequiredException("Servlet parameter list '"+ paramName + "' is required but not available.");
                    method.invoke(instance, new Object[] { paramValues });
                }
                else
                {
                    XmlDataModelSchema.AttributeSetter as = (XmlDataModelSchema.AttributeSetter) schema.getAttributeSetters().get(paramName);
                    String paramValue = req.getParameter(paramName);
                    if(paramValue == null)
                    {
                        if(required)
                            throw new ServletParameterRequiredException("Servlet parameter '"+ paramName + "' is required but not available.");

                        paramValue = defaultValue;
                    }
                    as.set(null, instance, paramValue);
                }
            }
            else if(log.isDebugEnabled())
                log.debug("Attempting to assign '"+ paramName +"' to a method in '"+ instance.getClass() +"' but the method has more than one argument.");
        }
        else if(log.isDebugEnabled())
            log.debug("Attempting to assign '"+ paramName +"' to a method in '"+ instance.getClass() +"' but there is no mutator available.");
    }

    /**
     * Given a list of HTTP parameter names, assign their current values using appropriate accessor methods of the
     * instance object (using Java reflection).
     * @param req The HTTP servlet request
     * @param instance The object who's mutator methods should be matched
     * @param paramNames The names of the parameters that should be assigned to the mutators of the instance object.
     *                   This may be '*' (for all parameters) or a comma-separated list of names. The parameter names
     *                   may optionally be followed by an '=' to indicate a default value for the parameter. Parameter
     *                   names may optionally be terminated with an '!' to indicate that they are required (an exception
     *                   is thrown if the parameter is unavailable. For example, "a,b!,c" would mean that parameter
     *                   'a', 'b' and 'c' should be assigned using setA(), setB() and setC() if available but an
     *                   exception should be thrown if 'b' is not available as a request parameter.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void assignParamsToInstance(HttpServletRequest req, Object instance, String paramNames) throws IllegalAccessException, InvocationTargetException, DataModelException
    {
        XmlDataModelSchema schema = XmlDataModelSchema.getSchema(instance.getClass());

        if(paramNames.equals("*"))
        {
            for(Enumeration e = req.getParameterNames(); e.hasMoreElements(); )
            {
                String paramName = (String) e.nextElement();
                assignParamToInstance(req, schema, instance, paramName, null);
            }
        }
        else
        {
            String[] retainParams = TextUtils.split(paramNames, ",", true);
            for(int i = 0; i < retainParams.length; i++)
            {
                String paramName = retainParams[i];
                int defaultValuePos = paramName.indexOf('=');
                if(defaultValuePos > 0)
                {
                    paramName = paramName.substring(0, defaultValuePos);
                    assignParamToInstance(req, schema, instance, paramName.substring(0, defaultValuePos), paramName.substring(defaultValuePos+1));
                }
                else
                    assignParamToInstance(req, schema, instance, paramName, null);
            }
        }
    }

    public static String appendParams(ServletRequest req, String url, String paramNames)
    {
        StringBuffer result = new StringBuffer(url);
        boolean hasQueryChar = url.indexOf('?') >= 0;

        if(paramNames.equals("*"))
        {
            int i = 0;
            for(Enumeration e = req.getParameterNames(); e.hasMoreElements(); )
            {
                String paramName = (String) e.nextElement();
                String paramValue = req.getParameter(paramName);
                if(paramValue != null)
                {
                    if(i > 0 || hasQueryChar)
                        result.append("&");
                    else
                        result.append("?");

                    result.append(paramName);
                    result.append("=");
                    result.append(paramValue);
                }
                i++;
            }
        }
        else
        {
            String[] retainParams = TextUtils.split(paramNames, ",", true);
            for(int i = 0; i < retainParams.length; i++)
            {
                String paramName = retainParams[i];
                String paramValue = req.getParameter(paramName);
                if(paramValue != null)
                {
                    if(i > 0 || hasQueryChar)
                        result.append("&");
                    else
                        result.append("?");

                    result.append(paramName);
                    result.append("=");
                    result.append(paramValue);
                }
            }
        }

        return result.toString();
    }

    public static void renderDevelopmentEnvironmentHeader(Writer writer, NavigationContext nc) throws IOException
    {
        if(! nc.getRuntimeEnvironmentFlags().flagIsSet(RuntimeEnvironmentFlags.DEVELOPMENT | RuntimeEnvironmentFlags.FRAMEWORK_DEVELOPMENT))
            return;

        final ProjectComponent projectComponent = nc.getProjectComponent();
        if(projectComponent.hasErrors())
        {
            int errorsCount = projectComponent.getErrors().size() + projectComponent.getWarnings().size();

            writer.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"3\" bgcolor=darkred><tr>\n");
            writer.write("  <td nowrap><font size=2 color=white style='font-family: tahoma,arial; font-size: 8pt'><b>You have <font color=yellow>"+ errorsCount +"</font> Netspective Frameworks Project Errors/Warnings.");
            writer.write("             Visit the <a href='"+ nc.getRootUrl() +"/console/project/input-source#errors' style='color: yellow'>Console</a> to see the messages</b></font></td>\n");
            writer.write("</tr></table>");
        }

        final NavigationPage.Flags flags = (NavigationPage.Flags) nc.getActiveState().getFlags();
        if(flags.isDebuggingRequest())
            develEnvironmentHeader.process(writer, nc, null);
    }
}
