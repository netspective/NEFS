/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Aye Thu
 */

package app.form.person.attribute;

import auto.dal.db.DataAccessLayer;
import auto.dal.db.dao.person.PersonAddressTable;
import auto.dcb.personattribute.AddressContext;
import auto.id.sql.schema.db.enum.RecordStatus;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.commons.value.GenericValue;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogContextUtils;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.handler.DialogExecuteRecordEditorHandler;
import com.netspective.sparx.form.listener.DialogInitialPopulateListener;
import com.netspective.sparx.panel.editor.PanelEditor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.NamingException;
import java.sql.SQLException;

/**
 * @version $Id: AddressDialogHandler.java,v 1.2 2004-03-17 23:48:27 shahbaz.javeed Exp $
 *
 */
public class AddressDialogHandler extends DialogExecuteRecordEditorHandler implements DialogInitialPopulateListener
{
    private static final Log log = LogFactory.getLog(AddressDialogHandler.class);

    public void addRecord(DialogContext dc, ConnectionContext cc) throws DialogExecuteException
    {

        AddressContext ac = new AddressContext(dc);
        PersonAddressTable personAddressTable = DataAccessLayer.getInstance().getPersonTable().getPersonAddressTable();
        PersonAddressTable.Record personAddressRow = personAddressTable.createRecord();

        try
        {
            personAddressRow.setRecStatId(new GenericValue(new Integer(RecordStatus.ACTIVE)));

            personAddressRow.setAddressName(ac.getAddressName());
            personAddressRow.setAddressTypeId(ac.getAddressTypeIdState().getValue());
            personAddressRow.setMailing(ac.getMailing());
            personAddressRow.setParentId(ac.getParentId());

            personAddressRow.setLine1(ac.getLine1());
            personAddressRow.setLine2(ac.getLine2());
            personAddressRow.setCity(ac.getCity());
            personAddressRow.setCounty(ac.getCounty());
            personAddressRow.setStateId(ac.getState());
            personAddressRow.setZip(ac.getZip());
            personAddressRow.setCountry(ac.getCountry());
            personAddressRow.insert(cc);
            cc.commitAndClose();
        }
        catch (NamingException e)
        {
            e.printStackTrace();
            log.error("Failed to add address in dialog '"+ dc.getDialog().getQualifiedName() + "' for person id '" + ac.getParentId() +"'.", e);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            log.error("Failed to add address in dialog '"+ dc.getDialog().getQualifiedName() + "' for person id '" + ac.getParentId() +"'.", e);
        }

    }

    public void editRecord(DialogContext dc, ConnectionContext cc) throws DialogExecuteException
    {
        AddressContext ac = new AddressContext(dc);
        PersonAddressTable personAddressTable = DataAccessLayer.getInstance().getPersonTable().getPersonAddressTable();
        PersonAddressTable.Record personAddressRow = null;

        try
        {
            personAddressRow = personAddressTable.getRecordByPrimaryKey(cc, ac.getSystemIdState().getValue().getTextValue());
            personAddressRow.setRecStatId(new GenericValue(new Integer(RecordStatus.ACTIVE)));

            personAddressRow.setAddressName(ac.getAddressName());
            personAddressRow.setAddressTypeId(ac.getAddressTypeIdState().getValue());
            personAddressRow.setMailing(ac.getMailing());
            personAddressRow.setParentId(ac.getParentId());

            personAddressRow.setLine1(ac.getLine1());
            personAddressRow.setLine2(ac.getLine2());
            personAddressRow.setCity(ac.getCity());
            personAddressRow.setCounty(ac.getCounty());
            personAddressRow.setStateId(ac.getState());
            personAddressRow.setZip(ac.getZip());
            personAddressRow.setCountry(ac.getCountry());
            personAddressRow.update(cc);
            cc.commitAndClose();
        }
        catch (NamingException e)
        {
            e.printStackTrace();
            log.error("Failed to update address in dialog '"+ dc.getDialog().getQualifiedName() + "' for person id '" + ac.getParentId() +"'.", e);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            log.error("Failed to update address in dialog '"+ dc.getDialog().getQualifiedName() + "' for person id '" + ac.getParentId() +"'.", e);
        }

    }

    public void deleteRecord(DialogContext dialogContext, ConnectionContext connectionContext) throws DialogExecuteException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void populateInitialDialogValues(DialogContext dc, int formatType)
    {
        if (dc.editingData() || dc.deletingData())
        {
            String idParam = (String) dc.getAttribute(PanelEditor.POPULATE_KEY_CONTEXT_ATTRIBUTE);
            if (idParam == null || idParam.length() == 0)
                throw new RuntimeException("Address ID was not found in the dialog context.");
            Query query = dc.getSqlManager().getQuery("personAttribute.get-address-record");
            try
            {
                QueryResultSet qrs = query.execute(dc, new Object[] { idParam }, false);
                DialogContextUtils.getInstance().populateFieldValuesFromResultSet(dc, qrs);
                qrs.close(true);
                qrs = null;
            }
            catch (NamingException e)
            {
                e.printStackTrace();
                log.error("Failed to populate dialog '" + dc.getDialog().getQualifiedName() + "'.", e);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                log.error("Failed to populate dialog '" + dc.getDialog().getQualifiedName() + "'.", e);
            }
        }
    }

    /*
    protected String addRecord(DialogContext dc, String crSessionId)
    {
        AddressContext ac = (AddressContext) dc;

        PersonAddressTable personAddressTable = DataAccessLayer.instance.getPersonAddressTable();
        PersonAddressRow personAddressRow = personAddressTable.createPersonAddressRow();

        personAddressRow.setCrSessId(crSessionId);
        personAddressRow.setRecStatId(RecordStatusTable.EnumeratedItem.ACTIVE);

        personAddressRow.setAddressName(ac.getAddressName());
        personAddressRow.setAddressTypeId(ac.getAddressTypeIdInt());
        personAddressRow.setMailing(ac.getMailing());

        personAddressRow.setParentId(ac.getParentId());
//		personAddressRow.setParentId(ac.getParentId());

        personAddressRow.setLine1(ac.getLine1());
        personAddressRow.setLine2(ac.getLine2());
        personAddressRow.setCity(ac.getCity());
        personAddressRow.setCounty(ac.getCounty());
        personAddressRow.setState(USStateTypeTable.EnumeratedItem.getEnum(ac.getStateInt()).getCaption());
        personAddressRow.setZip(ac.getZip());
        personAddressRow.setCountry(ac.getCountry());

        try
        {
            ConnectionContext cc = dc.getConnectionContext();

            cc.beginTransaction();

            personAddressTable.insert(cc, personAddressRow);

            cc.commitTransaction();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        catch (NamingException ne)
        {
            ne.printStackTrace();
        }

        return personAddressRow.getParentId().toString();
    }

    protected String deleteRecord(DialogContext dc, String crSessionId)
    {
        AddressContext ac = (AddressContext) dc;

        PersonAddressTable personAddressTable = DataAccessLayer.instance.getPersonAddressTable();
        PersonAddressRow personAddressRow = null;

        try
        {
            ConnectionContext cc = dc.getConnectionContext();

            cc.beginTransaction();

            personAddressRow = personAddressTable.getPersonAddressBySystemId(cc, ac.getSystemIdString());
            personAddressTable.delete(cc, personAddressRow);

            cc.commitTransaction();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        catch (NamingException ne)
        {
            ne.printStackTrace();
        }

        return personAddressRow.getParentId().toString();
    }

    protected void redirectBrowser(DialogContext dc, String personId)
    {
        HttpServletRequest request = (HttpServletRequest) dc.getRequest();
        String redirectURL = request.getContextPath();

        if (dc.addingData() || dc.editingData() || dc.deletingData())
        {
            redirectURL += "/person/summary.jsp" + "?person_id=" + personId;
        }

        try
        {
            ((HttpServletResponse) dc.getResponse()).sendRedirect(redirectURL);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected String genRedirectURL(DialogContext dc, String personId)
    {
        HttpServletRequest request = (HttpServletRequest) dc.getRequest();
        String redirectURL = request.getContextPath();

        if (dc.addingData() || dc.editingData() || dc.deletingData())
        {
            redirectURL += "/person/summary.jsp" + "?person_id=" + personId;
        }

        return redirectURL;
    }
    */
}
