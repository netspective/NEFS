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
 * $Id: PasswordDialogHandler.java,v 1.2 2003-10-13 05:51:19 aye.thu Exp $
 */
package app.cts.form.person;

import com.netspective.sparx.form.handler.DialogExecuteDefaultHandler;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogValidationContext;
import com.netspective.sparx.form.listener.DialogValidateListener;
import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.security.Crypt;

import java.io.Writer;
import java.io.IOException;
import java.sql.SQLException;

import auto.dcb.subject.LoginInfoContext;
import auto.dal.db.dao.person.PersonLoginTable;
import auto.dal.db.dao.PersonTable;
import auto.dal.db.DataAccessLayer;
import app.cts.AppAuthenticatedUser;

/**
 * Class for handling changing of a user's login password
 */
public class PasswordDialogHandler extends DialogExecuteDefaultHandler implements DialogValidateListener
{
    /**
     * Handles the validation of the password
     * @param dvc
     */
    public void validateDialog(DialogValidationContext dvc)
    {
        LoginInfoContext lic = new LoginInfoContext(dvc.getDialogContext());
        String oldPassword = lic.getOldPasswordState().getValue().getTextValue();
        String newPassword1 = lic.getNewPassword1State().getValue().getTextValue();
        String newPassword2 = lic.getNewPassword2State().getValue().getTextValue();
        AppAuthenticatedUser user = (AppAuthenticatedUser) dvc.getDialogContext().getAuthenticatedUser();

        String encryptedOldPassword = Crypt.crypt(AppAuthenticatedUser.PASSWORD_ENCRYPTION_SALT, oldPassword);
        if (!encryptedOldPassword.equals(user.getEncryptedPassword()))
        {
            // the old password doesnt match up
            System.out.println(user.getEncryptedPassword());
            dvc.addError("Old password value is not correct.");
            return;
        }

        if (!newPassword1.equals(newPassword2))
        {
            // the old password doesnt match up
            dvc.addError("The new password fields do not match up.");
            return;
        }
    }

    /**
     * Handles the update of the password
     * @param writer
     * @param dc
     * @throws IOException
     * @throws DialogExecuteException
     */
    public void executeDialog(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        LoginInfoContext lic = new LoginInfoContext(dc);

        String newPassword = lic.getNewPassword1State().getValue().getTextValue();

        PersonTable personTable = DataAccessLayer.getInstance().getPersonTable();
        PersonTable.Record personRecord = personTable.createRecord();
        personRecord.setPersonId(lic.getLoginId());

        PersonLoginTable plTable = personTable.getPersonLoginTable();
        ConnectionContext cc = null;
        try
        {
            cc = dc.getConnection(null, true);
            PersonLoginTable.Records loginRecords = plTable.getParentRecordsByPersonId(personRecord, cc);
            if (loginRecords != null)
            {
                PersonLoginTable.Record loginRecord = loginRecords.get(0);
                loginRecord.setPassword(new GenericValue(newPassword));
                loginRecord.update(cc);
                cc.commitAndClose();

                AppAuthenticatedUser user = (AppAuthenticatedUser) dc.getAuthenticatedUser();
                user.setEncryptedPassword(newPassword);
                dc.setAuthenticatedUser(user);
            }
            else
            {
                throw new DialogExecuteException("Failed to find the recrod for updating login information.");
            }

        }
        catch (Exception e)
        {
            try
            {
                if (cc != null)
                    cc.rollbackAndClose();
            }
            catch (Exception se)
            {
                se.printStackTrace();
            }
        }
        finally
        {
            try
            {
                if (cc != null)
                    cc.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

    }
}
