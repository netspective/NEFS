<#include "*/library.ftl"/>

<div class="textbox">
    <ul>
        <li>Your application's context identifier is <code>${vc.servletContext.servletContextName}</code>.
        <li>Your application's root directory is <code>${vc.servletContext.getRealPath('')}</code>.
        <li>Your application's <a href="application/configuration/servlet">J2EE Servlet configuration</a> file is <code>${vc.servletContext.getRealPath('WEB-INF/web.xml')}</code>.
        <li>Your application's <a href="framework/xdm/components">Sparx project file</a> is <@projectFile/>.
        <li>
            Based on your application's Servlet configuration file (<code>web.xml</code>), your application is using the
            following <a href="application/runtime-environment">Runtime Environment Flags</a>:
            <font color="darkred"><code>${vc.environmentFlags}</code></font>.
        </li>
        <li>Your Application Server is <code>${vc.servletContext.serverInfo}</code>.
    </ul>
</div>