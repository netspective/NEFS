<#include "*/library.ftl">

<div class="textbox">

    <b>Examples</b>
    <@xmlCode>
    <dialog name="DialogTest_12" heading="Test Custom Javascript">
        <client-js href="create-app-url:/resources/scripts/eimo.js"/>

        <field type="float" name="float_field" caption="Float">
            <client-js event="is-valid" type="extends" js-expr="return testValid(field, control);"/>
            <client-js event="value-changed" type="extends" js-expr="return testNum(field, control);"/>
        </field>

        <field type="integer" name="integer_field" caption="Integer">
            <client-js event="is-valid" type="override" js-expr="return testValid(field, control);"/>
            <client-js event="value-changed" type="extends" js-expr="return testNum(field, control);"/>
            <client-js event="click" type="extends" js-expr="alert('onClick');return true;"/>
            <client-js event="get-focus" type="extends" js-expr="alert('onFocus!');return true;"/>
            <client-js event="lose-focus" type="extends" js-expr="alert('onBlur!');return true;"/>
            <client-js event="key-press" type="extends" js-expr="alert('onKeypress!');return true;"/>
        </field>

        <field type="ssn" name="ssn_field" caption="SSN">
            <client-js event="is-valid" type="override" js-expr="return true;"/>
            <client-js event="value-changed" type="override" js-expr="return true;"/>
        </field>
    </dialog>
    </@xmlCode>

    <@xdmChildStructure parentClassName="com.netspective.sparx.form.field.DialogField" childElementName="client-js" expandFlagAliases='yes'/>

</div>