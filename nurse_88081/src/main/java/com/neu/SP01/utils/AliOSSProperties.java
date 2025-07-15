package com.neu.SP01.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOSSProperties {
    //属性名要和配置文件中的一样，才能匹配到
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
