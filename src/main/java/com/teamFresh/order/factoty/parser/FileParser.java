package com.teamFresh.order.factoty.parser;

import com.teamFresh.order.request.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface FileParser {
    List<OrderRequest> parseFileByOrder(MultipartFile file);
}
