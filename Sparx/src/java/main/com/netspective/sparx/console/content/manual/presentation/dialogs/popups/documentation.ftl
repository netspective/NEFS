<#include "*/library.ftl">

<div class="textbox">

<@xdmChildStructure parentClassName="com.netspective.sparx.form.field.DialogField" childElementName="popup" expandFlagAliases='yes'/>
<p>
Sample Dialog
<@xmlCode>
	<dialog ...>
		<field type="text" name="text_field" caption="Text Field">
			<popup action="/test/testPopup.html" fill="text_field"/>
		</field>
	</dialog>
</@xmlCode>
<p>
Sample HTML
<@htmlCode>
	<html>
		<head>
		<script src='/shared/resources/scripts/popup.js' language='JavaScript1.1'></script>
		</head>
		<a href="javascript:chooseItem('a', 'b', 'adjacent to b', 'static-value')">Click here!</a>
	</html>
</@htmlCode>

</div>