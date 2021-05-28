{
  "WM_CONSUMER.ID": "${DOMAIN_ENTITY.messageHeader.cnsmrId}",
  "WM_CONSUMER.INTIMESTAMP": "${SECURITY_HEADERS.consumerInTimestamp}",
  "WM_SEC.AUTH_SIGNATURE": "${SECURITY_HEADERS.secAuthSignature}",
  "WM_SEC.KEY_VERSION": "${SECURITY_HEADERS.secKeyVersion}",
  "x-nodeid": "${DOMAIN_ENTITY.messageBody.orderBeginEvent.node.nodeId}",
  "x-countrycode": "${DOMAIN_ENTITY.messageBody.orderBeginEvent.node.countryCode}",
  "x-userid": "Alpha",
  "content-type": "application/json"
}