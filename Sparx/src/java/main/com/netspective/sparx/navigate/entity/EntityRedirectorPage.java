/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.sparx.navigate.entity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.schema.table.type.EnumerationTableRows;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.sql.ResultSetUtils;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.Project;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationPath;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * This navigation page class is responsible for redirecting an entity supertype (person, org, etc)
 * to a entity subtype (patient, physician, etc).
 */
public class EntityRedirectorPage extends NavigationPage
{
    public static int ID_UNKNOWN = -9999;
    private static Log log = LogFactory.getLog(EntityRedirectorPage.class);
    private static final String SESSATTRNAME_ESRI = "EntityRedirectorPage.ESRI";

    public class EntityRedirectorPageState extends State
    {
        private int entitySubtypeId = ID_UNKNOWN;
        private String entitySubtypeName;
        private EntitySubtypeInfo entitySubtype;

        public EntityRedirectorPageState()
        {
        }

        public int getEntitySubtypeId()
        {
            return entitySubtypeId;
        }

        public String getEntitySubtypeName()
        {
            return entitySubtypeName;
        }

        public EntitySubtypeInfo getEntitySubtype()
        {
            return entitySubtype;
        }

        public void setEntitySubtype(EntitySubtypeInfo entitySubtype)
        {
            this.entitySubtype = entitySubtype;
        }

        public void setEntitySubtype(int id, String name)
        {
            entitySubtypeId = id;
            entitySubtypeName = name;

            setEntitySubtype(getEntitySubtypeInfoById(id));
            if(getEntitySubtype() == null && name != null)
                setEntitySubtype(getEntitySubtypeInfoByName(name));
        }
    }

    public class EntitySubtypeRedirectInfo
    {
        private Object id;
        private Object data;

        public EntitySubtypeRedirectInfo(Object id, Object data)
        {
            this.id = id;
            this.data = data;
        }

        public Object getId()
        {
            return id;
        }

        public Object getData()
        {
            return data;
        }
    }

    protected class EntitySubtypeInfo
    {
        private int id = ID_UNKNOWN;
        private String name;
        private ValueSource redirect;
        private String[] retainParams;
        private String schemaEnum;

        public EntitySubtypeInfo()
        {
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public ValueSource getRedirect()
        {
            return redirect;
        }

        public void setRedirect(ValueSource redirect)
        {
            this.redirect = redirect;
        }

        public String[] getRetainParams()
        {
            return retainParams;
        }

        public void setRetainParam(String retainParams)
        {
            this.retainParams = new String[]{retainParams};
        }

        public void setRetainParams(String retainParams)
        {
            this.retainParams = TextUtils.getInstance().split(retainParams, ",", true);
        }

        /**
         * Sets the retain parameters for the entity subtype. Protected because it should only be called from Java
         * and not XDM.
         */
        protected void setRetainParams(String[] retainParams)
        {
            this.retainParams = retainParams;
        }

        public String getSchemaEnum()
        {
            return schemaEnum;
        }

        public void setSchemaEnum(String schemaEnum)
        {
            this.schemaEnum = schemaEnum;
        }
    }

    private String entityIdRequestParamName;
    private Map subtypeInfoByIdMap = new HashMap();
    private Map subtypeInfoByNameMap = new HashMap();
    private Query activeEntityQuery;
    private Class activeEntityClass;

    public EntityRedirectorPage(NavigationTree owner)
    {
        super(owner);
        getFlags().setFlag(Flags.HIDDEN);
    }

    public boolean isEntityValid(NavigationContext nc, ActiveEntity activeEntity)
    {
        return true;
    }

    public ActiveEntity getActiveEntity(NavigationContext nc, ConnectionContext cc) throws NamingException, SQLException
    {
        if(activeEntityQuery != null)
        {
            try
            {
                QueryResultSet qrs = activeEntityQuery.execute(nc, new Object[]{getEntityIdRequestParamValue(nc)}, false);
                if(qrs != null)
                {
                    ActiveEntity activeEntity = (ActiveEntity) activeEntityClass.newInstance();
                    ResultSetUtils.getInstance().assignColumnValuesToInstance(qrs.getResultSet(), activeEntity, "*", true);
                    qrs.close(true);

                    return activeEntity;
                }
            }
            catch(Exception e)
            {
                log.error("Error assigning primary org to user", e);
            }
        }

        return null;
    }

    protected boolean isEntityValid(NavigationContext nc, ConnectionContext cc) throws NamingException, SQLException
    {
        if(activeEntityQuery != null)
        {
            try
            {
                QueryResultSet qrs = activeEntityQuery.execute(nc, null, false);
                if(qrs != null)
                {
                    ActiveEntity activeEntity = (ActiveEntity) activeEntityClass.newInstance();
                    ResultSetUtils.getInstance().assignColumnValuesToInstance(qrs.getResultSet(), activeEntity, "*", true);
                    qrs.close(true);

                    EntityRedirectorPageState state = (EntityRedirectorPageState) nc.getActiveState();
                    state.setEntitySubtype(activeEntity.getEntityType(), activeEntity.getEntityTypeName());

                    setEntitySubtypeRedirect(nc, activeEntity.getEntityId(), activeEntity);
                    return isEntityValid(nc, activeEntity);
                }
            }
            catch(Exception e)
            {
                log.error("Error assigning primary org to user", e);
            }
        }

        return false;
    }

    public NavigationPath.State constructState()
    {
        return new EntityRedirectorPageState();
    }

    public String getEntityIdRequestParamName()
    {
        return entityIdRequestParamName;
    }

    public void setEntityIdRequestParamName(String entityIdRequestParamName)
    {
        this.entityIdRequestParamName = entityIdRequestParamName;
    }

    public String getEntityIdRequestParamValue(HttpServletValueContext hsvc)
    {
        return hsvc.getRequest().getParameter(entityIdRequestParamName);
    }

    public Map getSubtypeInfoByIdMap()
    {
        return subtypeInfoByIdMap;
    }

    public Map getSubtypeInfoByNameMap()
    {
        return subtypeInfoByNameMap;
    }

    public Class getActiveEntityClass()
    {
        return activeEntityClass;
    }

    public void setActiveEntityClass(Class activeEntityClass)
    {
        this.activeEntityClass = activeEntityClass;
    }

    public Query getActiveEntityQuery()
    {
        return activeEntityQuery;
    }

    public Query createActiveEntityQuery()
    {
        return new Query();
    }

    public void addActiveEntityQuery(Query query)
    {
        this.activeEntityQuery = query;
    }

    public EntitySubtypeInfo createEntitySubtype()
    {
        return new EntitySubtypeInfo();
    }

    public void addEntitySubtype(EntitySubtypeInfo subtypeInfo)
    {
        if(subtypeInfo.getSchemaEnum() != null)
        {
            String[] params = TextUtils.getInstance().split(subtypeInfo.getSchemaEnum(), ",", true);
            if(params.length != 2)
                log.error("the schema-enum attribute in <sub-type> of entity redirector requires 2 params: schema.enum-table,enum-id-or-caption");
            else
            {
                Project project = getOwner().getProject();
                Table table = project.getSchemas().getTable(params[0]);
                if(table == null || !(table instanceof EnumerationTable))
                    log.error("the schema-enum attribute in <sub-type> of entity redirector has an invalid schema.enum-table: " + params[0]);
                else
                {
                    EnumerationTable enumTable = (EnumerationTable) table;
                    EnumerationTableRows enumRows = (EnumerationTableRows) enumTable.getData();
                    EnumerationTableRow enumRow = enumRows.getByIdOrCaptionOrAbbrev(params[1]);
                    if(enumRow == null)
                        log.error("the schema-enum attribute in <sub-type> of entity redirector has an invalid enum value for " + params[0] + ": " + params[1]);
                    else
                        subtypeInfo.setId(enumRow.getId());
                }
            }
        }

        if(subtypeInfo.getId() != ID_UNKNOWN)
            subtypeInfoByIdMap.put(new Integer(subtypeInfo.getId()), subtypeInfo);

        if(subtypeInfo.getName() != null)
            subtypeInfoByNameMap.put(subtypeInfo.getName(), subtypeInfo);
    }

    public EntitySubtypeInfo getEntitySubtypeInfoById(int id)
    {
        return (EntitySubtypeInfo) subtypeInfoByIdMap.get(new Integer(id));
    }

    public EntitySubtypeInfo getEntitySubtypeInfoByName(String name)
    {
        return (EntitySubtypeInfo) subtypeInfoByNameMap.get(name);
    }

    public static EntitySubtypeRedirectInfo getEntitySubtypeRedirectInfo(NavigationContext nc, Object id)
    {
        HttpSession session = nc.getHttpRequest().getSession();
        EntitySubtypeRedirectInfo esri = (EntitySubtypeRedirectInfo) session.getAttribute(SESSATTRNAME_ESRI);
        session.removeAttribute(SESSATTRNAME_ESRI); // get rid of it so it's not hanging around for next call
        if(esri != null && esri.getId().equals(id))
            return esri;
        else
            return null;
    }

    public void setEntitySubtypeRedirect(NavigationContext nc, Object id, Object data)
    {
        EntitySubtypeRedirectInfo esRedirectInfo = new EntitySubtypeRedirectInfo(id, data);
        nc.getHttpRequest().getSession().setAttribute(SESSATTRNAME_ESRI, esRedirectInfo);
    }

    public boolean isValid(NavigationContext nc)
    {
        if(!super.isValid(nc))
            return false;

        ConnectionContext cc;
        try
        {
            cc = nc.getConnection(null, false);
        }
        catch(Exception e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }

        try
        {
            if(!isEntityValid(nc, cc))
                return false;

            EntityRedirectorPageState state = (EntityRedirectorPageState) nc.getActiveState();
            if(state.getEntitySubtype() != null)
            {
                // set this to true so that the navigation controller will ask us later for the URL we want to redirect to
                nc.setRedirectRequired(true);

                // we always return false since this page is never valid (it's just a redirector)
                return false;
            }

            // if we get to here then we'll be handling the body since no other page matched
            return true;
        }
        catch(Exception e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
        finally
        {
            try
            {
                cc.close();
            }
            catch(SQLException e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }
    }

    public String getUrl(HttpServletValueContext vc)
    {
        EntityRedirectorPageState state = (EntityRedirectorPageState) ((NavigationContext) vc).getActiveState();
        if(state.getEntitySubtype() != null)
            return state.getEntitySubtype().getRedirect().getTextValue(vc);
        else
            return super.getUrl(vc);
    }
}
