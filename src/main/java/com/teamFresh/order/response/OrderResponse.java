package com.teamFresh.order.response;

import com.teamFresh.order.dto.OrderItemDto;
import com.teamFresh.order.entity.Order;
import com.teamFresh.order.entity.OrderItem;
import com.teamFresh.order.exception.ApplicationException;
import com.teamFresh.order.exception.OutOfStockException;
import com.teamFresh.order.request.OrderRequest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.teamFresh.order.exception.ErrorCode.SERVER_ERROR;


public record OrderResponse(
        String customerName,
        String customerAddress,
        List<OrderItemDto> orderItems,
        int status,
        String message,
        String reason
) {

    private static final Map<Class<? extends Exception>, Function<Exception, String>> exceptionHandlerMap = new HashMap<>();

    static {
        exceptionHandlerMap.put(OutOfStockException.class, e -> ((OutOfStockException) e).getReason());
        exceptionHandlerMap.put(ApplicationException.class, e -> ((ApplicationException) e).getReason());
    }

    public static OrderResponse of(Order order, List<OrderItem> orderItems) {
        return new OrderResponse(
                order.getCustomerName(),
                order.getCustomerAddress(),
                orderItems.stream().map(OrderItemDto::of).toList(),
                HttpStatus.OK.value(),
                "성공",
                ""
        );
    }

    public static OrderResponse of(OrderRequest request, Exception e) {
        return new OrderResponse(
                request.getCustomerName(),
                request.getCustomerAddress(),
                new ArrayList<>(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                exceptionHandlerMap.getOrDefault(e.getClass(), exception -> (SERVER_ERROR.getReason())).apply(e)
        );
    }
}
