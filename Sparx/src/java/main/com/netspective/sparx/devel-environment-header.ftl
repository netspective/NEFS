<table bgcolor=lightyellow>

    <tr>
        <td>
            Request Parameters:
            <table>
                <#list vc.request.parameterNames as paramName>
                    <tr><td>${paramName}</td><td>${vc.request.getParameter(paramName)}</td></tr>
                </#list>
            </table>
        </td>

        <td>
            Session Attributes:
            <table>
                <#list vc.request.session.attributeNames as attribName>
                    <tr><td>${attribName}</td><td>${vc.request.session.getAttribute(attribName)}</td></tr>
                </#list>
            </table>
        </td>
    </tr>

</table>