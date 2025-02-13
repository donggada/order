package com.teamFresh.order.severice;

import com.teamFresh.order.component.DistributeLockExecutor;
import com.teamFresh.order.factoty.order.OrderServiceFactory;
import com.teamFresh.order.factoty.parser.FileParserFactory;
import com.teamFresh.order.request.OrderRequest;
import com.teamFresh.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

import static com.teamFresh.order.enums.LockType.ORDER;
import static com.teamFresh.order.factoty.order.OrderServiceType.NOMAL;
import static com.teamFresh.order.factoty.parser.FileParserType.EXCEL;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final FileParserFactory fileParserFactory;
    private final DistributeLockExecutor lockExecutor;
    private final OrderServiceFactory orderServiceFactory;

    public List<OrderResponse> createOrder(MultipartFile file) {
        return fileParserFactory.getService(EXCEL)
                .parseFileByOrder(file)
                .stream()
                .sorted(Comparator.comparing(OrderRequest::getCustomerName))
                .map(request -> lockExecutor.execute(ORDER, () -> orderServiceFactory.getService(NOMAL).createOrder(request)))
                .toList();
    }
}
