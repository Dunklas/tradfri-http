package com.github.tradfrihttp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.straylightlabs.hola.dns.Domain;
import net.straylightlabs.hola.sd.Instance;
import net.straylightlabs.hola.sd.Query;
import net.straylightlabs.hola.sd.Service;
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

public class InitialAuth {

    public static Logger LOG = LoggerFactory.getLogger(InitialAuth.class.getCanonicalName());

    public static final String AUTH_ENDPOINT = "/15011/9063";

    public static void main(String[] args) {
        if (args.length != 2) {
            LOG.warn("Usage: ./InitialAuth CLIENT_ID PSK");
            System.exit(0);
        }

        String clientId = args[0];
        String psk = args[1];

        InetSocketAddress gatewayAddress = findGateway();
        if (gatewayAddress == null) {
            LOG.error("No IKEA trådfri gateway found.");
            System.exit(1);
        }
        doInitialAuth(gatewayAddress, clientId, psk);
    }

    private static InetSocketAddress findGateway() {
        Service service = Service.fromName("_coap._udp");
        Query query = Query.createFor(service, Domain.LOCAL);
        Set<Instance> instances = null;
        try {
            instances = query.runOnce();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        InetSocketAddress serverAddress = null;
        Instance firstInstance = instances.iterator().next();
        Set<InetAddress> addresses = firstInstance.getAddresses();
        for (InetAddress address : addresses) {
            if (address.getHostAddress().contains(".")) {
                serverAddress = new InetSocketAddress(address.getHostAddress(), firstInstance.getPort());
            }
        }
        printGatewayInfo(serverAddress, firstInstance);
        return serverAddress;
    }

    private static void printGatewayInfo(InetSocketAddress address, Instance instance) {
        LOG.info("Found IKEA trådfri gateway");
        LOG.info(String.format("Host: %s", address.getHostName()));
        LOG.info(String.format("Port: %d", address.getPort()));
        String version = instance.lookupAttribute("version");
        if (version != null && !version.equals("")) {
            LOG.info(String.format("Firmware: %s", version));
        }
    }

    private static void doInitialAuth(InetSocketAddress gatewayAddress, String clientId, String psk) {
        LOG.info(String.format("\nDoing initial auth.\nGateway: %s:%d\nClient-id: %s\nPSK: %s\n", gatewayAddress.getHostName(), gatewayAddress.getPort(), clientId, psk));
        StaticPskStore pskStore = new StaticPskStore("Client_identity", psk.getBytes());
        DtlsConnectorConfig config = new DtlsConnectorConfig.Builder()
                .setAddress(new InetSocketAddress(0))
                .setPskStore(pskStore)
                .build();
        DTLSConnector clientConnector = new DTLSConnector(config);
        CoapClient client = new CoapClient.Builder(gatewayAddress.getHostName(), gatewayAddress.getPort())
                .path("")
                .query("")
                .create();
        CoapEndpoint endpoint = new CoapEndpoint.Builder()
                .setConnector(clientConnector)
                .setNetworkConfig(NetworkConfig.getStandard())
                .build();
        client.setEndpoint(endpoint);

        Request req = new Request(CoAP.Code.POST);
        req.setURI(String.format("coap://%s:%d%s", gatewayAddress.getHostName(), gatewayAddress.getPort(), AUTH_ENDPOINT));
        req.setPayload(String.format("{\"9090\": \"%s\"}", clientId));
        try {
            endpoint.sendRequest(req);
            Response response = req.waitForResponse();
            printAuthResponse(response.getPayload());
        } catch (InterruptedException e) {
            LOG.error("No response from gateway");
            System.exit(1);
        }
        clientConnector.destroy();
    }

    private static void printAuthResponse(byte[] payload) {
        try {
            AuthResponse authResponse = new ObjectMapper().readValue(payload, AuthResponse.class);
            LOG.info(String.format("Secret key: %s", authResponse.getSecretKey()));
        } catch (IOException ie) {
            LOG.error("Malformed or missing payload.");
            System.exit(1);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class AuthResponse {

        private String secretKey;

        @JsonSetter("9091")
        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getSecretKey() {
            return this.secretKey;
        }
    }
}
