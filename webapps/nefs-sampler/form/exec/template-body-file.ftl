<#assign dateOfBirth = vc.fieldStates.getState("birth_date").value.value/>

Welcome to NEFS, ${vc.fieldStates.getState("full_name").value.textValue}.
Your birthday is on ${dateOfBirth?date}.
<p>
This form demonstrated how you can take two
pieces of data and execute an arbritrary <a href="http://www.freemarker.org">FreeMarker</a> template in a separate file
from the project.xml file.
<p>
Also, note that since the &lt;dialog&gt; loop attribute was set to "no" the dialog disappears once data is entered.
In the Form1 example, the loop attribute is not provided (and it defaults to yes) so the form remains on the screen
after data is entered.

<p>
<a href="body-file">Return to dialog</a>
