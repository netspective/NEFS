<#include "*/library.ftl"/>

<div class="textbox">
    <table class="report" width=100% border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-heading">Property</td>
            <td class="report-column-heading">Value</td>
        </tr>
        <tr>
            <td class="report-column-odd">Application Id</td>
            <td class="report-column-odd"><code>${vc.servletContext.servletContextName}</code> running on ${vc.servletContext.serverInfo}</td>
        </tr>
        <tr>
            <td class="report-column-even">Application Home</td>
            <td class="report-column-even"><code>${vc.servletContext.getRealPath('')}</code></td>
        </tr>
        <tr>
            <td class="report-column-odd"><a href="project/project-source">Sparx Project</a></td>
            <td class="report-column-odd"><code><@projectFile/></td>
        </tr>
        <tr>
            <td class="report-column-even"><a href="project/runtime-environment">Runtime Environment</a></td>
            <td class="report-column-even"><code>${vc.environmentFlags}</code></td>
        </tr>
        <tr>
            <td class="report-column-odd"><a href="project/configuration/servlet">Deployment Descriptor</a></td>
            <td class="report-column-odd"><code>${vc.servletContext.getRealPath('WEB-INF/web.xml')}</code></td>
        </tr>
    </table>
</div>