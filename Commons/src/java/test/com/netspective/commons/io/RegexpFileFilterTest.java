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

import junit.framework.TestCase;

/**
 * $Id: RegexpFileFilterTest.java,v 1.2 2004-08-15 01:41:14 shahid.shah Exp $
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
