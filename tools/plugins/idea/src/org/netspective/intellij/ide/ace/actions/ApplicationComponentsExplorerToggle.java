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
 * $Id: ApplicationComponentsExplorerToggle.java,v 1.1 2003-03-13 18:39:01 shahid.shah Exp $
 */

package org.netspective.intellij.ide.ace.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

import org.netspective.intellij.ide.ace.ApplicationComponentsExplorerView;

import javax.swing.*;
import java.util.*;

/**
 * Toggle Properties ApplicationComponentsExplorerView
 */
public class ApplicationComponentsExplorerToggle extends ToggleAction
{
    public static final String ID = "ViewNetspectiveACEWindow";
    private static final Icon ICON = new ImageIcon(ApplicationComponentsExplorerToggle.class.getResource("toggle.gif"));
    private Map consoles = new HashMap();
    private List managers = new ArrayList();

    public boolean isSelected(AnActionEvent event)
    {
        Project project = (Project) event.getDataContext().getData(DataConstants.PROJECT);
        return project != null && consoles.get(project) != null;
    }

    public void setSelected(AnActionEvent event, boolean isSelected)
    {
        Project project = (Project) event.getDataContext().getData(DataConstants.PROJECT);
        if (project != null)
        {
            final ToolWindowManager manager = ToolWindowManager.getInstance(project);
            ToolWindow console = (ToolWindow) consoles.get(project);
            if (isSelected && console == null)
            {
                // Show window
                manager.registerToolWindow(ApplicationComponentsExplorerView.ID, new ApplicationComponentsExplorerView(), ToolWindowAnchor.LEFT);
                console = manager.getToolWindow(ApplicationComponentsExplorerView.ID);
                if (console != null)
                {
                    // Add manager to array to do unregister on finalize
                    managers.add(manager);
                    consoles.put(project, console);
                    console.setIcon(ICON);
                    console.show(null);
                    console.activate(null);
                }
            }
            else if (!isSelected && console != null)
            {
                // Hide window
                console.hide(null);
                manager.unregisterToolWindow(ApplicationComponentsExplorerView.ID);
                managers.remove(manager);
                consoles.remove(project);
            }
        }
    }

    public ApplicationComponentsExplorerToggle()
    {
        super(ApplicationComponentsExplorerView.ID, "Show/Hide " + ApplicationComponentsExplorerView.ID, ICON);
    }

    public void clear()
    {
        // Unregister all ToolWindows
        Iterator iterator = managers.iterator();
        while (iterator.hasNext())
        {
            ToolWindowManager manager = (ToolWindowManager) iterator.next();
            manager.unregisterToolWindow(ApplicationComponentsExplorerView.ID);
        }
        managers.clear();
        consoles.clear();
    }

}