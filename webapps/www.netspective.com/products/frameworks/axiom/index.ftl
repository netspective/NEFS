<h1>The Axiom Data Service</h1>
Data management tasks that took hours and days using plain J2EE APIs can now be finished in minutes using Axiom's
J2EE wrapper APIs. Without forcing you to drop down to Java, Axiom is able to completely bind and execute your static
and dynamic SQL by using just a few simple lines of XML. Even legacy data can be brought in by using Axiom's built-in SQL
    generator saving time and making the entire process simple.

<ul class="check-list">
    <li>Use any relational database server to generate add/edit/delete
        SQL that tie into the Sparx Presentation Framework.</li>
    <li>Axiom can often shave weeks, if not months, of SQL data development tasks and
        your code is less prone to error caused by repetitive tasks.
    </li>
    <li>Automatic performance statistics gathering and externalization of SQL from your application code leaves you
        with less brittle code.</li>
</ul>

<h1>Externalize SQL Code into SQL Resource Libraries</h1>
Instead of storing dynamic and static SQL in <code>.java</code> files, Axiom encourages separating SQL from Java code
by providing XML-based storage of simple or complex SQL.

<ul class="check-list-in-body">
    <li>By externalizing the SQL, you can optimize and modify SQL without changing Java code or recompiling.
    <li>SQL in XML supports bind parameters and prevents security errors introduced by SQL injection in Java code.
    <li>Zero-Java SQL reporting allows you to define SQL, bind parameters, and report formats in XML so that you can
        generate reports based on SQL using pure XML and no Java. Of course, customizations are still done in plain Java.
    <li>All externalized SQL still remain internally as first-class objects with support for statistics collections
        (like seeing how many times particular SQL is executed, how long it takes, etc.).
</ul>

<h1>Forget About Writing DAOs Manually</h1>
Once you supply your SQL in XML files, all your Data Access Objects (DAOs) are automatically generated for you.
As your XML files change, the Java code is kept in synch with no manual intervention.

<h1>Manage Both Database Structure (Schemas) and Content</h1>
Your entire database structure, including tables, columns, indexes, and other database objects can be declared in
a vendor-independent XML meta-data format.
<ul class="check-list-in-body">
    <li>Once your schema is defined (or reverse engineered) into XML, you can generate SQL DDL and type-safe table-based
        DAOs.
    <li>All XML database schemas have automatic HTML-based documentation.
    <li>Zero-Java record management allows you to connect your XML database schema declarations directly to Sparx or
        other presentation frameworks so that your database and forms are kept in synch.
</ul>
<img src="${resourcesPath}/images/products/frameworks/axiom-outputs.gif"/>

<h1>Tie XML Import/Export Functionality to your Schema</h1>
Once your database schemas are modeled using XML, you can import and export data to and from your database instances using
structured XML instead of flat ASCII files. If your schema has parent/child or other relationships in tables then the
XML will automatically map to those structures with no Java code.

<h1>Connect to Multiple Data Sources</h1>
Any SQL you write can connection to multiple data sources simulataneously. This is useful if you have the same or
different SQL code that needs to be run on multiple, cross-vendor, databases at the same time within the same application.

<h1>Isolate Vendor-specific Database Functionality</h1>
Each SQL query you write in XML is uniquely identified in an auto-generated DAO. Using the same identifier, you can
supply different SQL statements to take advantage of vendor-specific extensions or optimizations. Then, when you ask
Axiom to execute a DAO it will automatically select the proper SQL based on which database it is connecting to at the time.

<h1>Leave Connection Pooling to Axiom or your App Server</h1>
Axiom supports its own internal JNDI-based connection pools or can delegate connection pooling to your application
server. Using the internal connection pool still allows statistics gathering and finding connection leaks but keeps
your application app-server-neutral. However, a single configuration option allows you to use your app server's or your
own connection pool.

<h1>Overview of Axiom Tags</h1>
Axiom defines and processes numerous, very high-level, tags that you use to declare data management
components. The <code>&lt;presentation&gt;</code> tag is optional and may be ignored if you want
clean separation between your UI and data layers.<p>
<img src="${resourcesPath}/images/products/frameworks/suite/database-tags.gif"/>


<h1>Summary of Axiom Features</h1>
<ul class="check-list-in-body">
    <li>Schema management
	<li>Lightweight Data Access Objects (DAOs)
	<li>Object-relational mapping and persistence
	<li>Connection pooling
	<li>SQL DML generation
	<li>SQL DDL generation
	<li>Static SQL queries library
	<li>Dynamic SQL queries library
	<li>HTML design documentation
	<li>Dynamic data sources
	<li>SQL performance statistics
	<li>Transaction management and isolation
</ul>