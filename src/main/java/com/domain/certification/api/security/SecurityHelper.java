package com.domain.certification.api.security;

import com.domain.certification.api.util.enumerator.RoleType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SecurityHelper {

    public static boolean isSignedInUserAdmin() {
        return signedInUserHasRole(RoleType.ADMIN.name());
    }

    public static boolean isSignedInUserStudent() {
        return signedInUserHasRole(RoleType.STUDENT.name());
    }

    private static boolean signedInUserHasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated())
            return false;

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null || authorities.size() == 0)
            return false;

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(role)) {
                return true;
            }
        }

        return false;
    }
}
