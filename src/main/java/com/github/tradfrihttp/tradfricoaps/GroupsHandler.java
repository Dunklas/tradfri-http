package com.github.tradfrihttp.tradfricoaps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import com.github.tradfrihttp.tradfricoaps.model.LightGroupIkea;
import org.eclipse.californium.core.coap.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

class GroupsHandler {

    private Logger LOG = LoggerFactory.getLogger(GroupsHandler.class.getCanonicalName());

    private static final String GROUPS_ENDPOINT = "/15004";

    private CoapsClient coapsClient;
    private String gatewayIp;
    private String gatewayPort;

    GroupsHandler(CoapsClient coapsClient, String gatewayIp, String gatewayPort) {
        this.coapsClient = coapsClient;
        this.gatewayIp = gatewayIp;
        this.gatewayPort = gatewayPort;
    }

    List<Integer> handleGetGroups() throws TradfriCoapsApiException {
        String uri = String.format("coap://%s:%s%s", gatewayIp, gatewayPort, GROUPS_ENDPOINT);
        Response response;
        try {
            response = coapsClient.get(uri);
        } catch (InterruptedException ie) {
            throw new TradfriCoapsApiException("No response from: " + uri, ie);
        }
        try {
            return new ObjectMapper().readValue(response.getPayload(), new TypeReference<List<Integer>>() {});
        } catch (IOException ie) {
            throw new TradfriCoapsApiException("Could not parse payload", ie);
        }
    }

    LightGroup handleGetGroup(int groupId) throws TradfriCoapsApiException {
        String uri = String.format("coap://%s:%s%s/%d", gatewayIp, gatewayPort, GROUPS_ENDPOINT, groupId);
        Response response;
        try {
            response = coapsClient.get(uri);
        } catch (InterruptedException ie) {
            throw new TradfriCoapsApiException("No response from: " + uri, ie);
        }
        try {
            return new ObjectMapper().readValue(response.getPayload(), LightGroupIkea.class)
                    .toLightGroup();
        } catch (IOException ie) {
            throw new TradfriCoapsApiException("Could not parse payload", ie);
        }
    }
}
