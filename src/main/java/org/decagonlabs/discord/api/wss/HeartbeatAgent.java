package org.decagonlabs.discord.api.wss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.tinylog.Logger;

import java.net.UnknownHostException;

public class HeartbeatAgent implements Runnable {
    private final int heartbeatInterval;
    private final DiscordGateway gateRef;

    public HeartbeatAgent(int heartbeatInterval, DiscordGateway gateRef) {
        this.heartbeatInterval = heartbeatInterval;
        this.gateRef = gateRef;
    }

    @Override
    public void run() {
        Logger.info("Running HeartbeatAgent at " + heartbeatInterval + "ms/beat");

        // heartbeat jitter
        try {
            Thread.sleep((long) (heartbeatInterval * Math.random()));
            gateRef.sendMessage(constructHeartbeat(), "Initial Heartbeat");
            gateRef.identify();
        } catch (InterruptedException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // continue heartbeat
        while(!gateRef.isClosed()) {
            try {
                Thread.sleep(heartbeatInterval);
                gateRef.sendMessage(constructHeartbeat(), "Heartbeat");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String constructHeartbeat() {
        JSONObject object = new JSONObject();
        object.put("op", 1);
        object.put("d", null);

        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }
}
