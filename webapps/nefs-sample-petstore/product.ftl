<#include "console/content/library.ftl"/>
<#assign queryResults = getQueryResultsAsMatrix("petstore.getItems")/>

<TABLE cellSpacing=0 width="100%" 
background="${vc.getAppResourceUrl('/images/product/bkg-topbar.gif')}" border=0>
  <TBODY>
  <TR>
    <TD><A 
      href="home"><IMG 
      src="${vc.getAppResourceUrl('/images/product/logo-topbar.gif')}" border=0></A> </TD>
    <TD align=left>
      <FORM 
      action=/scripts/SaISAPI.dll/JPS112.class/petstore/control/search><FONT 
      color=cyan size=-1>What are you looking for?</FONT> <BR><INPUT size=14 
      name=search_text> <INPUT type=image 
      src="${vc.getAppResourceUrl('/images/product/search.gif')}" border=0 name=search> 
      </FORM></TD>
    <TD align=right><A onmouseover="img_on('cart')" 
      onmouseout="img_off('cart')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/cart"><IMG 
      src="Product Category_Angelfish_files/cart.gif')}" border=0 
      name=img_cart></A> <IMG 
      src="${vc.getAppResourceUrl('/images/product/separator.gif')}" border=0> <A 
      onmouseover="img_on('signin')" onmouseout="img_off('signin')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/signin"><IMG 
      src="${vc.getAppResourceUrl('/images/product/sign-in.gif')}" border=0 
      name=img_signin></A> <IMG 
      src="${vc.getAppResourceUrl('/images/product/separator.gif')}" border=0> <A 
      onmouseover="img_on('help')" onmouseout="img_off('help')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/help"><IMG 
      src="${vc.getAppResourceUrl('/images/product/help.gif')}" border=0 
      name=img_help></A> </TD></TR></TBODY></TABLE>
<TABLE height=20 cellSpacing=0 width="100%" 
background="${vc.getAppResourceUrl('/images/product/white.htm')}" border=0>
  <TBODY>
  <TR>
    <TD align=middle><A onmouseover="img_on('fish')" 
      onmouseout="img_off('fish')" 
      href="category?category_id=FISH"><IMG 
      src="${vc.getAppResourceUrl('/images/product/fish.gif')}" border=0 
      name=img_fish></A> <IMG 
      src="${vc.getAppResourceUrl('/images/product/separator.gif')}" border=0> <A 
      onmouseover="img_on('dogs')" onmouseout="img_off('dogs')" 
      href="category?category_id=DOGS"><IMG 
      src="${vc.getAppResourceUrl('/images/product/dogs.gif')}" border=0 
      name=img_dogs></A> <IMG 
      src="${vc.getAppResourceUrl('/images/product/separator.gif')}" border=0> <A 
      onmouseover="img_on('reptiles')" onmouseout="img_off('reptiles')" 
      href="category?category_id=REPTILES"><IMG 
      src="${vc.getAppResourceUrl('/images/product/reptiles.gif')}" border=0 
      name=img_reptiles></A> <IMG 
      src="${vc.getAppResourceUrl('/images/product/separator.gif')}" border=0> <A 
      onmouseover="img_on('cats')" onmouseout="img_off('cats')" 
      href="category?category_id=CATS"><IMG 
      src="${vc.getAppResourceUrl('/images/product/cats.gif')}" border=0 
      name=img_cats></A> <IMG 
      src="${vc.getAppResourceUrl('/images/product/separator.gif')}" border=0> <A 
      onmouseover="img_on('birds')" onmouseout="img_off('birds')" 
      href="category?category_id=BIRDS"><IMG 
      src="${vc.getAppResourceUrl('/images/product/birds.gif')}" border=0 
      name=img_birds></A> </TD></TR></TBODY></TABLE>
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
          <TD>$${x[1]}</TD>
          <TD><A 
            href="cart?action=purchaseItem&amp;itemId=${x[0]}"><IMG 
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

