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
package com.netspective.commons.io;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * A simple class for loading java.util.Properties backed by .properties files
 * deployed as classpath resources. See individual methods for details.
 *
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public abstract class PropertiesLoader
{
    public static final String PROPNAME_PROPERTIES_SOURCE = "__source";
    public static final String PROPNAME_PROPERTIES_CLASS = "__sourceClass";

    /**
     * Looks up a resource named 'name' in the classpath. The resource must map
     * to a file with .properties extention. The name is assumed to be absolute
     * and can use either "/" or "." for package segment separation with an
     * optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     *
     * @param name   classpath resource name [may not be null]
     * @param loader classloader through which to load the resource [null
     *               is equivalent to the application loader]
     *
     * @return resource converted to java.util.Properties [may be null if the
     *         resource was not found and THROW_ON_LOAD_FAILURE is false]
     *
     * @throws IllegalArgumentException if the resource was not found and
     *                                  THROW_ON_LOAD_FAILURE is true
     */
    public static Properties loadProperties(String name, ClassLoader loader, final boolean throwOnFailure, final boolean loadAsResourceBundle)
    {
        if(name == null)
            throw new IllegalArgumentException("null input: name");

        if(name.startsWith("/"))
            name = name.substring(1);

        if(name.endsWith(SUFFIX))
            name = name.substring(0, name.length() - SUFFIX.length());

        Properties result = null;

        InputStream in = null;
        try
        {
            if(loader == null) loader = ClassLoader.getSystemClassLoader();

            if(loadAsResourceBundle)
            {
                name = name.replace('/', '.');

                // throws MissingResourceException on lookup failures:
                final ResourceBundle rb = ResourceBundle.getBundle(name,
                                                                   Locale.getDefault(), loader);

                result.setProperty(PROPNAME_PROPERTIES_CLASS, rb.getClass().toString());
                result.setProperty(PROPNAME_PROPERTIES_SOURCE, rb.toString());

                result = new Properties();
                for(Enumeration keys = rb.getKeys(); keys.hasMoreElements();)
                {
                    final String key = (String) keys.nextElement();
                    final String value = rb.getString(key);

                    result.put(key, value);
                }
            }
            else
            {
                name = name.replace('.', '/');

                if(!name.endsWith(SUFFIX))
                    name = name.concat(SUFFIX);

                // returns null on lookup failures:
                in = loader.getResourceAsStream(name);
                if(in != null)
                {
                    result = new Properties();
                    result.setProperty(PROPNAME_PROPERTIES_CLASS, in.getClass().toString());
                    result.setProperty(PROPNAME_PROPERTIES_SOURCE, in.toString());
                    result.load(in); // can throw IOException
                }
            }
        }
        catch(Exception e)
        {
            result = null;
        }
        finally
        {
            if(in != null) try
            { in.close(); }
            catch(Throwable ignore)
            {}
        }

        if(throwOnFailure && (result == null))
        {
            throw new IllegalArgumentException("could not load [" + name + "]" +
                                               " as " + (loadAsResourceBundle
                                                         ? "a resource bundle"
                                                         : "a classloader resource"));
        }

        return result;
    }

    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader, boolean, boolean)}
     * that uses the current thread's context classloader. A better strategy
     * would be to use techniques shown in
     * http://www.javaworld.com/javaworld/javaqa/2003-06/01-qa-0606-load.html
     */
    public static Properties loadProperties(final String name, final boolean throwOnError)
    {
        return loadProperties(name, Thread.currentThread().getContextClassLoader(), throwOnError, false);
    }

    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader, boolean, boolean)}
     * that uses the current thread's context classloader. A better strategy
     * would be to use techniques shown in
     * http://www.javaworld.com/javaworld/javaqa/2003-06/01-qa-0606-load.html
     */
    public static Properties loadProperties(final String name)
    {
        return loadProperties(name, Thread.currentThread().getContextClassLoader(), true, false);
    }

    private PropertiesLoader() {} // this class is not extentible

    private static final String SUFFIX = ".properties";

}
