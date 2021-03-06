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
 * 
 */

package com.netspective.medigy.model.org;

import com.netspective.medigy.model.TestCase;
import com.netspective.medigy.model.contact.City;
import com.netspective.medigy.model.contact.Country;
import com.netspective.medigy.model.contact.County;
import com.netspective.medigy.model.contact.GeographicBoundary;
import com.netspective.medigy.model.contact.PostalCode;
import com.netspective.medigy.model.contact.State;
import com.netspective.medigy.model.party.Party;
import com.netspective.medigy.model.party.PartyContactMechanism;
import com.netspective.medigy.model.party.PartyContactMechanismPurpose;
import com.netspective.medigy.model.party.PartyRelationship;
import com.netspective.medigy.model.party.PartyRole;
import com.netspective.medigy.model.party.PostalAddress;
import com.netspective.medigy.reference.custom.org.OrganizationClassificationType;
import com.netspective.medigy.reference.custom.party.ContactMechanismPurposeType;
import com.netspective.medigy.reference.custom.party.OrganizationRoleType;
import com.netspective.medigy.reference.custom.party.PartyRelationshipType;
import com.netspective.medigy.reference.type.party.PartyType;
import com.netspective.medigy.service.util.PartyRelationshipFacade;
import com.netspective.medigy.service.util.PartyRelationshipFacadeImpl;
import com.netspective.medigy.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;

import java.util.List;
import java.util.Set;

/**
 * Test class to test the Organization data model class. This should only test that the
 * correct child tables are cascaded correctly during CRUD. 
 *
 */
public class TestOrganization  extends TestCase
{
    private static final Log log = LogFactory.getLog(TestOrganization.class);

    /**
     * This is a simple ORG insertion test
     *
     */
    public void testOrganization()
    {
        Organization org = new Organization();
        org.setOrganizationName("Netspective");
        HibernateUtil.getSession().save(org);
        HibernateUtil.closeSession();
        
        // check to make sure the org was saved successfully
        Organization netspective = (Organization) HibernateUtil.getSession().load(Organization.class, org.getOrgId());
        assertNotNull(netspective);
        assertEquals(org.getOrgId(), netspective.getOrgId());
        // make sure there is a PARTY entry also
        Party netspectiveParty = (Party) HibernateUtil.getSession().load(Party.class, org.getOrgId());
        assertNotNull(netspectiveParty);
        assertEquals("Netspective", netspective.getOrganizationName());
        assertEquals(org.getOrgId(), netspectiveParty.getPartyId());
        assertEquals(PartyType.Cache.ORGANIZATION.getEntity(), netspectiveParty.getPartyType());
        
        // update the org
        netspective.setOrganizationName("Acme");
        HibernateUtil.getSession().flush();
        
        Organization acme = (Organization) HibernateUtil.getSession().load(Organization.class, netspective.getOrgId());
        assertEquals("Acme", acme.getOrganizationName());
    }
}
