package com.uci.utils.bot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BotUtilTest {

    static ObjectMapper objectMapper;

    @BeforeAll
    public static void init(){
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    public static void teardown(){
        System.out.println("teardown");
    }

    @Test
     void getBotValidFromJsonNode() throws JsonProcessingException {
        String jsonString = "{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCIDemo\",\"startingMessage\":\"HiUCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2022-02-11T14:09:53.570Z\",\"status\":\"enabled\",\"description\":\"ForInternalDemo\",\"startDate\":\"2022-02-01T00:00:00.000Z\",\"endDate\":null,\"purpose\":\"ForInternalDemo\",\"ownerOrgID\":\"ORG_001\",\"ownerID\":\"95e4942d-cbe8-477d-aebd-ad8e6de4bfc8\",\"userSegments\":[],\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":{\"id\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"channel\":\"WhatsApp\",\"provider\":\"gupshup\",\"config\":{\"2WAY\":\"2000193033\",\"phone\":\"9876543210\",\"HSM_ID\":\"2000193031\",\"credentials\":{\"vault\":\"samagra\",\"variable\":\"gupshupSamagraProd\"}},\"name\":\"SamagraProd\",\"updated_at\":\"2021-06-16T06:02:39.125Z\",\"created_at\":\"2021-06-16T06:02:41.823Z\"},\"name\":\"UCIDemo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2022-02-03T12:29:32.959Z\",\"description\":null}]}";
        JsonNode data = objectMapper.readTree(jsonString);
        String result = BotUtil.getBotValidFromJsonNode(data);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    void checkBotValidFromJsonNode() throws JsonProcessingException {
        String jsonString = "[{\"id\":\"d655cf03-1f6f-4510-acf6-d3f51b488a5e\",\"name\":\"UCIDemo\",\"startingMessage\":\"HiUCI\",\"users\":[],\"logicIDs\":[\"e96b0865-5a76-4566-8694-c09361b8ae32\"],\"owners\":null,\"created_at\":\"2021-07-08T18:48:37.740Z\",\"updated_at\":\"2022-02-11T14:09:53.570Z\",\"status\":\"enabled\",\"description\":\"ForInternalDemo\",\"startDate\":\"2022-02-01T00:00:00.000Z\",\"endDate\":null,\"purpose\":\"ForInternalDemo\",\"ownerOrgID\":\"ORG_001\",\"ownerID\":\"95e4942d-cbe8-477d-aebd-ad8e6de4bfc8\",\"userSegments\":[],\"logic\":[{\"id\":\"e96b0865-5a76-4566-8694-c09361b8ae32\",\"transformers\":[{\"id\":\"bbf56981-b8c9-40e9-8067-468c2c753659\",\"meta\":{\"form\":\"https://hosted.my.form.here.com\",\"formID\":\"UCI-demo-1\"}}],\"adapter\":{\"id\":\"44a9df72-3d7a-4ece-94c5-98cf26307324\",\"channel\":\"WhatsApp\",\"provider\":\"gupshup\",\"config\":{\"2WAY\":\"2000193033\",\"phone\":\"9876543210\",\"HSM_ID\":\"2000193031\",\"credentials\":{\"vault\":\"samagra\",\"variable\":\"gupshupSamagraProd\"}},\"name\":\"SamagraProd\",\"updated_at\":\"2021-06-16T06:02:39.125Z\",\"created_at\":\"2021-06-16T06:02:41.823Z\"},\"name\":\"UCIDemo\",\"created_at\":\"2021-07-08T18:47:44.925Z\",\"updated_at\":\"2022-02-03T12:29:32.959Z\",\"description\":null}]}]";
        JsonNode data = objectMapper.readTree(jsonString);
        Boolean result = BotUtil.checkBotValidFromJsonNode(data);
        assertTrue(result);
    }

    @Test
    void checkBotLiveStatus() {
        String status = "live";
        Boolean result = BotUtil.checkBotLiveStatus(status);
        assertTrue(result);
    }

    @Test
    void checkBotStartDateValid() {
        String startDate = "2022-02-01T00:00:00.000Z";
        Boolean result = BotUtil.checkBotStartDateValid(startDate);
        assertTrue(result);
    }

    @Test
    void checkBotEndDateValid() {
        String endDate = null;
        Boolean result = BotUtil.checkBotEndDateValid(endDate);
        assertTrue(result);
    }
}