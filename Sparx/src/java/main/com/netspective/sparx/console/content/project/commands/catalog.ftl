<#include "*/library.ftl">

<div class="textbox">

    <#assign factory = statics["com.netspective.commons.command.Commands"].getInstance()/>
    <#assign commandClassesMap = factory.getClassesMap()/>

    <@panel heading="All Available Commands">
        <@reportTable>
        <#assign classSuffix="odd"/>
            <tr>
                <td class="report-column-heading">Command</td>
                <td class="report-column-heading">Description</td>
            </tr>
        <#assign noDocs = { "description" : "No description available." }/>
        <#list commandClassesMap.keySet().iterator() as command>
            <#assign commandClass = commandClassesMap.get(command)/>
            <#assign docs = factory.getCommandDocumentation(commandClass)?default(noDocs)/>
            <tr>
                <td class="report-column-${classSuffix}">
                    <a href="unit-test?selected-command=${command}"><b>${command}</b></a>
                </td>
                <td class="report-column-${classSuffix}">
                    ${docs.description}
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