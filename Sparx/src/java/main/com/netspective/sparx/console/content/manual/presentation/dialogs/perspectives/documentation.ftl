<div class="textbox">

    <b>Standard Perspectives</b>

    <table class="report" width=100% border="0" cellspacing="2" cellpadding="0">
    <#assign classSuffix="odd"/>
        <tr>
            <td class="report-column-heading">Perspective</td>
            <td class="report-column-heading">Description</td>
        </tr>

    <#assign flags = getClassInstanceForName("com.netspective.sparx.form.DialogPerspectives")/>
    <#list flags.flagsDefns as flagDefn>
        <tr>
            <td class="report-column-${classSuffix}">
                ${flagDefn.name?lower_case}
            </td>
            <td class="report-column-${classSuffix}">
                ${flagDefn.description}
            </td>
        </tr>
        <#if classSuffix = 'odd'>
            <#assign classSuffix='even'/>
        <#else>
            <#assign classSuffix='odd'/>
        </#if>
    </#list>
    </table>

    <p>
    <b>Examples</b>
    <@xmlCode>
    <dialog ...>
        ...
        <field type="text" name="text_field_1" caption="Text Field" size="50"
               hint="Read-only when ADD" default="I guess the perspective is not 'add'">
            <conditional action="apply-flags" flags="read-only" perspective="add"/>
        </field>

        <field type="static" name="static_field_4" default="The perspective is not 'add' or 'edit'">
            <conditional action="apply-flags" flags="unavailable" perspective="add | edit"/>
        </field>
        ...
    </dialog>
    </@xmlCode>

</div>