The Schemas package allows almost all standard database schema objects like tables, columns, and data types (data
dictionaries) to be managed in a database-independent XML format. The same XML declarations can be used to generate
database-specific SQL DDL allowing a single XML source schema to work in a variety of SQL relational databases
(like Oracle, SQL Server, MySQL, etc.). Experienced DBAs are not required to create consistent, high-quality SQL DDL
during the design and construction phases of an application. Database-dependent objects like triggers and stored
procedures are not managed by Sparx and are created using existing means. Because almost all schema resources are
defined in XML, Sparx allows for re-use of Schemas across applications and different database vendors and produces and
maintains Schema documentation. Sparx encourages the creation and re-use of a set of data-types and table-types (the data
dictionary) that define standard behaviors for columns and tables. Data-types and table-types comprise the database
dictionary and can easily be inherited and extended. Sparx also generates database-independent Java Object-relational
classes. This is a called the Application DAL (Data Access Layer). Sparx can automatically generate a Java
Object-relational DAL for an entire schema, automating the majority of SQL calls by providing strongly-typed Java
wrappers for all tables and columns.