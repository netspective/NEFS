<#include "*/library.ftl">

<#assign doingFrameworkDeveploment = vc.getEnvironmentFlags().flagIsSet(statics["com.netspective.commons.RuntimeEnvironmentFlags"].FRAMEWORK_DEVELOPMENT)/>
<#assign dialogs = vc.project.dialogs/>
<#assign catalog = []/>
<#list dialogs.nameSpaceNames as nameSpaceId>
    <#if ! (nameSpaceId == 'console' && ! doingFrameworkDeveploment)>
        <#assign catalog = catalog + [[ ["colspan=4", "<i>${nameSpaceId}</i>"] ]]/>
        <#list dialogs.dialogs as dialog>
            <#if dialog.nameSpace?default('-') != '-' && dialog.nameSpace.nameSpaceId = nameSpaceId>
                <#assign catalog = catalog + [
                    ["&nbsp;&nbsp;&nbsp;&nbsp;<a href='unit-test?cmd=dialog,${dialog.qualifiedName}&selected-dialog-id=${dialog.qualifiedName}'>${dialog.name}</a>",
                     dialog.fields.size(), getClassReference(dialog.class.name), dialog.frame.heading?default('&nbsp;')]]/>
            </#if>
        </#list>
    </#if>
</#list>

<@panel heading="All Available Dialogs">
    <@reportTable headings=["Dialog", "Fields", "Class", "Heading"] data=catalog columnAttrs=["", "align=right", "", ""]/>
</@panel>
