package com.netspective.commons.value;

import junit.framework.TestCase;
import com.netspective.commons.security.BasicAuthenticatedUser;

/**
 * $Id: DefaultValueContextTest.java,v 1.1 2003-03-25 17:52:45 shahbaz.javeed Exp $
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

		assertFalse(vc.isInMaintenanceMode());
		vc.setMaintenanceMode(true);
		assertFalse(vc.isInMaintenanceMode());

        assertFalse(vc.isInConsoleMode());
		vc.setConsoleMode(true);
		assertTrue(vc.isInConsoleMode());

		assertNull(vc.getContextLocation());
		vc.setContextLocation(new String ("Test"));
		assertNull(vc.getContextLocation());

	    assertFalse(vc.isInMaintenanceMode());
	    assertFalse(vc.isAntBuildEnvironment());
	    assertFalse(vc.isDemonstrationEnvironment());
	    assertFalse(vc.isDevelopmentEnvironment());
	    assertFalse(vc.isProductionEnvironment());
	    assertFalse(vc.isTestEnvironment());
	    assertFalse(vc.isTrainingEnvironment());

	    assertNull(vc.getAccessControlListsManager());
	    assertNull(vc.getConfigurationsManager());

	    vc.setAttribute("test-attribute", new Float(3.14159));
	    assertNull(vc.getAttribute("test-attribute"));
	    vc.removeAttribute("test-attribute");

		assertEquals(0, vc.getFlags());
		assertFalse(vc.hasError());

		vc.setFlag(DefaultValueContext.VCFLAG_HASERROR);
		assertTrue(vc.hasError());
		assertEquals(DefaultValueContext.VCFLAG_HASERROR, vc.getFlags());

		vc.setFlag(2);
		assertTrue(vc.hasError());
		assertEquals(3, vc.getFlags());

		vc.clearFlag(DefaultValueContext.VCFLAG_HASERROR);
		assertFalse(vc.hasError());
		assertEquals(2, vc.getFlags());

		assertEquals(0, vc.getResultCode());
		vc.setResultCode(currTime);
		assertEquals(currTime, vc.getResultCode());
	}
}
