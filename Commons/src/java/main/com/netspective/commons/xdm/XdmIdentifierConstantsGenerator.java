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
 * $Id: XdmIdentifierConstantsGenerator.java,v 1.4 2004-08-09 22:14:28 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.netspective.commons.text.TextUtils;

public class XdmIdentifierConstantsGenerator
{
    private File rootPath;
    private String rootPkgAndClassName;
    private Map identifiers;
    private char subPackageSeparator = '.';

    public XdmIdentifierConstantsGenerator(File rootPath, String rootPkgAndClassName, Map identifiers)
    {
        this.rootPath = rootPath;
        this.rootPkgAndClassName = rootPkgAndClassName;

        this.identifiers = identifiers;
    }

    public XdmIdentifierConstantsGenerator(File rootPath, String rootPkgAndClassName, Map identifiers, char subPackageSeparator)
    {
        this(rootPath, rootPkgAndClassName, identifiers);
        this.subPackageSeparator = subPackageSeparator;
    }

    public void generateCode(String subPkgAndClassName, Map ids) throws IOException
    {
        TextUtils textUtils = TextUtils.getInstance();
        String fullPkgName, className;
        File file;

        if(subPkgAndClassName == null)
        {
            fullPkgName = textUtils.getPackageName(rootPkgAndClassName, '.');
            className = textUtils.getClassNameWithoutPackage(rootPkgAndClassName, '.');
            file = new File(rootPath, rootPkgAndClassName.replace('.', '/') + ".java");
        }
        else
        {
            String subPkgName = textUtils.getPackageName(subPkgAndClassName, subPackageSeparator);
            if(subPkgName == null)
                fullPkgName = rootPkgAndClassName.toLowerCase();
            else
                fullPkgName = (rootPkgAndClassName + subPackageSeparator + subPkgName).toLowerCase();
            className = textUtils.getClassNameWithoutPackage(subPkgAndClassName, subPackageSeparator);
            String fullClassSpec = fullPkgName + subPackageSeparator + textUtils.xmlTextToJavaIdentifier(className, true);
            file = new File(rootPath, fullClassSpec.replace(subPackageSeparator, '/') + ".java");
        }

        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);

        writer.write("\n/* this file is generated by "+ XdmIdentifierConstantsGenerator.class.getName() +", do not modify (you can extend it, though) */\n\n");
        if(fullPkgName != null)
            writer.write("package " + textUtils.xmlTextToJavaPackageName(fullPkgName.replace(subPackageSeparator, '.')) + ";\n\n");

        writer.write("public interface " + textUtils.xmlTextToJavaIdentifier(className, true) + "\n");
        writer.write("{\n");

        for(Iterator i = ids.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) i.next();

            String javaIdentifier = (String) entry.getKey();
            String constant = textUtils.xmlTextToJavaConstantTrimmed(subPkgAndClassName != null ? javaIdentifier.substring(subPkgAndClassName.length()+1) : javaIdentifier);

            Object constantValue = entry.getValue();
            String constantType = "UNKNOWN_TYPE";
            if(constantValue instanceof String)
            {
                constantValue = new String("\"" + entry.getValue() + "\"");
                constantType = "String";
            }
            else if(constantValue instanceof Integer)
                constantType = "int";

            if(constant.length() > 0)
                writer.write("    static public final "+ constantType +" " + constant + " = " + constantValue + ";\n");
            else
                writer.write("    // static public final "+ constantType +" " + constant + " = " + constantValue + ";\n");
        }

        writer.write("}\n");
        writer.close();
    }

    public void generateCode() throws IOException
    {
        Map idsWithNoPackages = new TreeMap();
        Map idsInPackages = new HashMap();

        for(Iterator i = identifiers.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) i.next();

            String javaIdentifer = (String) entry.getKey();
            if(javaIdentifer.indexOf(subPackageSeparator) > 0)
            {
                String packageName = javaIdentifer.substring(0, javaIdentifer.lastIndexOf(subPackageSeparator));
                Map ids = (Map) idsInPackages.get(packageName);
                if(ids == null)
                {
                    ids = new TreeMap();
                    idsInPackages.put(packageName, ids);
                }
                ids.put(entry.getKey(), entry.getValue());
            }
            else if(javaIdentifer.length() > 0)
                idsWithNoPackages.put(entry.getKey(), entry.getValue());
        }

        if(idsWithNoPackages.size() > 0)
            generateCode(null, idsWithNoPackages);

        Iterator idsInPackage = idsInPackages.entrySet().iterator();
        while(idsInPackage.hasNext())
        {
            Map.Entry entry = (Map.Entry) idsInPackage.next();
            String subPackageName = ((String) entry.getKey());
            Map ids = (Map) entry.getValue();
            generateCode(subPackageName, ids);
        }
    }

    public String getRootPkgAndClassName()
    {
        return rootPkgAndClassName;
    }

    public File getRootPath()
    {
        return rootPath;
    }
}

