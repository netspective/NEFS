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
 * $Id: AuthenticatedUserValueSource.java,v 1.5 2004-08-14 19:57:59 shahid.shah Exp $
 */

package com.netspective.sparx.value.source;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.security.AuthenticatedOrganization;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XmlDataModelSchema.AttributeAccessor;
import com.netspective.commons.xdm.exception.InvalidXdmEnumeratedAttributeValueException;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.value.HttpServletValueContext;

public class AuthenticatedUserValueSource extends AbstractValueSource
{
    private static final Log log = LogFactory.getLog(AuthenticatedUserValueSource.class);

    private static final String[] ATTR_TYPE_VALUES = new String[] { "user-id", "user-name", "org-id", "org-name", "encrypted-password", "[custom]" };
    public static final String[] IDENTIFIERS = new String[] { "authenticated-user" };
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation(
            "Provides access to the attributes in the currently authenticated user.",
            new ValueSourceDocumentation.Parameter[]
            {
                new ValueSourceDocumentation.Parameter("login-manager", false, "active", "The login manager to use."),
                new ValueSourceDocumentation.Parameter("attribute-name", true, ATTR_TYPE_VALUES, null, "An attribute of the user. If one of the enumerated attributes is not provide, then the AuthenticatedUser.getAttribute([custom]) method will be used."),
            }
    );

    private AttributeType attrType = new AttributeType();
    private String loginManagerName;
    private String customAttrName;

    public AuthenticatedUserValueSource()
    {

    }

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);

        // check to see if we have two parameters or one -- if we have two, the first is the login manager and the
        // second is the attribute name; otherwise, with a single parameter we use the "active" login manager the
        // single parameter is the attribute name

        String attrNameParam = null;

        StringTokenizer st = new StringTokenizer(spec.getParams(), ",");
        if(st.hasMoreTokens())
            loginManagerName = st.nextToken();

        if(st.hasMoreTokens())
            attrNameParam = st.nextToken();
        else
        {
            attrNameParam = loginManagerName;
            loginManagerName = null;
        }

        try
        {
            attrType.setValue(attrNameParam);
        }
        catch (InvalidXdmEnumeratedAttributeValueException e)
        {
            attrType.setValue(AttributeType.CUSTOM);
            customAttrName = spec.getParams();
        }

    }

    public Value getAuthenticatedUserAttrValue(AuthenticatedUser authUser)
    {
        if(authUser == null)
            return new GenericValue("No active user");
        switch(attrType.getValueIndex())
        {
            case AttributeType.USER_ID:
                return new GenericValue(authUser.getUserId());

            case AttributeType.USER_NAME:
                return new GenericValue(authUser.getUserName());

            case AttributeType.ORG_ID:
                AuthenticatedOrganization authOrg = authUser.getOrganizations().getPrimaryOrganization();
                return new GenericValue(authOrg != null ? authOrg.getOrgId() : null);

            case AttributeType.ORG_NAME:
                authOrg = authUser.getOrganizations().getPrimaryOrganization();
                return new GenericValue(authOrg != null ? authOrg.getOrgName() : null);

            case AttributeType.ENC_PASSWORD:
                return new GenericValue(authUser.getEncryptedPassword());

            case AttributeType.CUSTOM:
                XmlDataModelSchema schema = XmlDataModelSchema.getSchema(authUser.getClass());
                AttributeAccessor accessor = (AttributeAccessor) schema.getAttributeAccessors().get(customAttrName);
                if(accessor != null)
                {
                    try
                    {
                        return new GenericValue(accessor.get(null, authUser));
                    }
                    catch (Exception e)
                    {
                        log.error(e);
                        return new GenericValue("Error accessing " + customAttrName);
                    }
                }
                return new GenericValue("No accessor for " + customAttrName);

            default:
                log.error("Invalid attribute type " + attrType.getValueIndex());
                return new GenericValue("Invalid attribute type "+ attrType.getValueIndex() +" in " + getClass().getName() + ".getValue(ValueContext)");
        }
    }

    public Value getValue(ValueContext vc)
    {
        if(loginManagerName != null)
        {
            HttpServletValueContext hsvc = ((HttpServletValueContext) vc);
            HttpLoginManager loginManager = hsvc.getProject().getLoginManagers().getLoginManager(loginManagerName);
            return getAuthenticatedUserAttrValue(loginManager.getAuthenticatedUser(hsvc));
        }
        else
            return getAuthenticatedUserAttrValue(vc.getAuthenticatedUser());
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

    public class AttributeType extends XdmEnumeratedAttribute
    {
        public static final int USER_ID      = 0;
        public static final int USER_NAME    = 1;
        public static final int ORG_ID       = 2;
        public static final int ORG_NAME     = 3;
        public static final int ENC_PASSWORD = 4;
        public static final int CUSTOM       = 5;

        public AttributeType()
        {
        }

        public AttributeType(int valueIndex)
        {
            super(valueIndex);
        }

        public String[] getValues()
        {
            return ATTR_TYPE_VALUES;
        }
    }

}
