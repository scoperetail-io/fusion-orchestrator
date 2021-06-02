<#assign aDateTime = .now>
[
  {
    "eventName": "PICK_COMPLETE",
    "eventTs" : "${aDateTime?string.xs_ms_nz}",
    "eventPayload": {
      "pickCompleteDetails": [
        {
          "fulfillOrdNbr": "${DOMAIN_ENTITY.getMessageBody().getPickOrderComplete().getFulfillmentOrder().getOrderNbr()}",
          "lines": [
           <#list DOMAIN_ENTITY.getMessageBody().getPickOrderComplete().getFulfillmentOrder().getLines().getLine() as line>
            {
              "orderLine": "${line.getLineNbr()}",
              "qtyToFulfill": "${line.getOrderedQty()}",
              "pickDetails": [
               <#list line.pickDetails as pickDetail>
                {
                  "pickQty": ${pickDetail.getPickQty()},
                  "pickType": "${pickDetail.getPickType()}",
                  "pickUpcNbr": "${pickDetail.getPickUpcNbr()}",
                  <#if pickDetail.getPickType() == "ORDERED">
                    "upcTypeCd": "5100",
                  <#elseif pickDetail.getPickType() == "SUBSTITUTE">
                    "upcTypeCd": "5200",
                  <#elseif pickDetail.getPickType() == "CROSSREF">
                    "upcTypeCd": "5300",
                  <#elseif pickDetail.getPickType() == "OVERRIDE">
                    "upcTypeCd": "5400",
                  </#if>
                  "pickedUser": "${pickDetail.getPickedUser()}",
                  "pickDisplayTs": "${pickDetail.getPickDisplayTs()}", 
                  "pickedTs": "${pickDetail.getPickedTs()}",
                  "pickUom": "${pickDetail.getPickUom()}",
                  "container": [
                    {
                      "pickQty": ${pickDetail.getPickQty()},
                      "containerNbr": "${pickDetail.getContainer().getContainerNbr()}"
                    }
                  ],
                  "pickLocation": "${pickDetail.getContainer().getLocation()}"
                }
               </#list>
              ]
            }
            <#if line_has_next>,</#if>
           </#list>
          ]
        }
      ]
    }
  }
]