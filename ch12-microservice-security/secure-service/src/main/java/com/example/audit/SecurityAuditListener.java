package com.example.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class SecurityAuditListener {

    private static final Logger log = LoggerFactory.getLogger(SecurityAuditListener.class);

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.info("AUDIT: User '{}' successfully authenticated from IP: {}",
                event.getAuthentication().getName(),
                event.getAuthentication().getDetails()); // Details might contain remote address
    }

    @EventListener
    public void handleAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        log.warn("AUDIT: Failed authentication attempt for user '{}' from IP: {}",
                event.getAuthentication().getName(),
                event.getAuthentication().getDetails());
    }
}
