<#include "*/library.ftl"/>

<div class="textbox">

    <#assign consolePathFindResults = vc.navigationContext.activePathFindResults/>

    <#if consolePathFindResults.hasUnmatchedPathItems()>
        <#assign activeItemId = consolePathFindResults.getUnmatchedPath(0)/>
        <#assign activeItem = vc.project.accessControlLists.getPermission(activeItemId)?default('-')/>

        <table cellspacing=5>
        <tr valign=top>
        <td>
            <@panel heading="Permissions">
            <table class="report" border="0" cellspacing="2" cellpadding="0" width="100%">
                <#assign classSuffix="odd"/>

                <tr>
                    <td class="report-column-${classSuffix}">
                        <nobr>
                        <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-open.gif')}"/>
                        <a href="${vc.consoleUrl}/project/access-control/tree/${activeItem.owner.name}">${activeItem.owner.name}</a>
                        </nobr>
                    </td>
                </tr>
                <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

                <#list activeItem.ancestorsList.iterator() as parent>
                    <tr>
                        <td class="report-column-${classSuffix}">
                            <nobr>
                            <#list 0..parent.level as i>&nbsp;&nbsp;</#list>
                            <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-open.gif')}"/>
                            <a href="${vc.consoleUrl}/project/access-control/perm-inspector${parent.qualifiedName}">${parent.name}</a>
                            </nobr>
                        </td>
                    </tr>

                    <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>
                </#list>

                <tr><td class="report-column-${classSuffix}">
                    <nobr>
                    <#list 0..activeItem.level as i>&nbsp;&nbsp;</#list>
                    <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-open.gif')}"/>
                    <b><#if activeItem.name = ''>${activeACLName}<#else>${activeItem.name}</#if></b>
                    </nobr>
                </td></tr>
                <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

                <#list activeItem.children.iterator() as child>
                    <tr><td class="report-column-${classSuffix}">
                    <nobr>
                    <#list 0..child.level as i>&nbsp;&nbsp;</#list>
                    <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-default.gif')}"/>
                    <#if vc.request.requestURI?ends_with('/')>
                        <a href="${vc.request.requestURI}${child.name}">${child.name}</a>
                    <#else>
                        <a href="${vc.request.requestURI}/${child.name}">${child.name}</a>
                    </#if>
                    </nobr>
                    </td></tr>
                    <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>
                </#list>
            </table>
            </@panel>
        </td>

        <td>
            <@inspectObject object=activeItem heading="Permission '${activeItem.qualifiedName}' Inspector"/>
        </td>
        </table>
    <#else>
        Please <a href="catalog">choose an access control list</a> first, then click on a permission to inspect it.
    </#if>

</div>