<#include "*/library.ftl">

<@templateProducerInstances templateProducer=vc.project.fieldConditionalActions
                            consumerTag="conditional"
                            detailUrl="?action="
                            detail=vc.request.getParameter("action")?default("-")/>