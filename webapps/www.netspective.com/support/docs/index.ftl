<table class="data-table">
    <tr>
        <th>Document</th>
        <th>Purpose</th>
    </tr>
    <tr>
        <td><a href="${resourcesPath}/support/docs/nef-articles/getting-started.html" target="nefs-getting-started">Getting Started with NEFS</a></td>
        <td>Provides instructions for how to evaluate the Netspective Enterprise Frameworks Suite (<i>opens in a new window</i>).</td>
    </tr>
    <tr>
        <td><a href="${resourcesPath}/support/docs/nef-manual/index.html" target="nefs-um">NEFS User's Manual</a></td>
        <td>Provides instructions for how to use the Netspective Enterprise Frameworks Suite (<i>opens in a new window</i>).</td>
    </tr>
    <tr>
        <td><a href="documentation/upgrading">NEFS Upgrade Guide</a></td>
        <td>Provides instructions for how to upgrade one or more of our Java Frameworks.</td>
    </tr>
    <tr>
        <td><a href="documentation/change-log">NEFS Changes Log</a></td>
        <td>Provides a history of the changes to NEFS since 7.0 was released.</td>
    </tr>

    <#list sampleApps as app>
    <#if app.tutorialUrl?exists>
    <tr>
        <td><a href="${app.tutorialUrl}">${app.tutorialName}</a></td>
        <td>${app.tutorialDescr}</td>
    </tr>
    </#if>
    </#list>

    <tr>
        <td><a href="http://www.netspective.com/old-devel/">Old developer.netspective.com site</a></td>
        <td>In case you need access to the old site, the contents remain available.</td>
    </tr>
</table>
