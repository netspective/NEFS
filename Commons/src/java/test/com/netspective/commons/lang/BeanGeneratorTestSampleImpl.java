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
package com.netspective.commons.lang;

import java.sql.ResultSet;
import java.util.Date;

public class BeanGeneratorTestSampleImpl implements BeanGeneratorTestMutableInterface
{
    private boolean bool;
    private Date date;
    private double dbl;
    private float flt;
    private int i;
    private Integer integer;
    private Object o;
    private String s;
    private char ch;
    private byte b;
    private short shortInt;

    public byte getByte()
    {
        return b;
    }

    public char getChar()
    {
        return ch;
    }

    public short getShort()
    {
        return shortInt;
    }

    public void setByte(byte value)
    {
        this.b = value;
    }

    public void setChar(char value)
    {
        this.ch = value;
    }

    public void setShort(short value)
    {
        this.shortInt = value;
    }

    public boolean getBoolean()
    {
        return bool;
    }

    public Date getDate()
    {
        return date;
    }

    public double getDouble()
    {
        return dbl;
    }

    public float getFloat()
    {
        return flt;
    }

    public int getInt()
    {
        return i;
    }

    public Integer getInteger()
    {
        return integer;
    }

    public Object getObject()
    {
        return o;
    }

    public String getString()
    {
        return s;
    }

    public void setBoolean(boolean bool)
    {
        this.bool = bool;
    }

    public void setDate(Date value)
    {
        this.date = value;
    }

    public void setDouble(double value)
    {
        this.dbl = value;
    }

    public void setFloat(float value)
    {
        this.flt = value;
    }

    public void setInt(int value)
    {
        this.i = value;
    }

    public void setInteger(Integer value)
    {
        this.integer = value;
    }

    public void setObject(Object value)
    {
        this.o = value;
    }

    public void setString(String value)
    {
        this.s = value;
    }

    public void setFieldsToColumnsByIndex(ResultSet rs, ValueBeanFieldIndexTranslator translator)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
