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
package com.netspective.commons.xdm;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.netspective.commons.Product;

public class XdmProduct implements Product
{
    private String productName = "Unspecified";
    private String productId = "??";

    private Class buildLogClass;
    private int releaseNumber = 0;
    private int versionMajor = 0;
    private int versionMinor = 0;
    private int buildNumber = 0;

    private Date buildDate = null;

    public XdmProduct()
    {
    }

    public String getProductId()
    {
        return productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public final int getReleaseNumber()
    {
        return releaseNumber;
    }

    public final int getVersionMajor()
    {
        return versionMajor;
    }

    public final int getVersionMinor()
    {
        return versionMinor;
    }

    public final int getBuildNumber()
    {
        return buildNumber;
    }

    public Class getBuildLogClass()
    {
        return buildLogClass;
    }

    public void setBuildLogClass(Class buildLogClass) throws IllegalAccessException, InstantiationException, NoSuchFieldException
    {
        this.buildLogClass = buildLogClass;

        Object o = buildLogClass.newInstance();
        Field buildNumber = buildLogClass.getField("BUILD_NUMBER");
        Field buildDate = buildLogClass.getField("BUILD_DATE");
        setBuildNumber(buildNumber.getInt(o));

        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try
        {
            setBuildDate(dateFormat.parse(buildDate.get(o).toString()));
        }
        catch(ParseException e)
        {
            throw new RuntimeException("Unable to parse build date '" + buildDate.get(o) + "' using format '" + dateFormat + "'");
        }
    }

    public final String getBuildFilePrefix(boolean includeBuildNumber)
    {
        String filePrefix = productId + "-" + releaseNumber + "." + versionMajor + "." + versionMinor;
        if(includeBuildNumber)
            filePrefix = filePrefix + "_" + buildNumber;
        return filePrefix;
    }

    public final String getVersion()
    {
        return releaseNumber + "." + versionMajor + "." + versionMinor;
    }

    public final String getVersionAndBuild()
    {
        return "Version " + getVersion() + " Build " + buildNumber;
    }

    public final String getProductBuild()
    {
        return productName + " Version " + getVersion() + " Build " + buildNumber;
    }

    public final String getVersionAndBuildShort()
    {
        return "v" + getVersion() + " b" + buildNumber;
    }

    public Date getBuildDate()
    {
        return buildDate;
    }

    public void setBuildDate(Date buildDate)
    {
        this.buildDate = buildDate;
    }

    public void setBuildNumber(int buildNumber)
    {
        this.buildNumber = buildNumber;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public void setReleaseNumber(int releaseNumber)
    {
        this.releaseNumber = releaseNumber;
    }

    public void setVersionMajor(int versionMajor)
    {
        this.versionMajor = versionMajor;
    }

    public void setVersionMinor(int versionMinor)
    {
        this.versionMinor = versionMinor;
    }
}
