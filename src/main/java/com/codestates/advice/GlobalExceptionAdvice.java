package com.codestates.advice;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.hql.internal.ast.ErrorReporter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
//각 Controller 클래스에서 발생하는 예외를 GlobalExceptionAdvice 클래스에서 공통으로 처리

public class GlobalExceptionAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //DTO 클래스의 유효성 검증에서 발생하는 MethodArgumentNotValidException
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e ) {
        final ErrorResponse response = ErrorResponse.of(e.getBindingResult());
        //주로 객체 생성시 어떤 값들의(of~) 객체를 생성
// getBindingResult() 에서는 각각의 데이터가 바인딩 된 결과(Valiation 결과)를 하나씩 반환
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException e) {
        //URI 변수로 넘어오는 값의 유효성 검증에 대한 에러(ConstraintViolationException)
        final ErrorResponse response = ErrorResponse.of(e.getConstraintViolations());

        return response;
    }

    @ExceptionHandler
    // handleBusinessLogicException 비즈니스 요구사항에 따른 Exception
    //요구사항에 맞게 개발자가 직접 Exception을 발생시키는 것들이 Business Exception
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        final ErrorResponse response = ErrorResponse.of(e.getExceptionCode());

        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getExceptionCode()
                .getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(
            HtttpMessageNotReadableException e) {

        final ErrorResponse response = ErrorResponse.of(HttpStatus_BAD_REQUEST,
                "Required request body is missing");

        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParamParameterException(
            MissingServletRequestParameterException e ) {

        final ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());

        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error("# handle Exception", e);
        // TODO 애플리케이션의 에러는 에러 로그를 로그에 기록하고, 관리자에게 이메일이나 카카오 톡,
        //  슬랙 등으로 알려주는 로직이 있는게 좋습니다.

        final ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);

        return response;
    }
}
