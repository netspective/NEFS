<#include "/content/library.ftl"/>

<div class="textbox">

    <#list vc.project.loginManagers.loginManagers.values().iterator() as loginManager>
        <@inspectObject object=loginManager heading="Login Manager '${loginManager.name}'"/>
        <p>
    </#list>

</div>