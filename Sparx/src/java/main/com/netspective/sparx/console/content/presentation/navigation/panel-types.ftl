<#include "/content/library.ftl"/>

<@templateProducerInstances templateProducer=vc.project.panelTypes
                            consumerTag="panel"
                            detailUrl="?type-name="
                            detail=vc.request.getParameter("type-name")?default("-")/>