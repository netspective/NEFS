<#include "*/macros.ftl"/>

<#macro childMenus parentPage>
    <#list parentPage.childrenList as childPage>
        <#if ! childPage.flags.hidden>
        <tr>
            <td class="L${childPage.level}" valign="top"><a href="${childPage.getUrl(vc)}">${childPage.caption.getTextValue(vc)}</a></td>
            <td valign="top"> ${childPage.summary?default("&nbsp;")} </td>
        </tr>
        <#if childPage.childrenList?size gt 0>
            <@childMenus parentPage=childPage/>
        </#if>
    </#if>
    </#list>
</#macro>

<table class="site-map" border="0"  cellpadding="0" cellspacing="0">
    <@childMenus parentPage=vc.navigationContext.ownerTree.root/>
</table>
<p>
&nbsp;