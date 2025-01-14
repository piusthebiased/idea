package org.decagonlabs.discord.api.http;

import com.alibaba.fastjson.JSONObject;
import org.decagonlabs.discord.Authorization;
import org.tinylog.Logger;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.decagonlabs.discord.api.http.RequestArgs.RequestMethod;

import static org.decagonlabs.discord.Client.*;

public class Gateway {
    // Discord native Gateway fetch request [1]
    private static String getGateway() throws IOException, URISyntaxException, InterruptedException {
        final String path = "/gateway";
        HttpRequest req = new RequestBuilder(RequestMethod.GET, path).build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    // Discord Bot Native Gateway fetch, preferred fetch [2]
    public static String getGateway(Authorization auth) throws URISyntaxException, IOException, InterruptedException {
        final String path = "/gateway/bot";
        RequestBuilder builder = new RequestBuilder(RequestMethod.GET, path);
        auth.appendAuthorizationHeader(builder);

        HttpRequest req = builder.build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }
}
/*
 * [1] Despite being available on the Discord API specification sheet, using
 * this call may raise incompatibility issues. Discord has a special protocol
 * for "sharding" (i.e. using multiple instances to distribute message load).
 * As a result, using this API may incur a higher technical cost -- both on
 * the programmer's side and Discord's side.
 *
 * [2] This endpoint [...]
 */