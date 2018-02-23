package com.domain.certification.api.config;

import com.domain.certification.api.util.dto.response.ResponseDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class ExceptionHandlerConfig {

    private final ErrorResponseBuilder errorResponseBuilder;
    private final MessageSource messageSource;

    public ExceptionHandlerConfig(
            ErrorResponseBuilder errorResponseBuilder,
            MessageSource messageSource
    ) {
        this.errorResponseBuilder = errorResponseBuilder;
        this.messageSource = messageSource;
    }


    @ExceptionHandler
    public ResponseEntity<ResponseDTO> getAllException(Exception exception) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        int statusCode;
        if (responseStatus != null) {
            statusCode = responseStatus.code().value();
        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        return errorResponseBuilder.createErrorResponse(
                exception.getMessage(),
                statusCode,
                exception.getClass().getSimpleName()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        Locale currentLocale = LocaleContextHolder.getLocale();
        List<String> messages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
            messages.add(localizedErrorMessage);
        }

        String message;
        if (messages.size() > 0) {
            message = messages.get(0);
        } else {
            message = "Internal Server Error, could not validate request.";
        }

        return errorResponseBuilder.createErrorResponse(
                message,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                exception.getClass().getSimpleName()
        );
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = "Invalid " + e.getName() + " value.";
        return errorResponseBuilder.createErrorResponse(
                message,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getClass().getSimpleName()
        );
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = "Invalid parameter value.";
        return errorResponseBuilder.createErrorResponse(
                message,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getClass().getSimpleName()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO> handleBadCredentialsExceptionException(BadCredentialsException e) {
        String message = "Username and password does not match.";
        return errorResponseBuilder.createErrorResponse(
                message,
                HttpStatus.UNAUTHORIZED.value(),
                e.getClass().getSimpleName()
        );
    }
}
