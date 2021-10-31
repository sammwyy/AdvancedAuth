package dev._2lstudios.advancedauth.player;

import dev._2lstudios.mineorm.entity.Entity;
import dev._2lstudios.mineorm.entity.ID;
import dev._2lstudios.mineorm.entity.Prop;

public class AuthPlayerData extends Entity {
    @ID
    public String _id;

    @Prop
    public String email;

    @Prop
    public String username;

    @Prop
    public String uuid;

    @Prop
    public String password;

    @Prop
    public String registrationIP;

    @Prop
    public String lastLoginIP;
}
