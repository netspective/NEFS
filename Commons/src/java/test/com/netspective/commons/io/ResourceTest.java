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
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * $Id: ResourceTest.java,v 1.4 2004-08-15 01:41:14 shahid.shah Exp $
 */
public class ResourceTest extends TestCase
{
    final private String fileOne = "FindFileTestDirectory";
    final private String fileTwo = "FindFileTestDirectory/file11.c";

    public void testResource() throws IOException
    {
        Resource rsrcOne = new Resource(ResourceTest.class, fileOne);
        String fqrn = "resource://" + this.getClass().getName() + "/" + fileOne;

        assertEquals(fqrn, rsrcOne.getSystemId());
        assertEquals(fileOne, rsrcOne.getResourceName());
        assertEquals(this.getClass(), rsrcOne.getSourceClass());
        assertNull(rsrcOne.getSourceLoader());
        assertTrue(rsrcOne.isPhysicalFile());

        File rsrcFileOne = rsrcOne.getFile();
        assertTrue(rsrcFileOne.isDirectory());

        // Build the resource name by changing all \ to / (if needed) and prepending a 'file:/' to the result...
        String fileOneFQN = rsrcFileOne.getCanonicalPath();
        if (File.separatorChar == '\\')
            fileOneFQN = "/" + fileOneFQN.replace('\\', '/');
        assertEquals("file:" + fileOneFQN, rsrcOne.getResource().toString());

        Resource rsrcTwo = new Resource(this.getClass().getClassLoader(), fileTwo);
        assertFalse(rsrcTwo.isPhysicalFile());
        assertNotNull(rsrcTwo.getSourceLoader());

        rsrcTwo = new Resource(this.getClass(), fileTwo);
        String fqrnTwo = "resource://" + this.getClass().getName() + "/" + fileTwo;
        assertEquals(fqrnTwo, rsrcTwo.getSystemId());
        assertEquals(fileTwo, rsrcTwo.getResourceName());
        assertEquals(this.getClass(), rsrcTwo.getSourceClass());
        assertNull(rsrcTwo.getSourceLoader());
        assertTrue(rsrcTwo.isPhysicalFile());

        File rsrcFileTwo = rsrcTwo.getFile();
        assertFalse(rsrcFileTwo.isDirectory());
        assertTrue(rsrcFileTwo.isFile());

        // Build the resource name by changing all \ to / (if needed) and prepending a 'file:/' to the result...
        String fileTwoFQN = rsrcFileTwo.getCanonicalPath();
        if (File.separatorChar == '\\')
            fileTwoFQN = "/" + fileTwoFQN.replace('\\', '/');
        assertEquals("file:" + fileTwoFQN, rsrcTwo.getResource().toString());

        InputStream is = rsrcTwo.getResourceAsStream();
        // 0 byte file...
        assertEquals(0, is.available());
        is.close();
    }
}
