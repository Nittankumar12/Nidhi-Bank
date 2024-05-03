package com.RWI.Nidhi.admin.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminViewsAgentDto {
    private int AgentId;
    private String agentName;
    private String agentAddress;
    private String agentPhoneNum;
    private String agentEmail;
    private int numberOfFd;
    private int numberOfMis;
    private int numberOfRd;
    private int numberOfScheme;


}
