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
package com.netspective.axiom.schema;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inxar.jenesis.AbstractMethod;
import org.inxar.jenesis.Access;
import org.inxar.jenesis.Block;
import org.inxar.jenesis.ClassField;
import org.inxar.jenesis.ClassMethod;
import org.inxar.jenesis.Comment;
import org.inxar.jenesis.CompilationUnit;
import org.inxar.jenesis.Expression;
import org.inxar.jenesis.Freeform;
import org.inxar.jenesis.InnerClass;
import org.inxar.jenesis.Interface;
import org.inxar.jenesis.PackageClass;
import org.inxar.jenesis.Type;
import org.inxar.jenesis.VirtualMachine;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.column.BasicColumn;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.axiom.schema.table.BasicTable;
import com.netspective.axiom.schema.table.TableQueryDefinition;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.schema.table.type.EnumerationTableRows;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.dynamic.QueryDefnSelects;
import com.netspective.commons.lang.ResourceLoader;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.Value;

public class DataAccessLayerGenerator
{
    private static final Log log = LogFactory.getLog(DataAccessLayerGenerator.class);

    private Schema.TableTree structure;
    private File rootDir;
    private String rootNameSpace;
    private String dalClassName;
    private VirtualMachine vm;
    private CompilationUnit rootUnit;
    private CompilationUnit modelsUnit;
    private CompilationUnit enumsUnit;
    private CompilationUnit valueObjectUnit;
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
     */
    protected VirtualMachine prepareVirtualMachine()
    {
        VirtualMachine result = null;

        try
        {
            URL resourceUrl = ResourceLoader.getResource("com/inxar/jenesis/blockstyle.properties");

            if(resourceUrl == null)
                throw new RuntimeException
                        ("Cannot instantiate VirtualMachine: could not find blockstyle.properties resource ");

            Properties p = new Properties();
            InputStream is = resourceUrl.openStream();
            p.load(new BufferedInputStream(is));
            is.close();
            is = null;

            result = new com.inxar.jenesis.MVM(p);
        }
        catch(IOException ioex)
        {
            RuntimeException rex = new RuntimeException
                    ("Could not load VirtualMachine blockstyles: " + ioex.getMessage());
            log.error(rex);
            throw rex;
        }

        return result;
    }

    public void addImport(CompilationUnit unit, String importClass)
    {
        Set existingImports = (Set) unitImports.get(unit);
        if(existingImports == null)
        {
            existingImports = new HashSet();
            unitImports.put(unit, existingImports);
        }

        if(!existingImports.contains(importClass))
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
        rootClass.javadoc("This is the DataAccessLayer type-specific wrapper for the Axiom schema called '" + structure.getSchema().getName() + "'. " +
                          "This schema was generated on " + (new Date()) + " using the source in " + (structure.getSchema().getInputSourceLocator()) + ".");

        ClassField field = rootClass.newField(vm.newType(dalClassName), "INSTANCE");
        field.setAccess(Access.PRIVATE);
        field.isStatic(true);
        field.isFinal(true);
        field.setExpression(vm.newFree("new " + dalClassName + "()"));

        field = rootClass.newField(vm.newType("Schema"), "schema");
        field.setAccess(Access.PRIVATE);

        rootClassChildrenAssignmentBlock = rootClass.newMethod(vm.newType(dalClassName), "getInstance");
        rootClassChildrenAssignmentBlock.setAccess(Access.PUBLIC);
        rootClassChildrenAssignmentBlock.isFinal(true);
        rootClassChildrenAssignmentBlock.isStatic(true);
        rootClassChildrenAssignmentBlock.isSynchronized(true);
        rootClassChildrenAssignmentBlock.newStmt(vm.newFree("if(INSTANCE.schema == null || INSTANCE.schema != com.netspective.axiom.SqlManager.getThreadDefaultSchema()) INSTANCE.setSchema(com.netspective.axiom.SqlManager.getThreadDefaultSchema())"));
        rootClassChildrenAssignmentBlock.newReturn().setExpression(vm.newFree("INSTANCE"));

        rootClassChildrenAssignmentBlock = rootClass.newMethod(vm.newType(dalClassName), "getInstancePrime");
        rootClassChildrenAssignmentBlock.setAccess(Access.PUBLIC);
        rootClassChildrenAssignmentBlock.isFinal(true);
        rootClassChildrenAssignmentBlock.isStatic(true);
        rootClassChildrenAssignmentBlock.newReturn().setExpression(vm.newFree("INSTANCE"));

        rootClassChildrenAssignmentBlock = rootClass.newMethod(vm.newType("Schema"), "getSchema");
        rootClassChildrenAssignmentBlock.setAccess(Access.PUBLIC);
        rootClassChildrenAssignmentBlock.isFinal(true);
        rootClassChildrenAssignmentBlock.newReturn().setExpression(vm.newFree("this.schema"));

        rootClassChildrenAssignmentBlock = rootClass.newMethod(vm.newType(Type.VOID), "setSchema");
        rootClassChildrenAssignmentBlock.setAccess(Access.PUBLIC);
        rootClassChildrenAssignmentBlock.isFinal(true);
        rootClassChildrenAssignmentBlock.addParameter(vm.newType("Schema"), "schema");
        rootClassChildrenAssignmentBlock.newStmt(vm.newAssign(vm.newVar("this.schema"), vm.newVar("schema")));

        modelsUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
        modelsUnit.setNamespace(rootNameSpace + ".dao");

        enumsUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
        enumsUnit.setNamespace(rootNameSpace + ".enum");

        valueObjectUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
        valueObjectUnit.setNamespace(rootNameSpace + ".vo");

        List mainTables = structure.getChildren();
        for(int i = 0; i < mainTables.size(); i++)
            generate((Schema.TableTreeNode) mainTables.get(i), rootClass, rootClassChildrenAssignmentBlock);

        // write out the actual classes to .java files
        for(Iterator i = tableAccessorGenerators.values().iterator(); i.hasNext();)
        {
            TableAccessorGenerator tag = (TableAccessorGenerator) i.next();
            tag.accessorClass.getUnit().encode();
            tag.valueObjectInterface.getUnit().encode();
            tag.valueObjectClass.getUnit().encode();
        }

        Tables tables = structure.getSchema().getTables();
        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);
            if(table instanceof EnumerationTable)
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
        tag.generateValueObjects();

        List children = node.getChildren();
        for(int i = 0; i < children.size(); i++)
            generate((Schema.TableTreeNode) children.get(i), tag.accessorClass, tag.accessorClassConstructorBlock);
    }

    public void generate(EnumerationTable enumTable) throws IOException
    {
        EnumerationTableRows rows = enumTable.getEnums();
        if(rows != null && rows.size() > 0)
        {
            CompilationUnit enumUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            enumUnit.setNamespace(enumsUnit.getNamespace().getName());

            PackageClass enumerationClass = enumUnit.newClass(TextUtils.getInstance().xmlTextToJavaIdentifier(enumTable.getName(), true));
            enumerationClass.setAccess(Access.PUBLIC);
            enumerationClass.isFinal(true);

            for(int r = 0; r < rows.size(); r++)
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
        private Interface valueObjectInterface;
        private PackageClass valueObjectClass;
        private Block accessorClassConstructorBlock;
        private ClassMethod recordInnerClassValueObjectAccessorMethod;
        private ClassMethod recordInnerClassValueObjectMutatorMethod;
        private InnerClass recordInnerClass;
        private ClassMethod recordInnerClassRetrieveChildrenMethod;
        private InnerClass recordsInnerClass;
        private String fieldName;
        private String classNameNoPackage;
        private String recordInnerClassNameDecl;
        private String recordsInnerClassNameDecl;
        private String recordInnerClassName;
        private CompilationUnit valueInterfaceUnit;
        private CompilationUnit valueClassUnit;
        private String valueInterfaceName;
        private String valueClassName;
        private String recordsInnerClassName;
        private boolean customClass;
        private Type tableType;
        private ClassMethod recordInnerClassDeleteChildrenMethod;

        public TableAccessorGenerator(Schema.TableTreeNode node, PackageClass parentClass, Block parentChildrenAssignmentsBlock)
        {
            this.node = node;
            this.parentAccessorClass = parentClass;
            this.parentChildAssignmentsBlock = parentChildrenAssignmentsBlock;

            TextUtils textUtils = TextUtils.getInstance();
            fieldName = textUtils.xmlTextToJavaIdentifier(node.getTable().getName(), false) + "Table";
            classNameNoPackage = textUtils.xmlTextToJavaIdentifier(node.getTable().getName(), true) + "Table";

            recordInnerClassNameDecl = "Record";
            recordsInnerClassNameDecl = "Records";
            recordInnerClassName = classNameNoPackage + "." + recordInnerClassNameDecl;
            recordsInnerClassName = classNameNoPackage + "." + recordsInnerClassNameDecl;

            this.customClass = false;
            this.tableType = vm.newType(Table.class.getName());
            if(node.getTable().getClass().getClass() != BasicTable.class)
            {
                tableType = vm.newType(node.getTable().getClass().getName());
                customClass = true;
            }

            String ancestorNames = node.getAncestorTableNames(".");

            CompilationUnit unit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            unit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            if(ancestorNames != null)
                accessorNameSpace = modelsUnit.getNamespace().getName() + "." + ancestorNames.toLowerCase();
            else
                accessorNameSpace = modelsUnit.getNamespace().getName();
            unit.setNamespace(accessorNameSpace);

            accessorClass = unit.newClass(classNameNoPackage);
            accessorClass.setAccess(Access.PUBLIC);
            accessorClass.isFinal(true);

            valueInterfaceName = textUtils.xmlTextToJavaIdentifier(node.getTable().getName(), true);
            valueClassName = textUtils.xmlTextToJavaIdentifier(node.getTable().getName(), true) + "VO";

            valueInterfaceUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            valueInterfaceUnit.setNamespace(valueObjectUnit.getNamespace().getName());

            valueClassUnit = vm.newCompilationUnit(rootDir.getAbsolutePath());
            valueClassUnit.setNamespace(valueObjectUnit.getNamespace().getName() + ".impl");
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
            addImport(accessorClass, QueryResultSet.class);
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
            if(customClass)
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
            if(parentNode == null)
                return;

            ParentForeignKey parentFKey = node.getParentForeignKey();
            if(parentFKey == null)
                return;

            TextUtils textUtils = TextUtils.getInstance();

            // the rest of the code assumes we're a child table with a parent foreign key available
            TableAccessorGenerator parentTag = (TableAccessorGenerator) tableAccessorGenerators.get(parentNode);
            String fKeyVarName = textUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), false) + "ForeignKey";

            addImport(accessorClass, parentTag.accessorNameSpace + "." + parentTag.classNameNoPackage);

            ClassMethod method = accessorClass.newMethod(vm.newType(recordInnerClassName), "createChildLinkedBy" + textUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), true));
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType(parentTag.recordInnerClassName), "parentRecord");
            method.newStmt(vm.newFree("return new " + recordInnerClassName + "(table.createRow(" + fKeyVarName + ", parentRecord.getRow()))"));

            method = parentTag.recordInnerClass.newMethod(vm.newType(recordInnerClassName), "create" + classNameNoPackage + "Record");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newStmt(vm.newFree("return " + fieldName + ".createChildLinkedBy" + textUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), true) + "(this)"));

            ClassField field = parentTag.recordInnerClass.newField(vm.newType(recordsInnerClassName), fieldName + "Records");
            field.setAccess(Access.PRIVATE);

            if(node.hasChildren())
            {
                parentTag.recordInnerClassRetrieveChildrenMethod.newStmt(vm.newFree(fieldName + "Records" + " = get" + classNameNoPackage + "Records(cc, retrieveGrandchildren)"));
                parentTag.recordInnerClassDeleteChildrenMethod.newStmt(vm.newFree(fieldName + "Records.delete(cc)"));
            }
            else
            {
                parentTag.recordInnerClassRetrieveChildrenMethod.newStmt(vm.newFree(fieldName + "Records" + " = get" + classNameNoPackage + "Records(cc)"));
                parentTag.recordInnerClassDeleteChildrenMethod.newStmt(vm.newFree(fieldName + "Records.delete(cc)"));
            }

            String getParentRecsByFKeyMethodName = "getParentRecordsBy" + textUtils.xmlTextToJavaIdentifier(parentFKey.getSourceColumns().getOnlyNames("And"), true);
            method = accessorClass.newMethod(vm.newType(recordsInnerClassName), getParentRecsByFKeyMethodName);
            method.setComment(Comment.D, "Parent reference: " + parentFKey);
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType(parentTag.recordInnerClassName), "parentRecord");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            if(node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");
            method.newStmt((vm.newFree("Records result = new Records(parentRecord, " + fKeyVarName + ".getChildRowsByParentRow(cc, parentRecord.getRow()))")));
            if(node.hasChildren())
            {
                if(node.hasGrandchildren())
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
            if(node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");
            if(node.hasChildren())
                method.newStmt(vm.newFree(recordsFieldName + " = " + fieldName + "." + getParentRecsByFKeyMethodName + "(this, cc, retrieveChildren)"));
            else
                method.newStmt(vm.newFree(recordsFieldName + " = " + fieldName + "." + getParentRecsByFKeyMethodName + "(this, cc)"));
            method.newReturn().setExpression(vm.newVar(recordsFieldName));

            method = parentTag.recordInnerClass.newMethod(vm.newType(recordsInnerClassName), "get" + classNameNoPackage + "Records");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addParameter(vm.newType(Type.BOOLEAN), "useCachedIfAvailable");
            if(node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");
            method.newStmt(vm.newFree("if (useCachedIfAvailable && " + recordsFieldName + " != null) return " + recordsFieldName));
            if(node.hasChildren())
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

            method = accessorClass.newMethod(vm.newType(recordsInnerClassName), "getAccessorRecords");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addParameter(vm.newType("QueryDefnSelect"), "accessor");
            method.addParameter(vm.newType("Object[]"), "bindValues");
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.newStmt(vm.newFree("Rows rows = getTable().createRows()"));
            method.newStmt(vm.newFree("QueryResultSet qrs = accessor.execute(cc, bindValues, false)"));
            method.newStmt(vm.newFree("if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet())"));
            method.newStmt(vm.newFree("qrs.close(false)"));
            method.newStmt(vm.newFree("return new Records(rows)"));
        }

        public void generateRetrievalByPrimaryKeysMethod()
        {
            PrimaryKeyColumns pkCols = node.getTable().getPrimaryKeyColumns();
            if(pkCols == null || pkCols.size() == 0)
                return;

            // name is singular for one column and plural for multiple
            String methodName = "getRecordByPrimaryKey";
            if(pkCols.size() > 1)
                methodName += "s";

            ClassMethod method = accessorClass.newMethod(vm.newType(recordInnerClassName), methodName);
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType("ConnectionContext"), "cc");

            TextUtils textUtils = TextUtils.getInstance();

            StringBuffer callParams = new StringBuffer();
            for(int i = 0; i < pkCols.size(); i++)
            {
                if(i > 0)
                    callParams.append(", ");

                Column pkCol = pkCols.get(i);
                ColumnValue pkColValue = pkCol.constructValueInstance();
                Class pkValueHolderClass = pkColValue.getBindParamValueHolderClass();
                String paramName = textUtils.xmlTextToJavaIdentifier(pkCol.getName(), false);
                method.addParameter(vm.newType(pkValueHolderClass.getName()), paramName);
                callParams.append(paramName);
            }
            if(node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");

            method.addThrows("NamingException");
            method.addThrows("SQLException");

            method.newStmt(vm.newFree("Row row = table.getRowByPrimaryKeys(cc, new Object[] { " + callParams + " }, null)"));
            method.newStmt(vm.newFree("Record result = row != null ? new " + recordInnerClassName + "(row) : null"));
            if(node.hasChildren())
            {
                if(node.hasGrandchildren())
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
            if(node.hasChildren())
                method.addParameter(vm.newType(Type.BOOLEAN), "retrieveChildren");
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.newStmt(vm.newFree("Row row = table.getRowByPrimaryKeys(cc, pkValues, null)"));
            method.newStmt(vm.newFree("Record result = row != null ? new " + recordInnerClassName + "(row) : null"));
            if(node.hasChildren())
            {
                if(node.hasGrandchildren())
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
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.newStmt(vm.newFree("table.insert(cc, row)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "update");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addParameter(vm.newType("String"), "whereCond");
            method.addParameter(vm.newArray("Object", 1), "whereCondBindParams");
            method.newStmt(vm.newFree("table.update(cc, row, whereCond, whereCondBindParams)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "update");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.newStmt(vm.newFree("table.update(cc, row)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "delete");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addParameter(vm.newType("String"), "whereCond");
            method.addParameter(vm.newArray("Object", 1), "whereCondBindParams");
            method.newStmt(vm.newFree("table.delete(cc, row, whereCond, whereCondBindParams)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "delete");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            // TODO: Need to see if deleting the children should be a default behavior or not. If so, then this method needs to be moved below after initializing  recordInnerClassDeleteChildrenMethod
            //method.newStmt(vm.newFree(recordInnerClassDeleteChildrenMethod + "(cc)"));
            method.newStmt(vm.newFree("table.delete(cc, row)"));

            method = recordInnerClass.newMethod(vm.newType(Type.BOOLEAN), "dataChangedInStorage");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.newReturn().setExpression(vm.newFree("table.dataChangedInStorage(cc, row)"));

            method = recordInnerClass.newMethod(vm.newType(Type.VOID), "refresh");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addThrows("NamingException");
            method.addThrows("SQLException");
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.newStmt(vm.newFree("table.refreshData(cc, row)"));

            Type valueObjectIntefaceType = vm.newType(valueInterfaceUnit.getNamespace().getName() + '.' + valueInterfaceName);

            method = recordInnerClass.newMethod(valueObjectIntefaceType, "getValues");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.newReturn().setExpression(vm.newFree("getValues(new " + valueClassUnit.getNamespace().getName() + '.' + valueClassName + "())"));

            recordInnerClassValueObjectAccessorMethod = recordInnerClass.newMethod(valueObjectIntefaceType, "getValues");
            recordInnerClassValueObjectAccessorMethod.setAccess(Access.PUBLIC);
            recordInnerClassValueObjectAccessorMethod.isFinal(true);
            recordInnerClassValueObjectAccessorMethod.addParameter(valueObjectIntefaceType, "valueObject");

            recordInnerClassValueObjectMutatorMethod = recordInnerClass.newMethod(vm.newType(Type.VOID), "setValues");
            recordInnerClassValueObjectMutatorMethod.setAccess(Access.PUBLIC);
            recordInnerClassValueObjectMutatorMethod.isFinal(true);
            recordInnerClassValueObjectMutatorMethod.addParameter(valueObjectIntefaceType, "valueObject");

            if(node.hasChildren())
            {
                recordInnerClassRetrieveChildrenMethod = recordInnerClass.newMethod(vm.newType(Type.VOID), "retrieveChildren");
                recordInnerClassRetrieveChildrenMethod.setAccess(Access.PUBLIC);
                recordInnerClassRetrieveChildrenMethod.isFinal(true);
                recordInnerClassRetrieveChildrenMethod.addParameter(vm.newType("ConnectionContext"), "cc");
                if(node.hasGrandchildren())
                    recordInnerClassRetrieveChildrenMethod.addParameter(vm.newType(Type.BOOLEAN), "retrieveGrandchildren");
                recordInnerClassRetrieveChildrenMethod.addThrows("NamingException");
                recordInnerClassRetrieveChildrenMethod.addThrows("SQLException");

                // create a method for deleting all children
                recordInnerClassDeleteChildrenMethod = recordInnerClass.newMethod(vm.newType(Type.VOID), "deleteChildren");
                recordInnerClassDeleteChildrenMethod.setAccess(Access.PUBLIC);
                recordInnerClassDeleteChildrenMethod.isFinal(true);
                recordInnerClassDeleteChildrenMethod.addParameter(vm.newType("ConnectionContext"), "cc");
                //if (node.hasGrandchildren())
                //    recordInnerClassDeleteChildrenMethod.addParameter(vm.newType(Type.BOOLEAN), "retrieveGrandchildren");
                recordInnerClassDeleteChildrenMethod.addThrows("NamingException");
                recordInnerClassDeleteChildrenMethod.addThrows("SQLException");
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

            if(node.getParentForeignKey() != null)
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

            method = recordsInnerClass.newMethod(vm.newType(Type.VOID), "delete");
            method.setAccess(Access.PUBLIC);
            method.isFinal(true);
            method.addParameter(vm.newType("ConnectionContext"), "cc");
            method.addThrows("SQLException");
            Freeform forLoop = vm.newFree("for(int i = 0; i < cache.length; i++)");
            forLoop.write("get(i).delete(cc)");
            method.newStmt(forLoop);

            if(node.hasChildren())
            {
                method = recordsInnerClass.newMethod(vm.newType(Type.VOID), "retrieveChildren");
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                method.addParameter(vm.newType("ConnectionContext"), "cc");
                if(node.hasGrandchildren())
                    method.addParameter(vm.newType(Type.BOOLEAN), "retrieveGrandchildren");
                method.addThrows("NamingException");
                method.addThrows("SQLException");
                forLoop = vm.newFree("for(int i = 0; i < cache.length; i++) ");
                if(node.hasGrandchildren())
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
            TextUtils textUtils = TextUtils.getInstance();

            for(int i = 0; i < accessors.size(); i++)
            {
                QueryDefnSelect accessor = accessors.get(i);

                String constantId = "ACCESSORID_" + textUtils.xmlTextToJavaConstantTrimmed(accessor.getName());
                ClassField field = accessorClass.newField(vm.newType(Type.INT), constantId);
                field.setAccess(Access.PUBLIC);
                field.isStatic(true);
                field.isFinal(true);
                field.setExpression(vm.newInt(i));

                String methodSuffix = textUtils.xmlTextToJavaIdentifier(accessor.getName(), true);
                ClassMethod method = accessorClass.newMethod(vm.newType("QueryDefnSelect"), "getAccessor" + methodSuffix);
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                method.newStmt(vm.newFree("return accessors.get(" + constantId + ")"));
            }
        }

        public void generateColumnAccessors()
        {
            Columns columns = node.getTable().getColumns();
            TextUtils textUtils = TextUtils.getInstance();

            for(int i = 0; i < columns.size(); i++)
            {
                Column column = columns.get(i);

                Type columnType = vm.newType(Column.class.getName());
                if(!column.getClass().getPackage().getName().startsWith(BasicColumn.class.getPackage().getName()))
                    columnType = vm.newType(column.getClass().getName());
                else
                {
                    String stdColClassName = column.getClass().getName();
                    columnType = vm.newType(stdColClassName.substring(stdColClassName.lastIndexOf(".") + 1));
                    addImport(accessorClass, stdColClassName);
                }

                String constantId = "COLINDEX_" + textUtils.xmlTextToJavaConstantTrimmed(column.getName());
                ClassField field = accessorClass.newField(vm.newType(Type.INT), constantId);
                field.setAccess(Access.PUBLIC);
                field.isStatic(true);
                field.isFinal(true);
                field.setExpression(vm.newInt(column.getIndexInRow()));

                String methodSuffix = textUtils.xmlTextToJavaIdentifier(column.getName(), true);
                ClassMethod method = accessorClass.newMethod(columnType, "get" + methodSuffix + "Column");
                method.setAccess(Access.PUBLIC);
                method.isFinal(true);
                Expression columnExpr = vm.newFree("table.getColumns().get(" + constantId + ")");
                columnExpr = vm.newCast(columnType, columnExpr);
                method.newReturn().setExpression(columnExpr);

                ForeignKey fKey = column.getForeignKey();
                if(fKey != null)
                {
                    Type fKeyType = fKey instanceof ParentForeignKey
                                    ? vm.newType("ParentForeignKey") : vm.newType("ForeignKey");
                    String fkeyFieldName = textUtils.xmlTextToJavaIdentifier(column.getName(), false) + "ForeignKey";
                    String fkeyMethodName = textUtils.xmlTextToJavaIdentifier(column.getName(), true) + "ForeignKey";

                    method = accessorClass.newMethod(fKeyType, "get" + fkeyMethodName);
                    method.setAccess(Access.PUBLIC);
                    method.isFinal(true);
                    method.newReturn().setExpression(vm.newFree(fkeyFieldName));

                    field = accessorClass.newField(fKeyType, fkeyFieldName);
                    if(fKey instanceof ParentForeignKey)
                        accessorClassConstructorBlock.newStmt(vm.newAssign(vm.newVar(fkeyFieldName), vm.newCast(vm.newType("ParentForeignKey"), vm.newVar("table.getColumns().get(" + constantId + ").getForeignKey()"))));
                    else
                        accessorClassConstructorBlock.newStmt(vm.newAssign(vm.newVar(fkeyFieldName), vm.newVar("table.getColumns().get(" + constantId + ").getForeignKey()")));
                    field.setAccess(Access.PRIVATE);
                }

                ColumnValue valueInstance = column.constructValueInstance();
                String valueInstClassName = valueInstance.getClass().getName();

                Type columnValueType = vm.newType(valueInstClassName.replace('$', '.'));
                if(valueInstClassName.startsWith(BasicColumn.class.getPackage().getName()))
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
                method.newStmt(vm.newFree("get" + methodSuffix + "().copyValueByReference(value)"));

                recordInnerClassValueObjectAccessorMethod.newStmt(vm.newFree("valueObject.set" + methodSuffix + "((" + valueInstance.getValueHolderClass().getName() + ") values.getByColumnIndex(" + constantId + ").getValue())"));
                recordInnerClassValueObjectMutatorMethod.newStmt(vm.newFree("values.getByColumnIndex(" + constantId + ").setValue(valueObject.get" + methodSuffix + "())"));
            }

            recordInnerClassValueObjectAccessorMethod.newReturn().setExpression(vm.newFree("valueObject"));
        }

        /**
         * Gets the class object of a primitive type that is related to the passed in Class (e.g. int is the primitive
         * related to Integere class)
         */
        private Class getRelatedPrimitiveClass(Class mainClass)
        {
            String className = mainClass.getName();
            if(className.equals("java.lang.Integer"))
                return int.class;
            else if(className.equals("java.lang.Float"))
                return float.class;
            else if(className.equals("java.lang.Long"))
                return long.class;
            else
                return null;
        }

        /**
         * Returns the method string for converting the object into its representative primitive value
         */
        private String getPrimitiveToClassMethodName(String primitiveClassName)
        {
            String conversionString = null;
            if(primitiveClassName.equals("int"))
                conversionString = "intValue()";
            else if(primitiveClassName.equals("long"))
                conversionString = "longValue()";
            else if(primitiveClassName.equals("float"))
                conversionString = "floatValue()";

            return conversionString;
        }

        public void generateValueObjects() throws IOException
        {
            TextUtils textUtils = TextUtils.getInstance();

            valueObjectInterface = valueInterfaceUnit.newInterface(valueInterfaceName);
            valueObjectInterface.setAccess(Access.PUBLIC);

            valueObjectClass = valueClassUnit.newClass(valueClassName);
            valueObjectClass.setAccess(Access.PUBLIC);
            valueObjectClass.addImplements(valueInterfaceUnit.getNamespace().getName() + '.' + valueInterfaceName);

            Columns columns = node.getTable().getColumns();

            for(int i = 0; i < columns.size(); i++)
            {
                Column column = columns.get(i);

                ColumnValue valueInstance = column.constructValueInstance();
                Class valueHolderClass = valueInstance.getValueHolderClass();
                String valueInstClassName = valueHolderClass.getName();

                Type valueHolderValueType = vm.newType(valueInstClassName.replace('$', '.'));

                String fieldName = textUtils.xmlTextToJavaIdentifier(column.getName(), false);
                ClassField field = valueObjectClass.newField(valueHolderValueType, fieldName);
                field.setAccess(Access.PRIVATE);

                String methodSuffix = textUtils.xmlTextToJavaIdentifier(column.getName(), true);

                ClassMethod method = valueObjectClass.newMethod(valueHolderValueType, "get" + methodSuffix);
                method.setAccess(Access.PUBLIC);
                method.newReturn().setExpression(vm.newFree(fieldName));

                method = valueObjectClass.newMethod(vm.newType(Type.VOID), "set" + methodSuffix);
                method.addParameter(valueHolderValueType, fieldName);
                method.setAccess(Access.PUBLIC);
                method.newStmt(vm.newFree("this." + fieldName + " = " + fieldName));

                AbstractMethod abstractMethod = valueObjectInterface.newMethod(valueHolderValueType, "get" + methodSuffix);
                abstractMethod.setAccess(Access.PUBLIC);

                abstractMethod = valueObjectInterface.newMethod(vm.newType(Type.VOID), "set" + methodSuffix);
                abstractMethod.addParameter(valueHolderValueType, fieldName);
                abstractMethod.setAccess(Access.PUBLIC);

                // generate setters and getters for primitive types
                Class primitiveClass = getRelatedPrimitiveClass(valueHolderClass);
                if(primitiveClass != null)
                {
                    String primitiveClassName = primitiveClass.getName();
                    Type primitiveValueHolderValueType = vm.newType(primitiveClassName);
                    ClassMethod pMethod = valueObjectClass.newMethod(primitiveValueHolderValueType, "get" + methodSuffix +
                                                                                                    primitiveClassName.substring(0, 1).toUpperCase() + primitiveClassName.substring(1));
                    pMethod.setAccess(Access.PUBLIC);
                    pMethod.newReturn().setExpression(vm.newFree("get" + methodSuffix +
                                                                 primitiveClassName.substring(0, 1).toUpperCase() + primitiveClassName.substring(1) + "(-1)"));

                    // create a getter method with the default value to return if the value object itself is null.
                    pMethod = valueObjectClass.newMethod(primitiveValueHolderValueType, "get" + methodSuffix +
                                                                                        primitiveClassName.substring(0, 1).toUpperCase() + primitiveClassName.substring(1));
                    pMethod.addParameter(primitiveValueHolderValueType, "defaultValue");
                    pMethod.setAccess(Access.PUBLIC);
                    String conversionString = getPrimitiveToClassMethodName(primitiveClassName);
                    pMethod.newReturn().setExpression(vm.newFree(fieldName + " != null ? " + fieldName + "." + conversionString + " : defaultValue"));

                    pMethod = valueObjectClass.newMethod(vm.newType(Type.VOID), "set" + methodSuffix +
                                                                                primitiveClassName.substring(0, 1).toUpperCase() + primitiveClassName.substring(1));
                    pMethod.addParameter(primitiveValueHolderValueType, fieldName);
                    pMethod.setAccess(Access.PUBLIC);
                    pMethod.newStmt(vm.newFree("this." + fieldName + " = new " + valueInstClassName + "(" + fieldName + ")"));

                    // generate the interface methods
                    AbstractMethod primitiveAbstractMethod = valueObjectInterface.newMethod(primitiveValueHolderValueType, "get" + methodSuffix +
                                                                                                                           primitiveClassName.substring(0, 1).toUpperCase() + primitiveClassName.substring(1));
                    primitiveAbstractMethod.setAccess(Access.PUBLIC);

                    primitiveAbstractMethod = valueObjectInterface.newMethod(primitiveValueHolderValueType, "get" + methodSuffix +
                                                                                                            primitiveClassName.substring(0, 1).toUpperCase() + primitiveClassName.substring(1));
                    primitiveAbstractMethod.addParameter(primitiveValueHolderValueType, "defaultValue");
                    primitiveAbstractMethod.setAccess(Access.PUBLIC);

                    primitiveAbstractMethod = valueObjectInterface.newMethod(vm.newType(Type.VOID), "set" + methodSuffix +
                                                                                                    primitiveClassName.substring(0, 1).toUpperCase() + primitiveClassName.substring(1));
                    primitiveAbstractMethod.addParameter(primitiveValueHolderValueType, fieldName);
                    primitiveAbstractMethod.setAccess(Access.PUBLIC);

                }
            }
        }

    }
}

