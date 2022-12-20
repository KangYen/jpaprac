package com.codestates.exception;

//서비스 계층에서 사용할 BusinessLogicException이라는 Custom Exception을 정의

//BusinessLogicException은 RuntimeException을 상속하고 있으며 ExceptionCode를 멤버 변수로 지정하여
// 생성자를 통해서 조금 더 구체적인 예외 정보들을 제공해줄 수 있습니다.
        //그리고 상위 클래스인 RuntimeException의 생성자(super)로 예외 메시지를 전달

//@ResponseStatus 애너테이션은 고정된 HttpStatus를 지정하기 때문에 BusinessLogicException과 같이
// 다양한 Status를 동적으로 처리할 수 없으므로 ResponseEntity를 사용해서 HttpStatus를 동적으로 지정하도록 변경


import lombok.Getter;

public class BusinessLogicException extends  RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
