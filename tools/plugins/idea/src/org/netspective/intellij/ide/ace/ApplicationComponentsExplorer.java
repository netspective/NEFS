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
 * $Id: ApplicationComponentsExplorer.java,v 1.1 2003-03-13 18:39:01 shahid.shah Exp $
 */

package org.netspective.intellij.ide.ace;

import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.tree.DefaultMutableTreeNode;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.Anchor;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;

import org.netspective.intellij.ActionID;
import org.netspective.intellij.ide.ace.actions.ApplicationComponentsExplorerToggle;

import com.netspective.commons.xml.ComponentFactory;
import com.netspective.commons.xml.exception.DataModelException;
import com.netspective.axiom.SqlManagerComponent;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.dal.Schema;
import com.netspective.axiom.dal.Tables;

public class ApplicationComponentsExplorer implements ApplicationComponent
{
    public static final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Applications Components");
    public static final DefaultMutableTreeNode appNode = new DefaultMutableTreeNode("Presentation");
    public static final DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode("Data Management");
    public static final DefaultMutableTreeNode dataStaticSqlNode = new DefaultMutableTreeNode("Static SQL");
    public static final DefaultMutableTreeNode dataDynamicSqlNode = new DefaultMutableTreeNode("Dynamic SQL (Query Definitions)");
    public static final DefaultMutableTreeNode dataSchemaSqlNode = new DefaultMutableTreeNode("Schema");
    public static final DefaultMutableTreeNode securityNode = new DefaultMutableTreeNode("Security");

    private static final Logger log = Logger.getInstance(ApplicationComponentsExplorer.class.getName());
    public static final File SCHEMA_FILE = new File("C:/Projects/Frameworks/Axiom/src/java/test/com/netspective/axiom/dal/test-data/schema.xml");

    private DefaultActionGroup spg;
    private ApplicationComponentsExplorerToggle toggleAction;

    static
    {
        rootNode.add(appNode);
        rootNode.add(dataNode);
        dataNode.add(dataStaticSqlNode);
        dataNode.add(dataDynamicSqlNode);
        dataNode.add(dataSchemaSqlNode);
        rootNode.add(securityNode);
    }

    public ApplicationComponentsExplorer()
    {
        toggleAction = new ApplicationComponentsExplorerToggle();
        spg = new DefaultActionGroup();
        spg.addSeparator();
        spg.add(toggleAction);

        try
        {
            SqlManagerComponent component = (SqlManagerComponent) ComponentFactory.get(SqlManagerComponent.class, SCHEMA_FILE, true);
            SqlManager manager = component.getManager();
            Schema schema = manager.getSchema("db");
            Tables tables = schema.getTables();
            for(int i = 0; i < tables.size(); i++)
                dataSchemaSqlNode.add(new DefaultMutableTreeNode(tables.get(i).getNameForMapKey()));
        }
        catch (DataModelException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }

    public void initComponent()
    {
        final ActionManager actionManager = ActionManager.getInstance();
        // Try register action
        if (actionManager.getAction(ApplicationComponentsExplorerToggle.ID) == null)
        {
            actionManager.registerAction(ApplicationComponentsExplorerToggle.ID, toggleAction);
        }
        // Add toggle icon into IntelliJ UI
        // Add to MainToolbar
        DefaultActionGroup mainToolBar = (DefaultActionGroup) actionManager.getAction(ActionID.MAIN_TOOLBAR);
        if (mainToolBar != null)
        {
            mainToolBar.add(spg);
        }
        else
        {
            log.info("Can't find " + ActionID.MAIN_TOOLBAR + " group");
        }

        // Add to menu "Window"
        DefaultActionGroup menuWindow = (DefaultActionGroup) actionManager.getAction(ActionID.WINDOW_MENU);
        if (menuWindow != null)
        {
            menuWindow.add(toggleAction, new Constraints(Anchor.AFTER, ActionID.WINDOW_MENU_INSPECTION));
        }
        else
        {
            log.info("Can't find " + ActionID.WINDOW_MENU + " group");
        }
    }

    public void disposeComponent()
    {
        ActionManager actionManager = ActionManager.getInstance();
        // Remove toggle icon from InteliJ UI
        DefaultActionGroup mainToolBar = (DefaultActionGroup) actionManager.getAction(ActionID.MAIN_TOOLBAR);
        if (mainToolBar != null)
        {
            mainToolBar.remove(spg);
        }

        DefaultActionGroup menuWindow = (DefaultActionGroup) actionManager.getAction(ActionID.WINDOW_MENU);
        if (menuWindow != null)
        {
            menuWindow.remove(toggleAction);
        }

        toggleAction.clear();
        if (actionManager.getAction(ApplicationComponentsExplorerToggle.ID) != null)
        {
            actionManager.unregisterAction(ApplicationComponentsExplorerToggle.ID);
        }
    }

    public String getComponentName()
    {
        return ApplicationComponentsExplorer.class.getName();
    }
}
