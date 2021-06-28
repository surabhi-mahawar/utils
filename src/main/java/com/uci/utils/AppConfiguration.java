package com.uci.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableAutoConfiguration
public class AppConfiguration {


    @Value("${campaign.url}")
    public String CAMPAIGN_URL;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(CAMPAIGN_URL)
                .build();
    }


}
