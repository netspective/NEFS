The Static Queries package allows all SQL statements and dynamic parameters used in a project to be declared in XML. Once
defined, a single or multiple SQL statements may be used in reports, dialogs (forms), Servlets, or templates, JSP-pages.
In many cases, SQL statement pooling completely replaces simple data-serving beans since data objects are automatically
created for all SQL statements. Data can be easily aggregated from multiple data sources because each SQL statement in
the statement pool can be specified (either in XML or JSP) to come from a variety of pre-defined or dynamic data sources.

In addition to to basic SQL management (like prepare, bind parameters, and execute) the Sparx static queries objects
automatically maintain peformance and usage statistics.
