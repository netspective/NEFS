<#include "/content/library.ftl"/>

<@templateProducerInstances templateProducer=vc.project.fieldTypes
                            consumerTag="field"
                            detailUrl="?type-name="
                            detail=vc.request.getParameter("type-name")?default("-")/>