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
package com.netspective.axiom.sql.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.netspective.axiom.sql.Queries;
import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.axiom.sql.dynamic.exception.QueryDefnFieldNotFoundException;
import com.netspective.axiom.value.source.QueryDefnFieldsValueSource;
import com.netspective.axiom.value.source.QueryDefnSelectsValueSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class QueryDefinition implements QueriesNameSpace
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[]{"container", "identifier"});
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[]{"presentation"});
    }

    static public class QueryFieldSortInfo
    {
        private QueryDefnField field;
        private boolean isDescending;

        public QueryFieldSortInfo(QueryDefnField field, boolean descending)
        {
            this.field = field;
            isDescending = descending;
        }

        public QueryDefnField getField()
        {
            return field;
        }

        public void setField(QueryDefnField field)
        {
            this.field = field;
        }

        public boolean isDescending()
        {
            return isDescending;
        }

        public void setDescending(boolean descending)
        {
            isDescending = descending;
        }
    }

    private String name;
    private ValueSource defaultDataSource;
    private QueryDefnFields fields = new QueryDefnFields();
    private QueryDefnJoins joins = new QueryDefnJoins();
    private QueryDefnSelects selects = new QueryDefnSelects();
    private QueryDefnConditions defaultConditions = new QueryDefnConditions(null);
    private QueryDefnSqlWhereExpressions defaultWhereExprs = new QueryDefnSqlWhereExpressions();
    private QueryDefnFieldsValueSource fieldsValueSource = new QueryDefnFieldsValueSource(this);
    private QueryDefnSelectsValueSource selectsValueSource = new QueryDefnSelectsValueSource(this);
    private static int DEFAULT_QUERY_DEFINITION_COUNTER = 0;

    public QueryDefinition()
    {
        name = "QueryDefn-" + DEFAULT_QUERY_DEFINITION_COUNTER;
        DEFAULT_QUERY_DEFINITION_COUNTER++;
    }

    public static String translateNameForMapKey(String name)
    {
        if(name == null)
            return null;
        else
            return name.toUpperCase();
    }

    public String getName()
    {
        return name;
    }

    public String getNameForMapKey()
    {
        return translateNameForMapKey(getName());
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Queries getContainer()
    {
        //TODO: figure out what to do for this implementation
        return null;
    }

    public String getNameSpaceId()
    {
        return getName();
    }

    public void setContainer(Queries container)
    {
        //TODO: figure out what to do for this implementation
    }

    public void setNameSpaceId(String identifier)
    {
        setName(identifier);
    }

    public ValueSource getDataSrc()
    {
        return defaultDataSource;
    }

    public void setDataSrc(ValueSource value)
    {
        defaultDataSource = value;
    }

    public QueryDefnFields getFields()
    {
        return fields;
    }

    public QueryDefnJoins getJoins()
    {
        return joins;
    }

    public QueryDefnSelects getSelects()
    {
        return selects;
    }

    public QueryDefnConditions getDefaultConditions()
    {
        return defaultConditions;
    }

    public QueryDefnSqlWhereExpressions getWhereExpressions()
    {
        return defaultWhereExprs;
    }

    public QueryFieldSortInfo[] getFieldsFromDelimitedNames(String names, String delim) throws QueryDefnFieldNotFoundException
    {
        List result = new ArrayList();
        StringTokenizer st = new StringTokenizer(names, delim);
        while(st.hasMoreTokens())
        {
            String fieldName = st.nextToken().trim();
            boolean isDescending = false;
            if(fieldName.startsWith("-"))
            {
                fieldName = fieldName.substring(1);
                isDescending = true;
            }
            QueryDefnField fieldDefn = fields.get(fieldName);
            if(fieldDefn == null)
                throw new QueryDefnFieldNotFoundException(this, fieldName, "Field name '" + fieldName + "' not found in sort definition '" + names + "'");
            result.add(new QueryFieldSortInfo(fieldDefn, isDescending));
        }
        return (QueryFieldSortInfo[]) result.toArray(new QueryFieldSortInfo[result.size()]);
    }

    /*
    public void importFromXml(XmlSource xs, Element elem)
    {
        name = elem.getAttribute("id");

        setDataSource(elem.getAttribute("data-src"));

        List selectElems = new ArrayList();
        List selectDialogElems = new ArrayList();
        List condElems = new ArrayList();
        List whereExprElems = new ArrayList();

        NodeList children = elem.getChildNodes();
        for(int n = 0; n < children.getLength(); n++)
        {
            Node node = children.item(n);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String childName = node.getNodeName();
            if(childName.equals("field"))
            {
                Element fieldElem = (Element) node;
                ClassPath.InstanceGenerator instanceGen = new ClassPath.InstanceGenerator(fieldElem.getAttribute("class"), QueryGenField.class, true);
                QueryGenField field = (QueryGenField) instanceGen.getInstance();
                field.importFromXml(fieldElem);
                defineField(field);
            }
            else if(childName.equals("join"))
            {
                Element joinElem = (Element) node;
                ClassPath.InstanceGenerator instanceGen = new ClassPath.InstanceGenerator(joinElem.getAttribute("class"), QueryGenJoin.class, true);
                QueryGenJoin join = (QueryGenJoin) instanceGen.getInstance();
                join.importFromXml(joinElem);
                defineJoin(join);
            }
            else if(childName.equals("select"))
            {
                selectElems.add(node);
            }
            else if(childName.equals("select-dialog"))
            {
                selectDialogElems.add(node);
            }
            else if(childName.equals("default-condition"))
            {
                condElems.add(node);
            }
            else if(childName.equals("default-where-expr"))
            {
                whereExprElems.add(node);
            }
        }

        finalizeDefn();

        // now that we have all the fields and joins connected, define all
        // conditions that are specified

        if(condElems.size() > 0)
        {
            defaultConditions = new ArrayList();
            for(Iterator i = condElems.iterator(); i.hasNext();)
            {
                Element condElem = (Element) i.next();
                ClassPath.InstanceGenerator instanceGen = new ClassPath.InstanceGenerator(condElem.getAttribute("class"), QueryGenCondition.class, true);
                QueryGenCondition cond = (QueryGenCondition) instanceGen.getInstance();
                cond.importFromXml(this, condElem);
                defineDefaultCondition(cond);
            }
        }

        if(whereExprElems.size() > 0)
        {
            defaultWhereExprs = new ArrayList();
            for(Iterator i = whereExprElems.iterator(); i.hasNext();)
            {
                Element whereExprElem = (Element) i.next();
                ClassPath.InstanceGenerator instanceGen = new ClassPath.InstanceGenerator(whereExprElem.getAttribute("class"), SqlWhereExpression.class, true);
                SqlWhereExpression expr = (SqlWhereExpression) instanceGen.getInstance();
                expr.importFromXml(whereExprElem);
                defineWhereExpression(expr);
            }
        }

        // now that we have all the fields and joins connected, define all
        // selects that are specified

        for(Iterator i = selectElems.iterator(); i.hasNext();)
        {
            Element selectElem = (Element) i.next();
            ClassPath.InstanceGenerator instanceGen = new ClassPath.InstanceGenerator(selectElem.getAttribute("class"), QueryGenSelect.class, true);
            QueryGenSelect select = (QueryGenSelect) instanceGen.getInstance();
            select.setQueryDefn(this);
            select.importFromXml(selectElem);
            defineSelect(select);
        }

        // all the query-specific stuff is now known so try and create all the
        // fixed-condition dialogs

        for(Iterator i = selectDialogElems.iterator(); i.hasNext();)
        {
            QuerySelectDialog dialog = new QuerySelectDialog(this);
            Element dialogElem = (Element) i.next();
            xs.processTemplates(dialogElem);
            dialog.importFromXml(null, dialogElem);
            defineSelectDialog(dialog);
        }
    }
    */

    public void defineWhereExpression(QueryDefnSqlWhereExpression expr)
    {
        defaultWhereExprs.add(expr);
    }

    public void defineDefaultCondition(QueryDefnCondition cond)
    {
        defaultConditions.add(cond);
    }

    public QueryDefnField createField()
    {
        return new QueryDefnField(this);
    }

    public void addField(QueryDefnField field)
    {
        fields.add(field);
    }

    public QueryDefnJoin createJoin()
    {
        return new QueryDefnJoin(this);
    }

    public void addJoin(QueryDefnJoin join)
    {
        joins.add(join);
    }

    public QueryDefnSelect createSelect()
    {
        return new QueryDefnSelect(this);
    }

    public void addSelect(QueryDefnSelect select)
    {
        selects.add(select);
    }

    public void addSelect(QueryDefnSelect select, String[] aliases)
    {
        selects.add(select, aliases);
    }

    public QueryDefnFieldsValueSource getFieldsValueSource()
    {
        return fieldsValueSource;
    }

    public QueryDefnSelectsValueSource getSelectsValueSource()
    {
        return selectsValueSource;
    }
}