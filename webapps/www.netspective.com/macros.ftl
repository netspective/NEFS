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
    <#if childPage.childrenList?size gt 0>
        <@childMenus parentPage=childPage level="${level+1}"/>
    </#if>
    </#list>

</#macro>

<#macro primaryAncestorChildren>
    <table width="151" border="0" cellspacing="0" cellpadding="0">
        <@childMenus parentPage=activePage.primaryAncestor/>
    </table>
</#macro>

<#macro pageBodyBegin>

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
</#macro>

<#macro pageBodyEnd>

                <p>&nbsp;
            </td>
        </tr>
   </table>
</#macro>