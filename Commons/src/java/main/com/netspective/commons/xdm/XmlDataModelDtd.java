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
 * $Id: XmlDataModelDtd.java,v 1.3 2003-12-05 04:49:04 roque.hernandez Exp $
 */

package com.netspective.commons.xdm;

import java.util.*;
import java.io.*;

import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xdm.exception.DataModelException;

public class XmlDataModelDtd
{
    private final String lSep = System.getProperty("line.separator");

    private final String BOOLEAN = "%boolean;";
    private final String ELEMENTS = "%elements;";
    private final String ELEMNAME_INCLUDE = "xdm:include";

    private Map visitedElements = new HashMap();
    private Set visitedProducers = new HashSet();

    public void generate(XmlDataModel model, PrintWriter out) throws DataModelException
    {
        XmlDataModelSchema schema = XmlDataModelSchema.getSchema(model.getClass());
        Map nestedElements = schema.getNestedElements();

        try
        {
            printHead(out, nestedElements.keySet().iterator());

            Iterator elements = nestedElements.keySet().iterator();
            while (elements.hasNext())
            {
                String elementName = (String) elements.next();
                printElementDecl(out, model, schema, elementName, (Class) nestedElements.get(elementName));
            }

            printTail(out);
        }
        finally
        {
            if (out != null) out.close();
            visitedElements.clear();
        }
    }

    public void generate(XmlDataModel model, File output) throws IOException, DataModelException
    {
        PrintWriter out = null;
        try
        {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF8"));
        }
        catch (UnsupportedEncodingException ue)
        {
            /*
             * Plain impossible with UTF8, see
             * http://java.sun.com/products/jdk/1.2/docs/guide/internat/encoding.doc.html
             *
             * fallback to platform specific anyway.
             */
            out = new PrintWriter(new FileWriter(output));
        }
        generate(model, out);
    }

    public String getDtd(XmlDataModel model) throws DataModelException
    {
        StringWriter sw = new StringWriter();
        generate(model, new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Prints the header of the generated output.
     *
     * <p>Basically this prints the XML declaration, defines some
     * entities and the project element.</p>
     */
    private void printHead(PrintWriter out, Iterator elements)
    {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        out.println("<!ENTITY % boolean \"(true | false | on | off | yes | no)\">");
        out.print("<!ENTITY % elements \"");
        boolean first = true;
        while (elements.hasNext())
        {
            String elementName = (String) elements.next();
            if (!first)
            {
                out.print(" | ");
            }
            else
            {
                first = false;
            }
            out.print(elementName);
        }
        out.println("\">");
        out.println("");
    }

    private void printIncludeDecl(PrintWriter out)
    {
        out.print("<!ELEMENT ");
        out.print(ELEMNAME_INCLUDE);
        out.println(" EMPTY>");
        out.print("<!ATTLIST ");
        out.println(ELEMNAME_INCLUDE);
        out.println("          file CDATA #IMPLIED");
        out.println("          template CDATA #IMPLIED");
        out.println("          resource CDATA #IMPLIED>");
        out.println("          relative-to CDATA #IMPLIED>");
    }

    /**
     * Print the definition for a given element.
     */
    private void printElementDecl(PrintWriter out, XmlDataModel model, XmlDataModelSchema parentSchema, String name, Class element) throws DataModelException
    {
        TemplateProducers templateProducers = null;
        if(parentSchema instanceof TemplateProducerParent)
            templateProducers = ((TemplateProducerParent) parentSchema).getTemplateProducers();

        if (visitedElements.containsKey(name))
        {
            return;
        }
        visitedElements.put(name, "");

        XmlDataModelSchema schema = XmlDataModelSchema.getSchema(element);
        Map parentPropertyNames = parentSchema.getPropertyNames();
        Map childPropertyNames = schema.getPropertyNames();

        StringBuffer sb = new StringBuffer("<!ELEMENT ");
        sb.append(name).append(" ");

        List list = new ArrayList();
        if (schema.supportsCharacters())
        {
            list.add("#PCDATA");
        }

        Iterator iterator = schema.getNestedElements().keySet().iterator();
        while (iterator.hasNext())
        {
            String nestedName = (String) iterator.next();
            if(schema.getOptions().ignoreNestedElement(nestedName))
                continue;
            list.add(nestedName);
        }

        list.add(ELEMNAME_INCLUDE);

        if (list.isEmpty())
        {
            sb.append("EMPTY");
        }
        else
        {
            sb.append("(");
            final int count = list.size();
            for (int i = 0; i < count; i++)
            {
                if (i != 0)
                    sb.append(" | ");
                sb.append(list.get(i));
            }

            boolean first = count > 0 ? false : true;

            if(templateProducers != null)
            {
                iterator = templateProducers.getElementNames().iterator();
                while (iterator.hasNext())
                {
                    if (! first)
                        sb.append(" | ");
                    sb.append(iterator.next());
                }
            }

            sb.append(")");
            if (count > 1 || !list.get(0).equals("#PCDATA"))
            {
                sb.append("*");
            }
        }
        sb.append(">");

        XmlDataModelSchema.PropertyNames elemNames = (XmlDataModelSchema.PropertyNames) parentPropertyNames.get(name);
        if(! elemNames.isPrimaryName(name))
            sb.append(" <!-- alias for '"+ elemNames.getPrimaryName() +"' element -->");

        out.println(sb);

        sb.setLength(0);
        sb.append("<!ATTLIST ").append(name);
        sb.append(lSep).append("          class CDATA \""+ element.getName() + "\"");

        if(model instanceof TemplateConsumer)
        {
            TemplateConsumerDefn tcd = ((TemplateConsumer) model).getTemplateConsumerDefn();
            sb.append(lSep).append("          "+ tcd.getTemplateRefAttrName() +" CDATA #IMPLIED <!-- template consumer namespace: "+ tcd.getNameSpaceId() +" -->");
        }

        iterator = schema.getAttributes().iterator();
        while (iterator.hasNext())
        {
            String attrName = (String) iterator.next();
            if(schema.getOptions().ignoreAttribute(attrName))
                continue;

            sb.append(lSep).append("          ").append(attrName).append(" ");
            Class type = schema.getAttributeType(attrName);
            if (type.equals(Boolean.class) || type.equals(Boolean.TYPE))
            {
                sb.append(BOOLEAN).append(" ");
            }
            else if (XdmEnumeratedAttribute.class.isAssignableFrom(type))
            {
                try
                {
                    XdmEnumeratedAttribute ea = (XdmEnumeratedAttribute) type.newInstance();
                    String[] values = ea.getValues();
                    if (values == null || values.length == 0)
                    {
                        sb.append("CDATA ");
                    }
                    else if(!areNmtokens(values))
                    {
                        sb.append("CDATA (NOT VALID XML-NMTOKENS) ");
                    }
                    else
                    {
                        sb.append("(");
                        for (int i = 0; i < values.length; i++)
                        {
                            if (i != 0)
                            {
                                sb.append(" | ");
                            }
                            sb.append(values[i]);
                        }
                        sb.append(") ");
                    }
                }
                catch (InstantiationException ie)
                {
                    sb.append("CDATA ("+ ie.getMessage() +") ");
                }
                catch (IllegalAccessException iae)
                {
                    sb.append("CDATA ("+ iae.getMessage() +") ");
                }
            }
            else
            {
                sb.append("CDATA ");
            }
            sb.append("#IMPLIED <!-- ");
            XmlDataModelSchema.PropertyNames attrNames = (XmlDataModelSchema.PropertyNames) childPropertyNames.get(attrName);
            if(attrNames != null && !attrNames.isPrimaryName(attrName))
                sb.append("alias for '"+ attrNames.getPrimaryName() +"' attribute, ");
            sb.append(type.getName());
            sb.append(" -->");
        }

        sb.append(">").append(lSep);
        out.println(sb);

        final int count = list.size();
        for (int i = 0; i < count; i++)
        {
            String nestedName = (String) list.get(i);
            if(schema.getOptions().ignoreNestedElement(nestedName))
                continue;

            if (!"#PCDATA".equals(nestedName)
                    && !ELEMENTS.equals(nestedName)
                    && !ELEMNAME_INCLUDE.equals(nestedName))
            {
                printElementDecl(out, model, schema, nestedName, schema.getElementType(nestedName));
            }
        }

        if(templateProducers != null)
        {
            iterator = templateProducers.getElementNames().iterator();
            while (iterator.hasNext())
            {
                String producerName = (String) iterator.next();
                TemplateProducer tp = templateProducers.get(producerName);
                if(visitedProducers.contains(tp))
                    continue;

                visitedProducers.add(tp);

                sb = new StringBuffer("<!ELEMENT ");
                sb.append(tp.getElementName()).append(" (#PCDATA)*>").append("\n");
                sb.append("<!ATTLIST ").append(tp.getElementName()).append("\n");
                sb.append("          ").append(tp.getTemplateNameAttrName()).append(" CDATA #REQUIRED");
                sb.append(" <!-- template definition namespace: "+ tp.getNameSpaceId() +" -->").append("\n");
                sb.append("          ").append(tp.getTemplateInhAttrName()).append(" CDATA #IMPLIED>\n");

                out.println(sb);
            }
        }
    }

    private void printTail(PrintWriter out)
    {
        printIncludeDecl(out);
    }

    /**
     * Does this String match the XML-NMTOKEN production?
     */
    protected boolean isNmtoken(String s)
    {
        final int length = s.length();
        for (int i = 0; i < length; i++)
        {
            char c = s.charAt(i);
            // XXX - we are ommitting CombiningChar and Extender here
            if (!Character.isLetterOrDigit(c) &&
                    c != '.' && c != '-' &&
                    c != '_' && c != ':')
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Do the Strings all match the XML-NMTOKEN production?
     *
     * <p>Otherwise they are not suitable as an enumerated attribute,
     * for example.</p>
     */
    protected boolean areNmtokens(String[] s)
    {
        for (int i = 0; i < s.length; i++)
        {
            if (!isNmtoken(s[i]))
            {
                return false;
            }
        }
        return true;
    }

}
