<h1>Review the Getting Started Guide</h1>
Please review the <a href="${servletPath}/support/documentation">Getting Started with NEFS</a> Guide to help orient you
with how to best evaluate the Netspective Java Frameworks.

<h1>Try the Sample Applications Online</h1>
The links shown below allow you download NEFS sample applications but another way evaluate the Netspective Frameworks
is to view all the documentation online and follow along with the <a href="${servletPath}/products/frameworks/try">online
versions</a> of the sample applications. Since all you need is a web browser, you can start learning about NEF immediately.

<h1>Try the Sample Applications on your own App Server</h1>
Each of the downloadable sample applications are provided as app-server-independent <code>.war</code> files. Each sample
app's <code>.war</code> file includes all of the NEF binary files, resources, and everything you need to use NEF. Once
you have downloaded any of <code>.war</code> files shown below, you have everything you need to start modifying and
adding to the app. In essence, you're getting the sample app and the NEFS evaluation kit one one bundle.
<p>
<table class="data-table">
    <#list sampleApps as app>
    <#if app.allowDownload>
    <tr valign=top>
        <td><a href="${vc.rootUrl}/resources/downloads/${app.id}.war">${app.name}</a></td>
        <td>${app.descr}</td>
    </tr>
    </#if>
    </#list>
</table>
