<#include "/content/library.ftl"/>

<#assign factory = statics["com.netspective.commons.command.Commands"].getInstance()/>
<#assign commandClassesMap = factory.getClassesMap()/>
<#assign describeCommand = vc.getRequest().getParameter('selected-command')?default('-')/>

<#if describeCommand != '-' && commandClassesMap.containsKey(describeCommand)>
    <#assign commandClass = commandClassesMap.get(describeCommand)/>
    <#assign docs = factory.getCommandDocumentation(commandClass)?default('-')/>
    <#if docs != '-'>
        <@panel heading="${describeCommand} Command Documentation">
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
                            ${statics["com.netspective.commons.text.TextUtils"].getInstance().join(parameter.enums, ", ")}
                        <#elseif parameter.hasFlags()>
                            ${statics["com.netspective.commons.text.TextUtils"].getInstance().join(parameter.flags.flagNames, " | ")}
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
            <div class=textbox>
            <img src="${vc.activeTheme.getResourceUrl("/images/java-class.gif")}"> Handler: <code>${commandClass.name}</code>
            </div>
        </@panel>
    <#else>
        <div class="textbox">No documentation available for command <code>${describeCommand}</code>.</div>
    </#if>
<#else>
    <div class="textbox">Please select a command from the <a href='catalog'>Catalog</a> first.</div>
</#if>
