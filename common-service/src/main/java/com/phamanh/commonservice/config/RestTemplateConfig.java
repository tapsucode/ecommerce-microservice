package com.phamanh.commonservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // Thiết lập các cài đặt như timeout, proxy, ...
        factory.setConnectTimeout(2000); // Thời gian chờ kết nối (5 giây)
        factory.setReadTimeout(2000);    // Thời gian chờ đọc dữ liệu (5 giây)
        return factory;
    }
}
