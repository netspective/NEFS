<#include "*/library.ftl">

<div class="textbox">

    <@reportTable>
    <#assign classSuffix="odd"/>
        <tr>
            <td class="report-column-heading">Handler</td>
            <td class="report-column-heading">Class</td>
            <td class="report-column-heading">Description</td>
        </tr>
    <#assign instancesMap = vc.project.dialogExecuteHandlers.instancesMap/>
    <#list instancesMap.keySet().iterator() as typeName>
        <tr>
            <td class="report-column-${classSuffix}">
                <a href="?detail=${typeName}"><b>${typeName}</b></a>
            </td>
            <td class="report-column-${classSuffix}">
                <@classReference instancesMap.get(typeName).attributes.getValue("class")/>
            </td>
            <td class="report-column-${classSuffix}">
                <@classDescription instancesMap.get(typeName).attributes.getValue("class")/>
            </td>
        </tr>
        <#if classSuffix = 'odd'>
            <#assign classSuffix='even'/>
        <#else>
            <#assign classSuffix='odd'/>
        </#if>
    </#list>
    </@reportTable>


</div>