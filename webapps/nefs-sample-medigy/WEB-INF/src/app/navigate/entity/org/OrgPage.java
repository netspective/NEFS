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
 * @author Aye Thu
 */

/**
 * $Id: OrgPage.java,v 1.1 2004-05-17 03:17:23 aye.thu Exp $
 */

package app.navigate.entity.org;

import app.navigate.entity.EntityPage;
import app.navigate.entity.EntityRedirectorPage;
import app.navigate.entity.person.ActivePerson;
import auto.dal.db.dao.OrgTable;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPath;
import com.netspective.sparx.navigate.NavigationTree;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;

public abstract class OrgPage extends EntityPage implements OrgSubtypePage
{

    private static Log log = LogFactory.getLog(EntityRedirectorPage.class);
    private static ValueSource RETAIN_PARAMS = new StaticValueSource(ActivePerson.PARAMNAME_PERSON_ID);

    public class OrgPageState extends EntityPageState
    {
        private ActiveOrg activeOrg;

        public ActiveOrg getActiveOrg()
        {
            return activeOrg;
        }

        public void setActiveOrg(ActiveOrg activePerson)
        {
            this.activeOrg = activePerson;
        }
    }

    public class OrgPageHeadingValueSource extends AbstractValueSource
    {
        public boolean hasValue(ValueContext vc)
        {
            return true;
        }

        public Value getValue(ValueContext vc)
        {
            return ((OrgPage.OrgPageState) ((NavigationContext) vc).getActiveState()).getActiveOrg().getOrg().getOrgName();
        }

        public PresentationValue getPresentationValue(ValueContext vc)
        {
            return new PresentationValue(getValue(vc));
        }
    }

    public class OrgProfilePageSubHeadingValueSource extends AbstractValueSource
    {
        public boolean hasValue(ValueContext vc)
        {
            return true;
        }

        public Value getValue(ValueContext vc)
        {
            OrgPage.OrgPageState orgPageState = ((OrgPage.OrgPageState) ((NavigationContext) vc).getActiveState());
            OrgTable.Record activeOrg = orgPageState.getActiveOrg().getOrg();
            TextColumn.TextColumnValue code = activeOrg.getOrgCode();

            return new GenericValue("<table class=\"sub-heading-items-table\">\n" +
                    "<tr><td class=\"sub-heading-item\"><span class=\"sub-heading-item-caption\">Category:</span> " + getEntitySubTypeName() + "</td>" +
                    "<td class=\"sub-heading-item\"><span class=\"sub-heading-item-caption\">Code:</span> " +
                    code.getTextValueOrBlank() + "</td></tr>\n " +
                    "</table>\n");
        }

        public PresentationValue getPresentationValue(ValueContext vc)
        {
            return new PresentationValue(getValue(vc));
        }
    }

    private ValueSource profile;

    public OrgPage(NavigationTree owner)
    {
        super(owner);
        setRequireRequestParam(ActiveOrg.PARAMNAME_ORG_ID);
        setRetainParams(RETAIN_PARAMS);
        setHeading(new OrgPage.OrgPageHeadingValueSource());
        setSubHeading(new OrgPage.OrgProfilePageSubHeadingValueSource());
    }

    public ValueSource getProfile()
    {
        return profile;
    }

    public void setProfile(ValueSource profile)
    {
        this.profile = profile;
    }

    public NavigationPath.State constructState()
    {
        return new OrgPage.OrgPageState();
    }

    public boolean isValid(NavigationContext nc)
    {
        if(! super.isValid(nc))
            return false;

        ActiveOrg activeOrg;

        // if we're coming from a redirector then it means that we may not need to rerun our queries
        OrgRedirectorPage.EntitySubtypeRedirectInfo esri = OrgRedirectorPage.getEntitySubtypeRedirectInfo(nc, ActiveOrg.getOrgIdParamValue(nc));
        if(esri != null)
            activeOrg = (ActiveOrg) esri.getData();
        else
        {
            // if we get here it means we need to run our queries to get the active person
            ConnectionContext cc;
            try
            {
                cc = nc.getConnection(null, false);
            }
            catch (Exception e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }

            try
            {
                activeOrg = new ActiveOrg(nc, cc);
            }
            catch (Exception e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
            finally
            {
                try
                {
                    cc.close();
                }
                catch (SQLException e)
                {
                    log.error(e);
                    throw new NestableRuntimeException(e);
                }
            }
        }

        ((OrgPage.OrgPageState) nc.getActiveState()).setActiveOrg(activeOrg);
        return true;
    }
}