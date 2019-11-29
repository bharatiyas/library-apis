package com.skb.course.apis.libraryapis.testutils;

import com.skb.course.apis.libraryapis.model.common.Gender;

public class TestConstants {

    public static final String API_TRACE_ID = "Test-Trace-Id";

    // Test Publisher Details
    public static final String TEST_PUBLISHER_NAME = "TestPublisherName";
    public static final String TEST_PUBLISHER_EMAIL = "TestPublisherName@email.com";
    public static final String TEST_PUBLISHER_PHONE = "112-233-455";

    public static final String TEST_PUBLISHER_EMAIL_UPDATED = "TestPublisherNew@email.com";
    public static final String TEST_PUBLISHER_PHONE_UPDATED = "554-433-455";

    // Test Author Details
    public static final String TEST_AUTHOR_FIRST_NAME = "TestAuthorFn";
    public static final String TEST_AUTHOR_LAST_NAME = "TestAuthorLn";

    // Test User Details
    public static final String TEST_USER_FIRST_NAME = "TestUserFn";
    public static final String TEST_USER_LAST_NAME = "TestUserLn";
    public static final String TEST_USER_USERNAME = "test.username";
    public static final String TEST_USER_PASSWORD = "test.password";
    public static final String TEST_USER_EMAIL = TEST_USER_USERNAME + "@email.con";
    public static final String TEST_USER_PHONE = "223-344-566";
    public static final String TEST_USER_PHONE_UPDATED = "111-344-908";
    public static final Gender TEST_USER_GENDER = Gender.Female;

    // User API URIs
    public static final String USER_API_REGISTER_URI = "/v1/users";
    public static final String LOGIN_URI = "/login";
    public static final String USER_API_BASE_URI = "/v1/users";
}
