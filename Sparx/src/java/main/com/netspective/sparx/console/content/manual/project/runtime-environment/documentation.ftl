<#include "*/library.ftl"/>

<div class="textbox">
    The current runtime environment is available within your Java applications by using the
    <code>ValueContext.getRuntimeEnvironmentFlags()</code> method from any value context instance like NavigationContext
    or DialogContext.
    <p>
    Based on your application's configuration file <@webXmlFile/>, your application is using the following
    RuntimeEnvironmentFlags: <font color=red><code>${vc.runtimeEnvironmentFlags}</code></font>.

    <#assign paramName = 'com.netspective.sparx.RUNTIME_ENVIRONMENT_FLAGS'>
    If you'd like to modify the runtime environment flags, do the following:
    <ol>
        <li>Open your application's web.xml file (<@webXmlFile/>).</li>
        <li>Modify the <code>${paramName}</code> context parameter. The parameter may
        be set to a single value or may contain multiple flags separated using the '<code>|</code>' character. The valid
        values for the environment flag names are:
            <ul>
            <#list vc.runtimeEnvironmentFlags.flagNames as i>
                <li>${i}</li>
            </#list>
            </ul>
    </ol>
    <p>
    <b>Examples</b>

    <@xmlCode>
    <web-app>
      ...
      <context-param>
        <param-name>com.netspective.sparx.RUNTIME_ENVIRONMENT_FLAGS</param-name>
        <param-value>PRODUCTION</param-value>
      </context-param>
      ...
    </web-app>

    <web-app>
      ...
      <context-param>
        <param-name>com.netspective.sparx.RUNTIME_ENVIRONMENT_FLAGS</param-name>
        <param-value>PRODUCTION | UNDERGOING_MAINTENANCE</param-value>
      </context-param>
      ...
    </web-app>

    <web-app>
      ...
      <context-param>
        <param-name>com.netspective.sparx.RUNTIME_ENVIRONMENT_FLAGS</param-name>
        <param-value>DEVELOPMENT</param-value>
      </context-param>
      ...
    </web-app>
    </@xmlCode>


</div>