<#include "*/library.ftl">

<@templateProducerInstances templateProducer=vc.project.pageConditionalActions
                            consumerTag="conditional"
                            detailUrl="?action="
                            detail=vc.request.getParameter("action")?default("-")/>