<#include "/content/library.ftl"/>

<div class="textbox">

    <#assign factory = statics["com.netspective.commons.command.Commands"].getInstance()/>
    <#assign commandClassesMap = factory.getClassesMap()/>
    <#assign describeCommand = vc.getRequest().getParameter('selected-command')?default('-')/>

    <#if describeCommand != '-' && commandClassesMap.containsKey(describeCommand)>
        Try out the various options provided by the <code>${describeCommand}</code> command by using the
        form below.
        <p>
        <FORM name="unitTestCommand" method="GET" action="unit-test">
                Command:
                <input name="cmd" type="text" size=60 value="${describeCommand},">
                <input value="Test" type="submit">
                <input name="selected-command" type="hidden" value="${describeCommand}">
        </FORM>
        <p>
        <#include "documentation.ftl"/>
    <#else>
        Please select a command from the <a href='catalog'>Catalog</a> first.
    </#if>
</div>