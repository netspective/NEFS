/**
 * Created on Sep 17, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package app.form.claim;

import auto.dal.db.DataAccessLayer;
import auto.dal.db.dao.ClaimTable;
import auto.dcb.claim.ClaimContext;
import auto.id.sql.schema.db.enum.ClaimStatusType;
import auto.id.sql.schema.db.enum.RecordStatus;
import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.value.GenericValue;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.handler.DialogExecuteRecordEditorHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author Shahbaz Javeed
 * @author Aye Thu
 */
public class ClaimDialog extends DialogExecuteRecordEditorHandler
{
    private static Log log = LogFactory.getLog(ClaimDialog.class);
    /* Utility Methods Go Here */

    public void addRecord(DialogContext dc, ConnectionContext cc) throws DialogExecuteException
    {

        ClaimContext clc = new ClaimContext(dc);

        /* Claim Row */
        ClaimTable clmTbl = DataAccessLayer.getInstance().getClaimTable();
        ClaimTable.Record clmRow = populateClaimRow(clc, clmTbl);
//
//        Hashtable diagHash = new Hashtable();
//        Hashtable procHash = new Hashtable();
//        Vector dpVector = new Vector();
//
//        /* Claim Diagnosis Rows */
//        ClaimDiagnosisTable diagTbl = DataAccessLayer.instance.getClaimDiagnosisTable();
//        populateDiagnosisHash(clc, crSessionId, diagHash, diagTbl);
//
//        /* Claim Procedure Rows */
//        ClaimProcedureTable procTbl = DataAccessLayer.instance.getClaimProcedureTable();
//        populateProcedureHash(clc, crSessionId, procHash, procTbl);
//
//        /* Claim Diag-Proc Relationship Rows */
//        ClaimDiagProcRelTable cdpRelTbl = DataAccessLayer.instance.getClaimDiagProcRelTable();
//        populateDPRelationshipVector(clc, dpVector);

        try
        {
//
            /* ... and the Claim record... */
            clmRow.insert(cc);
//
//            /* Now insert the list of Diagnosis codes... */
//            int j = 0;
//            for (Enumeration e = diagHash.keys(); e.hasMoreElements();)
//            {
//                String diagCode = (String) e.nextElement();
//
//                ClaimDiagnosisRow clmDiagRow = (ClaimDiagnosisRow) diagHash.get(diagCode);
//                clmDiagRow.setClaimId(clmRow.getClaimId());
//
//                diagTbl.insert(cc, clmDiagRow);
//
//                diagHash.put(diagCode, clmDiagRow);
//                j++;
//            }
//
//            /* ... followed by the list of Procedure codes ... */
//            j = 0;
//            for (Enumeration e = procHash.keys(); e.hasMoreElements();)
//            {
//                String procCode = (String) e.nextElement();
//
//                ClaimProcedureRow clmProcRow = (ClaimProcedureRow) procHash.get(procCode);
//                clmProcRow.setClaimId(clmRow.getClaimId());
//
//                procTbl.insert(cc, clmProcRow);
//
//                procHash.put(procCode, clmProcRow);
//                j++;
//            }
//
//            /* ... and finally the relationships between the two ... */
//            for (int i = 0; i < dpVector.size(); i++)
//            {
//                DiagProcPair dpPair = (DiagProcPair) dpVector.get(i);
//
//                ClaimDiagnosisRow clmDiagRow = (ClaimDiagnosisRow) diagHash.get(dpPair.getDiagnosis());
//                ClaimProcedureRow clmProcRow = (ClaimProcedureRow) procHash.get(dpPair.getProcedure());
//                ClaimDiagProcRelRow clmDPRelRow = cdpRelTbl.createClaimDiagProcRelRow();
//
//                clmDPRelRow.setCrSessId(crSessionId);
//                clmDPRelRow.setCrStamp(new Date());
//                clmDPRelRow.setRecStatId(RecordStatusTable.EnumeratedItem.ACTIVE);
//
//                clmDPRelRow.setDiagnosisId(clmDiagRow.getSystemId());
//                clmDPRelRow.setProcedureId(clmProcRow.getSystemId());
//
//                cdpRelTbl.insert(cc, clmDPRelRow);
//            }
//
            cc.commitAndClose();
        }
        catch (NamingException e)
        {
            e.printStackTrace();
            log.error(e);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            log.error(e);
        }

    }

    public void editRecord(DialogContext dialogContext, ConnectionContext connectionContext) throws DialogExecuteException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteRecord(DialogContext dialogContext, ConnectionContext connectionContext) throws DialogExecuteException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    /*
    protected void populateDiagnosisHash(ClaimContext clc, String crSessionId, Hashtable diagHash, ClaimDiagnosisTable diagTbl)
    {
        for (int i = 0; i < 15; i++)
        {
            if (clc.hasValue("diagproc.diagproc_" + i + ".dp.diag_code") && !"".equals(clc.getValue("diagproc.diagproc_" + i + ".dp.diag_code")))
            {
                ClaimDiagnosisRow diagRow = diagTbl.createClaimDiagnosisRow();

                diagRow.setCrSessId(crSessionId);
                diagRow.setCrStamp(new Date());
                diagRow.setRecStatId(RecordStatusTable.EnumeratedItem.ACTIVE);

                diagRow.setDiagCodeTypeId(clc.getDptype_diagCodeTypeIdInt());
                diagRow.setDiagCode(clc.getValue("diagproc.diagproc_" + i + ".dp.diag_code"));

                if (!diagHash.containsKey(clc.getValue("diagproc.diagproc_" + i + ".dp.diag_code", "")))
                {
                    diagHash.put(clc.getValue("diagproc.diagproc_" + i + ".dp.diag_code", ""), diagRow);
                }
            }
        }

    }

    protected void populateProcedureHash(ClaimContext clc, String crSessionId, Hashtable procHash, ClaimProcedureTable procTbl)
    {
        for (int i = 0; i < 15; i++)
        {
            if (clc.hasValue("diagproc.diagproc_" + i + ".dp.proc_code") && !"".equals(clc.getValue("diagproc.diagproc_" + i + ".dp.proc_code")))
            {
                ClaimProcedureRow procRow = procTbl.createClaimProcedureRow();

                procRow.setCrSessId(crSessionId);
                procRow.setCrStamp(new Date());
                procRow.setRecStatId(RecordStatusTable.EnumeratedItem.ACTIVE);

                procRow.setProcCodeTypeId(clc.getDptype_diagCodeTypeIdInt());
                procRow.setProcCode(clc.getValue("diagproc.diagproc_" + i + ".dp.diag_code"));
                procRow.setModifier(clc.getValue("diagproc.diagproc_" + i + ".dp.proc_modifier"));

                if (!procHash.containsKey(clc.getValue("diagproc.diagproc_" + i + ".dp.proc_code", "")))
                {
                    procHash.put(clc.getValue("diagproc.diagproc_" + i + ".dp.proc_code", ""), procRow);
                }
            }
        }

    }

    protected void populateDPRelationshipVector(ClaimContext clc, Vector dpVector)
    {
        for (int i = 0; i < 15; i++)
        {
            if (clc.hasValue("diagproc.diagproc_" + i + ".dp.diag_code") && !"".equals(clc.getValue("diagproc.diagproc_" + i + ".dp.diag_code")))
            {
                DiagProcPair dpPair = new DiagProcPair(clc.getValue("diagproc_diagproc_" + i + ".dp.diag_code"), clc.getValue("diagproc_diagproc_" + i + ".dp.proc_code"));

                dpVector.add(dpPair);
            }
        }

    }
    */
    /**
     * Creates a record for the Claim table using the dialog context data
     *
     * @param clc           current dialog context
     * @param clmTbl        Claim table
     * @return              Claim table record
     */
    protected ClaimTable.Record populateClaimRow(ClaimContext clc, ClaimTable clmTbl)
    {
        ClaimTable.Record clmRow = clmTbl.createRecord();
        clmRow.setPatientId(clc.getPatientId());
        clmRow.setRecStatId(new GenericValue(new Integer(RecordStatus.ACTIVE)));
        clmRow.setCrStamp(new GenericValue(new Date()));
        clmRow.setClaimStatusId(new GenericValue(new Integer(ClaimStatusType.CREATED)));

        if (clc.getBatch_date().hasValue())
            clmRow.setBatchDate(clc.getBatch_date());
        if (clc.getBatch_id().hasValue())
            clmRow.setBatchId(clc.getBatch_id());
        clmRow.setAccident(clc.getIsAccident());
        if (clc.getIsAccident().getTextValue().equals("True"))
            clmRow.setAccidentStateId(clc.getAccident_stateId());

        if (clc.getAccident_state().hasValue())
            clmRow.setAccidentState(clc.getAccident_state());

        if (clc.getPriorAuthNum().hasValue())
            clmRow.setAuthorizationNumber(clc.getPriorAuthNum());

        //clmRow.setVisitTypeId(clc.getVisitTypeIdInt());
        if (clc.getServprov_providerId().hasValue())
            clmRow.setServiceProviderId(clc.getServprov_providerId());
        if (clc.getBillprov_providerId().hasValue())
            clmRow.setBillingProviderId(clc.getBillprov_providerId());
        if (clc.getServprov_facilityId().hasValue())
            clmRow.setServiceFacilityId(clc.getServprov_facilityId());
        if (clc.getBillprov_facilityId().hasValue())
            clmRow.setBillingFacilityId(clc.getBillprov_facilityId());

        return clmRow;
    }

    /**
     * @author Shahbaz Javeed
     *         <p/>
     *         To change this generated comment edit the template variable "typecomment":
     *         Window>Preferences>Java>Templates.
     *         To enable and disable the creation of type comments go to
     *         Window>Preferences>Java>Code Generation.
     */
    private class DiagProcPair
    {
        String diagnosis = "";
        String procedure = "";

        /**
         * Constructor for DiagProcPair.
         */
        public DiagProcPair()
        {
            diagnosis = "";
            procedure = "";
        }

        public DiagProcPair(String _diag, String _proc)
        {
            diagnosis = _diag;
            procedure = _proc;
        }

        /**
         * Returns the diagnosis.
         *
         * @return String
         */
        public String getDiagnosis()
        {
            return diagnosis;
        }

        /**
         * Returns the procedure.
         *
         * @return String
         */
        public String getProcedure()
        {
            return procedure;
        }

        /**
         * Sets the diagnosis.
         *
         * @param diagnosis The diagnosis to set
         */
        public void setDiagnosis(String diagnosis)
        {
            this.diagnosis = diagnosis;
        }

        /**
         * Sets the procedure.
         *
         * @param procedure The procedure to set
         */
        public void setProcedure(String procedure)
        {
            this.procedure = procedure;
        }

    }
}
