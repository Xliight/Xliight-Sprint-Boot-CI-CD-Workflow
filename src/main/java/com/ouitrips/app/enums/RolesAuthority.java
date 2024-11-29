package com.ouitrips.app.enums;

import java.util.List;

public final class RolesAuthority {

    private static final String USER_READ = "user:read";
    private static final String USER_UPDATE = "user:update";
    private static final String USER_CREATE = "user:create";
    private static final String USER_DELETE = "user:delete";

    private static final List<String> USER_AUTHORITIES = List.of(USER_READ);
    private static final List<String> HR_AUTHORITIES = List.of(USER_READ, USER_UPDATE);
    private static final List<String> MANAGER_AUTHORITIES = List.of(USER_READ, USER_UPDATE);
    private static final List<String> ADMIN_AUTHORITIES = List.of(USER_READ, USER_CREATE, USER_UPDATE);
    private static final List<String> SUPER_ADMIN_AUTHORITIES = List.of(USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE);


    public RolesAuthority() {
        throw new IllegalStateException("Utility class");
    }

    public static List<String> getUserAuthorities() {
        return USER_AUTHORITIES;
    }

    public static List<String> getHrAuthorities() {
        return HR_AUTHORITIES;
    }

    public static List<String> getManagerAuthorities() {
        return MANAGER_AUTHORITIES;
    }

    public static List<String> getAdminAuthorities() {
        return ADMIN_AUTHORITIES;
    }

    public static List<String> getSuperAdminAuthorities() {
        return SUPER_ADMIN_AUTHORITIES;
    }
}