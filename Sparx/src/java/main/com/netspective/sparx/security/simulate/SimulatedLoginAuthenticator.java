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
 * $Id: SimulatedLoginAuthenticator.java,v 1.3 2004-08-08 22:55:16 shahid.shah Exp $
 */

package com.netspective.sparx.security.simulate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.acl.PermissionNotFoundException;
import com.netspective.commons.acl.RoleNotFoundException;
import com.netspective.commons.security.AuthenticatedUserInitializationException;
import com.netspective.commons.security.MutableAuthenticatedOrganization;
import com.netspective.commons.security.MutableAuthenticatedOrganizations;
import com.netspective.commons.security.MutableAuthenticatedUser;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.security.LoginDialogContext;
import com.netspective.sparx.security.authenticator.AbstractLoginAuthenticator;

public class SimulatedLoginAuthenticator extends AbstractLoginAuthenticator
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(SimulatedLoginAuthenticator.class);

    public boolean isUserValid(HttpLoginManager loginManager, LoginDialogContext loginDialogContext)
    {
        // when simulating the user is always valid
        return true;
    }

    public void initAuthenticatedUser(HttpLoginManager loginManager, LoginDialogContext ldc, MutableAuthenticatedUser user) throws AuthenticatedUserInitializationException
    {
        super.initAuthenticatedUser(loginManager, ldc, user);

        SimulatedLoginDialog sld = (SimulatedLoginDialog) ldc.getLoginDialog();

        user.setUserId(sld.getUserId());
        user.setUserName(sld.getUserName());

        if(sld.getUserOrgId() != null)
        {
            MutableAuthenticatedOrganizations orgs = (MutableAuthenticatedOrganizations) user.getOrganizations();
            MutableAuthenticatedOrganization org = orgs.createOrganization();
            org.setPrimary(true);
            org.setOrgId(sld.getUserOrgId());
            org.setOrgName(sld.getUserOrgName());
            orgs.addOrganization(org);
        }

        if(sld.getPermissions() != null)
        {
            try
            {
                user.setPermissions(ldc.getProject(), sld.getPermissions());
            }
            catch(PermissionNotFoundException e)
            {
                log.error("Error assigning permissions to user " + user.getUserId(), e);
            }
        }

        if(sld.getRoles() != null)
        {
            try
            {
                user.setRoles(ldc.getProject(), sld.getRoles());
            }
            catch(RoleNotFoundException e)
            {
                log.error("Error assigning roles to user " + user.getUserId(), e);
            }
        }
    }
}
