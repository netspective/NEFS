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
 * $Id: ReportTest.java,v 1.1 2003-03-27 22:22:20 shahid.shah Exp $
 */

package com.netspective.commons.report;

import java.io.StringReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.TestCase;

import org.xml.sax.InputSource;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XmlDataModelDtd;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.io.Resource;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.DefaultValueContext;
import com.netspective.commons.config.*;

public class ReportTest extends TestCase
{
	public static String RESOURCE_NAME_ONE = "ReportTest-One.xml";

    public void testComponent() throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException
    {
	    ReportsComponent component =
	            (ReportsComponent) XdmComponentFactory.get(
	                    ReportsComponent.class,
	                    new Resource(ReportTest.class, RESOURCE_NAME_ONE),
	                    XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

        assertEquals(0, component.getErrors().size());
        if(component.getErrors().size() > 0)
                System.out.println(component.getErrors());

        ReportsManager reportsManager = component.getItems();
        Reports reports = reportsManager.getReports();
        //System.out.println(reports.size());
    }

    public void testComponentDtd() throws DataModelException
    {
        ReportsComponent component = new ReportsComponent();
        String dtd = new XmlDataModelDtd().getDtd(component);
        assertTrue(dtd != null);

        //System.out.println(dtd);
    }
}
