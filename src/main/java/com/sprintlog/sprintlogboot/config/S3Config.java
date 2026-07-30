package com.sprintlog.sprintlogboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@Slf4j
@ConditionalOnProperty(
        name = "sprintlog.storage", havingValue = "s3"
)
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

    @Bean
    public S3Client s3Client(S3Properties props) {
        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(props.getRegion()));

        if (StringUtils.hasText(props.getEndpoint())) {
            //endpoint에 값이 있다면 테스트 환경
            builder.endpointOverride(URI.create(props.getEndpoint()))
                    .forcePathStyle(true)   //경로 방식의 s3 주소 체계를 사용하겠다
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("test", "test")));
                    //더미 자격 증명 설정. 실제로 AWS에 접속하는 것이 아니기 때문에 아무 값이나 넣어서 형식만 갖추겠다.
            log.info("S3Client - 로컬 Mock 사용: {}", props.getEndpoint());

        } else {
            //endpoint에 값이 없다면 진짜 AWS에 요청을 보내야 하는 상황
            builder.forcePathStyle(false)
                    .credentialsProvider(DefaultCredentialsProvider.builder().build());
            log.info("S3Client - 실제 AWS(region={})", props.getRegion());
        }

        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner(S3Properties props) {
        S3Presigner.Builder builder = S3Presigner.builder()
                .region(Region.of(props.getRegion()));

        if (StringUtils.hasText(props.getEndpoint())) {
            //endpoint에 값이 있다면 테스트 환경
            builder.endpointOverride(URI.create(props.getEndpoint()))
                    .serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(true)
                            .build())
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("test", "test")));

        } else {
            //endpoint에 값이 없다면 진짜 AWS에 요청을 보내야 하는 상황
            builder.serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(false)
                            .build())
                    .credentialsProvider(DefaultCredentialsProvider.builder().build());
        }

        return builder.build();
    }

}

