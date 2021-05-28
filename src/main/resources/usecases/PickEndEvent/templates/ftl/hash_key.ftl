{
  "EventId":"PickEndEvent",
  "StoreNumber":"${DOMAIN_ENTITY.getMessageBody().getPickOrderComplete().getNode().getNodeId()}",
  "OrderNumber":"${DOMAIN_ENTITY.getMessageBody().getPickOrderComplete().getFulfillmentOrder().getOrderNbr()}"
}
