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
 * $Id: CalendarUtils.java,v 1.2 2004-03-26 16:18:45 shahid.shah Exp $
 */

package com.netspective.commons.schedule;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.discovery.tools.DiscoverClass;

public class CalendarUtils
{
    private static int JGREG = 2299161; //Julian day of adoption of Gregorian cal.
    private static final DiscoverClass DISCOVER_CLASS = new DiscoverClass();
    private static final CalendarUtils INSTANCE;

    static
    {
        CalendarUtils instance;
        try
        {
            instance = (CalendarUtils) DISCOVER_CLASS.newInstance(CalendarUtils.class);
        }
        catch (Exception e)
        {
            instance = new CalendarUtils();
        }
        INSTANCE = instance;
    }

    public static CalendarUtils getInstance()
    {
        return INSTANCE;
    }

    private CalendarUtils()
    {
    }

    public int getJulianDay(long dateMillis)
    {
        return getJulianDay(Calendar.getInstance(), dateMillis);
    }

    public int getJulianDay(Date date)
    {
        return getJulianDay(Calendar.getInstance(), date);
    }

    public int getJulianDay(Calendar calendar, long dateMillis)
    {
        calendar.setTimeInMillis(dateMillis);
        return getJulianDay(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
    }

    public int getJulianDay(Calendar calendar, Date date)
    {
        calendar.setTime(date);
        return getJulianDay(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
    }

    public Date getDateFromJulianDay(int julianDay)
    {
        return getDateFromJulianDay(julianDay, Calendar.getInstance());
    }

    /**
     * @return The Julian day number that begins at noon of
     *         this day
     *         Positive year signifies A.D., negative year B.C.
     *         Remember that the year after 1 B.C. was 1 A.D.
     *         <p/>
     *         A convenient reference point is that May 23, 1968 noon
     *         is Julian day 2440000.
     *         <p/>
     *         Julian day 0 is a Monday.
     *         <p/>
     *         This algorithm is from Press et al., Numerical Recipes
     *         in C, 2nd ed., Cambridge University Press 1992
     */
    public int getJulianDay(int month, int day, int year)
    {
        int jy = year;
        if (year < 0) jy++;
        int jm = month;
        if (month > 2)
            jm++;
        else
        {
            jy--;
            jm += 13;
        }
        int jul = (int) (Math.floor(365.25 * jy)
                + Math.floor(30.6001 * jm) + day + 1720995.0);

        int IGREG = 15 + 31 * (10 + 12 * 1582);
        // Gregorian Calendar adopted Oct. 15, 1582

        if (day + 31 * (month + 12 * year) >= IGREG)
        // change over to Gregorian calendar
        {
            int ja = (int) (0.01 * jy);
            jul += 2 - ja + (int) (0.25 * ja);
        }
        return jul;
    }

    /**
     * Converts a Julian day to a calendar date
     * This algorithm is from Press et al., Numerical Recipes
     * in C, 2nd ed., Cambridge University Press 1992
     */
    public Date getDateFromJulianDay(int julianDay, Calendar calendar)
    {
        int year, month, day;
        int ja = julianDay;

        if (julianDay >= JGREG)
        /* cross-over to Gregorian Calendar produces this
           correction
        */
        {
            int jalpha = (int) (((float) (julianDay - 1867216) - 0.25)
                    / 36524.25);
            ja += 1 + jalpha - (int) (0.25 * jalpha);
        }
        int jb = ja + 1524;
        int jc = (int) (6680.0 + ((float) (jb - 2439870) - 122.1)
                / 365.25);
        int jd = (int) (365 * jc + (0.25 * jc));
        int je = (int) ((jb - jd) / 30.6001);
        day = jb - jd - (int) (30.6001 * je);
        month = je - 1;
        if (month > 12) month -= 12;
        year = jc - 4715;
        if (month > 2) --year;
        if (year <= 0) --year;

        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }
}
