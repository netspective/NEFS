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
 * $Id: ValueSourceDocumentation.java,v 1.3 2003-03-24 13:24:31 shahid.shah Exp $
 */

package com.netspective.commons.value;

import java.util.List;
import java.util.ArrayList;

public class ValueSourceDocumentation
{
    public static class Parameter
    {
        private String name;
        private boolean required;
        private String[] enums;
        private String defaultValue;
        private String description;

        public Parameter(String name, boolean required, String[] enums, String defaultValue, String description)
        {
            this.name = name;
            this.required = required;
            this.enums = enums;
            this.defaultValue = defaultValue;
            this.description = description;
        }

        public String getDefaultValue()
        {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue)
        {
            this.defaultValue = defaultValue;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public boolean isRequired()
        {
            return required;
        }

        public void setRequired(boolean required)
        {
            this.required = required;
        }

        public String[] getEnums()
        {
            return enums;
        }

        public void setEnums(String[] enums)
        {
            this.enums = enums;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }
    }

    private String description;
    private List parameters = new ArrayList();

    public ValueSourceDocumentation(String descr, Parameter param)
    {
        this.description = descr;
        addParameter(param);
    }

    public ValueSourceDocumentation(String descr, Parameter[] params)
    {
        this.description = descr;
        for(int i = 0; i < params.length; i++)
            addParameter(params[i]);
    }

    public String getUsageHtml()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < parameters.size(); i++)
        {
            if(i > 0)
                sb.append(",");

            Parameter param = (Parameter) parameters.get(i);
            String name = param.getName();
            if(param.required)
                name = "<b>" + name + "</b>";
            if(param.enums != null)
                name = "<u>" + name + "</u>";
            sb.append(name);
        }
        return sb.toString();
    }

    public String getParamsHtml(String commandId)
    {
        return null;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List getParameters()
    {
        return parameters;
    }

    public void addParameter(Parameter param)
    {
        parameters.add(param);
    }
}
