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
 * $Id: AccessControlListTest.java,v 1.7 2003-03-20 14:57:13 shahid.shah Exp $
 */

package com.netspective.commons.acl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.BitSet;

import junit.framework.TestCase;

import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.DefaultXdmComponentItems;
import com.netspective.commons.io.Resource;
import com.netspective.commons.acl.AccessControlListsComponent;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.security.AuthenticatedUser;

public class AccessControlListTest extends TestCase
{
    public static final String RESOURCE_NAME_ONE = "AccessControlListTest-One.xml";
    public static final String RESOURCE_NAME_TWO = "AccessControlListTest-Two.xml";
    public static final String RESOURCE_NAME_THREE = "AccessControlListTest-Three.xml";

	/**
	 * This test makes sure that an ACL is read properly from a file containing just a single <access-control-list>
	 * tag with the default name of "acl"
	 *
	 * @throws DataModelException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    public void testSingleACLDataModelSchemaImportFromXmlValid() throws PermissionNotFoundException, DataModelException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
    {
        AccessControlListsComponent aclc =
                (AccessControlListsComponent) XdmComponentFactory.get(
                        AccessControlListsComponent.class,
                        new Resource(AccessControlListTest.class, RESOURCE_NAME_ONE),
                        XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		// Verify _something_ was loaded...
        assertNotNull(aclc);

		// Verify exactly _one_ ACL was loaded...
		AccessControlListsManager aclm = aclc.getItems();
		assertNotNull("Expected: AccessControlListsManager object, Found: null", aclm);
	    AccessControlLists acls = aclm.getAccessControlLists();
		Integer expectedNumACLs = new Integer (1);
		assertNotNull("Expected: AccessControlLists object, Found: null", acls);
	    assertEquals("Expected: " + expectedNumACLs + " ACL, Found: " + acls.size(), expectedNumACLs.intValue(), acls.size());

		// Verify the defaultAcl and the acl named "acl" are the same
	    AccessControlList defaultAcl = aclm.getDefaultAccessControlList();
	    AccessControlList aclAcl = aclm.getAccessControlList("acl");
		assertNotNull("Expected: Non-Null Default ACL, Found: null", defaultAcl);
		assertNotNull("Expected: Non-Null Default ACL, Found: null", aclAcl);
	    assertEquals("Expected: ACL with name 'acl', Found: ACL with name " + defaultAcl.getName(), aclAcl, defaultAcl);

		// Verify exactly _eleven_ permissions were loaded in this acl
		Map aclPermissions = defaultAcl.getPermissionsByName();
		assertEquals("Expected: Total permissions = 10, Found: Total permissions = " + aclPermissions.size(), 10, aclPermissions.size());

		// Verify the index of the /acl/role/normal-user permission is 10
		Permission normalUser = defaultAcl.getPermission("/acl/role/normal-user");
		assertEquals("Expected: Id for /acl/role/normal-user = 9, Found: " + normalUser.getId(), 9, normalUser.getId());

		// Verify the set of permissions for /acl/role/normal-user are exactly what we expect
		BitSet normalUserPermissionSet = normalUser.getChildPermissions();
		BitSet expectedPermissionSet = new BitSet(11);
		expectedPermissionSet.set(1);
		expectedPermissionSet.set(2);
		expectedPermissionSet.set(3);
		expectedPermissionSet.set(4);
		expectedPermissionSet.set(5);
		expectedPermissionSet.set(9);
		assertEquals("Expected: Permissions for /acl/role/normal-user = " + expectedPermissionSet + ", Found: " + normalUserPermissionSet, expectedPermissionSet, normalUserPermissionSet);
		assertEquals("Expected: Permissions for /acl/role/normal-user = " + expectedPermissionSet + ", Found: " + normalUserPermissionSet, expectedPermissionSet, normalUserPermissionSet);

        aclc.printErrorsAndWarnings();
    }

	/**
	 * This test makes sure that an ACL is read properly from a file containing just a single <access-control-list>
	 * tag with the name "main".
	 *
	 * @throws DataModelException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    public void testSingleACLNonDefaultDataModelSchemaImportFromXmlValid() throws PermissionNotFoundException, DataModelException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
    {
        AccessControlListsComponent aclc =
                (AccessControlListsComponent) XdmComponentFactory.get(
                        AccessControlListsComponent.class,
                        new Resource(AccessControlListTest.class, RESOURCE_NAME_TWO),
                        XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		// Verify _something_ was loaded...
        assertNotNull(aclc);

		// Verify exactly _one_ ACL was loaded...
		AccessControlListsManager aclm = aclc.getItems();
		assertNotNull("Expected: AccessControlListsManager object, Found: null", aclm);
	    AccessControlLists acls = aclm.getAccessControlLists();
		Integer expectedNumACLs = new Integer (1);
		assertNotNull("Expected: AccessControlLists object, Found: null", acls);
	    assertEquals("Expected: " + expectedNumACLs + " ACL, Found: " + acls.size(), expectedNumACLs.intValue(), acls.size());

		// Verify there is no defaultAcl and that it returns a null
	    AccessControlList defaultAcl = aclm.getDefaultAccessControlList();
	    assertNull("Expected: Default ACL = null, Found: Non-Null ACL", defaultAcl);

		AccessControlList mainAcl = aclm.getAccessControlList("main");
		assertNotNull("Expected: ACL named 'main', Found: null", mainAcl);
		assertEquals("Expected: ACL named 'main', Found: ACL named '" + mainAcl.getName() + "'", "main", mainAcl.getName());

		// Verify exactly _eleven_ permissions were loaded in this acl
		Map aclPermissions = mainAcl.getPermissionsByName();
		assertNotNull("Expected: List of Permissions for ACL, Found: null", aclPermissions);
		assertEquals("Expected: Total permissions = 11, Found: Total permissions = " + aclPermissions.size(), 11, aclPermissions.size());

		// Verify the index of the /acl/role/normal-user permission is 10
		//TODO: Create a Permission object manually and use that as 'expected' in a later test case
		Permission normalUser = mainAcl.getPermission("/main/role/normal-user");
		Permission expectedPermission = (Permission) aclPermissions.get("/main/role/normal-user");
		assertEquals("Expected: Id for /main/role/normal-user = 10, Found: " + normalUser.getId(), 10, normalUser.getId());
		assertEquals("Expected: " + expectedPermission + ", Found: " + normalUser, expectedPermission, normalUser);

		// Verify the set of permissions for /acl/role/normal-user are exactly what we expect
		BitSet normalUserPermissionSet = normalUser.getChildPermissions();
		BitSet alternatePermissionSet = expectedPermission.getChildPermissions();
		assertEquals("Expected: Permissions for /main/role/normal-user = " + normalUserPermissionSet + ", Found: " + alternatePermissionSet, normalUserPermissionSet, alternatePermissionSet);
		BitSet expectedPermissionSet = new BitSet(11);
		expectedPermissionSet.set(1);
		expectedPermissionSet.set(2);
		expectedPermissionSet.set(3);
		expectedPermissionSet.set(4);
		expectedPermissionSet.set(5);
		expectedPermissionSet.set(10);
		assertEquals("Expected: Permissions for /main/role/normal-user = " + expectedPermissionSet + ", Found: " + normalUserPermissionSet, expectedPermissionSet, normalUserPermissionSet);
		assertEquals("Expected: Permissions for /main/role/normal-user = " + expectedPermissionSet + ", Found: " + normalUserPermissionSet, expectedPermissionSet, normalUserPermissionSet);

        aclc.printErrorsAndWarnings();
    }

	/**
	 * This test makes sure that an ACL is read properly from a file containing multiple <access-control-list>
	 * tags with one having the default name of "acl" and the rest having other names.
	 *
	 * @throws DataModelException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    public void testMultipleACLWithDefaultDataModelSchemaImportFromXmlValid() throws PermissionNotFoundException, DataModelException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
    {
		AccessControlListsComponent aclc =
		        (AccessControlListsComponent) XdmComponentFactory.get(
		                AccessControlListsComponent.class,
		                new Resource(AccessControlListTest.class, RESOURCE_NAME_THREE),
		                XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		// Verify _something_ was loaded...
		assertNotNull(aclc);

		// Verify exactly _one_ ACL was loaded...
		AccessControlListsManager aclm = aclc.getItems();
		assertNotNull("Expected: AccessControlListsManager object, Found: null", aclm);
		AccessControlLists acls = aclm.getAccessControlLists();
		Integer expectedNumACLs = new Integer (3);
		assertNotNull("Expected: AccessControlLists object, Found: null", acls);
		assertEquals("Expected: " + expectedNumACLs + " ACLs, Found: " + acls.size(), expectedNumACLs.intValue(), acls.size());

		// Verify the defaultAcl and the acl named "acl" are the same
		AccessControlList defaultAcl = aclm.getDefaultAccessControlList();
		AccessControlList aclAcl = aclm.getAccessControlList(AccessControlList.ACLNAME_DEFAULT);
		AccessControlList aclTwoAcl = aclm.getAccessControlList(AccessControlList.ACLNAME_DEFAULT + "_two");
		AccessControlList aclThreeAcl = aclm.getAccessControlList(AccessControlList.ACLNAME_DEFAULT + "_three");
		assertNotNull("Expected: Non-Null ACL named " + AccessControlList.ACLNAME_DEFAULT + ", Found: null", defaultAcl);
		assertNotNull("Expected: Non-Null ACL named " + AccessControlList.ACLNAME_DEFAULT + ", Found: null", aclAcl);
		assertNotNull("Expected: Non-Null ACL named " + AccessControlList.ACLNAME_DEFAULT + "_two, Found: null", aclTwoAcl);
		assertNotNull("Expected: Non-Null ACL named " + AccessControlList.ACLNAME_DEFAULT + "_three, Found: null", aclThreeAcl);
		assertEquals("Expected: ACL with name 'acl', Found: ACL with name " + defaultAcl.getName(), aclAcl, defaultAcl);
		assertEquals("Expected: ACL with name 'acl', Found: ACL with name " + aclAcl.getName(), AccessControlList.ACLNAME_DEFAULT, aclAcl.getName());
		assertEquals("Expected: ACL with name 'acl_two', Found: ACL with name " + aclTwoAcl.getName(), AccessControlList.ACLNAME_DEFAULT + "_two", aclTwoAcl.getName());
		assertEquals("Expected: ACL with name 'acl_three', Found: ACL with name " + aclThreeAcl.getName(), AccessControlList.ACLNAME_DEFAULT + "_three", aclThreeAcl.getName());

		// Verify exactly _eleven_ permissions were loaded for aclAcl, exactly
		Map aclAclPermissions = aclAcl.getPermissionsByName();
		Map aclTwoAclPermissions = aclTwoAcl.getPermissionsByName();
		Map aclThreeAclPermissions = aclThreeAcl.getPermissionsByName();
		assertEquals("Expected: Total permissions = 10, Found: Total permissions = " + aclAclPermissions.size(), 10, aclAclPermissions.size());
		assertEquals("Expected: Total permissions = 8, Found: Total permissions = " + aclTwoAclPermissions.size(), 8, aclTwoAclPermissions.size());
		assertEquals("Expected: Total permissions = 7, Found: Total permissions = " + aclThreeAclPermissions.size(), 7, aclThreeAclPermissions.size());

		// Verify the index of the /acl/role/normal-user permission is 10
		Permission aclNormalUser = aclAcl.getPermission("/acl/role/normal-user");
		Permission aclTwoNormalUser = aclTwoAcl.getPermission("/acl_two/role/normal-user");
		Permission aclThreeReadOnlyUser = aclThreeAcl.getPermission("/acl_three/role/read-only-user");
		assertEquals("Expected: Id for /acl/role/normal-user = 9, Found: " + aclNormalUser.getId(), 9, aclNormalUser.getId());
		assertEquals("Expected: Id for /acl_two/role/normal-user = 17, Found: " + aclTwoNormalUser.getId(), 17, aclTwoNormalUser.getId());
		assertEquals("Expected: Id for /acl_three/role/read-only-user = 24, Found: " + aclThreeReadOnlyUser.getId(), 24, aclThreeReadOnlyUser.getId());

		// Verify the set of permissions for /acl/role/normal-user are exactly what we expect
		BitSet aclNormalUserPermissionSet = aclNormalUser.getChildPermissions();
		BitSet aclExpectedPermissionSet = new BitSet(24);
		aclExpectedPermissionSet.set(1);
		aclExpectedPermissionSet.set(2);
		aclExpectedPermissionSet.set(3);
		aclExpectedPermissionSet.set(4);
		aclExpectedPermissionSet.set(5);
		aclExpectedPermissionSet.set(9);
		assertEquals("Expected: Permissions for /acl/role/normal-user = " + aclExpectedPermissionSet + ", Found: " + aclNormalUserPermissionSet, aclExpectedPermissionSet, aclNormalUserPermissionSet);

		// Verify the set of permissions for /acl_two/role/normal-user are exactly what we expect
		BitSet aclTwoNormalUserPermissionSet = aclTwoNormalUser.getChildPermissions();
		BitSet aclTwoExpectedPermissionSet = new BitSet(24);
		aclTwoExpectedPermissionSet.set(11);
		aclTwoExpectedPermissionSet.set(12);
		aclTwoExpectedPermissionSet.set(13);
		aclTwoExpectedPermissionSet.set(17);
		assertEquals("Expected: Permissions for /acl_two/role/normal-user = " + aclTwoExpectedPermissionSet + ", Found: " + aclTwoNormalUserPermissionSet, aclTwoExpectedPermissionSet, aclTwoNormalUserPermissionSet);

		// Verify the set of permissions for /acl_three/role/read-only-user are exactly what we expect
		BitSet aclThreeReadOnlyUserPermissionSet = aclThreeReadOnlyUser.getChildPermissions();
		BitSet aclThreeExpectedPermissionSet = new BitSet(24);
		aclThreeExpectedPermissionSet.set(19);
		aclThreeExpectedPermissionSet.set(21);
		aclThreeExpectedPermissionSet.set(24);
		assertEquals("Expected: Permissions for /acl_three/role/read-only-user = " + aclThreeExpectedPermissionSet + ", Found: " + aclThreeReadOnlyUserPermissionSet, aclThreeExpectedPermissionSet, aclThreeReadOnlyUserPermissionSet);

		aclc.printErrorsAndWarnings();
    }

	public void testACLPermissionProgrammaticRobustness() throws DataModelException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException, IOException
	{
		AccessControlListsComponent aclc =
		        (AccessControlListsComponent) XdmComponentFactory.get(
		                AccessControlListsComponent.class,
		                new Resource(AccessControlListTest.class, RESOURCE_NAME_THREE),
		                XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		// Verify _something_ was loaded...
		assertNotNull(aclc);

		// Verify exactly _three_ ACL was loaded...
		AccessControlListsManager aclm = aclc.getItems();
		assertNotNull("Expected: AccessControlListsManager object, Found: null", aclm);
		AccessControlLists acls = aclm.getAccessControlLists();
		Integer expectedNumACLs = new Integer (3);
		assertNotNull("Expected: AccessControlLists object, Found: null", acls);
		assertEquals("Expected: " + expectedNumACLs + " ACLs, Found: " + acls.size(), expectedNumACLs.intValue(), acls.size());

		AccessControlList aclThree = null;
		aclThree = acls.getAccessControlList("blech");
		assertNull("Attempting to get a non-existant ACL actually returned something", aclThree);

        aclThree = acls.getAccessControlList("acl_three");
		assertNotNull("Attempting to get a valid ACL returned null", aclThree);

		Permission readOnlyUser = null;
		boolean exceptionThrown = true;
		try {
			readOnlyUser = aclThree.getPermission("/acl_three/role/readonlyuser");
			exceptionThrown = false;
		} catch (PermissionNotFoundException e) {
			assertTrue("No exception thrown on attempt to get invalid permission", exceptionThrown);
		}

        exceptionThrown = true;

		try {
			readOnlyUser = aclThree.getPermission("/acl_three/role/read-only-user");
			exceptionThrown = false;
		} catch (PermissionNotFoundException e) {
			assertFalse("Exception thrown on attempt to get _valid_ permission", exceptionThrown);
		}

		assertNotNull("Expecting a non-null Permission but found null", readOnlyUser);
	}

	public void testACLProgrammaticRobustness() throws DataModelException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException, IOException
	{
		AccessControlListsComponent aclc =
		        (AccessControlListsComponent) XdmComponentFactory.get(
		                AccessControlListsComponent.class,
		                new Resource(AccessControlListTest.class, RESOURCE_NAME_THREE),
		                XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		// Verify _something_ was loaded...
		assertNotNull(aclc);

		// Verify exactly _three_ ACL was loaded...
		AccessControlListsManager aclm = aclc.getItems();
		assertNotNull("Expected: AccessControlListsManager object, Found: null", aclm);
		AccessControlLists acls = aclm.getAccessControlLists();
		Integer expectedNumACLs = new Integer (3);
		assertNotNull("Expected: AccessControlLists object, Found: null", acls);
		assertEquals("Expected: " + expectedNumACLs + " ACLs, Found: " + acls.size(), expectedNumACLs.intValue(), acls.size());

		AccessControlList aclThree = null;
		aclThree = acls.getAccessControlList("blech");
		assertNull("Attempting to get a non-existant ACL actually returned something", aclThree);

        aclThree = acls.getAccessControlList("acl_three");
		assertNotNull("Attempting to get a valid ACL returned null", aclThree);

		AccessControlLists aclThreeManager = aclThree.getManager();
		assertNotNull("aclThree manager is null", aclThreeManager);
		assertEquals("aclThreeManager != aclm", acls, aclThreeManager);

	}

    public void testAuthenticatedUser() throws DataModelException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException, IOException
    {
        AccessControlListsComponent aclc =
                (AccessControlListsComponent) XdmComponentFactory.get(
                        AccessControlListsComponent.class,
                        new Resource(AccessControlListTest.class, RESOURCE_NAME_THREE),
                        XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

        // Verify _something_ was loaded...
        assertNotNull(aclc);

        ValueContext vc = ValueSources.getInstance().createDefaultValueContext();
        AuthenticatedUser user = vc.createAuthenticatedUser();

        user.setUserId("admin");
        user.setUserName("Administrator");

        // other tests...
    }
}
