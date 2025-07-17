package com.shmashine.api.module.fault.input;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class FaultDefinitionModule {

    List<String> elevatorCodes;

    List<Map<String, String>> faultShieldInfo;
}
