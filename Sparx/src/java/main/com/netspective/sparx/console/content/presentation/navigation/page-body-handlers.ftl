<#include "*/library.ftl">

<@templateProducerInstances templateProducer=vc.project.pageBodyHandlers
                            consumerTag="body"
                            detailUrl="?handler="
                            detail=vc.request.getParameter("handler")?default("-")/>