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
package com.netspective.sparx.navigate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

public class FileSystemEntry
{
    public static final String ROOT_URI = "/";
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private int level;
    private File file;
    private FileSystemEntry rootPath;
    private String entryCaption;
    private String entryExtension;
    private String entryURI = ROOT_URI;
    private String contentType = DEFAULT_CONTENT_TYPE;

    public FileSystemEntry(int level, FileSystemEntry aRootPath, File file)
    {
        this.level = level;
        this.file = file;
        rootPath = aRootPath;
        entryCaption = file.getName().replace('_', ' ');
        int extnIndex = entryCaption.lastIndexOf('.');
        if (extnIndex > -1)
        {
            entryExtension = entryCaption.substring(extnIndex + 1);
            entryCaption = entryCaption.substring(0, extnIndex);
        }
        else
            entryExtension = "";

        // find the "relative" portion of the path (everything but the root path)
        if (rootPath != null)
        {
            String absPath = file.getAbsolutePath();
            entryURI = absPath.substring(rootPath.file.getAbsolutePath().length()).replace('\\', '/');
        }
    }

    public int getLevel()
    {
        return level;
    }

    public File getFile()
    {
        return file;
    }

    public FileSystemEntry getRootPath()
    {
        return rootPath;
    }

    public final String getEntryCaption()
    {
        return entryCaption;
    }

    public void setEntryCaption(String value)
    {
        entryCaption = value;
    }

    public final String getEntryType()
    {
        return entryExtension;
    }

    public void setEntryType(String value)
    {
        entryExtension = value;
    }

    public final String getEntryURI()
    {
        return entryURI;
    }

    public void setEntryURI(String value)
    {
        entryURI = value;
    }

    public final boolean isRoot()
    {
        return rootPath == null;
    }

    public List getParents()
    {
        ArrayList result = new ArrayList();
        if (rootPath == null)
            return result;

        String rootPathStr = rootPath.file.getAbsolutePath();
        int parentLevel = level - 1;
        File parent = file.getParentFile();
        while (parent != null)
        {
            boolean isRootPath = parent.getAbsolutePath().equals(rootPathStr);
            if (isRootPath)
            {
                result.add(0, rootPath);
                break;
            }
            else
            {
                result.add(0, new FileSystemEntry(parentLevel, rootPath, parent));
                parent = parent.getParentFile();
            }
            parentLevel--;
        }

        return result;
    }

    public static void getDirectoryFileTypes(Set types, File dir, boolean recursive)
    {
        if (!dir.isDirectory())
            return;

        File[] children = dir.listFiles();
        if (children == null)
            return;

        for (int i = 0; i < children.length; i++)
        {
            File child = children[i];
            int extnIndex = child.getName().lastIndexOf('.');
            if (extnIndex > -1)
                types.add(child.getName().substring(extnIndex + 1));

            if (recursive)
                getDirectoryFileTypes(types, child, recursive);
        }
    }

    public String[] getFileTypes(boolean recursive)
    {
        Set result = new TreeSet();
        getDirectoryFileTypes(result, file, recursive);
        return (String[]) result.toArray(new String[result.size()]);
    }

    public List getChildren()
    {
        ArrayList result = new ArrayList();
        File[] children = file.listFiles();
        for (int i = 0; i < children.length; i++)
        {
            File child = children[i];
            result.add(new FileSystemEntry(level + 1, rootPath, child));
        }
        return result;
    }

    public String findInPath(String fileName)
    {
        String testFile = file.getAbsolutePath() + file.separator + fileName;
        if (new File(testFile).exists())
            return testFile;

        if (rootPath == null)
            return null;

        String rootPathStr = rootPath.file.getAbsolutePath();
        File parent = file.getParentFile();
        while (parent != null)
        {
            testFile = parent.getAbsolutePath() + parent.separator + fileName;
            if (new File(testFile).exists())
                return testFile;

            if (parent.getAbsolutePath().equals(rootPathStr))
                break;
            else
                parent = parent.getParentFile();
        }

        return null;
    }

    public int send(final HttpServletResponse response, final String contentType) throws IOException
    {
        int sentsize = 0;

        InputStream in = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));

        response.setContentType(contentType == null ? this.contentType : contentType);
        response.setContentLength((int) file.length()); // seems that here sits a possible problem for files >= 2 GB...

        OutputStream out = response.getOutputStream();

        int readlen;
        byte buffer[] = new byte[256];

        while ((readlen = in.read(buffer)) != -1)
        {
            // throws IOException: broken pipe when download is canceled.
            out.write(buffer, 0, readlen);
            sentsize += readlen;
        }

        // Success ! Close streams.
        out.flush();
        out.close();

        in.close();

        return sentsize;
    }
}
