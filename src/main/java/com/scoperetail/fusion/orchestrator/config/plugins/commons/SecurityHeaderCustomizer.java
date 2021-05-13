package com.scoperetail.fusion.orchestrator.config.plugins.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.scoperetail.fusion.security.service.WalmartSignatureGenerator;

public final class SecurityHeaderCustomizer {

  private static final String SECURITY_HEADERS = "SECURITY_HEADERS";
  private static final String CONSUMER_ID = "8be57d1a-3cec-45f4-b13f-ee465afb7284";
  private static final String PRIVATE_KEY_VERSION = "1";
  private static final String PRIVATE_KEY =
      "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+B1SfGS80wGHI\n"
          + "LhmHkanunoqNcoo2WiWtIlrs7ku4gtNwQ5UC/mHu9yOS/AO6qn+TlqGvx16StnkU\n"
          + "Ed+1rORyJmgwjULQ7qAPtAa9nHi4kiJ+pqEHpEyfZQo03EFh1otf3gnB37e2U7+U\n"
          + "YPSXYTSzEdo6d/cB4s+r6QKHU8Ud0huuvyETuRKK6I0U7EKmNCMxG5co1ibmE7jx\n"
          + "R9w735EdIzTlHw9RQwk6g9qEN0kh/gP5W5z0QNCdEycKhqM23bANbIIcX8exrEF3\n"
          + "iuHS42iaISEsmdTm8q3mMTUMXwByzsUqd2o5RK+I/K5N448121Al4p2su8foDQFq\n"
          + "qMcXpG8xAgMBAAECggEAF1I4/WmO1I+DsJiPw4aJ72H/qQpUkgUQOKZNuGQqYNOV\n"
          + "dWKl5/8mL7ie4gwRvRftnkRPRY5XOfQw+diTtu0oACZchRDhEVknxaWXai/I1QYC\n"
          + "O77hZlmwmaNB1GjvkT1OT46lbeix2CuBCIm5mtZwLEXVnXVZmB6o0/U9vTP3rggD\n"
          + "rbxlHDZdx7PLS4g5U6ASt+QLXpNKjGhAeQc7e2AUHhC6ujTB5Up8Uii4dtsyt5Cm\n"
          + "Lqs3uN1aBJv5CgaJ+evclI+47gEzspb0AdKEX3ZxxT/GcuzizJxblxQGTIWC4qw0\n"
          + "v3x3FPFgxP6gLAMx8yyfiaEWuJnXElctFnAhA4j3zQKBgQD2BkddYBcky0v0IfaU\n"
          + "sfi8sjYaaXDCthKmxgJ+j5foIGLOvKgNQc2rXaWPZBr8Fkzk8vjnZPJl+fEfkXCz\n"
          + "bYyy9R0phc+4WHqkXLY9nltgHQGM6Sb2IBfGtprlC3N8DpWLRovurj4TeTm+pF80\n"
          + "G2jZUXUDXB3gsTfCN0pY8AceTwKBgQDFu9FFtBhE1IlvwIayUcO2c3o3LoWI3TES\n"
          + "ozYy+JZ8Ena4wZQQin820er5Le9fQmTmIvSJcTnDaMpye1vlhdgeR3Bea+hlCdr8\n"
          + "+wx2kCwzBZ7G8dQl7jDNjLsYQPc/wzK4OqWZm8DRZR0ynCMepf+qm3+C5hFLHpJM\n"
          + "Cj0vv7m6fwKBgQC6IyX7T6gy/l0I6GlXNw+qNGsasIvPxVF3PHjGqH2V8mrPX40F\n"
          + "+rNQ5BlO075W28hxsCMBby62WNRFvhQ2rLu26hyWPdda8hZYJIz9McKQhWGW6w+/\n"
          + "R6i1f5LRmEhXo/Eg3s8gVlRBB4bY5iPhmJtLUIXndNpmFa7KEmr9vxyjoQKBgQCs\n"
          + "kuVsvmfH+u8Qd6mXjEERxetOqe2lq9ceew/coJVGKB8x1NFTNm0c/UzkuTCHpTzG\n"
          + "lo9yPT26zISUMhHcWlpq4tKxteAX/uf/j2QrPl01Epym6XzMtfwmCcNowZqJLeXg\n"
          + "YF8Tl5pLpsI634bPQvXGgvxg1EcXltD8SJtqRxzkJQKBgCW65G5uqLBF0p3NJ/1D\n"
          + "Sb6v9EtbYphD7l1ClbU7MKDMgO71kFzOx95FUk7s10lGUuhfxLY2uFQOuri//fsq\n"
          + "9AqiXRRp2mVo5suLcbt7Aqu/ixGNRxl/T9z9KFvY4pigowEsLFd1bHrT12BMgG64\n"
          + "VpuOUkxPDp8qhqHC3NS7LWzm";

  private SecurityHeaderCustomizer() {

  }

  public static Map<String, Object> getSecurityParams() {
    final Map<String, Object> paramsMap = new HashMap<>();
    final long currentTimeMillis = System.currentTimeMillis();
    final String signature = WalmartSignatureGenerator.getSignature(CONSUMER_ID, currentTimeMillis,
        PRIVATE_KEY_VERSION, PRIVATE_KEY);
    paramsMap.put("consumerInTimestamp", currentTimeMillis);
    paramsMap.put("secAuthSignature", signature);
    paramsMap.put("secKeyVersion", PRIVATE_KEY_VERSION);
    return Collections.singletonMap(SECURITY_HEADERS, paramsMap);
  }
}
