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
 * $Id: AuthenticatedRespondent.java,v 1.2 2003-08-30 00:32:43 shahid.shah Exp $
 */

package app;

import java.util.Set;
import java.util.HashSet;
import java.sql.SQLException;

import javax.servlet.ServletRequest;
import javax.naming.NamingException;

import auto.dal.db.dao.VisitedPageTable;
import auto.dal.db.DataAccessLayer;
import auto.dal.db.vo.VisitedPage;
import auto.dal.db.vo.impl.VisitedPageVO;

import com.netspective.commons.security.BasicAuthenticatedUser;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPath;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.sql.QueryResultSet;

public class AuthenticatedRespondent extends BasicAuthenticatedUser
{
    private static final String REQATTRNAME_VISITED_PAGES_CACHE = AuthenticatedRespondent.class.getName() + ".VISITED_PAGES_CACHE";

    private Integer pin;

    public void setUserId(String id)
    {
        super.setUserId(id);
        pin = Integer.valueOf(id);
    }

    public Integer getRespondentPin()
    {
        return pin;
    }

    private Set getVisitedPaths(NavigationContext nc)
    {
        ServletRequest request = nc.getRequest();
        Set visited = (Set) request.getAttribute(REQATTRNAME_VISITED_PAGES_CACHE);
        if(visited == null)
        {
            visited = new HashSet();
            ConnectionContext cc = null;
            try
            {
                VisitedPageTable table = DataAccessLayer.getInstance().getVisitedPageTable();
                cc = nc.getConnection(null, true, ConnectionContext.OWNERSHIP_DEFAULT);
                VisitedPageTable.Records records = table.getAccessorRecords(cc, table.getAccessorByPinEquality(), new Object[] { getRespondentPin() });
                for(int i = 0; i < records.size(); i++)
                {
                    VisitedPage visitedPage = records.get(i).getValues();
                    visited.add(visitedPage.getPageId());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if(cc != null) cc.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        request.setAttribute(REQATTRNAME_VISITED_PAGES_CACHE, visited);
        return visited;
    }

    public boolean visitedPage(NavigationContext nc, NavigationPath path)
    {
        Set visited = getVisitedPaths(nc);
        return visited.contains(path.getQualifiedName());
    }

    public void setVisitedPage(NavigationContext nc, NavigationPage page)
    {
        if(visitedPage(nc, page))
            return;

        // eliminate cache because we're going to change it
        nc.getRequest().removeAttribute(REQATTRNAME_VISITED_PAGES_CACHE);

        ConnectionContext cc = null;
        VisitedPageTable table = DataAccessLayer.getInstance().getVisitedPageTable();
        try
        {
            cc = nc.getConnection(null, true, ConnectionContext.OWNERSHIP_DEFAULT);

            QueryResultSet qrs = table.getAccessorByIndexUniqueVisitEquality().execute(cc, new Object[] { getRespondentPin(), page.getQualifiedName() }, false);
            boolean existsAlready = qrs.getResultSet().next();
            qrs.close(false);

            if(! existsAlready)
            {
                VisitedPage visitedPage = new VisitedPageVO();
                visitedPage.setPin(getRespondentPin());
                visitedPage.setPageId(page.getQualifiedName());

                VisitedPageTable.Record record = table.createRecord();
                record.setValues(visitedPage);
                record.insert(cc);
            }

            cc.commitAndClose();
        }
        catch (NamingException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        catch (SQLException e)
        {
            try
            {
                if(cc != null) cc.rollbackAndClose();
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
        }
    }

/*
    public static void sendEmail(String subject, String messageText) throws AddressException, MessagingException
    {
        String host = "localhost";
        String from = "marsh.survey@netspective.com";
        String to = "Julian.S.Wassenaar@marsh.com";
        String cc = "shahid.shah@netspective.com";

        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);

        Session mailSession = Session.getDefaultInstance(props, null);

        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
        message.setSubject(subject);
        message.setText(messageText);

        Transport.send(message);
    }
*/
}
