<div class="textbox">
    Your application is using the following RuntimeEnvironmentFlags: <font color=red><code>${vc.environmentFlags}</code></font>.
    If you'd like to modify the runtime environment flags, do the following:
    <ol>
        <li>Open your application's web.xml file (<code>${vc.servletContext.getRealPath('WEB-INF/web.xml')}</code>).</li>
        <li>Modify the <code>com.netspective.sparx.RUNTIME_ENVIRONMENT_FLAGS</code> context parameter. The parameter may
        be set to a single value or may contain multiple flags separated using the '<code>|</code>' character. The valid
        values for the environment flag names are:
            <ul>
            <#list vc.environmentFlags.flagNames as i>
                <li>${i}</li>
            </#list>
            </ul>
    </ol>
</div>