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
 * $Id: ScheduleParticipantTypeSet.java,v 1.1 2004-03-26 16:18:44 shahid.shah Exp $
 */

package com.netspective.commons.schedule.model;

import java.util.Collection;
import java.util.Set;

import com.netspective.commons.set.IntSpan;
import com.netspective.commons.set.IntSpan.ElementFormatter;
import com.netspective.commons.set.IntSpan.IntSpanIterator;
import com.netspective.commons.set.IntSpan.Mappable;
import com.netspective.commons.set.IntSpan.Testable;

/**
 * A type-safe wrapper of the IntSpan object designed to manage schedule participant types.
 */
public class ScheduleParticipantTypeSet implements Set
{
    private IntSpan typeSet = new IntSpan();

    public ScheduleParticipantTypeSet()
    {
    }

    protected ScheduleParticipantTypeSet(IntSpan typeSet)
    {
        this.typeSet = typeSet;
    }

    public boolean add(Object o)
    {
        return typeSet.add(o);
    }

    public void clear()
    {
        typeSet.clear();
    }

    public boolean contains(Object o)
    {
        return typeSet.contains(o);
    }

    public boolean remove(Object o)
    {
        return typeSet.remove(o);
    }

    public boolean addAll(Collection c)
    {
        return typeSet.addAll(c);
    }

    public boolean containsAll(Collection c)
    {
        return typeSet.containsAll(c);
    }

    public boolean removeAll(Collection c)
    {
        return typeSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return typeSet.retainAll(c);
    }

    public java.util.Iterator iterator()
    {
        return typeSet.iterator();
    }

    public Object clone()
    {
        return typeSet.clone();
    }

    public String toString()
    {
        return typeSet.toString();
    }

    public String runList()
    {
        return typeSet.runList();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return typeSet.getFormattedRunList(formatter);
    }

    public int[] getElements()
    {
        return typeSet.getElements();
    }

    public Object[] toArray()
    {
        return typeSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return typeSet.toArray(list);
    }

    public int size()
    {
        return typeSet.size();
    }

    public boolean isEmpty()
    {
        return typeSet.isEmpty();
    }

    public boolean isFinite()
    {
        return typeSet.isFinite();
    }

    public boolean isNegInfite()
    {
        return typeSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return typeSet.isPosInfite();
    }

    public boolean isInfinite()
    {
        return typeSet.isInfinite();
    }

    public boolean isUniversal()
    {
        return typeSet.isUniversal();
    }

    public boolean isMember(int n)
    {
        return typeSet.isMember(n);
    }

    public void add(int n)
    {
        typeSet.add(n);
    }

    public void remove(int n)
    {
        typeSet.remove(n);
    }

    public Integer getMin()
    {
        return typeSet.getMin();
    }

    public Integer getMax()
    {
        return typeSet.getMax();
    }

    public ScheduleParticipantTypeSet grep(Testable predicate)
    {
        return new ScheduleParticipantTypeSet(typeSet.grep(predicate));
    }

    public ScheduleParticipantTypeSet map(Mappable trans)
    {
        return new ScheduleParticipantTypeSet(typeSet.map(trans));
    }

    public IntSpanIterator first()
    {
        return typeSet.first();
    }

    public IntSpanIterator last()
    {
        return typeSet.last();
    }

    public IntSpanIterator start(int n)
    {
        return typeSet.start(n);
    }

    public static ScheduleParticipantTypeSet union(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return new ScheduleParticipantTypeSet(IntSpan.union(a.typeSet, b.typeSet));
    }

    public static ScheduleParticipantTypeSet intersect(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return new ScheduleParticipantTypeSet(IntSpan.intersect(a.typeSet, b.typeSet));
    }

    public static ScheduleParticipantTypeSet diff(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return new ScheduleParticipantTypeSet(IntSpan.diff(a.typeSet, b.typeSet));
    }

    public static ScheduleParticipantTypeSet xor(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return new ScheduleParticipantTypeSet(IntSpan.xor(a.typeSet, b.typeSet));
    }

    public static ScheduleParticipantTypeSet complement(ScheduleParticipantTypeSet s)
    {
        return new ScheduleParticipantTypeSet(IntSpan.complement(s.typeSet));
    }

    public static boolean superset(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return IntSpan.superset(a.typeSet, b.typeSet);
    }

    public static boolean subset(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return IntSpan.subset(a.typeSet, b.typeSet);
    }

    public static boolean equal(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return IntSpan.equal(a.typeSet, b.typeSet);
    }

    public static boolean equivalent(ScheduleParticipantTypeSet a, ScheduleParticipantTypeSet b)
    {
        return IntSpan.equivalent(a.typeSet, b.typeSet);
    }
}
