/**
 * This file contains client-side Javascript functions for handling 'actions' done on a tabular report.
 * To use this, your skin class must produce HTML similar to show below:
 *
 <script>
    myReportAction1 = new ReportAction('report1');
    myReportAction1.registerSubmitAction('Button1', 'http://slashdot.org');
    myReportAction1.registerSubmitAction('Button2', 'http://www.theserverside.net');
</script>
<form name="report1" action="http://yahoo.com">
<table>
<th>
<tr>
    <td><input type="checkbox" name="checkAll" value="column1=1" onclick="reportEventOnClick(myReportAction1, this, event)"/></td>
    <td>Index</td>
    <td>Value</td>
</tr>
</th>
<tr>
    <td><input type="checkbox" name="selectedItemList" value="column1=1" onclick="reportEventOnClick(myReportAction1, this, event)"/></td>
    <td>1</td>
    <td>Hello</td>
</tr>
<tr>
    <td><input type="checkbox" name="selectedItemList" value="column1=2" onclick="reportEventOnClick(myReportAction1, this, event)"/></td>
    <td>2</td>
    <td>Hello</td>
</tr>
<tr>
    <td><input type="checkbox" name="selectedItemList" value="column1=3" onclick="reportEventOnClick(myReportAction1, this, event)"/></td>
    <td>3</td>
    <td>Hello</td>
</tr>
<tr>
    <td><input type="button" name="submitAction" value="Button1" onclick="reportEventOnClick(myReportAction1, this, event)"/></td>
    <td><input type="button" name="submitAction" value="Button2" onclick="reportEventOnClick(myReportAction1, this, event)"/></td>
    <td><input type="button" name="submitAction" value="Button3" onclick="reportEventOnClick(myReportAction1, this, event)"/></td>
</tr>
</table>
</form>
 *
 *
 * The submit URL will need to handle the 'submitAction' and 'selectedItemList' request parameters to extract what type
 * of action was submitted and what items were selected.
 */

/**
* Function for handling the onClick event of all the html elements associated
* with the ReportAction.
*/
function reportEventOnClick(parentReport, source, event)
{
    if (source.type == 'checkbox')
        parentReport.actionCheck(source);
    else if (source.type == 'button')
        parentReport.actionButtonPress(source);
}

/**
* Object function for representing the actions associated with a report.
* A variable should be initialized for each report with this function.
* For example:
*      var myReportAction1 = new ReportAction('report1', 'Test Heading', 'Error');
*      var myReportAction2 = new ReportAction('report2');
*
* PARAMETERS:
*  name          Name of the report
*  heading       Heading of the report (optional)
*  errorMessage  Custom error message for display when the submit action fails (optional)
*/
function ReportAction(name, heading, errorMessage)
{
    this.reportName = name;
    this.reportHeading = heading;
    this.submitErrorMessage = errorMessage;
    this.submitActions = new Array();

    this.actionCheck = ReportAction_selectRow;
    this.actionButtonPress = ReportAction_submit;
    this.registerSubmitAction = ReportAction_registerSubmitAction;
}

/**
* Function for registering the submit action URL
*
* PARAMETERS:
*  name
*  url
*/
function ReportAction_registerSubmitAction(name, url)
{
  this.submitActions[name] = url;
}

/**
* Function for handling the submit button actions associated with the report.
* This will first make sure that at least one checkbox is checked before allowing
* the submit action to occur.
*
* PARAMETERS:
*  source  the html button element where the action occurred
*/
function ReportAction_submit(source)
{
    checkboxFields = source.form.selectedItemList;
    for (i = 0; i < checkboxFields.length; i++)
    {
        if (checkboxFields[i].checked)
        {
            source.form.action = this.submitActions[source.value];
            source.form.submit();
            return true;
        }
    }
    if (this.submitErrorMessage)
        alert(this.submitErrorMessage);
    else
        alert("No items have been selected for '" + this.reportHeading + "'");
}

/**
* Function for handling the check/uncheck action associated with the report.
*
* PARAMETERS:
*  source  the html checkbox element where the action occurred
*/
function ReportAction_selectRow(source)
{
    if (source.name == 'checkAll')
    {
        checkboxFields = source.form.selectedItemList;
        for (i = 0; i < checkboxFields.length; i++)
        {
            if (source.checked)
                checkboxFields[i].checked = true;
            else
                checkboxFields[i].checked = false;
        }
    }
    else
    {
        source.form.checkAll.checked = false;
    }
    return true;
}
