package eu.zickzenni.opencubes.client.session;

import java.util.UUID;

public class Session {
    private final String username;
    private final UUID uuid;

    public Session(String username, UUID uuid) {
        this.username = username;
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUUID() {
        return uuid;
    }
}