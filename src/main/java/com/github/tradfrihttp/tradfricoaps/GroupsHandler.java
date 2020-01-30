package com.github.tradfrihttp.tradfricoaps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import com.github.tradfrihttp.tradfricoaps.model.LightGroupIkea;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GroupsHandler {

    private Logger LOG = LoggerFactory.getLogger(TradfriCoapsClient.class.getCanonicalName());

    private static final String GROUPS_ENDPOINT = "/15004";

    private String gatewayIp;
    private String gatewayPort;

    GroupsHandler(String gatewayIp, String gatewayPort) {
        this.gatewayIp = gatewayIp;
        this.gatewayPort = gatewayPort;
    }

    List<LightGroup> handleGetGroups(CoapEndpoint endpoint, Request req) throws TradfriCoapsApiException {
        req.setURI(String.format("coap://%s:%s%s", gatewayIp, gatewayPort, GROUPS_ENDPOINT));
        List<LightGroup> lightGroups = new ArrayList<>();
        try {
            endpoint.sendRequest(req);
            Response response = req.waitForResponse();
            List<Integer> groupIds = new ObjectMapper().readValue(response.getPayload(), new TypeReference<List<Integer>>() {});
            for (Integer groupId : groupIds) {
                // TODO: Re-using Request object, this is weird
                LightGroup lightGroup = handleGetGroup(endpoint, Request.newGet(), groupId);
                if (lightGroup != null) {
                    lightGroups.add(lightGroup);
                } else {
                    LOG.warn(String.format("Could not fetch group with id: %d", groupId));
                }
            }
        } catch (InterruptedException|IOException ie) {
            LOG.error("An exception occurred while retrieving groups: ", ie);
            return new ArrayList<>();
        }
        return lightGroups;
    }

    LightGroup handleGetGroup(CoapEndpoint endpoint, Request req, int groupId) throws TradfriCoapsApiException {
        req.setURI(String.format("coap://%s:%s%s/%d", gatewayIp, gatewayPort, GROUPS_ENDPOINT, groupId));
        Response response;
        try {
            endpoint.sendRequest(req);
            response = req.waitForResponse();
        } catch (InterruptedException ie) {
            throw new TradfriCoapsApiException("No response from: " + req.getURI(), ie);
        }
        try {
            return new ObjectMapper().readValue(response.getPayload(), LightGroupIkea.class)
                    .toLightGroup();
        } catch (IOException ie) {
            throw new TradfriCoapsApiException("Could not parse payload", ie);
        }
    }
}
