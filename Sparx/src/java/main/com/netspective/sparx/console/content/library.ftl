<#macro childrenSummaries sourcePageType>

<div class="textbox">
    <#if sourcePageType = 'parent'>
        <#assign sourcePage = vc.navigationContext.activePage.parent/>
        <#assign parentName = '.'/>
    <#else>
        <#assign sourcePage = vc.navigationContext.activePage/>
        <#assign parentName = sourcePage.name/>
    </#if>


    <table class="report" width=100% border="0" cellspacing="2" cellpadding="0">
    <#assign classSuffix="odd"/>

    <#list sourcePage.childrenList as child>
        <tr>
            <td class="report-column-${classSuffix}">
                <a href="${parentName}/${child.name}">${child.getHeading(vc)}</a>
            </td>
            <td class="report-column-${classSuffix}">
                <#assign summaryTemplateName = "/content${child.qualifiedName}/summary.ftl"/>
                <#if templateExists(summaryTemplateName)>
                    <#include summaryTemplateName/>
                <#else>
                    ${summaryTemplateName} not available.
                </#if>
            </td>
        </tr>
        <#if classSuffix = 'odd'>
            <#assign classSuffix='even'/>
        <#else>
            <#assign classSuffix='odd'/>
        </#if>
    </#list>
    </table>
</div>

</#macro>