<#include "*/header.ftl"/>

Some heading goes here

<@pageBody>

<p>
${vc.navigationContext.pageHeading} in area ${vc.navigationContext.activePage.primaryAncestor.qualifiedName}
<p>

</@pageBody>

<#include "*/footer.ftl"/>
