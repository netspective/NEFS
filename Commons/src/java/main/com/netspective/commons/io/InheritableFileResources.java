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
 * $Id: InheritableFileResources.java,v 1.1 2003-03-23 04:42:52 shahid.shah Exp $
 */

package com.netspective.commons.io;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueContext;

/**
 * Given a root directory and a root URL, recusively discover all resources (physical files, etc) that belong to all
 * the children of the root directory with relative identifiers.
 */
public class InheritableFileResources
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(InheritableFileResources.class);
    public static final String PATH_SEPARATOR = "/";

    public class Directory
    {
        private Directory parent;
        private File directory;
        private String activePathId;
        private List childDirectories = new ArrayList();
        private List directoryFiles = new ArrayList();

        public Directory(String activePathId, File directory)
        {
            this.activePathId = activePathId;
            this.directory = directory;
        }

        public Directory(Directory parent, String activePathId, File directory)
        {
            this(activePathId, directory);
            this.parent = parent;
        }

        public void discover()
        {
            if(log.isTraceEnabled())
                log.trace("discover() entered " + directory.getAbsolutePath());

            // first catalog all the files and directories in this path
            File[] files = directory.listFiles();
            for (int i = 0; files != null && i < files.length; i++)
            {
                File file = files[i];
                if(ignoreEntries.contains(file.getName()))
                    continue;

                if (file.isDirectory())
                {
                    Directory childDir = new Directory(this, activePathId + (activePathId.endsWith(PATH_SEPARATOR) ? "" : PATH_SEPARATOR) + file.getName(), file);
                    childDir.discover();
                    childDirectories.add(childDir);
                }
                else
                    directoryFiles.add(file);
            }

            // now "inherit" and resolve files that should be obtained from the parent in case they are not found in the child
            if(parent != null)
            {
                List parentFiles = parent.directoryFiles;
                for(int i = 0; i < parentFiles.size(); i++)
                {
                    File file = (File) parentFiles.get(i);
                    String fileName = file.getName();
                    int extnIndex = fileName.lastIndexOf(".");
                    String justNameNoExtn = extnIndex == -1 ? fileName : fileName.substring(0, extnIndex);
                    String activePathWithSep = activePathId + (activePathId.endsWith(PATH_SEPARATOR) ? "" : PATH_SEPARATOR);
                    filesByRelativeResourceName.put(activePathWithSep + justNameNoExtn, file);
                    relativeFileNamesByRelativeResourceName.put(activePathWithSep + justNameNoExtn, activePathWithSep + fileName);

                    if(log.isTraceEnabled())
                    {
                        log.trace("inherited file '" + file + "' as " + activePathWithSep + justNameNoExtn);
                        log.trace("inherited name '" + activePathWithSep + fileName + "' as " + activePathWithSep + justNameNoExtn);
                    }
                }
            }

            // now override all the files in our own directory
            for(int i = 0; i < directoryFiles.size(); i++)
            {
                File file = (File) directoryFiles.get(i);
                String fileName = file.getName();
                int extnIndex = fileName.lastIndexOf(".");
                String justNameNoExtn = extnIndex == -1 ? fileName : fileName.substring(0, extnIndex);
                String activePathWithSep = activePathId + (activePathId.endsWith(PATH_SEPARATOR) ? "" : PATH_SEPARATOR);
                filesByRelativeResourceName.put(activePathWithSep + justNameNoExtn, file);
                relativeFileNamesByRelativeResourceName.put(activePathWithSep + justNameNoExtn, activePathWithSep + fileName);

                if(log.isTraceEnabled())
                {
                    log.trace("assigned file '" + file + "' as " + activePathWithSep + justNameNoExtn);
                    log.trace("assigned name '" + activePathWithSep + fileName + "' as " + activePathWithSep + justNameNoExtn);
                }
            }
        }
    }

    private Map filesByRelativeResourceName = new HashMap();
    private Map relativeFileNamesByRelativeResourceName = new HashMap();
    private Set ignoreEntries = new HashSet();
    private ValueSource rootPath;
    private File rootFile;
    private Directory rootDir;

    public InheritableFileResources()
    {
        ignoreEntries.add("CVS");
    }

    public File getRootDir()
    {
        return rootFile;
    }

    public void setRootDir(File rootDir)
    {
        this.rootFile = rootDir;
    }

    public ValueSource getRootPath()
    {
        return rootPath;
    }

    public void setRootPath(ValueSource rootPath)
    {
        this.rootPath = rootPath;
    }

    public void discover(ValueContext vc)
    {
        File activeDir = rootPath != null ? new File(rootPath.getTextValue(vc)) : rootFile;
        rootDir = new Directory("/", activeDir);
        rootDir.discover();
    }

    public Map getFilesByRelativeResourceName()
    {
        return filesByRelativeResourceName;
    }

    public Map getRelativeFileNamesByRelativeResourceName()
    {
        return relativeFileNamesByRelativeResourceName;
    }
}
