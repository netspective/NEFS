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
 * $Id: DialogExecuteXsltHandler.java,v 1.2 2003-10-11 14:39:27 shahid.shah Exp $
 */

package com.netspective.sparx.form.handler;

import java.io.Writer;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.commons.value.ValueSource;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.parsers.ParserConfigurationException;

public class DialogExecuteXsltHandler extends DialogExecuteDefaultHandler
{
    protected static class StyleSheetParameter
    {
        private String name;
        private ValueSource value;

        public StyleSheetParameter()
        {
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public ValueSource getValue()
        {
            return value;
        }

        public void setValue(ValueSource value)
        {
            this.value = value;
        }
    }

    protected static class StyleSheetParameters
    {
        private List params = new ArrayList();

        public StyleSheetParameters()
        {
        }

        public List getParams()
        {
            return params;
        }

        public StyleSheetParameter createParam()
        {
            return new StyleSheetParameter();
        }

        public void addParam(StyleSheetParameter param)
        {
            params.add(param);
        }
    }

    private static final Log log = LogFactory.getLog(DialogExecuteXsltHandler.class);
    private ValueSource styleSheet;
    private StyleSheetParameters parameters = new StyleSheetParameters();

    public DialogExecuteXsltHandler()
    {
    }

    public ValueSource getStyleSheet()
    {
        return styleSheet;
    }

    public void setStyleSheet(ValueSource styleSheet)
    {
        this.styleSheet = styleSheet;
    }

    public StyleSheetParameters createParameters()
    {
        return parameters;
    }

    public void addParameters(StyleSheetParameters parameters)
    {
        // do nothing, we already created the params
    }

    public void executeDialog(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        if(styleSheet == null)
        {
            writer.write("No style-sheet provided.");
            return;
        }

        try
        {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(styleSheet.getTextValue(dc)));

            List params = parameters.getParams();
            for(int i = 0; i < params.size(); i++)
            {
                StyleSheetParameter param = (StyleSheetParameter) params.get(i);
                transformer.setParameter(param.getName(), param.getValue().getTextValue(dc));
            }

            //TODO: convert the dialog form field values into stylesheet parameters

            transformer.transform
                    (new javax.xml.transform.dom.DOMSource(dc.getAsXmlDocument()),
                            new javax.xml.transform.stream.StreamResult(writer));
        }
        catch(TransformerConfigurationException e)
        {
            log.error("XSLT error in " + this.getClass().getName(), e);
            throw new DialogExecuteException(e);
        }
        catch(TransformerException e)
        {
            log.error("XSLT error in " + this.getClass().getName(), e);
            throw new DialogExecuteException(e);
        }
        catch (ParserConfigurationException e)
        {
            log.error("XSLT error in " + this.getClass().getName(), e);
            throw new DialogExecuteException(e);
        }
    }
}
