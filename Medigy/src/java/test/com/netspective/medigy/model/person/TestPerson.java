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

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.netspective.medigy.model.person;

import com.netspective.medigy.model.TestCase;
import com.netspective.medigy.model.session.ProcessSession;
import com.netspective.medigy.model.session.Session;
import com.netspective.medigy.model.session.SessionManager;
import com.netspective.medigy.reference.type.MaritalStatusType;
import com.netspective.medigy.util.HibernateUtil;

import java.util.Calendar;

public class TestPerson extends TestCase
{
    public void testPerson()
    {
        final Calendar calendar = Calendar.getInstance();
        HibernateUtil.beginTransaction();

        Session session = new ProcessSession();
        session.setProcessName(TestPerson.class.getName() + ".testPerson()");
        HibernateUtil.getSession().save(session);
        SessionManager.getInstance().setActiveSession(session);

        Person newPerson = new Person();
        newPerson.setFirstName("Ryan");
        newPerson.setMiddleName("Bluegrass");
        newPerson.setLastName("Hackett");

        final MaritalStatus singleStatus = new MaritalStatus();
        singleStatus.setPerson(newPerson);
        singleStatus.setType(MaritalStatusType.Cache.SINGLE.getEntity());
        calendar.set(1990, 6, 14);
        singleStatus.setThroughDate(calendar.getTime());

        final MaritalStatus marriedStatus = new MaritalStatus();
        marriedStatus.setPerson(newPerson);
        marriedStatus.setType(MaritalStatusType.Cache.MARRIED.getEntity());
        marriedStatus.setFromDate(calendar.getTime());

        newPerson.getMaritalStatuses().add(singleStatus);
        newPerson.getMaritalStatuses().add(marriedStatus);

        HibernateUtil.getSession().save(newPerson);
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();

        final Person persistedPerson = (Person) HibernateUtil.getSession().load(Person.class, newPerson.getPersonId());
        assertEquals(persistedPerson.getFirstName(), "Ryan");
        assertEquals(persistedPerson.getMiddleName(), "Bluegrass");
        assertEquals(persistedPerson.getLastName(), "Hackett");

        HibernateUtil.beginTransaction();

        ContactMechanism contactMechanism = new ContactMechanism();
        contactMechanism.setPerson(persistedPerson);

        contactMechanism.setPerson(persistedPerson);
        persistedPerson.getContactMechanisms().add(contactMechanism);

        HibernateUtil.getSession().save(contactMechanism);
        HibernateUtil.getSession().save(persistedPerson);
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();

        final Person updatedPerson = (Person) HibernateUtil.getSession().load(Person.class, persistedPerson.getPersonId());
        assertNotNull(updatedPerson);
        assertEquals(1, updatedPerson.getContactMechanisms().size());
        assertEquals(2, updatedPerson.getMaritalStatuses().size());
        assertEquals(MaritalStatusType.Cache.MARRIED.getEntity(), updatedPerson.getCurrentMaritalStatus());

        HibernateUtil.closeSession();
    }
}
