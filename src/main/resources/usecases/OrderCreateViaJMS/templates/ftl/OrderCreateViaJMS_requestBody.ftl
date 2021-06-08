<#assign aDateTime = .now>
[
  {
    "eventName": "ORDER_CREATE",
    "eventTs" : "${aDateTime?string.xs_ms_nz}",
    "eventPayload": {
     "requestId": "${DOMAIN_ENTITY.getRequestId()}",
     "orderType": "${DOMAIN_ENTITY.getOrderType()}",
     "customerId": "${DOMAIN_ENTITY.getCustomerId()}"
     "products": [
      <#list DOMAIN_ENTITY.getLines().getLine() as line>
        {
          "productId": ${line.getProductId()},
          "quantity": ${line.getQuantity()}
        }
      </#list>
      ]
    }
  }
]