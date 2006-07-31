var calendarsById = new Array();

// This function gets called when the end-user clicks on some date.
function calendarDateSelected(calendar, date)
{
    calendar.fieldControl.value = date; // just update the date in the input field.
    if(calendar.dateClicked)
    {
        calendar.callCloseHandler();
    }
}

// And this gets called when the end-user clicks on the _selected_ date,
// or clicks on the "Close" button.  It just hides the calendar without
// destroying it.
function calendarCloseHandler(calendar)
{
    calendar.hide();                        // hide the calendar
}

// This function shows the calendar under the element having the given id.
// It takes care of catching "mousedown" signals on document and hiding the
// calendar if the click was outside.
function showCalendar(fieldQualifiedName, format)
{
    if(activeDialog == null)
    {
        alert("No activeDialog found.");
    }

    var control = activeDialog.getFieldControl(fieldQualifiedName);
    if(control == null)
    {
        alert("No control found for field with the ID '"+ fieldQualifiedName +"' found.");
        return;
    }

    var calendar = calendarsById[fieldQualifiedName];
    if (calendar != null)
    {
        // we already have some calendar created
        calendar.hide();                 // so we hide it first.
    }
    else
    {
        // first-time call, create the calendar.
        var calendar = new Calendar(false, null, calendarDateSelected, calendarCloseHandler);
        calendarsById[fieldQualifiedName] = calendar;         // remember it in the global var
        calendar.weekNumbers = false;
        calendar.setRange(1900, 2070);        // min/max year allowed.
        calendar.create();
    }
    calendar.setDateFormat(format);    // set the specified date format
    calendar.parseDate(control.value);      // try to parse the text in field
    calendar.fieldControl = control;                 // inform it what input field we use
    calendar.showAtElement(control);        // show the calendar below it

    return false;
}
