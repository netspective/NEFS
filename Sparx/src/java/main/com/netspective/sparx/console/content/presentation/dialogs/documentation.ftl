<#include "*/library.ftl">

<#assign factory = statics["com.netspective.commons.value.ValueSources"].getInstance()/>
<#assign valueSourceClassesMap = factory.getValueSourceClassesMap()/>
<#assign describeValueSource = vc.getRequest().getParameter('selected-value-source')?default('-')/>

<#if describeValueSource != '-' && valueSourceClassesMap.containsKey(describeValueSource)>
    <#assign valueSourceClass = valueSourceClassesMap.get(describeValueSource)/>
    <#assign docs = factory.getValueSourceDocumentation(valueSourceClass)?default('-')/>
    <#if docs != '-'>
        <@panel heading="${describeValueSource} Value Source Documentation">
            <div class=textbox>
            ${docs.description}
            <#assign aliases = factory.getValueSourceAliases(describeValueSource)/>
            <#if aliases.size() == 1>
                <br>Alias: ${aliases}
            <#elseif aliases.size() gt 1>
                <br>Aliases: ${aliases}
            </#if>
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
            <div class=textbox>
            <img src="${vc.getThemeResourcesRootUrl(vc.activeTheme) + "/images/java-class.gif"}"> Handler: <code>${valueSourceClass.name}</code>
            </div>
        </@panel>
    <#else>
        <div class="textbox">No documentation available for value source <code>${describeValueSource}</code>.</div>
    </#if>
<#else>
    <div class="textbox">Please select a value source from the <a href='catalog'>Catalog</a> first.</div>
</#if>

