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
<dialog name="numeric-fields" retain-params="*" redirect-after-execute="false">
    <frame heading="Test Numeric Fields"/>
    <field type="integer" name="integer_field" caption="Integer Field"
        hint="Integer field" persist="yes"/>
    <field type="float" name="float_field" caption="Float Field"
        hint="Float field"/>
    <field type="currency" name="currency_field1" caption="Currency"
        decimals-required="2" default="123.45"
        negative-pos="after-symbol" hint="Currency field (US) with negative sign after the symbol"/>
    <field type="currency" name="currency_field2" caption="Currency"
        decimals-required="2" default="123.45"
        negative-pos="before-symbol" hint="Currency field (US) with negative sign before the symbol"/>
    <field type="phone" name="phone_field1" caption="Phone Field (dash format)"
        strip-brackets="yes" style="dash" hint="Phone field" default="8001234567"/>
    <field type="phone" name="phone_field2" caption="Phone Field (bracket format)"
        strip-brackets="yes" style="bracket" hint="Phone field" default="8001234567"/>
    <field type="zip-code" name="zip_field" caption="Zip Field"
        hint="Zip code field" default="12345"/>
    <field type="ssn" name="ssn_field" caption="SSN" strip-dashes="yes"
        default="999999999" hint="Social Security Number field"/>
    <director submit-caption="Submit"/>
</dialog>
</@xmlCode>
</div>
