<#include "console/content/library.ftl"/>

<#if !(vc.request.session.getAttribute("signedin")?exists)> 
   <#global signedin = false/>
<#else>
  <#if vc.request.session.getAttribute("signedin") = "yes">
     <#global signedin = true/>
  <#else>
      <#global signedin = false/>
  </#if>
</#if>

<#assign queryResults = getQueryResultsAsMatrix("petstore.getProducts")/>
<#assign productName = getQueryResultsAsMatrix("petstore.getProductName")/>

<TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top>
      <P><FONT color=green size=5>${vc.request.getParameter("category_id")}</FONT> 
      <P>
      <TABLE bgColor=#336666 border=0>
        <TBODY>
        <TR background="${vc.getAppResourceUrl('/images/bkg-topbar.gif')}">
          <TH><FONT color=white size=3>Product ID</FONT></TH>
          <TH><FONT color=white size=3>Product Name</FONT></TH></TR>
        <#foreach x in queryResults>
        <TR bgColor=#eeebcc>        
        <TD>${x[0]}<br></TD>
        <TD><A 
          href="product?product_id=${x[0]}&product_name=${x[1]}">${x[1]}
        
        </A></TD></TR>
        </foreach>
        <TR>          

</TR></TBODY></TABLE></P></TD></TR>
  <TR>
    <TD vAlign=bottom></TD></TR>
  <TR>
    <TD vAlign=bottom>
      <TABLE cellSpacing=0 width="100%" border=0 <tr>
        <TBODY>
</TBODY></TABLE></TD></TR></TBODY></TABLE>
