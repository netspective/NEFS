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
package com.netspective.junxion.edi.format.igml;

import java.util.ArrayList;
import java.util.List;

import com.netspective.junxion.edi.format.igml.attributes.Requirement;
import com.netspective.junxion.edi.format.igml.attributes.Usage;
import com.netspective.junxion.edi.format.igml.util.SegmentContainer;
import com.netspective.junxion.edi.format.igml.util.TextContainer;
import com.netspective.junxion.edi.format.igml.util.TextList;

/**
 * Defines a loop or group of segments.
 */
public class Group implements TextContainer, SegmentContainer
{
    private String id;
    private String pos;
    private Requirement req;
    private Usage usage;
    private int minUse;
    private int maxUse;
    private TextList texts = new TextList();
    private List segmentRefsAndGroups = new ArrayList();
    private Table table;

    public Group()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPos()
    {
        return pos;
    }

    public void setPos(String pos)
    {
        this.pos = pos;
    }

    public Requirement getReq()
    {
        return req;
    }

    public void setReq(Requirement req)
    {
        this.req = req;
    }

    public Usage getUsage()
    {
        return usage;
    }

    public void setUsage(Usage usage)
    {
        this.usage = usage;
    }

    public int getMinUse()
    {
        return minUse;
    }

    public void setMinUse(int minUse)
    {
        this.minUse = minUse;
    }

    public int getMaxUse()
    {
        return maxUse;
    }

    public void setMaxUse(String maxUse)
    {
        if(maxUse.equals("N/A"))
            this.maxUse = Integer.MAX_VALUE;
        else
            this.maxUse = Integer.parseInt(maxUse);
    }

    public void addText(Text text)
    {
        texts.add(text);
    }

    public TextList getTexts()
    {
        return texts;
    }

    public void setTexts(TextList texts)
    {
        this.texts = texts;
    }

    public void addSegmentRef(SegmentRef segmentRef)
    {
        segmentRefsAndGroups.add(segmentRef);
    }

    public void addGroup(Group group)
    {
        segmentRefsAndGroups.add(group);
    }

    public List getSegmentRefsAndGroups()
    {
        return segmentRefsAndGroups;
    }

    public void setSegmentRefsAndGroups(List segmentRefsAndGroups)
    {
        this.segmentRefsAndGroups = segmentRefsAndGroups;
    }

    public Table getTable()
    {
        return table;
    }

    public void setTable(Table table)
    {
        this.table = table;
    }
}
