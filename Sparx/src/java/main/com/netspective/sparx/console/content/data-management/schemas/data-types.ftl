<#include "/content/library.ftl"/>

<@reportTable>
    <#assign schemas = vc.sqlManager.schemas/>
    <#assign classSuffix = reportRowClassSuffix()/>

    <tr>
        <td class="report-column-heading">Data Type</td>
        <td class="report-column-heading">Class</td>
        <td class="report-column-heading">Source</td>
    </tr>

    <#list schemas.names as schemaName>
        <#assign schema = schemas.getByName(schemaName)/>

        <tr>
            <td class="report-column-${classSuffix}" colspan="3">
                Schema: '${schema.name}'
            </td>
        </tr>
        <#assign classSuffix = reportRowClassSuffix(classSuffix)/>

        <#assign instancesMap = schema.dataTypes.instancesMap/>
        <#list instancesMap.keySet().iterator() as typeName>
            <#assign template = instancesMap.get(typeName)/>
            <#assign className = template.alternateClassName?default('com.netspective.axiom.schema.column.BasicColumn')/>
            <tr>
                <td class="report-column-${classSuffix}">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${vc.consoleUrl}/reference/templates?ns=${template.templateProducer.nameSpaceId}&tmpl=${typeName}">${typeName}</a>
                </td>
                <td class="report-column-${classSuffix}">
                    <@classReference className/>
                </td>
                <td class="report-column-${classSuffix}" style="color: #999999">
                    <code>${vc.getConsoleFileBrowserLink(template.inputSourceLocator.inputSourceTracker.identifier, true)} ${template.inputSourceLocator.lineNumbersText}</code>
                </td>
            </tr>
            <#assign classSuffix = reportRowClassSuffix(classSuffix)/>
        </#list>
    </#list>
</@reportTable>
