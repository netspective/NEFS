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
 * $Id: JavaDocs.java,v 1.1 2003-08-13 12:18:31 shahid.shah Exp $
 */

package com.netspective.commons.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.xpath.XPathAPI;

public class JavaDocs
{
    private static final JavaDocs INSTANCE = (JavaDocs) DiscoverSingleton.find(JavaDocs.class, JavaDocs.class.getName());
    private static final Log log = LogFactory.getLog(JavaDocs.class);

    public static JavaDocs getInstance()
    {
        return INSTANCE;
    }

    private Map xmlDocsByClass = new HashMap();
    private Map classJavaDocsByClass = new HashMap();

    /**
     * Return the DOM document that represents the output from JavaDoc for the given class. The XML file is located
     * using Java resource loading mechanism using the following search order:
     * <ol>
     *      <li>Looks for fully qualified name (FQN) of class plus ".xml" (x.y.z.xml)
     *      <li>Looks for java-doc-xml/x.y.z.xml
     *      <li>Looks for resources/java-doc-xml/x.y.z.xml
     * </ol>
     * @param cls
     * @return The DOM document for the given class or null if x.y.z.xml resource could not be located.
     * @throws IOException
     */
    public Document getXmlDocForClass(Class cls) throws IOException
    {
        Document javaDoc = (Document) xmlDocsByClass.get(cls);
        if(javaDoc != null)
            return javaDoc;

        String javaDocXmlFileName = (cls.getName() + ".xml").replace('$', '.');
        URL javaDocUrl = ResourceLoader.getResource(new String[] { javaDocXmlFileName, "java-doc-xml/" + javaDocXmlFileName, "resources/java-doc-xml/" + javaDocXmlFileName });
        if(javaDocUrl != null)
        {
            InputStream is = javaDocUrl.openStream();
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                javaDoc = factory.newDocumentBuilder().parse(is);
                xmlDocsByClass.put(cls, javaDoc);
            }
            catch (SAXException e)
            {
                log.error("A parsing error occurred; the xml input is not valid: " + javaDocUrl.getRef(), e);
            }
            catch (ParserConfigurationException e)
            {
                log.error("A parser configuration error occurred; the xml input is not valid", e);
            }
            finally
            {
                is.close();
            }
        }

        return javaDoc;
    }

    public ClassJavaDoc getClassJavaDoc(Class cls)
    {
        ClassJavaDoc result = (ClassJavaDoc) classJavaDocsByClass.get(cls);
        if(result == null)
        {
            result = new ClassJavaDoc(cls);
            classJavaDocsByClass.put(cls, result);
        }
        return result;
    }

    public MethodDocXmlNodeLocator getMethodDocXmlNodeLocator(Class cls, String methodName, boolean inherit)
    {
        MethodDocXmlNodeLocator locator = new MethodDocXmlNodeLocator(cls, methodName, inherit);
        locator.locate();
        return locator;
    }

    public abstract class JavaDocXmlNodeLocator
    {
        protected Class startingClass;
        protected Class locatedInClass;
        protected Node locatedNode;
        protected int locatedInAncestorIndex;
        protected boolean searchParents;
        protected Exception retrievalError;

        protected JavaDocXmlNodeLocator(Class startingClass, boolean searchParents)
        {
            this.startingClass = startingClass;
            this.searchParents = searchParents;
        }

        public boolean isFound()
        {
            return locatedNode != null;
        }

        public Node getLocatedNode()
        {
            return locatedNode;
        }

        public Class getStartingClass()
        {
            return startingClass;
        }

        public Class getLocatedInClass()
        {
            return locatedInClass;
        }

        public int getLocatedInAncestorIndex()
        {
            return locatedInAncestorIndex;
        }

        public boolean isSearchParents()
        {
            return searchParents;
        }

        public Exception getRetrievalError()
        {
            return retrievalError;
        }

        protected Class getSuperClass(Document xmlDoc) throws TransformerException, ClassNotFoundException
        {
            Element superClassDocElem = (Element) XPathAPI.selectSingleNode(xmlDoc.getDocumentElement(), "//superclass");
            if(superClassDocElem != null)
            {
                String superClassName = superClassDocElem.getAttribute("inPackage") + "." +
                                        superClassDocElem.getAttribute("name");
                return Class.forName(superClassName);
            }
            else
                return null;
        }

        abstract protected void locate(Class activeClass, int inhLevel) throws IOException, ClassNotFoundException, TransformerException;
        abstract protected void locate();
    }

    public class MethodDocXmlNodeLocator extends JavaDocXmlNodeLocator
    {
        private String methodName;

        public MethodDocXmlNodeLocator(Class startingClass, String methodName, boolean searchParents)
        {
            super(startingClass, searchParents);
            this.methodName = methodName;
        }

        public String getMethodName()
        {
            return methodName;
        }

        protected void locate(Class activeClass, int inhLevel) throws IOException, ClassNotFoundException, TransformerException
        {
            Document xmlDoc = getXmlDocForClass(activeClass);
            if(xmlDoc == null)
                return;

            String mutatorDocNodeXPathExpr = "//method[@name = '"+ getMethodName() +"']";
            Node methodDocNode = XPathAPI.selectSingleNode(xmlDoc.getDocumentElement(), mutatorDocNodeXPathExpr);
            if(methodDocNode != null)
            {
                this.locatedInAncestorIndex = inhLevel;
                this.locatedInClass = activeClass;
                this.locatedNode = methodDocNode;
                return;
            }

            if(isSearchParents())
            {
                Class superClass = getSuperClass(xmlDoc);
                if(superClass != null)
                    locate(superClass, inhLevel+1);
            }
        }

        public void locate()
        {
            try
            {
                locate(getStartingClass(), 0);
            }
            catch (Exception e)
            {
                JavaDocs.log.error("Error retrieving description for " + getStartingClass() + " method " + getMethodName(), e);
                retrievalError = e;
            }
        }
    }
}
