
<#if !(vc.request.session.getAttribute("signedin")?exists)> 
   <#global signedin = false/>
<#else>
  <#if vc.request.session.getAttribute("signedin") = "yes">
     <#global signedin = true/>
  <#else>
      <#global signedin = false/>
  </#if>
</#if>
<#if signedin = false>
   ${vc.response.sendRedirect("signin")}
</#if>
<TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top>
      <P>
      <FORM action=validatebillinginformation><FONT color=green size=5>Credit 
      Card Information: </FONT>
      <P>
      <TABLE border=0>
        <TBODY>
        <TR>
          <TD>Credit Card Type:</TD>
          <TD><SELECT name=credit_card_type> <OPTION value=Visa 
              selected>Visa<OPTION value=Mastercard>Mastercard<OPTION 
              value="American Express">American Express</OPTION></SELECT> </TD></TR>
        <TR>
          <TD>Card Number: </TD>
          <TD><INPUT value="9999 9999 9999 9999" name=credit_card_number> 
        </TD></TR>
        <TR>
          <TD>Expiration Date: </TD>
          <TD>Month: <SELECT name=expiration_month> <OPTION value=01 
              selected>01</OPTION> <OPTION value=02>02</OPTION> <OPTION 
              value=03>03</OPTION> <OPTION value=04>04</OPTION> <OPTION 
              value=05>05</OPTION> <OPTION value=06>06</OPTION> <OPTION 
              value=07>07</OPTION> <OPTION value=08>08</OPTION> <OPTION 
              value=09>09</OPTION> <OPTION value=10>10</OPTION> <OPTION 
              value=11>11</OPTION> <OPTION value=12>12</OPTION></SELECT> Year: 
            <SELECT name=expiration_year> <OPTION value=2001>2001</OPTION> 
              <OPTION value=2001 selected>2001</OPTION> <OPTION 
              value=2002>2002</OPTION> <OPTION value=2003>2003</OPTION> <OPTION 
              value=2004>2004</OPTION> <OPTION value=2005>2005</OPTION> <OPTION 
              value=2006>2006</OPTION></SELECT> </TD></TR></TBODY></TABLE>
      <P><FONT color=green size=5>Billing Address: </FONT>
      <P>Please confirm that the following Billing Address is correct and press 
      the <B>Continue</B> button. 
      <P>
      <TABLE>
        <TBODY>
        <TR>
          <TD align=right>First Name:</TD>
          <TD align=left colSpan=2><INPUT maxLength=30 size=30 value=bob 
            name=given_name> </TD></TR>
        <TR>
          <TD align=right>Last Name:</TD>
          <TD align=left colSpan=2><INPUT maxLength=30 size=30 value=bob 
            name=family_name> </TD></TR>
        <TR>
          <TD align=right>Street Address:</TD>
          <TD align=left colSpan=2><INPUT maxLength=70 size=55 
            value="1 bob ln" name=address_1> </TD></TR>
        <TR>
          <TD></TD>
          <TD align=left colSpan=2><INPUT maxLength=70 size=55 name=address_2> 
          </TD></TR>
        <TR>
          <TD align=right>City:</TD>
          <TD align=left colSpan=2><INPUT maxLength=70 size=55 value=bobtown 
            name=city> </TD></TR>
        <TR>
          <TD>State/Province:</TD>
          <TD align=left><SELECT size=1 name=state_or_province> <OPTION 
              value=California selected>California</OPTION> <OPTION 
              value="New York">New York</OPTION> <OPTION 
              value=Texas>Texas</OPTION></SELECT> </TD>
          <TD>Postal Code: <INPUT maxLength=12 size=12 value=12345 
            name=postal_code> </TD></TR>
        <TR>
          <TD>Country:</TD>
          <TD align=left colSpan=2><SELECT size=1 name=country> <OPTION 
              value=USA selected>USA</OPTION> <OPTION 
              value=Canada>Canada</OPTION> <OPTION 
            value=Japan>Japan</OPTION></SELECT> </TD></TR>
        <TR>
          <TD>Telephone Number:</TD>
          <TD align=left colSpan=2><INPUT maxLength=70 size=12 
            value=1112223333 name=telephone_number> </TD></TR></TBODY></TABLE>
      <P>Ship to Billing Address <INPUT type=checkbox CHECKED 
      name=ship_to_billing_address> 
      <P><INPUT type=image src="" border=0> </FORM></P></TD></TR>
  <TR>
    <TD vAlign=bottom></TD></TR>
  <TR>
    <TD vAlign=bottom>
      <TABLE cellSpacing=0 width="100%" border=0 <tr>
        <TBODY>
        <TR>
          <TD align=middle><FONT color=black size=+1><!--Running on -->Running 
            on HP Bluestone Total-e-Server 7.3</FONT> 
  </TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE></BODY></HTML>

