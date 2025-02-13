package com.teamFresh.order.entity;

import com.teamFresh.order.dto.OrderItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    @DisplayName("상품명 호출")
    void getName () {
        assertThat(
                OrderItemDto.of(
                        OrderItem.createOrderItem(
                                Order.createOrder("tester", "서울"),
                                Product.createProduct("A", 10),
                                9
                        )
                ).getName()
        ).isEqualTo(
                "A"
        );
    }
}