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
package com.netspective.sparx.navigate.handler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;

import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;

/**
 * Transforms the given source XML file or resource using the style-sheet file or resource and places the output
 * in the page.
 */

public class NavigationPageHelpBodyHandler extends NavigationPageBodyDefaultHandler
{
    public void handleNavigationPageBody(NavigationPage page, Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        String[] unmatchedPathItems = nc.getActivePathFindResults().getUnmatchedPathItems();
        if (unmatchedPathItems == null || unmatchedPathItems.length == 0)
        {
            writer.write("No unmatched path items.");
            return;
        }

        if (unmatchedPathItems[0].equals("dialog"))
        {
            String dialogName = unmatchedPathItems[1];
            Dialog dialog = nc.getProject().getDialog(dialogName);
            if (dialog != null)
            {
                if (unmatchedPathItems.length > 2)
                {
                    String fieldName = unmatchedPathItems[2];
                    DialogField field = dialog.getFields().getByQualifiedName(fieldName);
                    if (field != null && field.isHelpAvailable())
                        field.getHelpPanel().render(writer, nc, nc.getActiveTheme(), 0);
                    else
                        writer.write("Dialog '" + dialogName + "' does not have a field called '" + fieldName + "'. No help available.");
                }
                else
                    writer.write("Dialog '" + dialogName + "' does not have help ability yet.");
            }
            else
                writer.write("Dialog '" + dialogName + "' not found. No help available.");
        }
    }
}
