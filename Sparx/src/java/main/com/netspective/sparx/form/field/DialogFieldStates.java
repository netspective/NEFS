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
package com.netspective.sparx.form.field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.netspective.sparx.form.DialogContext;

public class DialogFieldStates
{
    private DialogContext dialogContext;
    private Map statesByQualifiedName = new HashMap();

    public DialogFieldStates(DialogContext dialogContext)
    {
        this.dialogContext = dialogContext;
    }

    public Map getStatesByQualifiedName()
    {
        return statesByQualifiedName;
    }

    public void addState(DialogField.State state)
    {
        statesByQualifiedName.put(state.getField().getQualifiedName(), state);
    }

    public DialogField.State getState(DialogField field)
    {
        DialogField.State state = (DialogField.State) statesByQualifiedName.get(field.getQualifiedName());
        if(state == null)
        {
            state = field.constructStateInstance(dialogContext);
            statesByQualifiedName.put(field.getQualifiedName(), state);
        }

        return state;
    }

    public DialogField.State getState(String qualifiedName)
    {
        DialogField field = dialogContext.getDialog().getFields().getByQualifiedName(qualifiedName);
        if(field == null)
            throw new RuntimeException("Field '" + qualifiedName + "' not found in dialog '" + dialogContext.getDialog().getName() + "'.");
        else
            return getState(field);
    }

    public DialogField.State getState(String qualifiedName, DialogField.State defaultValue)
    {
        DialogField field = dialogContext.getDialog().getFields().getByQualifiedName(qualifiedName);
        if(field == null)
            return defaultValue;
        else
            return getState(field);
    }

    public void persistValues()
    {
        Iterator i = statesByQualifiedName.values().iterator();
        while(i.hasNext())
        {
            DialogField.State state = (DialogField.State) i.next();
            state.persistValue();
        }
    }

    /**
     * Given a map of values, assign the value to each field. Each key in the map is
     * a case-sensitive field name (should be the same fully qualified name as the field)
     * and the value is either a String[] or an Object. If the value is an Object, then
     * the toString() method will be called on the object to get a single value. If the
     * value is a String[] then the assignment will be made directly (by reference).
     */
    public void assignFieldValues(DialogContext dc, Map values)
    {
        DialogFields dialogFields = dc.getDialog().getFields();
        for(Iterator i = values.entrySet().iterator(); i.hasNext();)
        {
            String fieldName = (String) ((Map.Entry) i.next()).getKey();
            DialogField field = dialogFields.getByQualifiedName(fieldName);
            DialogField.State state = getState(field);
            state.getValue().setValue(((Map.Entry) i.next()).getValue());
        }
    }

    public void setStateFlag(DialogField field, long flag)
    {
        DialogField.State state = getState(field);
        state.getStateFlags().setFlag(flag);
        DialogFields children = field.getChildren();
        if(children != null)
        {
            for(int i = 0; i < children.size(); i++)
                setStateFlag(children.get(i), flag);
        }
    }

    public void clearStateFlag(DialogField field, long flag)
    {
        DialogField.State state = getState(field);
        state.getStateFlags().clearFlag(flag);
        DialogFields children = field.getChildren();
        if(children != null)
        {
            for(int i = 0; i < children.size(); i++)
                clearStateFlag(children.get(i), flag);
        }
    }

    public void importFromXml(Element parent)
    {
        NodeList children = parent.getChildNodes();
        for(int n = 0; n < children.getLength(); n++)
        {
            Node node = children.item(n);
            if(node.getNodeName().equals("field-state"))
            {
                Element fieldElem = (Element) node;
                String fieldName = fieldElem.getAttribute("name");
                DialogField.State state = getState(fieldName);
                if(state != null)
                    state.importFromXml(fieldElem);
            }
        }
    }

    public void exportToXml(Element parent)
    {
        Iterator i = statesByQualifiedName.values().iterator();
        while(i.hasNext())
        {
            DialogField.State state = (DialogField.State) i.next();
            state.exportToXml(parent);
        }
    }

    public String getAsUrlParams()
    {
        StringBuffer sb = new StringBuffer();
        Iterator i = statesByQualifiedName.values().iterator();
        while(i.hasNext())
        {
            if(sb.length() > 0)
                sb.append('&');

            DialogField.State state = (DialogField.State) i.next();
            state.appendAsUrlParam(sb);
        }
        return sb.toString();
    }

    public Map createTextValuesMap(String fieldNamePrefix)
    {
        Map result = new HashMap();
        Iterator i = statesByQualifiedName.values().iterator();

        if(fieldNamePrefix == null)
        {
            while(i.hasNext())
            {
                DialogField.State state = (DialogField.State) i.next();
                result.put(state.getField().getQualifiedName(), state.getValue().getTextValue());
            }
        }
        else
        {
            while(i.hasNext())
            {
                DialogField.State state = (DialogField.State) i.next();
                result.put(fieldNamePrefix + state.getField().getQualifiedName(), state.getValue().getTextValue());
            }
        }

        return result;
    }

    public int size()
    {
        return statesByQualifiedName.size();
    }

    public String toString()
    {
        return statesByQualifiedName.toString();
    }
}

