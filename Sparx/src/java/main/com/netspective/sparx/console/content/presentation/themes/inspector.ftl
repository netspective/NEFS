<#include "*/library.ftl">

<#assign activePage = vc.navigationContext.activePage/>
<#assign theme = vc.project.themes.getTheme(activePage.name)/>

<@inspectObject object=theme heading="Theme '${activePage.name}' Inspector"/>

<#if theme.resourceLocator?exists>
    <p>
    <@inspectObject object=theme.resourceLocator heading="Theme '${activePage.name}' Resource Locator Inspector"/>
</#if>

<p>
<@panel heading="Theme '${activePage.name}' Skins">
    <#assign skins = []/>

    <#assign skins = skins + [[ ["colspan=3", "<i>Navigation Skins</i>"] ]]/>
    <#list theme.navigationSkins.values().iterator() as navigationSkin>
        <#if navigationSkin.isDefault()><#assign defaultSkin="Yes"><#else><#assign defaultSkin="&nbsp;"></#if>
        <#assign skins = skins + [[
                    "&nbsp;&nbsp;&nbsp;&nbsp;<a href='#navSkin-${navigationSkin.name}'>${navigationSkin.name}</a>", defaultSkin, getClassReference(navigationSkin.class.name)]]/>
    </#list>

    <#assign skins = skins + [[ ["colspan=3", "<i>Panel Skins</i>"] ]]/>
    <#list theme.panelSkins.values().iterator() as panelSkin>
        <#if panelSkin.isDefault()><#assign defaultSkin="Yes"><#else><#assign defaultSkin="&nbsp;"></#if>
        <#assign skins = skins + [[
                    "&nbsp;&nbsp;&nbsp;&nbsp;<a href='#panelSkin-${panelSkin.name}'>${panelSkin.name}</a>", defaultSkin, getClassReference(panelSkin.class.name)]]/>
    </#list>

    <#assign skins = skins + [[ ["colspan=3", "<i>Dialog Skins</i>"] ]]/>
    <#list theme.dialogSkins.values().iterator() as dialogSkin>
        <#if dialogSkin.isDefault()><#assign defaultSkin="Yes"><#else><#assign defaultSkin="&nbsp;"></#if>
        <#assign skins = skins + [[
                    "&nbsp;&nbsp;&nbsp;&nbsp;<a href='#dialogSkin-${dialogSkin.name}'>${dialogSkin.name}</a>", defaultSkin, getClassReference(dialogSkin.class.name)]]/>
    </#list>

    <#assign skins = skins + [[ ["colspan=3", "<i>Report Skins</i>"] ]]/>
    <#list theme.reportSkins.values().iterator() as reportSkin>
        <#if reportSkin.isDefault()><#assign defaultSkin="Yes"><#else><#assign defaultSkin="&nbsp;"></#if>
        <#assign skins = skins + [[
                    "&nbsp;&nbsp;&nbsp;&nbsp;<a href='#reportSkin-${reportSkin.name}'>${reportSkin.name}</a>", defaultSkin, getClassReference(reportSkin.class.name)]]/>
    </#list>

    <@reportTable headings=["Skin", "Default", "Class"] data=skins columnAttrs=["", "", ""]/>
</@panel>

<#list theme.navigationSkins.values().iterator() as navigationSkin>
    <p>
    <@inspectObject object=navigationSkin heading="Navigation Skin '${navigationSkin.name}' Inspector"/>
</#list>

<#list theme.panelSkins.values().iterator() as panelSkin>
    <p>
    <@inspectObject object=panelSkin heading="Panel Skin '${panelSkin.name}' Inspector"/>
</#list>

<#list theme.dialogSkins.values().iterator() as dialogSkin>
    <p>
    <@inspectObject object=dialogSkin heading="Dialog Skin '${dialogSkin.name}' Inspector"/>
</#list>

<#list theme.reportSkins.values().iterator() as reportSkin>
    <p>
    <@inspectObject object=reportSkin heading="Report Skin '${reportSkin.name}' Inspector"/>
</#list>