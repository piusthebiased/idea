package org.decagonlabs.discord;

import org.tinylog.Logger;

public class ClientMetadata {
    private final int version;
    private final boolean doGlobalLogging;

    public ClientMetadata(int version, boolean doGlobalLogging) {
        this.version = version;
        this.doGlobalLogging = doGlobalLogging;
    }

    // getters and setters
    public int getVersion() {
        return version;
    }

    public boolean isDoGlobalLogging() {
        return doGlobalLogging;
    }
}
