package com.skb.course.apis.libraryapis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skb.course.apis.libraryapis.model.common.LibraryApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.PrintWriter;
import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
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
            String traceId = UUID.randomUUID().toString();
            logger.error("TraceId: {}, JWT Token Not Valid!!", traceId, verificationException);
            LibraryApiError errorResponse = new LibraryApiError(traceId, "Invalid JWT Token. Please Login Again!!");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            try {

                PrintWriter writer = response.getWriter();
                writer.print(objectMapper.writeValueAsString(errorResponse));
                writer.flush();
                return;
            } catch (IOException e) {
                logger.error("TraceId: {}, Error while writing response!!", traceId, e);
            }

        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

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
