<#include "/console/content/library.ftl">

<style>
    @import url(${vc.rootUrl}/resources/sampler.css);
</style>
<link rel="stylesheet" href="${vc.rootUrl}/resources/sampler.css" type="text/css">
<#macro childSummary parentPage>
    <#list parentPage.childrenList as childPage>
        <#if ! childPage.flags.hidden>
        <tr>
            <td>
                <a href="${childPage.getUrl(vc)}">${childPage.caption.getTextValue(vc)}</a>
            </td>
            <td>
                ${childPage.summary?default("&nbsp;")}
            </td>
        </tr>
    </#if>
    </#list>
</#macro>

<#macro pageChildren>
    <table class="page-children">
        <tr>
            <th>Page</th>
            <th>Purpose</th>
        </tr>
        <@childSummary parentPage=vc.navigationContext.activePage/>
    </table>
</#macro>

<#macro nextPageLink>
   <#local nextPage = vc.navigationContext.activePage.getNextPath()?default('')/>
   <#if nextPage != ''><p align=right><br><a href="${nextPage.getUrl(vc)}">${nextPage.getHeading(vc)}</a> &gt;&nbsp;</p></#if>
</#macro>

<#macro footer>
    <@nextPageLink/>
</#macro>

<#macro formDescription formName>
    <div class="textbox">
        <include "/console/content/library.ftl"/>
        The dialog and its member fields you see above were configured using the XML code displayed below. You can easily relate the XML with
        the displayed dialog fields by the individual <i>caption</i> values. Press the <i>Submit</i> button to submit the
        dialog (make sure to fill in all the REQUIRED fields!) and to view various information that is related to the state of the
        dialog.
        <br/>
        <br/>
        For a more detailed explanation
        about the various field settings, please consult the user manual.
        <div class="scrollbox">
        <@objectXmlSource object=vc.project.getDialog("${formName}")/>
        </div>
    </div>
</#macro>