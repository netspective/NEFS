package com.netspective.commons.io;

import java.io.IOException;
import java.io.File;
import junit.framework.TestCase;

/**
 * $Id: RegexpFileFilterTest.java,v 1.1 2003-03-25 08:02:36 shahbaz.javeed Exp $
 */
public class RegexpFileFilterTest extends TestCase
{
	public void testRegexpFileFilterErrors() throws IOException
	{
		// Test recursive search for an existing test dir...
		FileFind.FileFindResults ffrDirRecursiveTwo = FileFind.findInClasspath("FindFileTestDirectory", FileFind.FINDINPATHFLAG_SEARCH_FILE_MAY_BE_DIRECTORY | FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY | FileFind.FINDINPATHFLAG_SEARCH_INSIDE_ARCHIVES_DURING);
		assertNotNull(ffrDirRecursiveTwo);
		assertTrue(ffrDirRecursiveTwo.isFileFound());
        assertNotNull(ffrDirRecursiveTwo.getFoundFile());

		// ... locate where it was found ...
		String[] searchPath = ffrDirRecursiveTwo.getSearchPaths();
		int searchPathIdx = ffrDirRecursiveTwo.getFoundFileInPathItem();

		// ... and use it to search again using the regular Java File.listFiles() routine to test RegexpFileFilter
		// Mess around with the regexp file filter class...
		// Ensure that the default file filter does return the right result...
		RegexpFileFilter rff = new RegexpFileFilter();
		assertEquals(".*", rff.getFileRegexp());
		assertTrue(rff.isCaseSensitive());
		assertTrue(rff.isAllowDirectories());
		// This should be the src/test directory in the Commons hier...
        File testDir = ffrDirRecursiveTwo.getFoundFile();
		assertTrue(testDir.isDirectory());

		File[] foundFiles = null;
		foundFiles = testDir.listFiles(rff);
		// These sixteen files should be there in the test dir...
		assertEquals(16, foundFiles.length);

		rff.setAllowDirectories(false);
		assertFalse(rff.isAllowDirectories());
		assertTrue(rff.isCaseSensitive());
		assertEquals(".*", rff.getFileRegexp());
		foundFiles = testDir.listFiles(rff);
		// Since three of the 'files' found previously were dirs, this should return 13
		assertEquals(13, foundFiles.length);

		rff = new RegexpFileFilter("make");
		assertTrue(rff.isAllowDirectories());
		assertTrue(rff.isCaseSensitive());
		assertEquals("make", rff.getFileRegexp());
		foundFiles = testDir.listFiles(rff);
		// This should pick out no files since this is case-sensitive and Makefile doesnt match "make" ... the three
		// dirs should still be returned though...
		assertEquals(3, foundFiles.length);

		rff.setCaseSensitive(false);
		assertFalse(rff.isCaseSensitive());
		assertEquals("make", rff.getFileRegexp());
		foundFiles = testDir.listFiles(rff);
		// Matches Makefile and the three dirs
		assertEquals(4, foundFiles.length);

		// Now test it with a messed up pattern...
		rff = new RegexpFileFilter("file[");
		assertTrue(rff.isAllowDirectories());
		assertTrue(rff.isCaseSensitive());
		assertEquals("file[", rff.getFileRegexp());
		foundFiles = testDir.listFiles(rff);
		// Since the regexp is messed up, no files should be returned...  not even the three dirs
		assertEquals(0, foundFiles.length);

		// Finally test it with a null pattern...
		rff = new RegexpFileFilter(null);
		assertTrue(rff.isAllowDirectories());
		assertTrue(rff.isCaseSensitive());
		assertNull(rff.getFileRegexp());
		foundFiles = testDir.listFiles(rff);
		// Since the regexp is null, no files should be returned...  not even the three dirs
		assertEquals(0, foundFiles.length);
	}
}
