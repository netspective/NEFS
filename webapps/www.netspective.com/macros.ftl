<#macro childMenus parentPage>
    <#list parentPage.childrenList as childPage>
        <#if ! childPage.flags.hidden>
        <tr>
            <td>
                <a class="menu" href="${childPage.getUrl(vc)}">
                <span class="L${childPage.level}">
                    <#if childPage = activePage>
                        <span class="active-page">${childPage.caption.getTextValue(vc)}</a>
                    <#elseif childPage.isInActivePath(vc)>
                        <span class="active-path">${childPage.caption.getTextValue(vc)}</a>
                    <#else>
                        ${childPage.caption.getTextValue(vc)}
                    </#if>
                </span>
                </a>
            </td>
        </tr>
        <#if childPage.childrenList?size gt 0 && childPage.isInActivePath(vc)>
            <@childMenus parentPage=childPage/>
        </#if>
    </#if>
    </#list>
</#macro>

<#macro primaryAncestorChildren>
    <table width="151" border="0" cellspacing="0" cellpadding="0">
        <@childMenus parentPage=activePage.primaryAncestor/>
    </table>
</#macro>

<#macro nextPageLink>
   <#local nextPage = activePage.getNextPath()?default('')/>
   <#if nextPage != ''><p align=right><br><a href="${nextPage.getUrl(vc)}">${nextPage.getHeading(vc)}</a> &gt;&nbsp;</p></#if>
</#macro>

<#macro pageBodyBegin>

    <table class="site-area" width="600" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width=151 valign=bottom><img src="${resourcesPath}/images/products/frameworks/spacer-border-left.gif"/><table width=100% border="0" cellspacing="0" cellpadding="0">
                    <tr><td class="site-area-name">${activePage.primaryAncestor.getCaption(vc)}</td></tr>
                </table></td>
            <td width=443 class="tag-line" valign=bottom><p align=right>${activePage.getHeading(vc)}</p></td>
        </tr>
    </table>

    <table width="600" border="0" cellspacing="0" cellpadding="0" class="body-content">
        <tr>
            <td valign="top" class="page-nav" rowspan=2 width=151>
                <@primaryAncestorChildren/>
            </td>
            <#if activePage.summary?exists>
            <td class="body-summary">
                ${activePage.summary.getTextValue(vc)}
            </td>
            </#if>
        </tr>
        <tr>
            <td valign="top" class="body-content">
</#macro>

<#macro pageBodyEnd>
            </td>
        </tr>
   </table>
</#macro>

<#assign sampleApps =
    [
        { 'id': "nefs-starter-empty", 'name': "Starter App", 'allowTryOnline': false, 'allowDownload': true,
          'descr' : "The starter application is just an empty application that contains the minimal set of files required
                     for NEF web applications. It doesn't do anything particularly useful but when you're ready to start
                     your own NEF project, you can use this sample as your template for the new project."
        },

        { 'id': "nefs-sampler", 'name': "Sampler", 'allowTryOnline': true, 'allowDownload': true,
          'descr' : "The Sampler gives you an idea of how easy it is to build J2EE web applications using the powerful
                     building blocks provided by our Java Frameworks. It includes various examples of creating forms and
                     input fields, performing a variety of different data validations, and executing forms to provide end-user functionality.",
	      'tutorialUrl' : resourcesPath + '/support/docs/nef-articles/nefs-sampler-tutorial.html',
          'tutorialName' : 'NEFS Sampler App Tutorial',
          'tutorialDescr' : 'Provides a tour of NEFS Sampler Application.'                     
        },

        { 'id': "nefs-sample-hello-world", 'name': "Hello World", 'allowTryOnline': false, 'allowDownload': true,
          'descr' : "The Hello World example is a classic in nearly all texts dealing with programming. It is the simplest
                     way to demonstrate the syntax of a language or tool, how to compile a program written in the language
                     and how to run the resulting binary."
        },

        { 'id': "nefs-sample-books", 'name': "Books", 'allowTryOnline': true, 'allowDownload': true,
          'descr' : "The Books sample is a small application meant to be used a personal books database. It allows
                     you to track the books you have and add more books to your collection or edit information stored about
                     existing books. It also allows you to search your collection for a particular book based on your own
                     custom search criteria. This application demonstrates how to create basic forms, perform validations, create
                     tables, generate DDL, construct dynamic SQL, and other NEFS functionality.",
          'tutorialUrl' : resourcesPath + '/support/docs/nef-articles/books-app-tutorial.html',
          'tutorialPDF' : resourcesPath + '/books-app-tutorial.pdf',
          'tutorialName' : 'Books Sample App Tutorial',
          'tutorialDescr' : 'Provides step by step instructions for how to build the Books sample application using NEFS.'
        },

        { 'id': "nefs-sample-library", 'name': "Library", 'allowTryOnline': true, 'allowDownload': true,
          'descr' : "The Library sample builds upon the Books example by allowing books to be
                     defined and checked out/checked in. This sample demonstrates entity relationships and looks more like a
                     real-world application than the Books sample because it has more
                     elaborate functionality."
        },

        { 'id': "nefs-sample-petstore", 'name': "Petstore", 'allowTryOnline': true, 'allowDownload': true,
          'descr' : "The Petstore application demonstrates how to create an online Petstore complete with login and
                     shopping cart. This example is a NEFS-specific version of the same Petstore application that is
                     described by the Sun J2EE Blueprints."
        },

        { 'id': "nefs-sample-cts", 'name': "Clinical Trials Management System", 'allowTryOnline': true, 'allowDownload': true,
          'descr' : "The Clinical Trials Management System (CTS) sample application demonstrates real-world usage of Commons, Axiom,
                     and Sparx. CTS provides the ability to manage patients, studies, sites, and coordinators for
                     pharmaceutical clinical trials. Samples are provided for a relational database management system, domain logic,
                     navigation, security, and access control."
        },

        { 'id': "nefs-sample-survey", 'name': "Survey", 'allowTryOnline': true, 'allowDownload': false,
          'descr' : "The Survey application demonstrates how to create forms with hundreds of inputs and persist the
                     data in a wizard-like fashion. This sample application has a different look and feel than some of
                     the other applications."
        }
    ]>

