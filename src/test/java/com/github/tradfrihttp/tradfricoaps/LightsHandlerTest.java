package com.github.tradfrihttp.tradfricoaps;

import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LightsHandlerTest {

    @Mock
    private CoapsClient client;

    private LightsHandler lightsHandler;

    @BeforeEach
    void setup() {
        lightsHandler = new LightsHandler(client, "some-gateway-ip", "8000");
    }

    @Test
    void getLightShouldHandleValidResponse() throws InterruptedException, TradfriCoapsApiException {
        Response mockResponse = new Response(CoAP.ResponseCode.CONTENT);
        mockResponse.setPayload("{\"9001\":\"TRADFRI bulb\",\"9002\":1579467404,\"9020\":1580453223,\"9019\":1,\"9003\":65537,\"9054\":0,\"5750\":2,\"3\":{\"1\":\"TRADFRI bulb E14 WS opal 600lm\",\"0\":\"IKEA of Sweden\",\"2\":\"\",\"3\":\"2.0.023\",\"6\":1},\"3311\":[{\"5850\":1,\"9003\":0,\"5851\":137,\"5717\":0,\"5711\":370,\"5709\":32886,\"5710\":27217,\"5706\":\"f1e0b5\"}]}");
        when(client.get(any())).thenReturn(mockResponse);
        LightBulb lightBulb = lightsHandler.handleGetLight(0);
        assertEquals(65537, lightBulb.id);
        assertEquals("TRADFRI bulb", lightBulb.name);
        assertEquals("TRADFRI bulb E14 WS opal 600lm", lightBulb.deviceTypeName);
        assertEquals("2.0.023", lightBulb.deviceTypeId);
        assertEquals(true, lightBulb.powerOn);
        assertEquals(137, lightBulb.dimmer);
        assertEquals(32886, lightBulb.xColor);
        assertEquals(27217, lightBulb.yColor);
        assertEquals("f1e0b5", lightBulb.hexColor);
    }

    @Test
    void getLightShouldHandleNotFound() throws InterruptedException {
        Response mockResponse = new Response(CoAP.ResponseCode.NOT_FOUND);
        when(client.get(any())).thenReturn(mockResponse);
        try {
            lightsHandler.handleGetLight(0);
        } catch (TradfriCoapsApiException tcai) {
            assertEquals(HttpStatus.NOT_FOUND, tcai.getHttpStatus());
        }
    }

    @Test
    void getLightShouldHandleUnexpectedStatusCode() throws InterruptedException {
        Response mockResponse = new Response(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        when(client.get(any())).thenReturn(mockResponse);
        try {
            lightsHandler.handleGetLight(0);
        } catch (TradfriCoapsApiException tcai) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, tcai.getHttpStatus());
        }
    }

    @Test
    void putLightShouldHandleValidRequest() throws InterruptedException, TradfriCoapsApiException {
        Response mockResponse = new Response(CoAP.ResponseCode.CHANGED);
        when(client.put(anyString(), anyString())).thenReturn(mockResponse);
        lightsHandler.handlePutLight(0, true, 120);
    }

    @Test
    void putLightShouldHandleInvalidRequest() {
        int illegalDimmerValue = 255;
        try {
            lightsHandler.handlePutLight(0, true, illegalDimmerValue);
        } catch (TradfriCoapsApiException tcai) {
            assertEquals(tcai.getHttpStatus(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void putLightShouldHandleUnexpectedStatusCode() throws InterruptedException {
        Response mockResponse = new Response(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        when(client.put(anyString(), anyString())).thenReturn(mockResponse);
        try {
            lightsHandler.handlePutLight(0, true, 254);
        } catch (TradfriCoapsApiException tcai) {
            assertEquals(tcai.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
