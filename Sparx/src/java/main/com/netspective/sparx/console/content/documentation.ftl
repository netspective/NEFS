<#include "*/library.ftl"/>

<@panel heading="Frameworks Documentation">
    <#assign localSampler = vc.request.contextPath/>
    <@reportTable
            headings = ["Type", "Location", "Instructions"]
            data=[
              ["NEF Documentation in your local Sampler Site", "<a href='${vc.request.contextPath}'>${vc.request.contextPath}</a>", "Click on the <i>Documentation</i> tab in your Local Sampler. This site includes documentation for your current release."],
              ["NEF Documentation in Netspective Sampler Site", "<a href='http://sampler.netspective.com'>http://sampler.netspective.com</a>", "Click on the <i>Documentation</i> tab in Netspective Sampler. This site includes documentation for the most recent release."],
              ["NEF Documentation in Netspective Support Site", "<a href='http://support.netspective.com'>http://support.netspective.com</a>", "Click on the <i>Documentation</i> link. This site includes documentation for both recent and older releases."]
              ]/>
</@panel>
