<#include "*/library.ftl"/>

<div class="textbox">

    The Sparx Project Source is the single entry point for Sparx component declarations and the actual file where you
    declare all the components that will be used by your application. If there are errors in any of your Sparx source
    files they will be displayed here.

    <p>

    <table class="report" border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td class="report-column-odd">Data Model</td>
            <td class="report-column-odd"><code>${vc.project.class.name}</code></td>
        </tr>
        <tr>
            <td class="report-column-even">Input Source and Dependencies</td>
            <td class="report-column-even">
                ${getInputSourceDependencies()}
            </td>
        </tr>
        <tr>
            <td class="report-column-odd"><a name="errors">Errors</a></td>
            <td class="report-column-odd">
                <#if vc.projectComponent.errors?size = 0>
                    None
                <#else>
                    <ol>
                    <#list vc.projectComponent.errors as error>
                        <li>${error}</li>
                    </#list>
                    </ol>
                </#if>
            </td>
        </tr>
        <tr>
            <td class="report-column-even">Warnings</td>
            <td class="report-column-even">
                <#if vc.projectComponent.warnings?size = 0>
                    None
                <#else>
                    <ol>
                    <#list vc.projectComponent.warnings as warning>
                        <li>${warning}</li>
                    </#list>
                    </ol>
                </#if>
            </td>
        </tr>
    </table>

</div>