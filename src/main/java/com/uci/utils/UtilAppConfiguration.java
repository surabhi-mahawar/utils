package com.uci.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableAutoConfiguration
public class UtilAppConfiguration {


    @Value("${campaign.url}")
    public String CAMPAIGN_URL;
    
	@Value("${campaign.admin.token}")
	public String CAMPAIGN_ADMIN_TOKEN;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(CAMPAIGN_URL)
                .defaultHeader("admin-token", CAMPAIGN_ADMIN_TOKEN)
                .build();
    }


}
