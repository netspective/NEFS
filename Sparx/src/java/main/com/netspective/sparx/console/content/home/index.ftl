<#include "*/library.ftl"/>

<table cellpadding=5>
    <tr valign=top>
        <td>
            <@panel heading="Configuration Overview">
                <@reportTable
                        headings = ["Property", "Value"]
                        data=[
                          ["Application Id", "<code>${vc.servlet.servletContext.servletContextName}</code>"],
                          ["Application Home", "<code>${vc.getConsoleFileBrowserLink(vc.servlet.servletContext.getRealPath(''), false)}</code>"],
                          ["<a href='project/project-source'>Sparx Project Source</a>", "<code>${vc.getConsoleFileBrowserLink(vc.projectComponent.inputSource.identifier, true)}</code>"],
                          ["<a href='project/configuration/runtime-environment'>Runtime Environment</a>", "<code>${vc.runtimeEnvironmentFlags.flagsText}</code>"],
                          ["<a href='project/configuration/servlet'>Deployment Descriptor</a>", "<code>${vc.getConsoleFileBrowserLink(vc.servlet.servletContext.getRealPath('WEB-INF/web.xml'), true)}</code>"]
                          ]/>
            </@panel>
            <p>
            <@panel heading="Versions">
                <#assign appServerInfo = vc.servlet.servletContext.serverInfo?split("/")/>
                <#if appServerInfo?size gt 1>
                    <#assign appServerName = " (${appServerInfo[0]})"/>
                    <#assign appServerVersion = appServerInfo[1]/>
                <#else>
                    <#assign appServerName = ""/>
                    <#assign appServerVersion = appServerInfo/>
                </#if>

                <@reportTable
                        headings = ["Component", "Version"]
                        data=[
                          ["Java Developers Kit (${statics['java.lang.System'].getProperty('java.vendor')})", statics["java.lang.System"].getProperty("java.version")],
                          ["Java Virtual Machine (${statics['java.lang.System'].getProperty('java.vm.vendor')})", statics["java.lang.System"].getProperty("java.vm.version")],
                          ["Operating System (${statics['java.lang.System'].getProperty('os.name')})", statics["java.lang.System"].getProperty("os.version")],
                          ["Application Server${appServerName}", appServerVersion],
                          ["Sparx Application Platform (Netspective)", statics["com.netspective.sparx.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()],
                          ["Axiom Relational Data Service (Netspective)", statics["com.netspective.axiom.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()],
                          ["Commons Core Library (Netspective)", statics["com.netspective.commons.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()]
                          ]/>
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
                        <a href="project/configuration/servlet">J2EE Servlet Deployment Descriptor</a> file).
                    </ul>
                </div>
            </@panel>
        </td>
    </tr>
</table>