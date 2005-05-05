package com.netspective.medigy.model.party;

import java.util.List;

import org.hibernate.criterion.Expression;

import com.netspective.medigy.model.DbUnitTestCase;
import com.netspective.medigy.model.org.Organization;
import com.netspective.medigy.model.person.Person;
import com.netspective.medigy.reference.custom.party.OrganizationRoleType;
import com.netspective.medigy.reference.custom.party.PartyRelationshipType;
import com.netspective.medigy.util.HibernateUtil;

public final class TestPartyRelationship extends DbUnitTestCase
{
    
    public void testPartyRelationship()
    {
        // don't really need to do this since the XML insertion  is WORKING?
        List partyList = HibernateUtil.getSession().createCriteria(Party.class).list();
        assertEquals(3, partyList.size());
        List orgList = HibernateUtil.getSession().createCriteria(Organization.class).list();
        assertEquals(2, orgList.size());
        //List personList = HibernateUtil.getSession().createCriteria(Person.class).list();
        
        final Party parentOrg = (Party) HibernateUtil.getSession().load(Party.class, new Long(2));
        final Party childOrg = (Party) HibernateUtil.getSession().load(Party.class, new Long(3));
        assertNotNull(parentOrg);
        assertNotNull(childOrg);
        
        PartyRole employerRole = (PartyRole) HibernateUtil.getSession().load(PartyRole.class, new Long(1));
        PartyRole groupRole = (PartyRole) HibernateUtil.getSession().load(PartyRole.class, new Long(2));
        PartyRelationshipType orgRelationshipType = (PartyRelationshipType) HibernateUtil.getSession().load(PartyRelationshipType.class, new Long(1));
        assertNotNull(orgRelationshipType);
        
        final PartyRelationship rel = new PartyRelationship();
        rel.setPartyRoleFrom(groupRole);
        rel.setPartyRoleTo(employerRole);
        rel.setPartyFrom(childOrg);
        rel.setPartyTo(parentOrg);
        rel.setType(orgRelationshipType);
        
        groupRole.getFromPartyRelationships().add(rel);
        employerRole.getToPartyRelationships().add(rel);
        HibernateUtil.getSession().save(rel);
        HibernateUtil.closeSession();
        
        PartyRelationship newRelation = (PartyRelationship) HibernateUtil.getSession().load(PartyRelationship.class, rel.getPartyRelationshipId());
        assertNotNull(newRelation);
        assertEquals(childOrg.getPartyId(), newRelation.getPartyFrom().getPartyId());
        assertEquals(parentOrg.getPartyId(), newRelation.getPartyTo().getPartyId());
        assertEquals(employerRole.getPartyRoleId(), newRelation.getPartyRoleTo().getPartyRoleId());
        assertEquals(groupRole.getPartyRoleId(), newRelation.getPartyTo().getPartyId());
    }

    @Override
    public String getDataSetFile()
    {        
        return "/com/netspective/medigy/model/party/TestPartyRelationship.xml";
    }
}
