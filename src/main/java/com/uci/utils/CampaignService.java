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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CampaignService {

    @Value("${campaign.url}")
    public String CAMPAIGN_URL;


    @Autowired
    public WebClient webClient;

    /**
     * Retrieve Campaign Params From its Identifier
     *
     * @param campaignID - Campaign Identifier
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Mono<JsonNode> getCampaignFromID(String campaignID) throws Exception {
        webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/get/"+ campaignID).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                            if (response != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    return Mono.just(mapper.readTree(response));
                                } catch (JsonProcessingException e) {
                                    return null;
                                }
                            }
                            return null;
                        }
                );
        return null;

    }
    /**
     * Retrieve Campaign Params From its Name
     * @param campaignName - Campaign Name
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Application getCampaignFromName(String campaignName) throws Exception {
        List<Application> applications = new ArrayList<>();
        FusionAuthClient staticClient = new FusionAuthClient("c0VY85LRCYnsk64xrjdXNVFFJ3ziTJ91r08Cm0Pcjbc", "http://134.209.150.161:9011");
        ClientResponse<ApplicationResponse, Void> response = staticClient.retrieveApplications();
        if (response.wasSuccessful()) {
            applications = response.successResponse.applications;
        } else if (response.exception != null) {
            Exception exception = response.exception;
        }


        Application currentApplication = null;
        if(applications.size() > 0){
            for(Application application: applications){
                if(application.name.equals(campaignName)){
                    currentApplication = application;
                }
            }
        }
        return currentApplication;
    }


    /**
     * Retrieve Campaign Params From its Name
     *
     * @param campaignName - Campaign Name
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Mono<JsonNode> getCampaignFromNameTransformer(String campaignName) {

        webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/get/admin/v1/bot/search/").queryParam("name",campaignName).queryParam("match",true).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                            if (response != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    return Mono.just(mapper.readTree(response).get("data").get(0));
                                } catch (JsonProcessingException e) {
                                    return null;
                                }
                            }
                            return null;
                        }
                );
        return null;

    }

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param botID - Bot ID
     * @return FormID for the first transformer.
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Mono<String> getFirstFormByBotID(String botID) {
        webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/get/"+botID).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                            if (response != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    return Mono.just(mapper.readTree(response).findValue("formID").asText());
                                } catch (JsonProcessingException e) {
                                    return null;
                                }
                            }
                            return null;
                        }
                );
        return null;
    }


    /**
     * Retrieve Campaign Params From its Name
     *
     * @param botID - Bot ID
     * @return FormID for the first transformer.
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Mono<ArrayNode> getFirstFormHiddenFields(String botID) {
        webClient.get()
                .uri(builder -> builder.path("admin/v1/bot/get/"+botID).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                            if (response != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    return Mono.just(mapper.readTree(response).findValue("hiddenFields"));
                                } catch (JsonProcessingException e) {
                                    return null;
                                }
                            }
                            return null;
                        }
                );
        return null;

    }

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param campaignName - Campaign Name
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Application getCampaignFromNameESamwad(String campaignName) {
        List<Application> applications = new ArrayList<>();
        FusionAuthClient staticClient = new FusionAuthClient("c0VY85LRCYnsk64xrjdXNVFFJ3ziTJ91r08Cm0Pcjbc", "http://134.209.150.161:9011");
        ClientResponse<ApplicationResponse, Void> response = staticClient.retrieveApplications();
        if (response.wasSuccessful()) {
            applications = response.successResponse.applications;
        } else if (response.exception != null) {
            Exception exception = response.exception;
        }

        Application currentApplication = null;
        if (applications.size() > 0) {
            for (Application application : applications) {
                try {
                    if (application.data.get("appName").equals(campaignName)) {
                        currentApplication = application;
                    }
                }catch (Exception e){

                }
            }
        }
        return currentApplication;
    }
}

