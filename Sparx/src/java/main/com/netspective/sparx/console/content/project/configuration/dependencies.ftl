<#include "*/library.ftl">

<#assign factory = statics["com.netspective.commons.product.NetspectiveComponent"].getInstance()/>
<#assign allLibDependencies = factory.netspective.allLibraryDependencies/>
<#assign catalog = []/>
<#list allLibDependencies as lib>
    <#assign catalog = catalog + [["<a href='${lib.source.homeUri}'>${lib.name}</a>", lib.version, lib.source.license?default("&nbsp;"), lib.classPath]]/>
</#list>

<@panel heading="Third-party Library Dependencies">
    <@reportTable headings=["Library", "Version", "License", "File"] data=catalog/>
</@panel>
