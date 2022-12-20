package com.codestates.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//1. 변수 위에 사용하는 어노테이션이기 때문에 Target은 FIELD로 설정해줍니다.
@Target(ElementType.FIELD)
//@Retention  애노테이션이 언제까지 살아 남아 있을지를 정하는 것
//2. 어노테이션의 유지범위는 실행하는 동안으로 설정해주기 위해 Retention을 RUNTIME으로 설정해줍니다
@Retention(RetentionPolicy.RUNTIME)
//3. 검증 클래스를 지정해주기 위해 validateBy로 클래스를 지정해줍니다.
@Constraint(validatedBy = {NotSpaceValidator.class})
public interface NotSpace {
    String message() default "공백이 아니어야 합니다";    // 4. 기본적인 메세지를 설정할 수 있습니다.

    Class<?>[] groups() default {};
    //제네릭타입<?> : 제한 없음
    //모든 클래스나 인터페이스 타입이 올 수 있다.
    //제네릭타입<? extends 상위타입> : 상위 클래스 제한
    //제네릭타입<? super 하위타입> : 하위 클래스 제한
    Class<? extends Payload>[] payload() default {};
}
