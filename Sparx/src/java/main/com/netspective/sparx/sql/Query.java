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

import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.sql.QueryDialog;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.QueryReportPanel;

/**
 * Class representing a SQL Statement along with its parameters and report definitions defined using the &lt;query&gt; in XML.
 * The SQL text may be specified as the first text child in the query (mixed content).
 */
public class Query extends com.netspective.axiom.sql.Query
{
    /**
     * Child class for handling the presentation aspects of the query
     */
    public class Presentation
    {
        private Map reportPanels = new HashMap();
        private Map reportDialogs = new HashMap();
        private QueryReportPanel defaultPanel;
        private QueryDialog defaultDialog;

        /**
         * Creates a display report panel for the query
         */
        public QueryReportPanel createPanel()
        {
            QueryReportPanel result = new QueryReportPanel();
            result.setQuery(Query.this);
            return result;
        }

        /**
         * Creates a dialog for the query input
         */
        public QueryDialog createDialog()
        {
            QueryDialog result = new QueryDialog(getProject());
            result.setName("query-" + Query.this.getQualifiedName());
            result.getFrame().setHeading(new StaticValueSource("Query: " + Query.this.getQualifiedName()));
            result.setQuery(Query.this);
            return result;
        }

        // created here because we need to ignore text but can't include public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
        public void addText(String text)
        {

        }

        /**
         * Adds a display report panel for the query. Multiple report panels representing different presentations
         * of the results of the query can be added to each query.
         */
        public void addPanel(QueryReportPanel panel)
        {
            if(reportPanels.size() == 0 || panel.isDefaultPanel())
                defaultPanel = panel;
            reportPanels.put(panel.getName(), panel);
        }

        /**
         * Adds  a dialog associated with the query
         */
        public void addDialog(QueryDialog dialog)
        {
            if(reportDialogs.size() == 0)
                defaultDialog = dialog;
            reportDialogs.put(dialog.getName(), dialog);
        }

        /**
         * Gets the default report panel for the query. Usually the default panel is the one that is defined without
         * a name associated with it.
         */
        public QueryReportPanel getDefaultPanel()
        {
            if(defaultPanel == null)
            {
                defaultPanel = new QueryReportPanel();
                defaultPanel.setQuery(Query.this);
                defaultPanel.setFrame(new HtmlPanelFrame());
            }

            return defaultPanel;
        }

        /**
         * Gets the default dialog for the query
         */
        public QueryDialog getDefaultDialog()
        {
            if(defaultDialog == null)
            {
                defaultDialog = createDialog();
                defaultDialog.createParamFields();
                defaultDialog.showRowsPerPageField();
            }

            return defaultDialog;
        }

        /**
         * Gets the report panel
         *
         * @param name report panel name
         *
         * @return report panel object
         */
        public QueryReportPanel getPanel(String name)
        {
            return (QueryReportPanel) reportPanels.get(name);
        }

        /**
         * Gets a map of all the report panels of this query
         */
        public Map getPanels()
        {
            return reportPanels;
        }

        public QueryDialog getDialog(String name)
        {
            return (QueryDialog) reportDialogs.get(name);
        }

        /**
         * Sets the default report panel for this query
         */
        public void setDefaultPanel(QueryReportPanel defaultPanel)
        {
            this.defaultPanel = defaultPanel;
        }

        /**
         * Sets the default dialog for this query
         */
        public void setDefaultDialog(QueryDialog defaultDialog)
        {
            this.defaultDialog = defaultDialog;
        }

        /**
         * Gets the number of report panels for this query
         */
        public int size()
        {
            return reportPanels.size();
        }
    }

    private Project project;
    private Presentation presentation = new Presentation();

    public Query(Project project)
    {
        this.project = project;
    }

    public Query(Project project, QueriesNameSpace nameSpace)
    {
        super(nameSpace);
        this.project = project;
    }

    /**
     * Gets the <code>Project</code> associated with this query
     */
    public Project getProject()
    {
        return project;
    }

    /**
     * Gets the Presentation object of this query.
     */
    public Presentation getPresentation()
    {
        return presentation;
    }

    /**
     * Returns the Presentation object of this query. This is used by XDM.
     */
    public Presentation createPresentation()
    {
        return presentation;
    }

    /**
     * Empty method. This  method is needed so XDM knows that "presentation" is a valid XML child
     */
    public void addPresentation(Presentation presentation)
    {
        // do nothing, but this method is needed so XDM knows that "presentation" is a valid XML child
    }
}
