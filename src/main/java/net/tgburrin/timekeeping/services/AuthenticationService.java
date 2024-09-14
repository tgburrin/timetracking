package net.tgburrin.timekeeping.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import net.tgburrin.timekeeping.AuthPermission.SpringAuthentication;

@Component
public class AuthenticationService {
    public static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static String AUTH_TOKEN;
    @Value("${apiToken}")
    public void setApiToken(String apiToken) {
        if (AUTH_TOKEN == null)
            AUTH_TOKEN = apiToken;
        else
            throw new IllegalArgumentException("The token may not be changed after server start");
    }
    public static String getAuthToken() {
        return AUTH_TOKEN;
    }

    public static boolean testApiAuthentication(String inToken) {
        return AUTH_TOKEN.equals(inToken);
    }

    public static Authentication getApiAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !AUTH_TOKEN.equals(apiKey))
            throw new BadCredentialsException("Invalid API Key");

        return new SpringAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
