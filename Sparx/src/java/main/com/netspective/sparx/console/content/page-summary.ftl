<div class="textbox" style="background-color: #f8f8f8; font-size: 9pt">
    <#assign templateName = "/content${vc.navigationContext.activePage.qualifiedName}/summary.ftl">
    <#if templateExists(templateName)>
        <#include templateName>
    <#else>
        Template <font color=red>${templateName}</font> was not found.
    </#if>
</div>