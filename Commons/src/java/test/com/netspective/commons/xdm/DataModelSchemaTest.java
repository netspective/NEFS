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
 * $Id: DataModelSchemaTest.java,v 1.5 2003-04-01 13:09:37 shahbaz.javeed Exp $
 */

package com.netspective.commons.xdm;

import java.io.IOException;
import java.io.File;
import java.util.*;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import com.netspective.commons.io.Resource;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.XmlDataModelDtd;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.text.TextUtils;

public class DataModelSchemaTest extends TestCase
{
    public static final String RESOURCE_NAME = "DataModelSchemaTest.xml";
    public static final String TEMPLATENAMESPACEID_NESTED1 = "/nested1";

	protected DataModelTest dmt = null;
	List errors = null;
	String[] rootSchemaPropertyNames = new String[] { "integer", "nested-1", "path-separator-char", "root-attr-1", "test-boolean", "test-byte", "test-class", "test-double", "test-file", "test-float", "test-long", "test-short" };
	String[] schemaModifiedPropertyNames = null;

    static public class DataModelTest extends DefaultXdmComponent
    {
        public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
        private RootTest root;

        public DataModelTest()
        {
        }

        public RootTest createRoot()
        {
            root = new RootTest();
            return root;
        }

        public RootTest getRoot()
        {
            return root;
        }

        public void setRoot(RootTest root)
        {
            this.root = root;
        }
    }

    static public class RootTest implements XmlDataModelSchema.CustomElementCreator, TemplateProducerParent, XmlDataModelSchema.ConstructionFinalizeListener
    {
        public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();
        public static final TemplateProducers TEMPLATE_PRODUCERS = new TemplateProducers();
        static
        {
            XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[] { "ignore-me" });
            TEMPLATE_PRODUCERS.add(new TemplateProducer(TEMPLATENAMESPACEID_NESTED1, "nested-1-custom-template", "name", "extends", true, false));
        }

        private StringBuffer pcData = new StringBuffer();
        private String attr1;
        private int integer;
        private List nested1 = new ArrayList();
        private boolean encounteredCustom1;
        private boolean finalizedConstruction;

		private boolean testBoolean;
	    private char pathSeparatorChar;
	    private byte testByte;
	    private short testShort;
	    private long testLong;
	    private float testFloat;
	    private double testDouble;
	    private Class testClass;
	    private File testFile;

        public RootTest()
        {
        }

        public TemplateProducers getTemplateProducers()
        {
            return TEMPLATE_PRODUCERS;
        }

        public void finalizeConstruction() throws DataModelException
        {
            //System.out.println("called after end of root element.");
            finalizedConstruction = true;
        }

        public void addText(String pcData)
        {
            this.pcData.append(pcData);
        }

        public String getPcData()
        {
            return pcData.toString();
        }

        public String getRootAttr1()
        {
            return attr1;
        }

        public void setRootAttr1(String attr1)
        {
            this.attr1 = attr1;
        }

	    public boolean getTestBoolean () {
		    return testBoolean;
	    }

	    public void setTestBoolean (boolean testBoolean) {
		    this.testBoolean = testBoolean;
	    }

	    public char getPathSeparatorChar () {
		    return pathSeparatorChar;
	    }

	    public void setPathSeparatorChar (char pathSeparatorChar) {
		    this.pathSeparatorChar = pathSeparatorChar;
	    }

	    public byte getTestByte () {
		    return testByte;
	    }

	    public void setTestByte (byte testByte) {
		    this.testByte = testByte;
	    }

	    public short getTestShort () {
		    return testShort;
	    }

	    public void setTestShort (short testShort) {
		    this.testShort = testShort;
	    }

	    public long getTestLong () {
		    return testLong;
	    }

	    public void setTestLong (long testLong) {
		    this.testLong = testLong;
	    }

	    public float getTestFloat () {
		    return testFloat;
	    }

	    public void setTestFloat (float testFloat) {
		    this.testFloat = testFloat;
	    }

	    public double getTestDouble () {
		    return testDouble;
	    }

	    public void setTestDouble (double testDouble) {
		    this.testDouble = testDouble;
	    }

	    public Class getTestClass () {
		    return testClass;
	    }

	    public void setTestClass (Class testClass) {
		    this.testClass = testClass;
	    }

	    public File getTestFile () {
		    return testFile;
	    }

	    public void setTestFile (File testFile) {
		    this.testFile = testFile;
	    }

        public int getInteger()
        {
            return integer;
        }

        public void setInteger(int integer)
        {
            this.integer = integer;
        }

        public Nested1Test createNested1()
        {
            return new Nested1Test();
        }

        public List getNested1()
        {
            return nested1;
        }

        public void addNested1(Nested1Test nested1)
        {
            this.nested1.add(nested1);
        }

        public Object createCustomDataModelElement(XdmParseContext pc, XmlDataModelSchema schema, Object parent, String elementName, String alternateClassName)
                throws DataModelException, InvocationTargetException, IllegalAccessException, InstantiationException
        {
            if(elementName.equals("custom-1"))
                encounteredCustom1 = true;
            else
                return schema.createElement(pc, null, this, elementName, true);
            return null;
        }
    }

    static public class Nested1Test implements TemplateConsumer
    {
        public static final TemplateConsumerDefn TEMPLATE_CONSUMER_DEFN = new TemplateConsumerDefn(TEMPLATENAMESPACEID_NESTED1, "extends", null);

        private StringBuffer pcData = new StringBuffer();
        private String text;
        private int integer;
        private List nested11 = new ArrayList();
        private boolean bool;

        public Nested1Test()
        {
        }

        public TemplateConsumerDefn getTemplateConsumerDefn()
        {
            return TEMPLATE_CONSUMER_DEFN;
        }

        public void addText(String pcData)
        {
            this.pcData.append(pcData);
        }

        public String getPcData()
        {
            return pcData.toString();
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public int getInteger()
        {
            return integer;
        }

        public void setInteger(int integer)
        {
            this.integer = integer;
        }

        public boolean getBoolean()
        {
            return bool;
        }

        public void setBoolean(boolean bool)
        {
            this.bool = bool;
        }

        public Nested11Test createNested11()
        {
            return new Nested11Test();
        }

        public void addNested11(Nested11Test item)
        {
            nested11.add(item);
        }

        public List getNested11List()
        {
            return nested11;
        }
    }

    static public class Nested11TypeEnumeratedAttribute extends XdmEnumeratedAttribute
    {
        private static final String[] VALUES = new String[] { "type-A", "type-B", "type-C" };

        public String[] getValues()
        {
            return VALUES;
        }
    }

    static public class Nested11Test
    {
        public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

        private String text;
        private int integer;
        private int type;

        public Nested11Test()
        {
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public int getType()
        {
            return type;
        }

        public void setType(Nested11TypeEnumeratedAttribute valueEnum)
        {
            type = valueEnum.getValueIndex();
        }

        public int getInteger()
        {
            return integer;
        }

        public void setInteger(int integer)
        {
            this.integer = integer;
        }
    }

    static public class CustomNested11Test extends Nested11Test
    {
    }

	protected void setUp () throws Exception {
		super.setUp();

		dmt = (DataModelTest) XdmComponentFactory.get(DataModelTest.class, new Resource(DataModelSchemaTest.class, RESOURCE_NAME), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		errors = dmt.getErrors();
		if(errors.size() != 0)
		{
		    for(int i = 0; i < errors.size(); i++)
		    {
		        Object error = errors.get(i);
		        System.out.print(error.getClass().getName());
		        if(error instanceof Throwable)
		            ((Throwable) error).printStackTrace();
		        else
		            System.out.println(error.toString());
		    }
		}

		// Massage the Root Schema Property Names...
		Set modifiedPropertyNames = new HashSet();
		for (int i = 0; i < rootSchemaPropertyNames.length; i ++)
		{
			String removeDashes = TextUtils.replaceTextValues(rootSchemaPropertyNames[i], "-", "");

			if (! rootSchemaPropertyNames[i].equals(removeDashes))
				modifiedPropertyNames.add(removeDashes);
		}
		schemaModifiedPropertyNames = (String[]) modifiedPropertyNames.toArray(new String[modifiedPropertyNames.size()]);
	}

    public void testDataModelSchemaImportFromXmlValid() throws DataModelException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
    {
        assertTrue(errors.size() == 0);
        assertNotNull(dmt.getInputSource());

        assertEquals("root-attr-1-text", dmt.getRoot().getRootAttr1());

	    assertTrue(dmt.getRoot().getTestBoolean());
	    assertEquals(':', dmt.getRoot().getPathSeparatorChar());
	    assertEquals(96, dmt.getRoot().getTestByte());
	    assertEquals(128, dmt.getRoot().getTestShort());
	    assertEquals(1234567890, dmt.getRoot().getTestLong());
	    assertTrue(Math.abs(3.1415926535 - dmt.getRoot().getTestFloat()) < 0.000001);
	    assertTrue(Math.abs(3.1415926535897932384626433 - dmt.getRoot().getTestDouble()) < 0.00000000001);
	    assertNotNull(dmt.getRoot().getTestFile());
	    assertTrue(dmt.getRoot().getTestFile().exists());
	    assertEquals("DataModelSchemaTest.xml", dmt.getRoot().getTestFile().getName());

        assertTrue(dmt.getRoot().encounteredCustom1);
        assertTrue(dmt.getRoot().finalizedConstruction);
        assertTrue(dmt.getRoot().getPcData() != null);
        assertTrue(dmt.getRoot().getPcData().indexOf("PCDATA in root") > 0);

        assertEquals(5, dmt.getRoot().getNested1().size());

        Nested1Test nested1FromInclude = (Nested1Test) dmt.getRoot().getNested1().get(0);
        assertTrue(nested1FromInclude != null);
        assertTrue(nested1FromInclude.getPcData().indexOf("PCDATA in included-nested1.") > 0);
        assertEquals(2, nested1FromInclude.getNested11List().size());

        Nested1Test nested1FromMain = (Nested1Test) dmt.getRoot().getNested1().get(1);
        assertTrue(nested1FromMain != null);
        assertTrue(nested1FromMain.getPcData().indexOf("PCDATA in nested1") > 0);
        assertEquals("TestText1", nested1FromMain.getText());
        assertEquals(1, nested1FromMain.getInteger());
        assertEquals(true, nested1FromMain.getBoolean());
        assertTrue(nested1FromMain.getNested11List() != null);
        assertEquals(3, nested1FromMain.getNested11List().size());

        Nested11Test test = (Nested11Test) nested1FromMain.getNested11List().get(1);
        assertEquals("TestText12", test.getText());
        assertEquals(12, test.getInteger());
        assertEquals(2, test.getType());

        assertEquals(CustomNested11Test.class, nested1FromMain.getNested11List().get(2).getClass());
        CustomNested11Test ctest = (CustomNested11Test) nested1FromMain.getNested11List().get(2);
        assertEquals("CustomTestText12", ctest.getText());
        assertEquals(122, ctest.getInteger());

        Nested1Test nested1FromGenericTemplate = (Nested1Test) dmt.getRoot().getNested1().get(2);
        assertTrue(nested1FromGenericTemplate != null);
        assertEquals("TestNested1Template", nested1FromGenericTemplate.getText());
        assertTrue(nested1FromGenericTemplate.getPcData().indexOf("PCDATA in TestNested1Template.") > 0);
        assertEquals(3, nested1FromGenericTemplate.getNested11List().size());
        assertEquals(CustomNested11Test.class, nested1FromGenericTemplate.getNested11List().get(2).getClass());

        // this element contains java expressions
        Nested11Test firstNest11TestFromGenericTemplate = (Nested11Test) nested1FromGenericTemplate.getNested11List().get(0);
        assertEquals("param1-value", firstNest11TestFromGenericTemplate.getText());
        assertEquals(4234, firstNest11TestFromGenericTemplate.getInteger());

        Nested1Test nested1FromCustomTemplate = (Nested1Test) dmt.getRoot().getNested1().get(3);
        assertTrue(nested1FromCustomTemplate != null);
        assertTrue(nested1FromCustomTemplate.getPcData().indexOf("PCDATA in TestNested1CustomTemplate.") > 0);
        assertEquals(4, nested1FromCustomTemplate.getNested11List().size());

        // this element contains java expressions
        Nested11Test lastNest11TestFromCustomTemplate = (Nested11Test) nested1FromCustomTemplate.getNested11List().get(3);
        assertEquals(Nested1Test.class.getName() + " " + Nested11Test.class.getName(), lastNest11TestFromCustomTemplate.getText());
        assertEquals(2058, lastNest11TestFromCustomTemplate.getInteger());

        Nested1Test nested1FromXSLT = (Nested1Test) dmt.getRoot().getNested1().get(4);
        assertTrue(nested1FromXSLT != null);
        assertEquals("TestNested1XSLTNode", nested1FromXSLT.getText());
        assertTrue(nested1FromXSLT.getPcData().indexOf("PCDATA in TestNested1XSLTNode.") > 0);
        assertEquals(2, nested1FromXSLT.getNested11List().size());

        //System.out.println(dmt.getMetrics().toString());

        String dtd = new XmlDataModelDtd().getDtd(dmt);
        assertTrue(dtd != null);
        //System.out.println(dtd);
    }

	public void testXmlDataModelSchemaOptions ()
	{
		// This test exercizes the XmlDataModelSchema.Options class...

        XmlDataModelSchema.Options testOpt = new XmlDataModelSchema.Options(RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS);

		// Verify initial values...
		assertEquals(0, testOpt.getIgnoreAttributes().size());
		assertEquals(1, testOpt.getIgnoreNestedElements().size());
		assertFalse(testOpt.isIgnorePcData());
		assertEquals(XmlDataModelSchema.ADD_TEXT_DEFAULT_METHOD_NAME, testOpt.getPcDataHandlerMethodName());
		assertEquals(0, testOpt.getAliases().size());

		// ... now modify them and re-verify the new values...
		Set ignoredAttributes = testOpt.getIgnoreAttributes();
		ignoredAttributes.add("ignored-attribute");
		testOpt.setIgnoreAttributes(ignoredAttributes);
		assertEquals(1, testOpt.getIgnoreAttributes().size());

		testOpt.addIgnoreAttributes(new String[] { "ignored-attribute-two", "ignored-attribute-three" });
		assertEquals(3, testOpt.getIgnoreAttributes().size());

		Set ignoredNestedElements = testOpt.getIgnoreNestedElements();
		ignoredNestedElements.add("ignored-nested-element");
		testOpt.setIgnoreNestedElements(ignoredNestedElements);
		assertEquals(2, testOpt.getIgnoreNestedElements().size());

		testOpt.addIgnoreNestedElements(new String[] { "ignored-nested-element-two", "ignored-nested-element-three" });
		assertEquals(4, testOpt.getIgnoreNestedElements().size());

		testOpt.addAliases("ignored-attribute-two", new String[] { "ignored-attribute-too", "ignored-attribute-2" });
		assertEquals(1, testOpt.getAliases().size());
		testOpt.addAliases("ignored-attribute-two", new String[] { "ignored-attribute-too", "ignored-attribute-2", "ignored-attribute-to", "ignored-attr-too" });
		assertEquals(1, testOpt.getAliases().size());

		Set nestedElementAliasesOne = new HashSet();
		Set nestedElementAliasesTwo = new HashSet();
		String[] nestedElementAliasList = new String[] { "ignored-nested-element-too", "ignored-nested-element-2", "ignored-nested-element-to", "ignored-nested-elem-too" };

		for (int i = 0; i < nestedElementAliasList.length; i ++)
		{
			if (i < 2)
				nestedElementAliasesOne.add(nestedElementAliasList[i]);

			nestedElementAliasesTwo.add(nestedElementAliasList[i]);
		}

		testOpt.addAliases("ignored-nested-element-two", nestedElementAliasesOne);
		assertEquals(2, testOpt.getAliases().size());
		testOpt.addAliases("ignored-nested-element-two", nestedElementAliasesTwo);
		assertEquals(2, testOpt.getAliases().size());
	}

	public void testXmlDataModelSchemaOptionErrors ()
	{
		// This test exercizes the XmlDataModelSchema.Options class...

        XmlDataModelSchema.Options testOpt = new XmlDataModelSchema.Options(RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS);

		assertEquals(0, testOpt.getAliases().size());
		String[] ignoredAttributeTwoAliases = null;
		testOpt.addAliases("ignored-attribute-two", ignoredAttributeTwoAliases);
		assertEquals(0, testOpt.getAliases().size());

		Set nestedElementAliases = null;
		testOpt.addAliases("ignored-nested-element-two", nestedElementAliases);
		assertEquals(0, testOpt.getAliases().size());
	}

	public void testXmlDataModelSchemaProperties ()
	{
		XmlDataModelSchema schema = XmlDataModelSchema.getSchema(RootTest.class);
		assertNotNull(schema);

		XmlDataModelSchema.Options schemaOpt = schema.getOptions();
		// This should be the same as the RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS
		assertEquals(RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS, schemaOpt);

		Set schemaPropertySet = schema.getPropertyNames().keySet();
		assertEquals(rootSchemaPropertyNames.length + schemaModifiedPropertyNames.length, schemaPropertySet.size());
		for (int i = 0; i < rootSchemaPropertyNames.length; i ++)
			assertTrue(schemaPropertySet.contains(rootSchemaPropertyNames[i]));

		for (int i = 0; i < schemaModifiedPropertyNames.length; i ++)
			assertTrue(schemaPropertySet.contains(schemaModifiedPropertyNames[i]));

		// Removed a println containing a Property object => reduces coverage of Property by ~ 27%
	}

	public void testXmlDataModelSchemaAttributes ()
	{
		System.out.println("\n");
		Map schemas = XmlDataModelSchema.getSchemas();
		Set schemaNames = schemas.keySet();
		Class[] expectedSchemas = new Class[] { DataModelTest.class, RootTest.class, Nested1Test.class, Nested11Test.class, CustomNested11Test.class };

		for (int i = 0; i < expectedSchemas.length; i ++)
			assertTrue(schemaNames.contains(expectedSchemas[i]));

		XmlDataModelSchema rootSchema = XmlDataModelSchema.getSchema(RootTest.class);
		assertNotNull(rootSchema);
		assertEquals(schemas.get(RootTest.class), rootSchema);

		XmlDataModelSchema.Options schemaOpt = rootSchema.getOptions();
		// This should be the same as the RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS
		assertEquals(RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS, schemaOpt);

//		rootSchemaPropertyNames = new String[] { "integer", "nested-1", "path-separator-char", "root-attr-1", "test-boolean", "test-byte", "test-class", "test-double", "test-file", "test-float", "test-long", "test-short" };

		Map rootSchemaAttributeSetters = rootSchema.getAttributeSetters();
		Set rootSchemaAttributeSetterKeys = rootSchemaAttributeSetters.keySet();
		assertEquals(rootSchema.getAttributes(), rootSchemaAttributeSetterKeys);

		for (Iterator iter = rootSchemaAttributeSetterKeys.iterator(); iter.hasNext();)
		{
			Object key = iter.next();
//			System.out.println(key + " => " + rootSchemaAttributeSetters.get(key));
			// Decipher this...
		}

		Map expectedAttributeTypes = new HashMap();
		expectedAttributeTypes.put("integer", int.class);
		expectedAttributeTypes.put("path-separator-char", char.class);
		expectedAttributeTypes.put("root-attr-1", String.class);
		expectedAttributeTypes.put("test-boolean", boolean.class);
		expectedAttributeTypes.put("test-byte", byte.class);
		expectedAttributeTypes.put("test-class", Class.class);
		expectedAttributeTypes.put("test-double", double.class);
		expectedAttributeTypes.put("test-file", File.class);
		expectedAttributeTypes.put("test-float", float.class);
		expectedAttributeTypes.put("test-long", long.class);
		expectedAttributeTypes.put("test-short", short.class);

		Map rootSchemaAttributeTypes = rootSchema.getAttributeTypes();

		for (Iterator iter = expectedAttributeTypes.keySet().iterator(); iter.hasNext();)
		{
			String key = (String) iter.next();
			assertEquals(expectedAttributeTypes.get(key), rootSchemaAttributeTypes.get(key));

			String removeDashes = TextUtils.replaceTextValues(key, "-", "");
			assertEquals(expectedAttributeTypes.get(key), rootSchemaAttributeTypes.get(removeDashes));
		}
	}

	public void testXmlDataModelSchema ()
	{
		System.out.println("\n");
		Map schemas = XmlDataModelSchema.getSchemas();
		Set schemaNames = schemas.keySet();
		Class[] expectedSchemas = new Class[] { DataModelTest.class, RootTest.class, Nested1Test.class, Nested11Test.class, CustomNested11Test.class };

		for (int i = 0; i < expectedSchemas.length; i ++)
			assertTrue(schemaNames.contains(expectedSchemas[i]));

		XmlDataModelSchema rootSchema = XmlDataModelSchema.getSchema(RootTest.class);
		assertNotNull(rootSchema);
		assertEquals(schemas.get(RootTest.class), rootSchema);

		XmlDataModelSchema.Options schemaOpt = rootSchema.getOptions();
		// This should be the same as the RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS
		assertEquals(RootTest.XML_DATA_MODEL_SCHEMA_OPTIONS, schemaOpt);
	}

}
