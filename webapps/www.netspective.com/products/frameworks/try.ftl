<h1>Review the Getting Started Guide</h1>
Please review the <a href="${servletPath}/support/documentation">Getting Started with NEFS</a> Guide to help orient you
with how to best evaluate the Netspective Java Frameworks.

<h1>Try the Samples Apps on your own App Server</h1>
The links shown below allow you try NEFS sample applications online but another way evaluate the Netspective Frameworks
is to <a href="${servletPath}/downloads/frameworks/samples">download</a> <code>.war</code> files of any of the applications and
give them a test drive on your own App Server.

<h1>Try the NEFS Sample Applications Online</h1>
Below you will see a list of all available sample applications and links to both the applications and their Consoles.
All NEFS application components can be viewed with the <i>Netspective Enterprise Console</i> servlet. Each application
has a private instance of the Console using <code>http://[server]/[appName]/console</code>. When you log into the
Console for Application X (<code>appX/console</code>) versus Y (<code>appY/console</code>) you will only see components
for the appropriate application. The Console helps you explore the contents of the application (all the presentation,
data management, security, and related application components including application source code).
<p>
<b>Note:</b>The login for Console in every sample app is the same: user is '<code>console</code>' and the password is
also '<code>console</code>'.
<p>
<#assign samplesServerVS = statics["com.netspective.commons.value.ValueSources"].getInstance().getValueSourceOrStatic("netspective-url:nefs-sample-apps-server")/>
<#assign samplesServer = samplesServerVS.getTextValue(vc)/>

<table class="data-table">
    <#list sampleApps as app>
    <#if app.allowTryOnline>
    <tr valign=top>
        <td><a href="${samplesServer}/${app.id}">${app.name}</a></td>
        <td><a href="${samplesServer}/${app.id}/console">Console</a></td>
        <td>${app.descr}</td>
    </tr>
    </#if>
    </#list>
</table>

