package com.teamFresh.order.entity;

import com.teamFresh.order.exception.OutOfStockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("주문량 < 재고 경우")
    void decreaseStock_1() {
        Product product = Product.createProduct("test", 100);
        product.decreaseStock(90);
        Assertions.assertThat(product.getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("주문량 = 재고 경우")
    void decreaseStock_2() {
        Product product = Product.createProduct("test", 100);
        product.decreaseStock(100);
        Assertions.assertThat(product.getStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문량 > 재고 경우")
    void decreaseStock_3() {
        Product product = Product.createProduct("test", 100);
        OutOfStockException exception = assertThrows(OutOfStockException.class, () -> product.decreaseStock(110));
        assertEquals(exception.getMessage(), "재고가 부족합니다.");
    }

}