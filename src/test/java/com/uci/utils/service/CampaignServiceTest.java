package com.uci.utils.service;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uci.utils.ApplicationConfiguration;
import com.uci.utils.CampaignService;

import io.fusionauth.client.FusionAuthClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes=ApplicationConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
public class CampaignServiceTest {
	@Autowired
	private MockWebServer mockWebServer;
	
	@Autowired
	private CampaignService campaignService;
	
	/**
	 * Test case for get campaign id by campaign id 
	 */
	@Test
	public void getCampaignFromIDTest() throws Exception {
		MockResponse mockResponse = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"ts\":\"2021-10-14T06:25:06.808Z\",\"params\":{\"resmsgid\":\"7957d780-2cb7-11ec-a02a-a7c7511e33e7\",\"msgid\":null,\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"data\":{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"userSegments\":[],\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":{\"id\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"channel\":\"WhatsApp\",\"provider\":\"gupshup\",\"config\":{\"2WAY\":\"2000193033\",\"phone\":\"9876543210\",\"HSM_ID\":\"2000193031\",\"credentials\":{\"vault\":\"samagra\",\"variable\":\"gupshupSamagraProd\"}},\"name\":\"SamagraProd\",\"updated_at\":\"2021-06-16T06:02:39.125Z\",\"created_at\":\"2021-06-16T06:02:41.823Z\"},\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}}}");
		mockWebServer.enqueue(mockResponse);
		
		Mono<JsonNode> campaign = campaignService.getCampaignFromID("d655cf03-1f6f-4510-acf6-d3f51b488a5e");

		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree("{\"data\":{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"userSegments\":[],\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":{\"id\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"channel\":\"WhatsApp\",\"provider\":\"gupshup\",\"config\":{\"2WAY\":\"2000193033\",\"phone\":\"9876543210\",\"HSM_ID\":\"2000193031\",\"credentials\":{\"vault\":\"samagra\",\"variable\":\"gupshupSamagraProd\"}},\"name\":\"SamagraProd\",\"updated_at\":\"2021-06-16T06:02:39.125Z\",\"created_at\":\"2021-06-16T06:02:41.823Z\"},\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}}");
		
		StepVerifier.create(campaign.log()).expectNext(node).verifyComplete();
	}
	
	/**
	 * Test case for get campaign id by campaign name
	 */
	@Test
	public void getCampaignFromNameTransformerTest() throws Exception {
		MockResponse mockResponse = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"id\":\"api.bot.search\",\"ver\":\"1.0\",\"ts\":\"2021-10-14T06:33:13.836Z\",\"params\":{\"resmsgid\":\"9ba26ac0-2cb8-11ec-a02a-a7c7511e33e7\",\"msgid\":\"9b9f8490-2cb8-11ec-a02a-a7c7511e33e7\",\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"data\":[{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"userSegments\":[],\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":{\"id\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"channel\":\"WhatsApp\",\"provider\":\"gupshup\",\"config\":{\"2WAY\":\"2000193033\",\"phone\":\"9876543210\",\"HSM_ID\":\"2000193031\",\"credentials\":{\"vault\":\"samagra\",\"variable\":\"gupshupSamagraProd\"}},\"name\":\"SamagraProd\",\"updated_at\":\"2021-06-16T06:02:39.125Z\",\"created_at\":\"2021-06-16T06:02:41.823Z\"},\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}],\"total\":1}}");		
		mockWebServer.enqueue(mockResponse);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree("{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"userSegments\":[],\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":{\"id\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"channel\":\"WhatsApp\",\"provider\":\"gupshup\",\"config\":{\"2WAY\":\"2000193033\",\"phone\":\"9876543210\",\"HSM_ID\":\"2000193031\",\"credentials\":{\"vault\":\"samagra\",\"variable\":\"gupshupSamagraProd\"}},\"name\":\"SamagraProd\",\"updated_at\":\"2021-06-16T06:02:39.125Z\",\"created_at\":\"2021-06-16T06:02:41.823Z\"},\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}");
		
		Mono<JsonNode> campaign = campaignService.getCampaignFromNameTransformer("UCI Demo");
	
		StepVerifier.create(campaign.log()).expectNext(node).verifyComplete();
	}
	
	/**
	 * Test case for get first form id by bot id 
	 */
	@Test
	public void getFirstFormByBotIDTest() throws Exception {
		MockResponse mockResponse = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"ts\":\"2021-10-14T06:35:48.782Z\",\"params\":{\"resmsgid\":\"f7fd4ce0-2cb8-11ec-a02a-a7c7511e33e7\",\"msgid\":null,\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"data\":{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"userSegments\":[],\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":{\"id\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"channel\":\"WhatsApp\",\"provider\":\"gupshup\",\"config\":{\"2WAY\":\"2000193033\",\"phone\":\"9876543210\",\"HSM_ID\":\"2000193031\",\"credentials\":{\"vault\":\"samagra\",\"variable\":\"gupshupSamagraProd\"}},\"name\":\"SamagraProd\",\"updated_at\":\"2021-06-16T06:02:39.125Z\",\"created_at\":\"2021-06-16T06:02:41.823Z\"},\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}}}");
		mockWebServer.enqueue(mockResponse);
		
		Mono<String> form = campaignService.getFirstFormByBotID("d655cf03-1f6f-4510-acf6-d3f51b488a5e");

		StepVerifier.create(form.log()).expectNext("UCI-demo-1").verifyComplete();
	}
}
