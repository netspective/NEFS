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
 * $Id: JunxionException.java,v 1.1 2003-03-13 18:37:20 shahid.shah Exp $
 */

package com.netspective.junxion;

import java.util.ResourceBundle;

/**
 * Encapsulate a general Junxion error or warning.
 *
 * <p>This class can contain basic error or warning information from
 * either the a parser or the application: a parser writer or
 * application writer can subclass it to provide additional
 * functionality. Junxion handlers may throw this exception or
 * any exception subclassed from it.</p>
 *
 * <p>If the application needs to pass through other types of
 * exceptions, it must wrap those exceptions in a JunxionException
 * or an exception derived from a JunxionException.</p>
 *
 * <p>If the parser or application needs to include information about a
 * specific location in an XML document, it should use the
 * {@link com.netspective.junxion.edi.input.ParseException} subclass.</p>
 *
 */
public class JunxionException extends Exception
{
    /**
     * @serial The embedded exception if tunnelling, or null.
     */
    private Exception exception;

    /**
     * Create a new JunxionException.
     *
     * @param message The error or warning message.
     */
    public JunxionException(String message)
    {
        super(message);
        this.exception = null;
    }

    /**
     * Create a new JunxionException based on a ResourceBundle if available.
     *
     * @param message The error or warning message.
     */
    public JunxionException(ResourceBundle rb, String message)
    {
        super(rb != null ? rb.getString(message) : message);
        this.exception = null;
    }

    /**
     * Create a new JunxionException wrapping an existing exception.
     *
     * <p>The existing exception will be embedded in the new
     * one, and its message will become the default message for
     * the JunxionException.</p>
     *
     * @param e The exception to be wrapped in a JunxionException.
     */
    public JunxionException(Exception e)
    {
        super();
        this.exception = e;
    }


    /**
     * Create a new JunxionException from an existing exception.
     *
     * <p>The existing exception will be embedded in the new
     * one, but the new exception will have its own message.</p>
     *
     * @param message The detail message.
     * @param e The exception to be wrapped in a JunxionException.
     */
    public JunxionException(String message, Exception e)
    {
        super(message);
        this.exception = e;
    }


    /**
     * Return a detail message for this exception.
     *
     * <p>If there is an embedded exception, and if the JunxionException
     * has no detail message of its own, this method will return
     * the detail message from the embedded exception.</p>
     *
     * @return The error or warning message.
     */
    public String getMessage()
    {
        String message = super.getMessage();

        if (message == null && exception != null)
        {
            return exception.getMessage();
        }
        else
        {
            return message;
        }
    }


    /**
     * Return the embedded exception, if any.
     *
     * @return The embedded exception, or null if there is none.
     */
    public Exception getException()
    {
        return exception;
    }


    /**
     * Override toString to pick up any embedded exception.
     *
     * @return A string representation of this exception.
     */
    public String toString()
    {
        if (exception != null)
        {
            return exception.toString();
        }
        else
        {
            return super.toString();
        }
    }
}
