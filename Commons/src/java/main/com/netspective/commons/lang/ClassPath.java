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
 * $Id: ClassPath.java,v 1.3 2003-08-09 16:36:34 shahid.shah Exp $
 */

package com.netspective.commons.lang;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;

public class ClassPath
{
    private static final Map CLASS_PATH_PROVIDERS = new HashMap();

    public interface ClassPathProvider
    {
        public String getClassLoaderImplementationClassName();
        public String getClassLoaderClassPath();
    }

    public static Map getClassPathProviders()
    {
        return CLASS_PATH_PROVIDERS;
    }

    public static ClassPathProvider getClassPathProvider(ClassLoader classLoader)
    {
        return (ClassPathProvider) CLASS_PATH_PROVIDERS.get(classLoader.getClass().getName());
    }

    public static void registerClassPathProvider(ClassPathProvider provider)
    {
        CLASS_PATH_PROVIDERS.put(provider.getClassLoaderImplementationClassName(), provider);
    }

    /**
     * Prints the absolute pathname of the class file containing the specified class name, as prescribed by
     * the class path.
     *
     * @param clsName The class whose class location we're interested in
     */
    public static String getClassFileName(String clsName)
    {
        String resource = new String(clsName);

        if(!resource.startsWith("/"))
            resource = "/" + resource;

        resource = resource.replace('.', '/');
        resource = resource + ".class";

        java.net.URL classUrl = ClassPath.class.getResource(resource);

        if(classUrl == null)
            return null;
        else
            return classUrl.getFile();
    }

    public static String getClassFileName(Class cls)
    {
        return getClassFileName(cls.getName());
    }

    static public class ClassPathInfo
    {
        private File classPath;
        private boolean isValid;
        private boolean isDirectory;
        private boolean isJar;
        private boolean isZip;

        public ClassPathInfo(String path)
        {
            classPath = new File(path);

            if(classPath.exists())
            {
                if(classPath.isDirectory())
                {
                    isValid = true;
                    isDirectory = true;
                }
                else
                {
                    isValid = true;
                    String pathLower = path.toLowerCase();
                    if(pathLower.endsWith(".jar"))
                        isJar = true;
                    else if(pathLower.endsWith(".zip"))
                        isZip = true;
                    else
                        isValid = false;
                }
            }
            else
                isValid = false;
        }

        public File getClassPath()
        {
            return classPath;
        }

        public boolean isValid()
        {
            return isValid;
        }

        public boolean isDirectory()
        {
            return isDirectory;
        }

        public boolean isJar()
        {
            return isJar;
        }

        public boolean isZip()
        {
            return isZip;
        }
    }

    public static void addClassPaths(List classPathList, String path)
    {
        StringTokenizer tokenizer = new StringTokenizer(path, File.pathSeparator);
        while(tokenizer.hasMoreTokens())
        {
            String pathName = tokenizer.nextToken();
            classPathList.add(new ClassPathInfo(pathName));
        }
    }

    public static ClassPathInfo[] getClassPaths(String[] paths)
    {
        List classPathList = new ArrayList();
        for(int i = 0; i < paths.length; i++)
            addClassPaths(classPathList, paths[i]);
        if(classPathList.size() == 0)
            return null;

        return (ClassPathInfo[]) classPathList.toArray(new ClassPathInfo[classPathList.size()]);
    }

    public static ClassPathInfo[] getClassPaths(String path)
    {
        List classPathList = new ArrayList();
        addClassPaths(classPathList, path);
        if(classPathList.size() == 0)
            return null;

        return (ClassPathInfo[]) classPathList.toArray(new ClassPathInfo[classPathList.size()]);
    }

    public static ClassPathInfo[] getClassPaths(ClassLoader classLoader)
    {
        ClassPathProvider provider = getClassPathProvider(classLoader);
        if(provider != null)
            return getClassPaths(provider.getClassLoaderClassPath());

        return getSystemClassPaths();
    }

    public static ClassPathInfo[] getDefaultClassLoaderClassPaths()
    {
        return getClassPaths(ClassPath.class.getClassLoader());
    }

    public static ClassPathInfo[] getSystemClassPaths()
    {
        return getClassPaths(System.getProperty("java.class.path"));
    }

    public static ClassLoader getDefaultClassLoader()
    {
        return ClassPath.class.getClassLoader();
    }
}
