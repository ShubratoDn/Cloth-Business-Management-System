package com.cloth.business.configurations.security;

import org.springframework.stereotype.Component;

@Component
public class CustomSecurityExpression {

    public boolean hasPermission(String... role) {

        return false;
    }
}