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

import com.netspective.medigy.model.common.AbstractDateDurationEntity;
import com.netspective.medigy.reference.type.PartyRelationshipType;

import javax.ejb.CascadeType;
import javax.ejb.Column;
import javax.ejb.Entity;
import javax.ejb.GeneratorType;
import javax.ejb.Id;
import javax.ejb.JoinColumn;
import javax.ejb.ManyToOne;
import javax.ejb.OneToOne;
import javax.ejb.Table;

@Entity
@Table(name = "Party_Relationship")
public class PartyRelationship extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "party_rel_id";

    private Long partyRelationshipId;
    private String comment;
    private PartyRole partyRole;
    private PartyRelationshipType relationshipType;
    private PartyRelationshipStatus relationshipStatus;
    private Priority priority;

    public PartyRelationship()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PartyRelationship.PK_COLUMN_NAME)
    public Long getPartyRelationshipId()
    {
        return partyRelationshipId;
    }

    protected void setPartyRelationshipId(final Long partyRelationshipId)
    {
        this.partyRelationshipId = partyRelationshipId;
    }

    public PartyRelationshipType getRelationshipType()
    {
        return relationshipType;
    }

    protected void setRelationshipType(final PartyRelationshipType relationshipType)
    {
        this.relationshipType = relationshipType;
    }

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "party_role_id")
    public PartyRole getPartyRole()
    {
        return partyRole;
    }

    protected void setPartyRole(final PartyRole partyRole)
    {
        this.partyRole = partyRole;
    }

    @Column(length = 256)
    public String getComment()
    {
        return comment;
    }

    protected void setComment(final String comment)
    {
        this.comment = comment;
    }

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "party_rel_type_id")
    public PartyRelationshipType getType()
    {
        return relationshipType;
    }

    public void setType(final PartyRelationshipType type)
    {
        this.relationshipType = type;
    }

    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "party_rel_id")
    public PartyRelationshipStatus getRelationshipStatus()
    {
        return relationshipStatus;
    }

    protected void setRelationshipStatus(final PartyRelationshipStatus relationshipStatus)
    {
        this.relationshipStatus = relationshipStatus;
    }

    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "party_rel_id")
    public Priority getPriority()
    {
        return priority;
    }

    protected void setPriority(final Priority priority)
    {
        this.priority = priority;
    }
}