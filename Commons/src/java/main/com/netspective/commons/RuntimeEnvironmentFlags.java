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
package com.netspective.commons;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;

public class RuntimeEnvironmentFlags extends XdmBitmaskedFlagsAttribute
{
    public static final int ANT_BUILD = 1;
    public static final int DEVELOPMENT = ANT_BUILD * 2;
    public static final int TESTING = DEVELOPMENT * 2;
    public static final int TRAINING = TESTING * 2;
    public static final int STAGING = TRAINING * 2;
    public static final int PRODUCTION = STAGING * 2;
    public static final int SERVER = PRODUCTION * 2;
    public static final int WORKSTATION = SERVER * 2;
    public static final int DEMONSTRATION = WORKSTATION * 2;

    /**
     * Checks whether the current runtime environment, based on the given value source, is under maintenance.
     * Maintenance mode is usually reserved for only production environments.
     */
    public static final int UNDERGOING_MAINTENANCE = DEMONSTRATION * 2;

    /**
     * Checks whether the current environment is running in "framework debugging" mode (where the framework
     * itself it being development, as opposed to an application using the framework).
     */
    public static final int FRAMEWORK_DEVELOPMENT = UNDERGOING_MAINTENANCE * 2;

    public static final FlagDefn[] FLAG_DEFNS = new FlagDefn[]
    {
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "ANT_BUILD", ANT_BUILD),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "DEVELOPMENT", DEVELOPMENT),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "TESTING", TESTING),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "TRAINING", TRAINING),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "STAGING", STAGING),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "PRODUCTION", PRODUCTION),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "DEMONSTRATION", DEMONSTRATION),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "SERVER", SERVER),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "WORKSTATION", WORKSTATION),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "UNDERGOING_MAINTENANCE", UNDERGOING_MAINTENANCE),
        new FlagDefn(RuntimeEnvironmentFlags.ACCESS_XDM, "FRAMEWORK_DEVELOPMENT", FRAMEWORK_DEVELOPMENT),
    };

    public RuntimeEnvironmentFlags()
    {
    }

    public FlagDefn[] getFlagsDefns()
    {
        return FLAG_DEFNS;
    }

    public boolean isDevelopment()
    {
        return flagIsSet(DEVELOPMENT);
    }

    public boolean isFrameworkDevelopment()
    {
        return flagIsSet(FRAMEWORK_DEVELOPMENT);
    }

    public boolean isDevelopmentOrTesting()
    {
        return flagIsSet(DEVELOPMENT | TESTING | FRAMEWORK_DEVELOPMENT);
    }

    public boolean isProductionOrTraining()
    {
        return flagIsSet(PRODUCTION | TRAINING);
    }

    public boolean isProductionOrStaging()
    {
        return flagIsSet(PRODUCTION | STAGING);
    }

    public boolean isProductionOrTrainingOrStaging()
    {
        return flagIsSet(PRODUCTION | TRAINING | STAGING);
    }

    public boolean isStaging()
    {
        return flagIsSet(STAGING);
    }

    public boolean isProduction()
    {
        return flagIsSet(PRODUCTION);
    }
}

