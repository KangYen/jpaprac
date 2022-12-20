package com.codestates.order.dto;

import com.codestates.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCoffeeDto {
    private long orderId;
    private long memberId;
    private Order.OrderStatus orderStatus;
    private List<OrderCoffeeResponseDto> orderCoffees;
    private LocalDateTime createdAt;

    /** 제거됨
     public void setMember(Member member) {
     this.memberId = member.getMemberId();
     }
     */
}
