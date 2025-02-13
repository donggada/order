package com.teamFresh.order.factoty.order;

import com.teamFresh.order.request.OrderRequest;
import com.teamFresh.order.response.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    OrderResponse createOrder(OrderRequest request);
}
