<#include "*/library.ftl">

<div class="textbox">

    <#assign factory = statics["com.netspective.commons.value.ValueSources"].getInstance()/>
    <#assign valueSourceClassesMap = factory.getValueSourceClassesMap()/>
    <#assign describeValueSource = vc.getRequest().getParameter('selected-value-source')?default('-')/>

    <#if describeValueSource != '-' && valueSourceClassesMap.containsKey(describeValueSource)>
        ${executeCommand("dialog,console.value-source-test")}
        <p>
        <#include "documentation.ftl"/>
    <#else>
        Please select a value source from the <a href='catalog'>Catalog</a> first.
    </#if>
</div>