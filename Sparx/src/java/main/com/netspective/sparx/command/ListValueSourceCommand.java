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
package com.netspective.sparx.command;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.field.type.SelectFieldChoicesPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.PresentationValueDataSource;
import com.netspective.sparx.theme.Theme;


public class ListValueSourceCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(ValueSourceCommand.class);

    public static final String LVSTYPENAME_REFERENCE = "reference";
    public static final String LVSTYPENAME_INSTANCE = "instance";
    static public final int LVSTYPE_UNKNOWN = 0;
    static public final int LVSTYPE_REFERENCE = 1;
    static public final int LVSTYPE_INSTANCE = 2;
    public static final String[] IDENTIFIERS = new String[]{"list", "list-value"};
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation("Displays the contents of a StaticListValueSource. Useful for displaying in a popup window and retrieving values. Parameters are delimited with '|'.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("value-source-type", true, "The value-source-type parameter may be either 'reference' or 'instance'. When it is set " +
            "to 'instance', the value-source-spec parameter is basically the value-source spefication like xxx:yyy. " +
            "When the value-source-type parameter is set to 'reference' it means that the value-source-spec is actually " +
            "a single value source that points to an actual ListValueSource at runtime)."),
                new CommandDocumentation.Parameter("value-source-spec", true, "The value source specification (depends upon value-source-type)."),
                new QueryCommand.SkinParameter(),
            });

    private int valueSourceType;
    private String valueSourceSpec;
    private ValueSource valueSource;
    private String skinName;

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public String getParametersDelimiter()
    {
        return "|"; // this because we accept value source specs that may contain commas or semicolons
    }

    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    public void setParameters(String params)
    {
        super.setParameters(params);    //To change body of overriden methods use Options | File Templates.
    }

    public void setParameters(StringTokenizer params)
    {
        setValueSourceType(params.nextToken());
        setValueSourceSpec(params.nextToken());

        if (params.hasMoreTokens())
        {
            skinName = params.nextToken();
        }
    }

    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(valueSourceType);
        sb.append(delim);
        sb.append(valueSourceSpec);
        if (skinName != null)
        {
            sb.append(delim);
            sb.append(skinName);
        }
        return sb.toString();
    }

    public void setValueSourceType(int valueSourceType)
    {
        this.valueSourceType = valueSourceType;
    }

    public void setValueSourceType(String valueSourceType)
    {
        if (valueSourceType.equals(LVSTYPENAME_REFERENCE))
            setValueSourceType(LVSTYPE_REFERENCE);
        else
            setValueSourceType(LVSTYPE_INSTANCE);
    }

    public void setValueSourceSpec(String valueSourceSpec)
    {
        this.valueSourceSpec = valueSourceSpec;
        setValueSource(ValueSources.getInstance().getValueSourceOrStatic(valueSourceSpec));
    }

    public String getValueSourceSpec()
    {
        return valueSourceSpec;
    }

    public void setValueSource(ValueSource valueSourceInstance)
    {
        this.valueSource = valueSourceInstance;
    }

    public ValueSource getValueSource()
    {
        return valueSource;
    }


    /**
     * @param writer
     * @param nc
     * @param unitTest
     *
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        Theme theme = nc.getActiveTheme();
        switch (valueSourceType)
        {
            case LVSTYPE_INSTANCE:
                if (valueSource != null)
                {
                    SelectFieldChoicesPanel panel = new SelectFieldChoicesPanel();
                    panel.setReportSkin(skinName);

                    String listValueSourceString = getValueSource().getTextValue(nc);
                    ValueSource listValueSource = ValueSources.getInstance().getValueSourceOrStatic(listValueSourceString);
                    panel.setDataSource(new PresentationValueDataSource(listValueSource.getPresentationValue(nc)));
                    panel.render(writer, nc, theme, 1);
                }
                break;

            case LVSTYPE_REFERENCE:
                if (valueSource != null)
                {
                    SelectFieldChoicesPanel panel = new SelectFieldChoicesPanel();
                    panel.setReportSkin(skinName);
                    panel.setDataSource(new PresentationValueDataSource(valueSource.getPresentationValue(nc)));
                    panel.render(writer, nc, theme, 1);
                }
                break;

            default:
                log.error("Unknown value source type for List Value Source command");
                break;
        }
    }

    public void handleCommand(Writer writer, DialogContext dc, boolean unitTest) throws CommandException, IOException
    {
        this.handleCommand(writer, dc.getNavigationContext(), unitTest);
    }
}
