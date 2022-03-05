package dev._2lstudios.advancedauth.bukkit.migration.impl;

import dev._2lstudios.advancedauth.bukkit.migration.IMigration;

public class AuthMeMigration implements IMigration {
    @Override
    public String getPlugin() {
        return "authme";
    }

    @Override
    public String getEmailKey() {
        return "email";
    }

    @Override
    public String getDisplayNameKey() {
        return "realname";
    }

    @Override
    public String getUsernameKey() {
        return "username";
    }

    @Override
    public String getUUIDKey() {
        return null;
    }

    @Override
    public String getPasswordKey() {
        return "password";
    }

    @Override
    public String getRegistrationIPKey() {
        return "ip";
    }

    @Override
    public String getRegistrationDateKey() {
        return "registrationDate";
    }

    @Override
    public String getLastLoginIPKey() {
        return "regip";
    }

    @Override
    public String getLastLoginDateKey() {
        return "lastLogin";
    }
    
}
