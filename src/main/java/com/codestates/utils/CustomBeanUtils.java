package com.codestates.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

//CustomBeanUtils<T>
// 수정이 필요한 필드만 복사해서 업데이트해줌, patch정보의 경우 코드가 간결해지는 장점
// 서비스 클래스에서 변수를 수정해야할 때 길어지는 코드를 한 번에 변경하기 위해 리팩토링
@Component
public class CustomBeanUtils<T> {   //제네릭이 적용되어 있어서 아무 클래스에서나 사용 가능
    public T copyNonNullProperties(T source, T destination) {
        if (source == null || destination == null || source.getClass() != destination.getClass()) {
            return null;
        }

        final BeanWrapper src = new BeanWrapperImpl(source);    // 가져온 모든 필드를 저장할 변수
        final BeanWrapper dest = new BeanWrapperImpl(destination);  // 모든 필드들 중 변경한 값만 저장할 변수

        for (final Field property : source.getClass().getDeclaredFields()) {    //모든 필드를 가져와서
            Object sourceProperty = src.getPropertyValue(property.getName());       // 해당 필드 안에 들어있는 값을 가져옴
            //getPropertyValue() - BeanWrapper 에서 지원하는 메서드

            if (sourceProperty != null && !(sourceProperty instanceof Collection<?>)) { // null이 아닌 얘들만
                dest.setPropertyValue(property.getName(), sourceProperty);  //dest 에 저장 -> 수정하고자 하는 정보들만 업데이트 됨
            }
        }
        return destination; // 얘가 위의 모두를 포함하는 것이라 destination을 리턴
    }
}
