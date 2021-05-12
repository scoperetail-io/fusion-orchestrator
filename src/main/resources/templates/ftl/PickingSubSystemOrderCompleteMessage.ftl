<#assign aDateTime = .now>
[
  {
    "eventName": "PICK_COMPLETE",
    "eventTs" : "${aDateTime?string.xs_ms_nz}",
    "eventPayload": {
      "pickCompleteDetails": [{
        "fulfillOrdNbr": "${messageBody.pickingSubSystemOrderComplete.fulfillmentOrder.orderNbr}",
        "lines": [
        <#list messageBody.pickingSubSystemOrderComplete.fulfillmentOrder.lines.line as ln>
        {
          "orderLine": "${ln.lineNbr}",
          "qtyToFulfill": "${ln.orderedQty}",
          "pickDetails": [
          <#list ln.pickDetails as pickDetail>
          {
            "pickQty": "${pickDetail.pickQty}",
            "pickType": "${pickDetail.pickType}",
            "pickUpcNbr": "${pickDetail.pickUpcNbr}",
            "upcTypeCd": "5100",
            "pickedUser": "${pickDetail.pickedUser}",
            "pickDisplayTs": "${pickDetail.pickDisplayTs}",
            "pickedTs": "${pickDetail.pickedTs}",
            "pickUom": "${pickDetail.pickUom}",
            "container": {
              "pickQty": 1,
              "pickWeight": 5.0,
              "containerNbr": "A64723"
            },
            "pickLocation": "MFC",
            "pickPriceEmbeddedUpcNbr": "${pickDetail.pickPriceEmbeddedUpcNbr}",
            "gtinPriceAmt": 14.8,
            "pickWeight": 5.0
          }
          <#if pickDetail_has_next>,</#if>
          </#list>
          ]
        }
        <#if ln_has_next>,</#if>
        </#list>
        ]
      }]
    }
  }
]