<#macro screenShot name>
    <p align="center"><a href="${resourcesPath}/images/products/frameworks/suite/screen-shots/control/${name}.gif" target="nefs-control-screen-shot"
       ><img src="${resourcesPath}/images/products/frameworks/suite/screen-shots/control/${name}-preview.gif" border=0></a></p>
</#macro>

Bundled with the NEFS is the Netspective Enterprise Console (NEC). The browser-based interface displays all of your
application's dynamic components and objects enabling your development team to collectively manage the application
development process.

<table align=center cellpadding=7>
    <tr>
        <td><@screenShot name="console-login"/></td>
    </tr>
</table>
The Enterprise Console is an invaluable debugging aid, diagnostic tool, documentation management system, and process
artifact collection utility in one thin client application. And, because it's a standard NEFS application it's a great
demonstration and sample application.

<table align=center cellpadding=7>
    <tr>
        <td><@screenShot name="console-main"/></td>
    </tr>
</table>

<h1>Automatic Documentation</h1>
Your development team concentrates on implementation of business functionality while the Enterprise Console automatically
collects all relevant project metrics, documentation, and details. Managers can use ACE in real-time to track programmer work and productivity.

<ul class="check-list">
    <li>Automatically documents web dialogs, SQL statements, schema objects, and other programming artifacts.
        <@screenShot name="console-schema-doc"/><p>
        <@screenShot name="console-dynamic-qry"/><p>
        <@screenShot name="console-dialog-catalog"/>

    <li>Automatically provides browser-based testing of forms and statements.
        <@screenShot name="console-dialog-unit-test"/><p>
        <@screenShot name="console-sql-unit-test"/>

    <li>Automatically maintains application metrics.
        <@screenShot name="console-app-matrics"/><p>
    
    <li>Tracks execution statistics for SQL statements, servlet and JSP pages, dialogs/forms, and security.
    <li>Provides a browser-based repository for all project documentation alongside application code.
</ul>

<h1>Summary of Console Features</h1>

<ul class="check-list-in-body">
    <li>Administration Console
    <li>Unit Testing of Common Components
    <li>Unit Testing of Custom Components
    <li>Integration Testing
    <li>System Testing
    <li>Functional Specifications
    <li>Metrics (Function Points, SLOC)
    <li>Implementation documentation
</ul>