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
 * $Id: BasicAuthenticatedOrganizations.java,v 1.1 2004-08-08 22:53:32 shahid.shah Exp $
 */

package com.netspective.commons.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BasicAuthenticatedOrganizations implements MutableAuthenticatedOrganizations
{
    private static final Log log = LogFactory.getLog(BasicAuthenticatedOrganizations.class);
    private MutableAuthenticatedOrganization primary;
    private List allOrgs = new ArrayList();
    private List nonPrimaryOrgs = new ArrayList();
    private AuthenticatedOrganization[] allOrgsArray = new AuthenticatedOrganization[0];
    private AuthenticatedOrganization[] nonPrimaryOrgsArray = new AuthenticatedOrganization[0];

    public MutableAuthenticatedOrganization createOrganization()
    {
        return new BasicAuthenticatedOrganization();
    }

    public AuthenticatedOrganization getPrimaryOrganization()
    {
        return primary;
    }

    public AuthenticatedOrganization[] getOrganizations(boolean includePrimary)
    {
        return includePrimary ? allOrgsArray : nonPrimaryOrgsArray;
    }

    public void addOrganization(MutableAuthenticatedOrganization organization)
    {
        if(organization.isPrimary())
        {
            if(primary != null)
                log.warn("Overriding primary org '"+ primary.toString()  +"' with '"+ organization.toString() +"'.");

            primary = organization;
            allOrgs.add(organization);
            allOrgsArray = (AuthenticatedOrganization[]) allOrgs.toArray(new AuthenticatedOrganization[allOrgs.size()]);
        }
        else
        {
            nonPrimaryOrgs.add(organization);
            nonPrimaryOrgsArray = (AuthenticatedOrganization[]) nonPrimaryOrgs.toArray(new AuthenticatedOrganization[nonPrimaryOrgs.size()]);
        }
    }
}
