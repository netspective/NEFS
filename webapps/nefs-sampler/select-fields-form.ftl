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
<dialog name="select-fields" retain-params="*" redirect-after-execute="false">
    <frame heading="Test Select Fields"/>
    <field type="separator" heading="Select Fields"/>
    <field type="select" name="sel_field_combo" caption="Select Field (Combo)" style="combo"
        choices="text-list:Choice 1=A'S;Choice 2=B;Choice 3=C" persist="true"
        default="A'S" hidden="no" prepend-blank="yes" append-blank="yes"/>

    <field type="select" name="sel_field_combo_xml_items"
        caption="Select Field (Combo with XML Items)" style="combo"
        persist="true" default="A'S" hidden="no" prepend-blank="yes" append-blank="yes">
        <items>
            <item value="A" caption="Choice 1 (in XML Item)"/>
            <item value="B" caption="Choice 2 (in XML Item)"/>
            <item value="C" caption="Choice 3 (in XML Item)"/>
            <item value="D" caption="Choice 4 (in XML Item)"/>
            <item value="E" caption="Choice 5 (in XML Item)"/>
        </items>
    </field>

    <field type="select" name="sel_field_radio" caption="Select Field (Radio)"
        style="radio" required="no"
        choices="text-list:[;!]Choice 1!A;Choice 2!B;Choice 3!C"/>
    <field type="select" name="sel_field_list" caption="Select Field (List)" style="list"
        size="5" default="B" hidden="no" choices="text-list:Choice 1=A;Choice 2=B;Choice 3=C"/>
    <field type="select" name="sel_field_multilist" caption="Select Field (MultiList)"
        style="multilist" size="5"
        default="text-list:A,B"
        choices="text-list:Choice 1=A;Choice 2=B;Choice 3=C"/>
    <field type="select" name="sel_field_multicheck" caption="Select Field (MultiCheck)"
        style="multicheck" required="yes"
        choices="text-list:Choice 1=A;Choice 2=B;Choice 3=C" default="text-list:A,C"/>
    <field type="select" name="sel_field_multidual" caption="Select Field (MultiDual)"
        style="multidual" required="yes"
        choices="text-list:Choice 1=A;Choice 2=B;Choice 3=C" default="text-list:A" persist="true"
        multi-dual-caption-left="Left Caption" multi-dual-caption-right="Right Caption"
        multi-dual-width="100"/>
    <director submit-caption="Submit"/>
    <field type="select" name="sel_field_popup" caption="Select Field (Popup)" style="popup"
        choices="text-list:Choice 1=A;Choice 2=B;Choice 3=C"/>
</dialog>
</@xmlCode>
</div>