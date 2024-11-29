package com.ouitrips.app.constants;

public class StatusCodeConstant {
    private StatusCodeConstant() {
        throw new IllegalStateException("Utility class");
    }
    public static final String STATUS_OK = "OK";
    public static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
    public static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    public static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    public static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    public static final String STATUS_DATABASE = "ERROR_DATABASE";
    public static final String STATUS_ERROR = "ERROR";
}
