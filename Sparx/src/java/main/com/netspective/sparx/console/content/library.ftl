
<#function reportRowClassSuffix row='odd'>
    <#if row = 'odd'>
        <#return 'even'/>
    <#else>
        <#return 'odd'/>
    </#if>
</#function>

<#comment>
 ******************************************************************************
 ** MACRO: projectFile
 ** PARAMS: none
 ******************************************************************************
</#comment>
<#macro projectFile>
    <code>${vc.getConsoleFileBrowserLink(vc.projectComponent.inputSource.identifier, true)}</code>
</#macro>

<#macro webXmlFile>
    <code>${vc.getConsoleFileBrowserLink(vc.servlet.servletContext.getRealPath('/WEB-INF/web.xml'), true)}</code>
</#macro>

<#comment>
 ******************************************************************************
 ** MACRO: childrenSummaries
 ** PARAMS: sourcePageType ('parent' | 'active')
 ******************************************************************************
 </#comment>
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

<#comment>
 ****************************************************************************************
 ** MACRO: templateProducerInstances
 ** PARAMS: templateProducer
 ****************************************************************************************
 </#comment>
<#macro templateProducerInstances templateProducer consumerTag consumerTagNameAttr="type" caption="Type" detail="-" detailUrl="documentation?type-name=" noDetailMessage="-">
<div class="textbox">
    <#assign instancesMap = templateProducer.instancesMap/>
    <#if detail != '-'>
        <#assign template = instancesMap.get(detail)/>
        <#assign className = template.alternateClassName/>
        <@xdmStructure className=className heading="Documentation for &lt;${consumerTag} ${consumerTagNameAttr}='${detail}'&gt;" expandFlagAliases='yes' inputSourceLocator=template.inputSourceLocator/>
    <#else>
        <#if noDetailMessage != '-'>
            ${noDetailMessage}
        <#else>
            <@reportTable>
            <#assign classSuffix="odd"/>
                <tr>
                    <td class="report-column-heading">${caption}</td>
                    <td class="report-column-heading">Class</td>
                    <td class="report-column-heading">Description</td>
                </tr>
            <#list instancesMap.keySet().iterator() as typeName>
                <tr>
                    <td class="report-column-${classSuffix}">
                        <a href="${detailUrl}${typeName}"><b>${typeName}</b></a>
                    </td>
                    <td class="report-column-${classSuffix}">
                        <@classReference className=instancesMap.get(typeName).alternateClassName/>
                    </td>
                    <td class="report-column-${classSuffix}">
                        <@classDescription className=instancesMap.get(typeName).alternateClassName/>
                    </td>
                </tr>
                <#if classSuffix = 'odd'>
                    <#assign classSuffix='even'/>
                <#else>
                    <#assign classSuffix='odd'/>
                </#if>
            </#list>
            </@reportTable>
        </#if>
    </#if>
</div>
</#macro>

<#comment>
 ****************************************************************************************
 ** MACRO: xdmChildStructure
 ** PARAMS: parentClassName (the name of the class the structure is being requested for)
 ** PARAMS: childElementName (the name of the child element that should be displayed)
 ****************************************************************************************
 </#comment>
<#macro xdmChildStructure parentClassName childElementName expandFlagAliases>

    <#assign parentSchema = getXmlDataModelSchema(parentClassName)/>
    <#assign childElementClass = parentSchema.getElementType(childElementName)/>

    <@xdmStructure className=childElementClass.name heading="&lt;${childElementName}&gt;" expandFlagAliases=expandFlagAliases/>
</#macro>

<#comment>
 ****************************************************************************************
 ** MACRO: xdmPath
 ** PARAMS: parentTags (the '/' separated names of tags that lead to a XDM child)
 ** PARAMS: parentXdmClasses (the '/' separated names of classes that lead to a XDM child)
 ****************************************************************************************
 </#comment>
 <#macro xdmPath tags="" classes="" separator="<code> / </code>">
    <#if tags != "">
        <#assign tagsList=tags?split('/')/>
        <#assign classesList=classes?split('/')/>

        <code>${vc.projectComponent.inputSource.file.name}</code>

        <#assign activeTags = ""/>
        <#assign activeClasses = ""/>

        <#comment> start from 1 because of the leading / </#comment>
        <#list 1..tagsList?size-1 as index>
            <#local url = "${vc.servletRootUrl}/reference/tags?parent-tags=${activeTags}&parent-xdm-classes=${activeClasses}&xdm-tag=${tagsList[index]}&xdm-class=${classesList[index]}"/>
            ${separator}<code>&lt;<a href="${vc.constructAppUrl(url)}">${tagsList[index]}</a>&gt;</code>
            <#assign activeTags = "${activeTags}/${tagsList[index]}"/>
            <#assign activeClasses = "${activeClasses}/${classesList[index]}"/>
        </#list>
    </#if>
 </#macro>

<#comment>
 ****************************************************************************************
 ** MACRO: xdmStructure
 ** PARAMS: className (the name of the class the structure is being requested for)
 ** PARAMS: heading (the heading to display above the description of the class)
 ****************************************************************************************
 </#comment>
<#macro xdmStructure className tag="" heading="" expandFlagAliases="yes" parentTags="" parentXdmClasses="" inputSourceLocator="-">
<div class="textbox">

    <#assign schema = getXmlDataModelSchema(className)/>
    <#if expandFlagAliases = 'yes'>
        <#assign settableAttributesDetail = schema.getSettableAttributesDetail(true)/>
    <#else>
        <#assign settableAttributesDetail = schema.getSettableAttributesDetail(false)/>
    </#if>
    <#assign childElements = schema.getNestedElementsDetail()/>
    <#assign classSuffix="odd"/>

    <table width=100%>
        <tr>
            <td colspan=2><@xdmPath tags=parentTags classes=parentXdmClasses/></td>
        </tr>
        <tr valign=center>
            <td>
                <img src="${vc.activeTheme.getResourceUrl('/images/xml/xml.gif')}"/>
                <#if tag != ''>
                &lt;<b>${tag}</b>&gt;
                <#else>
                <b>${heading}</b>
                </#if>
            </td>
            <td align=right>
                <img src="${vc.activeTheme.getResourceUrl('/images/java-class.gif')}"/> <code>${schema.bean.name}</code>
            </td>
        </tr>
        <tr class="report-column-even">
            <td colspan=2>${schema.javaDoc.description?default('&nbsp;')}</td>
        </tr>
    </table>
    <#if inputSourceLocator != '-'>
        <p>
        Source: <code>${vc.getConsoleFileBrowserLink(inputSourceLocator.inputSourceTracker.identifier, true)} ${inputSourceLocator.lineNumbersText}</code>
    </#if>

    <#assign templateCatalog = vc.projectComponent.templateCatalog/>

    <#if schema.isTemplateConsumer()>
        <#assign consumerData = templateCatalog.getTemplateConsumerDataForClass(schema.bean)?default('-')/>
        <#if consumerData != '-'>
            <#assign templates = consumerData.getTemplates(templateCatalog)?default('-')/>
            <#if templates != '-'>
            <p>
            <b>Templates that may be applied to this tag</b>
            <ol>
                <#list templates.entrySet() as templateEntry>
                    <#local url = "${vc.servletRootUrl}/reference/templates?ns=${templateEntry.value.templateProducer.nameSpaceId}&tmpl=${templateEntry.key}"/>
                    <li>${consumerData.templateRefAttrName}="<a href="${vc.constructAppUrl(url)}"><b>${templateEntry.key}</b></a>"</li>
                </#list>
            </ol>
            </#if>
        </#if>
    </#if>

    <p>
    <b>Attributes</b><br>
    <table class="report" border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-heading" title="Attribute type (hover in row to see type)">T</td>
            <td class="report-column-heading" title="Declared in (hover in row to see where attribute was declared)">D</td>
            <td class="report-column-heading">Name</td>
            <td class="report-column-heading">Type</td>
            <td class="report-column-heading">Choices</td>
        </tr>

    <#list settableAttributesDetail as attrDetail>
        <tr>
            <td class="report-column-${classSuffix}" rowspan=2>
                <#if attrDetail.isRequired()>
                    <img src="${vc.activeTheme.getResourceUrl("/images/xml/xml-node-attribute-required.gif")}" title="Required attribute"/>
                <#else>
                    <img src="${vc.activeTheme.getResourceUrl("/images/xml/xml-node-attribute.gif")}" title="Attribute"/>
                </#if>
            </td>
            <td class="report-column-${classSuffix}" rowspan=2>
                <#if attrDetail.isInherited()>
                    <img src="${vc.activeTheme.getResourceUrl("/images/checkmark-empty-sm.gif")}" title="Declared in ${attrDetail.declaringClass.name}"/>
                <#else>
                    <img src="${vc.activeTheme.getResourceUrl("/images/checkmark-sm.gif")}" title="Declared in this class (${attrDetail.declaringClass.name})"/>
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                <nobr>
                <#if attrDetail.isRequired()>
                    <b>${attrDetail.attrName}</b>
                <#else>
                    ${attrDetail.attrName}
                </#if>
                </nobr>
            </td>
            <td class="report-column-${classSuffix}">
                <#if attrDetail.isFlagAlias()>
                    <span title="alias for ${attrDetail.primaryFlagsAttrName}='${attrDetail.getFlagAlias().name}' (${attrDetail.attrType})">boolean (flag alias)</title>
                <#elseif attrDetail.attrType.isPrimitive()>
                    ${attrDetail.attrType.name}
                <#else>
                    <@classReference className = attrDetail.attrType.name/>
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                <#if attrDetail.hasChoices()>
                    ${attrDetail.choices}
                <#else>
                    &nbsp;
                </#if>
            </td>
        </tr>
            <td class="report-column-${classSuffix}" colspan=4>
                <font color=#999999>${attrDetail.javaDoc.description?default('&nbsp;')}</font>
            </td>
        <tr>
        </tr>
        <#if classSuffix = 'odd'>
            <#assign classSuffix='even'/>
        <#else>
            <#assign classSuffix='odd'/>
        </#if>
    </#list>
    </table>

    <p>
    <b>Child Elements</b><br>
    <table class="report" border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-heading" title="Element type (hover in row to see type)">T</td>
            <td class="report-column-heading" title="Declared in (hover in row to see where element was declared)">D</td>
            <td class="report-column-heading">Name</td>
            <td class="report-column-heading">Class</td>
            <td class="report-column-heading" title="Supports characters (PCDATA) within element">Text</td>
            <td class="report-column-heading">Template</td>
        </tr>

    <#list childElements as childDetail>
        <tr>
            <td class="report-column-${classSuffix}" rowspan=2>
                <#if childDetail.isTemplateProducer()>
                    <img src="${vc.activeTheme.getResourceUrl("/images/xml/xml-node-template-producer.gif")}" title="Template Producer"/>
                <#else>
                    <img src="${vc.activeTheme.getResourceUrl("/images/xml/xml-node-element.gif")}" title="Element"/>
                </#if>
            </td>
            <td class="report-column-${classSuffix}" rowspan=2>
                <#if childDetail.isInherited()>
                    <img src="${vc.activeTheme.getResourceUrl("/images/checkmark-empty-sm.gif")}" title="Declared in ${childDetail.declaringClass.name}"/>
                <#else>
                    <img src="${vc.activeTheme.getResourceUrl("/images/checkmark-sm.gif")}" title="Declared in this class (${childDetail.declaringClass.name})"/>
                </#if>
            </td>
            <td class="report-column-${classSuffix}">
                <nobr>
                <#if childDetail.isTemplateProducer()>
                    <#local url = "${vc.servletRootUrl}/reference/templates?ns=${childDetail.getTemplateProducer().nameSpaceId}"/>
                <#else>
                    <#local href="xdm-tag=${childDetail.elemName}&xdm-class=${childDetail.elemType.name}"/>
                    <#local url="${vc.servletRootUrl}/reference/tags?parent-tags=${parentTags}/${tag}&parent-xdm-classes=${parentXdmClasses}/${className}&${href}"/>
                </#if>
                <a href="${vc.constructAppUrl(url)}">
                <#if childDetail.isRequired()>
                    &lt;<b>${childDetail.elemName}</b>&gt;
                <#else>
                    &lt;${childDetail.elemName}&gt;
                </#if>
                </a>
                </nobr>
            </td>
            <td class="report-column-${classSuffix}">
                <@classReference className = childDetail.elemType.name/>
            </td>
            <td class="report-column-${classSuffix}">
                <#if getXmlDataModelSchema(childDetail.elemType.name).supportsCharacters()>Yes<#else>&nbsp;</#if>
            </td>
            <td class="report-column-${classSuffix}">
                <#if childDetail.isTemplateConsumer()>
                    <#assign consumerData = templateCatalog.getTemplateConsumerDataForClass(childDetail.elemType)?default('-')/>
                    <#if consumerData != '-'>
                        <#assign templates = consumerData.getTemplates(templateCatalog)?default('-')/>
                        <#if templates != '-'>
                        <select onChange="document.location = this.options[this.selectedIndex].value">
                            <option>${templates.size()} templates</option>
                            <#list templates.entrySet() as templateEntry>
                                <option value="${vc.servletRootUrl}/reference/templates?ns=${templateEntry.value.templateProducer.nameSpaceId}&tmpl=${templateEntry.key}">${consumerData.templateRefAttrName}="${templateEntry.key}"</option>
                            </#list>
                        </select>
                        <#else>
                            No templates defined.
                        </#if>
                    <#else>
                        &nbsp;
                    </#if>
                <#else>
                    &nbsp;
                </#if>
                <#if childDetail.isTemplateProducer()>
                    <#assign producer = childDetail.getTemplateProducer()/>
                    <#assign templates = templateCatalog.getProducerTemplates(producer)/>
                    <select onChange="document.location = this.options[this.selectedIndex].value">
                        <option>${templates.size()} templates</option>
                        <#list templates.values() as template>
                            <option value="${vc.servletRootUrl}/reference/templates?ns=${producer.nameSpaceId}&tmpl=${template.templateName}">${template.templateName}</option>
                        </#list>
                    </select>
                <#else>
                    &nbsp;
                </#if>
            </td>
        </tr>
        <tr>
            <td class="report-column-${classSuffix}" colspan=5>
                <font color=#999999>${childDetail.javaDoc.description?default('&nbsp;')}</font>
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
    <#if className?exists>
        ${getClassReference(className)}
    </#if>
</#macro>

<#function getClassReference className>

    <#assign class = getClassForName(className)/>
    <#assign packageName = class.package.name/>
    <#assign classNameNoInner = class.name?replace('$', '.')/>
    <#assign classNameShort = classNameNoInner[(packageName?length + 1) .. (class.name?length - 1)]/>

    <#if packageName?starts_with('java.lang')>
        <#return "<span title='${className}'>${classNameShort}</span>"/>
    <#else>
        <#assign classJavaSourceFile = getClassSourceForName(class.name)/>
        <#if classJavaSourceFile = ''>
            <#return "<span title='${class.name}'>${classNameShort}</span>"/>
        <#else>
            <#if classJavaSourceFile.absolutePath?exists>
                <#assign servletRootPath = vc.servlet.servletContext.getRealPath('')/>
                <#if classJavaSourceFile.absolutePath.startsWith(servletRootPath)>
                    <#assign relativePath = classJavaSourceFile.absolutePath.substring(servletRootPath.length())/>
                    <#assign relativePathProperDelims = relativePath?replace('\\', '/')/>
                    <#return "<a href='${vc.servletRootUrl}/project/files/${relativePathProperDelims}' title='${class.name} (${classJavaSourceFile})'>${classNameShort}</a>"/>
                <#else>
                    <#return "<span title='${class.name} (${classJavaSourceFile})'>${classNameShort}</span>"/>
                </#if>
            <#else>
                <#return "<span title='${classJavaSourceFile}'>${classNameShort}</span>">
            </#if>
        </#if>
    </#if>

</#function>


<#macro contentImage image="">
    <#assign navigationContext = vc.navigationContext?default(vc)/>
    <#assign activePage = navigationContext.activePage/>
    <img src='${navigationContext.getSparxResourceUrl("/content/console" + activePage.qualifiedName + "/" + image?default(activePage.name + '.gif'))}'>
</#macro>


<#macro classDescription className>

    <#assign schema = getXmlDataModelSchema(className)/>
    ${schema.javaDoc.descriptionLead?default('')}

</#macro>

<#macro reportTable width="100%" headings=[] data=[] columnAttrs=[] headingAttrs=[] dataMayContainsHtmlCellAttrs="yes">
    <table class="report" border="0" cellspacing="2" cellpadding="0" width="${width}">
    <#if data?size = 0>
    <#nested>
    <#else>
        <#assign _headingAttrs = headingAttrs/>
        <#assign _columnAttrs = columnAttrs/>

        <#if _headingAttrs?size = 0>
            <#list headings as heading>
                <#assign _headingAttrs = _headingAttrs + [""]/>
            </#list>
        </#if>

        <#if _columnAttrs?size = 0>
            <#list data[0] as column>
                <#assign _columnAttrs = _columnAttrs + [""]/>
            </#list>
        </#if>

        <#if headings?size gt 0>
        <tr>
            <#list headings as heading>
                <td class="report-column-heading" ${_headingAttrs[heading_index]}>${heading}</td>
            </#list>
        </tr>
        </#if>
        <#assign classSuffix="odd"/>
        <#list data as row>
            <tr>
            <#list row as column>
                <#assign isHtmlCellAttrs = dataMayContainsHtmlCellAttrs="yes" && column?is_sequence />
                <td class="report-column-${classSuffix}" ${_columnAttrs[column_index]}
                    <#if isHtmlCellAttrs>${column[0]}</#if>  >
                    <#if isHtmlCellAttrs>
                        ${column[1]}
                    <#else>
                        ${column}
                    </#if>
                </td>
            </#list>
            <#if classSuffix = 'odd'>
                <#assign classSuffix='even'/>
            <#else>
                <#assign classSuffix='odd'/>
            </#if>
            </tr>
        </#list>
    </#if>
    </table>
</#macro>

<#macro inspectObject object heading="Object Inspector">
    <#assign schema = getXmlDataModelSchema(object.class.name)/>
    <#assign settableAttributesDetail = schema.getSettableAttributesDetail(false)/>
    <#assign childElements = schema.getNestedElementsDetail()/>

    <#assign attribs = []/>
    <#list settableAttributesDetail as attrDetail>
        <#if attrDetail.attrName = 'class'>
            <#assign attribs = attribs + [
                        [attrDetail.attrName, getClassReference(object.class.name), getClassReference(attrDetail.attrType.name)]
                        ]/>
        <#else>
            <#assign accessor = attrDetail.accessor?default('-')/>
            <#if accessor != '-'>
                <#assign attrValue = attrDetail.getAccessorValue(object, 'NULL')?html/>
            <#else>
                <#assign attrValue = '<i><font color=red>no accessor available</font></i>'/>
            </#if>
            <#assign attribs = attribs + [
                        [attrDetail.attrName, attrValue, getClassReference(attrDetail.attrType.name)]
                        ]/>
         </#if>
    </#list>

    <@panel heading="${heading}">
        <#if object.inputSourceLocator?exists>
            <div class="textbox">
            Source: <code>${vc.getConsoleFileBrowserLink(object.inputSourceLocator.inputSourceTracker.identifier, true)} ${object.inputSourceLocator.lineNumbersText}</code>
            </div>
        </#if>
        <@reportTable
                headings = ["Property", "Value", "Type"]
                data=attribs
                dataMayContainsHtmlCellAttrs="no"
                />
    </@panel>
</#macro>

<#macro objectXmlLocator object>
    <#if object.inputSourceLocator?exists>
        <#assign isl = object.inputSourceLocator/>
        ${vc.getConsoleFileBrowserLink(isl.inputSourceTracker.identifier, true)} ${isl.lineNumbersText}
    <#else>
        Unable to get input source locator for ${object.class.name};
    </#if>
</#macro>

<#macro objectXmlSource object>
    <#if object.inputSourceLocator?exists>
        <#assign isl = object.inputSourceLocator/>
        Source: <code>${vc.getConsoleFileBrowserLink(isl.inputSourceTracker.identifier, true)} ${isl.lineNumbersText}</code>
        <@xmlCode>
            ${statics["com.netspective.commons.text.TextUtils"].getUnindentedText(isl.sourceText?default(isl.toString() + " unavailable"))}
        </@xmlCode>
    <#else>
        Unable to get input source locator for ${object.class.name};
    </#if>
</#macro>