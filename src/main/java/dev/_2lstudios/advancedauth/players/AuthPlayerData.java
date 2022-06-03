package dev._2lstudios.advancedauth.players;

import com.dotphin.classserializer.annotations.Prop;
import com.dotphin.milkshake.Entity;

public class AuthPlayerData extends Entity {
    // Information.
    @Prop
    public String email;
    @Prop
    public String displayName;

    // Authentication.
    @Prop
    public String username;
    @Prop
    public String uuid;
    @Prop
    public String password;

    // Settings.
    @Prop
    public boolean enabledSession = false;

    // Extra.
    @Prop
    public String registrationIP;

    @Prop
    public long registrationDate;

    @Prop
    public String lastLoginIP;

    @Prop
    public long lastLoginDate;
}
