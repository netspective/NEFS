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
 * $Id: SystemProperty.java,v 1.3 2003-08-17 00:04:03 shahid.shah Exp $
 */

package com.netspective.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.io.Resource;

public class SystemProperty implements XmlDataModelSchema.ConstructionFinalizeListener
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setPcDataHandlerMethodName("appendPropertyValueText");
    private Property property;
    private File file;
    private String resource;
    private Class resourceRelativeToClass = SystemProperty.class;

    public SystemProperty()
    {
        property = new Property();
    }

    public SystemProperty(Configuration owner)
    {
        property = new Property(owner, null);
    }

    public Property getProperty()
    {
        return property;
    }

    public void setName(String name)
    {
        property.setName(name);
    }

    public void setValue(String expr)
    {
        property.setValue(expr);
    }

    public void appendPropertyValueText(String text)
    {
        property.appendPropertyValueText(text);
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public Class getResourceRelativeToClass()
    {
        return resourceRelativeToClass;
    }

    public void setResourceRelativeToClass(Class resourceRelativeToClass)
    {
        this.resourceRelativeToClass = resourceRelativeToClass;
    }

    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        register();
    }

    public void register()
    {
        if(file != null)
        {
            Properties properties = new Properties();
            try
            {
                InputStream is = new FileInputStream(file);
                properties.load(is);
                is.close();
            }
            catch (IOException e)
            {
                throw new NestableRuntimeException(e);
            }
            System.setProperties(properties);
        }
        else if(resource != null)
        {
            Resource res = new Resource(resourceRelativeToClass, resource);
            Properties properties = new Properties();
            try
            {
                InputStream is = res.getResourceAsStream();
                properties.load(is);
                is.close();
            }
            catch (IOException e)
            {
                throw new NestableRuntimeException(e);
            }
            System.setProperties(properties);
        }
        else
            System.setProperty(property.getName(), property.getValue(null));
    }
}