package com.teamFresh.order.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class UploadRequest {

    private MultipartFile file;
}
