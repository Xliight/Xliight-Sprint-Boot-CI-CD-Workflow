package com.ouitrips.app.exceptions;

import com.ouitrips.app.listeners.ExceptionListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.Map;

import static com.ouitrips.app.constants.StatusCodeConstant.*;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionControllerAdvice {
    private final ExceptionListener exceptionListener;
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String errorMessage = "Required request parameter '" + parameterName + "' is missing";//todo we should change this
        return new ErrorResponse(STATUS_INVALID_REQUEST, errorMessage);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleMissingServletRequestParameter(UsernameNotFoundException ex) {
        String errorMessage = "Username or password is incorrect";
        return new ErrorResponse(STATUS_ERROR, errorMessage);
    }
    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        String errorMessage = "The access token provided is expired, revoked, malformed, or invalid for other reason";
        return new ErrorResponse(STATUS_REQUEST_DENIED, errorMessage);
    }
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSQLExceptionException(SQLException ex) {
        String errorMessage = "ERROR DATABASE";
        return new ErrorResponse(STATUS_DATABASE, errorMessage);
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        String errorMessage = "You don't have permission";
        return new ErrorResponse(STATUS_REQUEST_DENIED, errorMessage);
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException ex) {
        String errorMessage = "Full authentication is required to access this resource";
        return new ErrorResponse(STATUS_REQUEST_DENIED, errorMessage);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherException(Throwable ex) {
        exceptionListener.handleException(ex);
        String errorMessage = "A server internal error occurs: "+ex.getMessage();
        return new ErrorResponse(STATUS_ERROR, errorMessage);
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String errorMessage = "Resource not found";
        return new ErrorResponse(STATUS_ERROR, errorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(SignInUsernameNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(SignInUsernameNotFoundException ex) {
        return ResponseEntity.ok(
                Map.of("status", STATUS_ERROR,
                        "error_username", true));
    }
    public static class SignInUsernameNotFoundException extends RuntimeException {
        public SignInUsernameNotFoundException() {
            super();
        }
    }
    @ExceptionHandler(ErrorPasswordException.class)
    public ResponseEntity<?> handleErrorPasswordException(ErrorPasswordException ex) {
        return ResponseEntity.ok(
                Map.of("status", STATUS_ERROR,
                        "error_password", true));
    }
    public static class ErrorPasswordException extends RuntimeException {
        public ErrorPasswordException() {
            super();
        }
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<ErrorResponse> handleRegisterException(RegisterException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    public static class RegisterException extends RuntimeException {
        public RegisterException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<ErrorResponse> handleSendEmailException(SendEmailException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    public static class SendEmailException extends RuntimeException {
        public SendEmailException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(ObjectNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getContent());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    @Getter
    public static class ObjectNotFoundException extends RuntimeException {
        private final Object content;
        public ObjectNotFoundException(Object content) {
            this.content = content;
        }
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(GeneralException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getContent());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    @Getter
    public static class GeneralException extends RuntimeException {
        private final Object content;
        public GeneralException(Object content) {
            this.content = content;
        }
    }

    @ExceptionHandler(SendSmsException.class)
    public ResponseEntity<ErrorResponse> handleSendSmsException(SendSmsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    public static class SendSmsException extends RuntimeException {
        public SendSmsException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthenticatedException(NotAuthenticatedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    public static class NotAuthenticatedException extends RuntimeException {
        public NotAuthenticatedException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        ErrorResponse errorResponse = new ErrorResponse(STATUS_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    public static class NullPointerException extends RuntimeException {
        public NullPointerException(String message) {
            super(message);
        }
    }
}