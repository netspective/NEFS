<#assign queryResults = getQueryResultsAsMatrix("petstore.getUserLogin")/>
<#foreach x in queryResults>
<#if x[0] = 0>
<h2>Sign In Error</h2>
<hr>
You could not be authenticated with the information provided. <br>
Please check your Username and Password.<br>

<p><a href="signin">Return to Sign in Page</a></p>
<#else>
     ${vc.request.session.setAttribute("signedin","yes")}
<#assign loginfo = {vc.request.getParameter("j_username") : vc.request.getParameter("j_username")}/>
${vc.request.session.setAttribute("loginfo",loginfo)}
<h2>Welcome to Java Pet Store Demo</h2>

<p><a href="home">Store Main Page</a></p>
 </#if>
</#foreach>
