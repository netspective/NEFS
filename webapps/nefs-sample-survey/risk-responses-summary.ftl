<#include "console/content/library.ftl"/>

<#assign queryResults = getQueryResultsAsMatrix("db.Risk_Response.all-responses-for-particular-pin")/>

<#assign lastGroup = ""/>
<#assign results = []/>
<#list queryResults as queryRow>
    <#assign resultRow = []/>
    <#if lastGroup = queryRow[0]>
        <#assign resultRow = resultRow + [ "&nbsp;" ]/>
    <#else>
        <#assign resultRow = resultRow + [ queryRow[0] ]/>
    </#if>
    <#assign resultRow = resultRow + queryRow[1..7]/>
    <#assign results = results + [resultRow]/>
    <#assign lastGroup = queryRow[0]/>
</#list>

<@reportTable
      headings=["Group", "Risk",
                "Business Unit Significance", "Business Unit Effectiveness",
                "Larger Group Significance", "Larger Group Effectiveness",
                "Batelle Overall Significance", "Batelle Overall Effectiveness"]
      data=results/>
