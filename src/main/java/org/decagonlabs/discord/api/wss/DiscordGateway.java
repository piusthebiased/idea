package org.decagonlabs.discord.api.wss;

import org.decagonlabs.discord.ClientMetadata;
import org.tinylog.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class DiscordGateway extends WebSocketClient {
    //private final static String base =

    public DiscordGateway(ClientMetadata meta) throws URISyntaxException {
        this(new URI("test"));
    }

    private DiscordGateway(URI serverUri) {
        super(serverUri);
    }

    // websocket client
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Logger.info("Websocket opened.");
    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
