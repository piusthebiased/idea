package org.decagonlabs.discord;

import org.tinylog.Logger;

import java.net.http.HttpClient;

public class Client {
    private int version;
    private Authorization token;


    public static final String BASE = "https://discord.com/api";
    public static HttpClient http = HttpClient.newHttpClient();

}
