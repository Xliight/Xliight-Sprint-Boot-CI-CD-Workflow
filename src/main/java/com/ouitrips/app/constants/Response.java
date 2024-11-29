package com.ouitrips.app.constants;

import org.springframework.http.ResponseEntity;

import java.util.Map;

import static com.ouitrips.app.constants.RequestNameConstant.*;
import static com.ouitrips.app.constants.ResponseObjectConstant.*;
import static com.ouitrips.app.constants.StatusCodeConstant.STATUS_ERROR;
import static com.ouitrips.app.constants.StatusCodeConstant.STATUS_OK;

public class Response {

    private Response() {
        throw new IllegalStateException("Utility class");
    }
    public static ResponseEntity<?> addedSuccessMessage() {
        return ResponseEntity.ok(
                Map.of(
                        REQUEST_STATUS, STATUS_OK,
                        REQUEST_MESSAGE, RESPONSE_ADD_SUCCESS_MSG
                )
        );
    }

    public static ResponseEntity<?> updatedSuccessMessage() {
        return ResponseEntity.ok(
                Map.of(
                        REQUEST_STATUS, STATUS_OK,
                        REQUEST_MESSAGE, RESPONSE_UPDATE_SUCCESS_MSG
                )
        );
    }

    public static ResponseEntity<?> deletedSuccessMessage() {
        return ResponseEntity.ok(
                Map.of(
                        REQUEST_STATUS, STATUS_OK,
                        REQUEST_MESSAGE, RESPONSE_DELETE_SUCCESS_MSG
                )
        );
    }

    public static ResponseEntity<?> savedSuccessMessage() {
        return ResponseEntity.ok(
                Map.of(
                        REQUEST_STATUS, STATUS_OK,
                        REQUEST_MESSAGE, RESPONSE_SAVE_SUCCESS_MSG
                )
        );
    }

    public static ResponseEntity<?> successMessage(Object data) {
        return ResponseEntity.ok(
                Map.of(
                        REQUEST_STATUS, STATUS_OK,
                        REQUEST_MESSAGE, data
                )
        );
    }

    public static ResponseEntity<?> errorMessage(Object data) {
        return ResponseEntity.ok(
                Map.of(
                        REQUEST_STATUS, STATUS_ERROR,
                        REQUEST_MESSAGE, data
                )
        );
    }

    public static ResponseEntity<?> responseData(Object data) {
        return ResponseEntity.ok(
                Map.of(
                        REQUEST_STATUS, STATUS_OK,
                        REQUEST_DATA, data
                )
        );
    }
}