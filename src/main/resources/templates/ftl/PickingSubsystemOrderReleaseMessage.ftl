<PickingSubSystemOrderReleaseMessage xmlns="http://www.xmlns.walmartstores.com/SuppyChain/FulfillmentManagement/GlobalIntegeratedFulfillment/Picking/PickingSubSystemOrderReleaseMessage/1.0/" xmlns:hdr="http://www.xmlns.walmartstores.com/Header/datatypes/MessageHeader/1.4/">
  <hdr:MessageHeader>
    <hdr:SubId>SUB-EIC-UPDPICK-V1</hdr:SubId>
    <hdr:CnsmrId>CON-NODE-UPDPICK-V1</hdr:CnsmrId>
    <hdr:SrvcNm>UpdateFulfillmentPicking.subSystemOrderRelease</hdr:SrvcNm>
    <hdr:TranId>${order.fulfillmentOrder.orderNbr}</hdr:TranId>
    <hdr:Version>1.0</hdr:Version>
  </hdr:MessageHeader>
  <MessageBody>
    <RoutingInfo>
      <SourceNode>
        <location>
          <countryCode>${routingInfo.sourceNode.location.countryCode}</countryCode>
        </location>
        <nodeID>${routingInfo.sourceNode.nodeID}</nodeID>
      </SourceNode>
      <DestinationNode>
        <location>
          <countryCode>${routingInfo.destinationNode.location.countryCode}</countryCode>
        </location>
        <nodeID>${routingInfo.destinationNode.nodeID}</nodeID>
        <cNodeID>${routingInfo.destinationNode.cNodeID}</cNodeID>
      </DestinationNode>
    </RoutingInfo>
    <Order>
      <node>
        <nodeId>${order.node.nodeId}</nodeId>
        <countryCode>${order.node.countryCode}</countryCode>
      </node>
      <fulfillmentOrder>
        <orderNbr>${order.fulfillmentOrder.orderNbr}</orderNbr>
        <orderPriority>
          <code>1</code>
          <description>Grocery Pickup Default</description>
        </orderPriority>
        <type code="6" name="${order.fulfillmentOrder.type.name}"/>
        <destinationBusinessUnit destBannerName="Walmart Grocery" destDivisonNumber="1"/>
        <pickDueTime>${order.fulfillmentOrder.pickDueTime}</pickDueTime>
        <expectedOrderPickupTime>${order.fulfillmentOrder.expectedOrderPickupTime}</expectedOrderPickupTime>
        <earliestPickTime>${order.fulfillmentOrder.earliestPickTime}</earliestPickTime>
        <orderSequenceNumber>${order.fulfillmentOrder.orderSequenceNumber}</orderSequenceNumber>
        <loadGroupNumber>${order.fulfillmentOrder.loadGroupNumber}</loadGroupNumber>
        <carrierBagAllowed>${order.fulfillmentOrder.carrierBagAllowed}</carrierBagAllowed>
        <recordCarrierBagCount>${order.fulfillmentOrder.recordCarrierBagCount}</recordCarrierBagCount>

        <lines>
            <#list order.fulfillmentOrder.lines as line>
                <line>
                    <lineNbr>${line.lineNbr}</lineNbr>
                    <upc>${line.upc}</upc>
                    <qty>${line.qty}</qty>
                    <weight>${line.weight}</weight>
                    <pickByType>${line.pickByType}</pickByType>
                    <substitutionAllowed>${line.substitutionAllowed}</substitutionAllowed>
                    <itemNbr>${line.itemNbr}</itemNbr>
                    <uom>${line.itemNbr}</uom>
                    <crossRefs>
                        <#list line.crossRefs as crossRef>
                            <crossRef>
                                <upc>${crossRef.upc}</upc>
                                <itemNbr>${crossRef.itemNbr}</itemNbr>
                            </crossRef>
                        </#list>
                    </crossRefs>
                </line>
            </#list>
        </lines>
      </fulfillmentOrder>
    </Order>
  </MessageBody>
</PickingSubSystemOrderReleaseMessage>
