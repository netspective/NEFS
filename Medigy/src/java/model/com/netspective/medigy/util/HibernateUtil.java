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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.NestableRuntimeException;

import com.netspective.medigy.reference.CachedReferenceEntity;
import com.netspective.medigy.reference.ReferenceEntity;
import com.netspective.medigy.reference.CachedReferenceEntity;
import com.netspective.medigy.reference.ReferenceEntity;

public class HibernateUtil
{
    private static final Log log = LogFactory.getLog(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
    private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

    public static void setConfiguration(Configuration cfg) throws HibernateException
    {
        sessionFactory = cfg.buildSessionFactory();
    }

    public static Session getSession()
    {
        Session session = threadSession.get();
        if(session == null)
        {
            try
            {
                session = sessionFactory.openSession();
                threadSession.set(session);
            }
            catch (HibernateException e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }

        return session;
    }

    public static void closeSession()
    {
        Session session = threadSession.get();
        if(session != null)
        {
            threadSession.set(null);
            try
            {
                if(session.isOpen())
                    session.close();
            }
            catch (HibernateException e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }
    }

    public static void beginTransaction()
    {
        Transaction tx = threadTransaction.get();
        if(tx == null)
        {
            try
            {
                tx = getSession().beginTransaction();
                threadTransaction.set(tx);
            }
            catch (HibernateException e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }
    }

    public static void commitTransaction()
    {
        Transaction tx = threadTransaction.get();
        try
        {
            threadTransaction.set(null);
            if(tx != null && !tx.wasCommitted() && ! tx.wasRolledBack())
                tx.commit();
        }
        catch (HibernateException e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }

    public static void rollbackTransaction()
    {
        Transaction tx = threadTransaction.get();
        try
        {
            threadTransaction.set(null);
            if(tx != null && !tx.wasCommitted() && ! tx.wasRolledBack())
                tx.rollback();
        }
        catch (HibernateException e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }

    public static void initReferenceEntityCache(final Class aClass, final CachedReferenceEntity[] cache)
    {
        final List list = getSession().createCriteria(aClass).list();

        for(final Object i : list)
        {
            final ReferenceEntity entity = (ReferenceEntity) i;
            final Object id = entity.getTypeId();
            if(id == null)
            {
                log.warn(entity + " id is NULL: unable to map to one of " + cache);
                continue;
            }

            for(final CachedReferenceEntity c : cache)
            {
                if(id.equals(c.getId()))
                {
                    final ReferenceEntity record = c.getEntity();
                    if(record != null)
                        log.warn(c.getClass().getName() + " enum '" + c + "' is bound to multiple rows.");
                    else
                        c.setEntity(entity);
                    break;
                }
            }
        }

        for(final CachedReferenceEntity c : cache)
        {
            if(c.getEntity() == null)
                log.warn(c.getClass().getName() + " enum '" + c + "' was not bound to a database row.");
        }
    }

    public static void initReferenceEntityCaches(final Map<Class, Class> referenceEntitiesAndCachesMap)
    {
        for(final Map.Entry<Class, Class> entry : referenceEntitiesAndCachesMap.entrySet())
            initReferenceEntityCache(entry.getKey(), (CachedReferenceEntity[]) entry.getValue().getEnumConstants());
    }
}
