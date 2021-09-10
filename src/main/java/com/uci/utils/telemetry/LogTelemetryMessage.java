package com.uci.utils.telemetry;

import org.apache.logging.log4j.message.Message;
import org.json.JSONObject;

public class LogTelemetryMessage implements Message {
	private final String requestBody;

	public LogTelemetryMessage(String requestBody) {
		this.requestBody = requestBody;
	}

	@Override
	public String getFormattedMessage() {
		LogTelemetryBuilder object = new LogTelemetryBuilder();
		
		String msgBody = object.build(requestBody, "INFO");
		JSONObject jsonBody = new JSONObject(msgBody);

		return jsonBody.toString();
	}

	@Override
	public String getFormat() {
		return requestBody.toString();
	}

	@Override
	public Object[] getParameters() {
		return new Object[0];
	}

	@Override
	public Throwable getThrowable() {
		return null;
	}
}

