<div style="padding: 5px; font-size: 12px">
The dialog and its member fields you see above were configured using the XML code displayed below. You can easily relate the XML with
the displayed dialog fields by the individual <i>caption</i> values. Press the <i>Submit</i> button to submit the
dialog (make sure to fill in all the REQUIRED fields!) and to view various information that is related to the state of the
dialog.
<br/>
<br/>
For a more detailed explanation
about the various field settings, please consult the user manual.
<@xmlCode>
    <dialog name="text-fields" retain-params="*" hide-hints-until-focus="yes"
        redirect-after-execute="false">
        <frame heading="Test Text Fields"/>
        <field name="text_field_required" type="text" caption="Text Required" required="yes"
            hint="This text field is always required"/>
        <field name="text_field_hidden" type="text" hidden="yes" default="request:id"/>
        <field name="static_field1" type="static" caption="Static 1" default="request:id"
            hint="Request attribute 'id' value"/>
        <field name="static_field2" type="static" caption="Static 2 "
            default="Static Field's Value"/>
        <field name="text_field_conditionally_required" type="text" caption="Text Required"
            hint="Text field is required only when req param 'abc' has value"
            default="conditional Required Text" required="no">
            <conditional action="apply-flags" flags="required" has-value="request:abc"/>
        </field>
        <field name="text_field" type="text" caption="Text"
            hint="Text field optional (max-length=5, uppercase=yes)"
            max-length="5" uppercase="yes"/>
        <field name="email_field" type="e-mail" caption="Email" hint="Email field"/>
        <field name="masked_field" type="text" caption="Masked Field" mask-entry="yes"
            hint="e.g password"/>
    </dialog>
</@xmlCode>
</div>
