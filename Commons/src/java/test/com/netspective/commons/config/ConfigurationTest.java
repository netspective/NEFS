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
 * $Id: ConfigurationTest.java,v 1.2 2003-03-14 03:37:54 shahid.shah Exp $
 */

package com.netspective.commons.config;

import java.io.StringReader;

import junit.framework.TestCase;

import org.xml.sax.InputSource;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XmlDataModelDtd;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;

public class ConfigurationTest extends TestCase
{
    public static String TEST_CONFIGURATION_XML =
            "<component>\n" +
            "    <configuration>"+
            "        <property name=\"test00\" value=\"test00-value\"/>"+
            "        <property name=\"test01\" value=\"test01-value\"/>"+
            "        <property name=\"test02\" value=\"${test00}.${test01}\"/>"+
            "        <property name=\"test03\" value=\"${test01}.${static:abc}\"/>"+
            "        <property name=\"test04\" value=\"${test03}.more\"/>"+
            "        <property name=\"test05\">"+
            "            <property name=\"test05.00\" value=\"test05.00-value\"/>"+
            "            <property name=\"test05.01\" value=\"${test05.00}\"/>"+
            "            <property name=\"test05.02\" value=\"${test05.01}\"/>"+
            "        </property>"+
            "        <property name=\"test06\">${test04}.even.more</property>"+
            "        <test-07>custom property tag with value in PCDATA: ${test02}</test-07>"+
            "    </configuration>"+

            "    <configuration name=\"not-default\">"+
            "        <property name=\"testND00\" value=\"testND00-value\"/>"+
            "        <property name=\"testND01\" value=\"testND01-value\"/>"+
            "        <property name=\"testND02\" value=\"${testND00}.${testND01}\"/>"+
            "        <property name=\"testND03\" value=\"${testND01}.${static:abc}\"/>"+
            "        <property name=\"testND04\" value=\"${/default/test03}.more\"/>"+
            "        <property name=\"testND05\">"+
            "            <property name=\"testND05.00\" value=\"testND05.00-value\"/>"+
            "            <property name=\"testND05.01\" value=\"${testND05.00}\"/>"+
            "            <property name=\"testND05.02\" value=\"${testND05.01}\"/>"+
            "        </property>"+
            "        <property name=\"testND06\" value=\"${testND04}.even.more\"/>"+
            "    </configuration>"+
            "</component>";

    public InputSource getInputSource(String xmlSource)
    {
        return new InputSource(new StringReader(xmlSource));
    }

    public void testDefaultPropertiesValues() throws DataModelException
    {
        ConfigurationsManagerComponent component = new ConfigurationsManagerComponent();
        assertNotNull(component);

        XmlDataModelSchema.getSchema(ConfigurationsManagerComponent.class);
        XdmParseContext pc = XdmParseContext.parse(component, TEST_CONFIGURATION_XML);

        if(pc.getErrors().size() != 0)
            System.out.println(pc.getErrors());

        assertTrue(pc.getErrors().size() == 0);

        Configuration defaultConfig = component.getDefaultConfiguration();
        assertNotNull(defaultConfig);

        //defaultConfig.dumpProperties();

        assertEquals("test00-value", defaultConfig.getTextValue(null, "test00"));
        assertEquals("test01-value", defaultConfig.getTextValue(null, "test01"));
        assertEquals("test00-value.test01-value", defaultConfig.getTextValue(null, "test02"));
        assertEquals("test01-value.abc", defaultConfig.getTextValue(null, "test03"));
        assertEquals("test01-value.abc.more", defaultConfig.getTextValue(null, "test04"));

        assertEquals(defaultConfig.getProperty("test05").getChildrenList().size(), 3);
        assertEquals("test05.00-value", defaultConfig.getTextValue(null, "test05.00"));
        assertEquals("test05.00-value", defaultConfig.getTextValue(null, "test05.01"));
        assertEquals("test01-value.abc.more.even.more", defaultConfig.getTextValue(null, "test06"));
        assertEquals("custom property tag with value in PCDATA: test00-value.test01-value", defaultConfig.getTextValue(null, "test-07"));
    }

    public void testNonDefaultPropertiesValues() throws DataModelException
    {
        ConfigurationsManagerComponent component = new ConfigurationsManagerComponent();
        assertNotNull(component);

        XmlDataModelSchema.getSchema(ConfigurationsManagerComponent.class);
        XdmParseContext pc = XdmParseContext.parse(component, TEST_CONFIGURATION_XML);

        if(pc.getErrors().size() != 0)
            System.out.println(pc.getErrors());

        assertTrue(pc.getErrors().size() == 0);

        Configuration notDefaultConfig = component.getConfigsManager().getConfiguration("not-default");
        assertNotNull(notDefaultConfig);

        //notDefaultConfig.dumpProperties();

        assertEquals("testND00-value", notDefaultConfig.getTextValue(null, "testND00"));
        assertEquals("testND01-value", notDefaultConfig.getTextValue(null, "testND01"));
        assertEquals("testND00-value.testND01-value", notDefaultConfig.getTextValue(null, "testND02"));
        assertEquals("testND01-value.abc", notDefaultConfig.getTextValue(null, "testND03"));
        assertEquals("test01-value.abc.more", notDefaultConfig.getTextValue(null, "testND04"));

        assertEquals(notDefaultConfig.getProperty("testND05").getChildrenList().size(), 3);
        assertEquals("testND05.00-value", notDefaultConfig.getTextValue(null, "testND05.00"));
        assertEquals("testND05.00-value", notDefaultConfig.getTextValue(null, "testND05.01"));
        assertEquals("test01-value.abc.more.even.more", notDefaultConfig.getTextValue(null, "testND06"));
    }

    public void testConfigurationDtd() throws DataModelException
    {
        ConfigurationsManagerComponent component = new ConfigurationsManagerComponent();
        String dtd = new XmlDataModelDtd().getDtd(component);
        assertTrue(dtd != null);

        //System.out.println(dtd);
    }
}
