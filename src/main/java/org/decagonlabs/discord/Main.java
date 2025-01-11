package org.decagonlabs.discord;

import java.net.MalformedURLException;
import java.net.URL;

import org.tinylog.Logger;

public class Main {
    public static URL base;
    public static final int version = 10;

    public static void main(String[] args) {
        try {
            base = new URL("https://discord.com/api");
        } catch (MalformedURLException e) {
            Logger.error(e);
        }


    }
}