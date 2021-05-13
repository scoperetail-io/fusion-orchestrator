package com.scoperetail.fusion.orchestrator.config.plugins.commons;

import com.scoperetail.commons.time.util.DateTimeZoneUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

public final class TimeZoneCustomizer {
  private static final String TIMEZONE_UTIL = "TIMEZONE_UTIL";
  private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  private TimeZoneCustomizer() {
  }

  public static Map<String, Object> getTimeZoneParams() {
    return Collections.singletonMap(TIMEZONE_UTIL, new TimeZoneUtil());
  }

  public static class TimeZoneUtil {
    public String toTimeZone(final String timezone, final String dateTime){
      ZoneId fromZone = ZoneId.of("GMT");
      ZoneId toZone = ZoneId.of(timezone.trim());
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
      LocalDateTime time = LocalDateTime.parse(dateTime, dateTimeFormatter);
      LocalDateTime result = DateTimeZoneUtil.toLocalDateTime(
          time,
          fromZone,
          toZone);
      return result.format(dateTimeFormatter);
    }
  }
}
