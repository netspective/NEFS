<#include "console/content/library.ftl"/>
<#-- Logic to handle update for cart -->
<#-- loops through QueryString looking for textboxes name itemQuantity_ -->
<#-- get corresponding parameter value -->
<#-- builds new hash to be loaded into session variable cartitems -->
<#if !(vc.request.session.getAttribute("signedin")?exists)> 
   <#global signedin = false/>
<#else>
  <#if vc.request.session.getAttribute("signedin") = "yes">
     <#global signedin = true/>
  <#else>
      <#global signedin = false/>
  </#if>
</#if>
<#if vc.request.getParameter("action") = "updateCart">
  <#assign temp =  vc.request.session.getAttribute("cartitems")/>
  <#assign pNames = vc.request.getParameterNames()/>
  <#foreach item in pNames>
    <#if item[0..4] = "itemQ">
      <#assign value = vc.request.getParameterValues(item)/>
      <#if item?length = 18 > 
        <#assign itemP = item[13..17]/>
      <#else>
        <#assign itemP = item[13..18]/>
      </#if>
      
      <#if !(value[0]?string = "0") >
        <#if !hold?exists>
          <#assign hold = {itemP : value[0]?number}/>
        <#else>
         <#assign hold = hold + {itemP : value[0]?number}/>
        </#if>
      </#if>
    </#if>
  </#foreach>
   ${vc.request.session.setAttribute("cartitems",hold)}
<#elseif vc.request.getParameter("action") = "removeItem">
   <#assign temp =  vc.request.session.getAttribute("cartitems")/>
   <#assign keys = temp?keys/>
    
   <#foreach key in keys>
     <#if key !=  vc.request.getParameter("item_id")>
       <#assign val = temp[key]/>
       <#if !hold?exists>
         <#assign hold = {key : val}/>
       <#else>
         <#assign hold = hold + {key : val}/>
       </#if>
     </#if>
   </foreach>
   ${vc.request.session.setAttribute("cartitems",hold)}
<#else>
<#-- Logic to create initial and subsequent cart entries -->
<#-- builds hash entries and loads into sesion variable -->
<#-- if a duplicate item exists it's quantity is incremented by 1 -->
  <#global item = {vc.request.getParameter("item_id"):1}/>
  <#if !vc.request.session.getAttribute("cartitems")?exists> 
   ${vc.request.session.setAttribute("cartitems",item)}
  <#else>
   <#assign temp =  vc.request.session.getAttribute("cartitems")/>
   <#assign found = "no"/>
   <#assign keys = temp?keys/>
    
   <#foreach key in keys>
     <#if key =  vc.request.getParameter("item_id")>
       <#assign val = temp[key] + 1/>
       <#assign found = "yes"/>
     <#else/>
     <#assign val = temp[key]/>
     </#if>
     <#if !hold?exists>
       <#assign hold = {key : val}/>
     <#else>
       <#assign hold = hold + {key : val}/>
     </#if>
   </foreach>
   <#if found = "no">
   <#assign hold =   hold + {vc.request.getParameter("item_id"):1}/>
   </#if>
   ${vc.request.session.setAttribute("cartitems",hold)}
</#if>
</#if>
<SCRIPT language=JavaScript>
  // change image to name_on 
  function img_on(name) {
    if (document.images) 
      document['img_'+name].src = eval('img_'+name+'_on.src');
  }

  // change image to name_off
  function img_off(name) {
    if (document.images)
      document['img_'+name].src = eval('img_'+name+'_off.src');
  }
</SCRIPT>

<SCRIPT language=JavaScript>

<!--
// load all our images here
if (document.images) {

  img_help_off = new Image();
  img_help_off.src = "${vc.getAppResourceUrl('/images/help.gif')}";
  img_help_on = new Image();
  img_help_on.src = "${vc.getAppResourceUrl('/images/help.gif')}";

  img_signin_off = new Image();
  img_signin_off.src = "${vc.getAppResourceUrl('/images/sign-in.gif')}";
  img_signin_on = new Image();
  img_signin_on.src = "${vc.getAppResourceUrl('/images/sign-in.gif')}";

  img_signout_off = new Image();
  img_signout_off.src = "${vc.getAppResourceUrl('/images/sign-out.gif')}";
  img_signout_on = new Image();
  img_signout_on.src = "${vc.getAppResourceUrl('/images/sign-out.gif')}";

  img_search_off = new Image();
  img_search_off.src = "${vc.getAppResourceUrl('/images/search.gif')}";
  img_search_on = new Image();
  img_search_on.src = "${vc.getAppResourceUrl('/images/search.gif')}";

  img_myaccount_off = new Image();
  img_myaccount_off.src = "${vc.getAppResourceUrl('/images/my_account.gif')}";
  img_myaccount_on = new Image();
  img_myaccount_on.src = "${vc.getAppResourceUrl('/images/my_account.gif')}";

  img_cart_off = new Image();
  img_cart_off.src = "${vc.getAppResourceUrl('/images/cart.gif')}";
  img_cart_on = new Image();
  img_cart_on.src = "${vc.getAppResourceUrl('/images/cart.gif')}";

  img_fish_off = new Image();
  img_fish_off.src = "${vc.getAppResourceUrl('/images/fish.gif')}";
  img_fish_on = new Image();
  img_fish_on.src = "${vc.getAppResourceUrl('/images/fish.gif')}";

  img_dogs_off = new Image();
  img_dogs_off.src = "${vc.getAppResourceUrl('/images/dogs.gif')}";
  img_dogs_on = new Image();
  img_dogs_on.src = "${vc.getAppResourceUrl('/images/dogs.gif')}";

  img_reptiles_off = new Image();
  img_reptiles_off.src = "${vc.getAppResourceUrl('/images/reptiles.gif')}";
  img_reptiles_on = new Image();
  img_reptiles_on.src = "${vc.getAppResourceUrl('/images/reptiles.gif')}";

  img_cats_off = new Image();
  img_cats_off.src = "${vc.getAppResourceUrl('/images/cats.gif')}";
  img_cats_on = new Image();
  img_cats_on.src = "${vc.getAppResourceUrl('/images/cats.gif')}";

  img_birds_off = new Image();
  img_birds_off.src = "${vc.getAppResourceUrl('/images/birds.gif')}";
  img_birds_on = new Image();
  img_birds_on.src = "${vc.getAppResourceUrl('/images/birds.gif')}";
}

// -->
</SCRIPT>
<TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top>
      <P>
      <TABLE border=0>
        <TBODY>
        <TR>
          <TD width="15%"></TD>
          <TD align=left><FONT color=black size=5>Shopping Cart:</FONT> 
            <P>
            <FORM action=cart><INPUT type=hidden value=updateCart name=action>
            <INPUT type=hidden value=all name=item_id> 
            <TABLE bgColor=white>
              <TBODY>
              <TR>
                <TD>
                  <TABLE bgColor=#336666>
                    <TBODY>
                    <TR border="0" background="${vc.getAppResourceUrl('/images/bkg-topbar.gif')}">
                      <TH><!-- for the remove column --></TH>
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
                      <TD><A 
                        href="cart?action=removeItem&amp;item_id=${x[0]}"><IMG 
                        alt="Remove Item From Shopping Cart" 
                        src="${vc.getAppResourceUrl('/images/cart/button_remove.gif')}" border=0></A> </TD>
                      <TD>${x[0]} </TD>
                      <TD><A 
                        href="productdetails?item_id=${x[0]}">${x[1]}</A> 
                      </TD>
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
                      <TD></TD>
                      <TD><FONT color=white size=3>Total:</FONT></TD>
                      <TD></TD>
                      <TD></TD>
                      <TD></TD>
                      <TD></TD>
                      <TD><FONT color=white 
                  size=3>$#{total;m2M2}</FONT></TD></TR>
                  </TBODY></TABLE></TD>
                <TD><INPUT type=image src="${vc.getAppResourceUrl('/images/cart/cart-update.gif')}" 
                  border=0 name=update> </TD></FORM></TR>
              <TR></TR>
              <TR>
                <TD colSpan=6><BR><A 
                  href="checkout"><IMG 
                  alt="Proceed To Checkout" src="${vc.getAppResourceUrl('/images/cart/button_checkout.gif')}" 
                  border=0></A> 
      </TD></TR></TBODY></TABLE></P></TD></TD></TR></TBODY></TABLE></P></TD></TR>
  <TR>
    <TD vAlign=bottom></TD></TR>
  <TR>
    <TD vAlign=bottom>
      <TABLE cellSpacing=0 width="100%" border=0 <tr>
        <TBODY>
</TBODY></TABLE></TD></TR></TBODY></TABLE>

