<%@ page import="java.util.Date,
                 com.netspective.sparx.form.handler.DialogExecuteIncludeResourceHandler,
                 com.netspective.sparx.form.DialogContext,
                 com.netspective.sparx.form.field.DialogFieldStates"%>
<%
    DialogContext dc = (DialogContext) request.getAttribute(DialogExecuteIncludeResourceHandler.REQATTRNAME_DIALOG_CONTEXT);
    DialogFieldStates fieldStates = dc.getFieldStates();
    Date dateOfBirth = (Date) fieldStates.getState("birth_date").getValue().getValue();
%>

Welcome to NEFS, <%= fieldStates.getState("full_name").getValue().getTextValue() %>.
Your birthday is on <%= dateOfBirth %>.
<p>
This form demonstrated how you can take two
pieces of data and execute an arbritrary JSP template in a separate file from the project.xml file.
<p>
Also, note that since the &lt;dialog&gt; loop attribute was set to "no" the dialog disappears once data is entered.
In the Form1 example, the loop attribute is not provided (and it defaults to yes) so the form remains on the screen
after data is entered.

<p>
<a href="inc-jsp">Return to dialog</a>
