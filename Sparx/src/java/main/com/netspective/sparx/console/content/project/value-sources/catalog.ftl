<#include "/content/library.ftl"/>

<#assign factory = statics["com.netspective.commons.value.ValueSources"].getInstance()/>
<#assign valueSourceClassesMap = factory.getValueSourceClassesMap()/>
<#assign catalog = []/>
<#assign noDocs = { "description" : "No description available." }/>
<#list valueSourceClassesMap.keySet().iterator() as valueSource>
    <#assign valueSourceClass = valueSourceClassesMap.get(valueSource)/>
    <#assign docs = factory.getValueSourceDocumentation(valueSourceClass)?default(noDocs)/>
    <#assign catalog = catalog + [["<a href='unit-test?selected-value-source=${valueSource}'><b>${valueSource}</b></a>", docs.description, factory.getUsageCount(valueSourceClass)]]/>
</#list>

<@panel heading="All Available Value Sources">
    <@reportTable headings=["Command", "Description", "Used"] data=catalog columnAttrs=["", "", "align=right"]/>
</@panel>
