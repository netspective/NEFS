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
 * $Id: StudyDialog.java,v 1.1 2003-09-22 05:23:08 aye.thu Exp $
 */
package app.cts.form;

import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogContextUtils;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.sql.ResultSetUtils;
import com.netspective.commons.value.PresentationValue;

import java.io.Writer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import auto.dcb.study.ProfileContext;
import auto.dal.db.DataAccessLayer;
import auto.dal.db.vo.impl.StudyVO;
import auto.dal.db.dao.StudyTable;
import auto.id.sql.schema.db.enum.RecordStatus;

import javax.naming.NamingException;


public class StudyDialog extends  Dialog
{
    /**
     *
     * @param dc
     * @param formatType
     */
    public void populateValues(DialogContext dc, int formatType)
    {
        super.populateValues(dc, formatType);
        if(dc.getAuthenticatedUser() == null)
            return;
        if (dc.isInitialEntry() && (dc.editingData() || dc.deletingData()))
        {
            String idParam = dc.getHttpRequest().getParameter("study_id");
            Query query = dc.getSqlManager().getQuery(auto.id.sql.query.Cts.GET_STUDY_INFO_BY_ID);
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
                getLog().error("Failed to populate dialog", e);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                getLog().error("Failed to populate dialog", e);
            }
        }
    }

    /**
     * Executes the study dialog
     * @param writer
     * @param dc
     * @throws IOException
     * @throws DialogExecuteException
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        ProfileContext pc = new ProfileContext(dc);
        ConnectionContext cc = null;

        try
        {
            cc = dc.getConnection(null, true);
            if (dc.addingData())
            {
                StudyTable studyTable = DataAccessLayer.getInstance().getStudyTable();
                StudyTable.Record record = studyTable.createRecord();

                StudyVO svo = new StudyVO();
                svo.setStudyName(pc.getStudyName().getTextValue());
                svo.setStudyDescr(pc.getStudyDescr().getTextValue());
                svo.setStudyCode(pc.getStudyCode().getTextValue());
                svo.setStartDate(pc.getStartDate().getDateValue());
                svo.setActualEndDate(pc.getActualEndDate().getDateValue());
                svo.setTargetEndDate(pc.getTargetEndDate().getDateValue());
                svo.setIrbName(pc.getIrbName().getTextValue());
                svo.setIrbNumber(pc.getIrbNumber().getTextValue());
                svo.setRecStatId(new Integer(RecordStatus.ACTIVE));
                svo.setStudyStatus(new Integer(pc.getStudyStatus().getIntValue()));
                svo.setStudyStage(new Integer(pc.getStudyPhase().getIntValue()));

                record.setValues(svo);
                try
                {
                    record.insert(cc);
                    cc.getConnection().commit();
                    getLog().info("Study added: id=" + record.getStudyId().getTextValue());
                }
                catch (NamingException e)
                {
                    e.printStackTrace();
                    getLog().error("Failed to insert row", e);
                    handlePostExecuteException(writer, dc, "Failed to insert row.", e);
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    getLog().error("Failed to insert row", e);
                    handlePostExecuteException(writer, dc, "Failed to insert row.", e);
                }
            }
            else if (dc.editingData())
            {

            }
            else if (dc.deletingData())
            {

            }
            handlePostExecute(writer, dc);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            getLog().error("Failed to get connection", e);
            handlePostExecuteException(writer, dc, "Failed to get connection.", e);
        }
        catch (NamingException ne)
        {
            ne.printStackTrace();
            getLog().error("Failed to get connection", ne);
            handlePostExecuteException(writer, dc, "Failed to get connection.", ne);
        }
        finally
        {
            try
            {
                if(cc != null)
                    cc.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
                handlePostExecuteException(writer, dc, "Unable to close connection", se);
            }
        }
    }
}
