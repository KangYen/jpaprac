package com.codestates.order.entity;

import com.codestates.audit.Auditable;

import javax.persistence.*;

public class OrderCoffee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCoffeeId;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(name = "COFFEE_ID")
    private Coffee coffee;

    public void setOrder(Order order) {
        this.order = order;
        if(!this.order.getOrderCoffees().contain(this)) {
            this.order.getOrderCoffees().add(this);
        }
    }
  public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
        if(!this.coffee.getOrderCoffees().contains(this)) {
            this.coffee.setOrderCoffee(this);
        }
  }
}
