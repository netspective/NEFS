<#include "*/library.ftl">

<#assign doingFrameworkDeveploment = vc.runtimeEnvironmentFlags.flagIsSet(statics["com.netspective.commons.RuntimeEnvironmentFlags"].FRAMEWORK_DEVELOPMENT)/>
<#assign trees = vc.project.navigationTrees/>
<#assign catalog = []/>
<#list trees.trees.keySet().iterator() as treeName>
    <#if ! (treeName == 'console' && ! doingFrameworkDeveploment)>
        <#assign tree=trees.getNavigationTree(treeName)/>
        <#if tree.isDefaultTree()>
            <#assign isDefault='Yes'>
        <#else>
            <#assign isDefault='&nbsp;'>
        </#if>
        <#assign catalog = catalog + [[ "<a href='documentation/${tree.name}'>${tree.name}</a>", tree.size(), getClassReference(tree.class.name), isDefault ]]/>
    </#if>
</#list>

<@panel heading="All Available Navigation Trees">
    <@reportTable headings=["Tree", "Pages", "Class", "Default"] data=catalog columnAttrs=["", "align=right", "", ""]/>
</@panel>
