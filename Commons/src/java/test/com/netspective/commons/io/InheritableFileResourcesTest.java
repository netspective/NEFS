package com.netspective.commons.io;

import java.io.IOException;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import junit.framework.TestCase;

/**
 * $Id: InheritableFileResourcesTest.java,v 1.1 2003-03-25 17:51:11 shahbaz.javeed Exp $
 */
public class InheritableFileResourcesTest extends TestCase
{
	public void testInheritableFileResources() throws IOException
	{
		String[] relativeFiles = new String[] {
			"/Makefile",
			"/file01",
			"/file02",
			"/file03",
			"/file04",
			"/file05",
			"/file06",
			"/file07",
			"/file08",
			"/file09",
			"/file10",
			"/file11",
			"/file12",
			"/dir02/findfile-test-01",
			"/dir02/filefind-test-01",
			"/dir02/filefind-final-01",
			"/dir02/findfile-test-02",
			"/dir01/test-file-01",
			"/dir01/test-file-02",
			"/dir01/test-file-03",
			"/dir01/test-file-04",
			"/dir01/test-file-05",
		};

		// Test recursive search for an existing test dir...
		FileFind.FileFindResults ffrDirRecursiveTwo = FileFind.findInClasspath("FindFileTestDirectory", FileFind.FINDINPATHFLAG_SEARCH_FILE_MAY_BE_DIRECTORY | FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY | FileFind.FINDINPATHFLAG_SEARCH_INSIDE_ARCHIVES_DURING);
		assertNotNull(ffrDirRecursiveTwo);
		assertTrue(ffrDirRecursiveTwo.isFileFound());
        assertNotNull(ffrDirRecursiveTwo.getFoundFile());

        // ... and use it as the root for our inheritable file resources test...
		InheritableFileResources ifr = new InheritableFileResources();
		assertNotNull(ifr);
		ifr.setRootDir(ffrDirRecursiveTwo.getFoundFile());
		assertEquals(ffrDirRecursiveTwo.getFoundFile().getCanonicalPath(), ifr.getRootDir().getCanonicalPath());

		ifr.discover(null);
		Map filesBRRN = ifr.getFilesByRelativeResourceName();
        assertEquals(relativeFiles.length, filesBRRN.size());
		for (int i = 0; i < relativeFiles.length; i ++)
			assertTrue(filesBRRN.containsKey(relativeFiles[i]));

		Map filesRBRRN = ifr.getRelativeFileNamesByRelativeResourceName();
		assertEquals(relativeFiles.length, filesRBRRN.size());
		for (int i = 0; i < relativeFiles.length; i ++)
			assertTrue(filesRBRRN.containsKey(relativeFiles[i]));
	}
}
