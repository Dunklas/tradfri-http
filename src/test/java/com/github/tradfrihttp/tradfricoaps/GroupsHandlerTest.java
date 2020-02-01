package com.github.tradfrihttp.tradfricoaps;

import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class GroupsHandlerTest {

    @Mock
    private CoapsClient client;

    private GroupsHandler groupsHandler;

    @BeforeEach
    void setup() {
        groupsHandler = new GroupsHandler(client, "some-gateway-ip", "8000");
    }

    @Test
    void getGroupsShouldHandleValidResponse() throws InterruptedException, TradfriCoapsApiException {
        Response mockResponse = new Response(CoAP.ResponseCode.CONTENT);
        mockResponse.setPayload("[131073]");
        when(client.get(any())).thenReturn(mockResponse);
        List<Integer> groupIds = groupsHandler.handleGetGroups();
        assertEquals(1, groupIds.size());
        assertEquals(131073, groupIds.get(0));
    }

    @Test
    void getGroupsShouldHandleUnknownStatusCodes() throws InterruptedException {
        Response mockResponse = new Response(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        when(client.get(any())).thenReturn(mockResponse);
        try {
            groupsHandler.handleGetGroups();
        } catch (TradfriCoapsApiException tcai) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, tcai.getHttpStatus());
        }
    }

    @Test
    void getGroupsShouldReturnEmptyList() throws InterruptedException, TradfriCoapsApiException {
        Response mockResponse = new Response(CoAP.ResponseCode.CONTENT);
        mockResponse.setPayload("[]");
        when(client.get(any())).thenReturn(mockResponse);
        List<Integer> groupIds = groupsHandler.handleGetGroups();
        assertEquals(0, groupIds.size());
    }

    @Test
    void getGroupShouldHandleValidResponse() throws InterruptedException, TradfriCoapsApiException {
        Response mockResponse = new Response(CoAP.ResponseCode.CONTENT);
        mockResponse.setPayload("{\"9001\":\"Dunk\",\"9002\":1579467603,\"5851\":0,\"9003\":131073,\"5850\":0,\"9039\":196608,\"9108\":0,\"9018\":{\"15002\":{\"9003\":[65537,65538]}}}");
        when(client.get(any())).thenReturn(mockResponse);
        LightGroup group = groupsHandler.handleGetGroup(0);
        assertEquals("Dunk", group.name);
        assertEquals(131073, group.id);
        assertEquals(65537, group.lightBulbs.get(0));
        assertEquals(65538, group.lightBulbs.get(1));
    }

    @Test
    void getGroupShouldHandleNotFound() throws InterruptedException {
        Response mockResponse = new Response(CoAP.ResponseCode.NOT_FOUND);
        when(client.get(any())).thenReturn(mockResponse);
        try {
            groupsHandler.handleGetGroup(0);
        } catch (TradfriCoapsApiException tcai) {
            assertEquals(HttpStatus.NOT_FOUND, tcai.getHttpStatus());
        }
    }

    @Test
    void getGroupShouldHandleUnknownStatusCode() throws InterruptedException {
        Response mockResponse = new Response(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        when(client.get(any())).thenReturn(mockResponse);
        try {
            groupsHandler.handleGetGroup(0);
        } catch (TradfriCoapsApiException tcai) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, tcai.getHttpStatus());
        }
    }
}
