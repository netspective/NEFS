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
 * $Id: Reader.java,v 1.1 2003-03-13 18:37:22 shahid.shah Exp $
 */

package com.netspective.junxion.edi.input;

import java.io.IOException;

import com.netspective.junxion.JunxionException;
import com.netspective.junxion.edi.format.InterchangeFormat;
import com.netspective.junxion.edi.format.validation.InputValidator;

/**
 * Interface for reading an EDI document using callbacks.
 *
 * <p><strong>Note:</strong> despite its name, this interface does
 * <em>not</em> extend the standard Java {@link java.io.Reader Reader}
 * interface, because reading EDI is a fundamentally different activity
 * than reading character data.</p>
 *
 * <p>Reader is the interface that an EDI parser's driver must
 * implement.  This interface allows an application to set and
 * query features and properties in the parser, to register
 * event handlers for document processing, and to initiate
 * a document parse.</p>
 *
 * <p>All EDI interfaces are assumed to be synchronous: the
 * {@link #parse parse} methods must not return until parsing
 * is complete, and readers must wait for an event-handler callback
 * to return before reporting the next event.</p>
 */

public interface Reader
{
    /**
     * Look up the value of a feature.
     *
     * <p>Implementors are free (and encouraged) to invent their own features,
     * using names built on their own URIs.</p>
     *
     * @param name The feature name, which is a fully-qualified URI.
     * @return The current state of the feature (true or false).
     * @exception NotRecognizedException When the
     *            Reader does not recognize the feature name.
     * @exception NotSupportedException When the
     *            Reader recognizes the feature name but
     *            cannot determine its value at this time.
     * @see #setFeature
     */
    public boolean getFeature(String name) throws NotRecognizedException, NotSupportedException;


    /**
     * Set the state of a feature.
     *
     * <p>The feature name is any fully-qualified URI.  It is
     * possible for an EDI Reader to recognize a feature name but
     * to be unable to set its value.</p>
     *
     * <p>Some feature values may be immutable or mutable only
     * in specific contexts, such as before, during, or after
     * a parse.</p>
     *
     * @param name The feature name, which is a fully-qualified URI.
     * @param value The requested state of the feature (true or false).
     * @exception NotRecognizedException When the
     *            Reader does not recognize the feature name.
     * @exception NotSupportedException When the
     *            Reader recognizes the feature name but
     *            cannot set the requested value.
     * @see #getFeature
     */
    public void setFeature(String name, boolean value) throws NotRecognizedException, NotSupportedException;


    /**
     * Look up the value of a property.
     *
     * <p>The property name is any fully-qualified URI.  It is
     * possible for an Reader to recognize a property name but
     * to be unable to return its state.</p>
     *
     * <p>Readers are not required to recognize any specific
     * property names.</p>
     *
     * <p>Some property values may be available only in specific
     * contexts, such as before, during, or after a parse.</p>
     *
     * <p>Implementors are free (and encouraged) to invent their own properties,
     * using names built on their own URIs.</p>
     *
     * @param name The property name, which is a fully-qualified URI.
     * @return The current value of the property.
     * @exception NotRecognizedException When the
     *            Reader does not recognize the property name.
     * @exception NotSupportedException When the
     *            Reader recognizes the property name but
     *            cannot determine its value at this time.
     * @see #setProperty
     */
    public Object getProperty(String name) throws NotRecognizedException, NotSupportedException;


    /**
     * Set the value of a property.
     *
     * <p>The property name is any fully-qualified URI.  It is
     * possible for an Reader to recognize a property name but
     * to be unable to set its value.</p>
     *
     * <p>EDI Readers are not required to recognize setting
     * any specific property names.</p>
     *
     * <p>Some property values may be immutable or mutable only
     * in specific contexts, such as before, during, or after
     * a parse.</p>
     *
     * <p>This method is also the standard mechanism for setting
     * extended handlers.</p>
     *
     * @param name The property name, which is a fully-qualified URI.
     * @param value The requested value for the property.
     * @exception NotRecognizedException When the
     *            Reader does not recognize the property name.
     * @exception NotSupportedException When the
     *            Reader recognizes the property name but
     *            cannot set the requested value.
     */
    public void setProperty(String name, Object value) throws NotRecognizedException, NotSupportedException;


    /**
     * Allow an application to register a content event handler.
     *
     * <p>If the application does not register a content handler, all
     * content events reported by the EDI parser will be silently
     * ignored.</p>
     *
     * <p>Applications may register a new or different handler in the
     * middle of a parse, and the EDI parser must begin using the new
     * handler immediately.</p>
     *
     * @param handler The content handler.
     * @exception NullPointerException If the handler
     *            argument is null.
     * @see #getContentHandler
     */
    public void setContentHandler(ContentHandler handler);


    /**
     * Return the current content handler.
     *
     * @return The current content handler, or null if none
     *         has been registered.
     * @see #setContentHandler
     */
    public ContentHandler getContentHandler();


    /**
     * Allow an application to register an error event handler.
     *
     * <p>If the application does not register an error handler, all
     * error events reported by the EDI parser will be silently
     * ignored; however, normal processing may not continue.  It is
     * highly recommended that all EDI applications implement an
     * error handler to avoid unexpected bugs.</p>
     *
     * <p>Applications may register a new or different handler in the
     * middle of a parse, and the EDI parser must begin using the new
     * handler immediately.</p>
     *
     * @param handler The error handler.
     * @exception NullPointerException If the handler
     *            argument is null.
     * @see #getErrorHandler
     */
    public void setErrorHandler(ErrorHandler handler);


    /**
     * Return the current error handler.
     *
     * @return The current error handler, or null if none
     *         has been registered.
     * @see #setErrorHandler
     */
    public ErrorHandler getErrorHandler();

    /**
     * Parse an EDI document.
     *
     * <p>The application can use this method to instruct the EDI
     * reader to begin parsing an EDI document from any valid input
     * source (a character stream, a byte stream, or a URI).</p>
     *
     * <p>Applications may not invoke this method while a parse is in
     * progress (they should create a new Reader instead for each
     * nested EDI document).  Once a parse is complete, an
     * application may reuse the same Reader object, possibly with a
     * different input source.</p>
     *
     * <p>During the parse, the Reader will provide information
     * about the EDI document through the registered event
     * handlers.</p>
     *
     * <p>This method is synchronous: it will not return until parsing
     * has ended.  If a client application wants to terminate
     * parsing early, it should throw an exception.</p>
     *
     * @param input The input source for the top-level of the
     *        EDI document.
     * @exception JunxionException Any EDI exception, possibly
     *            wrapping another exception.
     * @exception IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     * @see InputSource
     * @see #setContentHandler
     * @see #setErrorHandler
     */
    public void parse(InputSource input) throws IOException, JunxionException;
}

