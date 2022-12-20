package com.codestates.response;
import javax.validation.ConstraintViolation;

import com.codestates.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//필요한 Error 정보만 담을 수 있는 Error 전용 Response 객체를
// 사용하면 클라이언트에게 조금 더 친절한 에러 정보를 제공할 수 있다.
@Getter
public class ErrorrResponse {
    private int status;
    private String message;
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> violationErrors;

    private  ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private ErrorResponse(final List<FieldError> fieldErrors,
                           final List<ConstraintViolationError> violationErrors) {
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;
    }

    public static  ErrorrResponse of(BindingResult bindingResult) {
        return new ErrorrResponse(FieldError.of(bindingResult), null);
    }

    public static ErrorrResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorrResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorrResponse of(ExceptionCode exceptionCode) {
        return new ErrorrResponse(exceptionCode.getStatus(), exceptionCode.getMessage());
    }

    public static ErrorrResponse of(HttpStatus httpStatus) {
        return new ErrorrResponse(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public static ErrorrResponse of(HttpStatus httpStatus, String message) {
        return new ErrorrResponse(httpStatus.value(), message);
    }

    @Getter
    public static  class FieldError {
        private String field;
        private Object rejectValue;
        private String reason;
    }

    public static List<FieldError> of(BindingResult bindingResult) {
        final List<org.springframework.validation.FieldError> fieldErrors =
                bindingResult.getFieldErrors();

        return fieldErrors.stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getRejectedValue() == null ?
                                "" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}

@Getter
public static class ConstraintViolationError{
    private String propertyPath;
    private Object rejectedValue;
    private String reason;

    private ConstraintViolationError(String propertyPath)
}