<#include "*/library.ftl"/>

<div class="textbox">

The standard Sparx project file is located at <code>/WEB-INF/sparx/project.xml</code>. If you would like to change
the default Sparx project file, simply define a servlet context init parameter in <code>WEB-INF/web.xml</code> with
the appropriate alternate location.
<p>
Example:
<@xmlCode>
<web-app>
  ...
    <context-param>
      <param-name>com.netspective.sparx.PROJECT_FILE_NAME</param-name>
      <param-value>/WEB-INF/sparx/project.xml</param-value>
    </context-param>
  ...
</web-app>
</@xmlCode>

</div>