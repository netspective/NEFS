<div class="textbox">
    <ul>
        <li>Your application's context identifier is <code>${vc.servletContext.servletContextName}</code>.
        <li>Your application's root directory is <code>${vc.servletContext.getRealPath('')}</code>.
        <li>Your application's <a href="application/configuration/servlet">J2EE Servlet configuration</a> file is <code>${vc.servletContext.getRealPath('WEB-INF/web.xml')}</code>.
        <li>Your application's <a href="framework/xdm/components">Sparx configuration file</a> is <code>${vc.applicationManagerComponent.inputSource.identifier}</code>.
        <li>
            Based on your application's Servlet configuration file (<code>web.xml</code>), your application is using the
            following <a href="application/runtime-environment">Runtime Environment Flags</a>:
            <font color="darkred"><code>${vc.environmentFlags}</code></font>.
        </li>
    </ul>
</div>