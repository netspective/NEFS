<#include "*/library.ftl"/>

<#macro showPermAndDescendants activeItem rowClassSuffix='even'>
    <#assign children = activeItem.children/>
    <#if rowClassSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

    <tr>
    <td class="report-column-${classSuffix}">
        <nobr>
        <#if activeItem.level gt 0>
        <#list 1..activeItem.level as i>&nbsp;&nbsp;&nbsp;&nbsp;</#list>
        </#if>
        <#if activeItem.children?size gt 0>
            <img src="${vc.activeTheme.getResourceUrl('/images/access-control/permissions.gif')}"/>
        <#else>
            <img src="${vc.activeTheme.getResourceUrl('/images/access-control/permission.gif')}"/>
        </#if>
        <a href="${vc.consoleUrl}/project/access-control/perm-inspector${activeItem.qualifiedName}">${activeItem.name}</a>
        </nobr>
    </td>
    <td class="report-column-${classSuffix}" align=right>${activeItem.id}</td>
    <td class="report-column-${classSuffix}" align=right>${children?size}</td>
    <td class="report-column-${classSuffix}">
        ${activeItem.childPermissions}
    </td>

    <#list children as child>
        <@showPermAndDescendants activeItem=child rowClassSuffix=classSuffix/>
    </#list>
</#macro>

<#macro showRoleAndDescendants activeItem rowClassSuffix='even'>
    <#assign children = activeItem.children/>
    <#if rowClassSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

    <tr>
    <td class="report-column-${classSuffix}">
        <nobr>
        <#if activeItem.level gt 0>
        <#list 1..activeItem.level as i>&nbsp;&nbsp;&nbsp;&nbsp;</#list>
        </#if>
        <#if activeItem.children?size gt 0>
            <img src="${vc.activeTheme.getResourceUrl('/images/access-control/roles.gif')}"/>
        <#else>
            <img src="${vc.activeTheme.getResourceUrl('/images/access-control/role.gif')}"/>
        </#if>
        <a href="${vc.consoleUrl}/project/access-control/role-inspector${activeItem.qualifiedName}">${activeItem.name}</a>
        </nobr>
    </td>
    <td class="report-column-${classSuffix}" align=right>${activeItem.id}</td>
    <td class="report-column-${classSuffix}" align=right>${children?size}</td>
    <td class="report-column-${classSuffix}">
        ${activeItem.permissions}
    </td>

    <#list children as child>
        <@showRoleAndDescendants activeItem=child rowClassSuffix=classSuffix/>
    </#list>
</#macro>

<div class="textbox">

    <#assign consolePathFindResults = vc.navigationContext.activePathFindResults/>

    <#if consolePathFindResults.hasUnmatchedPathItems()>
        <#assign activeACLName = consolePathFindResults.unmatchedPathItems[0]/>
        <#assign activeACL = vc.project.accessControlLists.getAccessControlList(activeACLName)?default('-')/>

        <#if activeACL != '-'>
            <@inspectObject object=activeACL heading="ACL '${activeACL.name}' Inspector"/>
            <p>

            <@panel heading="ACL '${activeACL.name}' Permissions">
            <table cellspacing=5>
                <tr>
                    <td class="report-column-heading">Permission</td>
                    <td class="report-column-heading">Id</td>
                    <td class="report-column-heading">Children</td>
                    <td class="report-column-heading">Child Perms</td>
                </tr>
                <#list activeACL.permissions as permission>
                    <@showPermAndDescendants activeItem=permission/>
                </#list>
            </table>
            </@panel>
            <p>

            <@panel heading="ACL '${activeACL.name}' Roles">
            <table cellspacing=5>
                <tr>
                    <td class="report-column-heading">Role</td>
                    <td class="report-column-heading">Id</td>
                    <td class="report-column-heading">Children</td>
                    <td class="report-column-heading">Permissions</td>
                </tr>
                <#list activeACL.roles as role>
                    <@showRoleAndDescendants activeItem=role/>
                </#list>
            </table>
            </@panel>
            <p>
        <#else>
            No active ACL found for '${activeACLName}'.
        </#if>
    <#else>
        Please <a href="catalog">choose</a> an ACL first.
    </#if>

</div>