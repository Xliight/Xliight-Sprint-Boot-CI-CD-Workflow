package com.ouitrips.app.services.messagingsystemservice.sms;

public class SmsProviders {
    private SmsProviders() {
        throw new IllegalStateException("Utility class");
    }
    protected static final DataApiProviderSms[] LIST_SMS_PROVIDERS={
            new DataApiProviderSms("0ae784ddc89767a4eaa89ab26893faa1-51c64e4e-a6f3-44f1-b8e7-ea7963631235",
                    "https://ymvep.api.infobip.com/sms")
            //add another service sms providers
    };
}
