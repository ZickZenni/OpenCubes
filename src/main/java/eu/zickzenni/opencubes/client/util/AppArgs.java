package eu.zickzenni.opencubes.client.util;

import com.beust.jcommander.Parameter;

public class AppArgs {
    @Parameter(names={"--username"})
    private String username;

    public String getUsername() {
        return username;
    }
}
