package com.codestates.order.entity;

import com.codestates.audit.Auditable;
import com.codestates.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "ORDERS")
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Menber member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderCoffee> orderCoffees = new ArrayList<>();
    // order 객체만 영속성 컨텍스트에 영속화(persist)하면 order와 연관관계 매핑이 되어 있는 객체까지 영속화됩니다.
    //JPA에서는 persist()를 호출하면 영속화 되지만, Spring Data JPA에서는 orderRepository.save(order)를 호출하면
    // order 뿐만 아니라 orderCoffee까지 영속화 되고,내부적으로 flush()가 호출되므로 DB의 테이블(ORDER, ORDER_COFFEE)에 모두 INSERT 됩니다.

    public void setMember(Member member) {
        this.member = member;
        if(!this.member.getOrders().contain(this)) {
            this.member.getOrders().add(this);
        }

        public enum OrderStatus {
            ORDER_REQUEST( 1, "주문 요청"),
            ORDER_CONFIRM(2, "주문 확정"),
            ORDER_COMPLETE(3, "주문 처리 완료"),
            ORDER_CANCEL(4, "주문 취소");

            @Getter
            private int stepNumber;

            @Getter
            private String stepDescription;

            OrderStatus(int stepNumber, String stepDescription) {
                this.stepNumber = stepNumber;
                this.stepDescription = stepDescription;
            }
        }
    }
}
