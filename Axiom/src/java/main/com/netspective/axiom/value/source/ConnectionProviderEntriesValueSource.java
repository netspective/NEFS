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
 * $Id: ConnectionProviderEntriesValueSource.java,v 1.4 2003-05-24 20:27:57 shahid.shah Exp $
 */

package com.netspective.axiom.value.source;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

import org.apache.oro.text.perl.Perl5Util;

import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.ConnectionProvider;
import com.netspective.axiom.ConnectionProviderEntries;

public class ConnectionProviderEntriesValueSource extends AbstractValueSource
{
    public static final String[] IDENTIFIERS = new String[] { "data-sources" };
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation(
            "Provides a list of all JDBC data sources in the default ConnectionProvider prefixed by a particular " +
            "filter criteria.",
            new ValueSourceDocumentation.Parameter[]
            {
                new ValueSourceDocumentation.Parameter("filter-reg-ex", false, ".*", "The Perl5 regular expression to use as a filter.")
            }
    );

    static public Perl5Util perlUtil = new Perl5Util();
    static public String ALL_ENTRIES_FILTER = "/.*/";

    private String filter;

    public ConnectionProviderEntriesValueSource()
    {
        filter = ALL_ENTRIES_FILTER;
    }

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    public String getFilter()
    {
        return filter;
    }

    public void setFilter(String filter)
    {
        if(filter.startsWith("/"))
            this.filter = filter;
        else
            this.filter = "/" + filter + "/";
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);

        StringTokenizer st = new StringTokenizer(spec.getParams(), ",");
        if(st.hasMoreTokens())
        {
            String filterParam = st.nextToken().trim();
            if(filterParam.equals(""))
                filter = ALL_ENTRIES_FILTER;
            else
                setFilter(filterParam);
        }
        else
            filter = ALL_ENTRIES_FILTER;
    }

    public Value getValue(ValueContext vc)
    {
        ValueSources.getInstance().assertValueContextInstance(DatabaseConnValueContext.class, vc, this);
        DatabaseConnValueContext dbcvc = (DatabaseConnValueContext) vc;

        ConnectionProvider cp = dbcvc.getConnectionProvider();
        ConnectionProviderEntries entries = cp.getDataSourceEntries();

        List entriesSelected = new ArrayList();
        for(Iterator i = entries.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) i.next();
            String dataSourceId = (String) entry.getKey();
            if(perlUtil.match(filter, dataSourceId))
                entriesSelected.add(dataSourceId);
        }

        return new GenericValue(entriesSelected);
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        ValueSources.getInstance().assertValueContextInstance(DatabaseConnValueContext.class, vc, this);
        DatabaseConnValueContext dbcvc = (DatabaseConnValueContext) vc;

        ConnectionProvider cp = dbcvc.getConnectionProvider();
        ConnectionProviderEntries entries = cp.getDataSourceEntries();

        PresentationValue result = new PresentationValue();
        PresentationValue.Items items = result.createItems();
        for(Iterator i = entries.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) i.next();
            String dataSourceId = (String) entry.getKey();
            if(perlUtil.match(filter, dataSourceId))
            {
                PresentationValue.Items.Item item = items.addItem();
                item.setCaption(dataSourceId);
                item.setCustom(entry.getValue()); // the custom data is the ConnectionProviderEntry instance
            }
        }

        return result;
    }

    public boolean hasValue(ValueContext vc)
    {
        Value value = getValue(vc);
        return value.getListValue().size() > 0;
    }
}
