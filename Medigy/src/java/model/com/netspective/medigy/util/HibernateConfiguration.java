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
package com.netspective.medigy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Table;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;

import com.netspective.medigy.model.common.EntitySeedData;
import com.netspective.medigy.model.common.EntitySeedDataProvider;
import com.netspective.medigy.reference.CachedReferenceEntity;
import com.netspective.medigy.reference.ReferenceEntity;

public class HibernateConfiguration extends AnnotationConfiguration
{
    private final Map<Class, Class> referenceEntitiesAndCachesMap = new HashMap<Class, Class>();
    private final Set<Class> entitiesWithSeedDataSet = new HashSet<Class>();

    public HibernateConfiguration()
    {
        setNamingStrategy(HibernateNamingStrategy.getInstance());
    }

    public Map<Class, Class> getReferenceEntitiesAndCachesMap()
    {
        return referenceEntitiesAndCachesMap;
    }

    public AnnotationConfiguration addAnnotatedClass(final Class aClass) throws MappingException
    {
        if (ReferenceEntity.class.isAssignableFrom(aClass))
        {
            boolean foundCache = false;
            for (final Class ic : aClass.getClasses())
            {
                if (CachedReferenceEntity.class.isAssignableFrom(ic))
                {
                    if (ic.isEnum())
                    {
                        referenceEntitiesAndCachesMap.put(aClass, ic);
                        foundCache = true;
                    }
                    else
                        throw new HibernateException(ic + " must be an enum since " + aClass + " is a " + ReferenceEntity.class.getName());

                    break;
                }
            }

            if (!foundCache)
                throw new HibernateException(aClass + " is marked as a ReferenceEntity but does not contain a ReferenceEntityCache enum.");

            // TODO: find out how to ensure the new mapping for reference type is immutable and read only
            // final PersistentClass pClass = getClassMapping(aClass.getName());
        }

        if (EntitySeedDataProvider.class.isAssignableFrom(aClass))
            entitiesWithSeedDataSet.add(aClass);

        return super.addAnnotatedClass(aClass);
    }

    public String[] generateSchemaCreationScript(final Dialect dialect) throws HibernateException
    {
        final String[] existingDDL = super.generateSchemaCreationScript(dialect);
        if (dialect instanceof HSQLDialect)
        {
            for (int i = 0; i < existingDDL.length; i++)
                existingDDL[i] = existingDDL[i].replaceFirst("create table ", "create cached table ");
        }

        if (referenceEntitiesAndCachesMap.size() == 0)
            return existingDDL;

        final List<String> newDDL = new ArrayList<String>();
        for (String s : existingDDL)
            newDDL.add(s);

        for (final Map.Entry<Class, Class> entry : referenceEntitiesAndCachesMap.entrySet())
        {
            final Class refEntityClass = entry.getKey();
            final Class refEntityCacheEnum = entry.getValue();
            final String tableName = ((Table) refEntityClass.getAnnotation(Table.class)).name();

            for (final Object x : refEntityCacheEnum.getEnumConstants())
            {
                final CachedReferenceEntity cached = (CachedReferenceEntity) x;
                //TODO: this is kind of dumb right now, we need to do proper formatting of output, etc.
                newDDL.add("insert into " + tableName + " (type_id, type_label) values ('" + cached.getId() + "', '" + cached.getLabel() + "')");
            }
        }
        for (final Class seedDataEntityClass : entitiesWithSeedDataSet)
        {
            final Table tableAnn = (Table) seedDataEntityClass.getAnnotation(Table.class);
            if (tableAnn == null)
                throw new HibernateException(seedDataEntityClass + " does not have a Table annotation.");

            final String tableName = tableAnn.name();
            if (tableName == null)
                throw new HibernateException(seedDataEntityClass + " does not have a name property set in the Table annotation.");

            try
            {
                final EntitySeedDataProvider esdp = (EntitySeedDataProvider) seedDataEntityClass.newInstance();
                final EntitySeedData esd = esdp.getEntitySeedData();
                if (esd != null)
                {
                    final String[] columnNames = esd.getColumnNames();
                    if (columnNames == null)
                        throw new HibernateException("EntitySeedData column names are NULL for " + tableName + " " + seedDataEntityClass);

                    final Object[][] data = esd.getSeedData();
                    if (data == null || data.length == 0)
                        throw new HibernateException("EntitySeedData data is NULL for " + tableName + " " + seedDataEntityClass);

                    for (int row = 0; row < data.length; row++)
                    {
                        final Object[] rowData = data[row];
                        if (columnNames.length != rowData.length)
                            throw new HibernateException("Column names length does not match data columns length for row " + row + " of " + tableName + " " + seedDataEntityClass);

                        final StringBuffer rowSql = new StringBuffer("insert into " + tableName + " (");
                        for (int i = 0; i < columnNames.length; i++)
                        {
                            if (i > 0) rowSql.append(", ");
                            rowSql.append(columnNames[i]);
                        }

                        rowSql.append(") values (");
                        for (int column = 0; column < rowData.length; column++)
                        {
                            if (column != 0)
                                rowSql.append(", ");
                            final Object element = rowData[column];
                            if (element == null)
                                rowSql.append("NULL");
                            else if (element instanceof Number)
                                rowSql.append(element.toString());
                            else
                                rowSql.append("'" + element.toString().replaceAll("'", "''") + "'");
                        }
                        rowSql.append(")");
                        newDDL.add(rowSql.toString());
                    }
                }
                else
                    throw new HibernateException("EntitySeedData is NULL for " + tableName + " " + seedDataEntityClass);
            }
            catch (Exception e)
            {
                throw new HibernateException(e);
            }
        }

        return newDDL.toArray(new String[newDDL.size()]);
    }
}
