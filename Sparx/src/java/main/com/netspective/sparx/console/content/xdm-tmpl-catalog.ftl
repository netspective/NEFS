<#include "*/library.ftl"/>

<div class="textbox">
    <#assign templateCatalog = vc.projectComponent.templateCatalog/>
    <#assign templatesMap = templateCatalog.templatesByNameSpace/>

    <#if vc.request.getParameter('ns')?exists>
        <#assign nameSpaceId = vc.request.getParameter('ns')/>
        <#assign templates = templatesMap.get(nameSpaceId)/>

        <#if vc.request.getParameter('tmpl')?exists>
            <#assign templateName = vc.request.getParameter('tmpl')/>
            <#assign template = templates.get(templateName)/>
            <#assign producer = template.templateProducer/>

            ${vc.navigationContext.setPageHeading("Template '" + templateName + "' in " + nameSpaceId + " Namespace")}
            <#assign preport = [
                    ["Tag Name", "<code>&lt;" + producer.elementName + "&gt;</code>"],
                    ["Template Name Tag Attr", "<code>" + producer.templateNameAttrName?default('&nbsp;') + "</code>"],
                    ["Extends Template Tag Attr", "<code>" + producer.templateInhAttrName?default('&nbsp;') + "</code>"],
                    ["Source Location", "<code>${vc.getConsoleFileBrowserLink(template.inputSourceLocator.inputSourceTracker.identifier, true)} line ${template.inputSourceLocator.lineNumber}</code>"]
               ]/>

            <@panel heading="Template Producer">
                <@reportTable headings=["Attribute", "Value"] data=preport columnAttrs=["", ""]/>
            </@panel>

            <p>
            <@panel heading="Template Source">
            <code>
            <@getTemplateElementSource templateElement=template level=1/>
            </code>
            </@panel>

            <#if template.alternateClassName?exists>
                <p>
                <@panel heading="Template Tag Documentation">
                    <@xdmStructure className=template.alternateClassName/>
                </@panel>
            </#if>
        <#else>
            ${vc.navigationContext.setPageHeading(nameSpaceId + " Templates")}
            <#assign report = []/>
            <#assign first = true/>
            <#list templateCatalog.getTemplateNames(nameSpaceId) as templateName>
                <#assign template = templates.get(templateName)/>

                <#if first>
                    <!-- all templates should have the same producer so we just show it once -->
                    <@showProducerDetails producer=template.templateProducer/><p>
                    <#assign first = false/>
                </#if>

                <#if template.alternateClassName?exists>
                    <#assign className = getClassReference(template.alternateClassName)/>
                <#else>
                    <#assign className = "&nbsp;"/>
                </#if>
                <#assign report = report + [[ "<a href='?ns=${nameSpaceId}&tmpl=${template.templateName}'>${template.templateName}</a>", className, "<code>${vc.getConsoleFileBrowserLink(template.inputSourceLocator.inputSourceTracker.identifier, true)} line ${template.inputSourceLocator.lineNumber}</code>"]]/>
            </#list>

            <@panel heading="Templates">
                <@reportTable headings=["Template", "Class", "Source"] data=report columnAttrs=["", "", ""]/>
            </@panel>
        </#if>
    <#else>
        <#assign report = []/>
        <#list templateCatalog.templateNameSpaceIds as nameSpaceId>
            <#assign templates = templatesMap.get(nameSpaceId)/>
            <#assign firstTemplate = templates.values().iterator().next()/>
            <#assign report = report + [
                [ "<a href='?ns=${nameSpaceId}'>${nameSpaceId}</a>",
                  "<code>&lt;${firstTemplate.templateProducer.elementName}&gt;</code>",
                  templates.size()]]/>
        </#list>

        <@panel heading="All Template Namespaces">
            <@reportTable headings=["Namespace", "Tag", "Templates"] data=report columnAttrs=["", "", "align=right"]/>
        </@panel>
    </#if>
</div>

<#macro showProducerDetails producer>
    <#assign preport = [
            ["Tag Name", "<code>&lt;" + producer.elementName + "&gt;</code>"],
            ["Template Name Tag Attr", "<code>" + producer.templateNameAttrName?default('&nbsp;') + "</code>"],
            ["Extends Template Tag Attr", "<code>" + producer.templateInhAttrName?default('&nbsp;') + "</code>"],
            ["Templates", producer.instances.size()]
       ]/>

    <@panel heading="Template Producer">
        <@reportTable headings=["Attribute", "Value"] data=preport columnAttrs=["", ""]/>
    </@panel>
</#macro>

<#macro getTemplateElementSource templateElement level=0>
    <#if level gt 0>
        <br><#list 0..level as index>&nbsp;&nbsp;</#list>
    </#if>
    <#if templateElement.elementName?exists>
        &lt;<b><font color="navy">${templateElement.elementName}</font></b>
        <#assign attrs = templateElement.attributes/>
        <#if attrs.length gt 0>
            <#list 0..attrs.length-1 as index>
                <#assign attrName = attrs.getQName(index)/>
                <#assign attrValue = attrs.getValue(index)/>
                <font color="blue">${attrName}</font>="<font color="green">${attrValue}</font>"
            </#list>
        </#if>
        <#if templateElement.children.size() = 0>
            /&gt;
        <#else>
            &gt;
            <#list templateElement.children as child>
                <#assign nextLevel = level+1/>
                <@getTemplateElementSource templateElement=child level=nextLevel/>
            </#list>
            <br>
            <#if level gt 0>
                <#list 0..level as index>&nbsp;&nbsp;</#list>
            </#if>
            &lt;/<b><font color="navy">${templateElement.elementName}</font></b>&gt;<br>
        </#if>
    <#else>
        ${templateElement.text}
    </#if>
</#macro>
