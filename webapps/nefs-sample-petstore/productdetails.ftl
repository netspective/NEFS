<#include "console/content/library.ftl"/>
<#assign queryResults = getQueryResultsAsMatrix("petstore.getProductDetail")/>

<TABLE cellSpacing=0 width="100%" 
background="${vc.getAppResourceUrl('/images/productdetails/bkg-topbar.gif')}" 
  border=0><TBODY>
  <TR>
    <TD><A 
      href="home"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/logo-topbar.gif')}" 
      border=0></A> </TD>
    <TD align=left>
      <FORM 
      action=/scripts/SaISAPI.dll/JPS112.class/petstore/control/search><FONT 
      color=cyan size=-1>What are you looking for?</FONT> <BR><INPUT size=14 
      name=search_text> <INPUT type=image 
      src="${vc.getAppResourceUrl('/images/productdetails/search.gif')}" border=0 
      name=search> </FORM></TD>
    <TD align=right><A onmouseover="img_on('cart')" 
      onmouseout="img_off('cart')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/cart"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/cart.gif')}" border=0 
      name=img_cart></A> <IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/separator.gif')}" border=0> <A 
      onmouseover="img_on('signin')" onmouseout="img_off('signin')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/signin"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/sign-in.gif')}" border=0 
      name=img_signin></A> <IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/separator.gif')}" border=0> <A 
      onmouseover="img_on('help')" onmouseout="img_off('help')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/help"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/help.gif')}" border=0 
      name=img_help></A> </TD></TR></TBODY></TABLE>
<TABLE height=20 cellSpacing=0 width="100%" 
background="${vc.getAppResourceUrl('/images/productdetails/white.htm')}" border=0>
  <TBODY>
  <TR>
    <TD align=middle><A onmouseover="img_on('fish')" 
      onmouseout="img_off('fish')" 
      href="category?category_id=FISH"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/fish.gif')}" border=0 
      name=img_fish></A> <IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/separator.gif')}" border=0> <A 
      onmouseover="img_on('dogs')" onmouseout="img_off('dogs')" 
      href="category?category_id=DOGS"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/dogs.gif')}" border=0 
      name=img_dogs></A> <IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/separator.gif')}" border=0> <A 
      onmouseover="img_on('reptiles')" onmouseout="img_off('reptiles')" 
      href="category?category_id=REPTILES"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/reptiles.gif')}" border=0 
      name=img_reptiles></A> <IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/separator.gif')}" border=0> <A 
      onmouseover="img_on('cats')" onmouseout="img_off('cats')" 
      href="category?category_id=CATS"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/cats.gif')}" border=0 
      name=img_cats></A> <IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/separator.gif')}" border=0> <A 
      onmouseover="img_on('birds')" onmouseout="img_off('birds')" 
      href="category?category_id=BIRDS"><IMG 
      src="${vc.getAppResourceUrl('/images/productdetails/birds.gif')}" border=0 
      name=img_birds></A> </TD></TR></TBODY></TABLE>
<TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top>
      <TABLE width=600 bgColor=white>
        <TBODY>
        <#foreach x in queryResults>
        <TR>
          <TD><FONT color=green size=5>${x[0]}</FONT></TD>
          <TD>$${x[3]}</TD>
          <#if x[4] = "BACK">
             <TD><FONT color=red>Back Ordered</FONT> </TD>
          <#else />
             <TD><FONT color=red>In Stock</FONT> </TD>
          </#if>
          <TD><A 
            href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/cart?action=purchaseItem&amp;itemId=EST-1"><IMG 
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

