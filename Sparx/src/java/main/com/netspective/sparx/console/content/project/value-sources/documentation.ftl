<div class="textbox">

    The best way to learn about Value Sources is to consider some examples. Although value sources are used throughout
    Sparx, some of the most common usage patterns will occur in the presentation layer (like Navigation pages and
    Dialogs).

    <@xmlCode>
    <dialog heading="session:myHeading">
        <field type="text" name="text-field" caption="staticCaption" default="request:varname"/>
        <field type="integer" name="int-field" caption="request:myCaption" default="my-rule:some-value"/>
        <field type="select" name="files" caption="Files" choices="filesystem-entries:/home/all"/>
    </dialog>

    <navigation-tree name="abc">
        <page name="def" caption="something" heading="session:someVar"/>
        ...
    </@xmlCode>

    <br/>
    In the preceding examples,
    a dialog specification is defined to have a heading which will be dynamically
    generated at run-time from the value of the 'myHeading' session attribute. The
    text field will have a static caption, but its default value will come from
    a request parameter called 'varname'. In the integer field example, the
    caption and default will both be dynamic and the default value will actually come
    from a class that has been registered as 'my-rule'.	In the select field example, this
    select field fills its choices with the files contained in the directory /home/all.

</div>