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
package com.netspective.sparx.sql;

import java.util.HashMap;
import java.util.Map;

import com.netspective.axiom.sql.StoredProceduresNameSpace;
import com.netspective.sparx.Project;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.StoredProcedureReportPanel;

/**
 * Class for handling the &lt;stored-procedure&gt; entry defined in the project.
 * It extends the <code>com.netspective.axiom.sql.StoredProcedure</code> by providing reporting
 * functionalities.
 */
public class StoredProcedure extends com.netspective.axiom.sql.StoredProcedure
{

    /**
     * Child class for handling the presentation aspects of the stored query
     */
    public class Presentation
    {
        private Map reportPanels = new HashMap();
        private StoredProcedureReportPanel defaultPanel;

        /**
         * Creates a display report panel for the query
         *
         * @return
         */
        public StoredProcedureReportPanel createPanel()
        {
            StoredProcedureReportPanel result = new StoredProcedureReportPanel();
            result.setStoredProcedure(StoredProcedure.this);
            return result;
        }

        /**
         * Creates a dialog for the stored procedure input
         * @return
         */
        /*
        public QueryDialog createDialog()
        {
            StoredProcedureDialog result = new StoredProcedureDialog(getProject());
            result.setName("query-" + StoredProcedure.this.getQualifiedName());
            result.getFrame().setHeading(new StaticValueSource("Query: " + StoredProcedure.this.getQualifiedName()));
            result.setStored(StoredProcedure.this);
            return result;
        }
        */

        /**
         * Adds a display report panel for the stored procedure. Multiple report panels representing different presentations
         * of the results of the stored procedure can be added.
         *
         * @param panel
         */
        public void addPanel(StoredProcedureReportPanel panel)
        {
            if (reportPanels.size() == 0 || panel.isDefaultPanel())
                defaultPanel = panel;
            reportPanels.put(panel.getName(), panel);
        }

        /**
         * Gets the default report panel for the stored procedure. Usually the default panel is the one that is defined without
         * a name associated with it.
         *
         * @return
         */
        public StoredProcedureReportPanel getDefaultPanel()
        {
            if (defaultPanel == null)
            {
                defaultPanel = new StoredProcedureReportPanel();
                defaultPanel.setStoredProcedure(StoredProcedure.this);
                defaultPanel.setFrame(new HtmlPanelFrame());
            }
            return defaultPanel;
        }

        /**
         * Gets the report panel
         *
         * @param name report panel name
         *
         * @return report panel object
         */
        public StoredProcedureReportPanel getPanel(String name)
        {
            return (StoredProcedureReportPanel) reportPanels.get(name);
        }

        /**
         * Gets a map of all the report panels of this stored procedure
         *
         * @return
         */
        public Map getPanels()
        {
            return reportPanels;
        }

        /**
         * Sets the default report panel for this stored procedure
         *
         * @param defaultPanel
         */
        public void setDefaultPanel(StoredProcedureReportPanel defaultPanel)
        {
            this.defaultPanel = defaultPanel;
        }

        /**
         * Gets the number of report panels for this stored procedure
         *
         * @return
         */
        public int size()
        {
            return reportPanels.size();
        }

        // required for XDM because XmlDataModelSchema.Options().setIgnorePcData(true) cannot be used in inner class
        public void addText(String text)
        {
            // do nothing, we're ignoring the text
        }
    }

    private Project project;
    private StoredProcedure.Presentation presentation = new StoredProcedure.Presentation();

    public StoredProcedure(Project project)
    {
        this.project = project;
    }

    public StoredProcedure(Project project, StoredProceduresNameSpace nameSpace)
    {
        super(nameSpace);
        this.project = project;
    }

    public Project getProject()
    {
        return project;
    }

    /**
     * Gets the Presentation object of this stored procedure.
     *
     * @return
     */
    public Presentation getPresentation()
    {
        return presentation;
    }

    /**
     * Returns the Presentation object of this stored procedure. This is used by XDM.
     *
     * @return
     */
    public Presentation createPresentation()
    {
        return presentation;
    }

    /**
     * Empty method. This  method is needed so XDM knows that "presentation" is a valid XML child
     *
     * @param presentation
     */
    public void addPresentation(Presentation presentation)
    {
        // do nothing, but this method is needed so XDM knows that "presentation" is a valid XML child
    }

}
