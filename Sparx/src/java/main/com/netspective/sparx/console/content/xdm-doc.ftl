<#include "*/library.ftl"/>

<div class="textbox">
    <#assign class = getClassForName(xdmClass)/>
    <#assign packageName = class.package.name/>
    <#assign classNameNoInner = class.name?replace('$', '.')/>
    <#assign classNoPkg = classNameNoInner[(packageName?length + 1) .. (class.name?length - 1)]/>

    <@xdmStructure className=xdmClass tag=xdmTag?default(classNoPkg?lower_case) heading=xdmClassHeading?default('') expandFlagAliases=xdmExpandFlagAliases?default('yes')/>
</div>