package com.codestates.member.entity;

import com.codestates.stamp.Stamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
//파라미터가 없는 디폴트 생성자 추가, Entity에 디폴트 생성자가 존재x 데이터 조회 시 에러
@Getter
@Setter
@Entity
public class Member extends Auditable {
    //    {@link com.codestates.audit.Auditable} 클래스는 엔티티 클래스마다 공통으로 존재하는 엔티티 생성일, 수정일, 작성자 등의
// *                 필드를 공통화한 뒤, 엔티티에 대한 이벤트 발생 시 해당 필드의 값을 자동으로 채워주는 기능을 합니다.
    @Id //pk 매핑
    @GeneratedValue(strategy = GenerationType.IDENTITY)//db가 알아서 기본키 생성
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 13, nullable = false, unique = true)
    private String phone;

    @Enumerated(value = EnumType.STRING) // enum 이름을 db에 저장
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @OneToMany(mappedBy = "member") //주문이 멤버의 주인
    // 연관관계 중 하나를 정해서 테이블의 외래키를 관리 : 연관 관계의 주인
    // 주인이면 db연관 관계와 매핑 /등록 수정 삭제 ok, 주인 x : 읽기만 됨
    // 주인 mappdby x
    private List<Order> orders = new ArrayList<>();
    //참조 객체, order정보 알수있음

    //CascadeType.PERSIST : 엔티티를 영속화할 때 연관된 엔티티도 함께 유지
    // CascadeType.REMOVE : 엔티티를 제거할 때 연관된 엔티티도 모두 제거
    @OneToOne(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Stamp stamp;
    // STAMP에 MEMBER의 기본키인 MEMBER_ID를 외래키로 포함시키느냐하면
    // 아까 말한 회원이 존재하지 않는데 스탬프라는게 의미가 없기 때문에 MEMBER를 핵심인 주 테이블로 생각하고,
    // STAMP를 서브 테이블로 생각하기 때문에 이렇게 만든거에요.

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    //애너테이션으로 setter를 추가했지만 양방향 연관관계를 안전하게 매핑하기 위해 order쪽에도 member를 추가
    public void setOrder(Order order) {
        orders.add(order);
        if (order.getMember() != this) {
            order.setMember(this);
        }
    }
    //일단 order에 member를 세팅해서 연관관계를 맺는데 둘 사이가 양방향 관계이다보니 파라미터로 전달받는 member도 order와 미리 관계를 맺어주는거구요.
    // 일종의 편의 메서드에요. 이렇게 하면 한쪽에만 관계를 맺어주고 나머지 한쪽은 개발자가 신경쓰지 않아도 되기때문에 엔티티간에 관계가 맺어지지않는 오류를 예방할 수 있는 효과


        public void setStamp(Stamp stamp) {
            this.stamp = stamp;
            if (stamp.getMember() != this) {
                stamp.setMember(this);  // 스템프에 멤버가 없으면 멤버 넣어주기(안전)
    }

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }
}
