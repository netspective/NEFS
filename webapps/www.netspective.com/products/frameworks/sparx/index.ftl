<h1>The Sparx Application Platform</h1>
Because Sparx comes out-of-the-box with skinable navigation systems, auto validated forms and works on any modern
browswer that supports HTML and CSS, it takes under 30 minutes to fully prototype your J2EE applications and prep them
for <a href="${servletPath}/products/frameworks/axiom">Axiom's</a> data bindings and <a href="${servletPath}/products/frameworks/commons">Commons'</a> security authenticators. By starting your development process with Sparx, the bulk of your application's
presentation code is geneerated for you giving you more time to focus on any customization you may have.

<ul class="check-list">
    <li>Just declare your input fields, page navigation and validation criteria in simple XML files and let Sparx
        generate all HTML, error trapping, and Java code for you.</li>
    <li>Create entire application mockups, including navigation and data entry, without expert programmers.</li>
    <li>Once your prototypes are complete, drop in the business rules and application logic using standard Java and
        you'll be able to complete entire application functions in minutes instead of hours and days.</li>
</ul>

<h1>Object-oriented Pages and Forms</h1>
In Sparx, every page and every form (including validation rules, field types, and conditions) are full objects that
are cached using instance pooling.
<ul class="check-list">
    <li>You can have hierachies of pages that inherit common behaviors (like headers, footers, and meta-data) or delegate
        their content based on object-oriented principles.
    <li>You can create base form classes that perform common actions and extend them
        using standard Java inheritance and delegation.
    <li>You can new create field types that inherit behavior from other fields and validation rules that can be reused.
</ul>

<h1>Effortless MVC with Advanced Page Navigation and Workflow</h1>
Sparx supports the MVC (Model-View-Controller) paradigm with a sophisticated implementation of the <i>Front Controller</i>
design pattern.

<ul class="check-list">
    <li>Use simple tags like <code>&lt;navigation-tree&gt;</code> and <code>&lt;page&gt;</code> to define all navigation
        rules in XML. Create sophisticated layouts that automatically generate HTML without you writing any Java code.
    <li>Create multiple navigation trees and conditional pages that a personalized to individual users or roles or
        act differently based on who their ancestor and parent pages happen to be.
</ul>

<h1>Get Sophisticated Forms Managementent and Data Validation</h1>
Sparx provides a dialog (forms) state machine implementation that eliminates your burden of HTML generation, field
population, and data validation.
<ul class="check-list">
    <li>You declare the data entry rules using simple <code>&lt;dialog&gt;</code>, <code>&lt;field&gt;</code>, and
        <code>&lt;validation&gt;</code> tags and Sparx does the rest.
    <li>Full support for client-side validation, server-side validation, and client-side conditional logic without
        server-side roundtrips is included.
</ul>

<h1>Switch UI Looks and Feel Without Code Changes</h1>
Sparx leverages themes and skins to isolate your user interface from the business rules and app functionality. Designers
can concentrate on the UI while engineers do the coding. All UI and look-and-feel changes can be made globally but
each page can still control its own appearance.

<ul class="check-list">
    <li>Sparx has high-level objects like Dialogs and Fields that automatically know to render themselves using built-in themes
and skins.
    <li>If you like what you see already, you'll never have to worry about HTML again. If you want to customize the HTML, though,
        you can do the customizations in Java or your favorite template engine.
</ul>

<h1>Leverage your Favorite Template Engine</h1>
Although Sparx has high-level objects that render themselves, sometimes you need more control or customization. Sparx has built-in support for JSPs and FreeMarker but
supports Velocity, Tea, WebMacro, and other templating engines. Don't worry about learning a new template language - just
use what you're using today.

<h1>Perform Database Record Edits/Updates/Deletes without Java</h1>
A special form class, called the <code>SchemaRecordEditorDialog</code> is provided to support zero-Java CRUD operations
for record management. If you need to insert/update/delete data to/from your favorite database you can do so simply by
declaring XML-based rules and not require a single line of Java. But, customizations are always welcome so you can use
standard java extension mechanisms like inheritance or delegation.

<h1>Use the Built-in Report Writer or your Favorite Reporting Engine</h1>
Most web based applications have a need for a basic report writer. Sparx includes a fully functional report writer that
can take SQL and generate reports with one line of XML or take SQL and pass it on to an XSLT stylesheet for highly
customizable output. If you need even more advanced functionality you can attach your favorite reporting engine.

<h1>Summary of Sparx Features</h1>
<ul class="check-list-in-body">
    <li>Forms and Controls
    <li>Client-side Validation
    <li>Server-side Validation
    <li>Use any template language like JSPs, Velocity, Tea and others.
    <li>Automatic Form Reload on Error
    <li>Dialog Data State Machine
    <li>Complete HTML generation
    <li>Data Binding to Presentation Layer
    <li>Reports
    <li>Navigation and Workflow
    <li>Wireless, PDA, Browser Support
    <li>Multiple skins support (with no code changes)
    <li>Presentation Object Performance Statistics
</ul>