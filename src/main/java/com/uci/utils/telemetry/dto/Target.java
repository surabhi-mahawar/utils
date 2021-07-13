package com.uci.utils.telemetry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@Builder
public class Target {

  private String id;
  private String type;
  private String ver;
  private Map<String, String> rollup;
}
