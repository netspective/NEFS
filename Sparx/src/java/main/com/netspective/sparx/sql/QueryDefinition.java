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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.sql.QueryBuilderDialog;
import com.netspective.sparx.form.sql.QuerySelectDialog;

/**
 * Class representing a dynamic SQL Statement along with its bind parameters, join conditions
 * and report definitions defined using the <query-defn> in XML.
 */
public class QueryDefinition extends com.netspective.axiom.sql.dynamic.QueryDefinition
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[]{"container", "identifier"});
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[]{});
    }

    public class Presentation
    {
        private Map qsDialogs = new HashMap();

        public Presentation()
        {
        }

        public QuerySelectDialog createSelectDialog()
        {
            return new QuerySelectDialog(getProject(), QueryDefinition.this);
        }

        public QuerySelectDialog createSelectDialog(Class cls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
        {
            Constructor c = cls.getConstructor(new Class[]{Project.class, QueryDefinition.class});
            return (QuerySelectDialog) c.newInstance(new Object[]{getProject(), QueryDefinition.this});
        }

        // created here because we need to ignore text but can't include public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
        public void addText(String text)
        {

        }

        public void addSelectDialog(QuerySelectDialog dialog)
        {
            qsDialogs.put(dialog.getName(), dialog);
        }

        public void addDialog(QuerySelectDialog dialog)
        {
            qsDialogs.put(dialog.getName(), dialog);
        }

        public QuerySelectDialog getSelectDialog(String name)
        {
            return (QuerySelectDialog) qsDialogs.get(name);
        }

        public int size()
        {
            return qsDialogs.size();
        }
    }

    public static final int QBDIALOG_MAXIMUM_CONDITIONS = 5;

    private Project project;
    private QueryBuilderDialog qbDialog = null;
    private Presentation presentation = new Presentation();

    public QueryDefinition(Project project)
    {
        super();
        this.project = project;
    }

    public Project getProject()
    {
        return project;
    }

    /**
     * Sets the unique name for this dynamic query.
     *
     * @param name dynamic query's name
     */
    public void setName(String name)
    {
        super.setName(name);
    }

    public Presentation getPresentation()
    {

        return presentation;
    }

    public Presentation createPresentation()
    {
        return presentation;
    }

    public void addPresentation(Presentation presentation)
    {
        // do nothing, but this method is needed so XDM knows that "presentation" is a valid XML child
    }

    public QueryBuilderDialog createQueryBuilderDialog()
    {
        if(qbDialog == null)
        {
            qbDialog = new QueryBuilderDialog(getProject());
            qbDialog.setName(this.getName());
            qbDialog.setQueryDefn(this);
            qbDialog.setMaxConditions(QBDIALOG_MAXIMUM_CONDITIONS);
            qbDialog.finalizeContents();
        }
        return qbDialog;
    }
}
