<#include "*/library.ftl">

<#assign factory = statics["com.netspective.commons.command.Commands"].getInstance()/>
<#assign commandClassesMap = factory.getClassesMap()/>
<#assign catalog = []/>
<#assign noDocs = { "description" : "No description available." }/>
<#list commandClassesMap.keySet().iterator() as command>
    <#assign commandClass = commandClassesMap.get(command)/>
    <#assign docs = factory.getCommandDocumentation(commandClass)?default(noDocs)/>
    <#assign catalog = catalog + [["<a href='unit-test?selected-command=${command}'><b>${command}</b></a>", docs.description]]/>
</#list>

<@panel heading="All Available Commands">
    <@reportTable headings=["Command", "Description"] data=catalog/>
</@panel>
