package dev._2lstudios.advancedauth.player;

import com.dotphin.milkshakeorm.entity.Entity;
import com.dotphin.milkshakeorm.entity.ID;
import com.dotphin.milkshakeorm.entity.Prop;

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
