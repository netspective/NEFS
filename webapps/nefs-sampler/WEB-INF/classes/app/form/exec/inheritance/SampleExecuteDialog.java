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
package app.form.exec.inheritance;

import java.io.IOException;
import java.io.Writer;

import com.netspective.sparx.Project;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogsPackage;

/**
 * Sample dialog that demonstrates how to obtain input by using multiple <dialog> declarations in project.xml yet
 * creating a single custom dialog to perform the form actions.
 */
public class SampleExecuteDialog extends Dialog
{
    public SampleExecuteDialog(Project project)
    {
        super(project);
    }

    public SampleExecuteDialog(Project project, DialogsPackage pkg)
    {
        super(project, pkg);
    }

    /**
     * The dialog execute method is called as soon as all data is entered and validated. This method is guaranteed to
     * only be called when all fields' data is valid.
     *
     * @param writer The writer where HTML and other output goes -- this is most likely the same as the response stream
     *               but sometimes may be some other writer (if output is being redirected to a file or something).
     * @param dc     The {@see DialogContext} contains all the current user's field values, flags, and state-related data.
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        writer.write("Welcome to NEFS <b>" + dc.getFieldStates().getState("full_name").getValue().getTextValue() +
                     "</b>. ");
        writer.write("You are running the dialog called <b>" + dc.getDialog().getName() + "</b> in package <b>" +
                     dc.getDialog().getNameSpace().getNameSpaceId() + "</b>.");

        // because we're handling both forms (exec1a and exec1b we need to check if we're running the second form)
        if(dc.getDialog().getQualifiedName().equals("form.exec.inheritance.exec1b"))
            writer.write("<p>You are <b>" + dc.getFieldStates().getState("age").getValue().getIntValue() + "</b> " +
                         "years old.");

        writer.write("<p>");
        writer.write("This code demonstrated how you can take two pieces of data and execute an arbritrary class by " +
                     "setting the 'class' attribute of the dialog. ");
        writer.write("This method of execution is called <i>Dialog Inheritance</i>. ");

        String relativePath = "/WEB-INF/classes/app/form/exec/inheritance/SampleExecuteDialog.java";
        String sourceFileLink = dc.getConsoleFileBrowserLinkShowAlt(dc.getServlet().getServletConfig().getServletContext().getRealPath(relativePath),
                                                                    relativePath);
        writer.write("You may review the code at " + sourceFileLink + ".");
        writer.write("<p>");
        writer.write("<a href=\"" + dc.getNavigationContext().getActivePage().getName() + "\">Clear the dialog</a>");
    }
}
