package com.github.tradfrihttp.tradfricoaps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import com.github.tradfrihttp.tradfricoaps.model.LightBulbIkea;
import org.eclipse.californium.core.coap.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LightsHandler {

    private Logger LOG = LoggerFactory.getLogger(GroupsHandler.class.getCanonicalName());

    private static final String LIGHTS_ENDPOINT = "/15001";

    private CoapsClient coapsClient;
    private String gatewayIp;
    private String gatewayPort;

    LightsHandler(CoapsClient coapsClient, String gatewayIp, String gatewayPort) {
        this.coapsClient = coapsClient;
        this.gatewayIp = gatewayIp;
        this.gatewayPort = gatewayPort;
    }

    LightBulb handleGetLight(int id) throws TradfriCoapsApiException {
        String uri = String.format("coap://%s:%s%s/%d", gatewayIp, gatewayPort, LIGHTS_ENDPOINT, id);
        Response response;
        try {
            response = coapsClient.get(uri);
            LOG.info(response.getPayloadString());
        } catch (InterruptedException ie) {
            throw new TradfriCoapsApiException("No response from: " + uri, ie);
        }
        try {
            return new ObjectMapper().readValue(response.getPayload(), LightBulbIkea.class)
                    .toLightBulb();
        } catch (IOException ie) {
            throw new TradfriCoapsApiException("Could not parse payload", ie);
        }
    }
}
