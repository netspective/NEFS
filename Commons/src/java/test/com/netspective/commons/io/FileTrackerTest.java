package com.netspective.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;

/**
 * $Id: FileTrackerTest.java,v 1.2 2003-11-08 18:30:45 shahid.shah Exp $
 */
public class FileTrackerTest extends TestCase
{
	public static String fileOne = "test-file-02.xml";
	public static String fileTwo = "test-file-03.xml";
	public static String fileThree = "test-file-01.txt";
	public static String fileFour = "test-file-05.dll";
	public static String fileFive = "test-file-04.exe";

	File parent;
	File child;

	public void testFileTrackerSingleFile() throws InterruptedException
	{
		// Find the file first...
		FileFind.FileFindResults ffrOne = FileFind.findInClasspath(fileOne, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
		assertTrue(ffrOne.isFileFound());
		parent = ffrOne.getFoundFile();

		// Set the last modified time to a known value...
		long currTime = System.currentTimeMillis();
		parent.setLastModified(currTime);

		FileTracker parentTracker = new FileTracker();
		parentTracker.setFile(parent);

		//assertEquals(currTime, parent.lastModified());
		assertFalse(parentTracker.sourceChanged());

		// Wait for the current time to change (1 second)...
		while ((System.currentTimeMillis() - currTime) < 1000);

		// Change the modified time to a new known value... different from the previous
		long newTime = System.currentTimeMillis();
		assertTrue(newTime > currTime);
		assertTrue(parent.lastModified() < newTime);
		parent.setLastModified(newTime);
		//assertEquals(newTime, parent.lastModified());
		assertTrue("Source should show change since " + newTime + " is greater than " + currTime, parentTracker.sourceChanged());
	}

	public void testFileTrackerMultipleFiles() throws IOException
	{
		// Find the first file...
		FileFind.FileFindResults ffrOne = FileFind.findInClasspath(fileOne, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
		assertTrue(ffrOne.isFileFound());
		parent = ffrOne.getFoundFile();

		// Set the last modified time to a known value...
		long currTime = System.currentTimeMillis();

		FileTracker parentTracker = new FileTracker();
		parentTracker.setFile(parent);
		assertFalse(parentTracker.sourceChanged());
		assertEquals(parent.getCanonicalPath(), parentTracker.getIdentifier());

		// Find the second file...
		FileFind.FileFindResults ffrTwo = FileFind.findInClasspath(fileTwo, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
		assertTrue(ffrTwo.isFileFound());
		child = ffrTwo.getFoundFile();

		// Set the last modified time to a known value...
		FileTracker childTracker = new FileTracker();
		childTracker.setFile(child);
		assertFalse(childTracker.sourceChanged());
		assertEquals(child.getCanonicalPath(), childTracker.getIdentifier());

		// Assign 'child' as an include for 'parent' => create a dependency
		parentTracker.addInclude(childTracker);

		// Find the rest of the files ...
		FileFind.FileFindResults ffrThree = FileFind.findInClasspath(fileThree, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
		assertTrue(ffrThree.isFileFound());

		FileFind.FileFindResults ffrFour = FileFind.findInClasspath(fileFour, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
		assertTrue(ffrFour.isFileFound());

		FileFind.FileFindResults ffrFive = FileFind.findInClasspath(fileFive, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
		assertTrue(ffrFive.isFileFound());

		// ... and add a few more preprocessors and parentInc ...
		parentTracker.addPreProcessor(ffrFive.getFoundFile().getCanonicalPath());
		parentTracker.addInclude(ffrThree.getFoundFile());
		childTracker.addInclude(ffrFour.getFoundFile().getCanonicalPath());
		childTracker.addPreProcessor(ffrFive.getFoundFile());

/*
		Current structure:
		         +- PreProcessors: fileFive
		fileOne -|                             +- PreProcessors: fileFive
		(parent) +- Includes: fileTwo (child) -|
		                      fileThree        +- Includes: fileFour

		parent | child ||fileOne | fileTwo | fileThree | fileFour | fileFive
		--------------------------------------------------------------------
		   1   |   0   ||   1    |    0    |     0     |    0     |    0
		   1   |   1   ||   0    |    1    |     0     |    0     |    0
		   1   |   0   ||   0    |    0    |     1     |    0     |    0
		   1   |   1   ||   0    |    0    |     0     |    1     |    0
		   1   |   1   ||   0    |    0    |     0     |    0     |    1

		This table indicates the effect on parent and child if each of the files
		are changed individually.  For multiple changed files, the effects will be or'ed together.
*/

		// Verify the structure of the current FileTracker hier...
		assertEquals(3, parentTracker.getDependenciesCount());

		// first the includes ...
		List parentInc = parentTracker.getIncludes();
		FileTracker trackerThree = (FileTracker) parentInc.get(1);
		assertEquals(2, parentInc.size());
		assertSame(childTracker, parentInc.get(0));
		assertEquals(ffrThree.getFoundFile().getCanonicalPath(), trackerThree.getIdentifier());
		assertEquals(0, trackerThree.getDependenciesCount());

		assertEquals(2, childTracker.getDependenciesCount());
		List childInc = childTracker.getIncludes();
		FileTracker trackerFour = (FileTracker) childInc.get(0);
		assertEquals(0, trackerFour.getDependenciesCount());
		assertEquals(ffrFour.getFoundFile().getCanonicalPath(), trackerFour.getIdentifier());

		// ... then the preprocessors...
		List parentPreProc = parentTracker.getPreProcessors();
		FileTracker trackerFive = (FileTracker) parentPreProc.get(0);
		assertEquals(1, parentPreProc.size());
		assertEquals(ffrFive.getFoundFile().getCanonicalPath(), trackerFive.getIdentifier());

		assertEquals(trackerFive.getIdentifier(), ((FileTracker) childTracker.getPreProcessors().get(0)).getIdentifier());

		// Now do the modification tests ...

		// Modify fileOne and observe sourceChanged() signals...
		while (currTime == System.currentTimeMillis());
		long newTime = System.currentTimeMillis();
		parent.setLastModified(newTime);

		assertTrue(parentTracker.sourceChanged());
		assertFalse(childTracker.sourceChanged());
		assertFalse(trackerThree.sourceChanged());
		assertFalse(trackerFour.sourceChanged());
		assertFalse(trackerFive.sourceChanged());

		parentTracker.reset();
		childTracker.reset();
		trackerThree.reset();
		trackerFour.reset();
		trackerFive.reset();

		// Modify fileTwo and observe sourceChanged() signals...
		while (newTime == System.currentTimeMillis());
		newTime = System.currentTimeMillis();
		child.setLastModified(newTime);

		assertTrue(parentTracker.sourceChanged());
		assertTrue(childTracker.sourceChanged());
		assertFalse(trackerThree.sourceChanged());
		assertFalse(trackerFour.sourceChanged());
		assertFalse(trackerFive.sourceChanged());

		parentTracker.reset();
		childTracker.reset();
		trackerThree.reset();
		trackerFour.reset();
		trackerFive.reset();

		// Modify fileThree and observe sourceChanged() signals...
		while (newTime == System.currentTimeMillis());
		newTime = System.currentTimeMillis();
		ffrThree.getFoundFile().setLastModified(newTime);

		assertTrue(parentTracker.sourceChanged());
		assertFalse(childTracker.sourceChanged());
		assertTrue(trackerThree.sourceChanged());
		assertFalse(trackerFour.sourceChanged());
		assertFalse(trackerFive.sourceChanged());

		parentTracker.reset();
		childTracker.reset();
		trackerThree.reset();
		trackerFour.reset();
		trackerFive.reset();

		// Modify fileFour and observe sourceChanged() signals...
		while (newTime == System.currentTimeMillis());
		newTime = System.currentTimeMillis();
		ffrFour.getFoundFile().setLastModified(newTime);

		assertTrue(parentTracker.sourceChanged());
		assertTrue(childTracker.sourceChanged());
		assertFalse(trackerThree.sourceChanged());
		assertTrue(trackerFour.sourceChanged());
		assertFalse(trackerFive.sourceChanged());

		parentTracker.reset();
		childTracker.reset();
		trackerThree.reset();
		trackerFour.reset();
		trackerFive.reset();

		// Modify fileFive and observe sourceChanged() signals...
		while (newTime == System.currentTimeMillis());
		newTime = System.currentTimeMillis();
		ffrFive.getFoundFile().setLastModified(newTime);

		assertTrue(parentTracker.sourceChanged());
		assertTrue(childTracker.sourceChanged());
		assertFalse(trackerThree.sourceChanged());
		assertFalse(trackerFour.sourceChanged());
		assertTrue(trackerFive.sourceChanged());

	}
}
