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
                <a href="${parentName}/${child.name}">${child.getHeading(vc)}</a>
            </td>
            <td class="report-column-${classSuffix}">
                <#assign summaryTemplateName = "/content${child.qualifiedName}/summary.ftl"/>
                <#if templateExists(summaryTemplateName)>
                    <#include summaryTemplateName/>
                <#else>
                    ${summaryTemplateName} not available.
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
<#macro xdmChildStructure parentClassName childElementName>

    <#assign parentSchema = getXmlDataModelSchema(parentClassName)/>
    <#assign childElementClass = parentSchema.getElementType(childElementName)/>

    <@xdmStructure className=childElementClass.name heading="&lt;${childElementName}&gt;"/>
</#macro>

<!--
 ****************************************************************************************
 ** MACRO: xdmStructure
 ** PARAMS: className (the name of the class the structure is being requested for)
 ** PARAMS: heading (the heading to display above the description of the class)
 ****************************************************************************************
 -->
<#macro xdmStructure className heading>
<div class="textbox">

    <#assign schema = getXmlDataModelSchema(className)/>
    <#assign settableAttributesWithFlagsExpanded = schema.settableAttributesWithFlagsExpanded/>
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

    <#list settableAttributesWithFlagsExpanded as attrDetail>
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
