<#include "*/library.ftl">

<@templateProducerInstances templateProducer=vc.project.dialogExecuteHandlers consumerTag="dialog-execute-handler" detail=vc.request.getParameter("type-name")?default("-")
                            noDetailMessage="Please choose one of the dialog execute handlers listed below to view details."/>