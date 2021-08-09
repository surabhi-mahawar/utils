package com.uci.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inversoft.rest.ClientResponse;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.Application;
import io.fusionauth.domain.api.ApplicationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("ALL")
@Service
@Slf4j
@AllArgsConstructor
@Getter
@Setter
public class BotService {

    public WebClient webClient;

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param startingMessage - Starting Message
     * @return Application
     */
    public Mono<String> getCampaignFromStartingMessage(String startingMessage) {
        return webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/getByParam/").queryParam("startingMessage", startingMessage).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if (response != null) {

                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            JsonNode root = mapper.readTree(response);
                            JsonNode name = root.path("data").path("name");
                            return name.asText();
                        } catch (JsonProcessingException jsonMappingException) {
                            return "";
                        }

                    } else {
                        return "";
                    }
                }).onErrorReturn("").doOnError(throwable -> System.out.println("Error in getting campaign" + throwable.getMessage()));

    }
    
    /**
     * Check if a url connection returns ok
     *
     * @return Boolean
     */
    public Boolean statusUrlCheck(String url) throws IOException {
    	try {
    		ResponseEntity<String> result = webClient.get().uri(url).retrieve().toEntity(String.class).block();
        	if(result.getStatusCodeValue() == 200) {
    			return true;
            }
    	} catch (Exception ex) {
    		//
    	}
    	return false;
    }

    public Mono<String> getCurrentAdapter(String botName) {
        return webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/getByParam/").queryParam("name", botName).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if (response != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            JsonNode root = mapper.readTree(response);
                            JsonNode name = root.path("data");
                            if (name.has("name") && name.get("name").asText().equals(botName)) {
                                return (((JsonNode) ((ArrayNode) name.path("logic"))).get(0).path("adapter")).asText();

                            }
                            return null;
                        } catch (JsonProcessingException jsonMappingException) {
                            return null;
                        }

                    } else {
                    }
                    return null;
                }).onErrorReturn("").
                        doOnError(throwable -> System.out.println("Error in getting adapter >> " + throwable.getMessage()));

    }

    public Application getButtonLinkedApp(String appName) {
        try {
            Application application = getCampaignFromName(appName);
            String buttonLinkedAppID = (String) ((ArrayList<Map>) application.data.get("parts")).get(0).get("buttonLinkedApp");
            Application linkedApplication = this.getCampaignFromID(buttonLinkedAppID);
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
            return applicationResponse.successResponse.application;
        } else if (applicationResponse.exception != null) {
            throw applicationResponse.exception;
        }
        return null;
    }

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param campaignName - Campaign Name
     * @return Application
     */
    private Application getCampaignFromName(String campaignName) {
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
