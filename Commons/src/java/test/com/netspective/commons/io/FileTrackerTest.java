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
package com.netspective.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

/**
 * $Id: FileTrackerTest.java,v 1.3 2004-08-15 01:41:14 shahid.shah Exp $
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
        while ((System.currentTimeMillis() - currTime) < 1000) ;

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
        while (currTime == System.currentTimeMillis()) ;
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
        while (newTime == System.currentTimeMillis()) ;
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
        while (newTime == System.currentTimeMillis()) ;
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
        while (newTime == System.currentTimeMillis()) ;
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
        while (newTime == System.currentTimeMillis()) ;
        newTime = System.currentTimeMillis();
        ffrFive.getFoundFile().setLastModified(newTime);

        assertTrue(parentTracker.sourceChanged());
        assertTrue(childTracker.sourceChanged());
        assertFalse(trackerThree.sourceChanged());
        assertFalse(trackerFour.sourceChanged());
        assertTrue(trackerFive.sourceChanged());

    }
}
