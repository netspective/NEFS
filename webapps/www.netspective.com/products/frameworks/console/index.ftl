<#macro screenShot name>
    <p align="center"><a href="${resourcesPath}/images/products/frameworks/suite/screen-shots/console/${name}.gif" target="nefs-control-screen-shot"
       ><img src="${resourcesPath}/images/products/frameworks/suite/screen-shots/console/${name}-preview.gif" border=0></a></p>
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
collects all relevant project metrics, documentation, and details. Managers can use Console in real-time to track programmer work and productivity.

<ul class="check-list">
    <li>Automatically documents web dialogs, SQL statements, schema objects, and other programming artifacts.<p>
    		<table>
    		    <tr>
    		    	<td><@screenShot name="console-schema-doc"/></td>
    		    	<td><@screenShot name="console-dynamic-qry"/></td>
    		    </tr>
    		    <tr>
		        <td><@screenShot name="console-dialog-catalog"/></td>
    		    </tr>
    		</table>
    	
    <li>Automatically provides browser-based testing of forms and statements.<p>
	<table>
	    <tr>
		<td><@screenShot name="console-dialog-unit-test"/></td>
		<td><@screenShot name="console-sql-unit-test"/></td>
	    </tr>
	</table>
    <li>Automatically maintains application metrics.<p>
        <@screenShot name="console-app-matrics"/>
    
    <li>Tracks execution statistics for SQL statements, servlet and JSP pages, dialogs/forms, and security.
    <li>Provides a browser-based repository for all project documentation alongside application code.<p>
	<table>
	    <tr>
		<td><@screenShot name="console-prj-folders"/></td>
		<td><@screenShot name="console-prj-files"/></td>
	    </tr>
	</table>
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

<h1>Console Tour</h1>
Check the Console Tour to explore these features in more detail.<p>

    <#list tour as app>
    <#if app.tourUrl?exists>
            ${app.tourName} (<a href="${app.tourUrl}" title="Online (HTML) Version of Console Tour">HTML</a>)
            <#if app.tourPDF?exists>
            (<a href="${app.tourPDF}" title="PDF Version of Console Tour">PDF</a>)
            </#if>
    </#if>
    </#list>
    