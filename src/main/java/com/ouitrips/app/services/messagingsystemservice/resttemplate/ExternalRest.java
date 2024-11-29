package com.ouitrips.app.services.messagingsystemservice.resttemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.services.messagingsystemservice.sms.SMSList;
import com.ouitrips.app.services.messagingsystemservice.sms.SmsResponse;
import com.ouitrips.app.services.messagingsystemservice.sms.SmsStatusData;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExternalRest {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String ERROR_SENDING_SMS = "Error sending SMS ";
    private ExternalRest() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }
    public static ResponseEntity<String> callRestApi(String externalApiUrl){//todo check if this method will stay static
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(externalApiUrl, String.class);
    }
    public static SmsResponse callRestApiSms(String baseUrl, String apiKey, Map<String, List<Map<String, Object>>> postData){
        try {
            String endPointSendAdvancedSms = "/2/text/advanced";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION_HEADER, "App " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(ACCEPT_HEADER, MediaType.APPLICATION_JSON_VALUE);
            String postDataJson = new ObjectMapper().writeValueAsString(postData);
            HttpEntity<String> requestEntity = new HttpEntity<>(postDataJson, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseUrl+endPointSendAdvancedSms, requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                int httpCode = responseEntity.getStatusCode().value();
                SmsResponse arrayParams = new SmsResponse();
                arrayParams.setResponse(responseBody);
                arrayParams.setHttpCode(httpCode);
                arrayParams.setResponseBody(responseBody);
                return arrayParams;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_SENDING_SMS+e.getMessage());
        }
    }
    public static Boolean callRestApiPauseSmsScheduler(String baseUrl, String apiKey, Map<String, String> postData, String bulkId){
        try {
            String endPointPauseSms = "/1/bulks/status?bulkId=";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION_HEADER, "App " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(ACCEPT_HEADER, MediaType.APPLICATION_JSON_VALUE);
            String postDataJson = new ObjectMapper().writeValueAsString(postData);
            HttpEntity<String> requestEntity = new HttpEntity<>(postDataJson, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl+endPointPauseSms+bulkId, HttpMethod.PUT, requestEntity, String.class);
            return responseEntity.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_SENDING_SMS+e.getMessage());
        }
    }

    public static SmsStatusData callRestApiStatusSmsScheduler(String baseUrl, String apiKey, String bulkId){
        try {
            String endPointPauseSms = "/1/bulks/status?bulkId=";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION_HEADER, "App " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(ACCEPT_HEADER, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl+endPointPauseSms+bulkId, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                return convertJsonToDataResponse(responseBody);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_SENDING_SMS+e.getMessage());
        }
    }

    public static SMSList callRestApiSMSLogs(String baseUrl, String apiKey) {
        try {
            String endPointSMSLogs = "/2/logs";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION_HEADER, "App " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(ACCEPT_HEADER, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl+endPointSMSLogs, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                return convertJsonToListLogs(responseBody);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendSmsException(ERROR_SENDING_SMS+e.getMessage());
        }
    }

    public static SmsStatusData convertJsonToDataResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, SmsStatusData.class);
        } catch (IOException e) {
            throw new ExceptionControllerAdvice.GeneralException("Error converting object "+e.getMessage());
        }
    }
    public static SMSList convertJsonToListLogs(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        } catch (IOException e) {
            throw new ExceptionControllerAdvice.GeneralException("Error converting object"+e.getMessage());
        }
    }
}