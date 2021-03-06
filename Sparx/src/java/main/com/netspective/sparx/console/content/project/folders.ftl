<#include "/content/library.ftl"/>

<#macro showSelfDescendants activePath fileTypesToCount rowClassSuffix='even'>
    <#assign children = activePath.children/>
    <#if rowClassSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

    <tr>
    <td class="report-column-${classSuffix}">
        <nobr>
        <#if activePath.level gt 0>
        <#list 1..activePath.level as i>&nbsp;&nbsp;&nbsp;&nbsp;</#list>
        </#if>
        <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-closed.gif')}"/>
        ${vc.getConsoleFileBrowserLinkShowAlt(activePath.file.absolutePath, activePath.file.name)}
        </nobr>
    </td>
    <#assign fileCounts = {}/>
    <#assign dirList = activePath.file.list()/>
    <#list children as child>
        <#assign fileType = child.entryType/>
        <#assign fileCounts = fileCounts + { fileType : fileCounts[fileType]?default(0) + 1 } />
    </#list>

    <td class="report-column-${classSuffix}" align=right>${children?size}</td>

    <#list fileTypesToCount as fileType>
        <#if fileCounts[fileType]?exists>
            <td class="report-column-${classSuffix}" align=right>${fileCounts[fileType]}</td>
        <#else>
            <td class="report-column-${classSuffix}" align=right>&nbsp;</td>
        </#if>
    </#list>
    </tr>

    <#list children as child>
        <#assign include = true/>
        <#if child.file.name = 'CVS'><#assign include=false/></#if>

        <#if include && child.file.isDirectory()>
            <@showSelfDescendants activePath=child rowClassSuffix=classSuffix fileTypesToCount=fileTypesToCount/>
        </#if>
    </#list>
</#macro>

<div class="textbox">

    <#assign projectFilesContext = vc.navigationContext.projectFileSystemContext/>
    <#assign activePath = projectFilesContext.activePath/>

    <#assign allFileTypes = activePath.getFileTypes(true)/>

    <#if projectFilesContext.activePath.file.isDirectory()>
        <table cellspacing=5>
            <tr>
                <td class="report-column-heading">Folder</td>
                <td class="report-column-heading">Entries</td>
                <#list allFileTypes as fileType>
                    <td class="report-column-heading">*.${fileType}</td>
                </#list>
            </tr>
            <@showSelfDescendants activePath=projectFilesContext.activePath fileTypesToCount=allFileTypes/>
        </table>
    </#if>

</div>