<div class="textbox">
    Welcome to the Netspective Enterprise Console for your web application (<code>${vc.servletContext.servletContextName}</code>).
    <p>
    The Console is a Sparx servlet that provides a browser-based administrative interface to all of the dynamic
    Netspective Enterprise Frameworks Suite (NEFS) components and objects associated with your application. The Console is
    automatically available to all NEFS-based applications during development and can be optionally turned off in
    production (for security).
    <p>
    Because the Console provides full access to all aspects of your applications, it's important to ensure that it is
    appropriately secured before the application is put into production use. The Console may be secured by using the
    following methods:
    <ul>
        <li>Use the Console's built-in single password security model.
        <li>Use Servlet filters that can be wrapped around the ConsoleServlet to provide a custom authentication scheme.
        <li>Disable the ConsoleServlet completely by commenting it out in the application's web.xml file
        (<code>${vc.servletContext.getRealPath('WEB-INF/web.xml')}).
    </ul>
</div>