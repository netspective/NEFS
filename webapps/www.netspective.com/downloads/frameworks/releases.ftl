<#assign releases = getXmlDoc(vc.servlet.servletConfig.servletContext.getRealPath("/downloads/frameworks/releases.xml"))/>

<#assign latestSparxVersion = statics["com.netspective.sparx.ProductRelease"].PRODUCT_RELEASE.getVersion()/>
<#assign latestAxiomVersion = statics["com.netspective.axiom.ProductRelease"].PRODUCT_RELEASE.getVersion()/>
<#assign latestCommonsVersion = statics["com.netspective.commons.ProductRelease"].PRODUCT_RELEASE.getVersion()/>

<#assign latestSparxJar = "netspective-sparx-${latestSparxVersion}.jar"/>
<#assign latestAxiomJar = "netspective-axiom-${latestAxiomVersion}.jar"/>
<#assign latestCommonsJar = "netspective-commons-${latestCommonsVersion}.jar"/>


<#assign latestSparxJarDownloadHref = "${resourcesPath}/downloads/lib/${latestSparxJar}"/>
<#assign latestAxiomJarDownloadHref = "${resourcesPath}/downloads/lib/${latestAxiomJar}"/>
<#assign latestCommonsJarDownloadHref = "${resourcesPath}/downloads/lib/${latestCommonsJar}"/>
