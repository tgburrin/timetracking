package net.tgburrin.timekeeping.AuthPermission;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class SpringAuthentication extends AbstractAuthenticationToken {
    private final String apiKey;

    public SpringAuthentication(String apiKey) {
        super(null);
        super.setAuthenticated(false);
        this.apiKey = apiKey;
    }
    public  SpringAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
        this.apiKey = apiKey;
    }
    @Override
    public Object getCredentials() {
        return null;
    }
    @Override
    public Object getPrincipal() {
        return apiKey;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
