<div class="textbox">
    <#assign parentName = vc.navigationContext.activePage.name/>
    <table class="report" width=100% border="0" cellspacing="2" cellpadding="0">
    <#assign classSuffix="odd"/>
    <#list vc.navigationContext.activePage.childrenList as child>
        <tr>
            <td class="report-column-${classSuffix}">
                <a href="${parentName}/${child.name}">${child.getHeading(vc)}</a>
            </td>
            <td class="report-column-${classSuffix}">
                <#include "/content/${child.qualifiedName}/summary.ftl"/>
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