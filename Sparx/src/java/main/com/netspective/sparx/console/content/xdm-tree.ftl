<#include "*/library.ftl"/>

<#assign dTreeRootUrl = vc.activeTheme.getResourceUrl('/dtree-2.05')/>
<#assign classAliases = {
            'com.netspective.sparx.form.Dialog' : '/project/dialogs/dialog',
            'com.netspective.sparx.form.field.DialogField' : '/project/dialogs/dialog/field'
         }/>

<script src="${dTreeRootUrl}/dtree.js"></script>

<table width="100%">
    <tr valign="top">
        <td>
        <div class="dtree">
            <a href="javascript: tagsTree.openAll();">open all</a> | <a href="javascript: tagsTree.closeAll();">close all</a></p>
            <STYLE TYPE="text/css">
              @import url(${dTreeRootUrl}/dtree.css);
            </STYLE>
            <script type="text/javascript">

                var tagsTree = new dTree('tagsTree');
                tagsTree.config.target = 'doc';
                tagsTree.config.inOrder = true;
                tagsTree.config.folderLinks = true;
                for(var icon in tagsTree.icon)
                    tagsTree.icon[icon] = "${dTreeRootUrl}/" + tagsTree.icon[icon];

                <#assign activeIndex = 0/>
                <@addTreeNodes className="com.netspective.sparx.Project" tag="project"
                               tagClassPath="/com.netspective.sparx.Project" tagPath="/project"
                               parentIndex=-1/>

                document.write(tagsTree);

            </script>
        </div>
        </td>
        <td>&nbsp;&nbsp;&nbsp;</td>
        <td width="80%">
            <iframe name="doc" width="100%" height="100%" style="border: 0" frameborder="0"/>
        </td>
    </tr>
</table>

<#macro addTreeNodes className tag parentIndex tagClassPath tagPath level=0 ancestors=[] tmplProducer="">
    <#if classAliases[className]?exists && tagPath != classAliases[className]>
        tagsTree.add(${activeIndex}, ${parentIndex}, '&lt;${tag}&gt; (see ${classAliases[className]})');
        <#assign activeIndex = activeIndex+1/>
        <#return>
    </#if>

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

    <#if tmplProducer != ''>
        tagsTree.add(${activeIndex}, ${parentIndex}, '&lt;<i>${tag}</i>&gt;${suffix}',
                 '${vc.servletRootUrl}/reference/templates?ns=${tmplProducer}&page-flags=POPUP');
    <#else>
        tagsTree.add(${activeIndex}, ${parentIndex}, '&lt;${tag}&gt;${suffix}',
                 '${vc.servletRootUrl}/reference/tags?parent-tags=${tagPath}&parent-xdm-classes=${tagClassPath}&xdm-tag=${tag}&xdm-class=${className}&page-flags=POPUP');
    </#if>


    <#assign activeIndex = activeIndex+1/>

    <#if recursive = false>
        <#local childElements = schema.getNestedElementsDetail()/>
        <#list childElements as childDetail>
            <#assign tmplProducerInfo = ''>
            <#if childDetail.isTemplateProducer()>
                <#assign tmplProducerInfo = childDetail.getTemplateProducer().nameSpaceId/>
            </#if>
            <#if childDetail.elemType.name != className>
                <@addTreeNodes className=childDetail.elemType.name
                               tag=childDetail.elemName
                               tagClassPath=(tagClassPath + "/" + childDetail.elemType.name)
                               tagPath=(tagPath + "/" + childDetail.elemName)
                               parentIndex=activeParent level=(level+1)
                               ancestors=(ancestors + [className])
                               tmplProducer=tmplProducerInfo/>
            </#if>
        </#list>
    </#if>
</#macro>

