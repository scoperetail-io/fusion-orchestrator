package com.scoperetail.fusion.orchestrator.config.plugins.commons;

import com.scoperetail.commons.time.util.DateTimeZoneUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class TimeZoneCustomizer {
  private static final String TIMEZONE_UTIL = "TIMEZONE_UTIL";
  private final static String GMT = "GMT";
  private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  public TimeZoneCustomizer() {
  }

  public String toTimeZone(final String timezone, final String dateTime){
    ZoneId fromZone = ZoneId.of(GMT);
    ZoneId toZone = ZoneId.of(timezone.trim());
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    LocalDateTime result = DateTimeZoneUtil.toLocalDateTime(
        LocalDateTime.parse(dateTime, dateTimeFormatter),
        fromZone,
        toZone);
    return result.format(dateTimeFormatter);
  }
}
