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
 * $Id: NavigationSqlErrorPage.java,v 1.2 2004-02-11 18:26:01 shahid.shah Exp $
 */

package com.netspective.sparx.navigate.error;

import java.sql.SQLException;

import javax.servlet.ServletException;

import org.apache.commons.lang.exception.NestableException;

import com.netspective.sparx.navigate.NavigationErrorPage;
import com.netspective.sparx.navigate.NavigationTree;

public class NavigationSqlErrorPage extends NavigationErrorPage
{
    private int startSqlCode = -1;
    private int endSqlCode = -1;
    private boolean callSuperIfNoMatch = true;

    public NavigationSqlErrorPage(NavigationTree navTree)
    {
      super(navTree);

    }

    public int getStartSqlCode()
    {
        return startSqlCode;
    }

    public void setStartSqlCode(int startSqlCode)
    {
        this.startSqlCode = startSqlCode;
    }

    public int getEndSqlCode()
    {
        return endSqlCode;
    }

    public void setEndSqlCode(int endSqlCode)
    {
        this.endSqlCode = endSqlCode;
    }

    public boolean isCallSuperIfNoMatch()
    {
        return callSuperIfNoMatch;
    }

    public void setCallSuperIfNoMatch(boolean callSuperIfNoMatch)
    {
        this.callSuperIfNoMatch = callSuperIfNoMatch;
    }

    public void setSqlCode(int code)
    {
        setStartSqlCode(code);
        setEndSqlCode(code);
    }

    protected boolean canHandleSqlException(Throwable t)
    {
        if (t instanceof SQLException)
        {
            int sqlErrorCode = ((SQLException) t).getErrorCode();
            if (sqlErrorCode >= startSqlCode && sqlErrorCode <= endSqlCode)
               return true;
        }

        return false;
    }

    public boolean canHandle(Throwable t, boolean checkSuperClasses)
    {
        if(canHandleSqlException(t))
            return true;

        if(t instanceof ServletException)
        {
            ServletException se = (ServletException) t;
            if(canHandleSqlException(se.getRootCause()))
                return true;
        }

        // if we're dealing with a nested exception, check to see if one of the nested exceptions is something we
        // need to handle
        if(t instanceof NestableException)
        {
            NestableException ne = (NestableException) t;
            Throwable[] throwables = ne.getThrowables();
            for(int i = 0; i < throwables.length; i++)
            {
                Throwable nestedException = throwables[i];
                if(t.getClass() == nestedException.getClass()) // don't get stuck in an infinite loop
                    continue;

                if(canHandleSqlException(nestedException))
                    return true;
            }
        }

        return callSuperIfNoMatch ? super.canHandle(t, checkSuperClasses) : false;
    }
}
