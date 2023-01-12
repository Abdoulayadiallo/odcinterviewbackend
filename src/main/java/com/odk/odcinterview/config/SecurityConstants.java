package com.odk.odcinterview.config;

public class SecurityConstants {
	
	public static final String SECRET = "^[a-zA-Z0-9._]+$\r\nGuidelines89797987forAlphabeticalArraNumeralsandOtherSymbo$";
	public static final long EXPIRATION_TIME = 10*1000; // 1 jour
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_TYPE = "Authorization";
	public static final String CLIENT_DOMAIN_URL = "*";
}
