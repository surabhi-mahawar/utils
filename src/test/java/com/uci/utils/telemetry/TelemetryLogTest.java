package com.uci.utils.telemetry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import com.uci.utils.ApplicationConfiguration;


@SpringBootTest(classes=ApplicationConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
public class TelemetryLogTest {
	private static final Logger telemetrylogger = LogManager.getLogger(TelemetryLogger.class);
	
	public void telemetryLogTest() {
//		telemetrylogger.info(new LogTelemetryMessage(String.format("Start Conversation with incoming message : %s", incomingMessage),
//				TelemetryEventNames.STARTCONVERSATION, "", xmsg.getChannel(),
//				xmsg.getProvider(), producerID, xmsg.getFrom().getUserID(), id, ownerId));
	}
}
