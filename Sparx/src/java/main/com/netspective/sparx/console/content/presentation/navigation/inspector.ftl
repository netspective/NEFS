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
                <@panel heading="Page Locator">
                <table class="report" border="0" cellspacing="2" cellpadding="0" width="100%">
                    <#assign classSuffix="odd"/>

                    <tr>
                        <td class="report-column-${classSuffix}">
                            <nobr>
                            <img src="${vc.activeTheme.getResourceUrl('/images/navigation/tree.gif')}"/>
                            <a href="${vc.consoleUrl}/presentation/navigation/tree/${activePage.owner.name}">${activePage.owner.name}</a>
                            </nobr>
                        </td>
                    </tr>
                    <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

                    <#list activePage.ancestorsList.iterator() as parent>
                        <#if parent.name != ''>
                        <tr>
                            <td class="report-column-${classSuffix}">
                                <nobr>
                                <#list 0..parent.level as i>&nbsp;&nbsp;</#list>
                                <img src="${vc.activeTheme.getResourceUrl('/images/navigation/pages.gif')}"/>
                                <a href="${vc.consoleUrl}/presentation/navigation/inspector${parent.qualifiedNameIncludingTreeId}">${parent.name}</a>
                                </nobr>
                            </td>
                        </tr>

                        <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>
                        </#if>
                    </#list>

                    <tr><td class="report-column-${classSuffix}">
                        <nobr>
                        <#list 0..activePage.level as i>&nbsp;&nbsp;</#list>
                        <#if activePage.childrenList?size gt 0>
                            <img src="${vc.activeTheme.getResourceUrl('/images/navigation/pages.gif')}"/>
                        <#else>
                            <img src="${vc.activeTheme.getResourceUrl('/images/navigation/page.gif')}"/>
                        </#if>
                        <b><#if activePage.name = ''>${activeTreeName}<#else>${activePage.name}</#if></b>
                        </nobr>
                    </td></tr>
                    <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

                    <#list activePage.childrenList.iterator() as child>
                        <tr><td class="report-column-${classSuffix}">
                        <nobr>
                        <#list 0..child.level as i>&nbsp;&nbsp;</#list>
                        <#if child.childrenList?size gt 0>
                            <img src="${vc.activeTheme.getResourceUrl('/images/navigation/pages.gif')}"/>
                        <#else>
                            <img src="${vc.activeTheme.getResourceUrl('/images/navigation/page.gif')}"/>
                        </#if>
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
            No active tree found for '${activeTreeName}'.
        </#if>
    <#else>
        Please choose a navigation tree first.
    </#if>

</div>