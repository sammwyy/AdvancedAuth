package dev._2lstudios.advancedauth.players;

import com.sammwy.classserializer.annotations.Prop;
import com.sammwy.milkshake.Entity;

public class AuthPlayerData extends Entity {
    // Information.
    @Prop
    public String email;
    @Prop
    public String displayName;
    @Prop
    public String lang;

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
