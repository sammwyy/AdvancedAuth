package dev._2lstudios.advancedauth.utils;

public class URI {

    private String protocol;
    private String username;
    private String password;
    private String host;
    private int port = -1;
    private String path;

    public URI() {
    }

    public URI setProtocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }

    public URI setUsername(final String username) {
        this.username = username;
        return this;
    }

    public URI setPassword(final String password) {
        this.password = password;
        return this;
    }

    public URI setHost(final String host) {
        this.host = host;
        return this;
    }

    public URI setPort(final int port) {
        this.port = port;
        return this;
    }

    public URI setPath(final String path) {
        this.path = path;
        return this;
    }

    public String toString() {
        String output = protocol + "://";

        if (username != null && !username.isEmpty()) {
            output += username;
            if (password != null && !password.isEmpty()) {
                output += ":" + password;
            }
            output += "@";
        }

        if (host != null && !host.isEmpty()) {
            output += host;
        }

        if (port >= 0) {
            output += ":" + port;
        }

        if (path != null && !path.isEmpty()) {
            output += "/" + path;
        }

        return output;
    }
}
