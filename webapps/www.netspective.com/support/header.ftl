<#if activePageId = "/products/frameworks/sparx">
    <#assign boxImage="products/frameworks/box-sparx.gif"/>
<#elseif activePageId = "/products/frameworks/axiom">
    <#assign boxImage="products/frameworks/box-axiom.gif"/>
<#elseif activePageId = "/products/frameworks/commons">
    <#assign boxImage="products/frameworks/box-commons.gif"/>
<#else>
    <#assign boxImage="products/frameworks/suite/box-nefs.jpg"/>
</#if>

<table width="600" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td><img src="${resourcesPath + '/images/support/main.gif'}" alt="" /></td>
        <td><img src="${resourcesPath + '/images/support/heading.gif'}" width=348 height=104 alt=""/></td>
    </tr>
</table>

<@pageBodyBegin/>