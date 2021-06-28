package com.uci.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inversoft.rest.ClientResponse;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.Application;
import io.fusionauth.domain.api.ApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BotService{

    @Value("${campaign.url}")
    public String CAMPAIGN_URL;

    @Autowired
    public WebClient webClient;

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param startingMessage - Starting Message
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Mono<String> getCampaignFromStartingMessage(String startingMessage) {
        final boolean[] error = {false};
        webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/getByParam/").queryParam("startingMessage", startingMessage).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if (response != null) {

                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            JsonNode root = mapper.readTree(response);
                            JsonNode name = root.path("data").path("name");
                            return Mono.just(name.asText());
                        } catch (JsonProcessingException jsonMappingException) {
                            return Mono.just("");
                        }

                    } else {
                        return Mono.just("");
                    }
                });

        return Mono.just("");
    }

    public Mono<String> getCurrentAdapter(String botName){

        final boolean[] error = {false};
        webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/get/").queryParam("name", botName).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if (response != null) {

                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            JsonNode root = mapper.readTree(response);
                            ArrayNode login = (ArrayNode) root.path("data").path("logic");
                            String name = ((JsonNode)login.get(0)).path("adapter").asText();
                            return Mono.just(name);
                        } catch (JsonProcessingException jsonMappingException) {
                            return Mono.just("");
                        }

                    } else {
                        return Mono.just("");
                    }
                });

        return Mono.just("");
    }

    public Application getButtonLinkedApp(String appName) {
        try {
            Application application = this.getCampaignFromName(appName);
            String buttonLinkedAppID = (String) ((ArrayList<Map>) application.data.get("parts")).get(0).get("buttonLinkedApp");
            Application linkedApplication = BotService.getCampaignFromID(buttonLinkedAppID);
            return linkedApplication;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Application> getApplications() {
        List<Application> applications = new ArrayList<>();
        FusionAuthClient staticClient = new FusionAuthClient("c0VY85LRCYnsk64xrjdXNVFFJ3ziTJ91r08Cm0Pcjbc", "http://134.209.150.161:9011");
        ClientResponse<ApplicationResponse, Void> response = staticClient.retrieveApplications();
        if (response.wasSuccessful()) {
            applications = response.successResponse.applications;
        } else if (response.exception != null) {
            Exception exception = response.exception;
        }
        return applications;
    }

    /**
     * Retrieve Campaign Params From its Identifier
     *
     * @param campaignID - Campaign Identifier
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public static Application getCampaignFromID(String campaignID) throws Exception {
        System.out.println("CampaignID: " + campaignID);
        FusionAuthClient staticClient = new FusionAuthClient("c0VY85LRCYnsk64xrjdXNVFFJ3ziTJ91r08Cm0Pcjbc", "http://134.209.150.161:9011");
        System.out.println("Client: " + staticClient);
        ClientResponse<ApplicationResponse, Void> applicationResponse = staticClient.retrieveApplication(UUID.fromString(campaignID));
        if (applicationResponse.wasSuccessful()) {
            Application application = applicationResponse.successResponse.application;
            return application;
        } else if (applicationResponse.exception != null) {
            Exception exception = applicationResponse.exception;
            throw exception;
        }
        return null;
    }

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param campaignName - Campaign Name
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public static Application getCampaignFromName(String campaignName) throws Exception {
        List<Application> applications = getApplications();

        Application currentApplication = null;
        if (applications.size() > 0) {
            for (Application application : applications) {
                if (application.name.equals(campaignName)) {
                    currentApplication = application;
                }
            }
        }
        return currentApplication;
    }
}
