<!--
 ******************************************************************************
 ** MACRO: projectFile
 ** PARAMS: none
 ******************************************************************************
-->
<#macro projectFile>
    <code>${vc.applicationManagerComponent.inputSource.identifier}</code>
</#macro>

<!--
 ******************************************************************************
 ** MACRO: childrenSummaries
 ** PARAMS: sourcePageType ('parent' | 'active')
 ******************************************************************************
 -->
<#macro childrenSummaries sourcePageType>

<div class="textbox">
    <#if sourcePageType = 'parent'>
        <#assign sourcePage = vc.navigationContext.activePage.parent/>
        <#assign parentName = '.'/>
    <#else>
        <#assign sourcePage = vc.navigationContext.activePage/>
        <#assign parentName = sourcePage.name/>
    </#if>

    <table class="report" width=100% border="0" cellspacing="2" cellpadding="0">
    <#assign classSuffix="odd"/>

    <#list sourcePage.childrenList as child>
        <tr>
            <td class="report-column-${classSuffix}">
                <a href="${parentName}/${child.name}">${child.getCaption(vc)}</a>
            </td>
            <td class="report-column-${classSuffix}">
                <#assign summaryTemplateName = "/content${child.qualifiedName}/summary.ftl"/>
                <#if templateExists(summaryTemplateName)>
                    <#include summaryTemplateName/>
                <#else>
                    <font color=red>${summaryTemplateName} not available.</font>
                </#if>
            </td>
        </tr>
        <#if classSuffix = 'odd'>
            <#assign classSuffix='even'/>
        <#else>
            <#assign classSuffix='odd'/>
        </#if>
    </#list>
    </table>
</div>

</#macro>

<!--
 ****************************************************************************************
 ** MACRO: xdmChildStructure
 ** PARAMS: parentClassName (the name of the class the structure is being requested for)
 ** PARAMS: childElementName (the name of the child element that should be displayed)
 ****************************************************************************************
 -->
<#macro xdmChildStructure parentClassName childElementName expandFlagAliases>

    <#assign parentSchema = getXmlDataModelSchema(parentClassName)/>
    <#assign childElementClass = parentSchema.getElementType(childElementName)/>

    <@xdmStructure className=childElementClass.name heading="&lt;${childElementName}&gt;" expandFlagAliases=expandFlagAliases/>
</#macro>

<!--
 ****************************************************************************************
 ** MACRO: xdmStructure
 ** PARAMS: className (the name of the class the structure is being requested for)
 ** PARAMS: heading (the heading to display above the description of the class)
 ****************************************************************************************
 -->
<#macro xdmStructure className heading expandFlagAliases>
<div class="textbox">

    <#assign schema = getXmlDataModelSchema(className)/>
    <#if expandFlagAliases = 'yes'>
        <#assign settableAttributesDetail = schema.getSettableAttributesDetail(true)/>
    <#else>
        <#assign settableAttributesDetail = schema.getSettableAttributesDetail(false)/>
    </#if>
    <#assign childElements = schema.getNestedElementsDetail()/>
    <#assign classSuffix="odd"/>

    <b>${heading}</b> (<@classReference className = schema.bean.name/>)<br>
    ${schema.description}

    <table class="report" width=100% border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-heading">Element</td>
            <td class="report-column-heading">Type</td>
            <td class="report-column-heading">Description</td>
            <td class="report-column-heading">Choices</td>
        </tr>

    <#list settableAttributesDetail as attrDetail>
        <tr>
            <td class="report-column-${classSuffix}">
                <#if attrDetail.isRequired()>
                    <b>${attrDetail.attrName}</b>
                <#else>
                    ${attrDetail.attrName}
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                <#if attrDetail.isFlagAlias()>
                    <span title="alias for ${attrDetail.primaryFlagsAttrName}='${attrDetail.flagAlias.name}' (${attrDetail.attrType})">boolean (flag alias)</title>
                <#elseif attrDetail.attrType.isPrimitive()>
                    ${attrDetail.attrType.name}
                <#else>
                    <@classReference className = attrDetail.attrType.name/>
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                ${attrDetail.description}
            </td>
            <td class="report-column-${classSuffix}">
                <#if attrDetail.hasChoices()>
                    ${attrDetail.choices}
                <#else>
                    &nbsp;
                </#if>
            </td>
        </tr>
        <#if classSuffix = 'odd'>
            <#assign classSuffix='even'/>
        <#else>
            <#assign classSuffix='odd'/>
        </#if>
    </#list>

    <#list childElements as childDetail>
        <tr>
            <td class="report-column-${classSuffix}">
                <#if childDetail.isRequired()>
                    &lt;<b>${childDetail.elemName}</b>&gt;
                <#else>
                    &lt;${childDetail.elemName}&gt;
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                <@classReference className = childDetail.elemType.name/>
            </td>
            <td class="report-column-${classSuffix}">
                ${childDetail.description}
            </td>
            <td class="report-column-${classSuffix}">
                &nbsp;
            </td>
        </tr>
        <#if classSuffix = 'odd'>
            <#assign classSuffix='even'/>
        <#else>
            <#assign classSuffix='odd'/>
        </#if>
    </#list>

    </table>
</div>
</#macro>

<#macro classReference className>

    <#assign class = getClassForName(className)/>
    <#assign packageName = class.package.name/>
    <#assign className = class.name?replace('$', '.')/>

    <#if packageName?starts_with('java.lang')>
        <span title="${className}">${className[(packageName?length + 1) .. (class.name?length - 1)]}</a>
    <#else>
        <a href="${className}" title="${className}">${className[(packageName?length + 1) .. (class.name?length - 1)]}</a>
    </#if>

</#macro>
