package com.netspective.sparx.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.report.tabular.PresentationValueDataSource;
import com.netspective.sparx.form.field.type.SelectFieldChoicesPanel;
import com.netspective.sparx.form.DialogContext;

import java.io.Writer;
import java.io.IOException;
import java.util.StringTokenizer;


public class ListValueSourceCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(ValueSourceCommand.class);

    public static final String LVSTYPENAME_REFERENCE = "reference";
    public static final String LVSTYPENAME_INSTANCE  = "instance";
    static public final int LVSTYPE_UNKNOWN     = 0;
    static public final int LVSTYPE_REFERENCE   = 1;
    static public final int LVSTYPE_INSTANCE    = 2;
    public static final String[] IDENTIFIERS = new String[] { "list", "list-value" };
    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays the contents of a StaticListValueSource. Useful for displaying in a popup window and retrieving values. Parameters are delimited with '|'.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("value-source-type", true, "The value-source-type parameter may be either 'reference' or 'instance'. When it is set " +
                                "to 'instance', the value-source-spec parameter is basically the value-source spefication like xxx:yyy. " +
                                "When the value-source-type parameter is set to 'reference' it means that the value-source-spec is actually " +
                                "a single value source that points to an actual ListValueSource at runtime)."),
                new CommandDocumentation.Parameter("value-source-spec", true, "The value source specification (depends upon value-source-type)."),
                new QueryCommand.SkinParameter(),
            }
    );

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

        if(params.hasMoreTokens())
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
        if(valueSourceType.equals(LVSTYPENAME_REFERENCE))
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
     *
     * @param writer
     * @param nc
     * @param unitTest
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        Theme theme = nc.getActiveTheme();
        switch (valueSourceType)
        {
            case LVSTYPE_INSTANCE:
                if(valueSource != null)
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
                if(valueSource != null)
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
