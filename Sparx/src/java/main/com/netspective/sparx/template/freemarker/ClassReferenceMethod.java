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
package com.netspective.sparx.template.freemarker;

import java.util.List;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class ClassReferenceMethod implements TemplateMethodModel
{
    public static final Object[] PRIMITIVES = new Object[]{
        "boolean", java.lang.Boolean.class,
        "char", java.lang.Character.class,
        "byte", java.lang.Byte.class,
        "short", java.lang.Short.class,
        "int", java.lang.Integer.class,
        "long", java.lang.Long.class,
        "float", java.lang.Float.class,
        "double", java.lang.Double.class,
        "void", java.lang.Void.class,
    };

    public Object exec(List args) throws TemplateModelException
    {
        if (args.size() != 1)
            throw new TemplateModelException("Wrong arguments: expect name of class.");

        Class cls = null;

        String fullyQualifiedClassName = (String) args.get(0);
        for (int i = 0; i < PRIMITIVES.length; i += 2)
        {
            if (PRIMITIVES[i].equals(fullyQualifiedClassName))
                cls = (Class) PRIMITIVES[i + 1];
        }

        if (cls == null)
        {
            try
            {
                cls = Class.forName(fullyQualifiedClassName);
            }
            catch (ClassNotFoundException e)
            {
                throw new TemplateModelException("Provided class '" + fullyQualifiedClassName + "' not found", e);
            }
        }

        return BeansWrapper.getDefaultInstance().wrap(cls);
    }
}