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
 * $Id: GlobabllyUniqueIdValueSourceTest.java,v 1.2 2003-03-21 13:54:46 shahbaz.javeed Exp $
 */

package com.netspective.commons.value.source;

import java.util.Map;
import java.util.HashMap;
import junit.framework.TestCase;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.Value;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.perl.MalformedPerl5PatternException;

public class GlobabllyUniqueIdValueSourceTest extends TestCase
{
    public void testGetValue()
    {
		Perl5Util perlUtil = new Perl5Util();
	    String guid32Filter = "/[A-Z0-9]{8}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{12}/";

		int maxGuids = 2500;
	    int minUniqueGuids = 2500;
	    int maxMalformedGuids = 0;

		Map uniqueGuids = new HashMap();
	    int malformedGuids = 0;

		for (int i = 0; i < maxGuids; i ++)
		{
			ValueSource vs = ValueSources.getInstance().getValueSource("generate-id:", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
			assertTrue(vs.hasValue(null));

			Value value = vs.getValue(null);
			String guidValue = value.getTextValue();

			assertFalse(guidValue.equals(vs.getPresentationValue(null).getTextValue()));

			try {
				assertTrue(perlUtil.match(guid32Filter, guidValue));
			} catch (MalformedPerl5PatternException e) {
				malformedGuids ++;
			}

			uniqueGuids.put(guidValue, guidValue);
		}

	    // Statistical assertions for uniqueness of generated guids...
	    assertTrue(maxMalformedGuids >= malformedGuids);
	    assertTrue(minUniqueGuids >= uniqueGuids.size());

		uniqueGuids.clear();

		for (int i = 0; i < maxGuids; i ++)
		{
			ValueSource vs = ValueSources.getInstance().getValueSource("generate-id:,yes", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
			assertTrue(vs.hasValue(null));

			Value value = vs.getValue(null);
			String guidValue = value.getTextValue();

			assertFalse(guidValue.equals(vs.getPresentationValue(null).getTextValue()));

			try {
				assertTrue(perlUtil.match(guid32Filter, guidValue));
			} catch (MalformedPerl5PatternException e) {
				malformedGuids ++;
			}

			uniqueGuids.put(guidValue, guidValue);
		}

	    // Statistical assertions for uniqueness of generated guids...
	    assertTrue(maxMalformedGuids >= malformedGuids);
	    assertTrue(minUniqueGuids >= uniqueGuids.size());

    }
}
