  <#include "console/content/library.ftl"/>
  <#if vc.request.session.getAttribute("signedin")?exists>
     <#assign si = vc.request.session.getAttribute("signedin")/>
  <#else>
     <#assign si = "no"/>
  </#if>
  <#if si = "yes">
      ${vc.request.session.setAttribute("signedin","no")}
      ${vc.response.sendRedirect("signout")}
  </#if>
  <TABLE height="85%" cellSpacing=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD vAlign=top>
      <P><FONT color=green size=5>Thank you for shopping at Java Pet Store Demo. 
      </FONT>
      <P>Please visit us again soon. </P></TD></TR>
  <TR>
    <TD vAlign=bottom></TD></TR>
</TBODY></TABLE>
