<h1>Overview</h1>

The primary goal for the NEFS 7.0 upgrade was to move from a special-purpose single-product (Sparx) to a general-purpose
suite of frameworks called the <i>Netspective Enterprise Frameworks (NEF) Suite</i>. The suite is now comprised of the following products:

<ul>
  <li><b>The Axiom Relational Data Management Service</b>. Sparx's original static query, dynamic query (query definitions), and schema management functionality are now available in Axiom. It's called a <i>Service</i> instead of a <i>Framework</i> because it can co-exist peacefully with other persistence frameworks.</li>
  <li><b>The Commons Core Library</b>. Sparx's general purpose classes for access control, configuration management, value sources, data validation, and XML parsing are now available in Commons.</li>
  <li><b>The Sparx Application Platform</b> - the presentation and application management framework. The navigation, dialogs, reports, and other complex UI functionality remains in Sparx.</li>
  <li><b>The Medigy Clinical and Healthcare Informatics Platform</b> - the medical applications development framework.</li>
  <li><b>The Junxion Web Services Platform</b> - the EDI and Web Services Package. This is a new library and more announcements will be made later.</li>
</ul>

<h1>Enterprise Frameworks Suite Upgrade Goals</h1>
<ul>
  <li>Be able to create web-based user interfaces that look as great as they work. Support for multiple themes and skins should be simple.</li>
  <li>Lower learning curve -- programmers should be able to learn the frameworks and get up and running in half the time required by previous Sparx releases.
  <li>Improved error handling -- developers should have much less trouble locating XML tag errors and fixing them.
  <li>Increased ease of use -- provide a better console and additional consistency across all aspects of application development (presentation, application, frameworks) so that the NEF Suite becomes easier to use.
  <li>Increased ease of deployment
  <li>Increased extensibility -- every class should be easily inherited or composited into user classes.
  <li>Improved documentation
  <ul>
    <li>Built-in documentation where possible
    <li>Documentation that stays up to date when code is extended
    <li>Documentation built into Console
  </ul>
  <li>Improved code modularity (smaller, more manageable frameworks that are fully integrated)
  <li>Improved quality through better unit, integration, performance testing
  <li>Backward compatibility is not a goal and existing code will definitely break (but not much); however, XSLT stylesheets will be provided for XML conversions
</ul>

<h1>Package migrations from Sparx to NEF</h1>

<ul>
  <b>Commons Core Library</b>
  <li>XDM (XML Data Model)
  <li>Values
  <li>Value Sources
  <li>Access Control Lists (ACLs) and Security
  <li>Commands
  <li>Configuration
  <li>Tabular Reports
  <li>Java Expressions and Text Management
  <li>Data Validation
</ul>

<ul>
  <b>Axiom Relational Data Management Service</b>
  <li>Connection Management
  <li>Database Policies
  <li>Query (Statements)
  <li>Query Definitions
  <li>Schema
  <li>Data Access Layer (DAL)
  <li>Ant tasks, value sources, etc related to data management
</ul>

<ul>
  <b>Sparx Application Platform</b>
  <li>Console (no longer called ACE)
  <li>Dialogs (custom, Query Definition, Query Select Dialogs)
  <li>HTML Tabular Reports
  <li>Themes and Skins
  <li>Panels
  <li>Navigation Trees
  <li>Scrollable data sources
  <li>Ant tasks, value sources, commands, etc related to presentation layer
</ul>

<h1>Major technical differences between Sparx and NEF</h1>

<ul>
  <li>Unified object model - the same XML input source can hold all the various components - configuration, ACLs, forms, schemas, queries, etc in a single or multiple XML files (choice is up to the programmer, not the framework).
  <li>Reflection is used to generate the XML tags automatically (meaning almost everything that can be constructed through Java may be constructed through XML or vice-versa).
  <li>100% of framework may be used through Java (with or without XML).
  <li>Every tag in all frameworks is a plain java object and every object may be extended by subclassing or delegation
  <li>The basis for most of the changes (and the reason for the rewrite) is known as the XDM (XML Data Model)
  <li>Front controller design pattern in Sparx is now the default.
  <li>Multiple templating systems are supported (JSPs, Velocity, Freemarker, etc)
  <li>All factories have object registration done in XML configuration files that are located by resource loading
  <li>Unlike ACE (which was a "special" Sparx app), the Console is now a full Sparx application (written exactly how an end-user should write a Sparx app – best practices, structure, etc).
</ul>

<h1>The Netspective XML Data Model (XDM)</h1>

<ul>
  <li>Based on the concepts pioneered by the the Ant task model
  <li>NEF Uses SAX for XML parsing (not DOM) – much faster than Sparx XML parsing (e.g what took about 70 seconds now takes 4)
  <li>Each class becomes a tag
  <li>Each mutator method (setXXX) becomes an attribute
  <li>Each attribute automatically uses Java type model to assign text values to valid Java types (int, float, etc)
  <li>A child tag that matches a mutator name is the same as setting the mutator
  <li>Tag templates and tag inheritance is built into the XDM
  <li>Certain tags may contain or ignore PC Data
  <li>Tags and attributes may be ignored based on definitions in classes
</ul>
