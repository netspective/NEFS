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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xdm.XmlDataModelSchema;

/**
 * A search path implementation that allows users to specify a set of directories through either XDM or a properties
 * file and then allow callers to locate a file or directory from the search path.
 */
public class FileSearchPath
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    public class PathElement
    {
        private File path;

        public PathElement()
        {
        }

        public PathElement(File path)
        {
            this.path = path;
        }

        public File getPath()
        {
            return path;
        }

        public void setPath(File path)
        {
            if(!path.isAbsolute())
            {
                final InputSourceLocator inputSourceLocator = relativeToLocator != null
                                                              ? relativeToLocator.getInputSourceLocator() : null;
                if(inputSourceLocator != null)
                    path = inputSourceLocator.getRelativeFile(path);
                else if(relativeToFile != null)
                    path = new File(relativeToFile, path.toString());
            }

            this.path = path;
        }

        public boolean isValid()
        {
            return path != null && (path.exists() && path.isDirectory());
        }
    }

    private List directories = new ArrayList();
    private final File relativeToFile;
    private final XmlDataModelSchema.InputSourceLocatorListener relativeToLocator;
    private String searchPathPropertyName = "searchPath";
    private String searchPathSeparatorPropertyName = ",";

    public FileSearchPath()
    {
        relativeToFile = null;
        relativeToLocator = null;
    }

    public FileSearchPath(File relativeToFile)
    {
        this.relativeToFile = relativeToFile;
        this.relativeToLocator = null;
    }

    public FileSearchPath(XmlDataModelSchema.InputSourceLocatorListener relativeToLocator)
    {
        this.relativeToFile = null;
        this.relativeToLocator = relativeToLocator;
    }

    /**
     * Find the first directory in the search path exists.
     *
     * @return The first directory in the search path that exists
     */
    public File getFirstValidDirectory()
    {
        for(int i = 0; i < directories.size(); i++)
        {
            PathElement path = (PathElement) directories.get(i);
            if(path.isValid())
                return path.getPath();
        }

        return null;
    }

    /**
     * Find the first directory in the search path with the file name provided.
     *
     * @return The first directory in the search path that exists
     */
    public File getFirstValidFile(String name)
    {
        for(int i = 0; i < directories.size(); i++)
        {
            PathElement path = (PathElement) directories.get(i);
            if(path.isValid())
            {
                File test = new File(path.getPath(), name);
                if(test.exists())
                    return test;
            }
        }

        return null;
    }

    public List getDirectories()
    {
        return directories;
    }

    public PathElement createDirectory()
    {
        return new PathElement();
    }

    public void addDirectory(PathElement directory)
    {
        directories.add(directory);
    }

    public String getSearchPathPropertyName()
    {
        return searchPathPropertyName;
    }

    public void setSearchPathPropertyName(String searchPathPropertyName)
    {
        this.searchPathPropertyName = searchPathPropertyName;
    }

    public String getSearchPathSeparatorPropertyName()
    {
        return searchPathSeparatorPropertyName;
    }

    public void setSearchPathSeparatorPropertyName(String searchPathSeparatorPropertyName)
    {
        this.searchPathSeparatorPropertyName = searchPathSeparatorPropertyName;
    }

    /**
     * If the search path should be obtained from a properties file, supply the properties here after setting up the
     * search path property name and path separator property names.
     *
     * @param properties The object that contains the properties.
     */
    public void setSearchPathPropertiesFile(Properties properties) throws IOException
    {
        String indexDirPropValue = properties.getProperty(searchPathPropertyName);
        String indexDirPathSepPropValue = properties.getProperty(searchPathSeparatorPropertyName, ",");
        if(indexDirPropValue == null)
            throw new IOException("Property '" + searchPathPropertyName + "' not found in supplied properties file " + properties.getProperty(PropertiesLoader.PROPNAME_PROPERTIES_SOURCE));

        String[] indexDirSearchPath = TextUtils.getInstance().split(indexDirPropValue, indexDirPathSepPropValue, true);
        for(int i = 0; i < indexDirSearchPath.length; i++)
        {
            final PathElement directory = createDirectory();
            directory.setPath(new File(indexDirSearchPath[i]));
            addDirectory(directory);
        }
    }

}
