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

package com.netspective.sparx.sql;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.sql.QuerySelectDialog;

import java.util.Map;
import java.util.HashMap;

/**
 * @author aye
 * $Id: QueryDefinition.java,v 1.1 2003-07-03 22:25:19 aye.thu Exp $
 */
public class QueryDefinition extends com.netspective.axiom.sql.dynamic.QueryDefinition
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] { "container", "identifier" });
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[] {  });
    }

    public class Presentation
    {
        private Map qsDialogs = new HashMap();

        public Presentation()
        {
        }

        public QuerySelectDialog getSelectDialog()
        {
            return new QuerySelectDialog();
        }


        public QuerySelectDialog getDialog()
        {
            return new QuerySelectDialog();
        }

        public QuerySelectDialog createSelectDialog()
        {
            QuerySelectDialog result = new QuerySelectDialog();
            result.setQueryDefn(QueryDefinition.this);
            return result;
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
    private Presentation presentation = new Presentation();

    public QueryDefinition()
    {
        super();
    }

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
}
