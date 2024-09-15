package net.tgburrin.timekeeping.AuthPermission;
import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import net.tgburrin.timekeeping.services.AuthenticationService;

public class ApiTokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse resp,
                                    FilterChain chain) throws ServletException, IOException {
        String inToken = req.getHeader(AuthenticationService.AUTH_TOKEN_HEADER_NAME);
        if(inToken != null)
            SecurityContextHolder.getContext().setAuthentication(
                    AuthenticationService.getApiAuthentication(req)
            );
        chain.doFilter(req, resp);
    }
}
