<#include "*/library.ftl"/>

<#macro showSelfDescendants activePage rowClassSuffix='even'>
    <#assign children = activePage.childrenList/>
    <#if rowClassSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

    <tr>
    <td class="report-column-${classSuffix}">
        <nobr>
        <#if activePage.level gt 0>
        <#list 1..activePage.level as i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</#list>
        </#if>
        <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-default.gif')}"/>
        <#if activePage.name = ''>
            <a href="${vc.consoleUrl}/presentation/navigation/inspector/${activePage.owner.name}">${activePage.owner.name}</a>
        <#else>
            <a href="${vc.consoleUrl}/presentation/navigation/inspector${activePage.qualifiedNameIncludingTreeId}">${activePage.name}</a>
        </#if>
        </nobr>
    </td>
    <td class="report-column-${classSuffix}" align=right>${children?size}</td>
    <td class="report-column-${classSuffix}"><#if activePage.default>Yes<#else>&nbsp;</#if></td>
    <td class="report-column-${classSuffix}">${activePage.getCaption()?default('NULL')}</td>

    <#list children as child>
        <@showSelfDescendants activePage=child rowClassSuffix=classSuffix/>
    </#list>
</#macro>

<div class="textbox">

<#include "*/library.ftl"/>

<div class="textbox">

    <#assign consolePathFindResults = vc.navigationContext.activePathFindResults/>

    <#if consolePathFindResults.hasUnmatchedPathItems()>
        <#assign activeTreeName = consolePathFindResults.unmatchedPathItems[0]/>
        <#assign activeTree = vc.project.navigationTrees.getNavigationTree(activeTreeName)?default('-')/>

        <#if activeTree != '-'>
            <#assign activePageId = consolePathFindResults.getUnmatchedPath(1)/>
            <#if activePageId = '' || activePageId = '/'>
                <#assign currentPage = activeTree.root/>
            <#else>
                <#assign activeTreePageFindResults = activeTree.findPath(activePageId)/>
                <#assign currentPage = activeTreePageFindResults.matchedPath/>
            </#if>

            <table cellspacing=5>
                <tr>
                    <td class="report-column-heading">Page</td>
                    <td class="report-column-heading">Children</td>
                    <td class="report-column-heading">Default</td>
                    <td class="report-column-heading">Caption</td>
                </tr>
                <@showSelfDescendants activePage=currentPage/>
            </table>

        <#else>
            No active tree found for '${activeTreeName}'.
        </#if>
    <#else>
        Please choose a navigation tree first.
    </#if>

</div>