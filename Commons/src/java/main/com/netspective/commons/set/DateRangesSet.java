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
 * $Id: DateRangesSet.java,v 1.1 2004-03-26 16:18:45 shahid.shah Exp $
 */

package com.netspective.commons.set;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.netspective.commons.schedule.CalendarUtils;
import com.netspective.commons.set.IntSpan.ElementFormatter;
import com.netspective.commons.set.IntSpan.IntSpanIterator;
import com.netspective.commons.set.IntSpan.Mappable;
import com.netspective.commons.set.IntSpan.Testable;

public class DateRangesSet
{
    private CalendarUtils calendarUtils = CalendarUtils.getInstance();
    private Calendar defaultCalendar = Calendar.getInstance();
    private IntSpan daysSet = new IntSpan();

    public DateRangesSet()
    {
    }

    public DateRangesSet(CalendarUtils calendarUtils)
    {
        this.calendarUtils = calendarUtils;
    }

    public DateRangesSet(Calendar defaultCalendar)
    {
        this.defaultCalendar = defaultCalendar;
    }

    public DateRangesSet(CalendarUtils calendarUtils, Calendar defaultCalendar)
    {
        this.calendarUtils = calendarUtils;
        this.defaultCalendar = defaultCalendar;
    }

    public DateRangesSet(Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        applyDateRange(defaultCalendar, calendarUtils, beginDate, endDate, monthsOfYear, daysOfMonth, daysOfWeek);
    }

    public DateRangesSet(Calendar calendar, Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        this(calendar);
        applyDateRange(calendar, beginDate, endDate, monthsOfYear, daysOfMonth, daysOfWeek);
    }

    public DateRangesSet(CalendarUtils calendarUtils, Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        this(calendarUtils);
        applyDateRange(calendarUtils, beginDate, endDate, monthsOfYear, daysOfMonth, daysOfWeek);
    }

    public DateRangesSet(Calendar calendar, CalendarUtils calendarUtils, Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        this(calendarUtils, calendar);
        applyDateRange(calendar, calendarUtils, beginDate, endDate, monthsOfYear, daysOfMonth, daysOfWeek);
    }

    public void applyDateRange(Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        applyDateRange(defaultCalendar, calendarUtils, beginDate, endDate, monthsOfYear, daysOfMonth, daysOfWeek);
    }

    public void applyDateRange(Calendar calendar, Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        applyDateRange(calendar, calendarUtils, beginDate, endDate, monthsOfYear, daysOfMonth, daysOfWeek);
    }

    public void applyDateRange(CalendarUtils calendarUtils, Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        applyDateRange(defaultCalendar, calendarUtils, beginDate, endDate, monthsOfYear, daysOfMonth, daysOfWeek);
    }

    /**
     * Calculate a set in which the months of the year, days of the month, and days of the week occur in between
     * the given begin and end date.
     * @param beginDate The date to start calculating from
     * @param endDate The date to end calculation at
     * @param monthsOfYear A set of month numbers for which to calculate the date set (values must match input to Calendar.set(Calendar.MONTH))
     * @param daysOfMonth A set of of day numbers for which to calculate the date set (values must match input to Calendar.set(Calendar.DAY_OF_MONTH))
     * @param daysOfWeek A set of of days of the week to calculate the date set (values must match input to Calendar.set(Calendar.DAY_OF_WEEK))
     */
    public void applyDateRange(Calendar calendar, CalendarUtils calendarUtils, Date beginDate, Date endDate, IntSpan monthsOfYear, IntSpan daysOfMonth, IntSpan daysOfWeek)
    {
        final int begin = calendarUtils.getJulianDay(calendar, beginDate), end = calendarUtils.getJulianDay(calendar, endDate);
        final boolean haveMonthsOfYear = monthsOfYear != null, haveDaysOfMonth = daysOfMonth != null, haveDaysOfWeek = daysOfWeek != null;

        for (int julianDay = begin; julianDay <= end; julianDay++)
        {
            Date activeDate = calendarUtils.getDateFromJulianDay(julianDay, calendar);
            calendar.setTime(activeDate);

            if ((!haveMonthsOfYear || monthsOfYear.isMember(calendar.get(Calendar.MONTH))) &&
                (!haveDaysOfMonth || daysOfMonth.isMember(calendar.get(Calendar.DAY_OF_MONTH))) &&
                (!haveDaysOfWeek || daysOfWeek.isMember(calendar.get(Calendar.DAY_OF_WEEK))))
            {
                add(julianDay);
            }
        }
    }

    protected DateRangesSet(IntSpan typeSet)
    {
        this.daysSet = typeSet;
    }

    public boolean add(Object o)
    {
        add(defaultCalendar, (Date) o);
        return true;
    }

    public void clear()
    {
        daysSet.clear();
    }

    public boolean contains(Object o)
    {
        return isMember(defaultCalendar, (Date) o);
    }

    public boolean remove(Object o)
    {
        remove(defaultCalendar, (Date) o);
        return true;
    }

    public boolean addAll(Collection c)
    {
        return daysSet.addAll(c);
    }

    public boolean containsAll(Collection c)
    {
        return daysSet.containsAll(c);
    }

    public boolean removeAll(Collection c)
    {
        return daysSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return daysSet.retainAll(c);
    }

    public Iterator iterator()
    {
        return new DateSpanIterator(defaultCalendar, (IntSpanIterator) daysSet.iterator());
    }

    public Object clone()
    {
        return daysSet.clone();
    }

    public String runList()
    {
        return daysSet.runList();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return daysSet.getFormattedRunList(formatter);
    }

    public int[] getElements()
    {
        return daysSet.getElements();
    }

    public Object[] toArray()
    {
        return daysSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return daysSet.toArray(list);
    }

    public int size()
    {
        return daysSet.size();
    }

    public boolean isEmpty()
    {
        return daysSet.isEmpty();
    }

    public boolean isFinite()
    {
        return daysSet.isFinite();
    }

    public boolean isNegInfite()
    {
        return daysSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return daysSet.isPosInfite();
    }

    public boolean isInfinite()
    {
        return daysSet.isInfinite();
    }

    public boolean isUniversal()
    {
        return daysSet.isUniversal();
    }

    public boolean isMember(Calendar calendar, Date date)
    {
        return daysSet.isMember(calendarUtils.getJulianDay(calendar, date));
    }

    public boolean isMember(Date date)
    {
        return daysSet.isMember(calendarUtils.getJulianDay(defaultCalendar, date));
    }

    public boolean isMember(int julianDay)
    {
        return daysSet.isMember(julianDay);
    }

    public void add(Calendar calendar, Date date)
    {
        add(calendarUtils.getJulianDay(calendar, date));
    }

    public void add(Date date)
    {
        add(calendarUtils.getJulianDay(defaultCalendar, date));
    }

    public void add(int julianDay)
    {
        daysSet.add(julianDay);
    }

    public void remove(Calendar calendar, Date date)
    {
        remove(calendarUtils.getJulianDay(calendar, date));
    }

    public void remove(Date date)
    {
        remove(calendarUtils.getJulianDay(defaultCalendar, date));
    }

    public void remove(int julianDay)
    {
        daysSet.remove(julianDay);
    }

    public Integer getMin()
    {
        return daysSet.getMin();
    }

    public Integer getMax()
    {
        return daysSet.getMax();
    }

    public DateRangesSet grep(Testable predicate)
    {
        return new DateRangesSet(daysSet.grep(predicate));
    }

    public DateRangesSet map(Mappable trans)
    {
        return new DateRangesSet(daysSet.map(trans));
    }

    public DateSpanIterator first()
    {
        return new DateSpanIterator(defaultCalendar, daysSet.first());
    }

    public DateSpanIterator first(Calendar calendar)
    {
        return new DateSpanIterator(calendar, daysSet.first());
    }

    public DateSpanIterator last()
    {
        return new DateSpanIterator(defaultCalendar, daysSet.last());
    }

    public DateSpanIterator last(Calendar calendar)
    {
        return new DateSpanIterator(calendar, daysSet.last());
    }

    public DateSpanIterator start(int julianDay)
    {
        return new DateSpanIterator(defaultCalendar, daysSet.start(julianDay));
    }

    public DateSpanIterator start(Date date)
    {
        return new DateSpanIterator(defaultCalendar, daysSet.start(calendarUtils.getJulianDay(defaultCalendar, date)));
    }

    public DateSpanIterator start(Calendar calendar, int julianDay)
    {
        return new DateSpanIterator(calendar, daysSet.start(julianDay));
    }

    public DateSpanIterator start(Calendar calendar, Date date)
    {
        return new DateSpanIterator(calendar, daysSet.start(calendarUtils.getJulianDay(calendar, date)));
    }

    public String toString(Calendar calendar, DateFormat format, String delim)
    {
        return daysSet.getFormattedRunList(new ElementDateFormatter(calendar, format, delim));
    }

    public String toString(DateFormat format, String delim)
    {
        return daysSet.getFormattedRunList(new ElementDateFormatter(defaultCalendar, format, delim));
    }

    public String toString()
    {
        return toString(defaultCalendar, DateFormat.getDateInstance(), ", ");
    }

    public static DateRangesSet union(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.union(a.daysSet, b.daysSet));
    }

    public static DateRangesSet intersect(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.intersect(a.daysSet, b.daysSet));
    }

    public static DateRangesSet diff(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.diff(a.daysSet, b.daysSet));
    }

    public static DateRangesSet xor(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.xor(a.daysSet, b.daysSet));
    }

    public static DateRangesSet complement(DateRangesSet s)
    {
        return new DateRangesSet(IntSpan.complement(s.daysSet));
    }

    public static boolean superset(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.superset(a.daysSet, b.daysSet);
    }

    public static boolean subset(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.subset(a.daysSet, b.daysSet);
    }

    public static boolean equal(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.equal(a.daysSet, b.daysSet);
    }

    public static boolean equivalent(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.equivalent(a.daysSet, b.daysSet);
    }

    private class ElementDateFormatter implements IntSpan.ElementFormatter
    {
        private Calendar calendar;
        private DateFormat format;
        private String delim;

        public ElementDateFormatter(Calendar calendar, DateFormat format, String delim)
        {
            this.calendar = calendar;
            this.format = format;
            this.delim = delim;
        }

        public String getElementDelimiter()
        {
            return delim;
        }

        public String getFormattedElement(int element)
        {
            Date date = calendarUtils.getDateFromJulianDay(element, calendar);
            return format.format(date);
        }
    }

    public class DateSpanIterator implements Iterator
    {
        private Calendar calendar;
        private IntSpanIterator i;

        public DateSpanIterator(Calendar calendar, IntSpanIterator i)
        {
            this.calendar = calendar;
            this.i = i;
        }

        public boolean hasNext()
        {
            return i.hasNext();
        }

        public boolean hasPrevious()
        {
            return i.hasPrevious();
        }

        public Object next()
        {
            Integer result = (Integer) i.next();
            return calendarUtils.getDateFromJulianDay(result.intValue(), calendar);
        }

        public Object previous()
        {
            Integer result = (Integer) i.previous();
            return calendarUtils.getDateFromJulianDay(result.intValue(), calendar);
        }

        public void remove()
        {
        }

        public String toString()
        {
            return calendarUtils.getDateFromJulianDay(Integer.parseInt(i.toString()), calendar).toString();
        }
    }
}
