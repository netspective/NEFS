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
 * $Id: DatabaseLoginAuthenticator.java,v 1.8 2004-08-03 19:47:26 shahid.shah Exp $
 */

package com.netspective.sparx.security.authenticator;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.sql.ResultSetUtils;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.security.AuthenticatedUserInitializationException;
import com.netspective.commons.security.MutableAuthenticatedOrgUser;
import com.netspective.commons.security.MutableAuthenticatedOrgsUser;
import com.netspective.commons.security.MutableAuthenticatedUser;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.security.LoginDialogContext;

public class DatabaseLoginAuthenticator extends AbstractLoginAuthenticator
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(DatabaseLoginAuthenticator.class);

    private String passwordQueryPasswordColumnLabel = "password";
    private Query passwordQuery;        // the query to get the user's password
    private Query roleQuery;            // the query to get the user's roles
    private Query orgsQuery;            // the query to get the user's multiple organizations
    private Query orgQuery;             // the query to get the user's single organization
    private boolean passwordEncrypted;  // true if the password is encrypted
    private static final String ATTRNAME_PASSWORD_QUERY_RESULTS = "PASSWORD_QUERY_RESULTS";
    private static final String ATTRNAME_ORGS_QUERY_RESULTS = "ORGS_QUERY_RESULTS";

    public String getPasswordQueryPasswordColumnLabel()
    {
        return passwordQueryPasswordColumnLabel;
    }

    public void setPasswordQueryPasswordColumnLabel(String passwordQueryPasswordColumnLabel)
    {
        this.passwordQueryPasswordColumnLabel = passwordQueryPasswordColumnLabel;
    }

    public boolean isUserValid(HttpLoginManager loginManager, LoginDialogContext ldc)
    {
        if (passwordQuery == null)
        {
            ldc.getValidationContext().addError("No password query defined in DatabaseLoginAuthenticator");
            return false;
        }

        try
        {
            QueryResultSet qrs = passwordQuery.execute(ldc, new Object[]{ldc.getUserIdInput()}, false);
            if (qrs == null)
                return false;

            Map passwordQueryResultRow = ResultSetUtils.getInstance().getResultSetSingleRowAsMap(qrs.getResultSet(), true);
            Object loginPasswordObj = passwordQueryResultRow.get(passwordQueryPasswordColumnLabel);
            ldc.setAttribute(ATTRNAME_PASSWORD_QUERY_RESULTS, passwordQueryResultRow);
            qrs.close(true);

            // make sure the password doesn't stay in the map as it's passed around the system
            passwordQueryResultRow.remove(passwordQueryPasswordColumnLabel);

            if (loginPasswordObj == null)
                return false;

            String loginPasswordText = loginPasswordObj.toString();
            // if the password is not encrypted in the database, then encrypt it now because we deal with encrypted passwords internally
            if (!passwordEncrypted)
                loginPasswordText = ldc.encryptPlainTextPassword(loginPasswordText);

            // now we check if this is a valid user
            if (!loginPasswordText.equals(ldc.getPasswordInput(!ldc.hasEncryptedPassword())))
                return false;
        }
        catch (Exception e)
        {
            log.error("Error validating login", e);
            return false;
        }

        return true;
    }

    public void initAuthenticatedUser(HttpLoginManager loginManager, LoginDialogContext ldc, MutableAuthenticatedUser user) throws AuthenticatedUserInitializationException
    {
        assignUserInfo(ldc, user);
        assignOrganizations(ldc, user);
        assignRoles(ldc, user);

        // the super will call user.init so we want to give the authenticated user class a chance to initalize itself
        // now that the user information and roles have been assigned
        super.initAuthenticatedUser(loginManager, ldc, user);
    }

    /**
     * Take all of the columns that were selected by the passwordQuery and assign them using the setXXX() methods
     * of the AuthenticatedUser object. Using reflection, the assignUserInfo() method will take the labels assigned
     * in the query and find the appropriate setXXXX() method (where XXXX is the column label in the SQL query).
     *
     * @param ldc
     * @param user
     */
    protected void assignUserInfo(LoginDialogContext ldc, AuthenticatedUser user)
    {
        XmlDataModelSchema schema = XmlDataModelSchema.getSchema(user.getClass());
        try
        {
            schema.assignMapValues(user, (Map) ldc.getAttribute(ATTRNAME_PASSWORD_QUERY_RESULTS), "*");
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }

    protected void assignRoles(LoginDialogContext ldc, MutableAuthenticatedUser user)
    {
        if (roleQuery != null)
        {
            try
            {
                QueryResultSet qrs = roleQuery.execute(ldc, new Object[]{ldc.getUserIdInput()}, false);
                if (qrs != null)
                {
                    String[] roleNames = ResultSetUtils.getInstance().getResultSetRowsAsStrings(qrs.getResultSet());
                    if (roleNames != null && roleNames.length > 0)
                        user.setRoles(ldc.getAccessControlListsManager(), roleNames);
                    qrs.close(true);
                }
            }
            catch (Exception e)
            {
                log.error("Error assigning roles to user", e);
            }
        }
    }

    protected void assignOrganizations(LoginDialogContext ldc, AuthenticatedUser user)
    {
        if (orgsQuery != null)
        {
            if (!(user instanceof MutableAuthenticatedOrgsUser))
                log.error("Unable to assign organizations using orgs-query since the AuthenticatedUser does not implement MutableAuthenticatedOrgsUser");
            else
            {
                try
                {
                    QueryResultSet qrs = orgsQuery.execute(ldc, new Object[]{ldc.getUserIdInput()}, false);
                    if (qrs != null)
                    {
                        Object[][] orgsResult = ResultSetUtils.getInstance().getResultSetRowsAsMatrix(qrs.getResultSet());
                        ldc.setAttribute(ATTRNAME_ORGS_QUERY_RESULTS, orgsResult);

                        MutableAuthenticatedOrgsUser mutableOrgsUser = (MutableAuthenticatedOrgsUser) user;
                        for (int rowIndex = 0; rowIndex < orgsResult.length; rowIndex++)
                        {
                            Object[] row = orgsResult[rowIndex];
                            switch (row.length)
                            {
                                case 1:
                                    mutableOrgsUser.addUserOrg(false, row[0].toString(), row[0].toString());
                                    break;

                                case 2:
                                    mutableOrgsUser.addUserOrg(false, row[0].toString(), row[1].toString());
                                    break;

                                default: /** 3 or more **/
                                    Object isPrimaryObjValue = row[2];
                                    boolean isPrimary = false;
                                    if (isPrimaryObjValue instanceof Boolean)
                                        isPrimary = ((Boolean) isPrimaryObjValue).booleanValue();
                                    else if (isPrimaryObjValue instanceof Integer)
                                        isPrimary = ((Integer) isPrimaryObjValue).intValue() == 1 ? true : false;
                                    else if (isPrimaryObjValue instanceof Long)
                                        isPrimary = ((Long) isPrimaryObjValue).longValue() == 1 ? true : false;
                                    else
                                        isPrimary = TextUtils.toBoolean(isPrimaryObjValue.toString(), false);

                                    mutableOrgsUser.addUserOrg(isPrimary, row[0].toString(), row[1].toString());
                                    break;
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    log.error("Error assigning orgs to user", e);
                }
            }
        }

        if (orgQuery != null)
        {
            if (!(user instanceof MutableAuthenticatedOrgUser))
                log.error("Unable to assign organization using org-query since the AuthenticatedUser does not implement MutableAuthenticatedOrgUser");
            else
            {
                try
                {
                    QueryResultSet qrs = orgQuery.execute(ldc, new Object[]{ldc.getUserIdInput()}, false);
                    if (qrs != null)
                    {
                        Object[] orgResult = ResultSetUtils.getInstance().getResultSetSingleRowAsArray(qrs.getResultSet());
                        ldc.setAttribute(ATTRNAME_ORGS_QUERY_RESULTS, orgResult);

                        MutableAuthenticatedOrgUser mutableOrgUser = (MutableAuthenticatedOrgUser) user;
                        mutableOrgUser.setUserOrgId(orgResult[0].toString());
                        if (orgResult.length > 1)
                            mutableOrgUser.setUserOrgName(orgResult[1].toString());
                    }
                }
                catch (Exception e)
                {
                    log.error("Error assigning orgs to user", e);
                }
            }
        }
    }

    public boolean isPasswordEncrypted()
    {
        return passwordEncrypted;
    }

    public void setPasswordEncrypted(boolean passwordEncrypted)
    {
        this.passwordEncrypted = passwordEncrypted;
    }

    public Query getPasswordQuery()
    {
        return passwordQuery;
    }

    public Query createPasswordQuery()
    {
        return new Query();
    }

    public void addPasswordQuery(Query query)
    {
        passwordQuery = query;
    }

    public Query getRoleQuery()
    {
        return roleQuery;
    }

    public Query createRoleQuery()
    {
        return new Query();
    }

    public void addRoleQuery(Query query)
    {
        roleQuery = query;
    }

    public Query getOrgsQuery()
    {
        return orgsQuery;
    }

    public Query createOrgsQuery()
    {
        return new Query();
    }

    public void addOrgsQuery(Query query)
    {
        orgsQuery = query;
    }

    public Query getOrgQuery()
    {
        return orgQuery;
    }

    public Query createOrgQuery()
    {
        return new Query();
    }

    public void addOrgQuery(Query query)
    {
        orgQuery = query;
    }
}
