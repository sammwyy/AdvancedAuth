package dev._2lstudios.advancedauth.migration;

public interface IMigration {
    public String getPlugin();
    public String getEmailKey();
    public String getDisplayNameKey();
    public String getUsernameKey();
    public String getUUIDKey();
    public String getPasswordKey();
    public String getRegistrationIPKey();
    public String getRegistrationDateKey();
    public String getLastLoginIPKey();
    public String getLastLoginDateKey();
}
