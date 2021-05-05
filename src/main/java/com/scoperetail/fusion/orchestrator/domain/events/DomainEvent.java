package com.scoperetail.fusion.orchestrator.domain.events;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DomainEvent {
	private String eventId; //hash key using attributes
	private Event event;
	private Map<String,String> keyMap;
	private String payload;
	@Builder.Default
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalDateTime timestamp = LocalDateTime.now();
}
