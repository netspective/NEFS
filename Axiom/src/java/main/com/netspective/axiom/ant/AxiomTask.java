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
 * $Id: AxiomTask.java,v 1.1 2003-03-13 18:25:38 shahid.shah Exp $
 */

package com.netspective.axiom.ant;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;
import java.sql.SQLException;

import org.apache.tools.ant.BuildException;

import com.netspective.commons.xdm.XdmComponentTask;
import com.netspective.commons.xdm.XmlDataModelDtd;
import com.netspective.axiom.SqlManagerComponent;
import com.netspective.axiom.DatabasePolicy;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.connection.DriverManagerConnectionProvider;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.DataAccessLayerGenerator;
import com.netspective.axiom.schema.transport.DataImportDtd;
import com.netspective.axiom.schema.transport.DataImportParseContext;
import com.netspective.axiom.schema.transport.TableImportStatistic;
import com.netspective.axiom.value.DatabasePolicyValueContext;
import com.netspective.axiom.value.BasicDatabasePolicyValueContext;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.value.BasicDatabaseConnValueContext;

public class AxiomTask extends XdmComponentTask
{
    public static final int ACTIONID_GENERATE_AXIOM_DTD       = 1;
    public static final int ACTIONID_GENERATE_DDL             = 2;
    public static final int ACTIONID_GENERATE_IMPORT_DATA_DTD = 3;
    public static final int ACTIONID_GENERATE_DAL             = 4;
    public static final int ACTIONID_GENERATE_GRAPHVIZ_ERD    = 5;
    public static final int ACTIONID_IMPORT_DATA              = 6;
    public static final int ACTIONID_REVERSE_ENGINEER         = 7;

    public static final String DEFAULTCLASSNAME_DAL = "DataAccessLayer";

    private int action;
    private File destDir;
    private String schemaName;
    private String dbPolicyIdMatchRegEx = DatabasePolicies.DBPOLICYIDMATCH_ALL;
    private String fileExtn;
    private boolean createDdlDropSql;
    private File importFile;
    private File graphVizErdFile;
    private File dtdFile;
    private DriverManagerConnectionProvider.DataSourceInfo dsInfo;
    private String dalRootPackage;
    private String dalClassNameWithoutPackage = DEFAULTCLASSNAME_DAL;
    private File reverseEngineerDest;
    private String reverseEngineerSchema;
    private String reverseEngineerCatalogPattern;

    public void init() throws BuildException
    {
        super.init();
        action = 0;
        destDir = null;
        schemaName = null;
        dbPolicyIdMatchRegEx = DatabasePolicies.DBPOLICYIDMATCH_ALL;
        fileExtn = null;
        createDdlDropSql = false;
        importFile = null;
        graphVizErdFile = null;
        dsInfo = null;
        dalRootPackage = null;
        dalClassNameWithoutPackage = DEFAULTCLASSNAME_DAL;
        reverseEngineerDest = null;
        reverseEngineerSchema = null;
        reverseEngineerCatalogPattern = null;
    }

    public void setDestDir(File destDir)
    {
        this.destDir = destDir;
    }

    public String getFileExtn(String defaultExtn)
    {
        return fileExtn == null ? defaultExtn : null;
    }

    public void setFileExtn(String fileExtn)
    {
        this.fileExtn = fileExtn;
    }

    public Schema getSchema(SqlManagerComponent component) throws BuildException
    {
        if(schemaName == null)
            throw new BuildException("No schema attribute provide for source of DDL.");

        Schema schema = component.getManager().getSchema(schemaName);
        if(schema == null)
            throw new BuildException("Schema '"+ schemaName +"' does not exist in "+ component.getInputSource().getIdentifier() +".");

        return schema;
    }

    public void setSchema(String schemaName)
    {
        this.schemaName = schemaName;
    }

    /* ----- datasource-specific attributes and methods -------------------------------------------------------------*/

    public void createDataSource()
    {
        if(dsInfo == null)
            dsInfo = DriverManagerConnectionProvider.PROVIDER.createDataSource();
    }

    public void setDriver(String driverName)
    {
        createDataSource();
        dsInfo.setDriverName(driverName);
    }

    public void setUrl(String value)
    {
        createDataSource();
        dsInfo.setConnUrl(value);
    }

    public void setUserId(String value)
    {
        createDataSource();
        dsInfo.setConnUser(value);
    }

    public void setPassword(String value)
    {
        createDataSource();
        dsInfo.setConnPassword(value);
    }

    /* ----- import-specific attributes and methods -----------------------------------------------------------------*/

    public void setImport(File importFile)
    {
        action = ACTIONID_IMPORT_DATA;
        this.importFile = importFile;
    }

    public void setGenImportDtd(File importFile)
    {
        action = ACTIONID_GENERATE_IMPORT_DATA_DTD;
        this.dtdFile = importFile;
    }

    public void generateImportDtd(SqlManagerComponent component) throws BuildException
    {
        Schema schema = getSchema(component);
        try
        {
            new DataImportDtd().generate(schema, dtdFile);
        }
        catch (IOException e)
        {
            throw new BuildException(e);
        }
    }

    public void importData(SqlManagerComponent component) throws BuildException
    {
        Schema schema = getSchema(component);

        final String dataSourceId = AxiomTask.class.getName();
        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        DriverManagerConnectionProvider dmcp = new DriverManagerConnectionProvider();
        dmcp.addDataSourceInfo(dataSourceId, dsInfo);
        dbvc.setConnectionProvider(dmcp);
        ConnectionContext cc = null;
        DataImportParseContext pc = null;

        try
        {
            cc = dbvc.getConnection(dataSourceId, true);
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }

        try
        {
            pc = DataImportParseContext.parse(cc, schema, importFile);
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }
        finally
        {
            try
            {
                dbvc.returnConnection(cc);
            }
            catch (SQLException e)
            {
                throw new BuildException(e);
            }
        }

        List errors = pc.getErrors();
        if(pc.getErrors().size() > 0)
        {
            System.out.println("Import errors:");
            for(int i = 0; i < errors.size(); i++)
            {
                Object error = errors.get(i);
                if(error instanceof Throwable)
                    throw new BuildException((Exception) error);
                log(error.toString());
            }
        }

        Tables tables = schema.getTables();
        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);
            if(! table.isApplicationTable())
                continue;

            TableImportStatistic tis = pc.getStatistics(table);
            if(tis.getSuccessfulRows() > 0)
                log(tis.getTableName() + ": successful rows=" + tis.getSuccessfulRows() + ", unsuccessful rows="+ tis.getUnsuccessfulRows() + ", time=" + tis.getSqlTimeSpent());
        }
    }

    /* ----- GraphViz ERD attributes and methods --------------------------------------------------------------------*/

    public void setGraphVizErd(File file)
    {
        action = ACTIONID_GENERATE_GRAPHVIZ_ERD;
        graphVizErdFile = file;
    }

    public void generateGraphVizErd(SqlManagerComponent component) throws BuildException
    {
        Schema schema = getSchema(component);
        try
        {
            schema.generateGraphVizErd(new FileWriter(graphVizErdFile));
        }
        catch (IOException e)
        {
            throw new BuildException(e);
        }
    }

    /* ----- reverse-engineer specific attributes and methods -------------------------------------------------------*/

    public void setReverseEngineerDest(File reverseEngineerDest)
    {
        action = ACTIONID_REVERSE_ENGINEER;
        this.reverseEngineerDest = reverseEngineerDest;
    }

    public void setReverseEngineerSchema(String reverseEngineerSchema)
    {
        this.reverseEngineerSchema = reverseEngineerSchema;
    }

    public void setReverseEngineerCatalogPattern(String reverseEngineerCatalogPattern)
    {
        this.reverseEngineerCatalogPattern = reverseEngineerCatalogPattern;
    }

    public void reverseEngineer() throws BuildException
    {
        final String dataSourceId = AxiomTask.class.getName();
        DatabaseConnValueContext dbvc = new BasicDatabaseConnValueContext();
        DriverManagerConnectionProvider dmcp = new DriverManagerConnectionProvider();
        dmcp.addDataSourceInfo(dataSourceId, dsInfo);
        dbvc.setConnectionProvider(dmcp);
        ConnectionContext cc = null;

        try
        {
            cc = dbvc.getConnection(dataSourceId, true);
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }

        try
        {
            cc.getDatabasePolicy().reverseEngineer(reverseEngineerDest, cc.getConnection(), reverseEngineerSchema, reverseEngineerCatalogPattern);
        }
        catch(Exception e)
        {
            throw new BuildException(e);
        }
        finally
        {
            try
            {
                dbvc.returnConnection(cc);
            }
            catch (SQLException e)
            {
                throw new BuildException(e);
            }
        }
    }

    /* ----- DDL-specific attributes and methods --------------------------------------------------------------------*/

    public void setDdl(String policyIdMatchRegEx)
    {
        action = ACTIONID_GENERATE_DDL;
        dbPolicyIdMatchRegEx = policyIdMatchRegEx;
    }

    public void setDropFirst(boolean dropFirst)
    {
        createDdlDropSql = dropFirst;
    }

    public void setCreateDdlDropSql(boolean createDdlDropSql)
    {
        this.createDdlDropSql = createDdlDropSql;
    }

    public void generateDdlFiles(SqlManagerComponent component)
    {
        if(destDir == null)
            throw new BuildException("No destDir attribute provide for destination of DDL files.");

        destDir.mkdirs();

        DatabasePolicy[] policies = DatabasePolicies.getInstance().getMatchingPolices(dbPolicyIdMatchRegEx);
        if(policies.length == 0)
            throw new BuildException("Can not generate DDL -- no policies matched '"+ dbPolicyIdMatchRegEx +"'.");

        Schema schema = getSchema(component);
        for(int i = 0; i < policies.length; i++)
        {
            DatabasePolicy policy = policies[i];
            DatabasePolicyValueContext vc = new BasicDatabasePolicyValueContext(policy);

            File ddlFile = new File(destDir, schema.getName() + "-" + policy.getDbmsIdentifier() + getFileExtn(".sql"));
            try
            {
                policy.generateSqlDdl(ddlFile, vc, schema, createDdlDropSql);
            }
            catch (Exception e)
            {
                throw new BuildException("Error generating DDL for " + policy.getDbmsIdentifier() + ": " + e.getMessage(), e);
            }
            log(policy.getDbmsIdentifier() + " DDL file " + ddlFile.getAbsolutePath() + " created for schema '"+ schema.getName() +"'.");
        }
    }

    /* ----- DAL-specific methods -----------------------------------------------------------------------------------*/

    public void setDalPackage(String dalRootPackage)
    {
        this.dalRootPackage = dalRootPackage;
        action = ACTIONID_GENERATE_DAL;
    }

    public void setDalClass(String dalClassNameWithoutPackage)
    {
        this.dalClassNameWithoutPackage = dalClassNameWithoutPackage;
    }

    public void generateDalFiles(SqlManagerComponent component) throws BuildException
    {
        if(dalRootPackage == null)
            throw new BuildException("No dalPackage attribute provided for DAL root package.");

        if(dalClassNameWithoutPackage == null)
            throw new BuildException("No dalClass attribute provided for DAL root class.");

        if(destDir == null)
            throw new BuildException("No destDir attribute provided for destination of DAL files.");
        destDir.mkdirs();

        Schema schema = getSchema(component);
        try
        {
            schema.generateDataAccessLayer(destDir, dalRootPackage, dalClassNameWithoutPackage);
        }
        catch (IOException e)
        {
            throw new BuildException(e);
        }
    }

    /* ----- Utility methods ----------------------------------------------------------------------------------------*/

    public SqlManagerComponent getSqlManagerComponent() throws BuildException
    {
        return (SqlManagerComponent) getComponent(SqlManagerComponent.class);
    }

    /* -------------------------------------------------------------------------------------------------------------*/

    public void setAxiomDtd(File dtdFile)
    {
        action = ACTIONID_GENERATE_AXIOM_DTD;
        this.dtdFile = dtdFile;
    }

    public void execute() throws BuildException
    {
        if(action == ACTIONID_REVERSE_ENGINEER)
        {
            reverseEngineer();
            return;
        }

        SqlManagerComponent component = getSqlManagerComponent();

        if(generateIdentifierConstants(component))
            return;

        switch(action)
        {
            case ACTIONID_GENERATE_AXIOM_DTD:
                try
                {
                    new XmlDataModelDtd().generate(component, dtdFile);
                }
                catch (Exception e)
                {
                    throw new BuildException(e);
                }
                break;

            case ACTIONID_GENERATE_DDL:
                generateDdlFiles(component);
                break;

            case ACTIONID_GENERATE_DAL:
                generateDalFiles(component);
                break;

            case ACTIONID_IMPORT_DATA:
                importData(component);
                break;

            case ACTIONID_GENERATE_IMPORT_DATA_DTD:
                generateImportDtd(component);
                break;

            case ACTIONID_GENERATE_GRAPHVIZ_ERD:
                generateGraphVizErd(component);
                break;

            default:
                // if we get to here and we're not simply producing metrics there's been a problem
                if(! isMetrics()) log("No valid execute action provided ("+ action +").");
        }
    }
}
