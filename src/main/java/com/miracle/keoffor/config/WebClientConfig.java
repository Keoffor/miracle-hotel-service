package com.miracle.keoffor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private final static String BASEURL = "http://dev.kenstudy.com/payment";
    @Bean
    public WebClient webClient() {

        return WebClient.builder().baseUrl(BASEURL).build();
    }
}
