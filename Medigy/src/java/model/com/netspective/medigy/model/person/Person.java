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

import com.netspective.medigy.model.party.Party;
import com.netspective.medigy.reference.type.MaritalStatusType;

import javax.ejb.CascadeType;
import javax.ejb.Column;
import javax.ejb.Entity;
import javax.ejb.Inheritance;
import javax.ejb.InheritanceJoinColumn;
import javax.ejb.InheritanceType;
import javax.ejb.JoinColumn;
import javax.ejb.OneToMany;
import javax.ejb.OneToOne;
import javax.ejb.Transient;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@InheritanceJoinColumn(name="partyId")
public class Person extends Party
{
    //private Long personId;

    private String firstName;
    private String lastName;
    private String middleName;
    private String suffix;
    private Date birthDate;
    private String ssn;

    private Gender gender = new Gender();

    private Set<ContactMechanism> contactMechanisms = new HashSet<ContactMechanism>();
    private Set<MaritalStatus> maritalStatuses = new HashSet<MaritalStatus>();

    public Person()
    {
    }

    @Transient
    public String getPartyName()
    {
        return getFullName();
    }

    @Transient
    public Long getPersonId()
    {
        return super.getPartyId();
    }

    protected void setPersonId(final Long personId)
    {
        super.setPartyId(personId);
    }

    @Column(length = 128, nullable = false)
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    @Column(length = 128, nullable = false)
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    @Column(length = 96)
    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(final String middleName)
    {
        this.middleName = middleName;
    }

    @Column(name="suffix", length=5)
    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    @Transient
    public String getFullName()
    {
        final StringBuffer sb = new StringBuffer();
        final String firstName = getFirstName();
        final String middleName = getMiddleName();
        final String lastName = getLastName();
        sb.append(firstName);
        if(middleName != null)
        {
            sb.append(' ');
            sb.append(middleName);
        }
        sb.append(' ');
        sb.append(lastName);
        return sb.toString();
    }

    @Transient
    public String getSortableName()
    {
        final StringBuffer sb = new StringBuffer();
        final String firstName = getFirstName();
        final String middleName = getMiddleName();
        final String lastName = getLastName();
        sb.append(lastName);
        sb.append(", ");
        sb.append(firstName);
        if(middleName != null)
        {
            sb.append(' ');
            sb.append(middleName.substring(0, 1));
        }
        return sb.toString();
    }

    @Column()
    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "partyId")
    public Gender getGender()
    {
        return gender;
    }

    protected void setGender(final Gender gender)
    {
        this.gender = gender;
    }
    @Column(name="ssn", length=9)
    public String getSsn()
    {
        return ssn;
    }

    public void setSsn(String ssn)
    {
        this.ssn = ssn;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "partyId")
    public Set<ContactMechanism> getContactMechanisms()
    {
        return contactMechanisms;
    }

    protected void setContactMechanisms(final Set<ContactMechanism> contactMechanisms)
    {
        this.contactMechanisms = contactMechanisms;
    }

    @Transient
    public MaritalStatusType getCurrentMaritalStatus()
    {
        final Set<MaritalStatus> maritalStatuses = getMaritalStatuses();
        if(maritalStatuses.size() == 0)
            return MaritalStatusType.Cache.UNKNOWN.getEntity();

        TreeSet<MaritalStatus> inverseSorted = new TreeSet<MaritalStatus>(Collections.reverseOrder());
        inverseSorted.addAll(maritalStatuses);
        return inverseSorted.first().getType();
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "partyId")
    public Set<MaritalStatus> getMaritalStatuses()
    {
        return maritalStatuses;
    }

    protected void setMaritalStatuses(final Set<MaritalStatus> maritalStatuses)
    {
        this.maritalStatuses = maritalStatuses;
    }


    public String toString()
    {
        return "person{" +
                "identifier=" + getPersonId() +
                ", lastName='" + lastName + "'" +
                ", firstName='" + firstName + "'" +
                ", middleName='" + middleName + "'" +
                ", suffix=" + suffix + "'" +
                ", ssn=" + ssn + "'" +
                ", gender='" + gender + "'" +
                ", maritalStatuses=" + maritalStatuses +
                ", contactMechanisms=" + contactMechanisms +
                "}";
    }
}