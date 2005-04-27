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
 */
package com.netspective.medigy.ant;

import com.netspective.medigy.util.HibernateConfiguration;
import com.netspective.medigy.util.HibernateDiagramFilter;
import com.netspective.medigy.util.HibernateUtil;
import com.netspective.tool.graphviz.GraphvizDiagramGenerator;
import com.netspective.tool.graphviz.GraphvizLayoutType;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGeneratorFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

public class DiagramGeneratorTask extends Task
{
    private static final Log log = LogFactory.getLog(DiagramGeneratorTask.class);

    private String outputDir;
    private String driverClass;
    private String jdbcUrl;
    private String userName;
    private String password;
    private String showSql;
    private String hbm2ddlAuto;
    private String dialectClass;

    private String hibernateConfigFile;

    private String diagramNamePrefix;
    private String graphVizCommandSpec;
    private String outputFileTypes;

    public void execute() throws BuildException
    {
        try
        {
            final HibernateConfiguration hibernateConfiguration = getHibernateConfiguration();
            HibernateUtil.setConfiguration(hibernateConfiguration);

            generateModelDiagrams(hibernateConfiguration);
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }
    }

    protected HibernateConfiguration getHibernateConfiguration() throws HibernateException, FileNotFoundException, IOException
    {
        final HibernateConfiguration config = new HibernateConfiguration();

        final Properties hibProperties = new Properties();
        hibProperties.setProperty(Environment.DIALECT, dialectClass);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".driver_class", driverClass);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".url", jdbcUrl);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".username", userName);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".password", "");
        hibProperties.setProperty(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        hibProperties.setProperty(Environment.SHOW_SQL, showSql);
        config.addProperties(hibProperties);

        for (final Class c : com.netspective.medigy.reference.Catalog.ALL_REFERENCE_TYPES)
            config.addAnnotatedClass(c);

        config.configure(hibernateConfigFile);
        config.registerReferenceEntitiesAndCaches();
        return config;
    }

    protected void generateDiagram(final Configuration configuration,
                                    final String fileName,
                                    final HibernateDiagramGeneratorFilter filter,
                                    final String[] fileTypes) throws IOException
    {
        final File dotFileName = new File(fileName + ".dot");
        final GraphvizDiagramGenerator gdg = new GraphvizDiagramGenerator("MEDIGY", true, GraphvizLayoutType.DOT);
        final HibernateDiagramGenerator hdg = new HibernateDiagramGenerator(configuration, gdg, filter);
        hdg.generate();
        gdg.generateDOTSource(dotFileName);

        for (String type : fileTypes)
        {
            if (System.getProperty("os.name").contains("Windows"))
                Runtime.getRuntime().exec(graphVizCommandSpec + fileName + "." + type + " " + dotFileName);
        }
    }

    protected void generateModelDiagrams(final HibernateConfiguration hibernateConfiguration) throws IOException
    {
        String systemFileSep = System.getProperty("file.separator");
        final File outDir = new File(outputDir);
        final String dialectName = hibernateConfiguration.getProperties().getProperty(Environment.DIALECT);
        final String dialectShortName = dialectName.substring(dialectName.lastIndexOf('.') + 1);

        int i = 0;        
        final StringTokenizer tokenizer = new StringTokenizer(outputFileTypes, ",");
        final String[] typeList = new String[tokenizer.countTokens()];
        while (tokenizer.hasMoreTokens())
        {
            typeList[i++] = tokenizer.nextToken();
        }
        
        // Generate a DOT (GraphViz) diagram so we can visualize the DDL
        // the first version is good for software engineers
        generateDiagram(hibernateConfiguration,
                outDir.getAbsolutePath() + systemFileSep + diagramNamePrefix + dialectShortName + "-se",
                new HibernateDiagramFilter(true, true, true, true), typeList);

        // Generate a DOT (GraphViz) diagram so we can visualize the DDL
        // the second version is good for software engineers looking for general table structure (no column information)
        generateDiagram(hibernateConfiguration,
                outDir.getAbsolutePath() + systemFileSep + diagramNamePrefix + dialectShortName + "-set",
                new HibernateDiagramFilter(false, true, true, true), typeList);

        // the third version is good for database administrators (simple ERD)
        generateDiagram(hibernateConfiguration,
                outDir.getAbsolutePath() + systemFileSep + diagramNamePrefix + dialectShortName + "-erd",
                new HibernateDiagramFilter(true, false, false, false), typeList);
    }

    public void setHibernateConfigFile(String hibernateConfigFile)
    {
        this.hibernateConfigFile = hibernateConfigFile;
    }

    public void setDiagramNamePrefix(String diagramNamePrefix)
    {
        this.diagramNamePrefix = diagramNamePrefix;
    }

    public void setGraphVizCommandSpec(String graphVizCommandSpec)
    {
        this.graphVizCommandSpec = graphVizCommandSpec;
    }

    public void setOutputFileTypes(String outputFileTypes)
    {
        this.outputFileTypes = outputFileTypes;
    }

    public void setDialectClass(String dialectClass)
    {
        this.dialectClass = dialectClass;
    }

    public void setOutputDir(final String outputDir)
    {
        this.outputDir = outputDir;
    }

    public void setDriverClass(final String driverClass)
    {
        this.driverClass = driverClass;
    }

    public void setJdbcUrl(final String jdbcUrl)
    {
        this.jdbcUrl = jdbcUrl;
    }

    public void setUserName(final String userName)
    {
        this.userName = userName;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setShowSql(final String showSql)
    {
        this.showSql = showSql;
    }

    public void setHbm2ddlAuto(final String hbm2ddlAuto)
    {
        this.hbm2ddlAuto = hbm2ddlAuto;
    }

}
