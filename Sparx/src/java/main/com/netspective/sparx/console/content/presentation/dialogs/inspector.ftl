<#include "/content/library.ftl"/>

<#assign selectedDialogId = vc.getRequest().getParameter('selected-dialog-id')?default('-')/>
<#assign selectedFieldId = vc.getRequest().getParameter('selected-field-id')?default('-')/>
<#assign dialogs = vc.project.dialogs/>

<#if selectedDialogId != '-' && dialogs.names.contains(selectedDialogId?upper_case)>
    <#assign selectedDialog = vc.project.getDialog(selectedDialogId)/>
    <#if selectedFieldId != '-'>
        <@describeField dialog=selectedDialog fieldId=selectedFieldId/>
        <p>
        <@listDialogFields dialog=selectedDialog/>
        <p>
        <@describeDialog dialog=selectedDialog/>
    <#else>
        <@describeDialog dialog=selectedDialog/>
        <p>
        <@listDialogFields dialog=selectedDialog/>
    </#if>
<#else>
    <div class="textbox">Please select a dialog from the <a href='catalog'>Catalog</a> first.</div>
</#if>

<#macro describeDialog dialog>
    <@inspectObject object=dialog heading="Dialog '${dialog.qualifiedName}' Documentation"/>
</#macro>

<#function appendFieldData dialogFields indent="">
    <#assign fieldsData = []/>
    <#list dialogFields.fieldsList as field>
        <#assign fieldsData = fieldsData + [
                    [indent + field_index, indent + "<a href='?selected-dialog-id=${selectedDialogId}&amp;selected-field-id=${field.qualifiedName?default('')}'>" + field.qualifiedName?default('(none)') + "</a>", field.caption?default('&nbsp;'), field.fieldTypes.toString(), getClassReference(field.class.name)]
                    ]/>
        <#if field.children?default('-') != '-'>
            <#assign fieldsData = fieldsData + appendFieldData(field.children, indent + "&nbsp;&nbsp;&nbsp;&nbsp;")/>
        </#if>
    </#list>
    <#return fieldsData/>
</#function>

<#macro listDialogFields dialog>
    <#assign fieldsData = appendFieldData(dialog.fields)/>
    <@panel heading="Dialog '${dialog.qualifiedName}' Fields">
        <@reportTable
                headings = ["&nbsp;", "Field", "Caption", "Type(s)", "Class"]
                data=fieldsData/>
    </@panel>
</#macro>

<#macro describeField dialog fieldId>
    <#assign field = dialog.fields.getByQualifiedName(fieldId)/>
    <@inspectObject object=field heading="Dialog Field '${dialog.qualifiedName}.${field.qualifiedName}' Inspector"/>
</#macro>
