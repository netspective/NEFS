<@pp.dropOutputFile />

<#assign FKEYTYPE_PARENT = statics["com.netspective.axiom.schema.ForeignKey"].FKEYTYPE_PARENT/>
<#assign FKEYTYPE_LOOKUP = statics["com.netspective.axiom.schema.ForeignKey"].FKEYTYPE_LOOKUP/>
<#assign textUtils = statics["com.netspective.commons.text.TextUtils"].getInstance()/>
<#assign schema = xdm.project.schemas.default/>
<#assign tableTree = schema.structure/>
<#assign mappingFiles = []/>

<#list tableTree.children as tableTreeNode>
<@generateMapping node=tableTreeNode/>
</#list>

<#list schema.tables.list as table>
<#if table.enums?exists>
<@generateEnumeration enumTable=table/>
</#if>
</#list>

<@pp.changeOutputFile name="hibernate.cfg.xml"/>
<?xml version="1.0"?>

<!DOCTYPE hibernate-configuration PUBLIC
	"-//Hibernate/Hibernate Configuration DTD 2.0//EN"
	"http://hibernate.sourceforge.net/hibernate-configuration-2.0.dtd">

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.provider_class">app.util.HibernateAxiomConnectionProvider</property>
        <property name="dialect">net.sf.hibernate.dialect.HSQLDialect</property>

<#list mappingFiles as file>
        <mapping resource="${file}"/>
</#list>
    </session-factory>
</hibernate-configuration>

<#macro generateMapping node>
<#assign table=node.table/>
<#assign className = textUtils.xmlTextToJavaIdentifier(table.name, true)/>
<#assign fileName = className +".hbm.xml"/>
<@pp.changeOutputFile name=fileName />
<#assign mappingFiles = mappingFiles + [fileName]/>
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping SYSTEM "http://hibernate.sourceforge.net/hibernate-mapping-2.0.dtd" >
<hibernate-mapping package="${package}">
    <class name="${className}" table="${table.name}">

    <#if table.primaryKeyColumns.size() gt 1>
        <!-- table ${table.name} has composite primary keys, which this generator doesn't support yet -->
    </#if>

    <#list table.columns.list as column>
    <#if column.foreignKey?exists>
        <#assign parentSrcCol = column.foreignKey.sourceColumns.sole/>
        <#assign parentRefCol = column.foreignKey.referencedColumns.sole/>
        <#assign notNull="false"/>
        <#if column.foreignKey.type = FKEYTYPE_PARENT || column.isRequiredByApp() || column.isPrimaryKey()>
            <#assign notNull="true"/>
        </#if>
        <many-to-one name="${parentSrcCol.getJavaPropertyName(textUtils.xmlTextToJavaIdentifier(parentRefCol.table.name, false))}"
                     column="${parentSrcCol.name}"
                     class="${package}.${textUtils.xmlTextToJavaIdentifier(parentRefCol.table.name, true)}" not-null="${notNull}"/>
    <#else>
        <#assign hibernateTmpls = column.getHibernateMappingTemplateProducer().instances/>
        <#if column.isPrimaryKey()>
            <#assign hibernateTmpls = column.getHibernateIdMappingTemplateProducer().instances/>
        </#if>
        <#if hibernateTmpls?size gt 0>${hibernateTmpls[hibernateTmpls?size-1].asXML(column.hibernateMappingTemplateVars, true)}
        <#else>
            <!-- column ${column.name} has no <hibernate> mappings tag in <data-type> or <column> -->
        </#if>
    </#if>
    </#list>

    <#list node.children as childNode>
    <set name="${textUtils.xmlTextToJavaIdentifier(childNode.table.name, false)}" inverse="true" cascade="all-delete-orphan" lazy="true">
        <key column="${table.primaryKeyColumns.sole.name}"/>
        <one-to-many class="${package}.${textUtils.xmlTextToJavaIdentifier(childNode.table.name, true)}"/>
    </set>
    </#list>

    </class>
</hibernate-mapping>

<#list node.children as childNode>
<@generateMapping node=childNode/>
</#list>
</#macro>

<#macro generateEnumeration enumTable>
<#assign className = textUtils.xmlTextToJavaIdentifier(enumTable.name, true)/>
<#assign fileName = className +".hbm.xml"/>
<@pp.changeOutputFile name=fileName />
<#assign mappingFiles = mappingFiles + [fileName]/>
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping SYSTEM "http://hibernate.sourceforge.net/hibernate-mapping-2.0.dtd" >
<hibernate-mapping package="${package}">
    <class name="${className}" table="${enumTable.name}">
        <id name="id" type="integer" column="id">
            <generator class="assigned"/>
        </id>

        <property name="caption" type="string">
            <column name="caption" length="96" not-null="true" unique="true"/>
        </property>

        <property name="abbrev" type="string">
            <column name="abbrev" length="32" not-null="false" unique="false"/>
        </property>
    </class>
</hibernate-mapping>
</#macro>
