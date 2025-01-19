package org.decagonlabs.discord;

import org.tinylog.Logger;

public class ClientMetadata {
    private int version;
    private boolean doGlobalLogging;
    private Authorization auth;

    public ClientMetadata(Authorization auth, int version, boolean doGlobalLogging) {
        this.version = version;
        this.doGlobalLogging = doGlobalLogging;
        this.auth = auth;
    }

    // getters and setters
    public int getVersion() {
        return version;
    }

    public boolean isDoGlobalLogging() {
        return doGlobalLogging;
    }

    public Authorization getAuth() {
        return auth;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setDoGlobalLogging(boolean doGlobalLogging) {
        this.doGlobalLogging = doGlobalLogging;
    }

    public void setAuth(Authorization auth) {
        this.auth = auth;
    }
}
