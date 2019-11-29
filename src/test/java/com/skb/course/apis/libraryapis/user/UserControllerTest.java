package com.skb.course.apis.libraryapis.user;

import com.skb.course.apis.libraryapis.testutils.LibraryApiIntegrationTestUtil;
import com.skb.course.apis.libraryapis.testutils.LibraryApiTestUtil;
import com.skb.course.apis.libraryapis.testutils.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    LibraryApiIntegrationTestUtil libraryApiIntegrationTestUtil;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void registerUser_success() {

        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("register.user.success");

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User responseLibUser = response.getBody();
        Assert.assertNotNull(responseLibUser);
        Assert.assertNotNull(responseLibUser.getUserId());
        Assert.assertNotNull(responseLibUser.getPassword());
        Assert.assertTrue(responseLibUser.getUsername().contains("register.user.success"));
        Assert.assertNotNull(responseLibUser.getRole());
    }

    @Test
    public void getUser_success() {

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("get.user.success");

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User user = response.getBody();
        Assert.assertNotNull(user);

        Integer userId = user.getUserId();

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(user.getUsername(), user.getPassword());

        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        URI getUserUri = null;
        // /v1/users/{userId}

        try {
            getUserUri = new URI(TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        ResponseEntity<User> libUserResponse = testRestTemplate.exchange(getUserUri, HttpMethod.GET, new HttpEntity<Object>(headers),
                User.class);

        Assert.assertEquals(HttpStatus.OK, libUserResponse.getStatusCode());
        User libUser = libUserResponse.getBody();

        Assert.assertNotNull(libUser);
        Assert.assertNotNull(libUser.getUserId());
        Assert.assertTrue(libUser.getUsername().contains("get.user.success"));
        Assert.assertNotNull(libUser.getRole());
    }

    @Test
    public void getUser_user_doesnot_exist() {

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("get.user.doesnotexist");

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User user = response.getBody();
        Assert.assertNotNull(user);

        Integer userId = user.getUserId();

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(user.getUsername(), user.getPassword());

        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        URI getUserUri = null;
        // /v1/users/{userId}

        try {
            getUserUri = new URI(TestConstants.USER_API_BASE_URI + "/" + 1234);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        ResponseEntity<User> libUserResponse = testRestTemplate.exchange(getUserUri, HttpMethod.GET, new HttpEntity<Object>(headers),
                User.class);

        Assert.assertEquals(HttpStatus.FORBIDDEN, libUserResponse.getStatusCode());

    }
}