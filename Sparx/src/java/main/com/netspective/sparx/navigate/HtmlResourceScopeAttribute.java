package com.netspective.sparx.navigate;

import com.netspective.commons.xdm.XdmEnumeratedAttribute;

public class HtmlResourceScopeAttribute extends XdmEnumeratedAttribute
{
    public static final int THEME = 0;
    public static final int APP = 1;
    public static final int CUSTOM = 2;

    private static final String[] VALUES = new String[]{
        "theme", "app", "custom"
    };

    public HtmlResourceScopeAttribute()
    {
    }

    public HtmlResourceScopeAttribute(int valueIndex)
    {
        super(valueIndex);
    }

    public String[] getValues()
    {
        return VALUES;
    }
}
