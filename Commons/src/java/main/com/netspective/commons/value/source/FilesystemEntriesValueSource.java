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
 * $Id: FilesystemEntriesValueSource.java,v 1.6 2004-08-09 22:14:28 shahid.shah Exp $
 */

package com.netspective.commons.value.source;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.oro.text.perl.Perl5Util;

import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.exception.ValueSourceInitializeException;

public class FilesystemEntriesValueSource extends AbstractValueSource implements FilenameFilter
{
    public static final String[] IDENTIFIERS = new String[] { "filesystem-entries" };
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation(
            "Provides list of files contained in a directory (either all files or by filter). If only a path is " +
            "provided then this LVS returns a list of all the files in the given path. If a regular expression is " +
            "provided (filter-reg-ex) then it must be a Perl5 regular expression that will be used to match the " +
            "files that should be included in the list. If the {include-path} parameter is set to 1 then the full "+
            "path included in the selected value otherwise just the filename is provided.",
            new ValueSourceDocumentation.Parameter[]
            {
                new ValueSourceDocumentation.Parameter("path", true, "The path to search in."),
                new ValueSourceDocumentation.Parameter("filter-reg-ex", false, ".*", "The Perl5 regular expression to use as filter."),
                new ValueSourceDocumentation.Parameter("include-path", false, "no", "Whether or not to include the path in the files returned.")
            }
    );

    static public Perl5Util perlUtil = new Perl5Util();
    static public String ALL_FILES_FILTER = "/.*/";

    private boolean includePathInValue;
    private ValueSource rootPathValue;
    private String filter;

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    public FilesystemEntriesValueSource()
    {
        filter = ALL_FILES_FILTER;
    }

    public boolean isPathInSelection()
    {
        return includePathInValue;
    }

    public void setIncludePathInSelection(boolean includePathInSelection)
    {
        this.includePathInValue = includePathInSelection;
    }

    public ValueSource getRootPath()
    {
        return rootPathValue;
    }

    public void setRootPath(String rootPath)
    {
        setRootPath(ValueSources.getInstance().getValueSourceOrStatic(rootPath));
    }

    public void setRootPath(ValueSource rootPath)
    {
        this.rootPathValue = rootPath;
    }

    public String getFilter()
    {
        return filter;
    }

    public void setFilter(String filter)
    {
        if(filter.startsWith("/"))
            this.filter = filter;
        else
            this.filter = "/" + filter + "/";
    }

    public boolean accept(File file, String s)
    {
        return perlUtil.match(filter, s);
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);

        StringTokenizer st = new StringTokenizer(spec.getParams(), ",");
        if(st.hasMoreTokens())
            setRootPath(st.nextToken());

        if(rootPathValue == null)
            throw new ValueSourceInitializeException("Root path was not specified ("+ spec.getParams() +")", this, spec);

        if(st.hasMoreTokens())
        {
            String filterParam = st.nextToken().trim();
            if(filterParam.equals(""))
                filter = ALL_FILES_FILTER;
            else
                setFilter(filterParam);
        }
        else
            filter = ALL_FILES_FILTER;
        if(st.hasMoreTokens())
            setIncludePathInSelection(TextUtils.getInstance().toBoolean(st.nextToken().trim()));
    }

    public Value getValue(ValueContext vc)
    {
        File rootPath = new File(rootPathValue.getValue(vc).getTextValue());
        String[] allFilesInPath = rootPath.list(this);
        List filesSelected = new ArrayList();

        if(allFilesInPath != null && allFilesInPath.length > 0)
        {
            for(int f = 0; f < allFilesInPath.length; f++)
            {
                File file = new File(rootPath, allFilesInPath[f]);
                filesSelected.add(file.getAbsolutePath());
            }
        }

        return new GenericValue(filesSelected);
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        PresentationValue result = new PresentationValue();
        PresentationValue.Items items = result.createItems();

        File rootPath = new File(rootPathValue.getValue(vc).getTextValue());
        String[] allFilesInPath = rootPath.list(this);

        if(allFilesInPath != null && allFilesInPath.length > 0)
        {
            if(!includePathInValue)
            {
                for(int f = 0; f < allFilesInPath.length; f++)
                {
                    // a string will mean that both the presentation (what to show to user) and the value (what to
                    // store as a value) will be the same
                    items.addItem(allFilesInPath[f]);
                }
            }
            else
            {
                for(int f = 0; f < allFilesInPath.length; f++)
                {
                    // a string[] will mean that both the presentation (what to show to user) and the value (what to
                    // store as a value) will be different
                    File file = new File(rootPath, allFilesInPath[f]);
                    items.addItem(file.getName(), file.getAbsolutePath());
                }
            }
        }

        return result;
    }

    public boolean hasValue(ValueContext vc)
    {
        Value value = getValue(vc);
        return value.getListValue().size() > 0;
    }
}
