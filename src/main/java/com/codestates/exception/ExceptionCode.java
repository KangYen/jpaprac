package com.codestates.exception;

import lombok.Getter;

//MemberController의 handleException() 메서드에서 유효성 검사 실패에 대한 에러 메시지에 필 없는 정보도 있어서
// 에러 정보를 기반으로 한 Error Response 클래스를 만들어서 필요한 정보만 담은 후에 클라이언트 쪽에 전달
// ExceptionCode를 enum으로 정의하면 비즈니스 로직에서 발생하는 다양한 유형의 예외를 enum에 추가해서 사용 가능
public class ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXSISTS(409, "Member exists"),
    COFFEE_NOT_FOUND(404, "Coffee not found"),
    COFFEE_CODE_EXISTS(409, "Coffee Code exists"),
    ORDER_NOT_FOUND(404, "Order not found"),
    CANNOT_CHANGE_ORDER(403, "Order can not change"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    INVALID_MEMBER_STATUS(400, "Invalid member status"); // To 추가된 부분

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
