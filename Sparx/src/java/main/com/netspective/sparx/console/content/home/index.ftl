<#include "*/library.ftl"/>

<table cellpadding=5>
    <tr valign=top>
        <td>
            <@panel heading="Configuration Overview">
                <@reportTable>
                    <tr>
                        <td class="report-column-heading">Property</td>
                        <td class="report-column-heading">Value</td>
                    </tr>
                    <tr>
                        <td class="report-column-odd"><nobr>Application Id</td>
                        <td class="report-column-odd"><code>${vc.servletContext.servletContextName}</code></td>
                    </tr>
                    <tr>
                        <td class="report-column-even"><nobr>Application Home</td>
                        <td class="report-column-even"><code>${vc.servletContext.getRealPath('')}</code></td>
                    </tr>
                    <tr>
                        <td class="report-column-odd"><nobr><a href="project/project-source">Sparx Project Source</a></td>
                        <td class="report-column-odd"><code><@projectFile/></td>
                    </tr>
                    <tr>
                        <td class="report-column-even"><nobr><a href="manual/project/runtime-environment">Runtime Environment</a></td>
                        <td class="report-column-even"><code>${vc.environmentFlags.flagsText}</code></td>
                    </tr>
                    <tr>
                        <td class="report-column-odd"><nobr><a href="project/configuration/servlet">Deployment Descriptor</a></td>
                        <td class="report-column-odd"><code>${vc.servletContext.getRealPath('WEB-INF/web.xml')}</code></td>
                    </tr>
                </@reportTable>
            </@panel>
            <p>
            <@panel heading="Versions">
                <@reportTable>
                    <tr>
                        <td class="report-column-heading">Component</td>
                        <td class="report-column-heading">Version</td>
                    </tr>
                    <tr>
                        <td class="report-column-odd">Java Developers Kit (${statics["java.lang.System"].getProperty("java.vendor")})</td>
                        <td class="report-column-odd">${statics["java.lang.System"].getProperty("java.version")}</td>
                    </tr>
                    <tr>
                        <td class="report-column-even">Java Virtual Machine (${statics["java.lang.System"].getProperty("java.vm.vendor")})</td>
                        <td class="report-column-even">${statics["java.lang.System"].getProperty("java.vm.version")}</td>
                    </tr>
                    <tr>
                        <td class="report-column-odd">Operating System (${statics["java.lang.System"].getProperty("os.name")})</td>
                        <td class="report-column-odd">${statics["java.lang.System"].getProperty("os.version")}</td>
                    </tr>
                    <tr>
                        <#assign serverInfo = vc.servletContext.serverInfo?split("/")/>
                        <td class="report-column-even">Application Server<#if serverInfo?size gt 1> (${serverInfo[0]})</#if></td>
                        <td class="report-column-even"><#if serverInfo?size gt 1>${serverInfo[1]}<#else>${vc.servletContext.serverInfo}</#if></td>
                    </tr>
                    <tr>
                        <td class="report-column-odd">Sparx Application Platform (Netspective)</td>
                        <td class="report-column-odd">${statics["com.netspective.sparx.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()}</td>
                    </tr>
                    <tr>
                        <td class="report-column-even">Axiom Persistence Framework (Netspective)</td>
                        <td class="report-column-even">${statics["com.netspective.axiom.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()}</td>
                    </tr>
                    <tr>
                        <td class="report-column-odd">Commons Utility Library (Netspective)</td>
                        <td class="report-column-odd">${statics["com.netspective.commons.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()}</td>
                    </tr>
                </@reportTable>
            </@panel>
        </td>
        <td>
            <@panel heading="Welcome">
                <div class="textbox">
                    Welcome to the Netspective Enterprise Console for your web application.
                    The Console is a Sparx servlet that provides a browser-based administrative interface to all of the dynamic
                    Netspective Enterprise Frameworks Suite (NEFS) components and objects associated with your application. The Console is
                    automatically available to all NEFS-based applications during development and can be optionally turned off in
                    production (for security).
                </div>
            </@panel>
            <p>
            <@panel heading="Console Security">
                <div class="textbox">
                    Because the Console provides full access to all aspects of your applications, it's important to ensure that it is
                    appropriately secured before the application is put into production use. The Console may be secured by using the
                    following methods:
                    <ul>
                        <li>Use the Console's built-in security model.
                        <li>Use Servlet filters that can be wrapped around the ConsoleServlet to provide a custom authentication scheme.
                        <li>Disable the ConsoleServlet completely by commenting it out in the application's
                        <a href="application/configuration/servlet">J2EE Servlet Deployment Descriptor</a> file).
                    </ul>
                </div>
            </@panel>
        </td>
    </tr>
</table>