<#include "*/library.ftl">

<div class="textbox">

    <b>Examples</b>
    <@xmlCode>
    <dialog name="DialogTest_10" heading="Test Data Command and Conditionals" loop="yes">
        <field type="boolean" name="checkbox_field" caption="Checkbox" style="checkalone"/>

        <field type="static" name="static_field2" default="Checkbox checked!">
            <conditional action="display" partner="checkbox_field" js-expr="control.checked == true"/>
        </field>

        <field type="static" name="static_field_4" default="The data command is not 'add' or 'edit'">
            <conditional action="apply-flags" flags="unavailable" perspective="add | edit"/>
        </field>
    </dialog>
    </@xmlCode>

    <b>Conditional Actions</b>
    <p>
    <#list vc.project.conditionalActions.instances as action>
        <@xdmStructure className=action.attributes.getValue("class") heading="&lt;conditional action='${action.templateName}'&gt;" expandFlagAliases='no'/>
        <p>
    </#list>

</div>