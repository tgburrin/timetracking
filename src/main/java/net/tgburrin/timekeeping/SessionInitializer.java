package net.tgburrin.timekeeping;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {
    public SessionInitializer() {super(SessionConfig.class);}
}
