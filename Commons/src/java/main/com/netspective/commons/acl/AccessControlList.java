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
 * $Id: AccessControlList.java,v 1.6 2004-01-06 05:29:33 aye.thu Exp $
 */

package com.netspective.commons.acl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.io.InputSourceLocator;

public class AccessControlList implements XmlDataModelSchema.InputSourceLocatorListener
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    public static final String ACLNAME_DEFAULT = "acl";
    public static final String NAME_SEPARATOR = "/";

    private InputSourceLocator inputSourceLocator;
    private AccessControlLists manager;
    private String name;
    private String qualifiedName;
    private Map permissionsByName = new HashMap();
    private Map rolesByName = new HashMap();
    private List permissions = new ArrayList();
    private List roles = new ArrayList();

    public AccessControlList(AccessControlLists manager)
    {
        setName(ACLNAME_DEFAULT);
        setManager(manager);
    }

    public AccessControlLists getManager()
    {
        return manager;
    }

    public InputSourceLocator getInputSourceLocator()
    {
        return inputSourceLocator;
    }

    public void setInputSourceLocator(InputSourceLocator inputSourceLocator)
    {
        this.inputSourceLocator = inputSourceLocator;
    }

    /**
     * Sets the lists manager
     *
     * @param manager
     */
    protected void setManager(AccessControlLists manager)
    {
        this.manager = manager;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the list
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the qualified name of the ACL
     * @return
     */
    public String getQualifiedName()
    {
		if (null == qualifiedName)
        {
            String qName = AccessControlList.NAME_SEPARATOR + getName();
			setQualifiedName(qName);
        }

	    return qualifiedName;
    }

    /**
     * Sets the qualified name for the list
     *
     * @param qualifiedName
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }

    /**
     * Gets a map of all permissions including children
     *
     * @return
     */
    protected Map getPermissionsByName()
    {
        return permissionsByName;
    }

    /**
     * Gets a map of all roles including children
     *
     * @return
     */
    protected Map getRolesByName()
    {
        return rolesByName;
    }

    /**
     * Gets a list of qualified names of all the registered roles in the ACL
     *
     * @return array of qualified names of registered roles
     */
    public String[] getRoleQualifiedNames()
    {
        String[] roleNames = new String[rolesByName.size()];
        Object[] names = rolesByName.keySet().toArray();
        for (int i=0; i < roleNames.length; i++)
            roleNames[i] = (String)names[i];

        return roleNames;
    }

    /**
     * Gets a list of names of all the registered roles in the ACL
     *
     * @return array of names of registered roles
     */
    public String[] getRoleNames()
    {
        String[] roleNames = new String[rolesByName.size()];
        Object[] names = rolesByName.keySet().toArray();
        for (int i=0; i < roleNames.length; i++)
        {
            Role role = (Role)rolesByName.get((String)names[i]);
            roleNames[i] = role.getName();
        }
        return roleNames;
    }

    /**
     * Gets the owner of this ACL. Currently, all ACL's are their own owners.
     * @return
     */
    public AccessControlList getOwner()
    {
        return this;
    }

    public int getHighestPermissionId()
    {
        return manager.getHighestPermissionId();
    }

    public int getHighestRoleId()
    {
        return manager.getHighestRoleId();
    }

    /**
     * Registers a permission for the list. This SHOULD only be used to register children permissions of
     * other permissions.
     *
     * @param perm the permission object for registration
     */
    protected void registerPermission(Permission perm)
    {
        permissionsByName.put(perm.getQualifiedName(), perm);
        manager.registerPermission(perm);
    }

    /**
     * Registers a role for the ACL. This SHOULD only be used to register children roles of other roles.
     *
     * @param role the role object for registration
     */
    protected void registerRole(Role role)
    {
        rolesByName.put(role.getQualifiedName(), role);
        manager.registerRole(role);
    }

    /**
     * Creates a permission object
     * @return
     */
    public Permission createPermission()
    {
        return new Permission(this);
    }

    /**
     * Adds and registers a permission to the ACL
     * @param perm
     */
    public void addPermission(Permission perm)
    {
        permissions.add(perm);
        registerPermission(perm);
    }

    public Role createRole()
    {
        return new Role(this);
    }

    /**
     * Adds and registers a role to the ACL
     * @param role
     */
    public void addRole(Role role)
    {
        roles.add(role);
        registerRole(role);
    }

    /**
     * Gets a registered permission by its qualified name
     *
     * @param name the permission's qualified name
     * @return
     * @throws PermissionNotFoundException
     */
    public Permission getPermission(String name) throws PermissionNotFoundException
    {
        Permission result = (Permission) permissionsByName.get(name);
        if(result == null)
            throw new PermissionNotFoundException("Permission '"+ name +"' not found in ACL.", getOwner(), name);
        else
            return result;
    }

    /**
     * Gets a registered role by its qualified name
     *
     * @param name the role's qualified name
     * @return
     * @throws RoleNotFoundException
     */
    public Role getRole(String name) throws RoleNotFoundException
    {
        Role result = (Role) rolesByName.get(name);
        if(result == null)
            throw new RoleNotFoundException("Role '"+ name +"' not found in ACL.", getOwner(), name);
        else
            return result;
    }

    /**
     * Gets root permissions defined to the ACL. This does not include children permissions.
     * @return
     */
    public List getPermissions()
    {
        return permissions;
    }

    /**
     * Gets root roles belonging to the ACL. This does not include children roles.
     * @return
     */
    public List getRoles()
    {
        return roles;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Permissions:\n");
        for(int i = 0; i < permissions.size(); i++)
            sb.append(permissions.get(i));
        sb.append("Roles:\n");
        for(int i = 0; i < roles.size(); i++)
            sb.append(roles.get(i));
        return sb.toString();
    }
}