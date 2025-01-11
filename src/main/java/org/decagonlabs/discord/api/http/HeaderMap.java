package org.decagonlabs.discord.api.http;

import java.net.http.HttpRequest;
import java.util.*;

// A data type that extends HashMap to add an "appendHeaders" function.
public class HeaderMap extends HashMap<String, String> {
    public HeaderMap() {}

    public HttpRequest.Builder appendHeaders(HttpRequest.Builder builder) {
        for(String key : this.keySet()) builder.header(key, this.get(key));
        return builder;
    }
}
