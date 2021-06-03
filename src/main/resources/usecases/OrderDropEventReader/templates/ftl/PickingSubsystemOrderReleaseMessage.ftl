<PickingSubSystemOrderReleaseMessage xmlns="http://www.xmlns.walmartstores.com/SuppyChain/FulfillmentManagement/GlobalIntegeratedFulfillment/Picking/PickingSubSystemOrderReleaseMessage/1.0/" xmlns:hdr="http://www.xmlns.walmartstores.com/Header/datatypes/MessageHeader/1.4/">
  <hdr:MessageHeader>
    <hdr:SubId>SUB-EIC-UPDPICK-V1</hdr:SubId>
    <hdr:CnsmrId>CON-NODE-UPDPICK-V1</hdr:CnsmrId>
    <hdr:SrvcNm>UpdateFulfillmentPicking.subSystemOrderRelease</hdr:SrvcNm>
    <hdr:TranId>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getOrderNbr()}</hdr:TranId>
    <hdr:Version>1.0</hdr:Version>
  </hdr:MessageHeader>
  <MessageBody>
    <RoutingInfo>
      <SourceNode>
        <location>
          <countryCode>${DOMAIN_ENTITY.getRoutingInfo().getSourceNode().getLocation().getCountryCode()}</countryCode>
        </location>
        <nodeID>${DOMAIN_ENTITY.getRoutingInfo().getSourceNode().getNodeID()}</nodeID>
      </SourceNode>
      <DestinationNode>
        <location>
          <countryCode>${DOMAIN_ENTITY.getRoutingInfo().getDestinationNode().getLocation().getCountryCode()}</countryCode>
        </location>
        <nodeID>${DOMAIN_ENTITY.getRoutingInfo().getDestinationNode().getNodeID()}</nodeID>
        <cNodeID>${DOMAIN_ENTITY.getRoutingInfo().getDestinationNode().getNodeID()}</cNodeID>
      </DestinationNode>
    </RoutingInfo>
    <Order>
      <node>
        <nodeId>${DOMAIN_ENTITY.getOrder().getNode().getNodeId()}</nodeId>
        <countryCode>${DOMAIN_ENTITY.getOrder().getNode().getCountryCode()}</countryCode>
      </node>
      <fulfillmentOrder>
        <orderNbr>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getOrderNbr()}</orderNbr>
        <orderPriority>
          <code>1</code>
          <description>Grocery Pickup Default</description>
        </orderPriority>
        <type code="6" name="${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getType().getName()}"/>
        <destinationBusinessUnit destBannerName="Walmart Grocery" destDivisonNumber="1"/>
        <pickDueTime>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getPickDueTime()?datetime.iso?string("yyyy-MM-dd'T'HH:mm:ss Z")}</pickDueTime>
        <expectedOrderPickupTime>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getExpectedOrderPickupTime()?datetime.iso?string("yyyy-MM-dd'T'HH:mm:ss Z")}</expectedOrderPickupTime>
        <#if DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getEarliestPickTime()?has_content>
        <earliestPickTime>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getEarliestPickTime()?datetime.iso?string("yyyy-MM-dd'T'HH:mm:ss Z")}</earliestPickTime>
        </#if>
        <orderSequenceNumber>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getOrderSequenceNumber()}</orderSequenceNumber>
        <loadGroupNumber>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getLoadGroupNumber()}</loadGroupNumber>
        <carrierBagAllowed>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getCarrierBagAllowed()}</carrierBagAllowed>
        <recordCarrierBagCount>${DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getRecordCarrierBagCount()}</recordCarrierBagCount>

        <lines>
            <#list DOMAIN_ENTITY.getOrder().getFulfillmentOrder().getLines() as line>
                <line>
                    <lineNbr>${line.getLine().getLineNbr()}</lineNbr>
                    <upc>${line.getLine().getUpc()}</upc>
                    <qty>${line.getLine().getQty()}</qty>
                    <#if line.getLine().getWeight()?has_content>
                    <weight>${line.getLine().getWeight()}</weight>
                    </#if>
                    <pickByType>${line.getLine().getPickByType()}</pickByType>
                    <substitutionAllowed>${line.getLine().getSubstitutionAllowed()}</substitutionAllowed>
                    <itemNbr>${line.getLine().getItemNbr()}</itemNbr>
                    <uom>${line.getLine().getUom()}</uom>
                    <#if line.getLine().getCrossRefs()?has_content>
                    <crossRefs>
                        <#list line.getLine().getCrossRefs() as crossRef>
                            <crossRef>
                                <upc>${crossRef.getCrossRef().getUpc()}</upc>
                                <itemNbr>${crossRef.getCrossRef().getItemNbr()}</itemNbr>
                            </crossRef>
                        </#list>
                    </crossRefs>
                    </#if>
                </line>
            </#list>
        </lines>
      </fulfillmentOrder>
    </Order>
  </MessageBody>
</PickingSubSystemOrderReleaseMessage>
