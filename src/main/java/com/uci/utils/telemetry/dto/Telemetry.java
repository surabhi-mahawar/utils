package com.uci.utils.telemetry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uci.utils.models.util.LoggerEnum;
import com.uci.utils.models.util.ProjectLogger;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Telemetry V3 POJO to generate telemetry event. */
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@Builder
public class Telemetry {

  private static final ObjectMapper mapper = new ObjectMapper();
  private String eid;
  private long ets ;
  private String ver;
  private String mid;
  private Actor actor;
  private Context context;
  private Target object;
  private Map<String, Object> edata;
  private List<String> tags;


  public String getTelemetryRequestData() {
      String event = "";
      try {
        event = mapper.writeValueAsString(this);
        ProjectLogger.log("TelemetryGenerator:getTelemetry = Telemetry Event : " + event, LoggerEnum.DEBUG.name());
      } catch (Exception e) {
        ProjectLogger.log("TelemetryGenerator:getTelemetry = Telemetry Event: failed to generate audit events:" + e, LoggerEnum.ERROR.name());
      }
      return event;

  }
}
