package com.github.tradfrihttp.tradfricoaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import com.github.tradfrihttp.tradfricoaps.model.LightBulbIkea;
import com.github.tradfrihttp.tradfricoaps.model.PutLightPayload;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class LightsHandler {

    private Logger LOG = LoggerFactory.getLogger(LightsHandler.class.getCanonicalName());

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
            throw new TradfriCoapsApiException("No response from: " + uri, ie, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            return new ObjectMapper().readValue(response.getPayload(), LightBulbIkea.class)
                    .toLightBulb();
        } catch (IOException ie) {
            throw new TradfriCoapsApiException("Could not parse payload", ie, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    void handlePutLight(int id, boolean powerOn, int dimmer) throws TradfriCoapsApiException {
        if (!isValidPutLightRequest(dimmer)) {
            throw new TradfriCoapsApiException(String.format("Invalid request data: %b, %d", powerOn, dimmer), HttpStatus.BAD_REQUEST);
        }
        String uri = String.format("coap://%s:%s%s/%d", gatewayIp, gatewayPort, LIGHTS_ENDPOINT, id);
        PutLightPayload payloadContent = new PutLightPayload(powerOn, dimmer);
        String payload;
        try {
            payload = new ObjectMapper().writeValueAsString(payloadContent);
        } catch (JsonProcessingException jpe) {
            throw new TradfriCoapsApiException("Could not create request payload", jpe, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("Payload" + payload);
        Response response;
        try {
            response = coapsClient.put(uri, payload);
        } catch (InterruptedException ie) {
            throw new TradfriCoapsApiException("No response from: " + uri, ie, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!response.getCode().equals(CoAP.ResponseCode.CHANGED)) {
            throw new TradfriCoapsApiException(String.format("Unexpected status code from gateway: %s", response.getCode()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidPutLightRequest(int dimmer) {
        return dimmer >= 0 && dimmer < 255;
    }
}
