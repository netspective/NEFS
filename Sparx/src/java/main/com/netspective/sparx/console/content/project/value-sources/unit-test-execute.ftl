<#include "*/library.ftl">

<#assign fieldState = vc.fieldStates.getState("value-source-spec")/>
<#assign valueSource = statics["com.netspective.commons.value.ValueSources"].getInstance().getValueSource(fieldState.value.textValue, 2)/>
<#assign value = valueSource.getValue(vc)/>
<#assign pValue = valueSource.getPresentationValue(vc)?default('-')/>

<@panel heading="Value Source Test Results">
<@reportTable>
    <tr>
        <td class="report-column-even">&nbsp;</td>
        <td class="report-column-even">Test Specification</td>
        <td class="report-column-even"><code>${fieldState.value.textValue}</code></td>
    </tr>
    <tr>
        <td class="report-column-odd"><img src="${vc.getThemeResourcesRootUrl(vc.activeTheme) + "/images/java-class.gif"}"/></td>
        <td class="report-column-odd">Value Source Class</td>
        <td class="report-column-odd"><code>${valueSource.class.name}</code></td>
    </tr>
    <tr>
        <td class="report-column-even"><img src="${vc.getThemeResourcesRootUrl(vc.activeTheme) + "/images/java-class.gif"}"/></td>
        <td class="report-column-even">Value Class</td>
        <td class="report-column-even"><code>${value.class.name}</code></td>
    </tr>
    <tr>
        <td class="report-column-odd">&nbsp;</td>
        <td class="report-column-odd">Text Value</td>
        <td class="report-column-odd"><code>${value.textValue?default('NULL')}</code></td>
    </tr>
    <tr>
        <td class="report-column-even">&nbsp;</td>
        <td class="report-column-even">Presentation Value</td>
        <td class="report-column-even">
            <#assign itemCount = pValue.items.size()/>
            <#if itemCount = 0>No presentation items.</#if>
            <#if itemCount = 1>One presentation item.</#if>
            <#if itemCount gt 1>${itemCount} presentation items.</#if>
            <#if pValue != '-' && pValue.items?default('-') != '-'>
                <@reportTable>
                    <#assign classSuffix="odd"/>
                    <tr>
                        <td class="report-column-heading">Caption</td>
                        <td class="report-column-heading">Value</td>
                        <td class="report-column-heading">Flags</td>
                        <td class="report-column-heading">Custom</td>
                    </tr>
                    <#list pValue.items as item>
                    <tr>
                        <td class="report-column-${classSuffix}">${item.caption?default('-')}</td>
                        <td class="report-column-${classSuffix}">${item.value?default('-')}</td>
                        <td class="report-column-${classSuffix}">${item.flags?default('-')}</td>
                        <td class="report-column-${classSuffix}">${item.custom?default('-')}</td>
                    </tr>
                    <#if classSuffix = 'odd'>
                        <#assign classSuffix='even'/>
                    <#else>
                        <#assign classSuffix='odd'/>
                    </#if>
                    </#list>
                </@reportTable>
            </#if>
        </td>
    </tr>
</@reportTable>
</@panel>
