<#include "*/library.ftl">

<@templateProducerInstances templateProducer=vc.project.dialogExecuteHandlers
                            consumerTag="dialog-execute-handler"
                            detailUrl="?type-name="
                            detail=vc.request.getParameter("type-name")?default("-")/>