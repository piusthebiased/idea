package org.decagonlabs.discord.api.http;

import org.tinylog.Logger;

import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import org.decagonlabs.discord.api.http.RequestArgs.RequestMethod;

public class RequestBuilder {
    private HttpRequest.Builder builder;
    private HeaderMap headers;
    private RequestArgs args;

    public RequestBuilder(RequestMethod method, String path) {
        builder = HttpRequest.newBuilder();
        args = new RequestArgs(method, path);
        headers = new HeaderMap();
    }

    public HttpRequest.Builder build() throws URISyntaxException {
        args.appendArgs(builder);
        headers.appendHeaders(builder);

        return builder;
    }
}
