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

import com.netspective.medigy.dto.party.AddPostalAddressParameters;
import com.netspective.medigy.model.TestCase;
import com.netspective.medigy.model.party.Party;
import com.netspective.medigy.model.party.PartyIdentifier;
import com.netspective.medigy.model.party.PostalAddress;
import com.netspective.medigy.model.session.ProcessSession;
import com.netspective.medigy.model.session.Session;
import com.netspective.medigy.model.session.SessionManager;
import com.netspective.medigy.reference.custom.party.ContactMechanismPurposeType;
import com.netspective.medigy.reference.custom.person.EthnicityType;
import com.netspective.medigy.reference.custom.person.PersonIdentifierType;
import com.netspective.medigy.reference.type.GenderType;
import com.netspective.medigy.reference.type.LanguageType;
import com.netspective.medigy.reference.type.MaritalStatusType;
import com.netspective.medigy.service.ServiceLocator;
import com.netspective.medigy.service.party.AddContactMechanismService;
import com.netspective.medigy.service.person.PersonFacade;
import com.netspective.medigy.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.Date;

public class TestPerson extends TestCase
{
    private static final Log log = LogFactory.getLog(TestPerson.class);

    public void testPerson()
    {
        Session session = new ProcessSession();
        session.setProcessName(TestPerson.class.getName() + ".testPerson()");
        SessionManager.getInstance().pushActiveSession(session);
        HibernateUtil.getSession().save(session);

        final Calendar calendar = Calendar.getInstance();
        HibernateUtil.beginTransaction();

        Person newPerson = new Person();
        newPerson.setFirstName("Ryan");
        newPerson.setMiddleName("Bluegrass");
        newPerson.setLastName("Hackett");


        calendar.set(1990, 6, 14);
        newPerson.addMaritalStatus(MaritalStatusType.Cache.SINGLE.getEntity(), calendar.getTime(), new Date());
        newPerson.addMaritalStatus(MaritalStatusType.Cache.MARRIED.getEntity(), new Date(), null);
        newPerson.addGender(GenderType.Cache.MALE.getEntity());

        newPerson.setBirthDate(calendar.getTime());
        newPerson.addEthnicity(EthnicityType.Cache.CAUCASIAN.getEntity());
        newPerson.addEthnicity(EthnicityType.Cache.NATIVE_AMERICAN.getEntity());

        // Add languages
        newPerson.addLanguage(LanguageType.Cache.ENGLISH.getEntity(), true);
        newPerson.addLanguage(LanguageType.Cache.SPANISH.getEntity(), false);
        newPerson.setSsn("000-00-0000");

        HibernateUtil.getSession().save(newPerson);
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();

        final Person persistedPerson = (Person) HibernateUtil.getSession().load(Person.class, newPerson.getPersonId());
        assertEquals(persistedPerson.getFirstName(), "Ryan");
        assertEquals(persistedPerson.getMiddleName(), "Bluegrass");
        assertEquals(persistedPerson.getLastName(), "Hackett");
        assertEquals(persistedPerson.getPartyName(), "Ryan Bluegrass Hackett");

        // verify the ethnicites
        assertEquals(persistedPerson.getEthnicities().size(), 2);
        assertTrue(persistedPerson.hasEthnicity(EthnicityType.Cache.CAUCASIAN.getEntity()));
        assertTrue(persistedPerson.hasEthnicity(EthnicityType.Cache.NATIVE_AMERICAN.getEntity()));

        HibernateUtil.beginTransaction();
        final AddContactMechanismService addContactService = (AddContactMechanismService) ServiceLocator.getInstance().getService(AddContactMechanismService.class);
        addContactService.addPostalAddress(new AddPostalAddressParameters() {
            public Party getParty()
            {
                return persistedPerson;
            }

            public String getStreet1()
            {
                return "123 Acme Street";
            }

            public String getStreet2()
            {
                return null;
            }

            public String getCity()
            {
                return "Fairfax";
            }

            public String getState()
            {
                return "VA";
            }

            public String getPostalCode()
            {
                return "22033";
            }

            public String getCounty()
            {
                return "Fairfax County";
            }

            public String getCountry()
            {
                return "USA";
            }

            public String getPurpose()
            {
                return ContactMechanismPurposeType.Cache.HOME_ADDRESS.getEntity().getCode();
            }
        });

        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();

        final Person updatedPerson = (Person) HibernateUtil.getSession().load(Person.class, persistedPerson.getPersonId());
        assertNotNull(updatedPerson);
        assertEquals(2, updatedPerson.getMaritalStatuses().size());
        assertEquals(MaritalStatusType.Cache.MARRIED.getEntity(), updatedPerson.getCurrentMaritalStatus());
        assertEquals(GenderType.Cache.MALE.getEntity(), updatedPerson.getCurrentGender());
        assertEquals(calendar.getTime(), updatedPerson.getBirthDate());

        // verify the Identifiers
        assertEquals(updatedPerson.getPartyIdentifiers().size(), 1);
        assertEquals(((PartyIdentifier) updatedPerson.getPartyIdentifiers().toArray()[0]).getType(),
                PersonIdentifierType.Cache.SSN.getEntity());

        // verify the contact mechanisms
        assertEquals(updatedPerson.getPartyContactMechanisms().size(), 1);
        final PersonFacade pFacade = (PersonFacade) ServiceLocator.getInstance().getService(PersonFacade.class);
        final PostalAddress homeAddress = pFacade.getHomeAddress(updatedPerson);
        assertEquals(homeAddress.getAddress1(), "123 Acme Street");
        assertEquals(homeAddress.getCity().getName(), "Fairfax");
        assertEquals(homeAddress.getState().getName(), "VA");


        HibernateUtil.closeSession();
        SessionManager.getInstance().popActiveSession();
    }
}
