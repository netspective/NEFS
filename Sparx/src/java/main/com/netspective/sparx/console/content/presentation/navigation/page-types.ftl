<#include "/content/library.ftl"/>

<#assign doingFrameworkDeveploment = vc.runtimeEnvironmentFlags.flagIsSet(statics["com.netspective.commons.RuntimeEnvironmentFlags"].FRAMEWORK_DEVELOPMENT)/>
<#assign trees = vc.project.navigationTrees/>
<#list trees.trees.keySet().iterator() as treeName>
    <#if ! (treeName == 'console' && ! doingFrameworkDeveploment)>
        <#assign tree=trees.getNavigationTree(treeName)/>
        <div class="textbox">
            Navigation Tree: <b>${tree.name}</b><p>
        </div>
        <@templateProducerInstances templateProducer=tree.pageTypes
                            consumerTag="page"
                            detailUrl="?"+ tree.name +"-type-name="
                            detail=vc.request.getParameter(tree.name +"-type-name")?default("-")/>
        <p/>
    </#if>
</#list>
