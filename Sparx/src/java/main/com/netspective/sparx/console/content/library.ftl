
<#function reportRowClassSuffix row='odd'>
    <#if row = 'odd'>
        <#return 'even'/>
    <#else>
        <#return 'odd'/>
    </#if>
</#function>

<!--
 ******************************************************************************
 ** MACRO: projectFile
 ** PARAMS: none
 ******************************************************************************
-->
<#macro projectFile>
    <code>${vc.projectComponent.inputSource.identifier}</code>
</#macro>

<!--
 ******************************************************************************
 ** MACRO: childrenSummaries
 ** PARAMS: sourcePageType ('parent' | 'active')
 ******************************************************************************
 -->
<#macro childrenSummaries sourcePageType>

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
                <#assign summaryTemplateAltName = "/content${child.qualifiedName}.ftl"/>
                <#assign summary>
                    <#if templateExists(summaryTemplateName)>
                        <#include summaryTemplateName/>
                    <#elseif templateExists(summaryTemplateAltName)>
                        <#include summaryTemplateAltName/>
                    <#else>
                        Neither <font color=red>${summaryTemplateName}</font> nor <font color=red>${summaryTemplateAltName}</font> are available.
                    </#if>
                </#assign>
                <#assign sentenceEnd = summary?index_of('. ')/>
                <#if sentenceEnd != -1>
                    ${summary[0 .. sentenceEnd]}
                <#else>
                    ${summary}
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
<#macro xdmStructure className tag="" heading="" expandFlagAliases="yes">
<div class="textbox">

    <#assign schema = getXmlDataModelSchema(className)/>
    <#if expandFlagAliases = 'yes'>
        <#assign settableAttributesDetail = schema.getSettableAttributesDetail(true)/>
    <#else>
        <#assign settableAttributesDetail = schema.getSettableAttributesDetail(false)/>
    </#if>
    <#assign childElements = schema.getNestedElementsDetail()/>
    <#assign imageSrc=vc.getThemeResourcesRootUrl(vc.activeTheme) + "/images" />
    <#assign xmlImageSrc=imageSrc + "/xml" />
    <#assign classSuffix="odd"/>

    <table width=100%>
        <tr valign=center>
            <td>
                <img src="${xmlImageSrc}/xml.gif"/>
                <#if tag != ''>
                &lt;<b>${tag}</b>&gt;
                <#else>
                <b>${heading}</b>
                </#if>
            </td>
            <td align=right>
                <img src="${imageSrc}/java-class.gif"/> <code>${schema.bean.name}</code>
            </td>
        </tr>
        <tr class="report-column-even">
            <td colspan=2>${schema.description}</td>
        </tr>
    </table>

    <p>
    <table class="report" border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-heading">Node</td>
            <td class="report-column-heading">Type</td>
            <td class="report-column-heading">Description</td>
            <td class="report-column-heading">Choices</td>
        </tr>

    <#list settableAttributesDetail as attrDetail>
        <tr>
            <td class="report-column-${classSuffix}">
                <nobr>
                <#if attrDetail.isRequired()>
                    <img src="${xmlImageSrc}/xml-node-attribute-required.gif" title="Required attribute"/>
                <#else>
                    <img src="${xmlImageSrc}/xml-node-attribute.gif" title="Attribute"/>
                </#if>
                <#if attrDetail.isRequired()>
                    <b>${attrDetail.attrName}</b>
                <#else>
                    ${attrDetail.attrName}
                </#if>
                </nobr>
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
                <nobr>
                <#if childDetail.isTemplateProducer()>
                    <img src="${xmlImageSrc}/xml-node-template-producer.gif" title="Template Producer"/>
                <#else>
                    <img src="${xmlImageSrc}/xml-node-element.gif" title="Element"/>
                </#if>
                <#if childDetail.isRequired()>
                    &lt;<b>${childDetail.elemName}</b>&gt;
                <#else>
                    &lt;${childDetail.elemName}&gt;
                </#if>
                </nobr>
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
    <#assign classNameNoInner = class.name?replace('$', '.')/>

    <#if packageName?starts_with('java.lang')>
        <span title="${className}">${classNameNoInner[(packageName?length + 1) .. (class.name?length - 1)]}</span>
    <#else>
        <!-- need to HREF to something? -->
        <span title="${className}">${classNameNoInner[(packageName?length + 1) .. (class.name?length - 1)]}</span>
    </#if>

</#macro>


<#macro contentImage image="">
    <#assign navigationContext = vc.navigationContext?default(vc)/>
    <#assign activePage = navigationContext.activePage/>
    <#assign imagePath=vc.resourcesRootUrl + "/content/console/" + activePage.qualifiedName />
    <img src='${imagePath}/${image?default(activePage.name + '.gif')}'>
</#macro>

<#macro classDescription className>

    <#assign schema = getXmlDataModelSchema(className)/>
    ${schema.description}

</#macro>

<#macro reportTable>
    <table class="report" border="0" cellspacing="2" cellpadding="0">

        <#nested>

    </table>
</#macro>