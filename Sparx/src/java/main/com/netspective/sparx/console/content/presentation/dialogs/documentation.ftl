<#include "*/library.ftl">

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
    <@panel heading="Dialog '${dialog.qualifiedName}' Documentation">
        <@reportTable
                headings = ["Property", "Value"]
                data=[
                  ["Qualified Name", dialog.qualifiedName],
                  ["Dialog Name", dialog.name],
                  ["HTML Form Name", dialog.htmlFormName],
                  ["Dialog Class", dialog.class.name],
                  ["DialogContext Class", dialog.dcClass.name],
                  ["DialogDirector Class", dialog.director.class.name?default('&nbsp;')],
                  ["Heading", dialog.frame.heading.specification?default('&nbsp;')],
                  ["Number of Fields", dialog.fields.size()],
                  ["Dialog Flags", dialog.dialogFlags],
                  ["Debug Flags", dialog.debugFlags],
                  ["Loop Style", dialog.loop + " (separator '" + dialog.loopSeparator?html + "')"],
                  ["Retain Params", statics["com.netspective.commons.text.TextUtils"].join(dialog.retainParams, ', ') + "&nbsp;"],
                  ["Include JS", dialog.clientJs.toString()]
                  ]/>
    </@panel>
</#macro>

<#macro listDialogFields dialog>
    <#assign fields = []/>
    <#list dialog.fields.fieldsList as field>
        <#assign fields = fields + [
                    ["<a href='?selected-dialog-id=${selectedDialogId}&amp;selected-field-id=${field.qualifiedName?default('')}'>" + field.qualifiedName?default('(none)') + "</a>", field.caption?default('&nbsp;'), field.fieldTypes.toString(), getClassReference(field.class.name)]
                    ]/>
    </#list>

    <@panel heading="Dialog '${dialog.qualifiedName}' Fields">
        <@reportTable
                headings = ["Field", "Caption", "Type(s)", "Class"]
                data=fields/>
    </@panel>
</#macro>

<#macro describeField dialog fieldId>
    <#assign field = dialog.fields.getByQualifiedName(fieldId)/>

    <#assign schema = getXmlDataModelSchema(field.class.name)/>
    <#assign settableAttributesDetail = schema.getSettableAttributesDetail(false)/>
    <#assign childElements = schema.getNestedElementsDetail()/>

    <#assign attribs = []/>
    <#list settableAttributesDetail as attrDetail>
        <#if attrDetail.attrName = 'class'>
            <#assign attribs = attribs + [
                        [attrDetail.attrName, getClassReference(field.class.name), getClassReference(attrDetail.attrType.name)]
                        ]/>
        <#else>
            <#assign accessor = attrDetail.accessor?default('-')/>
            <#if accessor != '-'>
                <#assign attrValue = accessor.get(null, field)?default('NULL')/>
            <#else>
                <#assign attrValue = '<i><font color=red>no accessor available</font></i>'/>
            </#if>
            <#assign attribs = attribs + [
                        [attrDetail.attrName, attrValue, getClassReference(attrDetail.attrType.name)]
                        ]/>
         </#if>
    </#list>

    <@panel heading="Dialog Field '${dialog.qualifiedName}.${field.qualifiedName}' Inspector">
        <@reportTable
                headings = ["Property", "Value", "Type"]
                data=attribs/>
    </@panel>
</#macro>
