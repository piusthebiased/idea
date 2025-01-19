package org.decagonlabs.discord.api.http.utils;

import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import org.decagonlabs.discord.api.http.utils.RequestArgs.RequestMethod;

public class RequestBuilder {
    private HttpRequest.Builder builder;
    private HeaderMap headers;
    private RequestArgs args;

    public RequestBuilder(RequestMethod method, String path) {
        builder = HttpRequest.newBuilder();
        args = new RequestArgs(method, path);
        headers = new HeaderMap();
    }

    public HttpRequest build() throws URISyntaxException {
        args.appendArgs(builder);
        headers.appendHeaders(builder);

        return builder.build();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addParams(String key, String value) {
        args.put(key, value);
    }
}
