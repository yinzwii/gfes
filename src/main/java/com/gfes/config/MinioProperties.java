package com.gfes.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class MinioProperties {

//    private String endpoint = "http://localhost";
    private String endpoint = "http://minio.xx.com/";

//    private Integer port = 9000;
    private Integer port = 80;

    private String accessKey = "admin";

    private String secretKey = "";

    private Boolean secure = false;

    private String bucketName = "source-filer";

    private String bucketNameNode = "preview-filer";
}
