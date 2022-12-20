package com.codestates.audit;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
// 공통 매핑 정보가 필요할 때,
// 부모 클래스에 선언하고 속성만 상속 받아서 사용하고 싶을 때 @MappedSuperclass를 사용한다.
@MappedSuperclass
//@EntityListeners : JPA Entity에 이벤트가 발생할 때 콜백을 처리하고 코드를 실행하는 방법
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    // Entity가 생성된 날짜를 나타내는 필드에 추가
    private LocalDateTime createdAt;

    //Entity가 마지막으로 수정된 날짜를 나타내는 필드에 추가
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt;

    //Entity를 생성한 주체를 나타내는 필드에 추가
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}
