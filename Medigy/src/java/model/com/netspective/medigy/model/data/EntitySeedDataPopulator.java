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
package com.netspective.medigy.model.data;

import com.netspective.medigy.model.party.Party;
import com.netspective.medigy.reference.custom.party.PartyRoleType;
import com.netspective.medigy.util.HibernateUtil;
import org.hibernate.Session;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

public class EntitySeedDataPopulator
{

    public static void populateData()
    {
        Session session = HibernateUtil.getSession();

        HibernateUtil.beginTransaction();
        Party globalParty = new Party("SYS_GLOBAL_PARTY");
        session.save(globalParty);

        populatePartyRoleType(globalParty, session);
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }

    private static void populatePartyRoleType(Party globalParty, Session session)
    {
        populatePartyRoleTypes(session, PartyRoleType.class, new String[] {"code", "label", "description", "party"},
                new Object[][]
                {
                    {"P", "Prospect", "A sales prospect", globalParty},
                    {"DIV", "Division", "", globalParty},
                    {"OORG", "Other Organization Unit", "", globalParty},
                    {"DEPT", "Department", "", globalParty},
                    {"SORG", "Subsidiary", "", globalParty},
                    {"PORG", "Parent Organization", "", globalParty},
                    {"F", "Family Member", "", globalParty},
                    {"C", "Contractor", "", globalParty},
                    {"E", "Employee", "", globalParty}
                }
        );

    }

    public static void  populatePartyRoleTypes(Session session,
                                               Class entityClass,
                                               String[] propertyList,
                                               Object[][] data)
    {
        try
        {
            Hashtable pdsByName = new Hashtable();
            BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyDescriptor descriptor = descriptors[i];
                if (descriptor.getWriteMethod() != null)
                    pdsByName.put(descriptor.getName(), descriptor.getWriteMethod());
            }

            for (int i = 0; i < data.length; i++)
            {
                Object entityObject = entityClass.newInstance();
                for (int j = 0; j < propertyList.length; j++)
                {
                    Method setter = (Method) pdsByName.get(propertyList[j]);
                    if (setter != null)
                        setter.invoke(entityObject, new Object[] {data[i][j]});
                }
                session.save(entityObject);
            }
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }


    }
}
