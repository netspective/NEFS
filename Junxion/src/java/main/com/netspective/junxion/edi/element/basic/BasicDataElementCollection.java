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
package com.netspective.junxion.edi.element.basic;

import com.netspective.junxion.edi.InterchangeContext;
import com.netspective.junxion.edi.element.Collectable;
import com.netspective.junxion.edi.element.Collection;
import com.netspective.junxion.edi.element.CompositeDataElement;
import com.netspective.junxion.edi.element.DataElement;
import com.netspective.junxion.edi.element.Sequenced;
import com.netspective.junxion.edi.element.SequencedMap;
import com.netspective.junxion.edi.element.collection.CompositeDataElementsCollection;
import com.netspective.junxion.edi.element.collection.DataElementsCollection;

public class BasicDataElementCollection extends SequencedMap implements Sequenced, Collectable, DataElementsCollection, CompositeDataElementsCollection
{
    private int sequenceNum;
    private String identifier;
    private Collection owner;
    private Collection parent;

    public void addDataElement(DataElement dataElement)
    {
        add(dataElement);
    }

    public DataElement getDataElement(String identifier)
    {
        return (DataElement) get(identifier);
    }

    public DataElement getDataElement(int index)
    {
        return (DataElement) get(index);
    }

    public void addCompositeDataElement(CompositeDataElement dataElement)
    {
        add(dataElement);
    }

    public CompositeDataElement getCompositeDataElement(String identifier)
    {
        return (CompositeDataElement) get(identifier);
    }

    public CompositeDataElement getCompositeDataElement(int index)
    {
        return (CompositeDataElement) get(index);
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public Collection getOwner()
    {
        return owner;
    }

    public Collection getParent()
    {
        return parent;
    }

    public int getSequenceNumber()
    {
        return sequenceNum;
    }

    public boolean isValid(InterchangeContext ic)
    {
        return false;
    }

    public void registerChild(Collectable child)
    {
        child.setOwner(getOwner());
        child.setParent(this);
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public void setOwner(Collection owner)
    {
        this.owner = owner;
    }

    public void setParent(Collection parent)
    {
        this.parent = parent;
    }

    public void setSequenceNumber(int sequence)
    {
        this.sequenceNum = sequence;
    }
}
