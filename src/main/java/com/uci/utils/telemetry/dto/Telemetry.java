package com.uci.utils.telemetry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @JsonIgnore
  private long ets ;

  @JsonIgnore
  private String ver;

  @JsonIgnore
  private String mid;

  @JsonIgnore
  private Actor actor;

  @JsonIgnore
  private Context context;

  @JsonIgnore
  private Target object;

  @JsonIgnore
  private Map<String, Object> edata;

  @JsonIgnore
  private List<String> tags;


  public String getTelemetryRequestData() {
      String event = "";
//      try {
//        event = mapper.writeValueAsString(this);
//      } catch (Exception e) {
//      }
      return event;

  }
}
