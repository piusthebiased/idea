package org.decagonlabs.discord;

import org.decagonlabs.discord.api.http.utils.RequestBuilder;

// currently uses bot auth flow
public class Authorization {
    private final String token;
    private final TokenType type;

    public Authorization(TokenType type, String token) {
        this.token = token;
        this.type = type;
    }

    // get token as auth header
    public RequestBuilder appendAuthorizationHeader(RequestBuilder builder) {
        builder.addHeader("Authorization", getTokenString());

        return builder;
    }

    // getters and setters
    public String getToken() {
        return token;
    }

    public TokenType getType() {
        return type;
    }

    public String getTokenString() {
        return getType().getType() + " " + getToken();
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
