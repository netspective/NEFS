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
 * $Id: XdmComponentFactory.java,v 1.7 2003-08-24 18:37:15 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.discovery.tools.DiscoverClass;

import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.XdmComponent;
import com.netspective.commons.io.Resource;
import com.netspective.commons.io.FileFind;
import com.netspective.commons.text.TextUtils;

public class XdmComponentFactory
{
    public static final int XDMCOMPFLAG_ALLOWRELOAD = 1;
    public static final int XDMCOMPFLAG_ALLOWRELOAD_IF_FILE = XDMCOMPFLAG_ALLOWRELOAD;  // synonym, value should be same
    public static final int XDMCOMPFLAG_CACHE_WHEN_NO_ERRORS = XDMCOMPFLAG_ALLOWRELOAD * 2;
    public static final int XDMCOMPFLAG_CACHE_ALWAYS = XDMCOMPFLAG_CACHE_WHEN_NO_ERRORS * 2;
    public static final int XDMCOMPFLAG_INSIDE_ANT = XDMCOMPFLAG_CACHE_ALWAYS * 2;

    public static final int XDMCOMPFLAGS_DEFAULT = XDMCOMPFLAG_ALLOWRELOAD | XDMCOMPFLAG_CACHE_WHEN_NO_ERRORS;

    private static DiscoverClass discoverClass = new DiscoverClass();
    private static Map componentsBySystemId = new HashMap();

    public static Map getComponentsBySystemId()
    {
        return componentsBySystemId;
    }

    public static XdmComponent getCachedComponent(String systemId, int flags)
    {
        XdmComponent component = (XdmComponent) componentsBySystemId.get(systemId);
        if(component != null)
        {
            // If we have a component and we don't want to allow re-loads then we use what we have
            if((flags & XDMCOMPFLAG_ALLOWRELOAD) == 0)
                return component;

            // If we have a component and we do allow reloads but the source has not changed, then use what we have
            if(!component.getInputSource().sourceChanged())
                return component;

            // If we get to this point, we have an existing component and we are allowing reloads but the source seems
            // to have changed; we need to read the entire component again so remove the instance from the map and set
            // it to null to give the GC a hint to get rid of the instance as soon as possible.
            component.removedFromCache(componentsBySystemId, systemId, flags);
            componentsBySystemId.remove(systemId);
            component = null;
        }
        return component;
    }

    public static void cacheComponent(String key, XdmComponent component, int flags)
    {
        componentsBySystemId.put(key, component);
        component.addedToCache(componentsBySystemId, key, flags);
    }

    /**
     * Factory method for obtaining a particular component from a file. This method will load the appropriate
     * component file and cache it for future use. If, after caching, the file's input source has changed the file
     * will automatically be reloaded assuming reload is set to true.
     * @param file The file to obtain the content from
     * @param flags Whether or not to allow reloading if the input source has changed and other flags
     * @throws java.io.FileNotFoundException
     */
    public static XdmComponent get(Class componentClass, File file, int flags) throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, FileNotFoundException, NoSuchMethodException
    {
        XdmComponent component = getCachedComponent(file.getAbsolutePath(), flags);
        if(component != null)
            return component;

        // if we we get to this point, we're parsing an XML file into a given component class
        XdmParseContext pc = null;
        List errors = null;
        List warnings = null;

        // create a new class instance and hang onto the error list for use later
        component = (XdmComponent) discoverClass.newInstance(componentClass, componentClass.getName());
        errors = component.getErrors();
        warnings = component.getWarnings();

        // if the class's attributes and model is not known, get it now
        XmlDataModelSchema.getSchema(componentClass);

        // parse the XML file
        long startTime = System.currentTimeMillis();
        pc = XdmParseContext.parse(component, file);
        component.setLoadDuration(startTime, System.currentTimeMillis());

        // if we had some syntax errors, make sure the component records them for later use
        if(pc != null && pc.getErrors().size() != 0)
            errors.addAll(pc.getErrors());

        if(pc != null && pc.getWarnings().size() != 0)
            warnings.addAll(pc.getWarnings());

        component.loadedFromXml(flags);

        // if there are no errors, cache this component so if the file is needed again, it's available immediately
        if((flags & XDMCOMPFLAG_CACHE_ALWAYS) != 0 || (((flags & XDMCOMPFLAG_CACHE_WHEN_NO_ERRORS) != 0) && errors.size() == 0))
            cacheComponent(file.getAbsolutePath(), component, flags);

        return component;
    }

    /**
     * Factory method for obtaining a particular (already-instantiated) component from a file. This method will load
     * the appropriate component file but not cache it for future use.
     * @param file The file to obtain the content from
     * @throws java.io.FileNotFoundException
     */
    public static void load(XdmComponent component, File file) throws DataModelException, FileNotFoundException
    {
        // if the class's attributes and model is not known, get it now
        XmlDataModelSchema.getSchema(component.getClass());

        // parse the XML file
        long startTime = System.currentTimeMillis();
        XdmParseContext pc = XdmParseContext.parse(component, file);
        component.setLoadDuration(startTime, System.currentTimeMillis());

        // if we had some syntax errors, make sure the component records them for later use
        if(pc != null && pc.getErrors().size() != 0)
            component.getErrors().addAll(pc.getErrors());

        if(pc != null && pc.getWarnings().size() != 0)
            component.getWarnings().addAll(pc.getWarnings());
    }

    /**
     * Factory method for obtaining a particular component from a resource. The ClassLoader of the given componentClass
     * is used to locate the resource. If the resource is actually a file, then this method locates the resource, creates
     * a File object and calls get(componentClass, File, true).
     */
    public static XdmComponent get(Class componentClass, Resource resource, int flags) throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, NoSuchMethodException
    {
        // if the resource resolves to an actual file, just treat it as a normal file
        File resourceFile = resource.getFile();
        if(resourceFile != null)
            return get(componentClass, resourceFile, flags);

        // if we get to here, the resource is being loaded remotely or through a JAR or other source so it's not a physical file
        XdmParseContext pc = null;
        XdmComponent component = null;
        List errors = null;
        List warnings = null;

        // create a new class instance and hang onto the error list for use later
        component = (XdmComponent) discoverClass.newInstance(componentClass, componentClass.getName());
        errors = component.getErrors();
        warnings = component.getWarnings();

        // if the class's attributes and model is not known, get it now
        XmlDataModelSchema.getSchema(componentClass);

        // parse the XML file
        long startTime = System.currentTimeMillis();
        pc = XdmParseContext.parse(component, resource);
        component.setLoadDuration(startTime, System.currentTimeMillis());

        // if we had some syntax errors, make sure the component records them for later use
        if(pc != null && pc.getErrors().size() != 0)
            errors.addAll(pc.getErrors());

        if(pc != null && pc.getWarnings().size() != 0)
            warnings.addAll(pc.getWarnings());

        component.loadedFromXml(flags);

        return component;
    }

    /**
     * Factory method for obtaining a particular (pre-instantiated) component from a resource. This method will load
     * the appropriate component file but not cache it for future use.
     */
    public static void load(XdmComponent component, Resource resource) throws IOException, DataModelException, FileNotFoundException
    {
        // if the class's attributes and model is not known, get it now
        XmlDataModelSchema.getSchema(component.getClass());

        // parse the XML file
        long startTime = System.currentTimeMillis();
        XdmParseContext pc = XdmParseContext.parse(component, resource);
        component.setLoadDuration(startTime, System.currentTimeMillis());

        // if we had some syntax errors, make sure the component records them for later use
        if(pc != null && pc.getErrors().size() != 0)
            component.getErrors().addAll(pc.getErrors());

        if(pc != null && pc.getWarnings().size() != 0)
            component.getWarnings().addAll(pc.getWarnings());
    }

    /**
     * Factory method for obtaining a particular component from a file that can be found in the classpath (including
     * JARs and ZIPs on the classpath).
     */
    public static XdmComponent get(Class componentClass, String fileName, int flags) throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, FileNotFoundException
    {
        FileFind.FileFindResults ffResults = FileFind.findInClasspath(fileName, FileFind.FINDINPATHFLAG_SEARCH_INSIDE_ARCHIVES_LAST);
        if(ffResults.isFileFound())
        {
            if(ffResults.isFoundFileInJar())
            {
                ZipFile zipFile = new ZipFile(ffResults.getFoundFile());
                ZipEntry zipEntry = zipFile.getEntry(ffResults.getSearchFileName());
                String systemId = ffResults.getFoundFile().getAbsolutePath() + "!" + ffResults.getSearchFileName();

                XdmComponent component = getCachedComponent(systemId, flags);
                if(component != null)
                    return component;

                // if we we get to this point, we're parsing an XML file into a given component class
                XdmParseContext pc = null;
                List errors = null;
                List warnings = null;

                // create a new class instance and hang onto the error list for use later
                component = (XdmComponent) discoverClass.newInstance(componentClass, componentClass.getName());
                errors = component.getErrors();
                warnings = component.getWarnings();

                // if the class's attributes and model is not known, get it now
                XmlDataModelSchema.getSchema(componentClass);

                // parse the XML file
                long startTime = System.currentTimeMillis();
                pc = XdmParseContext.parse(component, ffResults.getFoundFile(), zipEntry);
                component.setLoadDuration(startTime, System.currentTimeMillis());

                // if we had some syntax errors, make sure the component records them for later use
                if(pc != null && pc.getErrors().size() != 0)
                    errors.addAll(pc.getErrors());

                if(pc != null && pc.getWarnings().size() != 0)
                    warnings.addAll(pc.getWarnings());

                component.loadedFromXml(flags);

                // if there are no errors, cache this component so if the file is needed again, it's available immediately
                if((flags & XDMCOMPFLAG_CACHE_ALWAYS) != 0 || (((flags & XDMCOMPFLAG_CACHE_WHEN_NO_ERRORS) != 0) && errors.size() == 0))
                    cacheComponent(systemId, component, flags);

                return component;
            }
            else
                return get(componentClass, ffResults.getFoundFile(), flags);
        }
        else
            throw new FileNotFoundException("File '"+ fileName +"' not found in classpath. Searched: " + TextUtils.join(ffResults.getSearchPaths(), ", "));
    }

}
