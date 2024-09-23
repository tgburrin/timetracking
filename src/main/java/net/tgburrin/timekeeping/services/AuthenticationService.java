package net.tgburrin.timekeeping.services;

import jakarta.servlet.http.HttpServletRequest;
import net.tgburrin.timekeeping.exceptions.InvalidApiTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public static boolean getApiAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (!AUTH_TOKEN.equals(apiKey))
            throw new InvalidApiTokenException("Invalid API Key");

        return true;
    }
}
