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
 * $Id: Message.java,v 1.1 2003-03-13 18:37:20 shahid.shah Exp $
 */

package com.netspective.junxion.edi.format.igml;

import java.util.List;
import java.util.ArrayList;

import com.netspective.junxion.edi.format.igml.util.TextContainer;
import com.netspective.junxion.edi.format.igml.util.TextList;
import com.netspective.junxion.edi.format.igml.util.SegmentContainer;
import com.netspective.junxion.edi.format.igml.attributes.MessageType;

/**
 * Defines the transaction set or message by specifying the tables, segments, loops or groups, etc.
 */
public class Message implements TextContainer, SegmentContainer
{
    /**
     * The name or number of the transaction set or message.
     */
    private String id;

    /**
     * Indicator used by UN/EDIFACT.
     */
    private MessageType type;

    /**
     * Revision of the message.
     */
    private String revision;

    /**
     * Date of the message in ISO 8601 format: CCYY-MM-DDThh:mm:ss.
     */
    private String date;

    /**
     * The controlling agency for the message.
     */
    private String controlAgency;

    /**
     * The list of <Text> elements contained in this element.
     */
    private TextList texts = new TextList();

    /**
     * The list of segment references and groups
     * <!ELEMENT Message ( Text*, ( (Table+) | (SegmentRef, (SegmentRef|Group)*) ),
     */
    private List segmentRefsAndGroups = new ArrayList();

    public Message()
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

    public MessageType getType()
    {
        return type;
    }

    public void setType(MessageType type)
    {
        this.type = type;
    }

    public String getRevision()
    {
        return revision;
    }

    public void setRevision(String revision)
    {
        this.revision = revision;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getControlAgency()
    {
        return controlAgency;
    }

    public void setControlAgency(String controlAgency)
    {
        this.controlAgency = controlAgency;
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

    public Table createTable()
    {
        return new Table(this);
    }

    public void addTable(Table table)
    {
        // we don't really manage table as children, just "active" or "inactive"
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

    public void setSegmentRefsAndGroups(List tablesAndSegmentRefsAndGroups)
    {
        this.segmentRefsAndGroups = tablesAndSegmentRefsAndGroups;
    }
}
