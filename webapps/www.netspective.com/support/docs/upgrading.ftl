<#include "/downloads/frameworks/releases.ftl"/>

NEF has been designed to be easy to upgrade. Typically, all that is required to upgrade any NEF-based application is to
get a copy of the new JAR files and drop them into the <code>APP_ROOT/WEB-INF/lib</code> directory for the application. Because
Sparx uses web resources such as JavaScript (.js), graphics (.gif, .jpg, etc), and templates (.jsp, .html, etc.) you
will need to run one extra step which will extract web resources from the Sparx JAR file and make them available to your
application server.
<p>
<b>Not sure what version you're running?</b> If you're not sure what versions of the Frameworks are in use
within your applications, simply go to your application's Console (<code>http://server/yourapp/console</code>)
Home page and review the contents of "Versions" panel in the bottom left portion of the page.
<p>
<b>Need to rollback to an earlier version?</b> Visit the <a href="${servletPath}/downloads/suite">Downloads</a> page
to pick any previous release.
<p>

<h1>Upgrading to Commons ${latestCommonsVersion}</h1>
<ol>
    <li><a href='${latestCommonsJarDownloadHref}'>Download</a> the latest version.</li>
    <li>Save the file as <code><b>netspective-commons.jar</b></code> (no version number) into your <code>APP_ROOT/WEB-INF/lib</code> directory if it's a web application or into your CLASSPATH for other application types.</li>
    <li>Restart your application.</li>
</ol>

<h1>Upgrading to Axiom ${latestAxiomVersion}</h1>
<ol>
    <li><a href='${latestAxiomJarDownloadHref}'>Download</a> the latest version.</li>
    <li>Save the file as <code><b>netspective-axiom.jar</b></code> (no version number) into your <code>APP_ROOT/WEB-INF/lib</code> directory if it's a web application or into your CLASSPATH for other application types.</li>
    <li>Restart your application.</li>
</ol>

<h1>Upgrading to Sparx ${latestSparxVersion}</h1>
<ol>
    <li><a href='${latestSparxJarDownloadHref}'>Download</a> the latest version.</li>
    <li>Save the file as <code><b>netspective-sparx.jar</b></code> (no version number) into your <code>APP_ROOT/WEB-INF/lib</code> directory.</li>
    <li>Restart your application.</li>
    <li>Go to your application's Console (<code>http://server/yourapp/console</code>).</li>
    <li>Click the <i>Project</i> Tab.</li>
    <li>Click the <i>Ant Build</i> Tab under the <i>Project</i> Tab.</li>
    <li>Click the <i>NEFS/Sparx</i> Tab on the left side.</li>
    <li>Choose the <code>install-sparx-resources-into-app</code> target to execute.</li>
    <li>
        Click OK to have the Sparx resources that are stored in the <code>${latestSparxJar}</code> copied into your <code>APP_ROOT/sparx</code> directory.
    </li>
</ol>
<b>Note</b>: The <code>APP_ROOT/sparx</code> directory is a private Sparx directory and you should not alter the contents
of that directory because when you upgrade Sparx in the future they will be overwritten. If you need to override
Sparx files, simply place them in your <code>APP_ROOT/resources/sparx</code> folder because the contents of that
folder are checked before <code>APP_ROOT/sparx</code>. As long as the filename and relative structure are consistent
you can override any Sparx resource with your own.
