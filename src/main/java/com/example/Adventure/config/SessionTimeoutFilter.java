package com.example.Adventure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SessionTimeoutFilter extends OncePerRequestFilter {

    private final SessionTimeoutHandler sessionTimeoutHandler;
    private static final Logger log = LoggerFactory.getLogger(SessionTimeoutFilter.class);

    public SessionTimeoutFilter(SessionTimeoutHandler sessionTimeoutHandler) {
        this.sessionTimeoutHandler = sessionTimeoutHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
            // セッションが切れた場合のログ
            log.info("Session has timed out. Redirecting to /login?expired");
            sessionTimeoutHandler.onAuthenticationFailure(request, response, new SessionAuthenticationException("Session has timed out"));
        } else {
            filterChain.doFilter(request, response);
        }
    }

}

