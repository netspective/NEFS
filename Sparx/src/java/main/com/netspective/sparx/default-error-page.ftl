<div class="sparxDefaultErrorBody">
    <style>
        div.sparxDefaultErrorBody
        {
            font-family: verdana, tahoma;
            font-size: 10pt;
        }

        table.sparxDefaultErrorInfo
        {
            border: 1px solid black;
        }

        table.sparxDefaultErrorInfo td
        {
            font-family: verdana, tahoma;
            font-size: 10pt;
            padding: 2px;
            margin: 0;
            border-bottom: 1px solid #999999;
            background: #EEEEEE;
        }

        table.sparxDefaultErrorInfo tr
        {
            vertical-align: top;
        }

        table.sparxDefaultErrorInfo td.heading
        {
            text-align: right;
            background: #FFFFFF;
        }

        table.sparxDefaultErrorInfo td.headingLastRow
        {
            text-align: right;
            border-bottom: none;
            background: #FFFFFF;
        }

        table.sparxDefaultErrorInfo td.lastRow
        {
            border-bottom: none;
        }
    </style>

    <#assign nc = vc.navigationContext/>

    <b><font color=red>Your application has encountered an error.</font></b><p>
    <#if nc.getRuntimeEnvironmentFlags().isDevelopmentOrTesting()>
    Since you have not specified an &lt;error-page&gt; tag in your
    navigation tree, this default error page is being displayed. In addition to supplying this message, the error has
    been logged according to the Jakarta Commons Logging properties you have specified for the application.
    The stack trace only appears in a development or testing environment and will not appear in other environments
    such as production.
    </#if>

    <p>

    <table class="sparxDefaultErrorInfo">
        <tr>
            <td class="heading">Active Page:</td>
            <td>
                <code>${nc.activePage.qualifiedName}</code>
            </td>
        </tr>

        <tr>
            <td class="heading">Active Page Class:</td>
            <td>
                <code>${nc.activePage.class.name}</code>
            </td>
        </tr>

        <tr>
            <td class="heading">Matched Exception Class:</td>
            <td>
                <code>${nc.matchedErrorPageExceptionClass.name}</code>
            </td>
        </tr>

        <tr>
            <td class="heading">Exception Message:</td>
            <td>
                <b><code>${vc.navigationContext.errorPageException.getMessage()}</code></b>
            </td>
        </tr>

        <#if nc.getRuntimeEnvironmentFlags().isDevelopmentOrTesting()>
        <tr>
            <td class="headingLastRow">Request Parameters:</td>
            <td class="lastRow">
                <table>
                    <#list vc.request.parameterNames as paramName>
                        <tr><td>${paramName}</td><td>${vc.request.getParameter(paramName)}</td></tr>
                    </#list>
                </table>
            </td>
        </tr>
        </#if>
    </table>

</div>

<#if nc.getRuntimeEnvironmentFlags().isDevelopmentOrTesting()>
<p>
<code>${nc.errorPageExceptionStackStraceHtml}</code>
</#if>
