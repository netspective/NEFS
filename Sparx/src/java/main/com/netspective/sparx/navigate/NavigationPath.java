/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: NavigationPath.java,v 1.4 2003-04-07 17:13:55 shahid.shah Exp $
 */

package com.netspective.sparx.navigate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;

public class NavigationPath
{
    static public final String PATH_SEPARATOR = "/";

    public static final Flags.FlagDefn[] FLAG_DEFNS = new XdmBitmaskedFlagsAttribute.FlagDefn[]
    {
    };

    public class Flags extends XdmBitmaskedFlagsAttribute
    {
        public static final int START_CUSTOM = 1;

        public XdmBitmaskedFlagsAttribute.FlagDefn[] getFlagsDefns()
        {
            return FLAG_DEFNS;
        }
    }

    public class State
    {
        private Flags flags;

        public State()
        {
            this.flags = (NavigationPath.Flags) NavigationPath.this.getFlags().cloneFlags();
        }

        public Flags getFlags()
        {
            return flags;
        }

        public NavigationPath getPath()
        {
            return NavigationPath.this;
        }
    }

    private NavigationTree owner;
    private NavigationPath parent;
    private Flags flags;
    private String qualifiedName;
    private String name;
    private List childrenList = new ArrayList();
    private Map childrenMap = new HashMap();
    private Map descendantsByQualifiedName = new HashMap();
    private Map ancestorMap = new HashMap();
    private List ancestorsList = new ArrayList();
    private NavigationPath defaultChild;
    private boolean defaultChildOfParent;
    private int maxChildLevel;
    private int level;

    public NavigationPath()
    {
        flags = createFlags();
    }

    public State constructState()
    {
        return new State();
    }

    public String getQualifiedName()
    {
        if(null == qualifiedName)
        {
            StringBuffer sb = new StringBuffer();
            if(parent != null)
                sb.append(parent.getQualifiedName());

            if(sb.length() == 0 || sb.charAt(sb.length()-1) != '/')
                sb.append(PATH_SEPARATOR);

            sb.append(getName());

            setQualifiedName(sb.toString());
        }

	    return qualifiedName;
    }

    public void setQualifiedName(String qName)
    {
        qualifiedName = qName;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public NavigationTree getOwner()
    {
        return owner;
    }

    public void setOwner(NavigationTree value)
    {
        owner = value;
    }

    public NavigationPath getParent()
    {
        return parent;
    }

    public void setParent(NavigationPath value)
    {
        if (value != this)
        {
            parent = value;
            setOwner(parent.getOwner());
            setLevel(parent.getLevel() + 1);
            if(defaultChildOfParent)
                parent.setDefaultChild(this);
            generateAncestorList();
        }
        else
            parent = null;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
        setMaxChildLevel(level);
        for (NavigationPath activeParent = getParent(); activeParent != null;)
        {
            activeParent.setMaxChildLevel(level);
            activeParent = activeParent.getParent();
        }
    }

    public int getMaxChildLevel()
    {
        return maxChildLevel;
    }

    public void setMaxChildLevel(int maxChildLevel)
    {
        if (maxChildLevel > this.maxChildLevel)
            this.maxChildLevel = maxChildLevel;
        owner.setMaxLevel(level);
    }

    public Flags createFlags()
    {
        return new Flags();
    }

    public Flags getFlags()
    {
        return flags;
    }

    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

    public void setFlagRecursively(long flag)
    {
        flags.setFlag(flag);
        if (childrenList.size() > 0)
        {
            Iterator i = childrenList.iterator();
            while (i.hasNext())
                ((NavigationPath) i.next()).getFlags().setFlag(flag);
        }
    }

    public void clearFlagRecursively(long flag)
    {
        flags.clearFlag(flag);
        if (childrenList.size() > 0)
        {
            Iterator i = childrenList.iterator();
            while (i.hasNext())
                ((NavigationPath) i.next()).getFlags().clearFlag(flag);
        }
    }

    /**
     * Returns the Map that contains all of its sibilings including itself.  It is basically obtained by getting a
     * reference to the parent and then get a map of all of its children.
     * @return  Map  A map object containing NavigationPath objects that represent the sibilings of the current object.
     */
    public Map getSibilingMap()
    {
        if (parent != null)
            return parent.getChildrenMap();

        return null;
    }

    /**
     * Returns the List that contains all of its sibilings including itself.  It is basically obtained by getting a
     * reference to the parent and then get a list of all of its children.
     * @return  List  A list object containing NavigationPath objects that represent the sibilings of the current object.
     */
    public List getSibilingList()
    {
        if (parent != null)
            return parent.getChildrenList();

        return null;
    }

    public void registerChild(NavigationPath path)
    {
        descendantsByQualifiedName.put(path.getQualifiedName(), path);
        if (parent != null)
            parent.registerChild(path);
        owner.register(path);
    }

    public void unregisterChild(NavigationPath path)
    {
        descendantsByQualifiedName.remove(path.getQualifiedName());
        if (parent != null)
            parent.unregisterChild(path);
        owner.unregister(path);
    }

    public void appendChild(NavigationPath path)
    {
        path.setParent(this);
        childrenList.add(path);
        childrenMap.put(path.getName(), path);
        registerChild(path);
    }

    public void removeChild(NavigationPath path)
    {
        childrenList.remove(path);
        childrenMap.remove(path.getName());
        unregisterChild(path);
    }

    public void removeAllChildren()
    {
        if(childrenList.size() == 0)
            return;

        NavigationPath[] children = (NavigationPath[]) childrenList.toArray(new NavigationPath[childrenList.size()]);
        for(int i = 0; i < children.length; i++)
            removeChild(children[i]);
    }

    /**
     * Get a child by its ID
     * @param id
     * @return NavigationPath
     */
    public NavigationPath getChildByName(String id)
    {
        return (NavigationPath) childrenMap.get(id);
    }

    public List getChildrenList()
    {
        return childrenList;
    }

    public Map getChildrenMap()
    {
        return childrenMap;
    }

    public NavigationPath getDefaultChild()
    {
        return defaultChild;
    }

    public void setDefaultChild(NavigationPath defaultChild)
    {
        this.defaultChild = defaultChild;
    }

    public boolean isDefaultChildOfParent()
    {
        return defaultChildOfParent;
    }

    public void setDefault(boolean defaultChildOfParent)
    {
        this.defaultChildOfParent = defaultChildOfParent;
        if(defaultChildOfParent && getParent() != null)
           getParent().setDefaultChild(this);
    }

    public Map getAncestorMap()
    {
        return ancestorMap;
    }

    public void setAncestorMap(Map ancestorMap)
    {
        this.ancestorMap = ancestorMap;
    }

    public List getAncestorsList()
    {
        return ancestorsList;
    }

    public void setAncestorsList(List ancestorsList)
    {
        this.ancestorsList = ancestorsList;
    }

    /**
     * Populates the ancestorMap and ancestorList.  It looks at its parent and adds a refence to it in the collections.
     * It then keeps doing that, but now looking at its parent's parent until it returns null.  It then reverses the list
     * to maintain a 0=top structure.
     */
    protected void generateAncestorList()
    {
        NavigationPath activePath = this.getParent();
        if (getParent() != null)
        {
            List ancestorListReversed = new ArrayList();
            Map ancestorMap = new HashMap();
            while (activePath != null)
            {
                ancestorMap.put(activePath.getQualifiedName(), activePath);
                ancestorListReversed.add(activePath);
                activePath = activePath.getParent();
            }
            setAncestorMap(ancestorMap);

            List ancestorList = new ArrayList();
            for (int i = ancestorListReversed.size() - 1; i >= 0; i--)
            {
                NavigationPath NavigationPath = (NavigationPath) ancestorListReversed.get(i);
                ancestorList.add(NavigationPath);
            }
            setAncestorsList(ancestorList);
        }
    }

    public String toString()
    {
        StringBuffer html = new StringBuffer();
        int atLevel = getLevel();
        for(int i = 0; i < atLevel; i++)
            html.append("  ");

        html.append(getQualifiedName() + ": level " + getLevel() + " (max " + getMaxChildLevel() + "), class: " + getClass().getName() + "\n");

        if (childrenList != null && childrenList.size() > 0)
        {
            Iterator i = childrenList.iterator();
            while (i.hasNext())
            {
                NavigationPath path = (NavigationPath) i.next();
                html.append(path.toString());
            }
        }

        return html.toString();
    }
}
