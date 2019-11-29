package com.skb.course.apis.libraryapis.testutils;

import com.skb.course.apis.libraryapis.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class LibraryApiIntegrationTestUtil {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Value("${library.api.user.admin.username}")
    private String adminUsername;

    @Value("${library.api.user.admin.password}")
    private String adminPassword;

    private ResponseEntity<String> adminLoginResponse;

    public ResponseEntity<User> registerNewUser(String username) {

        URI registerUri = null;

        try {
            registerUri = new URI(TestConstants.USER_API_REGISTER_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpEntity<User> newUserRequest = new HttpEntity<>(LibraryApiTestUtil.createUser(username));
        return testRestTemplate.postForEntity(registerUri, newUserRequest, User.class);
    }

    public ResponseEntity<String> loginUser(String username, String password) {

        if (username.equals("adminUsername") && (adminLoginResponse != null)) {
            return adminLoginResponse;
        }
        URI loginUri = null;

        try {
            loginUri = new URI(TestConstants.LOGIN_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpEntity<String> loginRequest = new HttpEntity<>(createLoginBody(username, password));
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(loginUri, loginRequest, String.class);

        if(username.equals("adminUsername")) {
            adminLoginResponse = responseEntity;
        }

        return responseEntity;
    }

    private String createLoginBody(String username, String password) {
        return "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
    }

    public MultiValueMap<String, String> createAuthorizationHeader(String bearerToken) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", bearerToken);
        return headers;
    }
}
