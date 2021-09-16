package com.uci.utils.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import com.uci.utils.ApplicationConfiguration;
import com.uci.utils.BotService;

import io.fusionauth.client.FusionAuthClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes=ApplicationConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
public class BotServiceTest {
	@Autowired
	private BotService botService;
	
	/**
	 * Test case for get campaign from starting message 
	 */
	@Test
	public void getCampaignFromStartingMessageTest() throws Exception {
		Mono<String> campaign = botService.getCampaignFromStartingMessage(getStartingMessage());

//		campaign.log().subscribe(System.out::println, 
//				(e) -> System.err.println("---------------Exception occured in get campaign from starting message adapter-------------: " + e), 
//				() -> System.out.println("------------Get campaign from starting message completed-----------"));

		
		StepVerifier.create(campaign.log()).verifyComplete();
	}

	/**
	 * Test case for get current adapter from bot name
	 */
	@Test
	public void getCurrentAdapterTest() throws Exception {
		Mono<String> adapter = botService.getCurrentAdapter(getBotName());

//		adapter.log().subscribe(System.out::println, 
//				(e) -> System.err.println("---------------Exception occured in get current adapter-------------: " + e), 
//				() -> System.out.println("------------Get current adapter completed-----------"));

		
		StepVerifier.create(adapter.log()).verifyComplete();
	}
	
	/**
	 * Test case for get bot id from bot name
	 */
	@Test
	public void getBotIdfromBotNameTest() {
		Mono<String> bot = botService.getBotIDFromBotName(getBotName());

//		bot.log().subscribe(System.out::println, 
//				(e) -> System.err.println("---------------Exception occured in get current adapter-------------: " + e), 
//				() -> System.out.println("------------Get bot from bot name completed-----------"));

		
		StepVerifier.create(bot.log()).verifyComplete();
	}
	
	/**
	 * Test case for get bot id from bot name
	 */
	@Test
	public void updateUserTest() {
		Mono<Pair<Boolean, String>> userResponse = botService.updateUser("phone:7597185708", getBotName());

//		userResponse.log().subscribe(System.out::println, 
//				(e) -> System.err.println("---------------Exception occured in update user-------------: " + e), 
//				() -> System.out.println("------------Update user completed-----------"));

		
		StepVerifier.create(userResponse.log()).verifyComplete();
	}
	
	/**
	 * Get Bot Name for testing
	 * 
	 * @return
	 */
	private String getBotName() {
		return "UCI Demo";
	}
	
	/**
	 * Get Bot id for testing
	 * 
	 * @return
	 */
	private String getBotId() {
		return "d655cf03-1f6f-4510-acf6-d3f51b488a5e";
	}
	
	/**
	 * Get Starting Message of testing
	 * 
	 * @return
	 */
	private String getStartingMessage() {
		return "Hi UCI";
	}
	
	/**
	 * Get Campaign Name for testing
	 * 
	 * @return
	 */
	private String getCampaignName() {
		return "UCI Demo";
	}
}
