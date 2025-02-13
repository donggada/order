package com.teamFresh.order.factoty.order;

import com.teamFresh.order.factoty.parser.ExcelParser;
import com.teamFresh.order.factoty.parser.FileParser;
import com.teamFresh.order.factoty.parser.FileParserType;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceFactory {

    private final Map<Class, OrderService> orderServiceMap;

    public OrderServiceFactory(List<OrderService> orderServices) {
        this.orderServiceMap = orderServices.stream()
                .collect(
                        Collectors.toMap(
                                AopUtils::getTargetClass,
                                service -> service
                        )
                );
    }

    public OrderService getService(OrderServiceType type) {
        if (Objects.requireNonNull(type) == OrderServiceType.NOMAL) {
            return orderServiceMap.get(OrderServiceImp.class);
        }
        throw new IllegalArgumentException("Unsupported mall type: " + type);
    }

}
