{
  "EventId":"PickBeginEvent",
  "StoreNumber":"${DOMAIN_ENTITY.messageBody.orderBeginEvent.node.nodeId}",
  <#list DOMAIN_ENTITY.messageBody.orderBeginEvent.fulfillmentOrders.fulfillmentOrder as fulfillmentOrder>
    "OrderNumber":"${fulfillmentOrder.orderNbr}"
  </#list>
}
