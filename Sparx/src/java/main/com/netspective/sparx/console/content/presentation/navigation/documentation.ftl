<#include "*/library.ftl"/>

<div class="textbox">

    <#assign consolePathFindResults = vc.navigationContext.activePathFindResults/>

    <#if consolePathFindResults.hasUnmatchedPathItems()>
        <#assign activeTreeName = consolePathFindResults.unmatchedPathItems[0]/>
        <#assign activeTree = vc.project.navigationTrees.getNavigationTree(activeTreeName)?default('-')/>

        <#if activeTree != '-'>
            <#assign activePageId = consolePathFindResults.getUnmatchedPath(1)/>
            <#if activePageId = '' || activePageId = '/'>
                <#assign activePage = activeTree.root/>
            <#else>
                <#assign activeTreePageFindResults = activeTree.findPath(activePageId)/>
                <#assign activePage = activeTreePageFindResults.matchedPath/>
            </#if>

            <table cellspacing=5>
            <tr valign=top>
            <td>
                <@panel heading="Location">
                <table class="report" border="0" cellspacing="2" cellpadding="0">
                    <#assign level = 0>
                    <#assign classSuffix="odd"/>
                    <#assign ancestorIndex = activePage.ancestorsList.size()-1/>

                    <#list activePage.ancestorsList.iterator() as parent>
                        <tr>
                            <td class="report-column-${classSuffix}">
                                <nobr>
                                <#list 0..level as i>&nbsp;&nbsp;</#list>
                                <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-open.gif')}"/>
                                <#if ancestorIndex gt 0>
                                <a href="<#list 1..ancestorIndex as i><#if i lt ancestorIndex>../<#else>..</#if></#list>">
                                    <#if parent.name = ''>${activeTreeName}<#else>${parent.name}</#if>
                                </a>
                                <#else>
                                    <a href=".."><#if parent.name = ''>${activeTreeName}<#else>${parent.name}</#if></a>
                                </#if>
                                </nobr>
                            </td>
                        </tr>

                        <#assign level = level + 1/>
                        <#assign ancestorIndex = ancestorIndex - 1/>
                        <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>
                    </#list>

                    <tr><td class="report-column-${classSuffix}">
                        <nobr>
                        <#list 0..level as i>&nbsp;&nbsp;</#list>
                        <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-open.gif')}"/>
                        <b><#if activePage.name = ''>${activeTreeName}<#else>${activePage.name}</#if></b>
                        </nobr>
                    </td></tr>
                    <#assign level = level + 1/>
                    <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

                    <#list activePage.childrenList.iterator() as child>
                        <tr><td class="report-column-${classSuffix}">
                        <nobr>
                        <#list 0..level as i>&nbsp;&nbsp;</#list>
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
                <@inspectObject object=activePage heading="${activePage.qualifiedName} Inspector"/>
            </td>
            </table>
        <#else>
            No active tree found for ${activeTreeName}
        </#if>
    <#else>
        Please choose a navigation tree first.
    </#if>

</div>