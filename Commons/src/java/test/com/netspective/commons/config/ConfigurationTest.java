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
package com.netspective.commons.config;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;

import com.netspective.commons.io.Resource;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.XmlDataModelDtd;
import com.netspective.commons.xdm.exception.DataModelException;

import junit.framework.TestCase;

public class ConfigurationTest extends TestCase
{
    public static String RESOURCE_NAME_ONE = "ConfigurationTest-One.xml";

    public InputSource getInputSource(String xmlSource)
    {
        return new InputSource(new StringReader(xmlSource));
    }

    public void testDefaultPropertiesValues() throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException
    {
        ConfigurationsComponent component =
                (ConfigurationsComponent) XdmComponentFactory.get(ConfigurationsComponent.class,
                        new Resource(ConfigurationTest.class, RESOURCE_NAME_ONE),
                        XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

        ConfigurationsManager configManager = component.getItems();
        assertNotNull("Configuration Manager is null", configManager);

        assertEquals("just a value no variables", System.getProperty("test.system-property.01"));

        Configurations configs = configManager.getConfigurations();
        int expectedNumConfigs = 2;
        int numConfigs = configs.size();
        assertEquals("Expected # Configurations: 1, Found: " + numConfigs, expectedNumConfigs, numConfigs);

        Configuration defaultConfig = component.getItems().getDefaultConfiguration();
        assertNotNull(defaultConfig);

        Set availableConfigs = configs.getConfigurationNames();
        Set expectedConfigs = new HashSet();
        expectedConfigs.add("DEFAULT");
        expectedConfigs.add("NOT-DEFAULT");
        assertEquals("Expected Configs: " + expectedConfigs + ", Found: " + availableConfigs, expectedConfigs, availableConfigs);

        Map defaultProperties = defaultConfig.getAllProperties();

        Property test00Property = (Property) defaultProperties.get("test00");
        assertEquals("test00-value", defaultConfig.getTextValue(null, "test00"));
        assertEquals("test00-value", defaultConfig.getTextValue(null, "test00", null));
        assertEquals("test00-value", defaultConfig.getExpression("test00"));
        assertNotNull(defaultConfig.getTextValue(null, "test00", null));
        assertEquals(test00Property, defaultConfig.getProperty("test00"));

        Property test01Property = (Property) defaultProperties.get("test01");
        assertEquals("test01-value", defaultConfig.getTextValue(null, "test01"));
        assertEquals("test01-value", defaultConfig.getTextValue(null, "test01", null));
        assertEquals("test01-value", defaultConfig.getExpression("test01"));
        assertNotNull(defaultConfig.getTextValue(null, "test01", null));
        assertEquals(test01Property, defaultConfig.getProperty("test01"));

        Property test02Property = (Property) defaultProperties.get("test02");
        assertTrue(defaultConfig.getProperty("test02").isDynamic());
        assertEquals("${test00}.${test01}", test02Property.getValue());
        assertEquals("${test00}.${test01}", defaultConfig.getExpression("test02"));
        assertEquals("test00-value.test01-value", defaultConfig.getTextValue(null, "test02"));
        assertEquals("test00-value.test01-value", defaultConfig.getTextValue(null, "test02", null));
        assertNotNull(defaultConfig.getTextValue(null, "test02", null));
        assertEquals(test02Property, defaultConfig.getProperty("test02"));

        Property test03Property = (Property) defaultProperties.get("test03");
        assertTrue(defaultConfig.getProperty("test03").isDynamic());
        assertEquals("${test01}.${static:abc}", test03Property.getValue());
        assertEquals("${test01}.${static:abc}", defaultConfig.getExpression("test03"));
        assertEquals("test01-value.abc", defaultConfig.getTextValue(null, "test03"));
        assertEquals(test03Property, defaultConfig.getProperty("test03"));

        Property test04Property = (Property) defaultProperties.get("test04");
        assertTrue(defaultConfig.getProperty("test04").isDynamic());
        assertEquals("${test03}.more", test04Property.getValue());
        assertEquals("${test03}.more", defaultConfig.getExpression("test04"));
        assertEquals("test01-value.abc.more", defaultConfig.getTextValue(null, "test04"));
        assertEquals(test04Property, defaultConfig.getProperty("test04"));

        Property test05Property = (Property) defaultProperties.get("test05");
        Map test05Children = test05Property.getChildrenMap();
        assertEquals(3, test05Property.getChildrenList().size());

        Property test0500Property = (Property) test05Children.get("test05.00");
        Property expected0500Property = test05Property.getProperty("test05.00");
        assertEquals("test05.00-value", test0500Property.getValue(null));
        assertEquals(expected0500Property, test0500Property);

        assertEquals("test05.00-value", defaultConfig.getTextValue(null, "test05.01"));
        assertEquals("test01-value.abc.more.even.more", defaultConfig.getTextValue(null, "test06"));
        assertEquals("custom property tag with value in PCDATA: test00-value.test01-value", defaultConfig.getTextValue(null, "test-07"));

        assertEquals("value with variables test01-value.abc", System.getProperty("test.system-property.02"));
    }

    public void testDefaultPropertiesErrorValues() throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException
    {
        ConfigurationsComponent component =
                (ConfigurationsComponent) XdmComponentFactory.get(ConfigurationsComponent.class,
                        new Resource(ConfigurationTest.class, RESOURCE_NAME_ONE),
                        XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

        ConfigurationsManager configManager = component.getItems();
        assertNotNull("Configuration Manager is null", configManager);

        Configurations configs = configManager.getConfigurations();
        int expectedNumConfigs = 2;
        int numConfigs = configs.size();
        assertEquals("Expected # Configurations: 1, Found: " + numConfigs, expectedNumConfigs, numConfigs);

        Configuration defaultConfig = component.getItems().getDefaultConfiguration();
        assertNotNull(defaultConfig);

        Configuration nonExistantConfigOne = configManager.getConfiguration("blah-blah");
        Configuration nonExistantConfigTwo = configs.getConfiguration("blah-blah");
        assertNull("Non Existant Configuration returns non-null value", nonExistantConfigOne);
        assertNull("Non Existant Configuration returns non-null value", nonExistantConfigTwo);

        Set availableConfigs = configs.getConfigurationNames();
        Set expectedConfigs = new HashSet();
        expectedConfigs.add("DEFAULT");
        expectedConfigs.add("NOT-DEFAULT");
        assertEquals("Expected Configs: " + expectedConfigs + ", Found: " + availableConfigs, expectedConfigs, availableConfigs);

        final String test00DefaultValue = "test00-here-be-dragons";
        final String test01DefaultValue = "test01-pinky-and-the-brain";
        final String test02DefaultValue = "test02-tom-and-jerry-were-here";
        assertEquals(test00DefaultValue, defaultConfig.getTextValue(null, "test00-non-existant-property", test00DefaultValue));
        assertEquals(test01DefaultValue, defaultConfig.getTextValue(null, "test01-non-existant-property", test01DefaultValue));
        assertEquals(test02DefaultValue, defaultConfig.getTextValue(null, "test02-non-existant-property", test02DefaultValue));

        boolean exceptionThrown = true;
        String propertyValue = null;
        final String errorTestDefaultValue = "play-more-neverwinter-nights";

        try
        {
            propertyValue = defaultConfig.getTextValue(null, "test00-non-existant-property");
            exceptionThrown = false;
        }
        catch (PropertyNotFoundException e)
        {
            assertTrue(exceptionThrown);
            assertNull(propertyValue);
            propertyValue = null;
        }

        assertTrue(exceptionThrown);
        propertyValue = null;

        try
        {
            propertyValue = defaultConfig.getTextValue(null, "test00-non-existant-property", errorTestDefaultValue);
            exceptionThrown = false;
        }
        catch (PropertyNotFoundException e)
        {
            assertTrue(exceptionThrown);
            assertEquals(errorTestDefaultValue, propertyValue);
            propertyValue = null;
        }

        assertFalse(exceptionThrown);
        exceptionThrown = true;
        propertyValue = null;

        try
        {
            propertyValue = defaultConfig.getExpression("test00-non-existant-property");
            exceptionThrown = false;
        }
        catch (PropertyNotFoundException e)
        {
            assertTrue(exceptionThrown);
            assertNull(propertyValue);
            propertyValue = null;
        }

        assertTrue(exceptionThrown);
    }

    public void testNonDefaultPropertiesValues() throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException
    {
        ConfigurationsComponent component =
                (ConfigurationsComponent) XdmComponentFactory.get(ConfigurationsComponent.class,
                        new Resource(ConfigurationTest.class, RESOURCE_NAME_ONE),
                        XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

        Configuration notDefaultConfig = component.getItems().getConfigurations().getConfiguration("not-default");
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
        ConfigurationsComponent component = new ConfigurationsComponent();
        String dtd = new XmlDataModelDtd().getDtd(component);
        assertTrue(dtd != null);

        //System.out.println(dtd);
    }
}
