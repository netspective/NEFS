<#include "*/macros.ftl"/>

<@panel heading="Welcome">
    <table width="100%" border="0" cellspacing="2" cellpadding="10">
        <tr>
            <td width="150">
                <div align="left">
                    <img src="${vc.getAppResourceUrl('/images/sampler-large.gif')}" alt="" align="left" border="0"></div>
            </td>
            <td width="90%">
                <div class="textbox">
                    <b>Welcome to the Netspective Enterprise Frameworks Suite (NEFS) Sampler.</b>
                    <p>
                    The purpose of this application is to give you an idea of how easy it is to build J2EE web
                    applications using the powerful building blocks provided by our Java Frameworks. Although we start
                    with simple NEFS usage examples, as you learn more about Sparx and Axiom you will see that
                    flexibility and extensibility are also a part of their attributes.
                    <p>
                    <b>Note</b>: the terms <i>dialog</i> and <i>form</i> will be used interchangeably in the Sampler
                                 because they mean the same thing.
                </div>
            </td>
        </tr>
    </table>
</@panel>
<p>
<@panel heading="Exploring The Sampler">
    <@pageChildren page=vc.navigationContext.ownerTree.root/>
    <p>
    <table width="100%" border="0" cellspacing="2" cellpadding="10">
        <tr>
            <td valign="top" align="center">
                <img src="${vc.getThemeResourceUrl('/images/xml-source.gif')}" border="0">
            </td>
            <td width="100%" class="textbox">
                <b>Viewing XDM Source Code</b><br>
                If you see this icon (on the left) you can view the Netspective Xml Data Model (XDM) source code
                for various objects on the page. You will find this icon at the bottom of each page where you can
                see what XML is required to create the page you're viewing. The icon also appears under various
                panels (like forms/dialogs) to see their code as well.
            </td>
        </tr>
        <tr>
            <td valign="top" align="center">
               <img src="${vc.getAppResourceUrl('/images/console-large.gif')}" border="0">
            </td>
            <td width="100%" class="textbox">
                <b><a href="${vc.consoleUrl}?_dc.userId=console&_dc.password=console">Exploring the Console</a></b><br>
                The Sampler, like all apps built using NEFS, can be viewed with the <i>Netspective Enterprise Console</i> servlet.
                Each application has a private instance of the Console using <code>http://[server]/[appName]/console</code>. When you log into the Console for
                Application X (<code>appX/console</code>) versus Y (<code>appY/console</code>)
                you will only see components for the appropriate application.
                <p>
                The login for Console in every sample app is the same: user is '<code><font color=red>console</font></code>'
                and the password is also '<code><font color=red>console</font></code>'.
                <p>
                The browser based Console can be used during your development process to monitor your code's functionality as
                well as comprehensive unit testing and documentation of code artifacts.
                <p>

                Try some other Sampler links:
                <ul>
                    <li>
                    <a href="${vc.consoleUrl}?_dc.userId=console&_dc.password=console">See the Console</a> for this NEFS Sampler application.
                    </li>
                    <li>
                        Review the Sampler project source file <@projectFile/>.
                    </li>
                    <li>
                        ${vc.getConsoleFileBrowserLinkShowAlt(vc.servlet.servletContext.getRealPath(''), "Browse")}
                        all the Sampler source directories and files.
                    </li>
                </ul>
            </td>
        </tr>
    </table>
</@panel>
