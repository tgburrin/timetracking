package net.tgburrin.timekeeping;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.tgburrin.timekeeping.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final HashSet<AntPathRequestMatcher> okPaths = new HashSet<>(Arrays.asList(
            (new AntPathRequestMatcher("/v3/api-docs/**")),
            (new AntPathRequestMatcher("/swagger-ui/**")),
            (new AntPathRequestMatcher("/actuator/**"))
    ));
    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse resp,
            FilterChain filter) throws ServletException, IOException {
        boolean allowRequest = false;
        for(AntPathRequestMatcher m : okPaths)
            if(m.matches(req))
                allowRequest = true;

        try {
            if (!allowRequest && AuthenticationService.getApiAuthentication(req))
                allowRequest = true;
        } catch (InvalidApiTokenException ex) {
            ApiError err = ExceptionResponse.returnErrorBody(ex);
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.getOutputStream().println(objectMapper.writeValueAsString(err));
        }

        if (allowRequest)
            filter.doFilter(req, resp);
    }
}
