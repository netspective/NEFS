<#include "*/library.ftl">

<#assign openConnContextsSet = statics["com.netspective.axiom.connection.AbstractConnectionContext"].getConnectionContextsWithOpenConnections()/>
<#assign catalog = []/>
<#list openConnContextsSet as connContext>
    <#assign overview = "DataSource '" + connContext.dataSourceId + "', created on " + connContext.creationDate?datetime/>
    <#if connContext.isBoundToSession()>
        <#assign overview = overview + ", bound to session."/>
    <#else>
        <#assign overview = overview + ", not bound to session."/>
    </#if>
    <#assign catalog = catalog + [[overview, "<pre>Instance: " + connContext + "<p>" + connContext.connectionOpenStackStrace + "</pre>"]]/>
</#list>

<@panel heading="Connection Contexts with Open Connections">
    <@reportTable headings=["Details", "Stack frame when connection was opened"] data=catalog/>
</@panel>
