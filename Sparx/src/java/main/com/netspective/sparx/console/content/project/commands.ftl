<#include "*/library.ftl">

<div class="textbox">

    <#assign factory = statics["com.netspective.commons.command.Commands"].getInstance()/>
    <#assign commandClassesMap = factory.getClassesMap()/>
    <#assign describeCommand = vc.getRequest().getParameter('selected-command')?default('-')/>

    <#if describeCommand != '-'>
        <#assign commandClass = commandClassesMap.get(describeCommand)/>
        <#assign docs = factory.getCommandDocumentation(commandClass)/>
        <@panel heading="${describeCommand} Command">
            <div class=textbox>
            ${docs.description}
            </div>
            <@reportTable>
                <#assign classSuffix="odd"/>
                <tr>
                    <td class="report-column-heading" colspan=2>Param</td>
                    <td class="report-column-heading">Required</td>
                    <td class="report-column-heading">Default</td>
                    <td class="report-column-heading">Choices</td>
                </tr>
           <#assign num = 0 />
            <#list docs.parameters as parameter>
                <#assign num = num + 1 />
                <tr>
                    <td class="report-column-${classSuffix}" rowspan=2>
                        ${num}
                    </td>
                    <td class="report-column-${classSuffix}">
                        <font color=green><code>${parameter.name}</code></font>
                    </td>
                    <td class="report-column-${classSuffix}">
                        <#if parameter.required>Yes<#else>&nbsp;</#if>
                    </td>
                    <td class="report-column-${classSuffix}">
                        ${parameter.defaultValue?default('&nbsp;')}
                    </td>
                    <td class="report-column-${classSuffix}">
                        <#if parameter.hasEnums()>
                            ${statics["com.netspective.commons.text.TextUtils"].join(parameter.enums, ", ")}
                        <#elseif parameter.hasFlags()>
                            ${statics["com.netspective.commons.text.TextUtils"].join(parameter.flags.flagNames, " | ")}
                        <#else>
                            &nbsp;
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td class="report-column-${classSuffix}" colspan=4>
                        <font color=#999999>${parameter.description}</font>
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
        <p>
    </#if>

    <@panel heading="All Commands">
        <@reportTable>
        <#assign classSuffix="odd"/>
            <tr>
                <td class="report-column-heading">Command</td>
                <td class="report-column-heading">Class</td>
                <td class="report-column-heading">Description</td>
            </tr>
        <#list commandClassesMap.keySet().iterator() as command>
            <#assign commandClass = commandClassesMap.get(command)/>
            <#assign docs = factory.getCommandDocumentation(commandClass)/>
            <tr>
                <td class="report-column-${classSuffix}">
                    <a href="?selected-command=${command}"><b>${command}</b></a>
                </td>
                <td class="report-column-${classSuffix}">
                    <@classReference commandClass.name/>
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