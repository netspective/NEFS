<h1>I have an application server, isn't that sufficient?</h1>
Most J2EE application servers contain a great deal of common infrastructure code that helps contain, manage, deploy
and execute applications. They also have features the increase scalability, reliability and availability of
applications by providing features like clustering and failover.
<p>
What they lack, because it's not part of their product
requirements, is the ability to help build the applications they contain. Just as an operating system is a container
for executing applications written in any language but does not assist in the development of the applications, a J2EE
application server is a container for executing Java and J2EE applications but does not assist in the development of
the Java/J2EE applications.
<p>
NEFS, as a J2EE framework that sits atop an app server, helps create the applications that you can then deploy onto any
J2EE container or application server.

<h1>Why not use the frameworks that come with my application server?</h1>
Many commercial embedded frameworks are provided by application server vendors like WebLogic or WebSphere. The main
issues you'll face with these frameworks are:

<ul>
    <li>They are usually vendor-specific and tie you to a particular application server, operating system, or database server
    <li>The frameworks are not designed for complete coverage of sophisticated applications and engineers end up having to do a great deal of infrastructure work on their own.
    <li>Unlike <a href="${servletPath}/products/frameworks/features">NEF's feature list</a>, most embedded application server frameworks are very light on functionality.
</ul>
With its unique architecture, NEF can supplement or replace your application server vendor's frameworks with a
good mix of easy to use components for faster development and the sophistication that senior architects need to create
robust applications. Also, keep in mind that all NEF-based applications are application-server, database-server, and
operating-system independent.
