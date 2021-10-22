package com.uci.utils.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import com.uci.utils.ApplicationConfiguration;
import com.uci.utils.BotService;

import io.fusionauth.client.FusionAuthClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = ApplicationConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
public class BotServiceTest {
	@Autowired
	private MockWebServer mockWebServer;
	
	@Autowired
	private BotService botService;

//	private WebClient.RequestBodyUriSpec requestBodyUriMock;
//	
//	private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
//	
//	private WebClient.RequestHeadersSpec requestHeadersMock;
//	
//	private WebClient.RequestBodySpec requestBodyMock;
//	
//	private WebClient.ResponseSpec responseMock;

//	@BeforeEach
//    void mockWebClient() {
//        requestBodyUriMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
//        requestHeadersUriMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
//        requestHeadersMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
//        requestBodyMock = Mockito.mock(WebClient.RequestBodySpec.class);
//        responseMock = Mockito.mock(WebClient.ResponseSpec.class);
//        webClientMock = Mockito.mock(WebClient.class);
//    }
	
//	@BeforeEach
//	public void setupMockServer() {
//	    this.botService = new BotService(WebClient.builder(), mockWebServer.url("/").toString());
//	    mockServer = ClientAndServer.startClientAndServer(2001);
//	}
	
	/**
	 * Test case for get campaign from starting message
	 */
//	@Test
//	public void getCampaignFromStartingMessageTest() throws Exception {
//		when(webClientMock.get()).thenReturn(requestHeadersUriMock);
//		
//		String expectedUri = "http://uci-apis.ngrok.samagra.io/admin/v1/bot/getByParam/startingMessage";
//		
//		when(requestHeadersUriMock.uri(expectedUri, "startingMessage=Hi UCI")).thenReturn(requestBodyMock);
//		
////	when(requestBodyMock.bodyValue(eq(request))).thenReturn(requestHeadersMock);
//      when(requestHeadersMock.retrieve()).thenReturn(responseMock);
//      when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just("UCI Demo"));
//
//		String responseBody = getDownstreamResponseDTOAsString();
//        mockServer.when(
//                request()
//                    .withMethod(HttpMethod.GET.name())
//                    .withPath("/legacy/persons")
//        ).respond(
//                response()
//                    .withStatusCode(HttpStatus.OK.value())
//                    .withContentType(MediaType.APPLICATION_JSON)
//                    .withBody(responseBody)
//        );
//
//        assertDoesNotThrow(() -> botService.getCampaignFromStartingMessage(getStartingMessage()));
//
//		Mono<String> campaign = botService.getCampaignFromStartingMessage(getStartingMessage());	
//		
//		campaign.log().subscribe(System.out::println, 
//			(e) -> System.err.println("---------------Exception occurred in get campaign from starting message -------------: " + e), 
//			() -> System.out.println("------------Get campaign from starting message completed-----------"));
//	
//		
//		StepVerifier.create(campaign.log()).expectNext("UCI Demo").verifyComplete();
//	}

	@AfterEach
	public void tearDownServer() {
//		log.info("shutdown");
	}

	/**
	 * Test case for get campaign from starting message
	 */
	@Test
	public void getCampaignFromStartingMessageTest() throws Exception {
		MockResponse mockResponse = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"id\":\"api.bot.getByParam\",\"ver\":\"1.0\",\"ts\":\"2021-10-14T05:56:28.609Z\",\"params\":{\"resmsgid\":\"79377b10-2cb3-11ec-a02a-a7c7511e33e7\",\"msgid\":\"79335c60-2cb3-11ec-a02a-a7c7511e33e7\",\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"data\":{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}}}");

		mockWebServer.enqueue(mockResponse);

		Mono<String> campaign = botService.getCampaignFromStartingMessage(getStartingMessage());	
		
		StepVerifier.create(campaign.log()).expectNext("UCI Demo").verifyComplete();
	}

	/**
	 * Test case for get current adapter from bot name
	 */
	@Test
	public void getCurrentAdapterTest() throws Exception {
		MockResponse mockResponse = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"id\":\"api.bot.getByParam\",\"ver\":\"1.0\",\"ts\":\"2021-10-14T06:09:04.544Z\",\"params\":{\"resmsgid\":\"3bca0200-2cb5-11ec-a02a-a7c7511e33e7\",\"msgid\":\"3bc93eb0-2cb5-11ec-a02a-a7c7511e33e7\",\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"data\":{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}}}");
//		.throttleBody(16, 5, TimeUnit.SECONDS);

		mockWebServer.enqueue(mockResponse);
		
		Mono<String> adapter = botService.getCurrentAdapter(getBotName());

		StepVerifier.create(adapter.log()).expectNext("44a9df72-3d7a-4ece-94c5-98cf26307324").verifyComplete();
	}

	/**
	 * Test case for get bot id from bot name
	 */
	@Test
	public void getBotIdfromBotNameTest() {
		MockResponse mockResponse = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"id\":\"api.bot.getByParam\",\"ver\":\"1.0\",\"ts\":\"2021-10-14T06:12:27.564Z\",\"params\":{\"resmsgid\":\"b4cc66c0-2cb5-11ec-a02a-a7c7511e33e7\",\"msgid\":\"b4cb7c60-2cb5-11ec-a02a-a7c7511e33e7\",\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"data\":{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}}}");
		mockWebServer.enqueue(mockResponse);
				
		Mono<String> bot = botService.getBotIDFromBotName(getBotName());
		
		StepVerifier.create(bot.log()).expectNext("d655cf03-1f6f-4510-acf6-d3f51b488a5e").verifyComplete();
	}

	/**
	 * Test case for get bot id from bot name
	 */
	@Test
	public void updateUserTest() {
		MockResponse mockResponseBot = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"id\":\"api.bot.getByParam\",\"ver\":\"1.0\",\"ts\":\"2021-10-14T06:12:27.564Z\",\"params\":{\"resmsgid\":\"b4cc66c0-2cb5-11ec-a02a-a7c7511e33e7\",\"msgid\":\"b4cb7c60-2cb5-11ec-a02a-a7c7511e33e7\",\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"data\":{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCI Demo\",\"startingMessage\":\"Hi UCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2021-10-11T05:54:03.471Z\",\"status\":\"Draft\",\"description\":\"For Internal Demo\",\"startDate\":\"2021-07-08T00:00:00.000Z\",\"endDate\":\"2021-07-23T00:00:00.000Z\",\"purpose\":\"For Internal Demo\",\"ownerOrgID\":null,\"ownerID\":null,\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"name\":\"UCI Demo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2021-09-15T06:04:39.793Z\",\"description\":null,\"ownerOrgID\":null,\"ownerID\":null}]}}}");
		mockWebServer.enqueue(mockResponseBot);
		
		MockResponse mockResponseUser = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody("{\"id\":\"api.userSegment.addUser\",\"ver\":\"1.0\",\"ts\":\"2021-10-14T06:15:07.677Z\",\"params\":{\"resmsgid\":\"143bb4d0-2cb6-11ec-a02a-a7c7511e33e7\",\"msgid\":\"142d5cf0-2cb6-11ec-a02a-a7c7511e33e7\",\"status\":\"successful\",\"err\":null,\"errmsg\":null},\"responseCode\":\"OK\",\"result\":{\"status\":\"Success\",\"message\":\"User Added\",\"userID\":\"4621a094-ea69-45fd-8e9d-01f703178199\"}}");
		mockWebServer.enqueue(mockResponseUser);
				
		Mono<Pair<Boolean, String>> userResponse = botService.updateUser("phone:7597185708", getBotName());
		
		StepVerifier.create(userResponse.log()).expectNext(Pair.of(true, "4621a094-ea69-45fd-8e9d-01f703178199")).verifyComplete();
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
