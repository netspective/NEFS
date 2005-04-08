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
package com.netspective.medigy.model.party;

import com.netspective.medigy.model.common.GeographicBoundary;
import com.netspective.medigy.reference.custom.GeographicBoundaryType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceJoinColumn;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.AssociationTable;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@InheritanceJoinColumn(name="party_contact_mech_id")
public class PostalAddress extends PartyContactMechanism
{
    private String address1;
    private String address2;
    private String directions;

    private Set<GeographicBoundary> geographicBoundaries = new HashSet<GeographicBoundary>();

    @Column(length = 100, nullable = false)
    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(final String address1)
    {
        this.address1 = address1;
    }

    @Column(length = 100)
    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(final String address2)
    {
        this.address2 = address2;
    }

    @Column(length = 1000)
    public String getDirections()
    {
        return directions;
    }

    public void setDirections(final String directions)
    {
        this.directions = directions;
    }

    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    @AssociationTable(
        table=@Table(name="Postal_Address_Boundary"),
        joinColumns={@JoinColumn(name="party_contact_mech_id")},
        inverseJoinColumns={@JoinColumn(name="geo_id")}
    )
    public Set<GeographicBoundary> getGeographicBoundaries()
    {
        return geographicBoundaries;
    }

    public void setGeographicBoundaries(final Set<GeographicBoundary> geographicBoundaries)
    {
        this.geographicBoundaries = geographicBoundaries;
    }

    @Transient
    public String getCity()
    {
        GeographicBoundary[] boundaries = (GeographicBoundary[]) geographicBoundaries.toArray();
        for (int i = 0; i < boundaries.length; i++)
        {
            GeographicBoundary boundary = boundaries[i];
            if (boundary.getType().equals(GeographicBoundaryType.Cache.CITY))
            {
                return boundary.getName();
            }
        }
        return null;
    }

    @Transient
    public String getState()
    {
        GeographicBoundary[] boundaries = (GeographicBoundary[]) geographicBoundaries.toArray();
        for (int i = 0; i < boundaries.length; i++)
        {
            GeographicBoundary boundary = boundaries[i];
            if (boundary.getType().equals(GeographicBoundaryType.Cache.STATE))
            {
                return boundary.getName();
            }
        }
        return null;
    }

    @Transient
    public String getPostalCode()
    {
        GeographicBoundary[] boundaries = (GeographicBoundary[]) geographicBoundaries.toArray();
        for (int i = 0; i < boundaries.length; i++)
        {
            GeographicBoundary boundary = boundaries[i];
            if (boundary.getType().equals(GeographicBoundaryType.Cache.POSTAL_CODE))
            {
                return boundary.getName();
            }
        }
        return null;
    }

    @Transient
    public String getCounty()
    {
        GeographicBoundary[] boundaries = (GeographicBoundary[]) geographicBoundaries.toArray();
        for (int i = 0; i < boundaries.length; i++)
        {
            GeographicBoundary boundary = boundaries[i];
            if (boundary.getType().equals(GeographicBoundaryType.Cache.COUNTY))
            {
                return boundary.getName();
            }
        }
        return null;
    }

    @Transient
    public String getProvince()
    {
        GeographicBoundary[] boundaries = (GeographicBoundary[]) geographicBoundaries.toArray();
        for (int i = 0; i < boundaries.length; i++)
        {
            GeographicBoundary boundary = boundaries[i];
            if (boundary.getType().equals(GeographicBoundaryType.Cache.PROVINCE))
            {
                return boundary.getName();
            }
        }
        return null;
    }

    @Transient
    public String getCountry()
    {
        GeographicBoundary[] boundaries = (GeographicBoundary[]) geographicBoundaries.toArray();
        for (int i = 0; i < boundaries.length; i++)
        {
            GeographicBoundary boundary = boundaries[i];
            if (boundary.getType().equals(GeographicBoundaryType.Cache.COUNTRY))
            {
                return boundary.getName();
            }
        }
        return null;
    }
}
