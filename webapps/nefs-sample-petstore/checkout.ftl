<TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top><FONT color=black size=5>Shopping Cart:</FONT> 
      <P></P>
      <TABLE bgColor=white>
        <TBODY>
        <TR>
          <TD>
            <TABLE bgColor=#336666>
              <TBODY>
              <TR border="0" background="${vc.getAppResourceUrl('/images//bkg-topbar.gif')}">
                <TH><FONT color=white size=3>Item ID</FONT></TH>
                <TH><FONT color=white size=3>Product Name</FONT></TH>
                <TH><FONT color=white size=3>In Stock</FONT></TH>
                <TH><FONT color=white size=3>Unit Price</FONT></TH>
                <TH><FONT color=white size=3>Quantity</FONT></TH>
                <TH><FONT color=white size=3>Total Cost</FONT></TH></TR>
                
                <#assign total = 0/>
                <#assign items = vc.request.session.getAttribute("cartitems")/>
                <#assign keys = items?keys/>
                <#foreach key in keys>
                   ${vc.request.session.setAttribute("item_id",key)}
                   <#assign queryResults = getQueryResultsAsMatrix("petstore.getCartDetail")/>
                   <#foreach x in queryResults>
              <TR bgColor=#eeebcc>
                <TD>EST-18 </TD>
                <TD><A 
                  href="productdetails?item_id=${x[0]}">${x[1]}</A> </TD>
                      <#if x[2] = "IN">
                         <TD>yes</TD>
                      <#else>
                         <TD>no</TD>
                      </#if>
                      <TD>$#{x[3];m2M2}</TD>
                      <TD><INPUT size=4 value=#{items[key]} name=itemQuantity_${x[0]}> </TD>
                      <TD>$#{x[3]*items[key];m2M2}</TD></TR>
                      <assign total = total + (x[3]*items[key])/>
                  </#foreach>
                  </#foreach>
              <TR background="${vc.getAppResourceUrl('/images/bkg-topbar.gif')}">
                <TD><FONT color=white size=3>Total:</FONT></TD>
                <TD></TD>
                <TD></TD>
                <TD></TD>
                <TD></TD>
                <TD><FONT color=white 
          size=3>$#{total;m2M2}</FONT></TD></TR></TBODY></TABLE></TD></FORM></TR>
        <TR></TR></TBODY></TABLE>
      <P></P><A 
      href="placeorder"><IMG 
      alt=Continue src="${vc.getAppResourceUrl('/images/cart/button_cont.gif')}" border=0></A> </TD></TR>
  <TR>
    <TD vAlign=bottom></TD></TR>
  <TR>
    <TD vAlign=bottom>
      <TABLE cellSpacing=0 width="100%" border=0 <tr>
        <TBODY>
</TBODY></TABLE></TD></TR></TBODY></TABLE></BODY></HTML>

