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
 ******************************************************************************
 ** MACRO: xdmStructure
 ** PARAMS: class (the name of the class the structure is being requested for)
 ******************************************************************************
 -->
<#macro xdmStructure class>
<div class="textbox">

    <#assign schema = getXmlDataModelSchema(class)/>
    <#assign settableAttributesWithFlagsExpanded = schema.settableAttributesWithFlagsExpanded/>
    <#assign classSuffix="odd"/>

    <table class="report" width=100% border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-heading">Element</td>
            <td class="report-column-heading">Type</td>
            <td class="report-column-heading">Choices</td>
        </tr>

    <#list settableAttributesWithFlagsExpanded as attrDetail>
        <tr>
            <td class="report-column-${classSuffix}">
                ${attrDetail.attrName}
            </td>
            <td class="report-column-${classSuffix}">
                <#if attrDetail.isFlagAlias()>
                    <span title="alias for ${attrDetail.primaryFlagsAttrName}='${attrDetail.flagAlias.name}' (${attrDetail.attrType})">boolean (flag alias)</title>
                <#else>
                    ${attrDetail.attrType.name}
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                <#if attrDetail.hasChoices()>
                    ${attrDetail.choices}
                <#else>
                    &nbsp;
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                ${attrDetail.description}
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