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
package com.netspective.medigy.service.party;

import com.netspective.medigy.dto.party.AddPostalAddressParameters;
import com.netspective.medigy.model.common.GeographicBoundary;
import com.netspective.medigy.model.party.PartyContactMechanism;
import com.netspective.medigy.model.party.PostalAddress;
import com.netspective.medigy.reference.custom.GeographicBoundaryType;
import com.netspective.medigy.reference.custom.party.ContactMechanismPurposeType;
import com.netspective.medigy.service.ServiceInvocationException;
import com.netspective.medigy.service.ServiceLocator;
import com.netspective.medigy.service.common.ReferenceEntityLookupService;
import com.netspective.medigy.service.common.UnknownReferenceTypeException;
import com.netspective.medigy.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class AddContactMechanismServiceImpl implements AddContactMechanismService
{
    private static final Log log = LogFactory.getLog(AddContactMechanismServiceImpl.class);

    protected GeographicBoundary getGeographicBoundary(final GeographicBoundaryType type, final String boundaryName)
    {
        if (boundaryName == null || boundaryName.length() == 0)
            return null;
        Criteria criteria =
                HibernateUtil.getSession().createCriteria(GeographicBoundary.class).add(Restrictions.eq("name", boundaryName));
        criteria.createAlias("type", "type").add(Restrictions.eq("type.code", type.getCode()));
        GeographicBoundary geo = (GeographicBoundary) criteria.uniqueResult();

        if (geo == null)
        {
            // this is a new geo boundary then
            geo = new GeographicBoundary();
            geo.setName(boundaryName);
            geo.setType(type);
        }
        return geo;
    }


    public void addPostalAddress(final AddPostalAddressParameters param)  throws ServiceInvocationException
    {
        final PostalAddress address = new PostalAddress();
        address.setAddress1(param.getStreet1());
        address.setAddress2(param.getStreet2());

        // create the common geo boundaries
        final GeographicBoundary cityGeo = getGeographicBoundary(GeographicBoundaryType.Cache.CITY.getEntity(), param.getCity());
        address.setCity(cityGeo);
        final GeographicBoundary stateGeo = getGeographicBoundary(GeographicBoundaryType.Cache.STATE.getEntity(), param.getState());
        address.setState(stateGeo);
        final GeographicBoundary postalCodeGeo = getGeographicBoundary(GeographicBoundaryType.Cache.POSTAL_CODE.getEntity(), param.getPostalCode());
        address.setPostalCode(postalCodeGeo);
        final GeographicBoundary countryGeo = getGeographicBoundary(GeographicBoundaryType.Cache.COUNTRY.getEntity(), param.getCountry());
        address.setCountry(countryGeo);

        HibernateUtil.getSession().save(address);

        final ReferenceEntityLookupService referenceEntityService = (ReferenceEntityLookupService) ServiceLocator.getInstance().getService(ReferenceEntityLookupService.class);
        // now create the relationship entry between party and the postal address
        final PartyContactMechanism mech = new PartyContactMechanism();
        mech.setParty(param.getParty());
        final ContactMechanismPurposeType contactMechanismPurposeType;
        try
        {
            contactMechanismPurposeType = referenceEntityService.getContactMechanismPurposeType(param.getPurpose());
            mech.addPurpose(contactMechanismPurposeType);
            mech.setContactMechanism(address);
            HibernateUtil.getSession().save(mech);
        }
        catch (UnknownReferenceTypeException e)
        {
            log.error(e);
            throw new ServiceInvocationException(e);
        }

    }
}
