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
 * $Id: HtmlSyntaxHighlightPanel.java,v 1.2 2003-04-21 20:05:17 shahid.shah Exp $
 */

package com.netspective.sparx.panel;

import java.util.Map;
import java.util.HashMap;
import java.io.Writer;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.lang.reflect.Constructor;

import com.Ostermiller.Syntax.Lexer.Lexer;
import com.Ostermiller.Syntax.Lexer.Token;

import com.netspective.sparx.navigate.NavigationContext;

public class HtmlSyntaxHighlightPanel implements HtmlPanel
{
    private static int panelNumber = 0;
    private int height = -1, width = -1;
    private HtmlPanelFrame frame;
    private HtmlPanelBanner banner;
    private String lexerType;
    private String text;
    private File file;
    private String identifier = "HtmlSyntaxHighlightPanel_" + getNextPanelNumber();

    synchronized static private final int getNextPanelNumber()
    {
        return ++panelNumber;
    }

    public HtmlSyntaxHighlightPanel()
    {
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public String getLexerType()
    {
        return lexerType;
    }

    public void setLexerType(String lexerType)
    {
        this.lexerType = lexerType;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public boolean affectsNavigationContext(NavigationContext nc)
    {
        return false;
    }

    public HtmlPanelBanner getBanner()
    {
        return banner;
    }

    public HtmlPanels getChildren()
    {
        return null;
    }

    public HtmlPanelFrame getFrame()
    {
        return frame;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public void render(Writer writer, NavigationContext nc) throws IOException
    {
        if(text != null)
        {
            Reader reader = new StringReader(text);
            emitHtml(lexerType, reader, writer);
        }
        else if(file != null)
        {
            emitHtml(file, writer);
        }
    }

    public void render(Writer writer, NavigationContext nc, HtmlPanelSkin skin) throws IOException
    {
        render(writer, nc);
    }

    public void setBanner(HtmlPanelBanner value)
    {
        banner = value;
    }

    public void setFrame(HtmlPanelFrame rf)
    {
        frame = rf;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    private static Map lexers = new HashMap();

    static
    {
        register(
                com.Ostermiller.Syntax.Lexer.HTMLLexer1.class,
                new String[]{
                    "htm",
                    "html",
                    "xml",
                    "jsp",
                    "xsl",
                }
        );
        register(
                com.Ostermiller.Syntax.Lexer.JavaLexer.class,
                new String[]{
                    "jav",
                    "java",
                }
        );
        register(
                com.Ostermiller.Syntax.Lexer.JavaScriptLexer.class,
                new String[]{
                    "js",
                }
        );
        register(
                com.Ostermiller.Syntax.Lexer.SQLLexer.class,
                new String[]{
                    "sql",
                }
        );
        register(
                com.Ostermiller.Syntax.Lexer.CLexer.class,
                new String[]{
                    "c",
                    "h",
                    "cc",
                    "cpp",
                    "cxx",
                    "c++",
                    "hpp",
                    "hxx",
                    "hh",
                }
        );
        register(
                com.Ostermiller.Syntax.Lexer.PropertiesLexer.class,
                new String[]{
                    "props",
                    "properties",
                }
        );
        register(
                com.Ostermiller.Syntax.Lexer.LatexLexer.class,
                new String[]{
                    "tex",
                    "sty",
                    "cls",
                    "dtx",
                    "ins",
                    "latex",
                }
        );
        register(
                com.Ostermiller.Syntax.Lexer.PlainLexer.class,
                new String[]{
                    "txt",
                    "text",
                }
        );
    }

    /**
     * Register a lexer to handle the given mime types and fileExtensions.
     * <p>
     * If a document has a type "text/plain" it will first match a registered
     * mime type of "text/plain" and then a registered mime type of "text".
     *
     * @param lexer String representing the fully qualified java name of the lexer.
     * @param fileExtensions array of mime types that the lexer can handle.
     * @param fileExtensions array of fileExtensions the lexer can handle. (case insensitive)
     */
    public static void register(Class lexer, String[] fileExtensions)
    {
        for (int i = 0; i < fileExtensions.length; i++)
        {
            lexers.put(fileExtensions[i].toLowerCase(), lexer);
        }
    }

    /**
     * Open a span if needed for the given style.
     *
     * @param description style description.
     * @param out place to write output.
     */
    private static void openSpan(String description, Writer out) throws IOException
    {
        out.write("<span class=" + description + ">");
    }

    /**
     * Close a span if needed for the given style.
     *
     * @param description style description.
     * @param out place to write output.
     */
    private static void closeSpan(String description, Writer out) throws IOException
    {
        out.write("</span>");
    }

    /**
     * Write a highlighted document html fragment.
     *
     * @param lexer Lexer from which to get input.
     * @param out place to write output.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public static void emitHtml(Lexer lexer, Writer out) throws IOException
    {
        String currentDescription = null;
        Token token;
        out.write("<pre>");
        while ((token = lexer.getNextToken()) != null)
        {
            // optimization implemented here:
            // ignored white space can be put in the same span as the stuff
            // around it.  This saves space because spans don't have to be
            // opened and closed.
            if ((token.isWhiteSpace()) || (currentDescription != null && token.getDescription().equals(currentDescription)))
            {
                writeEscapedHTML(token.getContents(), out);
            }
            else
            {
                if (currentDescription != null) closeSpan(currentDescription, out);
                currentDescription = token.getDescription();
                openSpan(currentDescription, out);
                writeEscapedHTML(token.getContents(), out);
            }
        }
        if (currentDescription != null) closeSpan(currentDescription, out);
        out.write("</pre>");
    }

    /**
     * Write a highlighted document html fragment.
     *
     * @param type the fileName to get the source from
     * @param out place to write output.
     * @return true if this file type can be handled
     * @throws java.io.IOException if an I/O error occurs.
     */
    public static boolean emitHtml(String type, Reader in, Writer out) throws IOException
    {
        Class lexerClass = (Class) lexers.get(type);
        if (lexerClass != null)
        {
            try
            {
                Constructor cons = lexerClass.getDeclaredConstructor(new Class[]{Class.forName("java.io.Reader")});
                Lexer lexer = (Lexer) cons.newInstance(new Object[]{in});
                emitHtml(lexer, out);
            }
            catch (Exception e)
            {
                throw new IOException(e.getMessage());
            }
            return true;
        }
        else
            return false;
    }

    /**
     * Write a highlighted document html fragment.
     *
     * @param file the fileName to get the source from
     * @param out place to write output.
     * @return true if this file type can be handled
     * @throws java.io.IOException if an I/O error occurs.
     */
    public static boolean emitHtml(File file, Writer out) throws IOException
    {
        String fileName = file.getAbsolutePath();
        if (file.exists())
        {
            String extn = fileName.substring(fileName.lastIndexOf('.') + 1);
            return emitHtml(extn, new FileReader(file), out);
        }
        else
            throw new IOException("File not found: " + fileName);
    }

    /**
     * Write a highlighted document html fragment.
     *
     * @param fileName the fileName to get the source from
     * @param out place to write output.
     * @return true if this file type can be handled
     * @throws java.io.IOException if an I/O error occurs.
     */
    public static boolean emitHtml(String fileName, Writer out) throws IOException
    {
        return emitHtml(new File(fileName), out);
    }

    /**
     * Write the string after escaping characters that would hinder
     * it from rendering in html.
     *
     * @param text The string to be escaped and written
     * @param out output gets written here
     */
    private static void writeEscapedHTML(String text, Writer out) throws IOException
    {
        for (int i = 0; i < text.length(); i++)
        {
            char ch = text.charAt(i);
            switch (ch)
            {
                case '<':
                        out.write("&lt;");
                        break;

                case '>':
                        out.write("&gt;");
                        break;

                case '&':
                        out.write("&amp;");
                        break;

                case '"':
                        out.write("&quot;");
                        break;

                default:
                        out.write(ch);
                        break;
            }
        }
    }
}
