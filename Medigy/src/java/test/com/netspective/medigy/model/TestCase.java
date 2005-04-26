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

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.netspective.medigy.model;

import com.netspective.medigy.util.HibernateConfiguration;
import com.netspective.medigy.util.HibernateUtil;
import com.netspective.medigy.util.ModelInitializer;
import com.netspective.medigy.service.ServiceLocator;
import com.netspective.medigy.service.party.PartyRelationshipFacade;
import com.netspective.medigy.service.party.AddContactMechanismService;
import com.netspective.medigy.service.party.AddContactMechanismServiceImpl;
import com.netspective.medigy.service.party.hibernate.PartyRelationshipFacadeImpl;
import com.netspective.medigy.service.common.ReferenceEntityLookupService;
import com.netspective.medigy.service.common.ReferenceEntityLookupServiceImpl;
import com.netspective.medigy.service.person.PersonFacade;
import com.netspective.medigy.service.person.PatientRegistrationService;
import com.netspective.medigy.service.person.PatientRegistrationServiceImpl;
import com.netspective.medigy.service.person.hibernate.PersonFacadeImpl;
import com.netspective.tool.graphviz.GraphvizDiagramGenerator;
import com.netspective.tool.graphviz.GraphvizLayoutType;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGeneratorFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

public abstract class TestCase extends junit.framework.TestCase
{
    private static final Log log = LogFactory.getLog(TestCase.class);

    protected static File DEFAULT_DB_DIR;
    protected static final String DEFAULT_DB_DIR_SUFFIX = "setup";


    protected String getClassNameWithoutPackage()
    {
        final String pkgAndClassName = getClass().getName();
        final int classNameDelimPos = pkgAndClassName.lastIndexOf('.');
        return classNameDelimPos != -1 ? pkgAndClassName.substring(classNameDelimPos + 1) : pkgAndClassName;
    }

    protected HibernateConfiguration getHibernateConfiguration() throws HibernateException, FileNotFoundException, IOException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Properties logProperties = new Properties();
        logProperties.setProperty("handlers", "java.util.logging.ConsoleHandler");
        logProperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
        logProperties.setProperty("org.hibernate.level", "WARNING");
        logProperties.store(out, "Generated by " + TestCase.class.getName());
        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(out.toByteArray()));

        final HibernateConfiguration config = new HibernateConfiguration();

        final Properties hibProperties = new Properties();
        hibProperties.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".driver_class", "org.hsqldb.jdbcDriver");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".url", "jdbc:hsqldb:" + DEFAULT_DB_DIR + "/db");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".username", "sa");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".password", "");
        hibProperties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        hibProperties.setProperty(Environment.SHOW_SQL, "false");
        config.addProperties(hibProperties);

        for (final Class c : com.netspective.medigy.reference.Catalog.ALL_REFERENCE_TYPES)
            config.addAnnotatedClass(c);

        try
        {
            config.configure("com/netspective/medigy/hibernate.cfg.xml");
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        config.registerReferenceEntitiesAndCaches();
        return config;
    }

    protected void generateDiagram(final Configuration configuration,
                                   final String fileName,
                                   final HibernateDiagramGeneratorFilter filter) throws IOException
    {
        final File dotFileName = new File(fileName + ".dot");
        final GraphvizDiagramGenerator gdg = new GraphvizDiagramGenerator("MEDIGY", true, GraphvizLayoutType.DOT);
        final HibernateDiagramGenerator hdg = new HibernateDiagramGenerator(configuration, gdg, filter);
        hdg.generate();
        gdg.generateDOTSource(dotFileName);

        if (System.getProperty("os.name").contains("Windows"))
            Runtime.getRuntime().exec("c:\\Windows\\system32\\cmd.exe /c C:\\PROGRA~1\\ATT\\Graphviz\\bin\\dot.exe -Tpng -o" + fileName + ".png " + dotFileName);
    }

    protected void loadServiceLocator()
    {
        ServiceLocator.getInstance().loadService(PersonFacade.class, new PersonFacadeImpl());
        ServiceLocator.getInstance().loadService(ReferenceEntityLookupService.class, new ReferenceEntityLookupServiceImpl());
        ServiceLocator.getInstance().loadService(PartyRelationshipFacade.class, new PartyRelationshipFacadeImpl());
        ServiceLocator.getInstance().loadService(PatientRegistrationService.class, new PatientRegistrationServiceImpl());
        ServiceLocator.getInstance().loadService(AddContactMechanismService.class, new AddContactMechanismServiceImpl());
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        String systemTempDir = System.getProperty("java.io.tmpdir");
        String systemFileSep = System.getProperty("file.separator");

        DEFAULT_DB_DIR = new File(systemTempDir + systemFileSep + getClassNameWithoutPackage());
        log.info(DEFAULT_DB_DIR.getAbsolutePath());
        loadServiceLocator();

        final HibernateConfiguration hibernateConfiguration = getHibernateConfiguration();
        HibernateUtil.setConfiguration(hibernateConfiguration);

        new ModelInitializer(HibernateUtil.getSession(),
                             ModelInitializer.SeedDataPopulationType.AUTO,
                             hibernateConfiguration).initialize();

        // Generate the DDL into a file so we can review it
        SchemaExport se = new SchemaExport(hibernateConfiguration);
        final String dialectName = hibernateConfiguration.getProperties().getProperty(Environment.DIALECT);
        final String dialectShortName = dialectName.substring(dialectName.lastIndexOf('.') + 1);
        se.setOutputFile(DEFAULT_DB_DIR.getAbsolutePath() + systemFileSep + "medigy-" + dialectShortName + ".ddl");
        se.create(false, false);

        /*
        // Generate a DOT (GraphViz) diagram so we can visualize the DDL
        // the first version is good for software engineers
        generateDiagram(hibernateConfiguration,
                DEFAULT_DB_DIR.getAbsolutePath() + systemFileSep + "medigy-" + dialectShortName + "-se",
                new HibernateDiagramFilter(true, true, true, true));

        // Generate a DOT (GraphViz) diagram so we can visualize the DDL
        // the second version is good for software engineers looking for general table structure (no column information)
        generateDiagram(hibernateConfiguration,
                DEFAULT_DB_DIR.getAbsolutePath() + systemFileSep + "medigy-" + dialectShortName + "-set",
                new HibernateDiagramFilter(false, true, true, true));

        // the third version is good for database administrators (simple ERD)
        generateDiagram(hibernateConfiguration,
                DEFAULT_DB_DIR.getAbsolutePath() + systemFileSep + "medigy-" + dialectShortName + "-erd",
                new HibernateDiagramFilter(true, false, false, false));
        */

    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        HibernateUtil.closeSession();
    }
}
