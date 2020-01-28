package com.github.tradfrihttp.tradfricoaps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tradfrihttp.tradfricoaps.model.LightGroupIkea;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.StaticPskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.github.tradfrihttp.model.LightGroup;

@Component
public class TradfriCoapsClient implements TradfriCoapsApi {

    private Logger LOG = LoggerFactory.getLogger(TradfriCoapsClient.class.getCanonicalName());

    private static final String GROUPS_ENDPOINT = "/15004";

    @Value("${gateway-ip}")
    private String gatewayIp;
    @Value("${gateway-port}")
    private String gatewayPort;
    @Value("${client-id}")
    private String clientId;
    @Value("${client-secret}")
    private String secretKey;

    private CoapEndpoint coapEndpoint;

    @PostConstruct
    private void init() {
        StaticPskStore pskStore = new StaticPskStore(clientId, secretKey.getBytes());
        DtlsConnectorConfig config = new DtlsConnectorConfig.Builder()
                .setAddress(new InetSocketAddress(0))
                .setPskStore(pskStore)
                .build();
        DTLSConnector clientConnector = new DTLSConnector(config);
        CoapClient client = new CoapClient.Builder(gatewayIp, Integer.parseInt(gatewayPort))
                .path("")
                .query("")
                .create();
        coapEndpoint = new CoapEndpoint.Builder()
                .setConnector(clientConnector)
                .setNetworkConfig(NetworkConfig.getStandard())
                .build();
        client.setEndpoint(coapEndpoint);
    }

    @Override
    public List<LightGroup> getGroups() {
        Request req = new Request(CoAP.Code.GET);
        req.setURI(String.format("coap://%s:%s%s", gatewayIp, gatewayPort, GROUPS_ENDPOINT));
        List<LightGroup> lightGroups = new ArrayList<>();
        try {
            coapEndpoint.sendRequest(req);
            Response response = req.waitForResponse();
            List<Integer> groupIds = new ObjectMapper().readValue(response.getPayload(), new TypeReference<List<Integer>>() {});
            for (Integer groupId : groupIds) {
                LightGroup lightGroup = getGroup(groupId);
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

    @Override
    public LightGroup getGroup(int groupId) {
        Request req = new Request(CoAP.Code.GET);
        req.setURI(String.format("coap://%s:%s%s/%d", gatewayIp, gatewayPort, GROUPS_ENDPOINT, groupId));
        try {
            coapEndpoint.sendRequest(req);
            Response response = req.waitForResponse();
            return new ObjectMapper().readValue(response.getPayload(), LightGroupIkea.class)
                    .toLightGroup();
        } catch (InterruptedException|IOException ie) {
            LOG.error("An exception occurred while retrieving group: ", ie);
            return null;
        }
    }
}
