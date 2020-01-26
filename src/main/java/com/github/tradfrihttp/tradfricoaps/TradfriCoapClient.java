package com.github.tradfrihttp.tradfricoaps;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.StaticPskStore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.github.tradfrihttp.model.LightGroup;

@Component
public class TradfriCoapClient implements TradfriCoapApi {

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
        JSONArray groupIds;
        try {
            coapEndpoint.sendRequest(req);
            Response response = req.waitForResponse();
            groupIds = (JSONArray) new JSONParser().parse(response.getPayloadString());
        } catch (InterruptedException| ParseException ie) {
            System.out.println("ERROR!!" + ie.getMessage());
            return new ArrayList<>();
        }

        List<LightGroup> lightGroups = new ArrayList<>();
        for (Object obj : groupIds) {
            Long groupId = (Long) obj;
            LightGroup group = getGroup(groupId);
            lightGroups.add(group);
        }
        return lightGroups;
    }

    @Override
    public LightGroup getGroup(long groupId) {
        Request req = new Request(CoAP.Code.GET);
        req.setURI(String.format("coap://%s:%s%s/%d", gatewayIp, gatewayPort, GROUPS_ENDPOINT, groupId));
        try {
            coapEndpoint.sendRequest(req);
            Response response = req.waitForResponse();
            JSONObject groupJson = (JSONObject) new JSONParser().parse(response.getPayloadString());
            LightGroup.Builder lightGroup = new LightGroup.Builder()
                    .setId((Long) groupJson.get("9003"))
                    .setName((String) groupJson.get("9001"));
            List<Long> lightBulbs = new ArrayList<>();
            JSONObject someNestedThing = (JSONObject) groupJson.get("9018");
            JSONObject someOtherNestedThing = (JSONObject) someNestedThing.get("15002");
            for (Object lightBulbObj : (JSONArray) someOtherNestedThing.get("9003")) {
                Long lightBulbId = (Long) lightBulbObj;
                lightBulbs.add(lightBulbId);
            }
            lightGroup.setLightBulbs(lightBulbs);
            return lightGroup.build();


        } catch (InterruptedException|ParseException e) {
            System.out.println("ERROR!!" + e.getMessage());
            return null;
        }
    }
}
