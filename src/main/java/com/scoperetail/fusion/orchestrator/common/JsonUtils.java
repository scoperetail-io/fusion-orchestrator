package com.scoperetail.fusion.orchestrator.common;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
	private JsonUtils() {
	}

	static final ObjectMapper mapper = new ObjectMapper();

	public static final <T> T unmarshal(final Optional<String> message, Optional<TypeReference<T>> typeReference)
			throws IOException {
		String incomingMessage = message.orElseThrow(() -> new IOException("Unable to unmarshal :: Message = null"));
		TypeReference<T> incomingType = typeReference
				.orElseThrow(() -> new IOException("Unable to unmarshal :: Type = null"));
		log.debug("Trying to unmarshal json message {} into {} type", incomingMessage, incomingType);
		return mapper.readValue(incomingMessage, incomingType);
	}

	public static final <T> T unmarshal(final Optional<String> message, String canonicalName) throws IOException {
		String incomingMessage = message.orElseThrow(() -> new IOException("Unable to unmarshal :: Message = null"));
		JavaType javaType = TypeFactory.defaultInstance().constructFromCanonical(canonicalName);
		log.debug("Trying to unmarshal json message {} into {} type", incomingMessage, javaType);
		return mapper.readValue(incomingMessage, javaType);
	}

	public static <E> String marshal(final Optional<E> obj) throws IOException {
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(obj.orElseThrow(() -> new IOException("Unable to marshal :: obj = null")));
	}
}
