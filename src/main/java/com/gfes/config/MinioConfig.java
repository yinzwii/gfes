package com.gfes.config;


import io.minio.MinioClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.util.Properties;

@Data
@Configuration
public class MinioConfig {

    @Bean
    public MinioProperties minioProperties(){
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/minio.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MinioProperties minioProperties = new MinioProperties();
        String endpoint = properties.getProperty("endpoint");
        String port = properties.getProperty("port");
        String accessKey = properties.getProperty("accessKey");
        String secretKey = properties.getProperty("secretKey");
        String secure = properties.getProperty("secure");
        String bucketName = properties.getProperty("bucketName");
        String bucketNameNode = properties.getProperty("bucketNameNode");
        minioProperties.setAccessKey(accessKey);
        minioProperties.setBucketName(bucketName);
        minioProperties.setEndpoint(endpoint);
        minioProperties.setPort(Integer.valueOf(port));
        minioProperties.setSecure("true".equals(secure));
        minioProperties.setSecretKey(secretKey);
        minioProperties.setBucketNameNode(bucketNameNode);
        return minioProperties;
    }

    @Bean
    public MinioClient getMinioClient(MinioProperties minioProperties) {
        MinioClient minioClient = new MinioClient(minioProperties.getEndpoint(), minioProperties.getPort(),
                minioProperties.getAccessKey(), minioProperties.getSecretKey(), minioProperties.getSecure());
        return minioClient;
    }
}
