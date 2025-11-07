package com.projeto_integrado_biblioteca.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record S3ConfigProps(
        String endpoint,
        String accessKey,
        String secretKey,
        String region,
        String bucket
) {
}
