
<#if !(vc.request.session.getAttribute("signedin")?exists)> 
   <#global signedin = false/>
<#else>
  <#if vc.request.session.getAttribute("signedin") = "yes">
     <#global signedin = true/>
  <#else>
      <#global signedin = false/>
  </#if>
</#if>


