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
<dialog name="boolean-fields" retain-params="*" redirect-after-execute="false">
    <frame heading="Test Boolean Fields"/>
    <field type="boolean" name="bool_field_radio" caption="Boolean Field (Radio)" style="radio"/>
    <field type="boolean" name="bool_field_alone" caption="Boolean Field (Alone)" style="check-alone"/>
    <field type="boolean" name="bool_field_combo" caption="Boolean Field (Combo)" style="combo"/>
    <director submit-caption="Submit"/>
</dialog>
</@xmlCode>
</div>
