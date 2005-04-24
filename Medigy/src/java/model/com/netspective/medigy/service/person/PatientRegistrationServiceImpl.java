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
 */
package com.netspective.medigy.service.person;

import com.netspective.medigy.dto.person.RegisterPatientParameters;
import com.netspective.medigy.dto.person.RegisteredPatient;
import com.netspective.medigy.model.party.PartyRole;
import com.netspective.medigy.model.person.Ethnicity;
import com.netspective.medigy.model.person.Gender;
import com.netspective.medigy.model.person.Language;
import com.netspective.medigy.model.person.MaritalStatus;
import com.netspective.medigy.model.person.Person;
import com.netspective.medigy.reference.custom.person.PersonRoleType;
import com.netspective.medigy.reference.custom.person.PatientResponsiblePartyRoleType;
import com.netspective.medigy.reference.custom.party.PartyRelationshipType;
import com.netspective.medigy.service.ServiceLocator;
import com.netspective.medigy.service.common.ReferenceEntityLookupService;
import com.netspective.medigy.service.party.PartyRelationshipFacade;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class PatientRegistrationServiceImpl implements PatientRegistrationService
{
    private static final Log log = LogFactory.getLog(PatientRegistrationServiceImpl.class);

    protected void registerResponsibleParty(final Person person, final RegisterPatientParameters patientParameters)
    {
        // TODO: Need to add  dynamic service lookup soon!
        final ReferenceEntityLookupService referenceEntityService = ServiceLocator.getInstance().getReferenceEntityLookupService();
        final PersonFacade personFacade = ServiceLocator.getInstance().getPersonFacade();
        final PartyRelationshipFacade partyRelFacade = ServiceLocator.getInstance().getPartyRelationshipFacade();

        // Create a patient role for the new patient
        final PartyRole patientRole = personFacade.addPersonRole(person, PersonRoleType.Cache.PATIENT.getEntity());

        String respLastName = patientParameters.getResponsiblePartyLastName();
        if (respLastName != null)
        {
            Person respParty = null;
            // if the responsible party ID is available then the person already exists
            if (patientParameters.getResponsiblePartyId() != null)
            {
                respParty = personFacade.getPersonById(patientParameters.getResponsiblePartyId());
            }
            else
            {
                respParty = new Person();
                respParty.setLastName(patientParameters.getResponsiblePartyLastName());
                respParty.setFirstName(patientParameters.getResponsiblePartyFirstName());
                personFacade.addPerson(respParty);
            }
            // responsible party processing
            final PatientResponsiblePartyRoleType entity = PatientResponsiblePartyRoleType.Cache.getEntity(patientParameters.getResponsiblePartyRole());
            final PartyRole respPartyRole = personFacade.addPersonRole(respParty, entity);

            // create a relationship between the patient and this person through the roles
            partyRelFacade.addPartyRelationship(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity(), patientRole, respPartyRole);
        }

    }

    public RegisteredPatient registerPatient(final RegisterPatientParameters patientParameters)
    {
        final ReferenceEntityLookupService referenceEntityService = ServiceLocator.getInstance().getReferenceEntityLookupService();
        final PersonFacade personFacade = ServiceLocator.getInstance().getPersonFacade();
        final PartyRelationshipFacade partyRelFacade = ServiceLocator.getInstance().getPartyRelationshipFacade();

        Person person = new Person();
        try
        {
            BeanUtils.copyProperties(person, patientParameters);

            assert  patientParameters.getGender() != null;  // REQUIREMENT
            Gender gender = referenceEntityService.getGender(patientParameters.getGender());
            gender.setPerson(person);
            MaritalStatus maritalStatus = referenceEntityService.getMaritalStatus(patientParameters.getMaritalStatus());
            maritalStatus.setPerson(person);
            person.getGenders().add(gender);
            person.getMaritalStatuses().add(maritalStatus);

            // add the languages
            final String[] languages = patientParameters.getLanguageCodes();
            assert languages != null && languages.length > 0 : languages;   // REQUIREMENT
            if (languages != null && languages.length > 0)
            {
                for (int i = 0; i < languages.length; i++)
                {
                    Language lang = referenceEntityService.getLanguage(languages[i]);
                    person.addLanguage(lang);
                }
            }

            // add the ethnicities
            final String[] ethnicities = patientParameters.getEthnicityCodes();
            assert ethnicities != null && ethnicities.length > 0 : ethnicities; // REQUIREMENT
            for (int i = 0; i < ethnicities.length; i++)
            {
                Ethnicity ethnicity = referenceEntityService.getEthnicity(ethnicities[i]);
                person.addEthnicity(ethnicity);
            }

            person.setSsn(patientParameters.getSsn());
            person.setDriversLicenseNumber(patientParameters.getDriversLicenseNumber());
            // Finally add the person
            personFacade.addPerson(person);

            registerResponsibleParty(person, patientParameters);

            final Long patientId = (Long) person.getPersonId();
            final RegisteredPatient patient = new RegisteredPatient() {
                public Serializable getPatientId()
                {
                    return patientId;
                }

                public RegisterPatientParameters getRegisterPatientParameters()
                {
                    return patientParameters;
                }
            };
            if (log.isInfoEnabled())
                log.info("New PERSON created with id = " + patient.getPatientId());
           return patient;
        }
        catch (Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    // TODO: Put a validator and return a list of errors/warnings
    public boolean isValid(RegisterPatientParameters person)
    {
        return false;
    }
}
