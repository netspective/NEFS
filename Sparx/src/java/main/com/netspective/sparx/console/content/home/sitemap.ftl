<#macro childMenus parentPage>
    <#list parentPage.childrenList as childPage>
        <#if ! childPage.flags.hidden>
        <tr>
            <td class="L${childPage.level}">
                <#if childPage.heading?exists>
                <a href="${childPage.getUrl(vc)}">${childPage.heading.getTextValue(vc)}</a>
                <#else>
                <a href="${childPage.getUrl(vc)}">${childPage.caption.getTextValue(vc)}</a>
                </#if>
            </td>
        </tr>
        <#if childPage.childrenList?size gt 0>
            <@childMenus parentPage=childPage/>
        </#if>
    </#if>
    </#list>
</#macro>

<style>
table.site-map td a { font-family: verdana, arial, helvetica, sans; font-size: 9pt;  }
table.site-map td.L1 { padding-left:   0px; padding-top: 15px; }
table.site-map td.L2 { padding-left:  25px; padding-top: 5px; }
table.site-map td.L3 { padding-left:  50px; }
table.site-map td.L4 { padding-left:  75px; }
table.site-map td.L5 { padding-left: 100px; }
table.site-map td.L6 { padding-left: 125px; }
table.site-map td.L7 { padding-left: 150px; }
</style>

<table class="site-map" width=100% border="0" cellspacing="0" cellpadding="0">
    <@childMenus parentPage=vc.navigationContext.ownerTree.root/>
</table>
<p>
&nbsp;