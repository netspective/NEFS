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
        <td><img src="${resourcesPath + '/images/' + boxImage}" width=252 height=166 alt="" /></td>
        <td><table width="348" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td><img src="${resourcesPath + '/images/products/frameworks/name-nefs.gif'}" width=348 height=104 alt=""/></td>
                </tr>
                <tr>
                    <td><table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td><a onmouseover="changeImages( /*CMP*/'presentation',/*URL*/'${resourcesPath}/images/products/frameworks/presentation-over.gif');return true" onmouseout="changeImages( /*CMP*/'presentation',/*URL*/'${resourcesPath}/images/products/frameworks/presentation.gif');return true" href="${servletPath}/products/frameworks/sparx"><img src="${resourcesPath}/images/products/frameworks/presentation.gif" alt="" name="presentation" height="24" width="82" border="0"></a></td>
                                <td><a onmouseover="changeImages( /*CMP*/'database',/*URL*/'${resourcesPath}/images/products/frameworks/database-over.gif');return true" onmouseout="changeImages( /*CMP*/'database',/*URL*/'${resourcesPath}/images/products/frameworks/database.gif');return true" href="${servletPath}/products/frameworks/axiom"><img src="${resourcesPath}/images/products/frameworks/database.gif" alt="" name="database" height="24" width="62" border="0"></a></td>
                                <td><a onmouseover="changeImages( /*CMP*/'security',/*URL*/'${resourcesPath}/images/products/frameworks/security-over.gif');return true" onmouseout="changeImages( /*CMP*/'security',/*URL*/'${resourcesPath}/images/products/frameworks/security.gif');return true" href="${servletPath}/products/frameworks/commons"><img src="${resourcesPath}/images/products/frameworks/security.gif" alt="" name="security" height="24" width="59" border="0"></a></td>
                                <td><a onmouseover="changeImages( /*CMP*/'online',/*URL*/'${resourcesPath}/images/products/frameworks/online-over.gif');return true" onmouseout="changeImages( /*CMP*/'online',/*URL*/'${resourcesPath}/images/products/frameworks/online.gif');return true" href="http://sampler.netspective.com" target="sampler.netspective.com"><img src="${resourcesPath}/images/products/frameworks/online.gif" alt="" name="online" height="24" width="74" border="0"></a></td>
                                <td><a onmouseover="changeImages( /*CMP*/'fact',/*URL*/'${resourcesPath}/images/products/frameworks/fact-over.gif');return true" onmouseout="changeImages( /*CMP*/'fact',/*URL*/'${resourcesPath}/images/products/frameworks/fact.gif');return true" href="#"><img src="${resourcesPath}/images/products/frameworks/fact.gif" alt="" name="fact" height="24" width="71" border="0"></a></td>
                            </tr>
                        </table></td>
                </tr>
                <tr>
                    <td><img src="${resourcesPath + '/images/products/frameworks/spacer-heading-bottom.gif'}" width=348 height=38 alt="" /></td>
                </tr>
            </table></td>
    </tr>
</table>

<@pageBodyBegin/>