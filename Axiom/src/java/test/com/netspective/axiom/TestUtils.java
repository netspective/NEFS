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
 * $Id: TestUtils.java,v 1.4 2003-06-11 23:24:38 roque.hernandez Exp $
 */

package com.netspective.axiom;

import com.netspective.axiom.connection.DriverManagerConnectionProvider;
import com.netspective.axiom.ant.AxiomTask;
import com.netspective.commons.io.Resource;
import com.netspective.commons.io.FileFind;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.taskdefs.Delete;

public class TestUtils {
   private static final String DB = "medspective";
   public static final String DATASRCID_DEFAULT = "default";
   protected static final DriverManagerConnectionProvider connProvider = new DriverManagerConnectionProvider();

   protected static String schemaFilename = "test-data-schema.xml";
   protected static String dataImportFile = "test-data.xml";
   protected static String destinationTestDataFolder = "/test-data";
   protected static String ddlPath = destinationTestDataFolder + "/ddl";
   protected static String dbPath = destinationTestDataFolder + "/hsqldb-data";
   protected static String dbName = "test";
   protected static String dataImportDtdFile = destinationTestDataFolder + "/import-data.dtd";


   public static Map connProviders = new HashMap();

   //TODO: We could setup a default DB for the whole Axiom and set that as the default DB to get if you don't pass an Id.
   //TODO: Cleanup the file structure (every package will have its own DB, it will pass the package name as an id to the DB)

   static public DriverManagerConnectionProvider getConnProvider(Class connProviderId){
      return getConnProvider(connProviderId.getName());
   }

   static public DriverManagerConnectionProvider getConnProvider(Package connProviderId){
      return getConnProvider(connProviderId.getName());
   }

   static public DriverManagerConnectionProvider getConnProvider(String connProviderId){
      return getConnProvider(connProviderId, false);  //TODO: Really need to think about if the default should false
   }

   static public DriverManagerConnectionProvider getConnProvider(Class connProviderId, boolean reCreateDb){
      return getConnProvider(connProviderId.getName(), reCreateDb);
   }

   static public DriverManagerConnectionProvider getConnProvider(Package connProviderId, boolean reCreateDb){
      return getConnProvider(connProviderId.getName(), reCreateDb);
   }

   static public DriverManagerConnectionProvider getConnProvider(String connProviderId, boolean reCreateDb){


      ConnectionProviderEntry entry = connProvider.getDataSourceEntry(connProviderId);

      if (entry == null || reCreateDb)
         setupDb(connProviderId, true, true);

      return connProvider;
   }

   static public DriverManagerConnectionProvider getConnProvider(String connProviderId, boolean createDb, boolean loadData){


      ConnectionProviderEntry entry = connProvider.getDataSourceEntry(connProviderId);

      if (entry == null)
         setupDb(connProviderId, createDb, loadData);

      return connProvider;
   }



   static public void setupDb(String connProviderId, boolean createDb, boolean loadData) {

      String classDir = connProviderId.replace('.', '/');

      FileFind.FileFindResults ffr = FileFind.findInClasspath(classDir + "/" + schemaFilename, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
      if (!ffr.isFileFound()){
          ffr = FileFind.findInClasspath(classDir + "-" + schemaFilename, FileFind.FINDINPATHFLAG_SEARCH_RECURSIVELY);
      }

      if (!ffr.isFileFound()) {
          return;
      }

      File schemaFile = ffr.getFoundFile();
      String rootPath = schemaFile.getParentFile().getAbsolutePath();

      Project project = new Project();

      Target target = new Target();
      target.setName("generate-ddl");

      AxiomTask task = new AxiomTask();
      task.setTaskName("generate-ddl");
      task.setSchema("db");
      task.setDdl("*");
      task.setXdmFile(new File(rootPath + "/" + schemaFilename));
      task.setDestDir(new File(rootPath + ddlPath));

      target.addTask(task);
      project.addTarget(target);
      task.setProject(project);

      SqlManager manager = task.getSqlManager();
      task.generateDdlFiles(manager);

      if (!createDb)
        return;


      SQLExec sqlExec = new SQLExec();
      target.addTask(sqlExec);
      sqlExec.setProject(project);
      File dbFolder = new File(rootPath + dbPath);

      //need to do a recursive delete or call the deleteTree ant task
      Delete del = new Delete();
      target.addTask(del);
      del.setProject(project);
      del.setDir(dbFolder);
      del.execute();

      dbFolder.mkdir();

      sqlExec.setSrc(new File(rootPath + ddlPath + "/db-hsqldb.sql"));
      sqlExec.setDriver("org.hsqldb.jdbcDriver");
      sqlExec.setUrl("jdbc:hsqldb:" + rootPath + dbPath + "/" + dbName);
      sqlExec.setUserid("sa");
      sqlExec.setPassword("");
      sqlExec.execute();

      //TODO: If we ever nee to generate the dal for a unit test, then we'll have to figure out how to
      //      go up the tree hirearchy and generate the dal at the c:\...\test folder
      //task.setDestDir(new File("C:/Projects/Frameworks/Axiom/src/java/test/com/netspective/axiom"));
      //task.setDalPackage("sql.dal");
      //task.generateDalFiles(manager);
      //System.out.println("TASK: dal");
      //System.out.println("destDir: " + "C:/Projects/Frameworks/Axiom/src/java/test");

      if (!loadData)
        return;

      task.setImport(new File(rootPath + "/" + dataImportFile));
      task.setGenImportDtd(new File(rootPath + dataImportDtdFile));
      task.generateImportDtd(manager);

      task.setDriver("org.hsqldb.jdbcDriver");
      task.setUrl("jdbc:hsqldb:" + rootPath + dbPath + "/" + dbName);
      task.setUserId("sa");
      task.setPassword("");
      task.importData(manager);

      connProvider.addDataSourceInfo(connProviderId, "org.hsqldb.jdbcDriver", "jdbc:hsqldb:" + rootPath + dbPath + File.separator + dbName, "sa", "");

   }
}
