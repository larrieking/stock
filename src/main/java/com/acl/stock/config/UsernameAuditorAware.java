package com.acl.stock.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author stephen.obi
 */
public class UsernameAuditorAware implements AuditorAware<String> {

    @SuppressWarnings("NullableProblems")
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("System.auto");
    }
}