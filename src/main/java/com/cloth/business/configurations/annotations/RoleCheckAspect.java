package com.cloth.business.configurations.annotations;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
import java.util.Arrays;

@Aspect
@Component
public class RoleCheckAspect {

    @Before("@annotation(checkRoles)")
    public void checkRoles(JoinPoint joinPoint, CheckRoles checkRoles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Not authenticated");
        }

        boolean hasRole = Arrays.stream(checkRoles.value())
                                .anyMatch(role -> authentication.getAuthorities()
                                .contains(new SimpleGrantedAuthority(role)));

        if (!hasRole) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
