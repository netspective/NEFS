<#include "/content/library.ftl"/>

<@templateProducerInstances templateProducer=vc.project.fieldTypes consumerTag="field" detail=vc.request.getParameter("type-name")?default("-")
                            noDetailMessage="Please choose one of the field types (controls) listed below to view details."/>
