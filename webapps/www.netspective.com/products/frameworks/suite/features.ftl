<#macro feature headline image>
    <table class="product-feature-synopsis">
        <tr>
            <td valign=top>
                <img src="${resourcesPath}/images/products/frameworks/suite/features/${image}.gif"/>
            </td>
            <td valign=top>
                <h1>${headline}</h1>
                <#nested/>
            </td>
        </tr>
    </table>
</#macro>

<#macro features listing columns=2 colWidth="50%">
<table width=100%>
    <tr>
    <#local column = 1>
    <#list listing as properties>
        <td width="${colWidth}" valign=top>
            <@feature headline=properties['headline'] image=properties['image']>
                ${properties['body']}
            </@feature>
        </td>
        <#local column = column + 1/>
        <#if column gt columns>
            <#local column = 1/>
            </tr><tr>
        </#if>
    </#list>
    </tr>
</table>
</#macro>

<h1>User Interface (Presentation)</h1>

<@features listing=
    [
        {'headline' : 'Navigation/Workflow', 'image': 'navigation', 'body': 'Control complex application behavior with integrated security and navigation personalized to individual users or roles.'},
        {'headline' : 'Subsites/Multi-Modules', 'image': 'navigation', 'body': 'Develop sophisticated multi-module single-sign-on apps using one Servlet context.'},
        {'headline' : 'Forms', 'image': 'dialogs', 'body': 'Create auto-validated forms with dozens of built-in field types that easily handle and recover from all user errors.'},
        {'headline' : 'Themes', 'image': 'themes', 'body': 'Develop apps that have a consistent UI that can switch looks and feel without code changes.'},
        {'headline' : 'Template Engines', 'image': 'template-engines', 'body': 'Use your favorite template engine for content: JSP, FreeMarker, Velocity, Tea, etc.'},
        {'headline' : 'Reports', 'image': 'reports', 'body': 'Leverage built-in report writers and XSLT transformers or connect to your favorite reporting engine.'}
    ]
/>

<h1>Database Management</h1>

<@features listing=
    [
        {'headline' : 'Static DAOs', 'image': 'static-queries', 'body': 'Define SQL in XML and generate Lightweight Data Access Objects (DAOs) with bind variables and statement pooling.'},
        {'headline' : 'Dynamic DAOs', 'image': 'dynamic-queries', 'body': 'Define SQL rules in XML and let your users enter search criteria that auto-generates SQL with bind variables and joins.'},
        {'headline' : 'Schemas', 'image': 'schemas', 'body': 'Create vendor-independent relational tables and auto-generate SQL DDL, DAOs, HTML docs, and XML import/exporters.'},
        {'headline' : 'Data Sources', 'image': 'data-sources', 'body': 'Connect to multiple data sources within a single page selectable per app, per user, role, location, or other rule.'},
        {'headline' : 'Database Policies', 'image': 'database-policies', 'body': 'Isolate database-specific functionality such as auto-inc into delegated objects.'},
        {'headline' : 'Connections Mgmt', 'image': 'open-connections', 'body': 'Manage pooled connections centrally to visualize open and leaked connections.'}
    ]
/>

<h1>Core Functionality</h1>

<@features listing=
    [
        {'headline' : 'Security', 'image': 'access-control', 'body': 'Harden your applications with auto-managed users, roles, and permissions.'},
        {'headline' : 'Declaration Sources', 'image': 'input-source', 'body': 'Retrieve XML declarations from files, URLs, or class-loader resources.'},
        {'headline' : 'Commands', 'image': 'commands', 'body': 'Encapsulate app functionality into commands and increase reuse across projects.'},
        {'headline' : 'Value Sources', 'image': 'value-sources', 'body': 'Encapsulate business values logic into objects that be reused across modules and projects.'},
        {'headline' : 'Code Collaboration', 'image': 'files', 'body': 'Collaborate with other developers across the room or across the world using only a browser.'},
        {'headline' : 'App Metrics', 'image': 'metrics', 'body': 'Automatically generate application metrics such as code size, function points, feature points.'},
        {'headline' : 'Documentation', 'image': 'documentation', 'body': 'Review tag and developer documentation online.'}
    ]
/>

<h1>Tools Integration</h1>

<@features listing=
    [
        {'headline' : 'Jakarta Ant', 'image': 'ant-build', 'body': 'Automate your builds using the integrated Ant build engine and supplied build files.'},
        {'headline' : 'Commons/Log4J Logging', 'image': 'logging', 'body': 'Improve debugging by using the integrated Jakarta Commons Logging engine.'},
        {'headline' : 'Any IDE', 'image': 'any-ide', 'body': 'Use your favorite IDE that can edit XML and compile Java files.'},
        {'headline' : 'Any App Server', 'image': 'any-app-server', 'body': 'Create completely app-server independent products.'},
        {'headline' : 'Any Relational Database', 'image': 'any-db-server', 'body': 'Create db-server independent DAOs and schemas.'}
    ]
/>

