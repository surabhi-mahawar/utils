package com.uci.utils.telemetry;

import java.util.HashMap;
import java.util.Map;

import com.uci.utils.telemetry.dto.Actor;
import com.uci.utils.telemetry.dto.Context;
import com.uci.utils.telemetry.dto.Telemetry;
import com.uci.utils.telemetry.util.TelemetryEvents;

public class LogTelemetryBuilder {
	private static final String TELEMETRY_IMPL_VERSION = "3.0";
    private static final String QUESTION_TELEMETRY_IMPL_VERSION = "1.0";
    private static final String ACTOR_TYPE_SYSTEM = "System";
    private static final String DIKSHA_ORG = "DIKSHA";
    private static final String LOG_EVENT_MID = "fb3db9abceb578d8acbc812cdbd9c931";
    //represents the MD5 Hash of "Individual Question-Responses - Survey/Questionnaire"

    public String build(
            String message,
            String level) {
        Context context = Context.builder()
                .channel(DIKSHA_ORG)
                .env("sys")
                .build();
        
        Map<String, Object> edata = new HashMap<>();
        edata.put("type", "system");
        edata.put("level", level);
        edata.put("message", message);
//        if(params != null) {
//        	edata.put("params", params);
//        }
       
        Telemetry telemetry = Telemetry.builder().
                eid(TelemetryEvents.LOG.getName()).
                ets(System.currentTimeMillis()).
                ver(TELEMETRY_IMPL_VERSION).
                mid(LOG_EVENT_MID)
                .actor(Actor.builder()
                        .type(ACTOR_TYPE_SYSTEM)
//                        .id()
                        .build())
                .context(context)
//                .object(object)
                .edata(edata)
                .build();
        return Telemetry.getTelemetryRequestData(telemetry);
    }
}

