<#include "releases.ftl"/>

<#macro file framework version>
    <nobr><a href="${vc.rootUrl}/resources/downloads/lib/netspective-${framework?lower_case}-${version}.jar">${framework} ${version}</a></nobr>
</#macro>

<#macro showReleases framework>
    <table class="data-table">
        <tr>
            <th>Release</th>
            <th>Summary</th>
            <th>Changes</th>
        </tr>
        <#list releases["netspective/release[product/@name = '${framework}']"] as release>
            <tr>
                <td><@file framework="${framework}" version=release["product[@name = '${framework}']/@version"]/></td>
                <td>${release["change-log/summary"]}</td>
                <td><p align=right>${release["count(change-log/change-log-entry)"]}</p></td>
            </tr>
        </#list>
    </table>
</#macro>

<h1>Latest Releases</h1>
The latest versions of each of the frameworks is listed below. If you'd like instructions for how to update to the
latest releases, please refer to the <a href="${servletPath}/support/documentation/upgrading">Upgrade Guide</a>.
<p>
<table class="data-table">
    <tr>
        <th>Product</th>
        <th>Latest</th>
    </tr>
    <tr>
        <td>Commons Core Library</td>
        <td><a href="${latestCommonsJarDownloadHref}">${latestCommonsVersion}</a></td>
    </tr>
    <tr>
        <td>Axiom Data Service</td>
        <td><a href="${latestAxiomJarDownloadHref}">${latestAxiomVersion}</a></td>
    </tr>
    <tr>
        <td>Commons Core Library</td>
        <td><a href="${latestSparxJarDownloadHref}">${latestSparxVersion}</a></td>
    </tr>
</table>

<h1>All Commons Core Library Releases</h1
<@showReleases framework="Commons"/>

<h1>All Axiom Data Service Releases</h1
<@showReleases framework="Axiom"/>

<h1>All Sparx Application Platform Releases</h1
<@showReleases framework="Sparx"/>