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
package com.netspective.sparx;

public class BuildLog
{
    public static final int BUILD_NUMBER = 16;
    public static final String BUILD_HOST_NAME = "Atlantis";
    public static final String BUILD_HOST_IP = "192.168.0.2";
    public static final String BUILD_DATE = "Sat Aug 14 18:10:55 EDT 2004";

    public static final String BUILD_OS_NAME = "Windows XP";
    public static final String BUILD_OS_VERSION = "5.1";

    public static final String BUILD_JAVA_VERSION = "1.4.2_05";
    public static final String BUILD_JAVA_VENDOR = "Sun Microsystems Inc.";

    public static final String BUILD_VM_NAME = "Java HotSpot(TM) Client VM";
    public static final String BUILD_VM_VERSION = "1.4.2_05-b04";
    public static final String BUILD_VM_VENDOR = "Sun Microsystems Inc.";

    public static final String[] BUILD_CLASS_PATH = new String[]{
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-antlr.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-apache-bsf.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-apache-resolver.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-commons-logging.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-commons-net.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-icontract.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jai.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jakarta-bcel.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jakarta-log4j.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jakarta-oro.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jakarta-regexp.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-javamail.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jdepend.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jmf.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-jsch.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-junit.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-launcher.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-netrexx.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-nodeps.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-starteam.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-stylebook.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-swing.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-trax.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-vaj.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-weblogic.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-xalan1.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-xalan2.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant-xslp.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/ant.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/xercesImpl.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/ant/lib/xml-apis.jar",
        "C:/java/j2sdk1.4.2_05/lib/tools.jar",
        "C:/Program Files/JetBrains/IntelliJ-IDEA-4.5/lib/idea_rt.jar"
    };
}
