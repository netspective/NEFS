<#include "*/library.ftl"/>

<@panel heading="Active Project Source">
<div class="textbox">

    The Sparx Project Source is the single entry point for Sparx component declarations and the actual file where you
    declare all the components that will be used by your application. If there are errors in any of your Sparx source
    files they will be displayed here.

    <p>

    <table class="report" border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-odd">Data Model</td>
            <td class="report-column-odd"><code>${vc.project.class.name}</code></td>
        </tr>
        <tr>
            <td class="report-column-even">Input Source and Dependencies</td>
            <td class="report-column-even">
                ${getInputSourceDependencies()}
            </td>
        </tr>
        <tr>
            <td class="report-column-odd"><a name="errors">Errors</a></td>
            <td class="report-column-odd">
                <#if vc.projectComponent.errors?size = 0>
                    None
                <#else>
                    <ol>
                    <#list vc.projectComponent.errors as error>
                        <li>${error}</li>
                    </#list>
                    </ol>
                </#if>
            </td>
        </tr>
        <tr>
            <td class="report-column-even">Warnings</td>
            <td class="report-column-even">
                <#if vc.projectComponent.warnings?size = 0>
                    None
                <#else>
                    <ol>
                    <#list vc.projectComponent.warnings as warning>
                        <li>${warning}</li>
                    </#list>
                    </ol>
                </#if>
            </td>
        </tr>
    </table>
</@panel>
    <p>
    <@panel heading="How to Specify A Differnt Project Source">
        <div class="textbox">
        If you would like to change the location of the default project file, simply define a servlet
        init parameter in <@webXmlFile/> with the appropriate alternate location.
        <p>
        Example:
<@xmlCode>
<web-app>
      ...
    <servlet>
        <servlet-name>SparxNavigationController</servlet-name>
        <servlet-class>com.netspective.sparx.navigate.NavigationControllerServlet</servlet-class>
        <init-param>
            <param-name>com.netspective.sparx.navigate.CONTROLLER_SERVLET_OPTIONS</param-name>
            <param-value>--project=/WEB-INF/other/path/project.xml</param-value>
        </init-param>
    </servlet>
      ...
</web-app>
</@xmlCode>
        </div>
    </@panel>

</div>