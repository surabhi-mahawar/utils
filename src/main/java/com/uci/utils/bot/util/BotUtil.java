package com.uci.utils.bot.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotUtil {
	public static String botLiveStatus = "Live";

	public static Boolean checkBotValidFromJsonNode(JsonNode root) {
		JsonNode data = root.path("result").path("data");
    	
    	String status = data.findValue("status").asText();
    	String startDate = data.findValue("startDate").asText();
		
    	log.info("Bot Status: "+status);
    	log.info("Bot Start Date: "+startDate);
    	
    	return checkBotValid(status, startDate);
	}
	
	public static Boolean checkBotValid(String status, String startDate) {
		System.out.println("status r:"+checkBotLiveStatus(status));
		System.out.println("date r:"+checkBotStartDateValid(startDate));
		
		if(checkBotLiveStatus(status) && checkBotStartDateValid(startDate)) {
			return true;
		}
		return false;
	}
	
	public static Boolean checkBotLiveStatus(String status) {
		if(status.equals(botLiveStatus)) {
			return true;
		}
		return false;
	}
	
	public static Boolean checkBotStartDateValid(String startDate) {
		try {
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        	
        	/* local date time */
        	LocalDateTime localNow = LocalDateTime.now();
        	String dateString = fmt.format(localNow).toString();
        	LocalDateTime localDateTime = LocalDateTime.parse(dateString, fmt);
        	
        	/* bot start date in local date time format */
        	LocalDateTime localStartDate = LocalDateTime.parse(startDate, fmt);
            
        	System.out.println(localDateTime);
        	System.out.println(localStartDate);
        	System.out.println(localDateTime.compareTo(localStartDate));
        	
        	if(localDateTime.compareTo(localStartDate) >= 0) {
        		return true;
        	}
		} catch (Exception e) {
			log.error("Error in checkBotStartDateValid: "+e.getMessage());
		}
		return false;
	}
}
