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
 * $Id: DialogExecuteIncludeResourceHandler.java,v 1.3 2003-11-14 19:47:42 shahid.shah Exp $
 */

package com.netspective.sparx.form.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.util.AlternateOutputDestServletResponse;

public class DialogExecuteIncludeResourceHandler extends DialogExecuteDefaultHandler
{
    private static final Log log = LogFactory.getLog(DialogExecuteIncludeResourceHandler.class);
    public static final String REQATTRNAME_DIALOG_CONTEXT = "dialogContext";
    private boolean local;
    private ValueSource path;

    public DialogExecuteIncludeResourceHandler()
    {
    }

    public ValueSource getPath()
    {
        return path;
    }

    public void setPath(ValueSource path)
    {
        this.path = path;
        setLocal(true);
    }

    public void setUrl(ValueSource url)
    {
        this.path = url;
        setLocal(false);
    }

    public boolean isLocal()
    {
        return local;
    }

    public void setLocal(boolean local)
    {
        this.local = local;
    }

    public void executeDialog(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        if (path == null)
        {
            writer.write("No path to resource or URL provided.");
            return;
        }

        String includePath = getPath().getTextValue(dc);
        if(local)
        {
            ServletRequest request = dc.getRequest();
            RequestDispatcher rd = request.getRequestDispatcher(includePath);
            try
            {
                ServletResponse response = dc.getResponse();
                if(writer != response.getWriter())
                    response = new AlternateOutputDestServletResponse(writer, response);
                request.setAttribute(REQATTRNAME_DIALOG_CONTEXT, dc);
                rd.include(request, response);
                request.removeAttribute(REQATTRNAME_DIALOG_CONTEXT);
            }
            catch (ServletException e)
            {
                log.error("Include error in " + this.getClass().getName(), e);
                throw new DialogExecuteException(e);
            }
        }
        else
        {
            URL url = new URL(includePath);
            URLConnection urlConn = url.openConnection();
            InputStream urlIn = urlConn.getInputStream();
            int iRead = urlIn.read();
            while (iRead != -1)
            {
                writer.write((char) iRead);
                iRead = urlIn.read();
            }
        }
    }
}
