All Sparx database features are designed to work across various databases like Oracle, Sybase/SQLServer, DB2,
HypersonicSQL, InstantDB, PostgreSQL, and MySQL. The support is provided through the use of the DatabasePolicy
interface (which defines methods that return database-specific information). It is easy to register new
DatabasePolicy objects for databases that are not supported right out of the box. All SQL generation, reading, and
writing is done through the DatabasePolicy interface so that vendor-specific features are still allowed while
database independence is maintained in the XML declarations.