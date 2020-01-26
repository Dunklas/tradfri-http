package com.github.tradfrihttp;

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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

public class InitialAuth {

    public static final String AUTH_ENDPOINT = "/15011/9063";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.printf("Usage: ./InitialAuth CLIENT_ID PSK\n");
            System.exit(0);
        }

        String clientId = args[0];
        String psk = args[1];

        InetSocketAddress gatewayAddress = findGateway();
        if (gatewayAddress == null) {
            System.err.printf("No IKEA trådfri gateway found.");
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
        System.out.print("Found IKEA trådfri gateway\n");
        System.out.printf("Host: %s\n", address.getHostName());
        System.out.printf("Port: %d\n", address.getPort());
        String version = instance.lookupAttribute("version");
        if (version != null && !version.equals("")) {
            System.out.printf("Firmware: %s\n", version);
        }
    }

    private static void doInitialAuth(InetSocketAddress gatewayAddress, String clientId, String psk) {
        System.out.printf("Doing initial auth.\nGateway: %s:%d\nClient-id: %s\nPSK: %s\n", gatewayAddress.getHostName(), gatewayAddress.getPort(), clientId, psk);
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
            printAuthResponse(response.getPayloadString());
        } catch (InterruptedException e) {
            System.out.println("No response");
            System.exit(1);
        }
        clientConnector.destroy();
    }

    private static void printAuthResponse(String payload) {
        if (payload == null || payload.trim().isEmpty()) {
            System.out.println("No payload.");
            return;
        }
        JSONObject jsonPayload;
        try {
            jsonPayload = (JSONObject) new JSONParser().parse(payload);
        } catch (ParseException pe) {
            System.out.println("Malformed payload.");
            return;
        }
        System.out.println("Secret key: " + jsonPayload.get("9091"));
    }
}
