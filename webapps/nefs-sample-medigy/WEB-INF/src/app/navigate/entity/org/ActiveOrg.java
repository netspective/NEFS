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

/**
 * $Id: ActiveOrg.java,v 1.1 2004-05-17 03:17:23 aye.thu Exp $
 */

package app.navigate.entity.org;

import auto.dal.db.DataAccessLayer;
import auto.dal.db.dao.OrgTable;
import auto.dal.db.dao.org.OrgClassificationTable;
import com.netspective.axiom.ConnectionContext;
import com.netspective.sparx.navigate.NavigationContext;

import javax.naming.NamingException;
import java.sql.SQLException;

public class ActiveOrg
{
    public static final String PARAMNAME_ORG_ID = "org_id";

    private String orgIdParamValue;
    private OrgTable.Record org;
    private int primaryOrgTypeId;
    private String primaryOrgTypeName;
    private boolean valid;
    private int[] allOrgTypes = new int[0];

    public static String getOrgIdParamValue(NavigationContext nc)
    {
        return nc.getRequest().getParameter(PARAMNAME_ORG_ID);
    }

    public String getOrgIdParamValue()
    {
        return orgIdParamValue;
    }


    public OrgTable.Record getOrg()
    {
        return org;
    }

    public boolean isValid()
    {
        return valid;
    }

    public ActiveOrg(NavigationContext nc, ConnectionContext cc) throws NamingException, SQLException
    {
        orgIdParamValue = getOrgIdParamValue(nc);

        OrgTable orgTable = DataAccessLayer.getInstance().getOrgTable();
        org = orgTable.getRecordByPrimaryKey(cc, orgIdParamValue, false);

        if(org == null)
        {
            valid = false;
            return;
        }

        setOrg(nc, cc);
        valid = true;
    }

    protected void setOrg(NavigationContext nc, ConnectionContext cc)  throws NamingException, SQLException
    {
        OrgClassificationTable.Records orgClasses = org.getOrgClassificationTableRecords(cc);
        int count = orgClasses.size();
        allOrgTypes = new int[count];
        if(count == 1)
        {
            OrgClassificationTable.Record oClass = orgClasses.get(0);
            int type = oClass.getOrgTypeId().getIntValue();
            primaryOrgTypeId = type;
            allOrgTypes[0] = type;
        }
        else
        {
            for(int i = 0; i < count; i++)
            {
                OrgClassificationTable.Record oClass = orgClasses.get(i);
                int type = oClass.getOrgTypeId().getIntValue();
                allOrgTypes[i] = type;

                switch(type)
                {
                    case auto.id.sql.schema.db.enum.OrgType.INSURANCE:
                        primaryOrgTypeId = auto.id.sql.schema.db.enum.OrgType.INSURANCE;
                        break;

                    case auto.id.sql.schema.db.enum.OrgType.CLINIC:
                        primaryOrgTypeId = auto.id.sql.schema.db.enum.OrgType.CLINIC;
                        break;

                    case auto.id.sql.schema.db.enum.OrgType.HOSPITAL:
                        primaryOrgTypeId = auto.id.sql.schema.db.enum.OrgType.HOSPITAL;
                        break;

                    case auto.id.sql.schema.db.enum.OrgType.PHARMACY:
                        primaryOrgTypeId = auto.id.sql.schema.db.enum.OrgType.PHARMACY;
                        break;
                }
            }
        }

    }

    public int getPrimaryOrgTypeId()
    {
        return primaryOrgTypeId;
    }

    public String getPrimaryOrgTypeName()
    {
        return primaryOrgTypeName;
    }

    public boolean isInsurance()
    {
        return primaryOrgTypeId == auto.id.sql.schema.db.enum.OrgType.INSURANCE;
    }

    public boolean isHospital()
    {
        return primaryOrgTypeId == auto.id.sql.schema.db.enum.OrgType.HOSPITAL;
    }

    public boolean isClinic()
    {
        return primaryOrgTypeId == auto.id.sql.schema.db.enum.OrgType.CLINIC;
    }

    public boolean isPharmacy()
    {
        return primaryOrgTypeId == auto.id.sql.schema.db.enum.OrgType.PHARMACY;
    }
}