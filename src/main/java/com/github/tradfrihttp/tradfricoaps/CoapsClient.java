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

import java.net.InetSocketAddress;

public class CoapsClient {

    private CoapEndpoint coapEndpoint;

    CoapsClient(String hostname, String port, String clientId, String secretKey) {
        StaticPskStore pskStore = new StaticPskStore(clientId, secretKey.getBytes());
        DtlsConnectorConfig config = new DtlsConnectorConfig.Builder()
                .setAddress(new InetSocketAddress(0))
                .setPskStore(pskStore)
                .build();
        DTLSConnector clientConnector = new DTLSConnector(config);
        CoapClient client = new CoapClient.Builder(hostname, Integer.parseInt(port))
                .path("")
                .query("")
                .create();
        coapEndpoint = new CoapEndpoint.Builder()
                .setConnector(clientConnector)
                .setNetworkConfig(NetworkConfig.getStandard())
                .build();
        client.setEndpoint(coapEndpoint);
    }

    Response get(String uri) throws InterruptedException {
        Request request = new Request(CoAP.Code.GET);
        request.setURI(uri);
        coapEndpoint.sendRequest(request);
        return request.waitForResponse();
    }
}
