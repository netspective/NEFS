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
package com.netspective.sparx.navigate.fs;

import java.util.HashMap;
import java.util.Map;

import com.netspective.sparx.navigate.FileSystemEntry;
import com.netspective.sparx.theme.Theme;

public class DefaultFileSystemBrowserImages implements FileSystemBrowserImages
{
    private String defaultFile = "/images/files/file-type-default.gif";
    private String folderOpen = "/images/files/file-type-folder-open.gif";
    private String folderActive = "/images/files/file-type-folder-active.gif";
    private String folderClosed = "/images/files/file-type-folder-closed.gif";
    private Map fileTypes = new HashMap();

    public DefaultFileSystemBrowserImages()
    {
    }

    public String getFolderActiveImage()
    {
        return folderActive;
    }

    public void setFolderActive(String folderActive)
    {
        this.folderActive = folderActive;
    }

    public String getFolderClosedImage()
    {
        return folderClosed;
    }

    public void setFolderClosed(String folderClosed)
    {
        this.folderClosed = folderClosed;
    }

    public String getFolderOpenImage()
    {
        return folderOpen;
    }

    public void setFolderOpen(String folderOpen)
    {
        this.folderOpen = folderOpen;
    }

    public FileTypeImage createFileImage()
    {
        return new FileTypeImage();
    }

    public void addFileImage(FileTypeImage fileTypeImage)
    {
        fileTypes.put(fileTypeImage.getType().toLowerCase(), fileTypeImage.getSrc());
    }

    public String constructImageSrc(FileSystemEntry fileSystemEntry)
    {
        return "/images/files/file-type-" + fileSystemEntry.getEntryType().toLowerCase() + ".gif";
    }

    public String getImage(Theme theme, FileSystemEntry fileSystemEntry)
    {
        if (fileSystemEntry.getFile().isDirectory())
            return theme.getResourceUrl(folderClosed);

        String imageSrc = constructImageSrc(fileSystemEntry);
        String imageSrcUrl = theme.getResourceUrl(imageSrc, null);

        if (imageSrcUrl == null)
        {
            String image = (String) fileTypes.get(fileSystemEntry.getEntryType().toLowerCase());
            return theme.getResourceUrl(image != null ? image : defaultFile);
        }
        else
            return imageSrcUrl;
    }

    public class FileTypeImage
    {
        private String type;
        private String src;

        public String getSrc()
        {
            return src;
        }

        public void setSrc(String src)
        {
            this.src = src;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }
    }
}
