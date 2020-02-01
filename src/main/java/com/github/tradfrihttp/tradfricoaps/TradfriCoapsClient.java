package com.github.tradfrihttp.tradfricoaps;

import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import com.github.tradfrihttp.model.LightGroup;

@Component
public class TradfriCoapsClient implements TradfriCoapsApi {

    private Logger LOG = LoggerFactory.getLogger(TradfriCoapsClient.class.getCanonicalName());

    @Value("${gateway-ip}")
    private String gatewayIp;
    @Value("${gateway-port}")
    private String gatewayPort;
    @Value("${client-id}")
    private String clientId;
    @Value("${client-secret}")
    private String secretKey;

    private GroupsHandler groupsHandler;
    private LightsHandler lightsHandler;

    @PostConstruct
    private void init() {
        CoapsClient coapsClient = new CoapsClient(gatewayIp, gatewayPort, clientId, secretKey);
        groupsHandler = new GroupsHandler(coapsClient, this.gatewayIp, this.gatewayPort);
        lightsHandler = new LightsHandler(coapsClient, this.gatewayIp, this.gatewayPort);
    }

    @Override
    public List<LightGroup> getGroups() throws TradfriCoapsApiException {
        List<Integer> groupIds = groupsHandler.handleGetGroups();
        List<LightGroup> groups = new ArrayList<>();
        for (Integer groupId : groupIds) {
            groups.add(getGroup(groupId));
        }
        return groups;
    }

    @Override
    public LightGroup getGroup(int groupId) throws TradfriCoapsApiException {
        return groupsHandler.handleGetGroup(groupId);
    }

    @Override
    public LightBulb getLight(int lightId) throws TradfriCoapsApiException {
        return lightsHandler.handleGetLight(lightId);
    }

    @Override
    public LightBulb putLight(int lightId, boolean powerOn, int dimmer) throws TradfriCoapsApiException {
        return lightsHandler.handlePutLight(lightId, powerOn, dimmer);
    }
}
