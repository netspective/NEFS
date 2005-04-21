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
package com.netspective.medigy.service.person.hibernate;

import com.netspective.medigy.dto.Person;
import com.netspective.medigy.service.common.ReferenceEntityLookupService;
import com.netspective.medigy.service.person.PersonFacade;
import com.netspective.medigy.util.HibernateUtil;
import com.netspective.medigy.model.person.Gender;
import com.netspective.medigy.model.person.MaritalStatus;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.io.Serializable;
import java.util.List;

public class PersonFacadeImpl implements PersonFacade
{
    private static final Log log = LogFactory.getLog(PersonFacadeImpl.class);

    private ReferenceEntityLookupService referenceEntityService;

    public PersonFacadeImpl()
    {
    }

    public ReferenceEntityLookupService getReferenceEntityService()
    {
        return referenceEntityService;
    }

    public void setReferenceEntityService(ReferenceEntityLookupService referenceEntityService)
    {
        this.referenceEntityService = referenceEntityService;
    }

    public Person[] listPersonByLastName(final String lastName, boolean exactMatch)
    {
        Criteria criteria = HibernateUtil.getSession().createCriteria(Person.class);
        if (!exactMatch)
            criteria.add(Expression.like("lastName", lastName));
        else
            criteria.add(Expression.eq("lastName", lastName));
        List list = criteria.list();
        return list != null ? (Person[]) list.toArray(new Person[0]) : null;
    }

    public Person getPersonById(final Serializable id)
    {
        return (Person) HibernateUtil.getSession().load(Person.class, id);
    }

    public void addPerson(Person person)
    {
        com.netspective.medigy.model.person.Person modelPerson = new com.netspective.medigy.model.person.Person();

        try
        {
            BeanUtils.copyProperties(person, modelPerson);

            Gender gender = referenceEntityService.getGender(person.getGender());
            gender.setPerson(modelPerson);
            MaritalStatus maritalStatus = referenceEntityService.getMaritalStatus(person.getMaritalStatus());
            maritalStatus.setPerson(modelPerson);
            
            modelPerson.getGenders().add(gender);
            modelPerson.getMaritalStatuses().add(maritalStatus);
            Serializable id = HibernateUtil.getSession().save(modelPerson);
            if (log.isInfoEnabled())
                log.info("New PERSON created with id = " + id);

        }
        catch (Exception e)
        {
            log.error(e);
        }
    }
}
