<#include "*/library.ftl"/>

<div class="textbox">

<h1>Introduction</h1>
Sparx is a comprehensive enterprise application framework which helps build web applications.
Design and prototyping, implementation, automatically generating unit tests, automatically creating implementation
documentation, and providing production logs and metrics are just some of the development deliverables and phases that
Sparx helps accelerate and standardize.

<h2>Sparx Features</h2>
<ul>
    <li>Instantly create sophisticated mission- and safety-critical web-based applications that run on <i>any application
        server</i>.</li>
    <li>Declare components for the presentation and data management layers in XML and have Sparx assemble up to 80% of
        your application for you with little or <i>no Java code</i>. All the Java code you write will be related to
        application functionality, not user interface (HTML) generation or database access.</li>
    <li>Write all your business logic using EJBs, rules processing systems, or plain Java objects and connect them to
        Sparx components like dialogs, fields, and schemas easily.</li>
    <li>Use the automatically-generated web services that change as the rest of your application changes -- as you add
        features, tables, forms, and functionality the web services will remain in sync.</li>
    <li>Declare your user interface forms, fields, validation rules, conditional logic, amd security policies using
        simple XML tags and get fully functional theme- and skin-based HTML output automatically.</li>
    <li>Declare your database structures and relationships using simple XML tags and get fully functional SQL DDL, SQL DML, HTML
        documentation and XML import/export capabilities automatically.</li>
    <li>Declare your database SQL and parameters using simple XML tags and get fully functional reports and synchronized
        and validated input capabilities automatically.</li>
    <li>Automatically produce programmer artificacts like HTML documentation, unit tests, functional specifications,
        and requirements traceability maps just by using XML tags and the Sparx API.</li>
</ul>

<@contentImage image='sparx-overview.gif'/>

<h2>Sparx Benefits</h2>
<ul>
    <li>Application developers spend time on real features significant to end-users instead of infrastructure issues.</li>
    <li>Technical managers can better manage their application development projects by utilizing the built-in project management, application documentation, unit-testing, and artifact-generation tools.</li>
    <li>Most of the user interface and database logic is coded in a declarative style using XML instead of a programmatic style using Java. This significantly reduces the amount of code (as much as 50-75% of code can be eliminated), increases re-use, maintains consistency across multiple projects, and improves code quality.</li>
    <li>Analysts can use the declarative user interface features to create prototypes that can later be completed by programmers (no more throw-away prototypes).</li>
    <li>Applications are built by assembling declared UI (forms/dialogs) and database (SQL) components combined with application-specific business logic using single or multiple distributed application tiers.</li>
    <li>Sparx is not a templating system that simply generates HTML but a feature-rich framework that significantly reduces the time to produce high-quality data-intensive thin-client applications.</li>
    <li>Sparx does not favor Servlets over JSPs or JSPs over Servlets and can work in one, the other, or both environments simultaneously with no loss of functionality in either environment.</li>
    <li>Implementation can be done using XML, Java, or both.</li>
    <li>Implements common design patterns like MVC and factories. Skins infrastructure allow identical business logic to be used across different user interfaces for a variety of browsers and platforms like handhelds.</li>
    <li>Sparx enhances and works equally well with all project development methodologies including waterfall, RAD, OOAD, and the agility of methodologies like eXtreme Programming (XP).</li>
</ul>

<h1>Sparx and its relationship to your application</h1>
Sparx is a pure Java library that resides <i>inside</i> your application, unlike other similar frameworks which are
containers for <i>your</i> application. The main difference is interoperability of frameworks. It is designed as an
enterprise framework that can stand-alone or enhance other COTS or in-house frameworks. Sparx consists of a single JAR
file containing the Sparx binary and a set of HTML and XML resources like XSLT style sheets, icons and an extensive
JavaScript library. This simple structure affords developers a great deal of flexibility in how they want to use Sparx.
Therefore, you do not have to redesign or recode your application as you would to comply with the limitations of a
framework container. Instead, you can start out by using a few Sparx features here and there and adopting more of the
Sparx ease and speed of development as the need arises.

<p>
<@contentImage image='sparx-relationship-to-app.gif'/>

<h1>What does Sparx produce?</h1>
Using simple XML declarations for the items on the left (schema, SQL, UI, etc.) Sparx produces a variety of objects.
Most of the components that Sparx produces are not <i>generated</i> but <i>instantiated</i>. For example, the XML is
translated at the startup of the application into objects that are cached, shared, and executed. Unlike most code
generators which create code that must be separately compiled, most of the Sparx code is automatically executed in
memory with no compile/link/debug cycle.
<p>
<@contentImage image='sparx-outputs.gif'/>

<h1>XML</h1>
The eXtensible Markup Language (XML) plays an important role in Sparx's ease of use, extensibility, and code generation.
Sparx declarations are performed using XML -- all dialogs, fields, validation rules, some conditional processing, all
SQL statements, dynamic queries, configuration files, database schemas, and many other resources are stored in XML
files that are re-usable across applications. Although XML is the preferred method for creating resource files, almost
anything that can be specified in XML can also be specified using the Sparx Java APIs. If you are not familiar with XML,
please visit <a href="http://www.xml.com/">http://www.xml.com/</a> for some training materials. Sparx uses the JAXP and
SAX standards for parsing and processing XML files. Sparx utilizes XML in a <i>declarative</i>, not <i>imperative</i>
manner. What this means is that XML is not used as yet another imperative programming or expression language like
Java, C/C++, or Pascal. Instead, it is simply used to declare classes, rules, specifications, and other application
requirements that are automatically parsed, read, cached, and executed by Sparx. The dynamic aspects of Sparx
applications comes from Java through the use of a simple Value Source interface (an implementation of the Value design
pattern), not a new programming language.

<h2>Sparx XML tags and its relationship to JSP taglibs</h2>
JSP tag libraries are usually used for emitting HTML. They are commonly use in the view component of the MVC paradigm.
Sparx XML tags are used to describe and declare all aspects of your application (UI, database, security, etc). Sparx can
use existing JSP tag libraries but they are seldom needed because Sparx will handle most tasks within its own, much
simpler, and much more powerful XML tags.

<h2>XML Performance</h2>
When engineers first learn about XML and the amount of flexibility it affords in both application and data management,
they jump at the chance to include XML features within their projects. However, soon they learn that dealing with XML
sometimes means sacrificing performance. With Sparx, all XML data is cached and only read when it changes. Basically,
all Sparx XML files are read using lazy-read approach; meaning, they are read only when needed and even then only once.
So, the majority of all Sparx XML performance impacts (if any) occur at the startup of a server-based application.
Once the application starts all data is cached and shared across users and XML-related performance issues are eliminated.

<h1>Web Services</h1>
The general topic of web services refers to the ability of applications and systems to speak to each other over
Internet protocols. The "normal" case of web applications has a customer accessing a catalog site and making
a purchase over a secure website. This interaction is quite common but sometimes it’s preferable to have a computer
system automatically place an order with other computer systems. For example, suppliers could provide web services to
large corporations so that corporations could automatically, without human intervention, place orders to the supplier
when their inventory runs low. Sparx supports both web <i>applications</i> (where a human being is interacting with an
application or computer system) and web <i>services</i> (where a service is being created for use by other computers).
Sparx allows the web services to automatically become applications and applications to automatically become services
with very little work on the part of analysts or programmers. For example, every Sparx form or dialog automatically
provides the capability for becoming a web service. Additionally, any table or SQL query defined using Sparx
automatically has the capability to run in both "application" and "service" modes.
</div>