<#include "*/library.ftl"/>

<div class="textbox">

The standard Frameworks project file is located at <@projectFile/>. If you would like to change
the location of the default project file, simply define a servlet context init parameter in <@webXmlFile/> with
the appropriate alternate location.
<p>
Example:
<@xmlCode>
<web-app>
  ...
    <context-param>
      <param-name> com.netspective.sparx.navigate.CONTROLLER_SERVLET_OPTIONS</param-name>
      <param-value>--project=/WEB-INF/other/path/project.xml</param-value>
    </context-param>
  ...
</web-app>
</@xmlCode>

</div>