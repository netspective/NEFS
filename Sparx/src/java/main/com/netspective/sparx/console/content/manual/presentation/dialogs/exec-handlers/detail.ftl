<#include "*/library.ftl">

<div class="textbox">

    <#assign typeName = vc.request.getParameter("detail")?default("-")/>
    <#if typeName = '-'>
        Please choose one of the dialog execute handlers listed below to view details.
    <#else>
        <#assign instancesMap = vc.project.dialogExecuteHandlers.instancesMap/>
        <#assign className = instancesMap.get(typeName).attributes.getValue("class")/>
        <@xdmStructure className=className heading="Documentation for &lt;dialog-execute-handler type='${typeName}'&gt;" expandFlagAliases='yes'/>
    </#if>

</div>