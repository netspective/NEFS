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

<#assign queryResults = getQueryResultsAsMatrix("petstore.getProductDetail")/>


<TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top>
      <TABLE width=600 bgColor=white>
        <TBODY>
        <#foreach x in queryResults>
        <TR>
          <TD><FONT color=green size=5>${x[0]}</FONT></TD>
          <TD>$#{x[3];m2M2}</TD>
          <#if x[4] = "BACK">
             <TD><FONT color=red>Back Ordered</FONT> </TD>
          <#else />
             <TD><FONT color=red>In Stock</FONT> </TD>
          </#if>
          <TD><A 
            href="cart?action=purchaseItem&amp;item_id=${x[5]}"><IMG 
            alt="Add Item to Your Shopping Cart" 
            src="${vc.getAppResourceUrl('/images/productdetails/button_cart-add.gif')}" 
            border=0></A> </TD></TR>
        <TR>
          <TD colSpan=3><IMG 
            src="${vc.getAppResourceUrl('images/productdetails/${x[2]}')}">${x[1]}
            </TD></TR>
            </foreach></TBODY></TABLE></TD></TR>
  <TR>
    <TD vAlign=bottom></TD></TR>
  <TR>
    <TD vAlign=bottom>
      <TABLE cellSpacing=0 width="100%" border=0 <tr>
        <TBODY>
</TBODY></TABLE></TD></TR></TBODY></TABLE></BODY></HTML>

