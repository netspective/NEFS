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
 * $Id: DataAccessLayerGenerator.java,v 1.6 2003-08-19 04:54:47 aye.thu Exp $
 */

package com.netspective.axiom.schema;

import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.Iterator;
import java.sql.SQLException;
import java.net.URL;

import org.inxar.jenesis.*;

import com.netspective.axiom.schema.table.BasicTable;
import com.netspective.axiom.schema.table.TableQueryDefinition;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.axiom.schema.table.type.EnumerationTableRows;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.axiom.schema.column.BasicColumn;
import com.netspective.axiom.sql.dynamic.QueryDefnSelects;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.lang.ResourceLoader;
import com.netspective.commons.value.Value;

import javax.naming.NamingException;

public class DataAccessLayerGenerator
{
    private Schema.TableTree structure;
    private File rootDir;
    private String rootNameSpace;
    private String dalClassName;
    private VirtualMachine vm;
    private CompilationUnit rootUnit;
    private CompilationUnit modelsUnit;
    private CompilationUnit enumsUnit;
    private PackageClass rootClass;
    private ClassMethod rootClassChildrenAssignmentBlock;
    private Map tableAccessorGenerators = new HashMap(); // key is table instance, value is TableAccessorGenerator instance
    private Map unitImports = new HashMap(); // entry is CompilationUnit instance, value is Set of Classes imported already

    public DataAccessLayerGenerator(Schema.TableTree structure, File rootDir, String rootNameSpace, String dalClassName)
    {
        this.vm = prepareVirtualMachine();
        this.structure = structure;
        this.rootDir = rootDir;
        this.rootNameSpace = rootNameSpace + "." + structure.getSchema().getName().toLowerCase();
        this.dalClassName = dalClassName;
    }

    /**
     * Overrides the normal VirtualMachine.getVirtualMachine() because the static method in the inxar package uses
     * getSystemResource() method call which only looks in the system classpath (which does not work in servlets).
     * @return
     */
    protected VirtualMachine prepareVirtualMachine()
    {
        VirtualMachine result = null;

        try
        {
            URL resourceUrl = ResourceLoader.getResource("com/inxar/jenesis/blockstyle.properties");

            if (resourceUrl == null)
                throw new RuntimeException
                        ("Cannot instantiate VirtualMachine: could not find blockstyle.properties resource ");

            Properties p = new Properties();
            InputStream is = resourceUrl.openStream();
            p.load(new BufferedInputStream(is));
            is.close();
            is = null;

            result = new com.inxar.jenesis.MVM(p);
        }
        catch (IOException ioex)
        {
            RuntimeException rex = new RuntimeException
                    ("Could not load VirtualMachine blockstyles: " + ioex.getMessage());
            ioex.printStackTrace();
            throw rex;
        }

        return result;
    }

    public void addImport(CompilationUnit unit, String importClass)
    {
        Set existingImports = (Set) unitImports.get(unit);
        if (existingImports == null)
        {
            existingImports = new HashSet();
            unitImports.put(unit, existingImports);
        }

        if (!existingImports.contains(importClass))
        {
            unit.addImport(importClass);
            existingImports.add(importClass);
        }
    }

    public void addImport(CompilationUnit unit, Class importClass)
    {
        addImport(unit, importClass.getName());
    }

    public void addImport(PackageClass pkgClass, Class importClass)
    {
        addImport(pkgClass.getUnit(), importClass);
    }

    public void addImport(PackageClass pkgClass, String importClass)
    {
        addImport(pkgClass.getUnit(), importClass);
    }

    public void generate() throws IOException
    {
        rootUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
        rootUnit.setNamespace(rootNameSpace);
        addImport(rootUnit, Schema.class);

        rootClass = rootUnit.newClass(dalClassName);
        rootClass.setAccess(Access.PUBLIC);
        rootClass.isFinal(true);

        ClassField field = rootClass.newField(vm.newType(dalClassName), "INSTANCE");
        field.setAccess(Access.PUBLIC);
        field.isStatic(true);
        field.isFinal(true);
        field.setExpression(vm.newFree("new " + dalClassName + "()"));

        field = rootClass.newField(vm.newType("Schema"), "schema");
        field.setAccess(Access.PRIVATE);

        rootClassChildrenAssignmentBlock = rootClass.newMethod(vm.newType(Type.VOID), "setSchema");
        rootClassChildrenAssignmentBlock.setAccess(Access.PUBLIC);
        rootClassChildrenAssignmentBlock.isFinal(true);
        rootClassChildrenAssignmentBlock.addParameter(vm.newType("Schema"), "schema");
        rootClassChildrenAssignmentBlock.newStmt(vm.newAssign(vm.newVar("this.schema"), vm.newVar("schema")));

        modelsUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
        modelsUnit.setNamespace(rootNameSpace + ".model");

        enumsUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
        enumsUnit.setNamespace(rootNameSpace + ".enum");

        List mainTables = structure.getChildren();
        for (int i = 0; i < mainTables.size(); i++)
            generate((Schema.TableTreeNode) mainTables.get(i), rootClass, rootClassChildrenAssignmentBlock);

        // write out the actual classes to .java files
        for (Iterator i = tableAccessorGenerators.values().iterator(); i.hasNext();)
        {
            TableAccessorGenerator tag = (TableAccessorGenerator) i.next();
            tag.accessorClass.getUnit().encode();
        }

        Tables tables = structure.getSchema().getTables();
        for (int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);
            if (table instanceof EnumerationTable)
                generate((EnumerationTable) table);
        }

        rootUnit.encode();
    }

    public void generate(Schema.TableTreeNode node, PackageClass parentClass, Block childrenAssignmentBlock) throws IOException
    {
        TableAccessorGenerator tag = new TableAccessorGenerator(node, parentClass, childrenAssignmentBlock);
        tableAccessorGenerators.put(node, tag);

        tag.generateImports();
        tag.generateFieldsAndConstructors();
        tag.generateDeclarationsInParent();
        tag.generateGeneralMethods();
        tag.generateTableDataAccessors();
        tag.generateRetrievalByPrimaryKeysMethod();
        tag.generateRecordInnerClass();
        tag.generateRecordsInnerClass();
        tag.generateColumnAccessors();
        tag.generateChildMethodsInParent();

        List children = node.getChildren();
        for (int i = 0; i < children.size(); i++)
            generate((Schema.TableTreeNode) children.get(i), tag.accessorClass, tag.accessorClassConstructorBlock);
    }

    public void generate(EnumerationTable enumTable) throws IOException
    {
        EnumerationTableRows rows = enumTable.getEnums();
        if (rows != null && rows.size() > 0)
        {
            CompilationUnit enumUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            enumUnit.setNamespace(enumsUnit.getNamespace().getName());

            PackageClass enumerationClass = enumUnit.newClass(TextUtils.xmlTextToJavaIdentifier(enumTable.getName(), true));
            enumerationClass.setAccess(Access.PUBLIC);
            enumerationClass.isFinal(true);

            for (int r = 0; r < rows.size(); r++)
            {
                EnumerationTableRow row = (EnumerationTableRow) rows.getRow(r);

                ClassField field = enumerationClass.newField(vm.newType(Type.INT), row.getJavaConstant());
                field.setAccess(Access.PUBLIC);
                field.isFinal(true);
                field.setExpression(vm.newFree(row.getIdAsInteger().toString()));
            }

            enumUnit.encode();
        }
    }

    protected class TableAccessorGenerator
    {
        private Schema.TableTreeNode node;
        private PackageClass parentAccessorClass;
        private Block parentChildAssignmentsBlock;
        private String accessorNameSpace;
        private PackageClass accessorClass;
        private Block accessorClassConstructorBlock;
        private InnerClass recordInnerClass;
        private ClassMethod recordInnerClassRetrieveChildrenMethod;
        private InnerClass recordsInnerClass;
        private String fieldName;
        private String classNameNoPackage;
        private String recordInnerClassNameDecl;
        private String recordsInnerClassNameDecl;
        private String recordInnerClassName;
        private String recordsInnerClassName;
        private boolean customClass;
        private Type tableType;

        public TableAccessorGenerator(Schema.TableTreeNode node, PackageClass parentClass, Block parentChildrenAssignmentsBlock)
        {
            this.node = node;
            this.parentAccessorClass = parentClass;
            this.parentChildAssignmentsBlock = parentChildrenAssignmentsBlock;

            fieldName = TextUtils.xmlTextToJavaIdentifier(node.getTable().getName(), false);
            classNameNoPackage = TextUtils.xmlTextToJavaIdentifier(node.getTable().getName(), true);

            recordInnerClassNameDecl = "Record";
            recordsInnerClassNameDecl = "Records";
            recordInnerClassName = classNameNoPackage + "." + recordInnerClassNameDecl;
            recordsInnerClassName = classNameNoPackage + "." + recordsInnerClassNameDecl;

            this.customClass = false;
            this.tableType = vm.newType(Table.class.getName());
            if (node.getTable().getClass().getClass() != BasicTable.class)
            {
                tableType = vm.newType(node.getTable().getClass().getName());
                customClass = true;
            }

            String ancestorNames = node.getAncestorTableNames(".");

            CompilationUnit unit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            unit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            if (ancestorNames != null)
                accessorNameSpace = modelsUnit.getNamespace().getName() + "." + ancestorNames.toLowerCase();
            else
                accessorNameSpace = modelsUnit.getNamespace().getName();
            unit.setNamespace(accessorNameSpace);

            accessorClass = unit.newClass(classNameNoPackage);
            accessorClass.setAccess(Access.PUBLIC);
            accessorClass.isFinal(true);
        }

        public void generateImports()
        {
            addImport(accessorClass, Schema.class);
            addImport(accessorClass, Row.class);
            addImport(accessorClass, Rows.class);
            addImport(accessorClass, QueryDefnSelects.class);
            addImport(accessorClass, Table.class);
            addImport(accessorClass, NamingException.class);
            addImport(accessorClass, SQLException.class);
            addImport(accessorClass, ConnectionContext.class);
            addImport(accessorClass, QueryDefnSelect.class);
            addImport(accessorClass, ColumnValues.class);
            addImport(accessorClass, ForeignKey.class);
            addImport(accessorClass, ParentForeignKey.class);
        }

        public void generateFieldsAndConstructors()
        {
            ClassField field = accessorClass.newField(vm.newType("Schema"), "schema");
            field.setAccess(Access.PRIVATE);

            field = accessorClass.newField(tableType, "table");
            field.setAccess(Access.PRIVATE);

            field = accessorClass.newField(vm.newType("QueryDefnSelects"), "accessors");
            field.setAccess(Access.PRIVATE);

            org.inxar.jenesis.Constructor constructor = accessorClass.newConstructor();
            constructor.setAccess(Access.PUBLIC);
            constructor.addParameter(vm.newType("Table"), "table");
            Expression tableExpr = vm.newVar("table");
            if (customClass)
                tableExpr = vm.newCast(tableType, tableExpr);
            constructor.newStmt(vm.newAssign(vm.newVar("this.table"), tableExpr));
            constructor.newStmt(vm.newAssign(vm.newVar("this.schema"), vm.newVar("table.getSchema()")));
            constructor.newStmt(vm.newAssign(vm.newVar("this.accessors"), vm.newVar("table.getQueryDefinition().getSelects()")));

            accessorClassConstructorBlock = constructor;
        }

        public void generateDeclarationsInParent()
        {
            addImport(parentAccessorClass, accessorNameSpace + "." + classNameNoPackage);

            ClassField field = parentAccessorClass.newField(vm.newType(classNameNoPackage), fieldName);
            field.setAccess(Access.PRIVATE);
            parentChildAssignmentsBlock.newStmt(vm.newFree(fieldName + " = new " + classNameNoPackage + "(schema.getTables().getByName(\"" + node.getTable().getName() + "\"))"));

            ClassMethod method = parentAccessorClass.newMethod(vm.newType(classNameNoPackage), "get" + classNameNoPackage);
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newReturn().setExpression(vm.newVar(fieldName));
        }

        public void generateChildMethodsInParent()
        {
            Schema.TableTreeNode parentNode = node.getParentNode();
            if (parentNode == null)
                return;

            ParentForeignKey parentFKey = node.getParentForeignKey();
            if (parentFKey == null)
                return;

            // the rest of the code assumes we're a child table with a parent foreign key available
            TableAccessorGenerator parentTag = (TableAccessorGenerator) tableAccessorGenerators.get(parentNode);
            String fKeyVarName = TextUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), false) + "ForeignKey";

            addImport(accessorClass, parentTag.accessorNameSpace + "." + parentTag.classNameNoPackage);

            ClassMethod method = accessorClass.newMethod(vm.newType(recordInnerClassName), "createChildLinkedBy" + TextUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), true));
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType(parentTag.recordInnerClassName), "parentRecord");
            method.newStmt(vm.newFree("return new " + recordInnerClassName + "(table.createRow(" + fKeyVarName + ", parentRecord.getRow()))"));

            method = parentTag.recordInnerClass.newMethod(vm.newType(recordInnerClassName), "create" + classNameNoPackage + "Record");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newStmt(vm.newFree("return " + fieldName + ".createChildLinkedBy" + TextUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), true) + "(this)"));

            ClassField field = parentTag.recordInnerClass.newField(vm.newType(recordsInnerClassName), fieldName + "Records");
            field.setAccess(Access.PRIVATE);

            if (node.hasChildren())
                parentTag.recordInnerClassRetrieveChildrenMethod.newStmt(vm.newFree(fieldName + "Records" + " = get" + classNameNoPackage + "Records(cc, retrieveGrandchildren)"));
            else
                parentTag.recordInnerClassRetrieveChildrenMethod.newStmt(vm.newFree(fieldName + "Records" + " = get" + classNameNoPackage + "Records(cc)"));

            String getParentRecsByFKeyMethodName = "getParentRecordsBy" + TextUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), true);
            method = accessorClass.newMethod(vm.newType(recordsInnerClassName), getParentRecsByFKeyMethodName);
            method.setComment(Comment.D, "Parent reference: " + parentFKey);
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType(parentTag.recordInnerClassName), "parentRecord");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            if (node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");
            method.newStmt((vm.newFree("Records result = new Records(parentRecord, " + fKeyVarName + ".getChildRowsByParentRow(cc, parentRecord.getRow()))")));
            if (node.hasChildren())
            {
                if (node.hasGrandchildren())
                    method.newStmt(vm.newFree("if(retrieveChildren) result.retrieveChildren(cc, retrieveChildren)"));
                else
                    method.newStmt(vm.newFree("if(retrieveChildren) result.retrieveChildren(cc)"));
            }
            method.newReturn().setExpression(vm.newVar("result"));

            String recordsFieldName = fieldName + "Records";
            method = parentTag.recordInnerClass.newMethod(vm.newType(recordsInnerClassName), "get" + classNameNoPackage + "Records");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            if (node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");
            method.newStmt(vm.newFree("if (" + recordsFieldName + " != null) return " + recordsFieldName));
            if (node.hasChildren())
                method.newStmt(vm.newFree(recordsFieldName + " = " + fieldName + "." + getParentRecsByFKeyMethodName + "(this, cc, retrieveChildren)"));
            else
                method.newStmt(vm.newFree(recordsFieldName + " = " + fieldName + "." + getParentRecsByFKeyMethodName + "(this, cc)"));
            method.newReturn().setExpression(vm.newVar(recordsFieldName));

            method = parentTag.recordInnerClass.newMethod(vm.newType(recordsInnerClassName), "get" + classNameNoPackage + "Records");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newReturn().setExpression(vm.newVar(recordsFieldName));

        }

        public void generateGeneralMethods()
        {
            ClassMethod method = accessorClass.newMethod(tableType, "getTable");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newStmt(vm.newFree("return table"));

            method = accessorClass.newMethod(vm.newType("String"), "toString");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newStmt(vm.newFree("return table.toString()"));

            method = accessorClass.newMethod(vm.newType(recordInnerClassName), "createRecord");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newStmt(vm.newFree("return new " + recordInnerClassName + "(table.createRow())"));

            method = accessorClass.newMethod(vm.newType(recordInnerClassName), "getRecord");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType("Row"), "row");
            method.newStmt(vm.newFree("return new " + recordInnerClassName + "(row)"));
        }

        public void generateRetrievalByPrimaryKeysMethod()
        {
            PrimaryKeyColumns pkCols = node.getTable().getPrimaryKeyColumns();
            if (pkCols == null || pkCols.size() == 0)
                return;

            // name is singular for one column and plural for multiple
            String methodName = "getRecordByPrimaryKey";
            if (pkCols.size() > 1)
                methodName += "s";

            ClassMethod method = accessorClass.newMethod(vm.newType(recordInnerClassName), methodName);
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType("ConnectionContext"), "cc");

            StringBuffer callParams = new StringBuffer();
            for (int i = 0; i < pkCols.size(); i++)
            {
                if (i > 0)
                    callParams.append(", ");

                Column pkCol = pkCols.get(i);
                ColumnValue pkColValue = pkCol.constructValueInstance();
                Class pkValueHolderClass = pkColValue.getBindParamValueHolderClass();
                String paramName = TextUtils.xmlTextToJavaIdentifier(pkCol.getName(), false);
                method.addParameter(vm.newType(pkValueHolderClass.getName()), paramName);
                callParams.append(paramName);
            }
            if (node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");

            method.addThrows("NamingException");
            method.addThrows("SQLException");

            method.newStmt(vm.newFree("Row row = table.getRowByPrimaryKeys(cc, new Object[] { " + callParams + " }, null)"));
            method.newStmt(vm.newFree("Record result = row != null ? new " + recordInnerClassName + "(row) : null"));
            if (node.hasChildren())
            {
                if (node.hasGrandchildren())
                    method.newStmt(vm.newFree("if(retrieveChildren && result != null) result.retrieveChildren(cc, true)"));
                else
                    method.newStmt(vm.newFree("if(retrieveChildren && result != null) result.retrieveChildren(cc)"));
            }
            method.newStmt(vm.newFree("return result"));

            addImport(accessorClass, PrimaryKeyColumnValues.class);
            method = accessorClass.newMethod(vm.newType(recordInnerClassName), methodName);
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addParameter(vm.newType("PrimaryKeyColumnValues"), "pkValues");
            if (node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.newStmt(vm.newFree("Row row = table.getRowByPrimaryKeys(cc, pkValues, null)"));
            method.newStmt(vm.newFree("Record result = row != null ? new " + recordInnerClassName + "(row) : null"));
            if (node.hasChildren())
            {
                if (node.hasGrandchildren())
                    method.newStmt(vm.newFree("if(retrieveChildren && result != null) result.retrieveChildren(cc, true)"));
                else
                    method.newStmt(vm.newFree("if(retrieveChildren && result != null) result.retrieveChildren(cc)"));
            }
            method.newStmt(vm.newFree("return result"));
        }

        public void generateRecordInnerClass()
        {
            recordInnerClass = accessorClass.newInnerClass(recordInnerClassNameDecl);
            recordInnerClass.isFinal(true);
            recordInnerClass.setAccess(Access.PUBLIC);

            ClassField field = recordInnerClass.newField(vm.newType("Row"), "row");
            field.setAccess(Access.PRIVATE);

            field = recordInnerClass.newField(vm.newType("ColumnValues"), "values");
            field.setAccess(Access.PRIVATE);

            org.inxar.jenesis.Constructor constructor = recordInnerClass.newConstructor();
            constructor.setAccess(Access.PUBLIC);
            constructor.addParameter(vm.newType("Row"), "row");
            constructor.newStmt(vm.newFree("if(row.getTable() != table) throw new ClassCastException(\"Attempting to assign row from table \"+ row.getTable().getName() +\" to \"+ this.getClass().getName() +\" (expecting a row from table \"+ table.getName() +\").\")"));
            constructor.newStmt(vm.newAssign(vm.newVar("this.row"), vm.newVar("row")));
            constructor.newStmt(vm.newAssign(vm.newVar("this.values"), vm.newVar("row.getColumnValues()")));

            ClassMethod method = recordInnerClass.newMethod(vm.newType("String"), "toString");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newStmt(vm.newFree("return row.toString()"));

            method = recordInnerClass.newMethod(vm.newType("Row"), "getRow");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newReturn().setExpression(vm.newVar("row"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "insert");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.newStmt(vm.newFree("table.insert(cc, row)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "update");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addParameter(vm.newType("String"), "whereCond");
            method.addParameter(vm.newArray("Object", 1), "whereCondBindParams");
            method.newStmt(vm.newFree("table.update(cc, row, whereCond, whereCondBindParams)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "update");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.newStmt(vm.newFree("table.update(cc, row)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "delete");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addParameter(vm.newType("String"), "whereCond");
            method.addParameter(vm.newArray("Object", 1), "whereCondBindParams");
            method.newStmt(vm.newFree("table.delete(cc, row, whereCond, whereCondBindParams)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "delete");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.newStmt(vm.newFree("table.delete(cc, row)"));

            if (node.hasChildren())
            {
                recordInnerClassRetrieveChildrenMethod = recordInnerClass.newMethod(vm.newType(Type.VOID), "retrieveChildren");
                recordInnerClassRetrieveChildrenMethod.setAccess(Access.PUBLIC);
                recordInnerClassRetrieveChildrenMethod.isFinal(true);
                recordInnerClassRetrieveChildrenMethod.addParameter(vm.newType("ConnectionContext"), "cc");
                if (node.hasGrandchildren())
                    recordInnerClassRetrieveChildrenMethod.addParameter(vm.newType(Type.BOOLEAN), "retrieveGrandchildren");
                recordInnerClassRetrieveChildrenMethod.addThrows("NamingException");
                recordInnerClassRetrieveChildrenMethod.addThrows("SQLException");
            }
        }

        public void generateRecordsInnerClass()
        {
            recordsInnerClass = accessorClass.newInnerClass(recordsInnerClassNameDecl);
            recordsInnerClass.isFinal(true);
            recordsInnerClass.setAccess(Access.PUBLIC);

            ClassField field = recordsInnerClass.newField(vm.newType("Rows"), "rows");
            field.setAccess(Access.PRIVATE);

            field = recordsInnerClass.newField(vm.newArray("Record", 1), "cache");
            field.setAccess(Access.PRIVATE);

            org.inxar.jenesis.Constructor constructor = recordsInnerClass.newConstructor();
            constructor.setAccess(Access.PUBLIC);
            constructor.addParameter(vm.newType("Rows"), "rows");
            constructor.newStmt(vm.newAssign(vm.newVar("this.rows"), vm.newVar("rows")));
            constructor.newStmt(vm.newAssign(vm.newVar("this.cache"), vm.newFree("new Record[rows.size()]")));

            if (node.getParentForeignKey() != null)
            {
                TableAccessorGenerator parentTag = (TableAccessorGenerator) tableAccessorGenerators.get(node.getParentNode());

                field = recordsInnerClass.newField(vm.newType(parentTag.recordInnerClassName), "parentRecord");
                field.setAccess(Access.PRIVATE);

                constructor = recordsInnerClass.newConstructor();
                constructor.setAccess(Access.PUBLIC);
                constructor.addParameter(vm.newType(parentTag.recordInnerClassName), "parentRecord");
                constructor.addParameter(vm.newType("Rows"), "rows");
                constructor.newStmt(vm.newFree("this(rows)"));
                constructor.newStmt(vm.newAssign(vm.newVar("this.parentRecord"), vm.newVar("parentRecord")));
            }

            ClassMethod method = recordsInnerClass.newMethod(vm.newType(Type.INT), "size");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newReturn().setExpression(vm.newFree("rows.size()"));

            method = recordsInnerClass.newMethod(vm.newType(recordInnerClassName), "get");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType(Type.INT), "i");
            method.newStmt(vm.newFree("if(cache[i] == null) cache[i] = new Record(rows.getRow(i))"));
            method.newStmt(vm.newFree("return cache[i]"));

            method = recordsInnerClass.newMethod(vm.newType("String"), "toString");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newStmt(vm.newFree("return rows.toString()"));

            if (node.hasChildren())
            {
                method = recordsInnerClass.newMethod(vm.newType(Type.VOID), "retrieveChildren");
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                method.addParameter(vm.newType("ConnectionContext"), "cc");
                if (node.hasGrandchildren())
                    method.addParameter(vm.newType(Type.BOOLEAN), "retrieveGrandchildren");
                method.addThrows("NamingException");
                method.addThrows("SQLException");
                Freeform forLoop = vm.newFree("for(int i = 0; i < cache.length; i++) ");
                if (node.hasGrandchildren())
                    forLoop.write("get(i).retrieveChildren(cc, retrieveGrandchildren)");
                else
                    forLoop.write("get(i).retrieveChildren(cc)");
                method.newStmt(forLoop);
            }
        }

        public void generateTableDataAccessors()
        {
            TableQueryDefinition tqd = node.getTable().getQueryDefinition();
            QueryDefnSelects accessors = tqd.getSelects();

            for (int i = 0; i < accessors.size(); i++)
            {
                QueryDefnSelect accessor = accessors.get(i);

                String constantId = "ACCESSORID_" + TextUtils.xmlTextToJavaConstantTrimmed(accessor.getName());
                ClassField field = accessorClass.newField(vm.newType(Type.INT), constantId);
                field.setAccess(Access.PUBLIC);
                field.isStatic(true);
                field.isFinal(true);
                field.setExpression(vm.newInt(i));

                String methodSuffix = TextUtils.xmlTextToJavaIdentifier(accessor.getName(), true);
                ClassMethod method = accessorClass.newMethod(vm.newType("QueryDefnSelect"), "getAccessor" + methodSuffix);
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                method.newStmt(vm.newFree("return accessors.get(" + constantId + ")"));
            }
        }

        public void generateColumnAccessors()
        {
            Columns columns = node.getTable().getColumns();

            for (int i = 0; i < columns.size(); i++)
            {
                Column column = columns.get(i);

                Type columnType = vm.newType(Column.class.getName());
                if (!column.getClass().getPackage().getName().startsWith(BasicColumn.class.getPackage().getName()))
                    columnType = vm.newType(column.getClass().getName());
                else
                {
                    String stdColClassName = column.getClass().getName();
                    columnType = vm.newType(stdColClassName.substring(stdColClassName.lastIndexOf(".") + 1));
                    addImport(accessorClass, stdColClassName);
                }

                String constantId = "COLINDEX_" + TextUtils.xmlTextToJavaConstantTrimmed(column.getName());
                ClassField field = accessorClass.newField(vm.newType(Type.INT), constantId);
                field.setAccess(Access.PUBLIC);
                field.isStatic(true);
                field.isFinal(true);
                field.setExpression(vm.newInt(column.getIndexInRow()));

                String methodSuffix = TextUtils.xmlTextToJavaIdentifier(column.getName(), true);
                ClassMethod method = accessorClass.newMethod(columnType, "get" + methodSuffix + "Column");
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                Expression columnExpr = vm.newFree("table.getColumns().get(" + constantId + ")");
                columnExpr = vm.newCast(columnType, columnExpr);
                method.newReturn().setExpression(columnExpr);

                ForeignKey fKey = column.getForeignKey();
                if (fKey != null)
                {
                    Type fKeyType = fKey instanceof ParentForeignKey ? vm.newType("ParentForeignKey") : vm.newType("ForeignKey");
                    String fkeyFieldName = TextUtils.xmlTextToJavaIdentifier(column.getName(), false) + "ForeignKey";
                    String fkeyMethodName = TextUtils.xmlTextToJavaIdentifier(column.getName(), true) + "ForeignKey";

                    method = accessorClass.newMethod(fKeyType, "get" + fkeyMethodName);
                    method.setAccess(Access.PUBLIC);
                    method.isFinal(true);
                    method.newReturn().setExpression(vm.newFree(fkeyFieldName));

                    field = accessorClass.newField(fKeyType, fkeyFieldName);
                    if (fKey instanceof ParentForeignKey)
                        accessorClassConstructorBlock.newStmt(vm.newAssign(vm.newVar(fkeyFieldName), vm.newCast(vm.newType("ParentForeignKey"), vm.newVar("table.getColumns().get(" + constantId + ").getForeignKey()"))));
                    else
                        accessorClassConstructorBlock.newStmt(vm.newAssign(vm.newVar(fkeyFieldName), vm.newVar("table.getColumns().get(" + constantId + ").getForeignKey()")));
                    field.setAccess(Access.PRIVATE);
                }

                ColumnValue valueInstance = column.constructValueInstance();
                String valueInstClassName = valueInstance.getClass().getName();

                Type columnValueType = vm.newType(valueInstClassName.replace('$', '.'));
                if (valueInstClassName.startsWith(BasicColumn.class.getPackage().getName()))
                {
                    String stdValueTypeName = valueInstClassName.substring(valueInstClassName.lastIndexOf(".") + 1);
                    columnValueType = vm.newType(stdValueTypeName.replace('$', '.'));

                    String valueInstColClassName = valueInstClassName.substring(0, valueInstClassName.indexOf('$'));
                    addImport(accessorClass, valueInstColClassName);
                }

                method = recordInnerClass.newMethod(columnValueType, "get" + methodSuffix);
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                method.newReturn().setExpression(vm.newCast(columnValueType, vm.newFree("values.getByColumnIndex(" + constantId + ")")));

                Type valueType = vm.newType(Value.class.getName().replace('$', '.'));
                method = recordInnerClass.newMethod(vm.newType(Type.VOID), "set" + methodSuffix);
                method.addParameter(valueType, "value");
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                method.newStmt(vm.newFree("get"+ methodSuffix +"().copyValueByReference(value)"));
            }
        }

    }
}

