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
 * $Id: AccessControlList.java,v 1.5 2003-10-11 14:31:53 shahid.shah Exp $
 */

package com.netspective.commons.acl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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

    protected void setManager(AccessControlLists manager)
    {
        this.manager = manager;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getQualifiedName()
    {
		if (null == qualifiedName)
        {
            String qName = AccessControlList.NAME_SEPARATOR + getName();
			setQualifiedName(qName);
        }

	    return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }

    protected Map getPermissionsByName()
    {
        return permissionsByName;
    }

    protected Map getRolesByName()
    {
        return rolesByName;
    }

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

    public void registerPermission(Permission perm)
    {
        permissionsByName.put(perm.getQualifiedName(), perm);
        manager.registerPermission(perm);
    }

    public void registerRole(Role role)
    {
        rolesByName.put(role.getQualifiedName(), role);
        manager.registerRole(role);
    }

    public Permission createPermission()
    {
        return new Permission(this);
    }

    public void addPermission(Permission perm)
    {
        permissions.add(perm);
        registerPermission(perm);
    }

    public Role createRole()
    {
        return new Role(this);
    }

    public void addRole(Role role)
    {
        roles.add(role);
        registerRole(role);
    }

    public Permission getPermission(String name) throws PermissionNotFoundException
    {
        Permission result = (Permission) permissionsByName.get(name);
        if(result == null)
            throw new PermissionNotFoundException("Permission '"+ name +"' not found in ACL.", getOwner(), name);
        else
            return result;
    }

    public Role getRole(String name) throws RoleNotFoundException
    {
        Role result = (Role) rolesByName.get(name);
        if(result == null)
            throw new RoleNotFoundException("Role '"+ name +"' not found in ACL.", getOwner(), name);
        else
            return result;
    }

    public List getPermissions()
    {
        return permissions;
    }

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