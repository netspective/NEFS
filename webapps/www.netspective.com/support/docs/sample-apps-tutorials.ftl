<#assign samplesServerVS = statics["com.netspective.commons.value.ValueSources"].getInstance().getValueSourceOrStatic("netspective-url:nefs-sample-apps-server")/>
<#assign samplesServer = samplesServerVS.getTextValue(vc)/>
<table class="data-table">
    <tr>
        <th>Document</th>
        <th>Purpose</th>
        <th>Links</th>
    </tr>
    <#list sampleApps as app>
    <#if app.tutorialUrl?exists>
    <tr>
        <td><a href="${app.tutorialUrl}">${app.tutorialName}</a></td>
        <td>${app.tutorialDescr}</td>
        <td>
            <a href="${samplesServer}/${app.id}">${app.name} App</a><br>
            <a href="${samplesServer}/${app.id}/console">${app.name} Console</a>
        </td>
    </tr>
    </#if>
    </#list>
</table>
