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
package com.netspective.sparx.navigate.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.oro.text.perl.Perl5Util;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;

/**
 * Allows the navigation of a HTML website but makes it looks like it's part of the same theme (including headers, footers, etc)
 */

public class NavigationPageHtmlSubsiteBodyHandler extends NavigationPageBodyDefaultHandler
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    protected static class Substitute
    {

        private ValueSource perlRegEx;

        public Substitute()
        {
        }

        public ValueSource getPerlRegEx()
        {
            return perlRegEx;
        }

        public void setPerlRegEx(ValueSource perlRegEx)
        {
            this.perlRegEx = perlRegEx;
        }

        // created here because we need to ignore text but can't include public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
        public void addText(String text)
        {

        }
    }

    private ValueSource indexFile = new StaticValueSource("index.html");
    private ValueSource root;
    private List substitutions = new ArrayList();
    private String fileNotFoundMessage = "URI {0} does not exist";

    public ValueSource getIndexFile()
    {
        return indexFile;
    }

    public void setIndexFile(ValueSource indexFile)
    {
        this.indexFile = indexFile;
    }

    public ValueSource getRoot()
    {
        return root;
    }

    public void setRoot(ValueSource root)
    {
        this.root = root;
    }

    public Substitute createSubstitute()
    {
        return new Substitute();
    }

    public void addSubstitute(Substitute replace)
    {
        substitutions.add(replace);
    }

    public String getFileNotFoundMessage()
    {
        return fileNotFoundMessage;
    }

    public void setFileNotFoundMessage(String fileNotFoundMessage)
    {
        this.fileNotFoundMessage = fileNotFoundMessage;
    }

    public void handleNavigationPageBody(NavigationPage page, Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        if (root == null)
        {
            writer.write("No root path specified.");
            return;
        }

        File rootPath = new File(root.getTextValue(nc));

        StringBuffer activePath = new StringBuffer(rootPath.getAbsolutePath());
        String relativePath = nc.getActivePathFindResults().getUnmatchedPath(0);
        if (relativePath != null && relativePath.length() > 0)
            activePath.append(relativePath);

        File activeFile = new File(activePath.toString());
        if (activeFile.isDirectory())
            activeFile = new File(activeFile.getAbsolutePath() + "/" + indexFile.getTextValue(nc));

        if (!activeFile.exists())
        {
            writer.write(MessageFormat.format(getFileNotFoundMessage(), new Object[]{activeFile.getAbsolutePath()}));
            return;
        }

        FileReader fr = new FileReader(activeFile);
        BufferedReader br = new BufferedReader(fr);

        try
        {
            if (substitutions.size() > 0)
            {
                final Perl5Util perl5Util = new Perl5Util();
                final String[] regExs = new String[substitutions.size()];

                for (int i = 0; i < substitutions.size(); i++)
                {
                    Substitute substitute = (Substitute) substitutions.get(i);
                    regExs[i] = substitute.getPerlRegEx().getTextValue(nc);
                }

                String rec;
                while ((rec = br.readLine()) != null)
                {
                    for (int i = 0; i < regExs.length; i++)
                        rec = perl5Util.substitute(regExs[i], rec);
                    writer.write(rec);
                    writer.write("\n");
                }
            }
            else
            {
                int c;
                while ((c = br.read()) != -1)
                    writer.write(c);
            }
        }
        finally
        {
            br.close();
            fr.close();
        }
    }
}
