<#include "*/templates/macros.ftl"/>

<style>
table.about td { font-family: verdana; font-size: 9pt; }
</style>

<table class="about">

    <tr valign=top>
        <td>
            <b>Application</b>
            <table>
                <#assign product = vc.project.product/>
                <tr><td align=right>ID:</td><td>${product.productId}</td></tr>
                <tr><td align=right>Name:</td><td>${product.productName}</td></tr>
                <tr><td align=right>Version:</td><td>${product.versionAndBuild}</td></tr>
            </table>
            <p>
            <#if vc.authenticatedUser?exists>
            <@inspectObject object=vc.authenticatedUser heading="AuthenticatedUser"/>
            </#if>
        </td>

        <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>

        <td>
            <b>Netspective Frameworks</b>
            <table>
                <tr><td align=right>${statics["com.netspective.commons.ProductRelease"].PRODUCT_RELEASE.getProductName()}: </td><td>${statics["com.netspective.commons.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()}</td></tr>
                <tr><td align=right>${statics["com.netspective.axiom.ProductRelease"].PRODUCT_RELEASE.getProductName()}: </td><td>${statics["com.netspective.axiom.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()}</td></tr>
                <tr><td align=right>${statics["com.netspective.sparx.ProductRelease"].PRODUCT_RELEASE.getProductName()}: </td><td>${statics["com.netspective.sparx.ProductRelease"].PRODUCT_RELEASE.getVersionAndBuild()}</td></tr>
            </table>
        </td>
    </tr>

</table>