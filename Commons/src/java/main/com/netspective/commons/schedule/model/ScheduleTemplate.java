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
 * $Id: ScheduleTemplate.java,v 1.3 2004-03-26 22:03:47 shahid.shah Exp $
 */

package com.netspective.commons.schedule.model;

import java.util.Date;

import com.netspective.commons.set.DateRangesSet;
import com.netspective.commons.set.IntSpan;

public interface ScheduleTemplate
{
    /**
     * Retrieve the unique identifier for this template instance
     * @return An object which uniquely identifies a specific template
     */
    public Object getIdentifier();

    /**
     * Retrieve the schedule manager that is managing this template
     * @return
     */
    public ScheduleManager getScheduleManager();

    /**
     * Retrieve the list of participants for which the template was defined
     * @return A list of participants that must be part of any event scheduled using this template
     */
    public ScheduleParticipants getOwners();

    /**
     * Ascertain whether or not the template is active
     * @return True if active, false if the template should no longer be used
     */
    public boolean isActive();

    /**
     * Ascertain the starting applicability date for this template
     * @return The date on or after which events should be allowed to use this template
     */
    public Date getBeginDate();

    /**
     * Ascertain the ending applicability date for this template
     * @return The date on before which events should be allowed to use this template
     */
    public Date getEndDate();

    /**
     * Get the participant types that this schedule template applies to
     * @return The list of participant types that may be applied to this template (other than the owners)
     */
    public ScheduleParticipantTypes getParticipantTypes();

    /**
     * Get the event types that this schedule template applies to
     * @return The list of events that may be applied to this template
     */
    public ScheduleEventTypes getEventTypes();

    /**
     * Retrieve the months of the year that this template is applicable
     * @return A IntSpan integer set with the months of the year that this template is applicable. The first month
     *         begins with 0 and the month indexes correspond to Calendar.set(Calendar.MONTH).
     */
    public IntSpan getMonthsOfYear();

    /**
     * Retrieve the days of the month that this template is applicable
     * @return A IntSpan integer set with the days of the month that this template is applicable. The first day
     *         begins with 1 and the day indexes correspond to Calendar.set(Calendar.DAY_OF_MONTH).
     */
    public IntSpan getDaysOfTheMonth();

    /**
     * Retrieve the days of the week that this template is applicable
     * @return A IntSpan integer set with the day of week of the that this template is applicable. The first day
     *         begins with Calendar.MONDAY and the day indexes correspond to Calendar.set(Calendar.DAY_OF_WEEK).
     */
    public IntSpan getDaysOfTheWeek();

    /**
     * Retrieve the slot width for parallel appointments
     * @return
     */
    public int getSlotWidth();

    /**
     * Retrieve all the template slots associated with this template
     * @return A list of template slots calculated for the given date range
     */
    public ScheduleTemplateSlots getScheduleTemplateSlots(DateRangesSet dateRanges);
}
