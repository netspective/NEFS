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
 * $Id: ImportConfigurationTask.java,v 1.1 2003-03-13 18:33:10 shahid.shah Exp $
 */

package com.netspective.commons.ant;

import java.util.Iterator;
import java.util.Map;
import java.io.File;

import org.apache.tools.ant.BuildException;

import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.commons.config.Configuration;
import com.netspective.commons.config.Property;
import com.netspective.commons.config.ConfigurationsManagerComponent;
import com.netspective.commons.xdm.XdmComponentTask;

public class ImportConfigurationTask extends XdmComponentTask
{
    private File file;
    private String configId = ConfigurationsManager.DEFAULT_CONFIG_NAME;
    private String prefix = "config.";

    public ImportConfigurationTask()
    {
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public void setConfigId(String configId)
    {
        this.configId = configId;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public void execute() throws BuildException
    {
        // because there's no "servlet context" available from Ant (command line) we need to simulate it so that if the
        // configuration items refer to the value source servlet-context-path the variables should still work
        File simulatedPath = new File(project.getProperty("app.root.dir"));
        System.setProperty("com.netspective.sparx.util.value.ServletContextPathValue.simulate", simulatedPath.getAbsolutePath());

        ConfigurationsManagerComponent component = (ConfigurationsManagerComponent) getComponent(ConfigurationsManagerComponent.class);
        ConfigurationsManager manager = component.getManager();

        int imported = 0;
        Configuration config = manager.getConfiguration(configId);
        for(Iterator i = config.getChildrenMap().keySet().iterator(); i.hasNext();)
        {
            Map.Entry configEntry = (Map.Entry) i.next();
            Property property = (Property) configEntry.getValue();

            String antPropertyName = prefix + property.getName();
            if(! property.isDynamic())
            {
                project.setProperty(antPropertyName, config.getTextValue(null, property.getName()));
                if(isDebug()) log(antPropertyName + " = " + project.getProperty(antPropertyName));
                imported++;
            }
        }

        log("Imported " + imported + " configuration items from '" + component.getInputSource().getIdentifier() + "' (prefix = '" + prefix + "')");
    }
}