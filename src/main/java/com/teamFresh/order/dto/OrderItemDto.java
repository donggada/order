package com.teamFresh.order.dto;

import com.teamFresh.order.entity.OrderItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class OrderItemDto {
    private Long id;
    private String name;
    private int quantity;

    private OrderItemDto(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static OrderItemDto of (OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProductId(), orderItem.getProductName(), orderItem.getQuantity());
    }

    public static OrderItemDto of (Long id, String name, int quantity) {
        return new OrderItemDto(id, name, quantity);
    }
}


