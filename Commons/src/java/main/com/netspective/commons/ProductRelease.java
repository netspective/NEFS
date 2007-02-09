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
package com.netspective.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProductRelease implements Product
{
    public static final com.netspective.commons.Product PRODUCT_RELEASE = new ProductRelease();

    public static final String PRODUCT_NAME = "Netspective Commons";
    public static final String PRODUCT_ID = "netspective-commons";

    public static final int PRODUCT_RELEASE_NUMBER = 7;
    public static final int PRODUCT_VERSION_MAJOR = 2;
    public static final int PRODUCT_VERSION_MINOR = 2;

    public ProductRelease()
    {
    }

    public String getProductId()
    {
        return PRODUCT_ID;
    }

    public String getProductName()
    {
        return PRODUCT_NAME;
    }

    public final int getReleaseNumber()
    {
        return PRODUCT_RELEASE_NUMBER;
    }

    public final int getVersionMajor()
    {
        return PRODUCT_VERSION_MAJOR;
    }

    public final int getVersionMinor()
    {
        return PRODUCT_VERSION_MINOR;
    }

    public final int getBuildNumber()
    {
        return BuildLog.BUILD_NUMBER;
    }

    public final String getBuildFilePrefix(boolean includeBuildNumber)
    {
        String filePrefix = PRODUCT_ID + "-" + PRODUCT_RELEASE_NUMBER + "." + PRODUCT_VERSION_MAJOR + "." + PRODUCT_VERSION_MINOR;
        if(includeBuildNumber)
            filePrefix = filePrefix + "_" + BuildLog.BUILD_NUMBER;
        return filePrefix;
    }

    public final String getVersion()
    {
        return PRODUCT_RELEASE_NUMBER + "." + PRODUCT_VERSION_MAJOR + "." + PRODUCT_VERSION_MINOR;
    }

    public final String getVersionAndBuild()
    {
        return "Version " + getVersion() + " Build " + BuildLog.BUILD_NUMBER;
    }

    public final String getProductBuild()
    {
        return PRODUCT_NAME + " Version " + getVersion() + " Build " + BuildLog.BUILD_NUMBER;
    }

    public final String getVersionAndBuildShort()
    {
        return "v" + getVersion() + " b" + BuildLog.BUILD_NUMBER;
    }

    public Date getBuildDate()
    {
        DateFormat format = DateFormat.getDateTimeInstance();
        try
        {
            return format.parse(BuildLog.BUILD_DATE);
        }
        catch(ParseException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return new Date();
        }
    }

    public String getBuildDateText()
    {
        return BuildLog.BUILD_DATE;
    }

    public static void fillLibraryVersions(Product product, Properties props, String dirName, String productInfoResourceName) throws ParserConfigurationException, IOException, SAXException
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder parser = factory.newDocumentBuilder();
        final InputStream inStream = product.getClass().getResource(productInfoResourceName).openStream();
        Document doc = doc = parser.parse(inStream);
        inStream.close();
        Element rootElem = doc.getDocumentElement();
        NodeList products = rootElem.getElementsByTagName("product");
        if(products == null || products.getLength() == 0)
            return;

        File dir = new File(dirName);
        File[] files = dir.listFiles();
        for(int i = 0; i < files.length; i++)
        {
            File file = files[i];
            String libFileName = file.getName();

            for(int n = 0; n < products.getLength(); n++)
            {
                Element productElement = (Element) products.item(n);
                NodeList dependencies = productElement.getElementsByTagName("dependencies");
                if(dependencies == null || dependencies.getLength() != 1)
                    continue;

                Element dependElem = (Element) dependencies.item(0);
                NodeList libraries = dependElem.getElementsByTagName("library");
                if(libraries == null || libraries.getLength() == 0)
                    continue;

                for(int l = 0; l < libraries.getLength(); l++)
                {
                    Element libraryElem = (Element) libraries.item(l);
                    NodeList children = libraryElem.getChildNodes();

                    String libName = null;
                    String libVersion = null;
                    String libClassPath = null;

                    for(int c = 0; c < children.getLength(); c++)
                    {
                        Node node = children.item(c);
                        if(node.getNodeType() != Node.ELEMENT_NODE)
                            continue;

                        if(node.getNodeName().equals("name"))
                            libName = node.getFirstChild().getNodeValue();
                        else if(node.getNodeName().equals("version"))
                            libVersion = node.getFirstChild().getNodeValue();
                        else if(node.getNodeName().equals("class-path"))
                            libClassPath = node.getFirstChild().getNodeValue();
                    }

                    if(libClassPath != null)
                    {
                        StringTokenizer st = new StringTokenizer(libClassPath, ":");
                        while(st.hasMoreTokens())
                        {
                            final String checkLibName = st.nextToken();
                            if(checkLibName.equals(libFileName))
                                props.setProperty(libFileName, libName + " Version " + libVersion);
                        }
                    }
                }
            }
        }

        return;
    }

    public static void handleCommandLineOutput(Product product, String[] args) throws IOException, ParserConfigurationException, SAXException
    {
        if(args.length > 0)
        {
            final String firstArg = args[0];
            if(firstArg.equals("-v"))
                System.out.print(product.getVersionAndBuildShort());
            else if(firstArg.equals("-V"))
                System.out.print(product.getProductBuild());
            else if(firstArg.equals("-n"))
                System.out.print(product.getProductName());
            else if(firstArg.equals("-r"))
                System.out.print(product.getReleaseNumber());
            else if(firstArg.equals("-m"))
                System.out.print(product.getVersionMajor());
            else if(firstArg.equals("-i"))
                System.out.print(product.getVersionMinor());
            else if(firstArg.equals("-d"))
                System.out.print(product.getBuildDateText());
            else if(firstArg.equals("-l"))
            {
                final String dirName = args[1];
                final String propFileName = args[2];
                final String thisJarName = args[3];

                final Properties props = new Properties();
                if(new File(propFileName).exists())
                {
                    final FileInputStream inStream = new FileInputStream(propFileName);
                    props.load(inStream);
                    inStream.close();
                }

                fillLibraryVersions(product, props, dirName, "conf/product-info.xml");
                props.setProperty(thisJarName, product.getProductBuild() + " (built on " + product.getBuildDateText() + ")");

                final FileOutputStream out = new FileOutputStream(propFileName);
                props.store(out, null);
                out.flush();
                out.close();

                System.out.print(product.getProductBuild() + " (built on " + product.getBuildDateText() + ")");
            }
            else if(firstArg.equals("-?"))
            {
                System.out.print(product.getClass().getName() + " <options>");
                System.out.print(" -v returns getVersionAndBuildShort()");
                System.out.print(" -V returns getProductBuild()");
                System.out.print(" -n returns getProductName()");
                System.out.print(" -r returns getReleaseNumber()");
                System.out.print(" -m returns getVersionMajor()");
                System.out.print(" -i returns getVersionMinor()");
                System.out.print(" -d returns getBuildDateText()");
                System.out.print(" -l <dir> <propfile> will output version of this library plus versions of required libraries matched in <dir> to <propfile>");
            }
        }
        else
            System.out.print(product.getProductBuild() + " (built on " + product.getBuildDateText() + ")");
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
    {
        handleCommandLineOutput(PRODUCT_RELEASE, args);
    }
}
