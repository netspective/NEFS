<#include "*/library.ftl"/>

<#macro showSelfDescendants activePath classSuffix>
    <tr><td class="report-column-${classSuffix}">
        <nobr>
        <#list 0..activePath.level as i>&nbsp;&nbsp;&nbsp;&nbsp;</#list>
        <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-closed.gif')}"/>
        ${vc.getConsoleFileBrowserLinkShowAlt(activePath.file.absolutePath, activePath.file.name)}
        </nobr>
    </td></tr>

    <#list activePath.children as child>
        <#assign include = true/>
        <#if child.file.name = 'CVS'><#assign include=false/></#if>

        <#if include && child.file.isDirectory()>
            <#if classSuffix = 'odd'><#assign nextClassSuffix='even'/><#else><#assign nextClassSuffix='odd'/></#if>
            <@showSelfDescendants activePath=child classSuffix=nextClassSuffix/>
        </#if>
    </#list>
</#macro>

<div class="textbox">

    <#assign projectFilesContext = vc.navigationContext.projectFileSystemContext/>
    <#assign activePath = projectFilesContext.activePath/>

    <#if projectFilesContext.activePath.file.isDirectory()>
        <table cellspacing=5>
            <@showSelfDescendants activePath=projectFilesContext.activePath classSuffix='odd'/>
        </table>
    </#if>

</div>