<#include "*/library.ftl">

<#assign classPathUtilityClass = statics["com.netspective.commons.lang.ClassPath"]/>
<#assign classLoader = classPathUtilityClass.getDefaultClassLoader()/>
<#assign classPaths = []/>
<#list classPathUtilityClass.getClassPaths(classLoader) as classPathInfo>
    <#assign classPaths = classPaths + [[classPathInfo.classPath]]/>
</#list>
<#assign classPathProvider = classPathUtilityClass.getClassPathProvider(classLoader)?default('-')/>

<@panel heading="Class Paths">
    <div class="textbox">
    Class Loader: <code><@classReference className=classLoader.class.name/></code><br>
    Class Path Provider: <code>
    <#if classPathProvider = '-'>
        Using System Property '<code>java.class.path</code>'.
    <#else>
        <@classReference className=classPathProvider.class.name/>
    </#if>
    </code>
    </div>
    <@reportTable headings=["Path"] data=classPaths/>
</@panel>
