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
package com.netspective.medigy.reference.type.party;

import com.netspective.medigy.reference.AbstractReferenceEntity;
import com.netspective.medigy.reference.CachedReferenceEntity;
import com.netspective.medigy.reference.ReferenceEntity;

import javax.ejb.Entity;
import javax.ejb.Table;

@Entity
@Table(name = "Party_Role_Type")
public class PartyRoleType extends AbstractReferenceEntity
{
    public enum Cache implements CachedReferenceEntity
    {
        // TODO: Party Roles need to be subtyped to person and org???
        PROSPECT("P", "Prospect", ""),
        SHAREHOLDER("SH", "Shareholder", ""),

        // PERSON ROLES
        EMPLOYEE("EMP", "Employee", ""),
        CONTRACTOR("CONT", "Contractor", ""),
        FAMILY_MEMBER("FM", "Family Member", ""),
        CONTACT("C", "Contact", ""),

        // ORGANIZATION ROLES
        PARENT_ORGANIZATION("P_ORG", "Parent Organization", ""),
        DEPARTMENT("DEPT", "Department", ""),
        SUBSIDIARY("SUB", "Subsidiary", ""),
        DIVISION("DIV", "Division", ""),
        OTHER_ORGANIZATION_UNIT("OTHER_ORG", "Other Organization Unit", ""),
        INTERNAL_ORGANIZATION("INT_ORG", "Internal Organization", ""),
        PARTNER("PART", "Partner", ""),
        HOUSEHOLD("HOUSE", "Household", ""),
        SUPPLIER("SUPP", "Supplier", ""),
        ASSOCIATION("ASSC", "Association", ""),
        COMPETITOR("COMP", "Competitor", ""),
        REGULATORY_AGENCY("REG_AGN", "Regulatory Agency", ""),
        AGENT("AGENT", "Agent", ""),
        DISTRIBUTOR("DIST", "Distributor", ""),

        // CUSTOMER ROLES
        BILL_TO_CUSTOMER("BILL_TO", "Bill To Customer", ""),
        SHIP_TO_CUSTOMER("SHIP_TO", "Ship To Customer", ""),
        END_USER_CUSTOMER("END_USER", "End User Customer", "");

        private final String code;
        private final String label;
        private final String description;
        private PartyRoleType entity;

        private Cache(final String code, final String label, final String description)
        {
            this.code = code;
            this.label = label;
            this.description = description;
        }

        public String getId()
        {
            return code;
        }

        public String getLabel()
        {
            return label;
        }

        public String getDescription()
        {
            return description;
        }

        public PartyRoleType getEntity()
        {
            return entity;
        }

        public void setEntity(final ReferenceEntity entity)
        {
            this.entity = (PartyRoleType) entity;
        }
    }
}
