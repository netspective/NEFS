<#include "console/content/library.ftl"/>
<#assign queryResults = getQueryResultsAsMatrix("petstore.getProducts")/>
<#assign productName = getQueryResultsAsMatrix("petstore.getProductName")/>

<TABLE cellSpacing=0 width="100%" 
background="${vc.getAppResourceUrl('/images/category/bkg-topbar.gif')}" border=0>
  <TBODY>
  <TR>
    <TD><A 
      href="home"><IMG 
      src="${vc.getAppResourceUrl('/images/category/logo-topbar.gif')}" border=0></A> </TD>
    <TD align=left>
      <FORM 
      action=/scripts/SaISAPI.dll/JPS112.class/petstore/control/search><FONT 
      color=cyan size=-1>What are you looking for?</FONT> <BR><INPUT size=14 
      name=search_text> <INPUT type=image 
      src="${vc.getAppResourceUrl('/images/category/search.gif')}" border=0 name=search> </FORM></TD>
    <TD align=right><A onmouseover="img_on('cart')" 
      onmouseout="img_off('cart')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/cart"><IMG 
      src="${vc.getAppResourceUrl('/images/category/cart.gif')}" border=0 name=img_cart></A> <IMG 
      src="Product Category_files/separator.gif" border=0> <A 
      onmouseover="img_on('signin')" onmouseout="img_off('signin')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/signin"><IMG 
      src="${vc.getAppResourceUrl('/images/category/sign-in.gif')}" border=0 name=img_signin></A> 
      <IMG src="${vc.getAppResourceUrl('/images/category/separator.gif')}" border=0> <A 
      onmouseover="img_on('help')" onmouseout="img_off('help')" 
      href="http://gallery.bluestone.com/scripts/SaISAPI.dll/JPS112.class/petstore/control/help"><IMG 
      src="${vc.getAppResourceUrl('/images/category/help.gif')}" border=0 name=img_help></A> 
  </TD></TR></TBODY></TABLE>
<TABLE height=20 cellSpacing=0 width="100%" 
background="${vc.getAppResourceUrl('/images/category/white.htm')}" border=0>
  <TBODY>
  <TR>
    <TD align=middle><A onmouseover="img_on('fish')" 
      onmouseout="img_off('fish')" 
      href="category?category_id=FISH"><IMG 
      src="${vc.getAppResourceUrl('/images/category/fish.gif')}" border=0 name=img_fish></A> <IMG 
      src="${vc.getAppResourceUrl('/images/category/separator.gif')}" border=0> <A 
      onmouseover="img_on('dogs')" onmouseout="img_off('dogs')" 
      href="category?category_id=DOGS"><IMG 
      src="${vc.getAppResourceUrl('/images/category/dogs.gif')}" border=0 name=img_dogs></A> <IMG 
      src="${vc.getAppResourceUrl('/images/category/separator.gif')}" border=0> <A 
      onmouseover="img_on('reptiles')" onmouseout="img_off('reptiles')" 
      href="category?category_id=REPTILES"><IMG 
      src="${vc.getAppResourceUrl('/images/category/reptiles.gif')}" border=0 name=img_reptiles></A> 
      <IMG src="${vc.getAppResourceUrl('/images/category/separator.gif')}" border=0> <A 
      onmouseover="img_on('cats')" onmouseout="img_off('cats')" 
      href="category?category_id=CATS"><IMG 
      src="${vc.getAppResourceUrl('/images/category/cats.gif')}" border=0 name=img_cats></A> <IMG 
      src="${vc.getAppResourceUrl('/images/category/separator.gif')}" border=0> <A 
      onmouseover="img_on('birds')" onmouseout="img_off('birds')" 
      href="category?category_id=BIRDS"><IMG 
      src="${vc.getAppResourceUrl('/images/category/birds.gif')}" border=0 name=img_birds></A> 
  </TD></TR></TBODY></TABLE>
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
