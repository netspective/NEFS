<#include "/content/library.ftl"/>

<#assign doingFrameworkDeveploment = vc.runtimeEnvironmentFlags.flagIsSet(statics["com.netspective.commons.RuntimeEnvironmentFlags"].FRAMEWORK_DEVELOPMENT)/>
<#assign accessControlLists = vc.project.accessControlLists/>
<#assign catalog = []/>
<#list accessControlLists.accessControlLists.keySet().iterator() as aclName>
    <#if ! (aclName == 'console' && ! doingFrameworkDeveploment)>
        <#assign acl=accessControlLists.getAccessControlList(aclName)/>
        <#assign catalog = catalog + [[ "<img src='${vc.activeTheme.getResourceUrl('/images/access-control/acl.gif')}'/>", "<a href='tree/${acl.name}'>${acl.name}</a>", acl.permissions?size, acl.roles?size, getClassReference(acl.class.name) ]]/>
    </#if>
</#list>

<#if catalog?size = 0>
    <div class="textbox">No access control lists defined.</div>
<#else>
    <@panel heading="All Available Access Control Lists">
        <@reportTable headings=["&nbsp;", "ACL", "Permissions", "Roles", "Class"] data=catalog columnAttrs=["", "", "align=right", "align=right", ""]/>
    </@panel>
</#if>
