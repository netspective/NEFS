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
 * $Id: AntBuildDialog.java,v 1.4 2003-07-08 02:30:09 shahid.shah Exp $
 */

package com.netspective.sparx.console.form;

import java.io.Writer;
import java.io.IOException;
import java.io.File;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.NoBannerLogger;
import org.apache.tools.ant.Main;

import com.netspective.sparx.console.form.ConsoleDialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.form.field.type.SelectField;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.value.ServletValueContext;
import com.netspective.sparx.ant.AntProjects;
import com.netspective.sparx.ant.AntProject;
import com.netspective.sparx.theme.Theme;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.AbstractValue;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.axiom.ConnectionContext;

public class AntBuildDialog extends ConsoleDialog
{
    private static final Log log = LogFactory.getLog(AntBuildDialog.class);
    public static final String REQATTRPARAMNAME_ANT_PROJECT_ID = "ant-project-id";

    private ActiveAntProjectIdValueSource activeAntProjectIdValueSource = new ActiveAntProjectIdValueSource();
    private ActiveAntProjectFileValueSource activeAntProjectFileValueSource = new ActiveAntProjectFileValueSource();
    private ActiveAntProjectTargetsValueSource activeAntProjectTargetsValueSource = new ActiveAntProjectTargetsValueSource();
    private AntTargetsDocumentationPanel activeTargetsDocumentationPanel = new AntTargetsDocumentationPanel();

    /**
     * If a target name starts with one of characters in this string, it will be considered a "private target" and not
     * shown to the user.
     */
    private String privateTargetPrefixChars = "._";
    private boolean showPrivateTargets = false;

    public static Project getConfiguredProject(File buildFile)
    {
        Project project = new Project();
        project.init();
        ProjectHelper.configureProject(project, buildFile);
        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        project.setUserProperty("ant.version", Main.getAntVersion());
        return project;
    }

    public void finalizeContents(NavigationContext nc)
    {
        super.finalizeContents(nc);
        DialogFields fields = getFields();
        fields.getByName("project-id").setDefault(activeAntProjectIdValueSource);
        fields.getByName("project-file").setDefault(activeAntProjectFileValueSource);
        ((SelectField) fields.getByName("target")).setChoices(activeAntProjectTargetsValueSource);
    }

    public void populateValues(DialogContext dc, int formatType)
    {
        super.populateValues(dc, formatType);
        if(dc.isInitialEntry() && formatType == DialogContext.STATECALCSTAGE_INITIAL)
        {
            File projectFile = new File(activeAntProjectFileValueSource.getTextValue(dc));
            if(!projectFile.exists())
                return;

            Project antProject = getConfiguredProject(projectFile);
            dc.getFieldStates().getState("target").getValue().setTextValue(antProject.getDefaultTarget());
        }
    }

    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        DialogContext.DialogFieldStates states = dc.getFieldStates();

        Value projectValue = states.getState("project-file").getValue();
        Value targetValue = states.getState("target").getValue();
        File projectFile = new File(projectValue.getTextValue());

        Project antProject = getConfiguredProject(projectFile);

        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        PrintStream pstream = new PrintStream(ostream);

        BuildLogger logger = new NoBannerLogger();
        logger.setMessageOutputLevel(Project.MSG_INFO);
        logger.setOutputPrintStream(pstream);
        logger.setErrorPrintStream(pstream);

        antProject.addBuildListener(logger);
        try
        {
            antProject.executeTarget(targetValue.getTextValue());
        }
        catch (Exception e)
        {
            StringWriter stackTraceWriter = new StringWriter();
            PrintWriter stackTrace = new PrintWriter(stackTraceWriter);
            e.printStackTrace(stackTrace);
            writer.write("<div class='textbox'>"+ Main.getAntVersion() +"<p><pre>");
            writer.write(stackTraceWriter.toString());
            writer.write("</pre>");
            return;
        }

        writer.write("<div class='textbox'>"+ Main.getAntVersion() +"<p><pre>");
        writer.write(ostream.toString());
        writer.write("</pre>");
    }

    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        super.render(writer, nc, theme, flags);
        File projectFile = new File(activeAntProjectFileValueSource.getTextValue(nc));
        if(! projectFile.exists())
            return;

        writer.write("<p>");
        activeTargetsDocumentationPanel.render(writer, nc, theme, flags);
    }

    public ActiveAntProjectFileValueSource getActiveAntProjectFileValueSource()
    {
        return activeAntProjectFileValueSource;
    }

    public ActiveAntProjectIdValueSource getActiveAntProjectIdValueSource()
    {
        return activeAntProjectIdValueSource;
    }

    public ActiveAntProjectTargetsValueSource getActiveAntProjectTargetsValueSource()
    {
        return activeAntProjectTargetsValueSource;
    }

    public String getPrivateTargetPrefixChars()
    {
        return privateTargetPrefixChars;
    }

    public boolean isPrivateTargetName(String targetName)
    {
        return privateTargetPrefixChars.indexOf(targetName.charAt(0)) != -1;
    }

    public boolean isShowPrivateTargets()
    {
        return showPrivateTargets;
    }

    protected class ActiveAntProjectIdValueSource extends AbstractValueSource
    {
        public PresentationValue getPresentationValue(ValueContext vc)
        {
            return new PresentationValue(getValue(vc));
        }

        public Value getValue(ValueContext vc)
        {
            final ServletValueContext svc = (ServletValueContext)
                    (vc instanceof ConnectionContext ? ((ConnectionContext) vc).getDatabaseValueContext() :
                    vc);

            AntProjects antProjects = svc.getProject().getAntProjects();
            if(antProjects.size() == 1)
                return new GenericValue(antProjects.getByIndex(0).getId());
            else
            {
                String id = (String) svc.getRequest().getAttribute(REQATTRPARAMNAME_ANT_PROJECT_ID);
                if(id == null)
                    id = svc.getRequest().getParameter(REQATTRPARAMNAME_ANT_PROJECT_ID);
                if(id != null)
                {
                    AntProject antProject = antProjects.getById(id);
                    if(antProject != null)
                        return new GenericValue(id);
                    else
                        return new GenericValue("Ant project with id '"+ id +"' not registered.");
                }
                else
                    return new GenericValue("No '"+ REQATTRPARAMNAME_ANT_PROJECT_ID +"' request parameter or request attribute found.");
            }
        }

        public boolean hasValue(ValueContext vc)
        {
            return true;
        }
    }

    protected class ActiveAntProjectFileValueSource extends AbstractValueSource
    {
        public PresentationValue getPresentationValue(ValueContext vc)
        {
            return new PresentationValue(getValue(vc));
        }

        public Value getValue(ValueContext vc)
        {
            final ServletValueContext svc = (ServletValueContext)
                    (vc instanceof ConnectionContext ? ((ConnectionContext) vc).getDatabaseValueContext() :
                    vc);

            AntProjects antProjects = svc.getProject().getAntProjects();
            if(antProjects.size() == 1)
                return new GenericValue(antProjects.getByIndex(0).getFile().getTextValue(vc));
            else
            {
                String id = (String) svc.getRequest().getAttribute(REQATTRPARAMNAME_ANT_PROJECT_ID);
                if(id == null)
                    id = svc.getRequest().getParameter(REQATTRPARAMNAME_ANT_PROJECT_ID);
                if(id != null)
                {
                    AntProject antProject = antProjects.getById(id);
                    if(antProject != null)
                        return new GenericValue(antProject.getFile().getTextValue(vc));
                    else
                        return new GenericValue("Ant project with id '"+ id +"' not registered.");
                }
                else
                    return new GenericValue("No '"+ REQATTRPARAMNAME_ANT_PROJECT_ID +"' request parameter or request attribute found.");
            }

        }

        public boolean hasValue(ValueContext vc)
        {
            return true;
        }
    }

    protected class ActiveAntProjectTargetsValueSource extends AbstractValueSource
    {
        public PresentationValue getPresentationValue(ValueContext vc)
        {
            PresentationValue result = new PresentationValue();
            PresentationValue.Items items = result.createItems();

            File projectFile = new File(activeAntProjectFileValueSource.getTextValue(vc));
            if(!projectFile.exists())
            {
                items.addItem(projectFile + " not found.");
                return result;
            }

            Project project = AntBuildDialog.getConfiguredProject(projectFile);

            String defaultTargetName = project.getDefaultTarget();
            Set sortedTargetNames = new TreeSet(project.getTargets().keySet());
            for(Iterator i = sortedTargetNames.iterator(); i.hasNext(); )
            {
                String targetName = (String) i.next();
                if(! isShowPrivateTargets() && isPrivateTargetName(targetName))
                    continue;
                if(targetName.equals(defaultTargetName))
                    items.addItem(targetName + " (default)", targetName);
                else
                    items.addItem(targetName);
            }

            return result;
        }

        public Value getValue(ValueContext vc)
        {
            final File projectFile = new File(activeAntProjectFileValueSource.getTextValue(vc));
            if(! projectFile.exists())
            {
                return new AbstractValue()
                {
                    public Object getValue()
                    {
                        return getTextValue();
                    }

                    public String getTextValue()
                    {
                        return projectFile + " not found.";
                    }

                    public String[] getTextValues()
                    {
                        return new String[] { getTextValue() };
                    }

                    public List getListValue()
                    {
                        List list = new ArrayList();
                        list.add(getTextValue());
                        return list;
                    }
                };
            }

            Project project = getConfiguredProject(projectFile);

            final Set sortedTargetNames = new TreeSet(project.getTargets().keySet());
            return new AbstractValue()
            {
                public Object getValue()
                {
                    return getTextValue();
                }

                public String getTextValue()
                {
                    // just return the first one if a single value is requested
                    return (String) sortedTargetNames.iterator().next();
                }

                public String[] getTextValues()
                {
                    return (String[]) sortedTargetNames.toArray(new String[sortedTargetNames.size()]);
                }

                public List getListValue()
                {
                    List list = new ArrayList();
                    list.addAll(sortedTargetNames);
                    return list;
                }
            };
        }

        public boolean hasValue(ValueContext vc)
        {
            Value value = getValue(vc);
            return value.getTextValue() != null;
        }
    }
}
