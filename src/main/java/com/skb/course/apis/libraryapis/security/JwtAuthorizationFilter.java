package com.skb.course.apis.libraryapis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        if(authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            authenticationToken = getAuthentication(authorizationHeader);
        } catch (JWTVerificationException verificationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, Object> data = new HashMap<>();
            data.put(
                    "timestamp",
                    Calendar.getInstance().getTime());
            data.put(
                    "exception",
                    verificationException.getMessage());

            try {
                response.getOutputStream()
                        .println( "Token Expire. Please login again!!");


            } catch (IOException e) {
                e.printStackTrace();
            }

            chain.doFilter(request, response);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

   /* @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put(
                "timestamp",
                Calendar.getInstance().getTime());
        data.put(
                "exception",
                failed.getMessage());

        try {
            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private UsernamePasswordAuthenticationToken getAuthentication(String authorizationHeader) {

        if(authorizationHeader != null) {
            String userNameFromJwt = JWT.require(HMAC512(SecurityConstants.SIGNING_SECRET.getBytes()))
                                    .build()
                                    .verify(authorizationHeader.replace(SecurityConstants.BEARER_TOKEN_PREFIX, ""))
                                    .getSubject();

            if(userNameFromJwt != null) {
                return new UsernamePasswordAuthenticationToken(userNameFromJwt, null, new ArrayList<>());
            }
        }

        return null;
    }
}
