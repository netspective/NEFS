<#include "/downloads/frameworks/releases.ftl"/>

<style>
    ul.change-log
    {
        padding-left: 15;
    }

    ul.change-log li
    {
        font-family: tahoma, arial, helvetica, sans; font-size: 8pt;
    }
</style>

The latest releases and all previous releases may be obtained from the
<a href="${servletPath}/downloads/frameworks/suite">Downloads</a> pages.

<#macro file framework version>
    <nobr><a href="${vc.rootUrl}/resources/downloads/lib/netspective-${framework?lower_case}-${version}.jar">${framework} ${version}</a></nobr>
</#macro>

<#macro showChangeLog framework>
    <#list releases["netspective/release[product/@name = '${framework}']"] as release>
        <#local productVersion = release["product[@name = '${framework}']/@version"]/>
        <a name="${framework}-${productVersion}"><h2><@file framework="${framework}" version=productVersion/>: ${release["change-log/summary"]}</h2></a>
        <ul class="change-log">
        <#list release["change-log/change-log-entry"] as entry>
            <li>${entry}</li>
        </#list>
        </ul>
    </#list>
</#macro>

<h1>Table of Contents</h1>
<ul>
    <li><a href="#Commons">Commons Core Library ChangeLog</a></li>
    <li><a href="#Axiom">Axiom Data Service ChangeLog</a></li>
    <li><a href="#Sparx">Sparx Application Platform ChangeLog</a></li>
</ul>

<a name="Commons"><h1>Commons Core Library ChangeLog</h1></a>
<@showChangeLog framework="Commons"/>

<a name="Axiom"><h1>Axiom Data Service ChangeLog</h1></a>
<@showChangeLog framework="Axiom"/>

<a name="Sparx"><h1>Sparx Application Platform ChangeLog</h1></a>
<@showChangeLog framework="Sparx"/>