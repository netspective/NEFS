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

/**
 * $Id: ActivePerson.java,v 1.1 2004-02-27 01:48:14 shahid.shah Exp $
 */

package app.navigate.entity.person;

import java.sql.SQLException;

import javax.naming.NamingException;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.axiom.ConnectionContext;

import auto.dal.db.dao.PersonTable;
import auto.dal.db.dao.person.PersonClassificationTable;
import auto.dal.db.DataAccessLayer;
import app.navigate.entity.EntityRedirectorPage;

public class ActivePerson
{
    public static final String PARAMNAME_PERSON_ID = "person_id";

    private String personIdParamValue;
    private boolean valid;
    private PersonTable.Record person;
    private int activeOrgPrimaryPersonTypeId = EntityRedirectorPage.ID_UNKNOWN;
    private String activeOrgPrimaryPersonTypeName; // only used if the person type id is 'other'
    private int[] activeOrgAllPersonTypes = new int[0];

    public static String getPersonIdParamValue(NavigationContext nc)
    {
        return nc.getRequest().getParameter(PARAMNAME_PERSON_ID);
    }

    public ActivePerson(NavigationContext nc, ConnectionContext cc) throws NamingException, SQLException
    {
        personIdParamValue = getPersonIdParamValue(nc);

        PersonTable personTable = DataAccessLayer.getInstance().getPersonTable();
        person = personTable.getRecordByPrimaryKey(cc, personIdParamValue, false);

        if(person == null)
        {
            valid = false;
            return;
        }

        setPerson(nc, cc);
        valid = true;
    }

    public String getPersonIdParamValue()
    {
        return personIdParamValue;
    }

    public boolean isValid()
    {
        return valid;
    }

    public PersonTable.Record getPerson()
    {
        return person;
    }

    protected void setPerson(NavigationContext nc, ConnectionContext cc)  throws NamingException, SQLException
    {
        PersonClassificationTable.Records pClasses = person.getPersonClassificationTableRecords(cc);
        int count = pClasses.size();
        activeOrgAllPersonTypes = new int[count];

        if(count == 1)
        {
            PersonClassificationTable.Record pClass = pClasses.get(0);
            int type = pClass.getPersonTypeId().getIntValue();
            activeOrgPrimaryPersonTypeId = type;
            activeOrgAllPersonTypes[0] = type;
        }
        else
        {
            for(int i = 0; i < count; i++)
            {
                //TODO: be sure to only check the person type for the active user's current org
                PersonClassificationTable.Record pClass = pClasses.get(i);

                int type = pClass.getPersonTypeId().getIntValue();
                activeOrgAllPersonTypes[i] = type;

                switch(type)
                {
                    case auto.id.sql.schema.db.enum.PersonType.PATIENT:
                        activeOrgPrimaryPersonTypeId = auto.id.sql.schema.db.enum.PersonType.PATIENT;
                        break;

                    case auto.id.sql.schema.db.enum.PersonType.PHYSICIAN:
                    case auto.id.sql.schema.db.enum.PersonType.PHYSICIAN_EXTENDER__DIRECT_BILLING_:
                        activeOrgPrimaryPersonTypeId = auto.id.sql.schema.db.enum.PersonType.PHYSICIAN;
                        break;
                }
            }
        }
    }

    public int getActiveOrgPrimaryPersonTypeId()
    {
        return activeOrgPrimaryPersonTypeId;
    }

    public String getActiveOrgPrimaryPersonTypeName()
    {
        return activeOrgPrimaryPersonTypeName;
    }

    public int[] getActiveOrgAllPersonTypes()
    {
        return activeOrgAllPersonTypes;
    }

    public boolean isPatient()
    {
        return activeOrgPrimaryPersonTypeId == auto.id.sql.schema.db.enum.PersonType.PATIENT;
    }

    public boolean isPhysician()
    {
        return activeOrgPrimaryPersonTypeId == auto.id.sql.schema.db.enum.PersonType.PHYSICIAN;
    }
}
