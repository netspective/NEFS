package com.netspective.commons.io;

import java.io.File;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.*;

/**
 * $Id: RegexpFileFilter.java,v 1.2 2003-03-25 08:01:31 shahbaz.javeed Exp $
 */
public class RegexpFileFilter implements java.io.FileFilter
{
	boolean allowDirectories = true;
	boolean patternInitialized = false;
	int patternType = Perl5Compiler.DEFAULT_MASK;
	PatternCompiler pc = new Perl5Compiler();
	String fileRegexp = ".*";
	Pattern pattern = null;
	PatternMatcher pm = new Perl5Matcher();

	public RegexpFileFilter ()
	{
	}

	public RegexpFileFilter (String fileRegexp) {
		this.fileRegexp = fileRegexp;
	}

	public RegexpFileFilter (String fileRegexp, boolean allowDirectories) {
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
	public boolean accept (File pathname) {
		// if we're given a null pattern, return false...
		if (null == fileRegexp)
			return false;

		// if the pattern hasnt been initialized yet, initialize it...
		if (!patternInitialized)
			try {
				pattern = pc.compile(fileRegexp, patternType);
				patternInitialized = true;
			} catch (MalformedPatternException e) {
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

	public String getFileRegexp ()
	{
		return fileRegexp;
	}

	public void setFileRegexp (String fileRegexp)
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

	public boolean isAllowDirectories ()
	{
		return allowDirectories;
	}

	public void setAllowDirectories (boolean allowDirectories)
	{
		this.allowDirectories = allowDirectories;
		patternInitialized = false;
	}
}
