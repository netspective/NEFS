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
package com.netspective.commons.metric;

import java.text.NumberFormat;
import java.util.Iterator;

public class CountMetric extends Metric
{
    public final static NumberFormat PCT_FORMAT = NumberFormat.getPercentInstance();
    private static final NumberFormat SUM_FORMAT = NumberFormat.getNumberInstance();

    private long count;
    private long sum;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    public CountMetric(Metric parent, String name)
    {
        super(parent.getLevel() + 1, parent, name);
    }

    public String getFormattedValue()
    {
        return SUM_FORMAT.format(getSum()) + getPercentageOfParent();
    }

    public String getPercentageOfParent()
    {
        if(getParent() != null && flagIsSet(this.METRICFLAG_SHOW_PCT_OF_PARENT))
            return " (" + PCT_FORMAT.format((double) sum / (double) (((CountMetric) getParent()).getSum())) + ")";
        else
            return "";
    }

    public long getCount()
    {
        return count;
    }

    public long getMax()
    {
        return max;
    }

    public long getMin()
    {
        return min;
    }

    public long getSum()
    {
        if(getChildren() != null && flagIsSet(METRICFLAG_SUM_CHILDREN))
        {
            count = 0;
            sum = 0;

            for(Iterator i = getChildren().iterator(); i.hasNext();)
            {
                count++;
                sum += ((CountMetric) i.next()).getSum();
            }
        }

        return sum;
    }

    public void setSum(long sum)
    {
        this.sum = sum;
    }

    public void incrementCount(int amount)
    {
        count++;
        sum += amount;
    }

    public void incrementCount()
    {
        count++;
        sum++;
    }

    public void incrementAverage(long value)
    {
        this.count++;
        this.sum += value;
        if(value < min)
            min = value;
        if(value > max)
            max = value;
    }
}
