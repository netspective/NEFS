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
package com.netspective.commons.value.source;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XmlDataModelSchema.AttributeAccessor;

public abstract class AbstractObjectInstanceWrapperValueSource extends AbstractValueSource
{
    private static final Log log = LogFactory.getLog(AbstractObjectInstanceWrapperValueSource.class);
    private static final GenericValue NO_INSTANCE_MESSAGE = new GenericValue("No instance available.");

    private String customAttrName;
    private ValueSource defaultValue;

    public AbstractObjectInstanceWrapperValueSource()
    {

    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);
        StringTokenizer st = new StringTokenizer(spec.getParams());
        customAttrName = st.nextToken();
        if(st.hasMoreTokens())
            defaultValue = ValueSources.getInstance().getValueSourceOrStatic(st.nextToken());
        else
            defaultValue = null;
    }

    protected abstract Object getActiveInstance(ValueContext vc);

    protected Value getNoInstanceAvailableMessage()
    {
        return NO_INSTANCE_MESSAGE;
    }

    protected Value getInstanceAttrValue(ValueContext vc, Object instance)
    {
        if(instance == null)
            return getNoInstanceAvailableMessage();

        XmlDataModelSchema schema = XmlDataModelSchema.getSchema(instance.getClass());
        AttributeAccessor accessor = (AttributeAccessor) schema.getAttributeAccessors().get(customAttrName);
        if(accessor != null)
        {
            try
            {
                final Object value = accessor.get(null, instance);
                return new GenericValue(value != null
                                        ? value : (defaultValue != null ? defaultValue.getValue(vc) : null));
            }
            catch(Exception e)
            {
                log.error(e);
                return defaultValue != null
                       ? defaultValue.getValue(vc) : new GenericValue("Error accessing " + customAttrName);
            }
        }
        return defaultValue != null
               ? defaultValue.getValue(vc)
               : new GenericValue("No accessor for " + customAttrName + " in " + instance.getClass());
    }

    public Value getValue(ValueContext vc)
    {
        return getInstanceAttrValue(vc, getActiveInstance(vc));
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        return new PresentationValue(getValue(vc));
    }

    public boolean hasValue(ValueContext vc)
    {
        Value value = getValue(vc);
        return value.getListValue().size() > 0;
    }
}