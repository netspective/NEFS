<#include "/content/library.ftl"/>

<!-- FTL designed for xdm-doc <panel-type> in console.xml; assumes xdm-class attribute will be set or defaults to project -->

<div class="textbox">
    <#if vc.request.getParameter('xdm-class')?default('-') != '-'>
        <#assign xdmClass = vc.request.getParameter('xdm-class')/>
        <#assign xdmTag = vc.request.getParameter('xdm-tag')?default('')/>
        <#assign xdmParentClasses = vc.request.getParameter('parent-xdm-classes')?default('')/>
        <#assign xdmParentTags = vc.request.getParameter('parent-tags')?default('')/>
    </#if>
    <#if xdmClass?default('-') = '-'>
        <#assign xdmClass = "com.netspective.sparx.Project"/>
        <#assign xdmTag = "project"/>
    </#if>

    <#assign class = getClassForName(xdmClass)/>
    <#assign packageName = class.package.name/>
    <#assign classNameNoInner = class.name?replace('$', '.')/>
    <#assign classNoPkg = classNameNoInner[(packageName?length + 1) .. (class.name?length - 1)]/>

    <@xdmStructure className=xdmClass tag=xdmTag?default(classNoPkg?lower_case) heading=xdmClassHeading?default('')
                   expandFlagAliases=xdmExpandFlagAliases?default('yes')
                   parentTags=xdmParentTags
                   parentXdmClasses=xdmParentClasses />
</div>