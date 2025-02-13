package com.teamFresh.order.factoty.order;

import com.teamFresh.order.entity.Order;
import com.teamFresh.order.entity.OrderItem;
import com.teamFresh.order.entity.Product;
import com.teamFresh.order.repository.OrderItemRepository;
import com.teamFresh.order.repository.OrderRepository;
import com.teamFresh.order.repository.ProductRepository;
import com.teamFresh.order.request.OrderRequest;
import com.teamFresh.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.teamFresh.order.exception.ErrorCode.INVALID_PRODUCT;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        try {
            Order order = createOrderEntity(request);

            List<OrderItem> orderItems = createOrderItems(request, order);

            orderItemRepository.saveAll(orderItems);

            return OrderResponse.of(order, orderItems);
        } catch (Exception e) {
            return OrderResponse.of(request, e);
        }

    }

    private Order createOrderEntity(OrderRequest request) {
        Order order = Order.createOrder(request.getCustomerName(), request.getCustomerAddress());
        return orderRepository.save(order);
    }

    private List<OrderItem> createOrderItems(OrderRequest request, Order order) {
        return aggregateOrderItems(
                request.getOrderItems().stream()
                        .map(orderItem -> {
                            Product product = productRepository.findById(orderItem.getId())
                                    .orElseThrow(() -> INVALID_PRODUCT.build(orderItem.getId()));
                            product.decreaseStock(orderItem.getQuantity());
                            return OrderItem.createOrderItem(order, product, orderItem.getQuantity());
                        }).toList(),
                order);
    }



    private List<OrderItem> aggregateOrderItems(List<OrderItem> orderItems, Order order) {
        return orderItems.stream()
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                        Collectors.summingInt(OrderItem::getQuantity)
                )).entrySet().stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    int totalQuantity = entry.getValue();
                    return OrderItem.createOrderItem(order, product, totalQuantity);
                })
                .collect(Collectors.toList());
    }
}
