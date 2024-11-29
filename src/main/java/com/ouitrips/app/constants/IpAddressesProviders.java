package com.ouitrips.app.constants;

public class IpAddressesProviders {
    private IpAddressesProviders() {
        throw new IllegalStateException("Utility class");
    }
    protected static final String[] LIST_PROVIDERS={"https://api.ipify.org?format=json"};//,"http://ip-api.com/json"
}