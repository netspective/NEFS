<#include "*/library.ftl"/>

<div class="textbox">
    The current runtime environment is available within your Java applications by using the
    <code>ValueContext.getRuntimeEnvironmentFlags()</code> method from any value context instance like NavigationContext
    or DialogContext.
    <p>
    Based on your application's configuration file <@webXmlFile/>, your application is using the following
    RuntimeEnvironmentFlags: <font color=red><code>${vc.runtimeEnvironmentFlags}</code></font>.

    <#assign navControllerServetOptionsParamName = 'com.netspective.sparx.navigate.CONTROLLER_SERVLET_OPTIONS'>
    If you'd like to modify the runtime environment flags, do the following:
    <ol>
        <li>Open your application's web.xml file (<@webXmlFile/>).</li>
        <li>Modify the <code>${navControllerServetOptionsParamName}</code> servlet init parameter. The parameter may
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
    <servlet>
        <servlet-name>SparxNavigationController</servlet-name>
        <servlet-class>com.netspective.sparx.navigate.NavigationControllerServlet</servlet-class>
        <init-param>
            <param-name>com.netspective.sparx.navigate.CONTROLLER_SERVLET_OPTIONS</param-name>
            <param-value>--runtime-environment=PRODUCTION</param-value>
        </init-param>
    </servlet>
      ...
</web-app>

<web-app>
      ...
    <servlet>
        <servlet-name>SparxNavigationController</servlet-name>
        <servlet-class>com.netspective.sparx.navigate.NavigationControllerServlet</servlet-class>
        <init-param>
            <param-name>com.netspective.sparx.navigate.CONTROLLER_SERVLET_OPTIONS</param-name>
            <param-value>--runtime-environment="PRODUCTION | UNDERGOING_MAINTENANCE"</param-value>
        </init-param>
    </servlet>
      ...
</web-app>

    </@xmlCode>


</div>