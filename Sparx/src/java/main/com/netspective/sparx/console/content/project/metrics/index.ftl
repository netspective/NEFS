<#include "*/library.ftl"/>

<div class="textbox">

    <#macro showMetric metric rowClassSuffix='even'>
        <#assign metricValue = metric.formattedValue?default('-')/>
        <#if rowClassSuffix = 'odd'><#assign classSuffix='even'/><#else><#assign classSuffix='odd'/></#if>

        <tr>
            <#if metricValue = '-'>
                <td colspan=2 class="report-column-${classSuffix}">
                    <#if metric.level gt 0><#list 1..metric.level as i>&nbsp;&nbsp;&nbsp;</#list></#if>
                    <b>${metric.name}</b>
                </td>
            <#else>
                <td class="report-column-${classSuffix}">
                    <#if metric.level gt 0><#list 1..metric.level as i>&nbsp;&nbsp;&nbsp;</#list></#if>
                    ${metric.name}
                </td>
                <td class="report-column-${classSuffix}">${metric.formattedValue}</td>
            </#if>
        </tr>
        <#if metric.children?default('-') != '-'>
            <#list metric.children as childMetric>
                <@showMetric metric=childMetric rowClassSuffix=classSuffix/>
            </#list>
        </#if>
    </#macro>

    <@panel heading="Project Metrics">
        <table class="report" border="0" cellspacing="2" cellpadding="0">
            <@showMetric metric=vc.projectComponent.metrics/>
        </table>
    </@panel>

</div>