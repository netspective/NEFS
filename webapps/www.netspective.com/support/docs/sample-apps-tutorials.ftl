<#assign samplesServerVS = statics["com.netspective.commons.value.ValueSources"].getInstance().getValueSourceOrStatic("netspective-url:nefs-sample-apps-server")/>
<#assign samplesServer = samplesServerVS.getTextValue(vc)/>
<table class="data-table">
    <tr>
        <th>Document</th>
        <th>Purpose</th>
        <th>Related</th>
    </tr>
    <#list sampleApps as app>
    <#if app.tutorialUrl?exists>
    <tr>
        <td>
            ${app.tutorialName} (<a href="${app.tutorialUrl}" title="Online (HTML) Version of Tutorial">HTML</a>)
            <#if app.tutorialPDF?exists>
            (<a href="${app.tutorialPDF}" title="PDF Version of Tutorial">PDF</a>)
            </#if>
        </td>
        <td>${app.tutorialDescr}</td>
        <td>
            <a href="${samplesServer}/${app.id}">${app.name} App</a><br><br>
            <a href="${samplesServer}/${app.id}/console">${app.name} Console</a><br><br>
            <a href="${vc.rootUrl}/resources/downloads/${app.id}.war">Download ${app.name}.war</a>
        </td>
    </tr>
    </#if>
    </#list>
</table>
