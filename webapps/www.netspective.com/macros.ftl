<#include "console/content/library.ftl"/>

<#macro childMenus parentPage level=2>

    <#list parentPage.childrenList as childPage>
    <tr>
        <td>
            <a class="menu" href="${childPage.getUrl(vc)}">
            <span class="L${level}">
                ${childPage.caption.getTextValue(vc)}
                <#if childPage = activePage> *</#if>
            </span>
            </a>
        </td>
    </tr>
    <#if childPage.childrenList.size() gt 0>
        <@childMenus parentPage=childPage level="${level+1}"/>
    </#if>
    </#list>

</#macro>

<#macro primaryAncestorChildren>
    <table width="151" border="0" cellspacing="0" cellpadding="0">
        <@childMenus parentPage=activePage.primaryAncestor/>
    </table>
</#macro>

<#macro pageBody>

    <table class="site-area" width="600" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width=151 valign=bottom>
                <img src="${resourcesPath}/images/products/frameworks/spacer-border-left.gif"/>
                <table width=100% border="0" cellspacing="0" cellpadding="0">
                    <tr><td class="site-area-name">${activePage.primaryAncestor.getCaption(vc)}</td></tr>
                </table>
            </td>
            <td width=443 class="tag-line"><p align=right>${activePage.tagLine}</p></td>
        </tr>
    </table>

    <table width="600" border="0" cellspacing="0" cellpadding="0" class="body-content">
        <tr>
            <td valign="top" class="page-nav">
                <@primaryAncestorChildren/>
            </td>
            <td valign="top" class="body-content">
                <#if activePage.summary?exists>
                <div class="body-summary">${activePage.summary}</div>
                <p>
                </#if>
                <#nested>
                <p>&nbsp;
            </td>
        </tr>
   </table>

</#macro>

<#macro frameworksPageHeading boxImage="products/frameworks/box-nefs.jpg" productNameImage="products/frameworks/name-nefs.gif">
    <#if activePageId = "/products/frameworks/sparx">
        <#local boxImage="products/frameworks/box-sparx.gif"/>
    <#elseif activePageId = "/products/frameworks/axiom">
        <#local boxImage="products/frameworks/box-axiom.gif"/>
    <#elseif activePageId = "/products/frameworks/commons">
        <#local boxImage="products/frameworks/box-commons.gif"/>
    </#if>

    <table width="600" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td><img src="${resourcesPath + '/images/' + boxImage}" width=252 height=166 alt="" /></td>
            <td>
                <table width="348" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td><img src="${resourcesPath + '/images/' + productNameImage}" width=348 height=104 alt=""/></td>
                    </tr>
                    <tr>
                        <td>

                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td><a onmouseover="changeImages( /*CMP*/'presentation',/*URL*/'images/presentation-over.gif');return true" onmouseout="changeImages( /*CMP*/'presentation',/*URL*/'images/presentation.gif');return true" href="#"><img src="images/presentation.gif" alt="" name="presentation" height="24" width="82" border="0"></a></td>
                                    <td><a onmouseover="changeImages( /*CMP*/'database',/*URL*/'images/database-over.gif');return true" onmouseout="changeImages( /*CMP*/'database',/*URL*/'images/database.gif');return true" href="#"><img src="images/database.gif" alt="" name="database" height="24" width="62" border="0"></a></td>
                                    <td><a onmouseover="changeImages( /*CMP*/'security',/*URL*/'images/security-over.gif');return true" onmouseout="changeImages( /*CMP*/'security',/*URL*/'images/security.gif');return true" href="#"><img src="images/security.gif" alt="" name="security" height="24" width="59" border="0"></a></td>
                                    <td><a onmouseover="changeImages( /*CMP*/'online',/*URL*/'images/online-over.gif');return true" onmouseout="changeImages( /*CMP*/'online',/*URL*/'images/online.gif');return true" href="#"><img src="images/online.gif" alt="" name="online" height="24" width="74" border="0"></a></td>
                                    <td><a onmouseover="changeImages( /*CMP*/'fact',/*URL*/'images/fact-over.gif');return true" onmouseout="changeImages( /*CMP*/'fact',/*URL*/'images/fact.gif');return true" href="#"><img src="images/fact.gif" alt="" name="fact" height="24" width="71" border="0"></a></td>
                                </tr>
                            </table>

                        </td>
                    </tr>
                    <tr>
                        <td><img src="${resourcesPath + '/images/products/frameworks/spacer-heading-bottom.gif'}" width=348 height=38 alt="" /></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</#macro>

