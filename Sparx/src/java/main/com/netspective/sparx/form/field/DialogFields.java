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
 * $Id: DialogFields.java,v 1.8 2003-10-20 15:56:37 shahid.shah Exp $
 */

package com.netspective.sparx.form.field;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContextBeanMemberInfo;

public class DialogFields
{
    private Dialog owner;
    private DialogField parent;
    private List fields = new ArrayList();
    private List namesOnly = new ArrayList();
    private Map mapByName = new HashMap();
    private Map mapByQualifiedName = new HashMap();

    public DialogFields(Dialog owner)
    {
        this.owner = owner;
    }

    public DialogFields(DialogField parent)
    {
        this(parent.getOwner());
        this.parent = parent;
    }

    public List getFieldsList()
    {
        return fields;
    }

    protected void storeNames(DialogField field)
    {
        namesOnly.add(field.getName());
        mapByName.put(field.getNameForMapKey(), field);
        mapByQualifiedName.put(field.getQualifiedName(), field);

        // make sure to register field in both parent and owner so fields can be found just by querying the hierarchy
        owner.getFields().mapByQualifiedName.put(field.getQualifiedName(), field);
        if(parent != null)
            parent.getChildren().mapByQualifiedName.put(field.getQualifiedName(), field);
    }

    public int add(DialogField field)
    {
        if(parent == null)
            field.setQualifiedName(field.getName());
        else
            field.setQualifiedName(parent.getQualifiedName() + "." + field.getName());

        fields.add(field);
        storeNames(field);
        return fields.size() - 1; // this was the index used to add the field
    }

    public boolean contains(DialogField field)
    {
        return fields.indexOf(field) != -1;
    }

    public DialogField getFirst()
    {
        return get(0);
    }

    public List getOnlyNames()
    {
        return namesOnly;
    }

    public String getOnlyNames(String delimiter)
    {
        StringBuffer result = new StringBuffer();
        List names = getOnlyNames();
        for(int i = 0; i < names.size(); i++)
        {
            if(i > 0)
                result.append(delimiter);
            result.append(names.get(i));
        }
        return result.toString();
    }

    public String getNamesDelimited(String delimiter)
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < namesOnly.size(); i++)
        {
            if(i > 0)
                sb.append(delimiter);
            sb.append(namesOnly.get(i));
        }
        return sb.toString();
    }

    public DialogField getSole()
    {
        if(size() != 1)
            throw new RuntimeException("Only a single field is expected in this collection (not "+ size() +"): " + this);
        return (DialogField) fields.get(0);
    }

    public DialogField get(int i)
    {
        return (DialogField) fields.get(i);
    }

    public DialogField getByName(String name)
    {
        return (DialogField) mapByName.get(DialogField.translateFieldNameForMapKey(name));
    }

    public DialogField getByQualifiedName(String name)
    {
        return (DialogField) mapByQualifiedName.get(name);
    }

    public DialogFields getByNames(String names, String delimiter)
    {
        DialogFields sublist = parent != null ? new DialogFields(parent) : new DialogFields(owner);
        StringTokenizer st = new StringTokenizer(names, delimiter);
        while(st.hasMoreTokens())
        {
            String colName = st.nextToken().trim();
            sublist.add(getByName(colName));
        }
        return sublist;
    }

    public void clearFlags(long flags)
    {
        for(int i = 0; i < fields.size(); i++)
            ((DialogField) fields.get(i)).getFlags().clearFlag(flags);
    }

    public void setFlags(long flags)
    {
        for(int i = 0; i < fields.size(); i++)
            ((DialogField) fields.get(i)).getFlags().setFlag(flags);
    }

    public int totalSize()
    {
        int result = 0;
        for(int i = 0; i < fields.size(); i++)
        {
            DialogFields children = get(i).getChildren();
            result += children != null ? (get(i).getChildren().totalSize() + 1) : 1;
        }
        return result;
    }

    public int size()
    {
        return fields.size();
    }

    public boolean requiresMultiPartEncoding()
    {
        // if any child requires multi part encoding, then return true (this will take of things recursively)
        for(int i = 0; i < fields.size(); i++)
            if(((DialogField) fields.get(i)).requiresMultiPartEncoding())
                return true;

        return false;
    }

    /**
     * Produces Java code when a custom DialogContext is created
     * The default method produces nothing; all the subclasses must define what they need.
     */
    public DialogContextBeanMemberInfo getDialogContextBeanMemberInfo(DialogContextBeanMemberInfo parentMI)
    {
        for(int i = 0; i < size(); i++)
        {
            DialogField field = get(i);
            DialogContextBeanMemberInfo childMI = field.getDialogContextBeanMemberInfo();
            if (childMI == null)
                continue;

            String[] childImports = childMI.getImportModules();
            if (childImports != null)
            {
                for (int m = 0; m < childImports.length; m++)
                    parentMI.addImportModule(childImports[m]);
            }

            parentMI.addJavaCode(childMI.getCode());
        }

        return parentMI;
    }

    public void finalizeContents()
    {
        for(int i = 0; i < fields.size(); i++)
        {
            DialogField field = get(i);
            field.finalizeContents();
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < fields.size(); i++)
        {
            sb.append("  ");
            sb.append(fields.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
