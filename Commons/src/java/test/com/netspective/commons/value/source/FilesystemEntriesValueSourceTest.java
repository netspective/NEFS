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
 * $Id: FilesystemEntriesValueSourceTest.java,v 1.3 2003-03-23 16:49:03 shahbaz.javeed Exp $
 */

package com.netspective.commons.value.source;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import junit.framework.TestCase;

import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.Value;

public class FilesystemEntriesValueSourceTest extends TestCase
{
    public void testGetValue()
    {
        // in 'c\\:\\' the first \\ is to escape ':' because otherwise c: will be regarded as its own value source
        // because the rootPath in FileSystemEntriesValueSource is a ValueSource, not a string
        String rootPath = "c\\:\\";
	    String unescapedRootPath = "c:\\";

        ValueSource vs = ValueSources.getInstance().getValueSource("filesystem-entries:" + rootPath, ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
	    FilesystemEntriesValueSource fsVS = new FilesystemEntriesValueSource();
	    fsVS.setRootPath(rootPath);
        Value value = vs.getValue(null);

	    assertEquals(unescapedRootPath, fsVS.getRootPath().getTextValue(null));
		// Verify the presence of the default filter...
	    assertEquals("/.*/", fsVS.getFilter());
	    assertTrue(0 < vs.getTextValues(null).length);
	    assertNotNull(vs.getTextValueOrBlank(null));

		String[] theValue = value.getTextValues();
	    List altValue = fsVS.getPresentationValue(null).getListValue();

		assertEquals(theValue.length, altValue.size());
	    assertFalse(fsVS.isPathInSelection());
	    for (int i = 0; i < theValue.length; i ++)
	        assertFalse(altValue.contains(theValue[i]));

		fsVS.setIncludePathInSelection(true);
	    List altValueWithPath = fsVS.getPresentationValue(null).getListValue();

	    assertEquals(theValue.length, altValueWithPath.size());
	    assertTrue(fsVS.isPathInSelection());
	    for (int i = 0; i < theValue.length; i ++)
	    {
	        assertEquals(theValue[i], ((String[]) altValueWithPath.get(i))[1]);
		    assertEquals(altValue.get(i), ((String[]) altValueWithPath.get(i))[0]);
	    }

	    // Try the same tests with a filter...
		// only files/dirs that contain the letter s in them...
	    fsVS.setFilter("[sS]");
	    assertEquals("/[sS]/", fsVS.getFilter());
	    assertTrue(fsVS.isPathInSelection());

	    List filterValue = fsVS.getPresentationValue(null).getListValue();
	    assertTrue(filterValue.size() <= altValue.size());

	    List altValueWithPathList  = new ArrayList();
	    for (int i = 0; i < altValueWithPath.size(); i ++)
		    altValueWithPathList.add(((String[]) altValueWithPath.get(i))[1]);

	    assertEquals(altValueWithPath.size(), altValueWithPathList.size());

	    for (int i = 0; i < filterValue.size(); i ++)
		    assertTrue(altValueWithPathList.contains(((String[]) filterValue.get(i))[1]));



        vs = ValueSources.getInstance().getValueSource("filesystem-entries:" + rootPath + ",exe$", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
        value = vs.getValue(null);
//        System.out.println(value.getTextValue());
//        System.out.println(value.getTextValues());
//        System.out.println(value.getListValue());
    }
}
