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
 * $Id: RegexpFileFilter.java,v 1.3 2003-08-23 16:05:16 shahid.shah Exp $
 */

package com.netspective.commons.io;

import java.io.File;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.*;

public class RegexpFileFilter implements java.io.FileFilter
{
    boolean allowDirectories = true;
    boolean patternInitialized = false;
    int patternType = Perl5Compiler.DEFAULT_MASK;
    PatternCompiler pc = new Perl5Compiler();
    String fileRegexp = ".*";
    Pattern pattern = null;
    PatternMatcher pm = new Perl5Matcher();

    public RegexpFileFilter()
    {
    }

    public RegexpFileFilter(String fileRegexp)
    {
        this.fileRegexp = fileRegexp;
    }

    public RegexpFileFilter(String fileRegexp, boolean allowDirectories)
    {
        this.fileRegexp = fileRegexp;
        this.allowDirectories = allowDirectories;
    }

    /**
     * Tests whether or not the specified abstract pathname should be
     * included in a pathname list.
     *
     * @param  pathname  The abstract pathname to be tested
     * @return  <code>true</code> if and only if <code>pathname</code>
     *          should be included
     */
    public boolean accept(File pathname)
    {
        // if we're given a null pattern, return false...
        if (null == fileRegexp)
            return false;

        // if the pattern hasnt been initialized yet, initialize it...
        if (!patternInitialized)
            try
            {
                pattern = pc.compile(fileRegexp, patternType);
                patternInitialized = true;
            }
            catch (MalformedPatternException e)
            {
                pattern = null;
                patternInitialized = true;
            }

        // if the pattern is null => previous initialization failed => return false so no files are 'accepted'
        if (null == pattern)
            return false;

        // if this is a dir _and_ dirs are allowed, dont check their filenames...
        if (pathname.isDirectory())
            return (allowDirectories ? true : false);

        // otherwise, do a regexp match...
        return pm.contains(pathname.getName(), pattern);
    }

    public String getFileRegexp()
    {
        return fileRegexp;
    }

    public void setFileRegexp(String fileRegexp)
    {
        this.fileRegexp = fileRegexp;
        patternInitialized = false;
    }

    public void setCaseSensitive(boolean sensitivity)
    {
        if (false == sensitivity)
            patternType = Perl5Compiler.DEFAULT_MASK | Perl5Compiler.SINGLELINE_MASK | Perl5Compiler.CASE_INSENSITIVE_MASK;
        else
            patternType = Perl5Compiler.DEFAULT_MASK;

        patternInitialized = false;
    }

    public boolean isCaseSensitive()
    {
        return ((patternType & Perl5Compiler.CASE_INSENSITIVE_MASK) == 0);
    }

    public boolean isAllowDirectories()
    {
        return allowDirectories;
    }

    public void setAllowDirectories(boolean allowDirectories)
    {
        this.allowDirectories = allowDirectories;
        patternInitialized = false;
    }
}
