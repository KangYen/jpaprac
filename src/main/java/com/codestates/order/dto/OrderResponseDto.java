package com.codestates.order.dto;

import com.codestates.order.entity.Order;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private long orderId;
    private long memberId;
    private Order.OrderStatus orderStatus;
    private List<OrderCoffeeResponseDto> orderCoffees;
    private LocalDateTime createdAt;

    /** 제거됨
     public void setMember(Member member) {
     this.memberId = member.getMemberId();
     }
     */ // 왜 제거 된거야
}
