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
package com.netspective.commons.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSourceTracker implements InputSourceTracker
{
    protected long lastModified;
    private InputSourceTracker owner;
    private InputSourceTracker parent;
    protected List preProcessors;
    protected List includes;

    public int getDependenciesCount()
    {
        int count = preProcessors != null ? preProcessors.size() : 0;
        count += includes != null ? includes.size() : 0;
        return count;
    }

    public InputSourceTracker getOwner()
    {
        return owner;
    }

    public InputSourceTracker getParent()
    {
        return parent;
    }

    public List getIncludes()
    {
        return includes;
    }

    public List getPreProcessors()
    {
        return preProcessors;
    }

    public void setOwner(InputSourceTracker owner)
    {
        this.owner = owner;
    }

    public void setParent(InputSourceTracker parent)
    {
        this.parent = parent;
        parent.addInclude(this);
        owner = parent.getOwner();
    }

    public void addPreProcessor(InputSourceTracker value)
    {
        if(null == value) return;

        if(preProcessors == null) preProcessors = new ArrayList();
        preProcessors.add(value);
    }

    public void addPreProcessor(File file)
    {
        if(null == file) return;

        FileTracker tracker = new FileTracker();
        tracker.setFile(file);
        addPreProcessor(tracker);
    }

    public void addPreProcessor(String filename)
    {
        if(null == filename) return;

        FileTracker tracker = new FileTracker();
        File file = new File(filename);
        tracker.setFile(file);
        addPreProcessor(tracker);
    }

    public void addInclude(InputSourceTracker value)
    {
        if(null == value) return;

        if(includes == null) includes = new ArrayList();
        includes.add(value);
    }

    public void addInclude(File file)
    {
        if(null == file) return;

        FileTracker tracker = new FileTracker();
        tracker.setFile(file);
        addInclude(tracker);
    }

    public void addInclude(String filename)
    {
        if(null == filename) return;

        FileTracker tracker = new FileTracker();
        File file = new File(filename);
        tracker.setFile(file);
        addInclude(tracker);
    }

    public boolean dependenciesSourcesChanged()
    {
        if(preProcessors != null)
        {
            for(int i = 0; i < preProcessors.size(); i++)
            {
                if(((InputSourceTracker) preProcessors.get(i)).sourceChanged())
                    return true;
            }
        }

        if(includes != null)
        {
            for(int i = 0; i < includes.size(); i++)
            {
                if(((InputSourceTracker) includes.get(i)).sourceChanged())
                    return true;
            }
        }

        return false;
    }
}
