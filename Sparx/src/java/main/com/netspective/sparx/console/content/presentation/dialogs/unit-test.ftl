<#include "/content/library.ftl"/>

<div class="textbox">
    <#assign selectedDialogId = vc.getRequest().getParameter('selected-dialog-id')?default('-')/>
    <#assign dialogs = vc.project.dialogs/>

    <#if selectedDialogId != '-' && dialogs.names.contains(selectedDialogId?upper_case)>
        ${executeCommand("dialog," + selectedDialogId)}
    <#else>
        Please select a dialog from the <a href='catalog'>Catalog</a> first.
    </#if>
</div>