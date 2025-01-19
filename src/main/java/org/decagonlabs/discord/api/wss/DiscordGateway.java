package org.decagonlabs.discord.api.wss;

import com.alibaba.fastjson.JSONObject;
import org.decagonlabs.discord.Authorization;
import org.decagonlabs.discord.ClientMetadata;
import org.decagonlabs.discord.api.http.Gateway;
import org.decagonlabs.discord.api.http.Gateway.GatewayMetadata;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class DiscordGateway extends WebSocketClient {
    private Thread heartbeatAgent;
    private Integer sequenceNumber;

    private ClientMetadata meta;
    private GatewayMetadata gatewayMetadata;

    public DiscordGateway(ClientMetadata meta) throws URISyntaxException, IOException, InterruptedException {
        this(Gateway.getGateway(meta), meta);
    }

    private DiscordGateway(GatewayMetadata gatewayMetadata, ClientMetadata meta) throws URISyntaxException {
        super(new URI(gatewayMetadata.constructURL(meta)));
        this.gatewayMetadata = gatewayMetadata;
        this.meta = meta;
    }

    // websocket client
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (meta.isDoGlobalLogging()) Logger.info("Websocket opened at: " + super.uri.toASCIIString());
    }

    @Override
    public void onMessage(String s) {
        if(meta.isDoGlobalLogging()) Logger.info(s);
        JSONObject msg = JSONObject.parseObject(s);
        switch (msg.getIntValue("op")) {
            case 0  -> dispatch(msg);
            case 10 -> hello(msg);  
            case 11 -> ack(msg);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    // opcode handling
    public void dispatch(JSONObject msg) {
        if(meta.isDoGlobalLogging()) Logger.info("Received Dispatch");
        sequenceNumber = msg.getInteger("s");
        switch (msg.getString("t")) {
            case "READY" -> Dispatcher.READY(this, msg);
        }
    }

    public void hello(JSONObject msg) {
        int heartbeatInterval = msg.getJSONObject("d").getIntValue("heartbeat_interval");
        this.sequenceNumber = msg.getInteger("s");
        heartbeatAgent = new Thread(new HeartbeatAgent(heartbeatInterval, this));
        heartbeatAgent.setPriority(Thread.MAX_PRIORITY);
        heartbeatAgent.start();
    }

    public void ack(JSONObject msg) {

    }

    // package utility methods
    protected void sendMessage(String message, String reason) {
        if(meta.isDoGlobalLogging()) Logger.info("Sent Gateway Message for: " + reason);
        Logger.info(message);
        send(message);
    }

    protected void identify() throws UnknownHostException { // callable by HeartbeatAgent only
        JSONObject identity = new JSONObject();
        identity.put("op", 2);

        // prepare identity data
        JSONObject data = new JSONObject();
        data.put("token", meta.getAuth().getToken());
        JSONObject properties = new JSONObject();
        properties.put("os", System.getProperty("os.name"));
        properties.put("browser", "Aptitude Library on " + System.getProperty("java.version"));
        properties.put("device", "Aptitude Library on " + InetAddress.getLocalHost().getHostName()); // might change
        data.put("properties", properties);
        // TODO: make mutable soon
        data.put("compress", false);
        data.put("large_threshold", 100);
//        JSONArray sharding = new JSONArray();
//        sharding.add(0);
//        sharding.add(1);
//        data.put("shard", sharding);
        // TODO: presence?
        data.put("intents", 8);
        identity.put("d", data);

        sendMessage(identity.toJSONString(), "Identify Session to Discord");
    }

    protected Integer getSequenceNumber() {
        return sequenceNumber;
    }

    // temporary test
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String token = "MTMyNzU2MjY0NTA2MjI5MTQ4Nw.GGKis5.WcJT81EQ9Ua0QHaZgzOTlkT4SvlBAFLAtMkKh4";
        Authorization auth = new Authorization(Authorization.TokenType.Bot, token);
        ClientMetadata meta = new ClientMetadata(auth, 10, true);
        DiscordGateway gateway = new DiscordGateway(meta);
        gateway.connect();
    }
}
