package com.teamFresh.order.severice;

import com.teamFresh.order.dto.OrderItemDto;
import com.teamFresh.order.entity.Order;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImpTest {
    @InjectMocks
    private OrderServiceImp orderServiceImp;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("주문1개_상품1개 (주문량 < 재고)")
    void createOrder1() {
        OrderRequest request = new OrderRequest("주문자1", "서울 강남구", List.of(OrderItemDto.of(1L,"Product A", 20)));

        Product product = Product.createProduct("Product A", 100);
        Order order = Order.createOrder("주문자1", "서울 강남구");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);


        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");
        assertThat(product.getStock()).isEqualTo(80);
    }

    @Test
    @DisplayName("주문1개_상품1개 (주문량 = 재고)")
    void createOrder2() {
        OrderRequest request = new OrderRequest("주문자1", "서울 강남구", List.of(OrderItemDto.of(1L,"Product A", 100)));

        Product product = Product.createProduct("Product A", 100);
        Order order = Order.createOrder("주문자1", "서울 강남구");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);


        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");
        assertThat(product.getStock()).isEqualTo(0);
    }


    @Test
    @DisplayName("주문1개_상품1개 (주문량 > 재고)")
    void createOrder3() {
        OrderRequest request = new OrderRequest("주문자1", "서울 강남구", List.of(OrderItemDto.of(1L,"Product A", 110)));

        Product product = Product.createProduct("Product A", 100);
        Order order = Order.createOrder("주문자1", "서울 강남구");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderServiceImp.createOrder(request);
        assertEquals(response, OrderResponse.of(request, OutOfStockException.build(product.getId(), product.getStock(), 110)));
    }

    @Test
    @DisplayName("주문1개_상품여러개 (주문량 <= 재고)")
    void createOrder4() {
        OrderRequest request = new OrderRequest("주문자1", "서울 강남구",
                List.of(OrderItemDto.of(1L,"Product A", 20),
                        OrderItemDto.of(2L,"Product B", 30)));

        Product productA = Product.createProduct("Product A", 100);
        Product productB = Product.createProduct("Product B", 30);
        Order order = Order.createOrder("주문자1", "서울 강남구");

        when(productRepository.findById(1L)).thenReturn(Optional.of(productA));
        when(productRepository.findById(2L)).thenReturn(Optional.of(productB));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");
        assertThat(productA.getStock()).isEqualTo(80);
        assertThat(productB.getStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문1개_상품여러개 (주문량 <= 재고)")
    void createOrder5() {
        OrderRequest request = new OrderRequest("주문자1", "서울 강남구",
                List.of(OrderItemDto.of(1L,"Product A", 20),
                        OrderItemDto.of(1L,"Product A", 30)));

        Product productA = Product.createProduct("Product A", 100);
        Order order = Order.createOrder("주문자1", "서울 강남구");

        when(productRepository.findById(1L)).thenReturn(Optional.of(productA));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderServiceImp.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.customerName()).isEqualTo("주문자1");
        assertThat(response.customerAddress()).isEqualTo("서울 강남구");
        assertThat(productA.getStock()).isEqualTo(50);
    }

    @Test
    @DisplayName("주문1개_상품여러개 (주문량 > 재고)")
    void createOrder6() {
        OrderRequest request = new OrderRequest("주문자1", "서울 강남구",
                List.of(OrderItemDto.of(1L,"Product A", 20),
                        OrderItemDto.of(2L,"Product B", 30)
                )
        );

        Product productA = Product.createProduct("Product A", 100);
        Product productB = Product.createProduct("Product B", 29);
        Order order = Order.createOrder("주문자1", "서울 강남구");

        when(productRepository.findById(1L)).thenReturn(Optional.of(productA));
        when(productRepository.findById(2L)).thenReturn(Optional.of(productB));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderServiceImp.createOrder(request);
        assertEquals(response, OrderResponse.of(request, OutOfStockException.build(productB.getId(), productB.getStock(), 30)));

    }





}