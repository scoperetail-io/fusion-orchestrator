<#assign aDateTime = .now>
[
  {
    "eventName": "PICK_BEGIN",
     "eventTs" : "${aDateTime?string.xs_ms_nz}",
    "eventPayload": {
      "fulfillmentOrders": [
      <#list DOMAIN_ENTITY.getMessageBody().getOrderBeginEvent().getFulfillmentOrders().getFulfillmentOrder() as fulfillmentOrder>
        {
          "orderNbr": ${fulfillmentOrder.getOrderNbr()},
          "priorityCode": ${fulfillmentOrder.getPriorityCode()}
        }
      </#list>
      ]
    },
    "MessageExtensions": [
     <#list DOMAIN_ENTITY.getMessageBody().getMessageExtensions().getMessageExtension() as extension>
       {
          "name": ${extension.getName()},
          "value": ${extension.getValue()}
       }
     </#list>
     ]
  }
]