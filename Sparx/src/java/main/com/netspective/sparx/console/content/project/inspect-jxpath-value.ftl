<#include "/content/library.ftl"/>


<@panel heading="JXPath Inspection">
    <@reportTable
        data=[
            ["Expression", jxPathExpr],
            ["Value", getObjectAsText(jxPathValue)]
        ]
        />
</@panel>


<#function getObjectAsText object>

    <#if object?is_enumerable>
        <#local listValues = []/>
        <#list object as item>
            <#local listValues = listValues + [[getObjectAsText(item)]]/>
        </#list>
        <#local value>
            <@reportTable data=listValues dataMayContainsHtmlCellAttrs="no"/>
        </#local>
        <#return value/>
    <#elseif object?is_hash_ex>
        <#local hashValues = []/>
        <#list object?keys as key>
            <#if object[key]?exists>
                <#local hashValues = hashValues + [[ key, "NULL" ]]/>
            <#else>
                <#if object[key]?exists && object[key]?is_method>
                    <#local hashValues = hashValues + [[ key, "Method" ]]/>
                <#else>
                    <#local hashValues = hashValues + [[ key, getObjectAsText(object[key])]]/>
                </#if>
            </#if>
        </#list>
        <#local value>
            <@reportTable data=hashValues dataMayContainsHtmlCellAttrs="no"/>
        </#local>
        <#return value/>
    <#elseif object?is_string || object?is_number || object?is_boolean || object?is_date>
        <#return object/>
    <#else>
        <#return "Unknown type"/>
    </#if>

</#function>