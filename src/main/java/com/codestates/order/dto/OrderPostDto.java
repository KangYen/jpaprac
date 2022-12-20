package com.codestates.order.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderPostDto {
    @Positive
    private long memberId;

    @NotNull
    @Valid
    private List<OrderCoffeeDto> orderCoffees;
}
