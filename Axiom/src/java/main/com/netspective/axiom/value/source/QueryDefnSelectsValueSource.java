/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following 
 * conditions are provided as a summary of the NSL but the NSL remains the 
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL. 
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only 
 *    (as Java .class files or a .jar file containing the .class files) and only 
 *    as part of an application that uses The Software as part of its primary 
 *    functionality. No distribution of the package is allowed as part of a software 
 *    development kit, other library, or development tool without written consent of 
 *    Netspective Corporation. Any modified form of The Software is bound by 
 *    these same restrictions.
 * 
 * 3. Redistributions of The Software in any form must include an unmodified copy of 
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective 
 *    Corporation and may not be used to endorse products derived from The 
 *    Software without without written consent of Netspective Corporation. "Sparx" 
 *    and "Netspective" may not appear in the names of products derived from The 
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the 
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind. 
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING 
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.      
 *
 * @author Shahid N. Shah
 */
 
/**
 * $Id: QueryDefnSelectsValueSource.java,v 1.1 2003-05-30 23:06:54 shahid.shah Exp $
 */

package com.netspective.axiom.value.source;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.netspective.axiom.value.source.QueryDefnItemValueSource;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.sql.dynamic.QueryDefnFields;
import com.netspective.axiom.sql.dynamic.QueryDefnField;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.source.StaticValueSource;

public class QueryDefnSelectsValueSource extends QueryDefnItemValueSource
{
    static public final ValueSource CUSTOMIZE = new StaticValueSource("Customize...");

    public static final String[] IDENTIFIERS = new String[] { "query-defn-selects" };
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation(
            "Retrieves the list of selects defined in a particular query definition.",
            new ValueSourceDocumentation.Parameter[]
            {
                new QueryDefnSourceParameter(),
                new ValueSourceDocumentation.Parameter("allow-custom", false, "Set to 'yes' to add 'Customize...' or a custom non-null string to list of selects."),
            }
    );

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private ValueSource allowCustom;

    public QueryDefnSelectsValueSource()
    {
    }

    public QueryDefnSelectsValueSource(QueryDefinition queryDefn)
    {
        super(queryDefn);
    }

    public void initialize(StringTokenizer params)
    {
        super.initialize(params);
        if(params.hasMoreTokens())
        {
            String allowCustom = params.nextToken();
            if(allowCustom.equals("no"))
                this.allowCustom = null;
            else if(allowCustom.equals("yes"))
                this.allowCustom = CUSTOMIZE;
            else
                this.allowCustom = ValueSources.getInstance().getValueSourceOrStatic(allowCustom);
        }
        else
            this.allowCustom = CUSTOMIZE;
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        DatabaseConnValueContext dcvc = (DatabaseConnValueContext) vc;
        com.netspective.axiom.sql.dynamic.QueryDefinition qd = getQueryDefn(dcvc);
        QueryDefnFields fields = qd.getFields();

        PresentationValue result = new PresentationValue();
        PresentationValue.Items items = result.createItems();

        for(int i = 0; i < fields.size(); i++)
        {
            QueryDefnField field = fields.get(i);
            items.addItem(field.getCaption(), field.getName());
        }

        if(allowCustom != null)
            items.addItem(allowCustom.getTextValue(vc));

        return result;
    }

    public Value getValue(ValueContext vc)
    {
        DatabaseConnValueContext dcvc = (DatabaseConnValueContext) vc;
        com.netspective.axiom.sql.dynamic.QueryDefinition qd = getQueryDefn(dcvc);
        QueryDefnFields fields = qd.getFields();

        List result = new ArrayList();
        for(int i = 0; i < fields.size(); i++)
            result.add(fields.get(i).getName());

        if(allowCustom != null)
            result.add(allowCustom.getTextValue(vc));

        return new GenericValue(result);
    }

    public boolean hasValue(ValueContext vc)
    {
        return true;
    }
}