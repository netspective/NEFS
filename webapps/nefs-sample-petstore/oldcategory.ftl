<#include "console/content/library.ftl"/>

<#assign queryResults = getQueryResultsAsMatrix("petstore.getProducts")/>

   
  
   
<table border="1">
<#foreach x in queryResults>
<tr>
<td>
${x[1]}<br>
</td><td>
${x[2]}
</td></tr>
</foreach>
</table>
