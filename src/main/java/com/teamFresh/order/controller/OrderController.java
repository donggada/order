package com.teamFresh.order.controller;

import com.teamFresh.order.component.DistributeLockExecutor;
import com.teamFresh.order.factoty.order.OrderServiceFactory;
import com.teamFresh.order.factoty.order.OrderServiceImp;
import com.teamFresh.order.request.OrderRequest;
import com.teamFresh.order.response.OrderResponse;
import com.teamFresh.order.severice.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.teamFresh.order.enums.LockType.ORDER;
import static com.teamFresh.order.factoty.order.OrderServiceType.NOMAL;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImp orderServiceImp;
    private final UploadService uploadService;
    private final DistributeLockExecutor lockExecutor;
    private final OrderServiceFactory orderServiceFactory;

    @PostMapping("")
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        return lockExecutor.execute(ORDER, () -> orderServiceFactory.getService(NOMAL).createOrder(request));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<OrderResponse> createOrder(@RequestParam MultipartFile file){
        return uploadService.createOrder(file);
    }
}
