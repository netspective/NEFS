<#include "*/library.ftl"/>

<div class="textbox">

    <#assign projectFilesContext = vc.navigationContext.projectFileSystemContext/>
    <#assign activePath = projectFilesContext.activePath/>
    <#assign activePathParents = activePath.parents/>
    <#assign activePathParentsCount = activePath.parents.size()/>
    <#assign ancestors = activePathParentsCount-1/>

    <#if activePath.file.isDirectory()>
        <#assign activePathChildren = activePath.children/>
        <#assign level = 0>
        <#assign classSuffix="odd"/>

        <table cellspacing=5>
        <tr valign=top>
        <td>
            <@panel heading="Location">
            <table class="report" border="0" cellspacing="2" cellpadding="0">
                <#list activePathParents as parent>
                    <tr>
                        <td class="report-column-${classSuffix}">
                            <nobr>
                            <#list 0..level as i>&nbsp;&nbsp;</#list>
                            <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-open.gif')}"/>
                            <#if ancestors gt 0>
                            <a href="<#list 1..ancestors as i><#if i lt ancestors>../<#else>..</#if></#list>">
                                ${parent.file.name}
                            </a>
                            <#else>
                                <a href=".">${parent.file.name}</a>
                            </#if>
                            </nobr>
                        </td>
                    </tr>

                    <#assign level = level + 1/>
                    <#assign ancestors = ancestors - 1/>
                    <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>
                </#list>

                <tr><td class="report-column-${classSuffix}">
                    <#list 0..level as i>&nbsp;&nbsp;</#list>
                    <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-active.gif')}"/>
                    <b>${activePath.file.name}</b>
                </td></tr>
                <#assign level = level + 1/>
                <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

                <#list activePathChildren as child>
                    <#assign include = true/>
                    <#if child.file.name = 'CVS'><#assign include=false/></#if>

                    <#if include && child.file.isDirectory()>
                        <tr><td class="report-column-${classSuffix}">
                        <#list 0..level as i>&nbsp;&nbsp;</#list>
                        <img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-closed.gif')}"/>
                        <#if vc.request.requestURI?ends_with('/')>
                            <a href="${vc.request.requestURI}${child.file.name}">${child.entryCaption}</a>
                        <#else>
                            <a href="${vc.request.requestURI}/${child.file.name}">${child.entryCaption}</a>
                        </#if>
                        </td></tr>
                        <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>
                    </#if>
                </#list>
            </table>
            </@panel>
        </td>

        <td>
            <@panel heading="Files">
            <#assign classSuffix="odd"/>
            <table class="report" border="0" cellspacing="2" cellpadding="0">
            <#list activePathChildren as child>
                <#assign include = true/>
                <#if child.file.name = 'CVS'><#assign include=false/></#if>

                <#if include && ! child.file.isDirectory()>
                    <#assign imageUrl = vc.activeTheme.getResourceUrl('/images/files/file-type-${child.entryType}.gif', '-')/>
                    <#if imageUrl = '-'><#assign imageUrl = vc.activeTheme.getResourceUrl('/images/files/file-type-default.gif')/></if>
                    <tr>
                        <td class="report-column-${classSuffix}">
                            <img src="${imageUrl}"/>
                            <#if vc.request.requestURI?ends_with('/')>
                                <a href="${vc.request.requestURI}${child.file.name}">${child.file.name}</a>
                            <#else>
                                <a href="${vc.request.requestURI}/${child.file.name}">${child.file.name}</a>
                            </#if>
                        </td>
                    </tr>
                    <#if classSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>
                </#if>
            </#list>
            </table>
            </@panel>
        </td>
        </table>
    <#else>
        <div class="textbox">
        <table>
            <tr>
                <td><img src="${vc.activeTheme.getResourceUrl('/images/files/file-type-folder-open.gif')}"/></td>
                <td>
                    <#list activePathParents as parent>
                    <#if ancestors gt 0>
                    <a href="<#list 1..ancestors as i><#if i lt ancestors>../<#else>..</#if></#list>">${parent.file.name}</a>
                    <#else>
                        <a href=".">${parent.file.name}</a>
                    </#if>
                    <#assign ancestors = ancestors - 1/>
                    /
                    </#list>
                </td>
            </tr>
            <tr>
                <td>
                    <#assign imageUrl = vc.activeTheme.getResourceUrl('/images/files/file-type-${activePath.entryType}.gif', '-')/>
                    <#if imageUrl = '-'><#assign imageUrl = vc.activeTheme.getResourceUrl('/images/files/file-type-default.gif')/></if>
                    <img src="${imageUrl}"/>
                </td>
                <td>
                ${activePath.file.name}
                </td>
            </tr>
        </table>
        <hr size=1 color=silver>

        <#assign type = activePath.entryType/>
        <#if type = 'gif' || type = 'jpg' || type = 'jpeg' || type = 'png'>
            <img src="${vc.rootUrl}/${activePath.entryURI}"/>
        <#else>
            ${getFileContentsSyntaxHighlighted(activePath.file.absolutePath)}
        </#if>
        </div>
    </#if>

</div>