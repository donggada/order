package com.teamFresh.order.request;

import com.teamFresh.order.dto.OrderItemDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class OrderRequest {
    private String customerName;
    private String customerAddress;
    private List<OrderItemDto> orderItems;

    public OrderRequest(String customerName, String customerAddress, List<OrderItemDto> orderItems) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.orderItems = orderItems;
    }

    public static OrderRequest of (String customerName, String customerAddress, List<OrderItemDto> orderItems) {
        return new OrderRequest(customerName, customerAddress, orderItems);
    }

    public void addItems(OrderItemDto orderItemDto) {
        orderItems.add(orderItemDto);
    }
}
