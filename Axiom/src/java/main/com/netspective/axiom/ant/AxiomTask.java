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
package com.netspective.axiom.ant;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecTask;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.DatabasePolicy;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.SqlManagerComponent;
import com.netspective.axiom.connection.DriverManagerConnectionProvider;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Schema.TableTree;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.diagram.DefaultSchemaDiagramFilter;
import com.netspective.axiom.schema.diagram.FocusedTableSchemaDiagramFilter;
import com.netspective.axiom.schema.diagram.GraphvizSchemaDiagramGenerator;
import com.netspective.axiom.schema.transport.DataImportDtd;
import com.netspective.axiom.schema.transport.DataImportParseContext;
import com.netspective.axiom.schema.transport.TableImportStatistic;
import com.netspective.axiom.value.BasicDatabaseConnValueContext;
import com.netspective.axiom.value.BasicDatabasePolicyValueContext;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.value.DatabasePolicyValueContext;
import com.netspective.commons.ant.XdmComponentTask;
import com.netspective.commons.diagram.GraphvizDiagramGenerator;
import com.netspective.commons.diagram.GraphvizLayoutType;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xdm.XdmComponent;
import com.netspective.commons.xdm.XmlDataModelDtd;

public class AxiomTask extends XdmComponentTask
{
    public static final String DEFAULTCLASSNAME_DAL = "DataAccessLayer";

    private String schemaName;
    private String dbPolicyIdMatchRegEx = DatabasePolicies.DBPOLICYIDMATCH_ALL;
    private String fileExtn;
    private boolean createDdlDropSql;
    private boolean createDdlCommentObjects;
    private boolean createAbbreviationsMapCommentBlock;
    private File importFile;
    private File graphVizErdFile;
    private String graphVizErdFocusedFileNameFormat;
    private String graphVizErdIgnoreColumnNames;
    private String graphVizErdIgnoreTableNames;
    private String graphVizErdDotCmdStr = "dot";
    private String graphVizErdDotOutputType = "png";
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
        schemaName = null;
        dbPolicyIdMatchRegEx = DatabasePolicies.DBPOLICYIDMATCH_ALL;
        fileExtn = null;
        createDdlDropSql = false;
        createDdlCommentObjects = true;
        createAbbreviationsMapCommentBlock = true;
        importFile = null;
        graphVizErdFile = null;
        graphVizErdFocusedFileNameFormat = null;
        graphVizErdIgnoreColumnNames = null;
        graphVizErdIgnoreTableNames = null;
        graphVizErdDotCmdStr = "dot";
        graphVizErdDotOutputType = "png";
        dsInfo = null;
        dalRootPackage = null;
        dalClassNameWithoutPackage = DEFAULTCLASSNAME_DAL;
        reverseEngineerDest = null;
        reverseEngineerSchema = null;
        reverseEngineerCatalogPattern = null;
    }

    public void setupActionHandlers()
    {
        super.setupActionHandlers();

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "get-default-schema-name"; }

            public void execute() throws BuildException
            {
                getDefaultSchemaName(getSqlManager());
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "generate-id-constants"; }

            public void execute() throws BuildException
            {
                generateIdentifierConstants(getComponent());
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "generate-dtd"; }

            public void execute() throws BuildException
            {
                try
                {
                    new XmlDataModelDtd().generate(getComponent(), dtdFile);
                }
                catch(Exception e)
                {
                    throw new BuildException(e);
                }
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "generate-ddl"; }

            public void execute() throws BuildException
            {
                generateDdlFiles(getSqlManager());
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "generate-dal"; }

            public void execute() throws BuildException
            {
                generateDalFiles(getSqlManager());
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "import-data"; }

            public void execute() throws BuildException
            {
                importData(getSqlManager());
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "generate-import-data-dtd"; }

            public void execute() throws BuildException
            {
                generateImportDtd(getSqlManager());
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "generate-graphviz-erd"; }

            public void execute() throws BuildException
            {
                generateGraphVizErd(getSqlManager());
            }
        });

        addActionHandler(new ActionHandler()
        {
            public String getName() { return "reverse-engineer-schema"; }

            public void execute() throws BuildException
            {
                reverseEngineer();
            }
        });
    }

    /* ----- reusable attributes and methods ------------------------------------------------------------------------*/

    public String getFileExtn(String defaultExtn)
    {
        return fileExtn == null ? defaultExtn : null;
    }

    public void setFileExtn(String fileExtn)
    {
        this.fileExtn = fileExtn;
    }

    public void getDefaultSchemaName(SqlManager sqlManager) throws BuildException
    {
        Schema schema = getSchema(sqlManager, false);
        if(schema != null)
        {
            String propertyName = getProperty();
            project.setProperty(propertyName != null ? propertyName : "sparx.default.schema.name", schema.getName());
        }
    }

    public Schema getSchema(SqlManager sqlManager, boolean throwExceptionIfNotFound) throws BuildException
    {
        // no schema provided, use the "default"
        if(schemaName == null)
        {
            //if no schema name provided, just use the first one unless there are multiple schemas available
            if(sqlManager.getSchemas().size() > 1)
                throw new BuildException("No schema attribute provided. Available: " + sqlManager.getSchemas().getNames());
            else if(sqlManager.getSchemas().size() == 1)
                return sqlManager.getSchemas().getDefault();
            else
            {
                if(throwExceptionIfNotFound)
                    throw new BuildException("No schemas available.");
                else
                    return null;
            }
        }

        Schema schema = sqlManager.getSchema(schemaName);
        if(schema == null)
            throw new BuildException("Schema '" + schemaName + "' does not exist.");

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
        this.importFile = importFile;
    }

    public void setDtdFile(File dtdFile)
    {
        this.dtdFile = dtdFile;
    }

    public void generateImportDtd(SqlManager sqlManager) throws BuildException
    {
        dtdFile.getParentFile().mkdirs();
        Schema schema = getSchema(sqlManager, true);
        try
        {
            new DataImportDtd().generate(schema, dtdFile);
        }
        catch(IOException e)
        {
            throw new BuildException(e);
        }
        log("Created XML import data dtd " + dtdFile + " for schema '" + schema.getName() + "'.");
    }

    public void importData(SqlManager sqlManager) throws BuildException
    {
        Schema schema = getSchema(sqlManager, true);

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
        catch(Exception e)
        {
            throw new BuildException(e);
        }

        try
        {
            pc = DataImportParseContext.parse(cc, schema, importFile);
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
            catch(SQLException e)
            {
                throw new BuildException(e);
            }
        }

        List errors = pc.getErrors();
        if(pc.getErrors().size() > 0)
        {
            log("Import errors:");
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
            if(!table.isApplicationTable())
                continue;

            TableImportStatistic tis = pc.getStatistics(table);
            if(tis.getSuccessfulRows() > 0)
                log(tis.getTableName() + ": successful rows=" + tis.getSuccessfulRows() + ", unsuccessful rows=" + tis.getUnsuccessfulRows() + ", time=" + tis.getSqlTimeSpent());
        }
    }

    /* ----- GraphViz ERD attributes and methods --------------------------------------------------------------------*/

    public void setGraphVizDot(File file)
    {
        graphVizErdFile = file;
    }

    public void setGraphVizErdFocusedFileNameFormat(String format)
    {
        graphVizErdFocusedFileNameFormat = format;
    }

    public void setGraphVizErdIgnoreColumnNames(String graphVizErdIgnoreColumnNames)
    {
        this.graphVizErdIgnoreColumnNames = graphVizErdIgnoreColumnNames;
    }

    public void setGraphVizErdIgnoreTableNames(String graphVizErdIgnoreTableNames)
    {
        this.graphVizErdIgnoreTableNames = graphVizErdIgnoreTableNames;
    }

    public void generateGraphVizErd(SqlManager sqlManager) throws BuildException
    {
        Schema schema = getSchema(sqlManager, true);
        try
        {
            final DefaultSchemaDiagramFilter defaultSchemaDiagramFilter = new DefaultSchemaDiagramFilter();
            if(graphVizErdIgnoreTableNames != null) defaultSchemaDiagramFilter.setIgnoreTableNamesAndPatterns(graphVizErdIgnoreTableNames);
            if(graphVizErdIgnoreColumnNames != null) defaultSchemaDiagramFilter.setIgnoreColumnNamesAndPatterns(graphVizErdIgnoreColumnNames);

            GraphvizDiagramGenerator diagramGenerator = new GraphvizDiagramGenerator(schema.getName(), true, GraphvizLayoutType.DOT);
            GraphvizSchemaDiagramGenerator schemaDiagramGenerator = new GraphvizSchemaDiagramGenerator(null, schema, diagramGenerator, defaultSchemaDiagramFilter);
            schemaDiagramGenerator.generate();
            diagramGenerator.generateDOTSource(graphVizErdFile);
            log("Created GraphViz digraph " + graphVizErdFile + " for schema '" + schema.getName() + "'.");

            executeDot(graphVizErdFile.getAbsolutePath());

            if(graphVizErdFocusedFileNameFormat != null)
            {
                final TableTree tableTree = schema.getStructure();
                final List children = tableTree.getChildren();
                for(int i = 0; i < children.size(); i++)
                {
                    final Schema.TableTreeNode tableNode = (Schema.TableTreeNode) children.get(i);
                    final Table table = tableNode.getTable();
                    if(!table.isParentTable())
                        continue;

                    final String tableName = table.getName();
                    final File focusedFile = new File(TextUtils.getInstance().replaceTextValues(graphVizErdFocusedFileNameFormat, "{tableName}", tableName));

                    final FocusedTableSchemaDiagramFilter focusedSchemaDiagramFilter = new FocusedTableSchemaDiagramFilter(tableName);
                    if(graphVizErdIgnoreTableNames != null) focusedSchemaDiagramFilter.setIgnoreTableNamesAndPatterns(graphVizErdIgnoreTableNames);
                    if(graphVizErdIgnoreColumnNames != null) focusedSchemaDiagramFilter.setIgnoreColumnNamesAndPatterns(graphVizErdIgnoreColumnNames);

                    diagramGenerator = new GraphvizDiagramGenerator(tableName, true, GraphvizLayoutType.DOT);
                    schemaDiagramGenerator = new GraphvizSchemaDiagramGenerator(null, schema, diagramGenerator, focusedSchemaDiagramFilter);
                    schemaDiagramGenerator.generate();
                    diagramGenerator.generateDOTSource(focusedFile);
                    log("Created GraphViz digraph " + focusedFile + " for schema '" + schema.getName() + "' table '" + tableName + "'.");

                    executeDot(focusedFile.getAbsolutePath());
                }
            }

        }
        catch(IOException e)
        {
            throw new BuildException(e);
        }
        catch(SQLException e)
        {
            throw new BuildException(e);
        }
        catch(NamingException e)
        {
            throw new BuildException(e);
        }
    }

    private String executeDot(final String dotSrcFile) throws BuildException
    {
        if(graphVizErdDotOutputType == null && graphVizErdDotCmdStr == null)
            return null;

        String outFileName = dotSrcFile.substring(0, dotSrcFile.lastIndexOf('.')) + "." + graphVizErdDotOutputType;
        final String cmd = "/c " + graphVizErdDotCmdStr + " -T" + graphVizErdDotOutputType + " -o" + outFileName + " " + dotSrcFile;
        try
        {
            ExecTask execTask = new ExecTask();
            execTask.setProject(getProject());
            execTask.setExecutable("cmd.exe");
            execTask.createArg().setValue(cmd);
            execTask.setOutputproperty(outFileName + "-log");
            execTask.execute();
            log("Created GraphViz diagram " + outFileName + " from " + dotSrcFile);
        }
        catch(BuildException e)
        {
            log("Unable to create GraphViz diagram using cmd.exe " + cmd + ": " + e.getMessage());
        }
        return outFileName;
    }

    /* ----- reverse-engineer specific attributes and methods -------------------------------------------------------*/

    public void setReverseEngineerDest(File reverseEngineerDest)
    {
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
        catch(Exception e)
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
            catch(SQLException e)
            {
                throw new BuildException(e);
            }
        }
    }

    /* ----- DDL-specific attributes and methods --------------------------------------------------------------------*/

    public void setDdl(String policyIdMatchRegEx)
    {
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

    public boolean isCreateDdlCommentObjects()
    {
        return createDdlCommentObjects;
    }

    public void setCreateDdlCommentObjects(boolean createDdlCommentObjects)
    {
        this.createDdlCommentObjects = createDdlCommentObjects;
    }

    public boolean isCreateAbbreviationsMapCommentBlock()
    {
        return createAbbreviationsMapCommentBlock;
    }

    public void setCreateAbbreviationsMapCommentBlock(boolean createAbbreviationsMapCommentBlock)
    {
        this.createAbbreviationsMapCommentBlock = createAbbreviationsMapCommentBlock;
    }

    public void generateDdlFiles(SqlManager sqlManager)
    {
        if(getDestDir() == null)
            throw new BuildException("No destDir attribute provide for destination of DDL files.");

        getDestDir().mkdirs();

        DatabasePolicy[] policies = DatabasePolicies.getInstance().getMatchingPolices(dbPolicyIdMatchRegEx);
        if(policies.length == 0)
            throw new BuildException("Can not generate DDL -- no policies matched '" + dbPolicyIdMatchRegEx + "'.");

        Schema schema = getSchema(sqlManager, true);
        for(int i = 0; i < policies.length; i++)
        {
            DatabasePolicy policy = policies[i];
            DatabasePolicyValueContext vc = new BasicDatabasePolicyValueContext(policy);

            File ddlFile = new File(getDestDir(), schema.getName() + "-" + policy.getDbmsIdentifier() + getFileExtn(".sql"));
            try
            {
                policy.getDdlGenerator().generateSqlDdl(ddlFile, vc, schema, createDdlDropSql, createDdlCommentObjects, createAbbreviationsMapCommentBlock);
            }
            catch(Exception e)
            {
                throw new BuildException("Error generating DDL for " + policy.getDbmsIdentifier() + ": " + e.getMessage(), e);
            }
            log(policy.getDbmsIdentifier() + " DDL file " + ddlFile.getAbsolutePath() + " created for schema '" + schema.getName() + "'.");
        }
    }

    /* ----- DAL-specific methods -----------------------------------------------------------------------------------*/

    public void setDalPackage(String dalRootPackage)
    {
        this.dalRootPackage = dalRootPackage;
    }

    public void setDalClass(String dalClassNameWithoutPackage)
    {
        this.dalClassNameWithoutPackage = dalClassNameWithoutPackage;
    }

    public void generateDalFiles(SqlManager sqlManager) throws BuildException
    {
        if(dalRootPackage == null)
            throw new BuildException("No dalPackage attribute provided for DAL root package.");

        if(dalClassNameWithoutPackage == null)
            throw new BuildException("No dalClass attribute provided for DAL root class.");

        if(getDestDir() == null)
            throw new BuildException("No destDir attribute provided for destination of DAL files.");

        Schema schema = getSchema(sqlManager, true);

        if(isCleanFirst())
            delete(new File(getDestDir(), (dalRootPackage + "." + schema.getName().toLowerCase()).replace('.', '/')));

        getDestDir().mkdirs();

        try
        {
            schema.generateDataAccessLayer(getDestDir(), dalRootPackage, dalClassNameWithoutPackage);
        }
        catch(IOException e)
        {
            throw new BuildException(e);
        }
        log("Created DAL package '" + dalRootPackage + "' for schema '" + schema.getName() + "' in " + getDestDir() + ".");
    }

    /* ----- Utility methods ----------------------------------------------------------------------------------------*/

    public XdmComponent getComponent()
    {
        return getComponent(SqlManagerComponent.class);
    }

    public SqlManager getSqlManager() throws BuildException
    {
        return ((SqlManagerComponent) getComponent()).getManager();
    }
}
