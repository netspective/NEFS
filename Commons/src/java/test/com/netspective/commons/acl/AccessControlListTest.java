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
 * $Id: AccessControlListTest.java,v 1.10 2003-03-21 13:53:48 shahbaz.javeed Exp $
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
import com.netspective.commons.value.DefaultValueContext;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.security.BasicAuthenticatedUser;

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
    public void testSingleACLDataModelSchemaImportFromXmlValid() throws PermissionNotFoundException, DataModelException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, RoleNotFoundException
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

		// Verify number of permissions
		Map aclPermissions = defaultAcl.getPermissionsByName();
		assertEquals("Expected: Total permissions = 7, Found: Total permissions = " + aclPermissions.size(), 7, aclPermissions.size());

        // Verify number of roles
        Map aclRoles = defaultAcl.getRolesByName();
        assertEquals("Expected: Total roles = 3, Found: Total roles = " + aclRoles.size(), 3, aclRoles.size());

		// Verify the index of the /acl/role/normal-user permission is 2
		Role normalUser = defaultAcl.getRole("/acl/role/normal-user");
		assertEquals("Expected: Id for /acl/role/normal-user = 2, Found: " + normalUser.getId(), 2, normalUser.getId());

		// Verify the set of permissions for /acl/role/normal-user are exactly what we expect
		BitSet normalUserPermissionSet = normalUser.getPermissions();
		BitSet expectedPermissionSet = new BitSet(11);
		expectedPermissionSet.set(1);
		expectedPermissionSet.set(2);
		expectedPermissionSet.set(3);
		expectedPermissionSet.set(4);
		expectedPermissionSet.set(5);
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
    public void testSingleACLNonDefaultDataModelSchemaImportFromXmlValid() throws RoleNotFoundException, PermissionNotFoundException, DataModelException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
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
		assertEquals("Expected: Total permissions = 8, Found: Total permissions = " + aclPermissions.size(), 8, aclPermissions.size());

		// Verify exactly _two_ roles were loaded in this acl
		Map aclRoles = mainAcl.getRolesByName();
		assertNotNull(aclRoles);
		assertEquals(3, aclRoles.size());

		// Verify the /main/app/orders/edit_order permission has the right values
		Permission editOrder = mainAcl.getPermission("/main/app/orders/edit_order");
		assertEquals(4, editOrder.getId());
		assertEquals(2, editOrder.getAncestorsCount());
		assertEquals("    /main/app/orders/edit_order = 4 {4}\n", editOrder.toString());

		// Verify the /acl/role/normal-user has the proper id
		Role normalUser = mainAcl.getRole("/main/role/normal-user");
		assertEquals("Expected: Id for /main/role/normal-user = 2, Found: " + normalUser.getId(), 2, normalUser.getId());
		assertEquals(1, normalUser.getAncestorsCount());
		assertEquals("  /main/role/normal-user = 2 {1, 2, 3, 4, 5}\n", normalUser.toString());

		// Verify the set of permissions for /acl/role/normal-user are exactly what we expect
		BitSet normalUserPermissionSet = normalUser.getPermissions();
		BitSet expectedPermissionSet = new BitSet(11);
		expectedPermissionSet.set(1);
		expectedPermissionSet.set(2);
		expectedPermissionSet.set(3);
		expectedPermissionSet.set(4);
		expectedPermissionSet.set(5);
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
    public void testMultipleACLWithDefaultDataModelSchemaImportFromXmlValid() throws RoleNotFoundException, PermissionNotFoundException, DataModelException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
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

		// Verify basic statistics about the ACLS object
		assertEquals(17, acls.getHighestPermissionId());
		assertEquals(8, acls.getHighestRoleId());

		assertEquals(17, acls.getNextPermissionId());
		assertEquals(8, acls.getNextRoleId());

		assertEquals(17, acls.permissionsCount());
		assertEquals(8, acls.rolesCount());

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

		// Verify the number of permissions loaded for each ACL
		Map aclAclPermissions = aclAcl.getPermissionsByName();
		Map aclTwoAclPermissions = aclTwoAcl.getPermissionsByName();
		Map aclThreeAclPermissions = aclThreeAcl.getPermissionsByName();
		assertEquals("Expected: Total permissions = 7, Found: Total permissions = " + aclAclPermissions.size(), 7, aclAclPermissions.size());
		assertEquals("Expected: Total permissions = 5, Found: Total permissions = " + aclTwoAclPermissions.size(), 5, aclTwoAclPermissions.size());
		assertEquals("Expected: Total permissions = 5, Found: Total permissions = " + aclThreeAclPermissions.size(), 5, aclThreeAclPermissions.size());

		// Verify the number of roles loaded for each ACL
		Map aclAclRoles = aclAcl.getRolesByName();
		Map aclTwoAclRoles = aclTwoAcl.getRolesByName();
		Map aclThreeAclRoles = aclThreeAcl.getRolesByName();
		assertEquals(3, aclAclRoles.size());
		assertEquals(3, aclTwoAclRoles.size());
		assertEquals(2, aclThreeAclRoles.size());

		// Verify the index of the /acl/role/normal-user permission is 10
		Role aclNormalUser = aclAcl.getRole("/acl/role/normal-user");
		Role aclTwoNormalUser = aclTwoAcl.getRole("/acl_two/role/normal-user");
		Role aclThreeReadOnlyUser = aclThreeAcl.getRole("/acl_three/role/read-only-user");
		assertEquals("Expected: Id for /acl/role/normal-user = 2, Found: " + aclNormalUser.getId(), 2, aclNormalUser.getId());
		assertEquals("Expected: Id for /acl_two/role/normal-user = 5, Found: " + aclTwoNormalUser.getId(), 5, aclTwoNormalUser.getId());
		assertEquals("Expected: Id for /acl_three/role/read-only-user = 7, Found: " + aclThreeReadOnlyUser.getId(), 7, aclThreeReadOnlyUser.getId());

        //TODO: Fix Role so that the bit corresponding to the role's Id is not set in the role's permissions BitSet

		// Verify the set of permissions for /acl/role/normal-user are exactly what we expect
		BitSet aclNormalUserPermissionSet = aclNormalUser.getPermissions();
		BitSet aclExpectedPermissionSet = new BitSet(aclAcl.getHighestPermissionId());
		aclExpectedPermissionSet.set(1);
		aclExpectedPermissionSet.set(2);
		aclExpectedPermissionSet.set(3);
		aclExpectedPermissionSet.set(4);
		aclExpectedPermissionSet.set(5);
		//TODO: Fix this after Role's have been fixed
//		assertEquals("Expected: Permissions for /acl/role/normal-user = " + aclExpectedPermissionSet + ", Found: " + aclNormalUserPermissionSet, aclExpectedPermissionSet, aclNormalUserPermissionSet);

		// Verify the set of permissions for /acl_two/role/normal-user are exactly what we expect
		BitSet aclTwoNormalUserPermissionSet = aclTwoNormalUser.getPermissions();
		BitSet aclTwoExpectedPermissionSet = new BitSet(aclTwoAcl.getHighestPermissionId());
		aclTwoExpectedPermissionSet.set(8);
		aclTwoExpectedPermissionSet.set(9);
		aclTwoExpectedPermissionSet.set(10);
		aclTwoExpectedPermissionSet.set(11);
		//TODO: Fix this after Role's have been fixed
//		System.out.println("\n/acl_two/role/normal-user(" + aclTwoNormalUser.getId() + "): " + aclTwoNormalUserPermissionSet + "\n");
//		assertEquals("Expected: Permissions for /acl_two/role/normal-user = " + aclTwoExpectedPermissionSet + ", Found: " + aclTwoNormalUserPermissionSet, aclTwoExpectedPermissionSet, aclTwoNormalUserPermissionSet);

		// Verify the set of permissions for /acl_three/role/read-only-user are exactly what we expect
		BitSet aclThreeReadOnlyUserPermissionSet = aclThreeReadOnlyUser.getPermissions();
		BitSet aclThreeExpectedPermissionSet = new BitSet(aclThreeAcl.getHighestPermissionId());
		aclThreeExpectedPermissionSet.set(12);
		aclThreeExpectedPermissionSet.set(13);
		aclThreeExpectedPermissionSet.set(15);
		//TODO: Fix this after Roles have been fixed
//		assertEquals("Expected: Permissions for /acl_three/role/read-only-user = " + aclThreeExpectedPermissionSet + ", Found: " + aclThreeReadOnlyUserPermissionSet, aclThreeExpectedPermissionSet, aclThreeReadOnlyUserPermissionSet);

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

		Role readOnlyUser = null;
		boolean exceptionThrown = true;
		try {
			readOnlyUser = aclThree.getRole("/acl_three/role/readonlyuser");
			exceptionThrown = false;
		} catch (RoleNotFoundException e) {
			assertTrue("No exception thrown on attempt to get invalid role", exceptionThrown);
			assertEquals("/acl_three/role/readonlyuser", e.getReference());
			assertEquals(aclThree, e.getAccessControlList());
		}

		assertTrue(exceptionThrown);

		try {
			readOnlyUser = aclThree.getRole("/acl_three/role/read-only-user");
			exceptionThrown = false;
		} catch (RoleNotFoundException e) {
			assertFalse("Exception thrown on attempt to get _valid_ role", exceptionThrown);
			assertEquals("/acl_three/role/read-only-user", e.getReference());
			assertEquals(aclThree, e.getAccessControlList());
		}

		assertFalse(exceptionThrown);
		exceptionThrown = true;

		Permission viewOrder = null;

		try {
			viewOrder = aclThree.getPermission("/acl_three/app/orders/vieworder");
			exceptionThrown = false;
		} catch (PermissionNotFoundException e) {
			assertTrue(exceptionThrown);
			assertEquals("/acl_three/app/orders/vieworder", e.getReference());
			assertEquals(aclThree, e.getAccessControlList());
		}

		assertTrue(exceptionThrown);

		try {
			viewOrder = aclThree.getPermission("/acl_three/app/orders/view_order");
			exceptionThrown = false;
		} catch (PermissionNotFoundException e) {
			assertFalse(exceptionThrown);
			assertEquals("/acl_three/app/orders/view_order", e.getReference());
			assertEquals(aclThree, e.getAccessControlList());
		}

		assertFalse(exceptionThrown);

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

	    // ValueContext Created User...
        ValueContext vcOne = ValueSources.getInstance().createDefaultValueContext();
		assertNotNull(vcOne);

		// Test ValueContext...
	    DefaultValueContext dvc = (DefaultValueContext) vcOne;
	    assertFalse(dvc.inMaintenanceMode());
	    assertFalse(dvc.isAntBuildEnvironment());
	    assertFalse(dvc.isDemonstrationEnvironment());
	    assertFalse(dvc.isDevelopmentEnvironment());
	    assertFalse(dvc.isProductionEnvironment());
	    assertFalse(dvc.isTestEnvironment());
	    assertFalse(dvc.isTrainingEnvironment());
	    assertFalse(dvc.withinACE());

	    assertNull(dvc.getAccessControlListsManager());
	    assertNull(dvc.getConfigurationsManager());

	    dvc.setAttribute("test-attribute", new Float(3.14159));
	    assertNull(dvc.getAttribute("test-attribute"));
	    dvc.removeAttribute("test-attribute");

        AuthenticatedUser userOne = vcOne.createAuthenticatedUser();
	    assertNotNull(userOne);

        userOne.setUserId("admin");
	    assertEquals("admin", userOne.getUserId());
	    assertEquals("admin", userOne.getName());

        userOne.setUserName("Administrator");
	    assertEquals("Administrator", userOne.getUserName());

		// Independently created User...
	    ValueContext vcTwo = ValueSources.getInstance().createDefaultValueContext();
	    AuthenticatedUser userTwo = new BasicAuthenticatedUser("Administrator", "admin", "Administrative Services, Inc.", "admin-org");

		assertEquals("admin", userTwo.getUserId());
		assertEquals("Administrator", userTwo.getUserName());

	    //TODO: Implement setAuthenticatedUser (and getAuthenticatedUser) and _then_ enable these lines
/*
	    vcTwo.setAuthenticatedUser(userTwo);
	    assertEquals(userTwo, vcTwo.getAuthenticatedUser());
*/

	    // Alternate constructor
	    ValueContext vcThree = ValueSources.getInstance().createDefaultValueContext();
	    AuthenticatedUser userThree = new BasicAuthenticatedUser("Administrator", "admin");

	    assertEquals("admin", userThree.getUserId());
	    assertEquals("Administrator", userThree.getUserName());

		//TODO: Implement setAuthenticatedUser (and getAuthenticatedUser) and _then_ enable these lines
/*
	    vcThree.setAuthenticatedUser(userThree);
	    assertEquals(userThree, vcThree.getAuthenticatedUser());
*/

	    userThree.setAttribute("pi", new Float(3.14159));
	    assertEquals(new Float(3.14159), userThree.getAttribute("pi"));
	    userThree.removeAttribute("pi");
	    assertNull(userThree.getAttribute("pi"));
    }

	public void testAuthenticatedUsersPermissions() throws DataModelException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException, IOException, PermissionNotFoundException, RoleNotFoundException
	{
		AccessControlListsComponent aclc =
		        (AccessControlListsComponent) XdmComponentFactory.get(
		                AccessControlListsComponent.class,
		                new Resource(AccessControlListTest.class, RESOURCE_NAME_THREE),
		                XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		// Verify _something_ was loaded...
		assertNotNull(aclc);

        AccessControlListsManager aclm = aclc.getItems();
		assertNotNull(aclm);

		AccessControlList defaultAcl = aclm.getDefaultAccessControlList();
		assertNotNull(defaultAcl);

		assertEquals(7, defaultAcl.getPermissionsByName().size());

		// Independently created User...
	    ValueContext vcTwo = ValueSources.getInstance().createDefaultValueContext();
	    AuthenticatedUser userTwo = new BasicAuthenticatedUser("Administrator", "admin", "Administrative Services, Inc.", "admin-org");

		// Experiment with AuthenticatedUsers and Roles/Permissions
        assertNull(userTwo.getUserRoleNames());

		String[] userTwoRoles = { "/acl/role/normal-user" };
		userTwo.setRoles(aclm, userTwoRoles);
		assertEquals(userTwoRoles, userTwo.getUserRoleNames());

		Role normalUser = defaultAcl.getRole("/acl/role/normal-user");
		assertEquals(2, normalUser.getId());
		BitSet aclNormalUserPermissionSet = new BitSet(defaultAcl.getHighestPermissionId());
		aclNormalUserPermissionSet.set(1);
		aclNormalUserPermissionSet.set(2);
		aclNormalUserPermissionSet.set(3);
		aclNormalUserPermissionSet.set(4);
		aclNormalUserPermissionSet.set(5);

		assertEquals(normalUser.getPermissions(), userTwo.getUserPermissions());
		assertEquals(aclNormalUserPermissionSet, userTwo.getUserPermissions());

		userTwo.setPermissions(aclm, new String[] { "/acl/app/orders/create_order", "/acl/app/orders/view_order", "/acl/app/orders/delete_order" });
		BitSet userTwoPermissionSet = new BitSet(defaultAcl.getHighestPermissionId());
		userTwoPermissionSet.set(3);
		userTwoPermissionSet.set(5);
		userTwoPermissionSet.set(6);

		assertFalse(normalUser.getPermissions().equals(userTwo.getUserPermissions()));
		assertEquals(userTwoPermissionSet, userTwo.getUserPermissions());
	}

	public void testAuthenticatedUsersPermissionErrors() throws DataModelException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException, IOException, PermissionNotFoundException, RoleNotFoundException
	{
		AccessControlListsComponent aclc =
		        (AccessControlListsComponent) XdmComponentFactory.get(
		                AccessControlListsComponent.class,
		                new Resource(AccessControlListTest.class, RESOURCE_NAME_THREE),
		                XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

		// Verify _something_ was loaded...
		assertNotNull(aclc);

        AccessControlListsManager aclm = aclc.getItems();
		assertNotNull(aclm);

		AccessControlList defaultAcl = aclc.getItems().getDefaultAccessControlList();
		assertNotNull(defaultAcl);

		// Independently created User...
	    ValueContext vcTwo = ValueSources.getInstance().createDefaultValueContext();
	    AuthenticatedUser userTwo = new BasicAuthenticatedUser("Administrator", "admin", "Administrative Services, Inc.", "admin-org");

		// Experiment with AuthenticatedUsers and Roles/Permissions
        assertNull(userTwo.getUserRoleNames());

		boolean exceptionThrown = true;

		try {
			userTwo.setRoles(aclm, new String[] { "/acl/role/invalid-user" });
			exceptionThrown = false;
		} catch (RoleNotFoundException e) {
			assertTrue(exceptionThrown);
		}

		assertTrue(exceptionThrown);

		try {
			userTwo.setPermissions(aclm, new String[] { "/acl/app/orders/invalidate_order" });
			exceptionThrown = false;
		} catch (PermissionNotFoundException e) {
			assertTrue(exceptionThrown);
		}

		assertTrue(exceptionThrown);

		// Verify proper behavior upon setting null roles or permissions
		userTwo.setRoles(aclm, null);
		assertNull(userTwo.getUserRoleNames());
		assertNull(userTwo.getUserPermissions());

		userTwo.setPermissions(aclm, null);
		assertNull(userTwo.getUserPermissions());

		String[] userTwoRoles = { "/acl/role/normal-user" };
		userTwo.setRoles(aclm, userTwoRoles);
		assertEquals(userTwoRoles, userTwo.getUserRoleNames());

		Role normalUser = defaultAcl.getRole("/acl/role/normal-user");
//		BitSet aclNormalUserPermissionSet = normalUser.getChildPermissions();
		BitSet aclNormalUserPermissionSet = new BitSet(defaultAcl.getHighestPermissionId());
		aclNormalUserPermissionSet.set(1);
		aclNormalUserPermissionSet.set(2);
		aclNormalUserPermissionSet.set(3);
		aclNormalUserPermissionSet.set(4);
		aclNormalUserPermissionSet.set(5);

		assertEquals(aclNormalUserPermissionSet, normalUser.getPermissions());

		assertEquals(normalUser.getPermissions(), userTwo.getUserPermissions());
		assertEquals(aclNormalUserPermissionSet, userTwo.getUserPermissions());

		userTwo.setRoles(aclm, userTwoRoles);
		assertTrue(userTwo.hasAnyPermission(aclm, new String[] { "/acl/app/orders/create_order", "/acl/app/orders/view_order", "/acl/app/orders/edit_order" }));
		assertTrue(userTwo.hasAnyPermission(aclm, new String[] { "/acl/app/orders/delete_order", "/acl/app/orders/view_order", "/acl/app/orders/edit_order" }));
		assertFalse(userTwo.hasPermission(aclm, "/acl/app/orders/delete_order"));
		assertFalse(userTwo.hasAnyPermission(aclm, new String[] { "/acl/app/orders/delete_order" }));

		try {
			userTwo.hasAnyPermission(aclm, new String[] { "/acl/app/orders/invalidate_order", "/acl/app/orders/misplace_order" });
			exceptionThrown = false;
		} catch (PermissionNotFoundException e) {
			assertTrue(exceptionThrown);
		}

		assertTrue(exceptionThrown);

		try {
			userTwo.hasPermission(aclm, "/acl/app/orders/invalidate_order");
			exceptionThrown = false;
		} catch (PermissionNotFoundException e) {
			assertTrue(exceptionThrown);
		}

		assertTrue(exceptionThrown);

		assertTrue(userTwo.hasPermission(aclm, "/acl/app/orders/view_order"));
	}

}
