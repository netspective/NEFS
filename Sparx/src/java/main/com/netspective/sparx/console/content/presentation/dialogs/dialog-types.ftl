<#include "/content/library.ftl"/>

<@templateProducerInstances templateProducer=vc.project.dialogTypes consumerTag="dialog" detailUrl="?type-name=" caption="Dialog Type" detail=vc.request.getParameter("type-name")?default("-")/>
