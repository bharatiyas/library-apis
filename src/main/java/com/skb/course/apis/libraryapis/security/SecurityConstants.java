package com.skb.course.apis.libraryapis.security;

import javax.sql.rowset.serial.SerialBlob;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 1800000;
    public static final String SIGNING_SECRET = "MyApiSecret";
    public static final String NEW_USER_DEFAULT_PASSWORD = "Password123";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    public static final String NEW_USER_REGISTRATION_URL = "/v1/users";
}