<#include "/console/content/library.ftl">

<style>
    @import url(${vc.rootUrl}/resources/app.css);
</style>
<#macro childSummary parentPage>
    <#list parentPage.childrenList as childPage>
        <#if ! childPage.flags.hidden>
        <tr>
            <td>
                <a href="${childPage.getUrl(vc)}">${childPage.caption.getTextValue(vc)}</a>
            </td>
            <td>
                <#if childPage.summary?exists>
                ${childPage.summary.getTextValue(vc)}
                <#else>
                Please add a &lt;summary&gt; tag (or attribute) to ${childPage.qualifiedNameIncludingTreeId}
                </#if>
            </td>
        </tr>
    </#if>
    </#list>
</#macro>

<#macro pageChildren page=vc.navigationContext.activePage>
    <table class="page-children">
        <tr>
            <th>Page</th>
            <th>Purpose</th>
        </tr>
        <@childSummary parentPage=page/>
    </table>
</#macro>

<#macro nextPageLink>
   <#local nextPage = vc.navigationContext.activePage.getNextPath()?default('')/>
   <#if nextPage != ''><p align=right><br><a href="${nextPage.getUrl(vc)}">${nextPage.getHeading(vc)}</a> &gt;&nbsp;</p></#if>
</#macro>

<#macro footer>
</#macro>
