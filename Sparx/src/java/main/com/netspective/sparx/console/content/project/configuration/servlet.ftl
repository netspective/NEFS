<#include "/content/library.ftl"/>

<#assign servletContext = vc.servlet.servletContext/>

<#assign initParams = []/>
<#list servletContext.initParameterNames as initParamName>
    <#assign initParams = initParams + [[initParamName, servletContext.getInitParameter(initParamName)]]/>
</#list>

<@panel heading="Servlet Context">
    <div class="textbox">
    Server: <code>${servletContext.serverInfo}</code><br>
    Servlet Context: <code>${servletContext.servletContextName}</code><br>
    Servlet Context Path: <code>${vc.getConsoleFileBrowserLink(servletContext.getRealPath(''), false)}</code><br>
    Servlet Context Descriptor: <@webXmlFile/><br>
    Servlet Context Class: <code>${servletContext.class.name}</code><br>
    </div>
    <@reportTable headings=["Init Param", "Value"] data=initParams/>
</@panel>

<#assign navgControllerServletClass = statics["com.netspective.sparx.navigate.NavigationControllerServlet"]/>
<#list navgControllerServletClass.getAllControllerServlets() as controller>

    <#assign initParams = []/>
    <#list controller.initParameterNames as initParamName>
        <#assign initParams = initParams + [[initParamName, controller.getInitParameter(initParamName)]]/>
    </#list>

    <#assign servletOptions = []/>

    <p>
    <@panel heading="Controller Servlet">
        <div class="textbox">
        Servlet Name: <code>${controller.servletName}</code><br>
        Servlet Class: <code>${controller.class.name}</code><br>
        </div>
        <@reportTable headings=["Init Param", "Value"] data=initParams/>
        <p>
        <@reportTable headings=["Servlet Option", "Value"] data=servletOptions/>
        <p>
        <pre>${controller.servletOptions.getHelp()?html}</pre>
    </@panel>

</#list>
