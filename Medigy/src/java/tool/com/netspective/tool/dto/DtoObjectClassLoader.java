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
 */
package com.netspective.tool.dto;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.sql.ResultSet;


/**
 * A classloader that generates a value object bean based on one or more interfaces. Each getter/setter in the interfaces
 * is implemented in the generated bean with private field names using the suffix of the set/get methods. This class
 * allows you to manage an entire bean by simply defining an interface. There is special handling for creating value
 * beans that need to be settable (mutable) by a ResultSet instance.
 * <p/>
 * Assume the following interfaces:
 * <p/>
 * public interface BeanGeneratorTestInterface
 * {
 * public String getString();
 * public int getInt();
 * public short getShort();
 * public byte getByte();
 * public char getChar();
 * public Integer getInteger();
 * public boolean getBoolean();
 * public float getFloat();
 * public Date getDate();
 * public double getDouble();
 * public Object getObject();
 * }
 * <p/>
 * public interface BeanGeneratorTestMutableInterface
 * {
 * public void setString(String value);
 * public void setInt(int value);
 * public void setInteger(Integer value);
 * public void setShort(short value);
 * public void setByte(byte value);
 * public void setChar(char value);
 * public void setBoolean(boolean bool);
 * public void setFloat(float value);
 * public void setDate(Date value);
 * public void setDouble(double value);
 * public void setObject(Object value);
 * <p/>
 * public void setColumnsByName(ResultSet rs, DtoFieldIndexTranslator translator);
 * public void setColumnsByName(ResultSet rs, DtoFieldNameTranslator translator);
 * }
 * <p/>
 * If the programmer defines the above two interfaces (they can also be combined into one if desired), then a call like
 * the following will automatically generate a class on the fly:
 * <p/>
 * final String className = "com.netspective.commons.lang.BeanGeneratorTestImpl";
 * DtoObjectClassLoader bgcl = DtoObjectClassLoader.getInstance(className, new Class[]
 * { BeanGeneratorTestInterface.class, BeanGeneratorTestMutableInterface.class });
 * <p/>
 * The auto-generated class that can manage the values as fields and the special handlers setColumnsByName(ResultSet, ?)
 * will automatically assign values to the fields using the items in the ResultSet using optional name or index
 * translators. The index version is the fastest.
 */
public class DtoObjectClassLoader  extends ClassLoader
{
    private static final Log log = LogFactory.getLog(DtoObjectClassLoader.class);
    private static final Map INSTANCES = new HashMap();

    public static synchronized DtoObjectClassLoader getInstance(final String className, final Class[] interfaces) throws IOException, ClassNotFoundException
    {
        DtoObjectClassLoader bgcl = (DtoObjectClassLoader) INSTANCES.get(className);
        if(bgcl == null)
        {
            bgcl = new DtoObjectClassLoader(interfaces[0].getClassLoader(), className, interfaces);
            INSTANCES.put(className, bgcl);
        }

        return bgcl;
    }

    private final String className;
    private final Class[] interfaces;
    private final Class generatedClass;

    public DtoObjectClassLoader(String className, Class[] interfaces) throws IOException, ClassNotFoundException
    {
        this.className = className;
        this.interfaces = interfaces;
        final byte[] byteCode = generateSimpleBean();
        this.generatedClass = defineClass(className, byteCode, 0, byteCode.length);
    }

    public DtoObjectClassLoader(ClassLoader parent, String className, Class[] interfaces) throws IOException, ClassNotFoundException
    {
        super(parent);
        this.className = className;
        this.interfaces = interfaces;
        final byte[] byteCode = generateSimpleBean();
        this.generatedClass = defineClass(className, byteCode, 0, byteCode.length);
    }

    public Class getGeneratedClass()
    {
        return generatedClass;
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        if(name.equals(className))
            return generatedClass;
        else
            return super.loadClass(name, resolve);
    }

    protected byte[] generateSimpleBean() throws IOException, ClassNotFoundException
    {
        List interfaceNames = new ArrayList();
        for(int i = 0; i < interfaces.length; i++)
            interfaceNames.add(interfaces[i].getName());

        final BeanUtil[] beanUtils = new BeanUtil[interfaces.length];
        for(int i = 0; i < interfaces.length; i++)
        {
            final Class intf = interfaces[i];
            final BeanUtil beanUtil = new BeanUtil(intf);
            beanUtils[i] = new BeanUtil(intf);

            for(Iterator mutatorIter = beanUtil.generators.iterator(); mutatorIter.hasNext();)
            {
                SingleFieldBytecodeGenerator generator = (SingleFieldBytecodeGenerator) mutatorIter.next();
                if(generator instanceof IndexMappedResultSetMutatorBytecodeGenerator)
                    interfaceNames.add(DtoIndexedResultSetAssignable.class.getName());
            }
        }

        final ClassGen classGen = new ClassGen(className, "java.lang.Object", className + ".java", Constants.ACC_PUBLIC | Constants.ACC_SUPER, (String[]) interfaceNames.toArray(new String[interfaceNames.size()]));
        final ConstantPoolGen constantsPool = classGen.getConstantPool();
        final InstructionFactory instructionFactory = new InstructionFactory(classGen, constantsPool);

        // generate the default constructor
        classGen.addEmptyConstructor(Constants.ACC_PUBLIC);

        for(int i = 0; i < interfaces.length; i++)
        {
            final BeanUtil beanUtil = beanUtils[i];

            for(Iterator mutatorIter = beanUtil.generators.iterator(); mutatorIter.hasNext();)
            {
                SingleFieldBytecodeGenerator generator = (SingleFieldBytecodeGenerator) mutatorIter.next();
                generator.generateBytecode(instructionFactory, constantsPool, classGen);
            }
        }

        return classGen.getJavaClass().getBytes();
    }

    /**
     * Convert runtime java.lang.Class to BCEL Type object.
     *
     * @param cl Java class
     *
     * @return corresponding Type object
     */
    public static Type getBCELType(java.lang.Class cl)
    {
        if(cl == null)
        {
            throw new IllegalArgumentException("Class must not be null");
        }

        /* That's an amzingly easy case, because getName() returns
         * the signature. That's what we would have liked anyway.
         */
        if(cl.isArray())
        {
            return Type.getType(cl.getName());
        }
        else if(cl.isPrimitive())
        {
            if(cl == Integer.TYPE)
            {
                return Type.INT;
            }
            else if(cl == Void.TYPE)
            {
                return Type.VOID;
            }
            else if(cl == Double.TYPE)
            {
                return Type.DOUBLE;
            }
            else if(cl == Float.TYPE)
            {
                return Type.FLOAT;
            }
            else if(cl == Boolean.TYPE)
            {
                return Type.BOOLEAN;
            }
            else if(cl == Byte.TYPE)
            {
                return Type.BYTE;
            }
            else if(cl == Short.TYPE)
            {
                return Type.SHORT;
            }
            else if(cl == Long.TYPE)
            {
                return Type.LONG;
            }
            else if(cl == Character.TYPE)
            {
                return Type.CHAR;
            }
            else
            {
                throw new IllegalStateException("Ooops, what primitive type is " + cl);
            }
        }
        else
        { // "Real" class
            return new ObjectType(cl.getName());
        }
    }

    protected interface ClassField
    {
        public String getFieldName();

        public Class getFieldType();

        public InvokeInstruction getResultSetAccessorByColNameInvokeInstruction(InstructionFactory instructionFactory);

        public InvokeInstruction getResultSetAccessorByColIndexInvokeInstruction(InstructionFactory instructionFactory);
    }

    protected interface SingleFieldBytecodeGenerator
    {
        public void generateBytecode(InstructionFactory instructionFactory, ConstantPoolGen constantsPool, ClassGen classGen);
    }

    protected interface MultiFieldBytecodeGenerator extends SingleFieldBytecodeGenerator
    {
    }

    protected class SimpleFieldAccessorSingleFieldBytecodeGenerator implements SingleFieldBytecodeGenerator
    {
        private final BeanMethod beanMethod;

        public SimpleFieldAccessorSingleFieldBytecodeGenerator(BeanMethod beanMethod)
        {
            this.beanMethod = beanMethod;
        }

        public void generateBytecode(InstructionFactory instructionFactory, ConstantPoolGen constantsPool, ClassGen classGen)
        {
            final InstructionList il = new InstructionList();
            final Type returnType = getBCELType(beanMethod.method.getReturnType());
            final MethodGen accessor = new MethodGen(Constants.ACC_PUBLIC, returnType, Type.NO_ARGS, new String[]{}, beanMethod.method.getName(), className, il, constantsPool);

            il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
            il.append(instructionFactory.createFieldAccess(className, beanMethod.fieldName, returnType, Constants.GETFIELD));
            il.append(InstructionFactory.createReturn(returnType));
            accessor.setMaxStack();
            accessor.setMaxLocals();
            classGen.addMethod(accessor.getMethod());
            il.dispose();
        }
    }

    protected class SimpleFieldMutatorSingleFieldBytecodeGenerator implements SingleFieldBytecodeGenerator
    {
        private final BeanMethod beanMethod;

        public SimpleFieldMutatorSingleFieldBytecodeGenerator(BeanMethod beanMethod)
        {
            this.beanMethod = beanMethod;
        }

        public void generateBytecode(InstructionFactory instructionFactory, ConstantPoolGen constantsPool, ClassGen classGen)
        {
            final Type paramType = getBCELType(beanMethod.method.getParameterTypes()[0]);
            classGen.addField(new FieldGen(Constants.ACC_PRIVATE, paramType, beanMethod.fieldName, constantsPool).getField());

            final InstructionList il = new InstructionList();
            final MethodGen mutator = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, new Type[]{paramType}, new String[]{
                beanMethod.fieldName
            }, beanMethod.method.getName(), className, il, constantsPool);

            il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
            il.append(InstructionFactory.createLoad(paramType, 1));
            il.append(instructionFactory.createFieldAccess(className, beanMethod.fieldName, paramType, Constants.PUTFIELD));
            il.append(InstructionFactory.createReturn(Type.VOID));
            mutator.setMaxStack();
            mutator.setMaxLocals();
            classGen.addMethod(mutator.getMethod());
            il.dispose();
        }
    }

    protected class IndexMappedResultSetMutatorBytecodeGenerator implements MultiFieldBytecodeGenerator
    {
        private final BeanMethod beanMethod;
        private final Set classFields;

        public IndexMappedResultSetMutatorBytecodeGenerator(BeanMethod beanMethod, Set classFields)
        {
            this.beanMethod = beanMethod;
            this.classFields = classFields;
        }

        public void generateBytecode(InstructionFactory instructionFactory, ConstantPoolGen constantsPool, ClassGen classGen)
        {
            final InstructionList il = new InstructionList();
            final MethodGen method = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, new Type[]{
                new ObjectType("java.sql.ResultSet"), new ObjectType(DtoFieldIndexTranslator.class.getName())
            }, new String[]{"rs", "mapper"}, beanMethod.method.getName(), className, il, constantsPool);

            BranchInstruction ifle = null;

            for(Iterator i = classFields.iterator(); i.hasNext();)
            {
                final ClassField field = (ClassField) i.next();
                final InvokeInstruction resultSetAccessorByColIndexInvokeInstruction = field.getResultSetAccessorByColIndexInvokeInstruction(instructionFactory);

                if(resultSetAccessorByColIndexInvokeInstruction == null)
                    continue;

                /* PSEUDO CODE:
                 *   int columnIndex = mapper.getTranslatedIndex("fieldName");
                 *   if(columnIndex > 0)
                 *      this.fieldName = rs.getXXX(columnIndex);
                 */

                InstructionHandle start = il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
                if(ifle != null)
                    ifle.setTarget(start);

                il.append(new PUSH(constantsPool, field.getFieldName()));
                il.append(instructionFactory.createInvoke(DtoFieldIndexTranslator.class.getName(), "getTranslatedIndex", Type.INT, new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE));
                il.append(InstructionFactory.createStore(Type.INT, 3));
                il.append(InstructionFactory.createLoad(Type.INT, 3));
                ifle = InstructionFactory.createBranchInstruction(Constants.IFLE, null);
                il.append(ifle);
                il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
                il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
                il.append(InstructionFactory.createLoad(Type.INT, 3));
                il.append(resultSetAccessorByColIndexInvokeInstruction);
                il.append(instructionFactory.createFieldAccess(className, field.getFieldName(), getBCELType(field.getFieldType()), Constants.PUTFIELD));
            }

            InstructionHandle exit = il.append(InstructionFactory.createReturn(Type.VOID));
            if(ifle != null)
                ifle.setTarget(exit);

            method.setMaxStack();
            method.setMaxLocals();

            classGen.addMethod(method.getMethod());
            il.dispose();
        }
    }

    protected class NameMappedResultSetMutatorBytecodeGenerator implements MultiFieldBytecodeGenerator
    {
        private final BeanMethod beanMethod;
        private final Set classFields;

        public NameMappedResultSetMutatorBytecodeGenerator(BeanMethod beanMethod, Set classFields)
        {
            this.beanMethod = beanMethod;
            this.classFields = classFields;
        }

        public void generateBytecode(InstructionFactory instructionFactory, ConstantPoolGen constantsPool, ClassGen classGen)
        {
            final InstructionList il = new InstructionList();
            final MethodGen method = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, new Type[]{
                new ObjectType("java.sql.ResultSet"), new ObjectType(DtoFieldNameTranslator.class.getName())
            }, new String[]{"rs", "mapper"}, beanMethod.method.getName(), className, il, constantsPool);

            BranchInstruction ifNullBranch = null;

            for(Iterator i = classFields.iterator(); i.hasNext();)
            {
                final ClassField field = (ClassField) i.next();
                final InvokeInstruction resultSetAccessorByColNameInvokeInstruction = field.getResultSetAccessorByColNameInvokeInstruction(instructionFactory);

                if(resultSetAccessorByColNameInvokeInstruction == null)
                    continue;

                /* PSEUDO CODE:
                 *   String columnName = mapper.getTranslatedName("fieldName");
                 *   if(columnName != null)
                 *       this.fieldName = rs.getString(columnName);
                 */

                InstructionHandle start = il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
                if(ifNullBranch != null)
                    ifNullBranch.setTarget(start);

                il.append(new PUSH(constantsPool, field.getFieldName()));
                il.append(instructionFactory.createInvoke(DtoFieldIndexTranslator.class.getName(), "getTranslatedName", Type.STRING, new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE));
                il.append(InstructionFactory.createStore(Type.OBJECT, 3));
                il.append(InstructionFactory.createLoad(Type.OBJECT, 3));
                ifNullBranch = InstructionFactory.createBranchInstruction(Constants.IFNULL, null);
                il.append(ifNullBranch);
                il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
                il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
                il.append(InstructionFactory.createLoad(Type.OBJECT, 3));
                il.append(resultSetAccessorByColNameInvokeInstruction);
                il.append(instructionFactory.createFieldAccess(className, field.getFieldName(), getBCELType(field.getFieldType()), Constants.PUTFIELD));
            }

            InstructionHandle exit = il.append(InstructionFactory.createReturn(Type.VOID));
            if(ifNullBranch != null)
                ifNullBranch.setTarget(exit);

            method.setMaxStack();
            method.setMaxLocals();

            classGen.addMethod(method.getMethod());
            il.dispose();
        }
    }

    protected class BeanMethod implements ClassField
    {
        private final String fieldName;
        private final Method method;
        private final SingleFieldBytecodeGenerator generator;

        public BeanMethod(Method method, Set classFields)
        {
            this.method = method;
            final String methodName = method.getName();
            final Class[] parameterTypes = method.getParameterTypes();
            if(methodName.startsWith("get") && parameterTypes.length == 0)
            {
                this.fieldName = methodName.substring(3);
                this.generator = new SimpleFieldAccessorSingleFieldBytecodeGenerator(this);
            }
            else if(methodName.startsWith("set"))
            {
                if(parameterTypes.length == 1)
                {
                    this.fieldName = methodName.substring(3);
                    this.generator = new SimpleFieldMutatorSingleFieldBytecodeGenerator(this);
                }
                else if(parameterTypes.length == 2 && parameterTypes[0].equals(ResultSet.class) && parameterTypes[1].equals(DtoFieldIndexTranslator.class))
                {
                    this.fieldName = null;
                    this.generator = new IndexMappedResultSetMutatorBytecodeGenerator(this, classFields);
                }
                else if(parameterTypes.length == 2 && parameterTypes[0].equals(ResultSet.class) && parameterTypes[1].equals(DtoFieldNameTranslator.class))
                {
                    this.fieldName = null;
                    this.generator = new NameMappedResultSetMutatorBytecodeGenerator(this, classFields);
                }
                else
                {
                    fieldName = null;
                    generator = null;
                }
            }
            else
            {
                fieldName = null;
                generator = null;
            }
        }

        public String getFieldName()
        {
            return fieldName;
        }

        public Class getFieldType()
        {
            return method.getParameterTypes()[0];
        }

        public boolean equals(Object obj)
        {
            if(super.equals(obj))
                return fieldName.equals(((BeanMethod) obj).fieldName);

            return false;
        }

        public SingleFieldBytecodeGenerator getGenerator()
        {
            return generator;
        }

        public InvokeInstruction getResultSetAccessorByColNameInvokeInstruction(InstructionFactory instructionFactory)
        {
            Class cl = getFieldType();
            if(cl == null)
            {
                throw new IllegalArgumentException("Class must not be null");
            }

            /* That's an amzingly easy case, because getName() returns
             * the signature. That's what we would have liked anyway.
             */
            if(cl.isArray())
            {
                throw new IllegalArgumentException("Arrays not allowed yet");
            }
            else if(cl.isAssignableFrom(String.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getString", Type.STRING, new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.sql.Timestamp.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getTimestamp", new ObjectType("java.sql.Timestamp"), new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.sql.Date.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getDate", new ObjectType("java.sql.Date"), new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.sql.Time.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getTime", new ObjectType("java.sql.Time"), new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.util.Date.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getDate", new ObjectType("java.util.Date"), new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isPrimitive())
            {
                if(cl == Integer.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getInt", Type.INT, new Type[]{
                        Type.STRING
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Double.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getDouble", Type.DOUBLE, new Type[]{
                        Type.STRING
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Float.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getFloat", Type.FLOAT, new Type[]{
                        Type.STRING
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Boolean.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getBoolean", Type.BOOLEAN, new Type[]{
                        Type.STRING
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Byte.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getByte", Type.BYTE, new Type[]{
                        Type.STRING
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Short.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getShort", Type.SHORT, new Type[]{
                        Type.STRING
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Long.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getLong", Type.LONG, new Type[]{
                        Type.STRING
                    }, Constants.INVOKEINTERFACE);
                }
                else
                {
                    log.error("Primitive does not have ResultSet mapping: " + cl);
                    return null;
                }
            }
            else
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getObject", new ObjectType(cl.getName()), new Type[]{
                    Type.STRING
                }, Constants.INVOKEINTERFACE);
            }
        }

        public InvokeInstruction getResultSetAccessorByColIndexInvokeInstruction(InstructionFactory instructionFactory)
        {
            Class cl = getFieldType();
            if(cl == null)
            {
                throw new IllegalArgumentException("Class must not be null");
            }

            /* That's an amzingly easy case, because getName() returns
             * the signature. That's what we would have liked anyway.
             */
            if(cl.isArray())
            {
                throw new IllegalArgumentException("Arrays not allowed yet");
            }
            else if(cl.isAssignableFrom(String.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getString", Type.STRING, new Type[]{
                    Type.INT
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.sql.Timestamp.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getTimestamp", new ObjectType("java.sql.Timestamp"), new Type[]{
                    Type.INT
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.sql.Date.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getDate", new ObjectType("java.sql.Date"), new Type[]{
                    Type.INT
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.sql.Time.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getTime", new ObjectType("java.sql.Time"), new Type[]{
                    Type.INT
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isAssignableFrom(java.util.Date.class))
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getDate", new ObjectType("java.util.Date"), new Type[]{
                    Type.INT
                }, Constants.INVOKEINTERFACE);
            }
            else if(cl.isPrimitive())
            {
                if(cl == Integer.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getInt", Type.INT, new Type[]{
                        Type.INT
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Double.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getDouble", Type.DOUBLE, new Type[]{
                        Type.INT
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Float.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getFloat", Type.FLOAT, new Type[]{
                        Type.INT
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Boolean.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getBoolean", Type.BOOLEAN, new Type[]{
                        Type.INT
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Byte.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getByte", Type.BYTE, new Type[]{
                        Type.INT
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Short.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getShort", Type.SHORT, new Type[]{
                        Type.INT
                    }, Constants.INVOKEINTERFACE);
                }
                else if(cl == Long.TYPE)
                {
                    return instructionFactory.createInvoke("java.sql.ResultSet", "getLong", Type.LONG, new Type[]{
                        Type.INT
                    }, Constants.INVOKEINTERFACE);
                }
                else
                {
                    log.error("Primitive does not have ResultSet mapping: " + cl);
                    return null;
                }
            }
            else
            {
                return instructionFactory.createInvoke("java.sql.ResultSet", "getObject", new ObjectType(cl.getName()), new Type[]{
                    Type.INT
                }, Constants.INVOKEINTERFACE);
            }
        }
    }

    protected class BeanUtil
    {
        private Class clazz;
        private Set classFields = new HashSet();
        private Set generators = new HashSet();

        public BeanUtil(Class clazz)
        {
            this.clazz = clazz;
            final Method[] methods = clazz.getMethods();
            for(int m = 0; m < methods.length; m++)
            {
                final Method method = methods[m];
                final BeanMethod beanMethod = new BeanMethod(method, classFields);
                if(beanMethod.generator != null)
                {
                    generators.add(beanMethod.generator);
                    if(beanMethod.generator instanceof SimpleFieldMutatorSingleFieldBytecodeGenerator)
                        classFields.add(beanMethod);
                }
            }
        }
    }
}
