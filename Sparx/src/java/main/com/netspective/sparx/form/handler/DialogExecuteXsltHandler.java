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
package com.netspective.sparx.form.handler;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.netspective.commons.text.Transform;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;

public class DialogExecuteXsltHandler extends DialogExecuteDefaultHandler
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private Transform transform = new Transform();
    private boolean writeErrorsToOutput = false;

    public DialogExecuteXsltHandler()
    {
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void addParam(Transform.StyleSheetParameter param)
    {
        transform.addParam(param);
    }

    public void addSystemProperty(Transform.SystemProperty param)
    {
        transform.addSystemProperty(param);
    }

    public Transform.StyleSheetParameter createParam()
    {
        return transform.createParam();
    }

    public Transform.SystemProperty createSystemProperty()
    {
        return transform.createSystemProperty();
    }

    public void setRelativeToClass(Class relativeToClass)
    {
        transform.setRelativeToClass(relativeToClass);
    }

    public void setSourceResource(ValueSource source)
    {
        transform.setSourceResource(source);
    }

    public void setSourceFile(ValueSource source)
    {
        transform.setSourceFile(source);
    }

    public void setStyleSheetResource(ValueSource styleSheet)
    {
        transform.setStyleSheetResource(styleSheet);
    }

    public void setStyleSheetFile(ValueSource styleSheet)
    {
        transform.setStyleSheetFile(styleSheet);
    }

    public boolean isWriteErrorsToOutput()
    {
        return writeErrorsToOutput;
    }

    public void setWriteErrorsToOutput(boolean writeErrorsToOutput)
    {
        this.writeErrorsToOutput = writeErrorsToOutput;
    }

    public void executeDialog(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        try
        {
            Map textValuesMap = dc.getFieldStates().createTextValuesMap("field.");
            transform.render(writer, dc, transform.getSource() != null ? null :
                    new javax.xml.transform.dom.DOMSource(dc.getAsXmlDocument()), textValuesMap, writeErrorsToOutput);
        }
        catch (Exception e)
        {
            dc.getDialog().getLog().error("XSLT error in " + dc.getDialog().getQualifiedName(), e);
            throw new DialogExecuteException(e);
        }
    }
}
