package com.uci.utils.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inversoft.error.Errors;
import com.inversoft.rest.ClientResponse;
import com.uci.utils.CampaignService;
import com.uci.utils.UtilsTestConfig;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.Application;
import io.fusionauth.domain.FamilyMember;
import io.fusionauth.domain.User;
import io.fusionauth.domain.api.FamilyRequest;
import io.fusionauth.domain.api.UserRequest;
import io.fusionauth.domain.api.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(classes = UtilsTestConfig.class)
//@RunWith(SpringRunner.class)
//@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    CampaignService campaignService;

    @Test
    // this method is being used in transformer (internal bot)
    void findByEmail() {
        String email = "example@gmail.com";
        User result = userService.findByEmail(email);
        // result is coming null
        System.out.println(result + "");
    }


    @Test
    void findUsersForESamwad() throws Exception {
        Application application = new Application();
        application.data.put("group", new ArrayList<String>());
        Mockito.when(campaignService.getCampaignFromNameESamwad(anyString())).thenReturn(application);

        String campaignName = "Test 4/11";
        List<String> result = userService.findUsersForESamwad(campaignName);

        assertNotNull(result);
    }


    @Test
    void getUsersFromFederatedServers() {
        String campaignID = "TestConstraintBot";
        userService.getUsersFromFederatedServers(campaignID);
    }
//
//    @Test
//    void getUsersMessageByTemplate() {
//        ObjectNode jsonData = new ObjectNode(JsonNodeFactory.instance);
//        userService.getUsersMessageByTemplate(jsonData);
//    }

    @Test
    void getProgramConstruct() {
        User user = new User();
        String value = "someValue";
        user.data.put("programConstruct", value);
        String result = userService.getProgramConstruct(user);
        assertEquals(result, value);
    }

//    @Test
//    void update() {
//        //first have to add a user for testing
//        User result = UserService.update(new User());
//    }

    @Test
    void isAssociate() {
        User user = new User();
        user.data.put("role", "Program Associate");
        Boolean result = userService.isAssociate(user);
        assertTrue(result);
    }

}