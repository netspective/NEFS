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
<#assign queryResults = getQueryResultsAsMatrix("petstore.getItems")/>

<TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top>
      <P><FONT color=green size=5>${vc.request.getParameter("product_name")}</FONT> 
      <P>
      <TABLE bgColor=#336666 border=0>
        <TBODY>
        <TR background="${vc.getAppResourceUrl('/images/bkg-topbar.gif')}">
          <TH><FONT color=white size=3>Item ID</FONT></TH>
          <TH><FONT color=white size=3>Item Name</FONT></TH>
          <TH><FONT color=white size=3>Item Price</FONT></TH></TR>
        <#foreach x in queryResults>
        <TR bgColor=#eeebcc>
          <TD>${x[0]}</TD>
          <TD><A 
            href="productdetails?item_id=${x[0]}"> 
            ${x[2]}</A> </TD>
          <TD>$#{x[1];m2M2}</TD>
          <TD><A 
            href="cart?action=purchaseItem&amp;item_id=${x[0]}"><IMG 
            alt="Add Item to Your Shopping Cart" 
            src="${vc.getAppResourceUrl('/images/product/button_cart-add.gif')}" 
            border=0></A> </TD></TR></A>
        <P></P>
       <TR></TR>
       </foreach>
       </TBODY></TABLE></P></TD></TR>
  <TR>
    <TD vAlign=bottom></TD></TR>
  <TR>
    <TD vAlign=bottom>
      <TABLE cellSpacing=0 width="100%" border=0 <tr>
        <TBODY>
</TBODY></TABLE></TD></TR></TBODY></TABLE>

