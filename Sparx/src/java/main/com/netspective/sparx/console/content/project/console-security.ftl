<div class="textbox">
    Because the Console provides full access to all aspects of your applications, it's important to ensure that it is
    appropriately secured before the application is put into production use. The Console may be secured by using the
    following methods:
    <ul>
        <li>Use the Console's built-in security model.
        <li>Use Servlet filters that can be wrapped around the ConsoleServlet to provide a custom authentication scheme.
        <li>Disable the ConsoleServlet completely by commenting it out in the application's
        <a href="application/configuration/servlet">J2EE Servlet Deployment Descriptor</a> file).
    </ul>
</div>