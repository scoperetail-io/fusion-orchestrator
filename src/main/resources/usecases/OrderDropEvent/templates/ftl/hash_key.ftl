{
  "EventId":"OrderDropEvent",
  "StoreNumber":"${DOMAIN_ENTITY.getRoutingInfo().getDestinationNode().getNodeID()}",
  "OrderNumber":"${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getOrderNbr()}"
}