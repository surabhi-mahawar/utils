package com.uci.utils.telemetry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Actor {

  private String id;
  private String type;
}
