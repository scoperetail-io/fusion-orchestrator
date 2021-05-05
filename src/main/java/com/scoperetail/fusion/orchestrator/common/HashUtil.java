package com.scoperetail.fusion.orchestrator.common;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

	private static final String SHA3_512 = "SHA3-512";


	public static byte[] digest(byte[] input, String algorithm) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		return md.digest(input);
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public static String getHash(String input) {
		byte[] shaInBytes = digest(input.getBytes(UTF_8), SHA3_512);
		return bytesToHex(shaInBytes);
	}

}