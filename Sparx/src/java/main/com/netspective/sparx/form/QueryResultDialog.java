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
package com.netspective.sparx.form;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.schema.SchemaRecordEditorDialog;
import com.netspective.sparx.form.action.ActionDialog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Class for producing a grid-type dialog which is created using a report produced from a query. Each row of the
 * report corresponds to each row of fields in the grid.
 *
 */
public class QueryResultDialog extends ActionDialog implements TemplateProducerParent
{
    private static final Log log = LogFactory.getLog(QueryResultDialog.class);
    public static final String ROW_FIELD_PREFIX = "row-";
    public static final String GRID_FIELD_PREFIX = "grid";

    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[]{"name", "query"};


    public class RowFieldTemplate extends TemplateProducer
    {
        public RowFieldTemplate()
        {
            super("/dialog-type/" + getQualifiedName() + "/row", "row", null, null, false, false);
        }

        public String getTemplateName(String url, String localName, String qName, Attributes attributes) throws SAXException
        {
            return getName();
        }
    }

    private String queryName;
    private TemplateProducers templateProducers;
    private RowFieldTemplate rowFieldTemplateProducer;

    /**
     * Default constructor used by XDM
     */
    public QueryResultDialog()
    {
        super();
    }

    /**
     * Creates a dialog in the project.
     *
     * @param project the project in which the dialog is created
     */
    public QueryResultDialog(Project project)
    {
        super(project);
    }

    /**
     * Creates a dialog in the project.
     *
     * @param project the project in which the dialog is created
     * @param pkg     the package in which the dialog is created
     */
    public QueryResultDialog(Project project, DialogsPackage pkg)
    {
        super(project, pkg);
    }

    public TemplateProducers getTemplateProducers()
    {
        if(templateProducers == null)
        {
            templateProducers = new TemplateProducers();
            rowFieldTemplateProducer = new RowFieldTemplate();
            templateProducers.add(rowFieldTemplateProducer);
        }
        return templateProducers;
    }

    public RowFieldTemplate getRowFieldTemplate()
    {
        return rowFieldTemplateProducer;
    }

    /**
     * Gets the query associated with the dialog
     *
     * @return qualified name of the query
     */
    public String getQuery()
    {
        return queryName;
    }


    /**
     * Sets the query associated with the dialog
     *
     * @param queryName qualified name of the query
     */
    public void setQuery(String queryName)
    {
        this.queryName = queryName;
    }



}
