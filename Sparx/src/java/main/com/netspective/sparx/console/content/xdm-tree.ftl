<#include "*/library.ftl"/>

<#assign dTreeRootUrl = vc.activeTheme.getResourceUrl('/dtree-2.05')/>
<script src="${dTreeRootUrl}/dtree.js"></script>

<div class="dtree">
	<a href="javascript: tagsTree.openAll();">open all</a> | <a href="javascript: tagsTree.closeAll();">close all</a></p>
    <STYLE TYPE="text/css">
      @import url(${dTreeRootUrl}/dtree.css);
    </STYLE>
	<script type="text/javascript">

		var tagsTree = new dTree('tagsTree');
        for(var icon in tagsTree.icon)
            tagsTree.icon[icon] = "${dTreeRootUrl}/" + tagsTree.icon[icon];

        <#assign activeIndex = 0/>
		<@addTreeNodes className="com.netspective.sparx.Project" tag="project" parentIndex=-1/>

		document.write(tagsTree);

	</script>
</div>

<#macro addTreeNodes className tag parentIndex level=0 ancestors=[]>
    <#local activeParent = activeIndex/>

    <#local schema = getXmlDataModelSchema(className)/>
    <#local recursive = false>
    <#local suffix = ''/>
    <#list ancestors as ancestor>
        <#if ancestor = className>
            <#local recursive = true/>
            <#local suffix = ' (recursive)'/>
            <#break>
        </#if>
    </#list>

    tagsTree.add(${activeIndex}, ${parentIndex}, '&lt;${tag}&gt;${suffix}');

    <#assign activeIndex = activeIndex+1/>

    <#if recursive = false>
    <#local childElements = schema.getNestedElementsDetail()/>
    <#list childElements as childDetail>
        <#if childDetail.elemType.name != className>
            <@addTreeNodes className=childDetail.elemType.name
                           tag=childDetail.elemName
                           parentIndex=activeParent level=(level+1)
                           ancestors=(ancestors + [className])/>
        </#if>
    </#list>
    </#if>
</#macro>

