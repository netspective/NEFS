package com.netspective.commons.value;

import junit.framework.TestCase;
import com.netspective.commons.security.BasicAuthenticatedUser;

/**
 * $Id: DefaultValueContextTest.java,v 1.2 2003-05-16 21:21:32 shahid.shah Exp $
 */
public class DefaultValueContextTest extends TestCase
{
	public void testDefaultValueContext()
	{
		DefaultValueContext vc = new DefaultValueContext();

        long currTime = System.currentTimeMillis();
		while (System.currentTimeMillis() == currTime);
		currTime = System.currentTimeMillis();

		// Test ValueContext...
		assertTrue(vc.getCreationTime() < currTime);

		// Currently unimplemented?
		assertNull(vc.getAuthenticatedUser());
        vc.setAuthenticatedUser(new BasicAuthenticatedUser());
		assertNull(vc.getAuthenticatedUser());

		assertNull(vc.getContextLocation());
		vc.setContextLocation(new String ("Test"));
		assertNull(vc.getContextLocation());

	    assertNull(vc.getAccessControlListsManager());
	    assertNull(vc.getConfigurationsManager());

	    vc.setAttribute("test-attribute", new Float(3.14159));
	    assertNull(vc.getAttribute("test-attribute"));
	    vc.removeAttribute("test-attribute");

        assertNotNull(vc.getEnvironmentFlags());
	}
}
