package com.ouitrips.app.services.messagingsystemservice.sms;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.services.messagingsystemservice.resttemplate.ExternalRest;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class SmsService {
    private final FreeMarkerConfigurer freemarkerConfigurer;
    private static final String STATUS_KEY = "status";
    private static final String ERROR_UPDATING_SMS = "Error updating sms: ";
    public SmsResponse sendSms(String from,
                               String to,
                               String content,
                               Map<String, Object> templateModel,
                               String templateName,
                               Date sendAt) {
        try {
            SmsResponse smsResponse=null;
            for (DataApiProviderSms apiProviderSms :
                    SmsProviders.LIST_SMS_PROVIDERS) {
                if (content != null){
                    Map<String, List<Map<String, Object>>> resultMap = getStringListMap(from, to, content, sendAt);
                    smsResponse = ExternalRest.callRestApiSms(apiProviderSms.getBaseUrl(), apiProviderSms.getApiKey(), resultMap);
                }else {
                    Map<String, List<Map<String, Object>>> resultMap = getStringListMap(from, to, getBodyTemplate(templateModel,templateName), sendAt);
                    smsResponse = ExternalRest.callRestApiSms(apiProviderSms.getBaseUrl(), apiProviderSms.getApiKey(), resultMap);
                    if (smsResponse != null) {
                        break;
                    }
                }
            }
            return smsResponse;
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException("Error sending sms: "+e.getMessage());
        }
    }

    public void cancelSms(String bulkId) {//todo check which provider you will paused it
        try {
            for (DataApiProviderSms apiProviderSms :
                SmsProviders.LIST_SMS_PROVIDERS) {
                if(!ExternalRest.callRestApiPauseSmsScheduler(
                        apiProviderSms.getBaseUrl(),
                        apiProviderSms.getApiKey(),
                        Map.of(STATUS_KEY,"CANCELED"),
                        bulkId
                )) break;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_UPDATING_SMS+e.getMessage());
        }
    }
    public void pauseSms(String bulkId) {//todo check which provider you will paused it
        try {
            for (DataApiProviderSms apiProviderSms :
                    SmsProviders.LIST_SMS_PROVIDERS) {
                if(!ExternalRest.callRestApiPauseSmsScheduler(
                        apiProviderSms.getBaseUrl(),
                        apiProviderSms.getApiKey(),
                        Map.of(STATUS_KEY,"PAUSED"),
                        bulkId
                )) break;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_UPDATING_SMS+e.getMessage());
        }
    }
    public void processingSms(String bulkId) {//todo check which provider you will paused it
        try {
            for (DataApiProviderSms apiProviderSms :
                    SmsProviders.LIST_SMS_PROVIDERS) {
                if(!ExternalRest.callRestApiPauseSmsScheduler(
                        apiProviderSms.getBaseUrl(),
                        apiProviderSms.getApiKey(),
                        Map.of(STATUS_KEY,"PROCESSING"),
                        bulkId
                )) break;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_UPDATING_SMS+e.getMessage());
        }
    }

    public SmsStatusData getStatusSmsScheduler(String bulkId) {//todo check which provider you will paused it
        try {
            for (DataApiProviderSms apiProviderSms :
                    SmsProviders.LIST_SMS_PROVIDERS) {
                SmsStatusData smsStatusData = ExternalRest.callRestApiStatusSmsScheduler(
                        apiProviderSms.getBaseUrl(),
                        apiProviderSms.getApiKey(),
                        bulkId
                );
                if(smsStatusData==null)
                    break;
                else
                    return smsStatusData;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_UPDATING_SMS+e.getMessage());
        }
        return null;
    }

    public SMSList getSMSLogs() {
        try {
            for (DataApiProviderSms apiProviderSms :
                    SmsProviders.LIST_SMS_PROVIDERS) {
                SMSList smsLogs = ExternalRest.callRestApiSMSLogs(
                        apiProviderSms.getBaseUrl(),
                        apiProviderSms.getApiKey()
                );
                if(smsLogs==null)
                    break;
                else
                    return smsLogs;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_UPDATING_SMS+e.getMessage());
        }
        return null;
    }

    public String getBodyTemplate(
            Map<String, Object> templateModel,
            String templateName) throws IOException, TemplateException {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                .getTemplate("sms/"+templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
    }

    private static Map<String, List<Map<String, Object>>> getStringListMap(String from, String to, String text, Date sendAt) {
        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("from", from);
        List<Map<String, String>> destinations = new ArrayList<>();
        Map<String, String> destination = new HashMap<>();
        destination.put("to", to);
        destinations.add(destination);
        message.put("destinations", destinations);
        message.put("text", text);
        if(sendAt!=null)
        {
            message.put("sendAt", sendAt);
        }
        messages.add(message);
        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
        resultMap.put("messages", messages);
        return resultMap;
    }
}