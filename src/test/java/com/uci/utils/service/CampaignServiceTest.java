package com.uci.utils.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.JsonNode;
import com.uci.utils.ApplicationConfiguration;
import com.uci.utils.BotService;
import com.uci.utils.CampaignService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes=ApplicationConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
public class CampaignServiceTest {
	@Autowired
	private CampaignService campaignService;
	
	/**
	 * Test case for get campaign id by campaign id 
	 */
	@Test
	public void getCampaignFromIDTest() throws Exception {
		Mono<JsonNode> campaign = campaignService.getCampaignFromID("d655cf03-1f6f-4510-acf6-d3f51b488a5e");

//		campaign.log().subscribe(System.out::println, 
//				(e) -> System.err.println("---------------Exception occured in get campaign by campaign id-------------: " + e), 
//				() -> System.out.println("------------Get campaign by campaign id completed-----------"));

		StepVerifier.create(campaign.log()).verifyComplete();
	}
	
	/**
	 * Test case for get campaign id by campaign name
	 */
	@Test
	public void getCampaignFromNameTransformerTest() throws Exception {
		Mono<JsonNode> campaign = campaignService.getCampaignFromNameTransformer("UCI Demo");

//		campaign.log().subscribe(System.out::println, 
//				(e) -> System.err.println("---------------Exception occured in get campaign by campaign name-------------: " + e), 
//				() -> System.out.println("------------Get campaign by campaign name completed-----------"));
		
		StepVerifier.create(campaign.log()).verifyComplete();
	}
	
	/**
	 * Test case for get first form id by bot id 
	 */
	@Test
	public void getFirstFormByBotIDTest() throws Exception {
		Mono<String> form = campaignService.getFirstFormByBotID("d655cf03-1f6f-4510-acf6-d3f51b488a5e");

//		form.log().subscribe(System.out::println, 
//				(e) -> System.err.println("---------------Exception occured in get first form by botID -------------: " + e), 
//				() -> System.out.println("------------Get first form by botID completed-----------"));
		
		StepVerifier.create(form.log()).verifyComplete();
	}
}
