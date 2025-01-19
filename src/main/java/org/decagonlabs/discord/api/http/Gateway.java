package org.decagonlabs.discord.api.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.decagonlabs.discord.ClientMetadata;
import org.decagonlabs.discord.api.http.utils.RequestBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.decagonlabs.discord.api.http.utils.RequestArgs.RequestMethod;

import static org.decagonlabs.discord.Client.*;

public class Gateway {
    // Discord native Gateway fetch request [1]
    private static URI getGateway() throws IOException, URISyntaxException, InterruptedException {
        final String path = "/gateway";
        HttpRequest req = new RequestBuilder(RequestMethod.GET, path).build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        JSONObject responseObject = JSONObject.parseObject(res.body());

        return new URI(responseObject.getString("url"));
    }

    // Discord Bot Native Gateway fetch, preferred fetch [2]
    public static GatewayMetadata getGateway(ClientMetadata meta) throws URISyntaxException, IOException, InterruptedException {
        final String path = "/gateway/bot";
        RequestBuilder builder = new RequestBuilder(RequestMethod.GET, path);
        meta.getAuth().appendAuthorizationHeader(builder);

        HttpRequest req = builder.build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println(res.body());
        return GatewayMetadata.createObjectFromJSON(res.body());
    }

    // Nested Class [2]
    public class GatewayMetadata {
        private final String url;
        private final int shards;
        private final SessionStartLimit sessionStartLimit;

        public GatewayMetadata(String url, int shards, SessionStartLimit sessionStartLimit) {
            this.url = url;
            this.shards = shards;
            this.sessionStartLimit = sessionStartLimit;
        }

        // TODO: implement compression
        public String constructURL(ClientMetadata meta) {
            return url + "/?v=" + meta.getVersion() + "&encoding=json";
        }

        public String getUrl() {
            return url;
        }

        public int getShards() {
            return shards;
        }

        public SessionStartLimit getSessionStartLimit() {
            return sessionStartLimit;
        }

        public static GatewayMetadata createObjectFromJSON(String json) {
            return JSON.parseObject(json, GatewayMetadata.class);
        }

        public class SessionStartLimit {
            private final int total;
            private final int remaining;
            private final int resetAfter;
            private final int maxConcurrency;

            public SessionStartLimit(int total, int remaining, int resetAfter, int maxConcurrency) {
                this.total = total;
                this.remaining = remaining;
                this.resetAfter = resetAfter;
                this.maxConcurrency = maxConcurrency;
            }

            public int getMaxConcurrency() {
                return maxConcurrency;
            }

            public int getRemaining() {
                return remaining;
            }

            public int getResetAfter() {
                return resetAfter;
            }

            public int getTotal() {
                return total;
            }

            public static SessionStartLimit createObjectFromJSON(String json) {
                return JSON.parseObject(json, SessionStartLimit.class);
            }
        }
    }
}
/*
 * [1] Despite being available on the Discord API specification sheet, using
 * this call may raise incompatibility issues. Discord has a special protocol
 * for "sharding" (i.e. using multiple instances to distribute message load).
 * As a result, using this API may incur a higher technical cost -- both on
 * the programmer's side and Discord's side.
 *
 * [2] We might need to refactor this soon, but I'm not entirely sure if
 * we should use nested classes to define return objects from these JSON
 * serialization libraries. On one side, nobody will use GatewayMetadata
 * other than our internal client, so it's fine to use our own programming
 * conventions. On the other hand, this violates everything. Eh, it's whatever.
 */