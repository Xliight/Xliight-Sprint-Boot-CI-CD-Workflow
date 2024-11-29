package com.ouitrips.app.config;

public class CorsOriginAllowed {
    protected static final String[] ORIGIN_ALLOWED = {"*"};

    private CorsOriginAllowed() {
        throw new IllegalStateException("Utility class");
    }
}
