package com.example.ChatBot.Configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

    private static final String COOKIE_NAME = "accessToken";

    private final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/auth/log-in",
            "/users", "/csrf"
    );
    @Override
    public String resolve(HttpServletRequest request) {

        if (PUBLIC_ENDPOINTS.contains(request.getServletPath())) {
            return null;
        }

        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {

            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
