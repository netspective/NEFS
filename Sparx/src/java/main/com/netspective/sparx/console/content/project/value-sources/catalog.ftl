<#include "*/library.ftl">

<div class="textbox">

    <#assign factory = statics["com.netspective.commons.value.ValueSources"].getInstance()/>
    <#assign valueSourceClassesMap = factory.getValueSourceClassesMap()/>

    <@panel heading="All Available Value Sources">
        <@reportTable>
        <#assign classSuffix="odd"/>
            <tr>
                <td class="report-column-heading">Command</td>
                <td class="report-column-heading">Description</td>
                <td class="report-column-heading">Used</td>
            </tr>
        <#assign noDocs = { "description" : "No description available." }/>
        <#list valueSourceClassesMap.keySet().iterator() as valueSource>
            <#assign valueSourceClass = valueSourceClassesMap.get(valueSource)/>
            <#assign docs = factory.getValueSourceDocumentation(valueSourceClass)?default(noDocs)/>
            <tr>
                <td class="report-column-${classSuffix}">
                    <a href="unit-test?selected-value-source=${valueSource}"><b>${valueSource}</b></a>
                </td>
                <td class="report-column-${classSuffix}">
                    ${docs.description}
                </td>
                <td class="report-column-${classSuffix}" align=right>
                    ${factory.getUsageCount(valueSourceClass)}
                </td>
            </tr>
            <#if classSuffix = 'odd'>
                <#assign classSuffix='even'/>
            <#else>
                <#assign classSuffix='odd'/>
            </#if>
        </#list>
        </@reportTable>
    </@panel>

</div>