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
 * @author Aye Thu
 */

package com.netspective.medigy.model.org;

import com.netspective.medigy.model.TestCase;
import com.netspective.medigy.model.party.PartyRole;
import com.netspective.medigy.model.person.TestPerson;
import com.netspective.medigy.model.session.ProcessSession;
import com.netspective.medigy.model.session.Session;
import com.netspective.medigy.model.session.SessionManager;
import com.netspective.medigy.util.HibernateUtil;
import com.netspective.medigy.reference.type.party.PartyRoleType;

public class TestOrganization  extends TestCase
{
    public void testOrg()
    {
        Session session = new ProcessSession();
        session.setProcessName(TestPerson.class.getName() + ".testOrg()");
        HibernateUtil.getSession().save(session);
        SessionManager.getInstance().setActiveSession(session);

        Organization org1 = new Organization();
        org1.setName("Acme Corporation");

        final Organization org2 = new Organization();
        org2.setName("Acme Subsidiary");

        HibernateUtil.beginTransaction();

        HibernateUtil.getSession().save(org1);
        HibernateUtil.getSession().save(org2);

        HibernateUtil.commitTransaction();
        //HibernateUtil.closeSession();

        final Organization persistedOrg1 = (Organization) HibernateUtil.getSession().load(Organization.class,
                org1.getOrgId());
        assertEquals(persistedOrg1.getName(), "Acme Corporation");
        assertEquals(persistedOrg1.getPartyName(), "Acme Corporation");

        final Organization persistedOrg2 = (Organization) HibernateUtil.getSession().load(Organization.class,
                org2.getOrgId());
        assertEquals(persistedOrg2.getName(), "Acme Subsidiary");
        assertEquals(persistedOrg2.getPartyName(), "Acme Subsidiary");

        HibernateUtil.beginTransaction();

        final PartyRole role1 = new PartyRole();
        role1.setParty(persistedOrg1);
        role1.setType(PartyRoleType.Cache.PARENT_ORGANIZATION.getEntity());
        persistedOrg1.getPartyRoles().add(role1);


        final PartyRole role2 = new PartyRole();
        role2.setParty(persistedOrg2);
        role2.setType(PartyRoleType.Cache.SUBSIDIARY.getEntity());
        persistedOrg2.getPartyRoles().add(role2);

        HibernateUtil.getSession().update(persistedOrg1);
        HibernateUtil.getSession().save(role2);

        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();

        // validate that the roles are assigned to the organizations
        final Organization updatedOrg1 = (Organization) HibernateUtil.getSession().load(Organization.class,
                persistedOrg1.getOrgId());
        assertEquals(1, updatedOrg1.getPartyRoles().size());
        assertEquals(PartyRoleType.Cache.PARENT_ORGANIZATION.getEntity(),
                ((PartyRole) updatedOrg1.getPartyRoles().toArray()[0]).getType());

        
        HibernateUtil.closeSession();
    }
}
