<#include "*/library.ftl">

<#assign openConnContextsSet = statics["com.netspective.axiom.connection.AbstractConnectionContext"].getConnectionContextsWithOpenConnections()/>
<#assign catalog = []/>
<#list openConnContextsSet as connContext>
    <#assign overview = "DataSource '" + connContext.dataSourceId + "', created on " + connContext.creationDate?datetime/>
    <#if connContext.ownership = 0>
        <#assign overview = overview + ", owned by application."/>
    <#else>
        <#assign overview = overview + ", owned by session (user)."/>
    </#if>
    <#assign catalog = catalog + [[overview, "<pre>" + connContext.connectionOpenStackStrace + "</pre>"]]/>
</#list>

<@panel heading="Connections Contexts with Open Connections">
    <@reportTable headings=["Details", "Stack frame when connection was opened"] data=catalog/>
</@panel>
