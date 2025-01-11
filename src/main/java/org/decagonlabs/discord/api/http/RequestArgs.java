package org.decagonlabs.discord.api.http;

import org.tinylog.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;

import static org.decagonlabs.discord.Client.BASE;

public class RequestArgs extends HashMap<String, String> {
    public RequestMethod method;
    public String path, body;

    public RequestArgs(RequestMethod method, String path) {
        this.method = method;
        this.path = path;
        this.body = null;
    }

    public RequestArgs(RequestMethod method, String path, String body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public HttpRequest.Builder appendArgs(HttpRequest.Builder builder) throws URISyntaxException {
        // build request URI
        URI requestPath = new URI(BASE + path);
        if(!this.isEmpty()) {
            StringBuilder path = new StringBuilder(this.path).append('?');
            for(String arg : this.keySet()) {
                path.append(arg).append('=').append(this.get(arg)).append('&');
            }
            path.setLength(path.length() - 1); // remove dangling '&'
            requestPath = new URI(BASE + path);
        }

        // configure builder for different methods
        builder.uri(requestPath);
        switch (method) { // handle method and body
            case RequestMethod.GET -> builder.GET();
            case RequestMethod.POST -> {
                HttpRequest.BodyPublisher body;
                if (this.body == null) body = HttpRequest.BodyPublishers.noBody();
                else body = HttpRequest.BodyPublishers.ofString(this.body);
                builder.POST(body);
            }
            case RequestMethod.DELETE -> builder.DELETE();
        }

        return builder;
    }

    // add REST types
    public enum RequestMethod {
        GET, POST, DELETE, PUT, PATCH;
    }
}