package com.teamFresh.order.severice;

import com.teamFresh.order.dto.OrderItemDto;
import com.teamFresh.order.entity.Product;
import com.teamFresh.order.exception.OutOfStockException;
import com.teamFresh.order.factoty.order.OrderServiceImp;
import com.teamFresh.order.repository.OrderItemRepository;
import com.teamFresh.order.repository.OrderRepository;
import com.teamFresh.order.repository.ProductRepository;
import com.teamFresh.order.request.OrderRequest;
import com.teamFresh.order.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceImpIntegrationTest {
    @Autowired
    private OrderServiceImp orderServiceImp;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("주문1개_상품1개 (주문량 < 재고)")
    void createOrder() {
        Product product = Product.createProduct("Product A", 100);
        productRepository.save(product);

        OrderRequest request = new OrderRequest("주문자1", "서울 강남구", List.of(OrderItemDto.of(product.getId(), product.getName(), 20)));

        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");

        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(orderItemRepository.count()).isEqualTo(1);
        assertThat(productRepository.findById(product.getId()).get().getStock()).isEqualTo(80);
    }

    @Test
    @DisplayName("주문1개_상품1개 (주문량 = 재고)")
    void createOrder2() {
        Product product = Product.createProduct("Product A", 100);
        productRepository.save(product);
        OrderRequest request = new OrderRequest("주문자1", "서울 강남구", List.of(OrderItemDto.of(product.getId(), product.getName(), 100)));

        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");

        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(orderItemRepository.count()).isEqualTo(1);
        assertThat(productRepository.findById(product.getId()).get().getStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문1개_상품1개 (주문량 > 재고)")
    void createOrder3() {
        Product product = Product.createProduct("Product A", 100);
        productRepository.save(product);

        OrderRequest request = new OrderRequest("주문자1", "서울 강남구", List.of(OrderItemDto.of(product.getId(), product.getName(), 110)));

        OrderResponse response = orderServiceImp.createOrder(request);

        assertEquals(response, OrderResponse.of(request, OutOfStockException.build(product.getId(), product.getStock(), 110)));
    }


    @Test
    @DisplayName("주문1개_상품여러개 (주문량 <= 재고)")
    void createOrder4() {
        Product productA = Product.createProduct("Product A", 100);
        Product productB = Product.createProduct("Product B", 50);
        productRepository.save(productA);
        productRepository.save(productB);

        OrderRequest request = new OrderRequest("주문자1", "서울 강남구",
                List.of(
                        OrderItemDto.of(productA.getId(), productA.getName(), 100),
                        OrderItemDto.of(productB.getId(), productB.getName(), 49)
                )
        );

        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");

        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(orderItemRepository.count()).isEqualTo(2);
        assertThat(productRepository.findById(productA.getId()).get().getStock()).isEqualTo(0);
        assertThat(productRepository.findById(productB.getId()).get().getStock()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문1개_상품여러개 (주문량 <= 재고)")
    void createOrder5() {
        Product productA = Product.createProduct("Product A", 100);
        productRepository.save(productA);

        OrderRequest request = new OrderRequest("주문자1", "서울 강남구",
                List.of(
                        OrderItemDto.of(productA.getId(), productA.getName(), 90),
                        OrderItemDto.of(productA.getId(), productA.getName(), 9)
                )
        );

        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");

        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(orderItemRepository.count()).isEqualTo(1);
        assertThat(productRepository.findById(productA.getId()).get().getStock()).isEqualTo(1);
    }


    @Test
    @DisplayName("주문1개_상품여러개 (주문량 > 재고)")
    void createOrder6() {
        Product productA = Product.createProduct("Product A", 100);
        Product productB = Product.createProduct("Product B", 100);
        productRepository.save(productA);
        productRepository.save(productB);

        OrderRequest request = new OrderRequest("주문자1", "서울 강남구",
                List.of(
                        OrderItemDto.of(productA.getId(), productA.getName(), 90),
                        OrderItemDto.of(productB.getId(), productB.getName(), 101)
                )
        );
        OrderResponse response = orderServiceImp.createOrder(request);
        assertEquals(response, OrderResponse.of(request, OutOfStockException.build(productB.getId(), productB.getStock(), 101)));
    }






}