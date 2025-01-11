package org.decagonlabs.discord;

import org.tinylog.Logger;

// currently uses bot auth flow
public class Authorization {
    private final String token;
    private final TokenType type;

    public Authorization(TokenType type, String token) {
        this.token = token;
        this.type = type;
    }

    // getters and setters
    public String getToken() {
        return token;
    }

    public TokenType getType() {
        return type;
    }

    // enumeration for TokenType
    public enum TokenType {
        Bot("Bot"),
        Bearer("Bearer");

        private final String type;
        TokenType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
