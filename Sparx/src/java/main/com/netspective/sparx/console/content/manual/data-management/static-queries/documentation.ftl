<div class="textbox">

<h1>What's the difference?</h1>
Simple scenario
<p>
Let’s say a programmer has to query a database table and return the selected rows as a collection of JAVA objects.
<p>
Manual Code
<ol>
  <li>Obtain a database connection
  <li>Construct appropriate SQL statement
  <li>Execute SQL statement
  <li>Loop over Result Set(s).
  <li>For every row returned from the database, check if column is null or not
  <li>Appropriately set the resulting column value into Object’s data member.
  <li>Repeat step 6 for every column in the result set.
  <li>Set the resultant object in a collection.
  <li>Repeat steps 4 through 8 for every row in the result
  <li>Return collection to calling code.
</ol>
Axiom Code TO DO<br>
1. Obtain a database connection using blah blah...The obtained connection is a PooledConnection. Programmer has to do practically no work to accomplish this step.
2. Construct appropriate SQL statement in XML


<h1>Customization</h1>
There are numerous customization and configuration options available for each SQL statement. Each query can supply
alternate SQL for different databases (e.g. one SQL fragment for Oracle versus another for SQL*Server). Also, each
query can act as a template for SQL injection (which can rewrite the query based on value sources).

<h1>Data Sources</h1>
Each SQL statement can specify either no data source (meaning the data comes from the default data
source), a static data source, or a dynamic data source (meaning the data comes from a source that will be determined
automatically based on some rule).

</div>