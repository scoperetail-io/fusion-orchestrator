<#assign aDateTime = .now>
[{
  "eventName":"PICK_BEGIN",
  "eventTs" : "${aDateTime?string.xs_ms_nz}",
  "eventPayload" : {
      "fulfillmentOrders" : [
      <#list messageBody.orderBeginEvent.fulfillmentOrders.fulfillmentOrder as order>
        {
            "orderNbr" : ${order.orderNbr},
            "priorityCode" : ${order.priorityCode},
        }
        <#if order_has_next>,</#if>
      </#list>
      ]
  },
  "MessageExtensions" : null
}]